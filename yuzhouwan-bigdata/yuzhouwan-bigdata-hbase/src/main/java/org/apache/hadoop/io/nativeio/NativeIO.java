/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding Copyright @ 2020 yuzhouwan.com
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.hadoop.io.nativeio;

import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.classification.InterfaceAudience;
import org.apache.hadoop.classification.InterfaceStability;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.CommonConfigurationKeys;
import org.apache.hadoop.fs.HardLink;
import org.apache.hadoop.io.SecureIOUtils.AlreadyExistsException;
import org.apache.hadoop.util.NativeCodeLoader;
import org.apache.hadoop.util.PerformanceAdvisory;
import org.apache.hadoop.util.Shell;
import sun.misc.Unsafe;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * JNI wrappers for various native IO-related calls not available in Java.
 * These functions should generally be used alongside a fallback to another
 * more portable mechanism.
 */
@InterfaceAudience.Private
@InterfaceStability.Unstable
public class NativeIO {
    private static final Log LOG = LogFactory.getLog(NativeIO.class);
    private static final Map<Long, CachedUid> uidCache =
            new ConcurrentHashMap<Long, CachedUid>();
    private static boolean workaroundNonThreadSafePasswdCalls = false;
    private static boolean nativeLoaded = false;
    private static long cacheTimeout;
    private static boolean initialized = false;

    static {
        if (NativeCodeLoader.isNativeCodeLoaded()) {
            try {
                initNative();
                nativeLoaded = true;
            } catch (Throwable t) {
                // This can happen if the user has an older version of libhadoop.so
                // installed - in this case we can continue without native IO
                // after warning
                PerformanceAdvisory.LOG.debug("Unable to initialize NativeIO libraries", t);
            }
        }
    }

    /**
     * Return true if the JNI-based native IO extensions are available.
     */
    public static boolean isAvailable() {
        return NativeCodeLoader.isNativeCodeLoaded() && nativeLoaded;
    }

    /**
     * Initialize the JNI method ID and class ID cache
     */
    private static native void initNative();

    /**
     * Get the maximum number of bytes that can be locked into memory at any
     * given point.
     *
     * @return 0 if no bytes can be locked into memory;
     * Long.MAX_VALUE if there is no limit;
     * The number of bytes that can be locked into memory otherwise.
     */
    static long getMemlockLimit() {
        return isAvailable() ? getMemlockLimit0() : 0;
    }

    private static native long getMemlockLimit0();

    /**
     * @return the operating system's page size.
     */
    static long getOperatingSystemPageSize() {
        try {
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            Unsafe unsafe = (Unsafe) f.get(null);
            return unsafe.pageSize();
        } catch (Throwable e) {
            LOG.warn("Unable to get operating system page size.  Guessing 4096.", e);
            return 4096;
        }
    }

    /**
     * The Windows logon name has two part, NetBIOS domain name and
     * user account name, of the format DOMAIN\UserName. This method
     * will remove the domain part of the full logon name.
     *
     * @param name full principal name containing the domain
     * @return name with domain removed
     */
    private static String stripDomain(String name) {
        int i = name.indexOf('\\');
        if (i != -1)
            name = name.substring(i + 1);
        return name;
    }

    public static String getOwner(FileDescriptor fd) {
        ensureInitialized();
        if (Shell.WINDOWS) {
            String owner = Windows.getOwner(fd);
            owner = stripDomain(owner);
            return owner;
        } else {
            long uid = POSIX.getUIDforFDOwnerforOwner(fd);
            CachedUid cUid = uidCache.get(uid);
            long now = System.currentTimeMillis();
            if (cUid != null && (cUid.timestamp + cacheTimeout) > now) {
                return cUid.username;
            }
            String user = POSIX.getUserName(uid);
            LOG.info("Got UserName " + user + " for UID " + uid
                    + " from the native implementation");
            cUid = new CachedUid(user, now);
            uidCache.put(uid, cUid);
            return user;
        }
    }

    /**
     * Create a FileInputStream that shares delete permission on the
     * file opened, i.e. other process can delete the file the
     * FileInputStream is reading. Only Windows implementation uses
     * the native interface.
     */
    public static FileInputStream getShareDeleteFileInputStream(File f)
            throws IOException {
        if (!Shell.WINDOWS) {
            // On Linux the default FileInputStream shares delete permission
            // on the file opened.
            //
            return new FileInputStream(f);
        } else {
            // Use Windows native interface to create a FileInputStream that
            // shares delete permission on the file opened.
            //
            FileDescriptor fd = Windows.createFile(
                    f.getAbsolutePath(),
                    Windows.GENERIC_READ,
                    Windows.FILE_SHARE_READ |
                            Windows.FILE_SHARE_WRITE |
                            Windows.FILE_SHARE_DELETE,
                    Windows.OPEN_EXISTING);
            return new FileInputStream(fd);
        }
    }

    /**
     * Create a FileInputStream that shares delete permission on the
     * file opened at a given offset, i.e. other process can delete
     * the file the FileInputStream is reading. Only Windows implementation
     * uses the native interface.
     */
    public static FileInputStream getShareDeleteFileInputStream(File f, long seekOffset)
            throws IOException {
        if (!Shell.WINDOWS) {
            RandomAccessFile rf = new RandomAccessFile(f, "r");
            if (seekOffset > 0) {
                rf.seek(seekOffset);
            }
            return new FileInputStream(rf.getFD());
        } else {
            // Use Windows native interface to create a FileInputStream that
            // shares delete permission on the file opened, and set it to the
            // given offset.
            //
            FileDescriptor fd = NativeIO.Windows.createFile(
                    f.getAbsolutePath(),
                    NativeIO.Windows.GENERIC_READ,
                    NativeIO.Windows.FILE_SHARE_READ |
                            NativeIO.Windows.FILE_SHARE_WRITE |
                            NativeIO.Windows.FILE_SHARE_DELETE,
                    NativeIO.Windows.OPEN_EXISTING);
            if (seekOffset > 0)
                NativeIO.Windows.setFilePointer(fd, seekOffset, NativeIO.Windows.FILE_BEGIN);
            return new FileInputStream(fd);
        }
    }

    /**
     * Create the specified File for write access, ensuring that it does not exist.
     *
     * @param f           the file that we want to create
     * @param permissions we want to have on the file (if security is enabled)
     * @throws AlreadyExistsException if the file already exists
     * @throws IOException            if any other error occurred
     */
    public static FileOutputStream getCreateForWriteFileOutputStream(File f, int permissions)
            throws IOException {
        if (!Shell.WINDOWS) {
            // Use the native wrapper around open(2)
            FileDescriptor fd = POSIX.open(f.getAbsolutePath(),
                    POSIX.O_WRONLY | POSIX.O_CREAT
                            | POSIX.O_EXCL, permissions);
            return new FileOutputStream(fd);
        } else {
            // Use the Windows native APIs to create equivalent FileOutputStream
            try {
                FileDescriptor fd = NativeIO.Windows.createFile(f.getCanonicalPath(),
                        NativeIO.Windows.GENERIC_WRITE,
                        NativeIO.Windows.FILE_SHARE_DELETE
                                | NativeIO.Windows.FILE_SHARE_READ
                                | NativeIO.Windows.FILE_SHARE_WRITE,
                        NativeIO.Windows.CREATE_NEW);
                NativeIO.POSIX.chmod(f.getCanonicalPath(), permissions);
                return new FileOutputStream(fd);
            } catch (NativeIOException nioe) {
                if (nioe.getErrorCode() == 80) {
                    // ERROR_FILE_EXISTS
                    // 80 (0x50)
                    // The file exists
                    throw new AlreadyExistsException(nioe);
                }
                throw nioe;
            }
        }
    }

    private synchronized static void ensureInitialized() {
        if (!initialized) {
            cacheTimeout =
                    new Configuration().getLong("hadoop.security.uid.cache.secs",
                            4 * 60 * 60) * 1000;
            LOG.info("Initialized cache for UID to User mapping with a cache" +
                    " timeout of " + cacheTimeout / 1000 + " seconds.");
            initialized = true;
        }
    }

    /**
     * A version of renameTo that throws a descriptive exception when it fails.
     *
     * @param src The source path
     * @param dst The destination path
     * @throws NativeIOException On failure.
     */
    public static void renameTo(File src, File dst)
            throws IOException {
        if (!nativeLoaded) {
            if (!src.renameTo(dst)) {
                throw new IOException("renameTo(src=" + src + ", dst=" +
                        dst + ") failed.");
            }
        } else {
            renameTo0(src.getAbsolutePath(), dst.getAbsolutePath());
        }
    }

    public static void link(File src, File dst) throws IOException {
        if (!nativeLoaded) {
            HardLink.createHardLink(src, dst);
        } else {
            link0(src.getAbsolutePath(), dst.getAbsolutePath());
        }
    }

    /**
     * A version of renameTo that throws a descriptive exception when it fails.
     *
     * @param src The source path
     * @param dst The destination path
     */
    private static native void renameTo0(String src, String dst)
    ;

    private static native void link0(String src, String dst)
            ;

    /**
     * Unbuffered file copy from src to dst without tainting OS buffer cache
     * <p>
     * In POSIX platform:
     * It uses FileChannel#transferTo() which internally attempts
     * unbuffered IO on OS with native sendfile64() support and falls back to
     * buffered IO otherwise.
     * <p>
     * It minimizes the number of FileChannel#transferTo call by passing the the
     * src file size directly instead of a smaller size as the 3rd parameter.
     * This saves the number of sendfile64() system call when native sendfile64()
     * is supported. In the two fall back cases where sendfile is not supported,
     * FileChannle#transferTo already has its own batching of size 8 MB and 8 KB,
     * respectively.
     * <p>
     * In Windows Platform:
     * It uses its own native wrapper of CopyFileEx with COPY_FILE_NO_BUFFERING
     * flag, which is supported on Windows Server 2008 and above.
     * <p>
     * Ideally, we should use FileChannel#transferTo() across both POSIX and Windows
     * platform. Unfortunately, the wrapper(Java_sun_nio_ch_FileChannelImpl_transferTo0)
     * used by FileChannel#transferTo for unbuffered IO is not implemented on Windows.
     * Based on OpenJDK 6/7/8 source code, Java_sun_nio_ch_FileChannelImpl_transferTo0
     * on Windows simply returns IOS_UNSUPPORTED.
     * <p>
     * Note: This simple native wrapper does minimal parameter checking before copy and
     * consistency check (e.g., size) after copy.
     * It is recommended to use wrapper function like
     * the Storage#nativeCopyFileUnbuffered() function in hadoop-hdfs with pre/post copy
     * checks.
     *
     * @param src The source path
     * @param dst The destination path
     * @throws IOException
     */
    public static void copyFileUnbuffered(File src, File dst) throws IOException {
        if (nativeLoaded && Shell.WINDOWS) {
            copyFileUnbuffered0(src.getAbsolutePath(), dst.getAbsolutePath());
        } else {
            try (FileInputStream fis = new FileInputStream(src);
                 FileOutputStream fos = new FileOutputStream(dst);
                 FileChannel input = fis.getChannel();
                 FileChannel output = fos.getChannel()) {
                long remaining = input.size();
                long position = 0;
                long transferred;
                while (remaining > 0) {
                    transferred = input.transferTo(position, remaining, output);
                    remaining -= transferred;
                    position += transferred;
                }
            }
        }
    }

    private static native void copyFileUnbuffered0(String src, String dst);

    public static class POSIX {
        // Flags for open() call from bits/fcntl.h
        public static final int O_RDONLY = 0;
        public static final int O_WRONLY = 1;
        public static final int O_RDWR = 2;
        public static final int O_CREAT = 100;
        public static final int O_EXCL = 200;
        public static final int O_NOCTTY = 400;
        public static final int O_TRUNC = 1000;
        public static final int O_APPEND = 2000;
        public static final int O_NONBLOCK = 4000;
        public static final int O_SYNC = 10000;
        public static final int O_ASYNC = 20000;
        public static final int O_FSYNC = O_SYNC;
        public static final int O_NDELAY = O_NONBLOCK;

        // Flags for posix_fadvise() from bits/fcntl.h
        /* No further special treatment.  */
        public static final int POSIX_FADV_NORMAL = 0;
        /* Expect random page references.  */
        public static final int POSIX_FADV_RANDOM = 1;
        /* Expect sequential page references.  */
        public static final int POSIX_FADV_SEQUENTIAL = 2;
        /* Will need these pages.  */
        public static final int POSIX_FADV_WILLNEED = 3;
        /* Don't need these pages.  */
        public static final int POSIX_FADV_DONTNEED = 4;
        /* Data will be accessed once.  */
        public static final int POSIX_FADV_NOREUSE = 5;


        /* Wait upon writeout of all pages
           in the range before performing the
           write.  */
        public static final int SYNC_FILE_RANGE_WAIT_BEFORE = 1;
        /* Initiate writeout of all those
           dirty pages in the range which are
           not presently under writeback.  */
        public static final int SYNC_FILE_RANGE_WRITE = 2;

        /* Wait upon writeout of all pages in
           the range after performing the
           write.  */
        public static final int SYNC_FILE_RANGE_WAIT_AFTER = 4;
        public final static int MMAP_PROT_READ = 0x1;
        public final static int MMAP_PROT_WRITE = 0x2;
        public final static int MMAP_PROT_EXEC = 0x4;
        static final String WORKAROUND_NON_THREADSAFE_CALLS_KEY =
                "hadoop.workaround.non.threadsafe.getpwuid";
        static final boolean WORKAROUND_NON_THREADSAFE_CALLS_DEFAULT = true;
        private static final Log LOG = LogFactory.getLog(NativeIO.class);
        private static final Map<Integer, CachedName> USER_ID_NAME_CACHE =
                new ConcurrentHashMap<Integer, CachedName>();
        private static final Map<Integer, CachedName> GROUP_ID_NAME_CACHE =
                new ConcurrentHashMap<Integer, CachedName>();
        private static boolean nativeLoaded = false;
        private static boolean fadvisePossible = true;
        private static boolean syncFileRangePossible = true;
        private static long cacheTimeout = -1;
        private static CacheManipulator cacheManipulator = new CacheManipulator();

        static {
            if (NativeCodeLoader.isNativeCodeLoaded()) {
                try {
                    Configuration conf = new Configuration();
                    workaroundNonThreadSafePasswdCalls = conf.getBoolean(
                            WORKAROUND_NON_THREADSAFE_CALLS_KEY,
                            WORKAROUND_NON_THREADSAFE_CALLS_DEFAULT);

                    initNative();
                    nativeLoaded = true;

                    cacheTimeout = conf.getLong(
                            CommonConfigurationKeys.HADOOP_SECURITY_UID_NAME_CACHE_TIMEOUT_KEY,
                            CommonConfigurationKeys.HADOOP_SECURITY_UID_NAME_CACHE_TIMEOUT_DEFAULT) *
                            1000;
                    LOG.debug("Initialized cache for IDs to User/Group mapping with a " +
                            " cache timeout of " + cacheTimeout / 1000 + " seconds.");

                } catch (Throwable t) {
                    // This can happen if the user has an older version of libhadoop.so
                    // installed - in this case we can continue without native IO
                    // after warning
                    PerformanceAdvisory.LOG.debug("Unable to initialize NativeIO libraries", t);
                }
            }
        }

        public static CacheManipulator getCacheManipulator() {
            return cacheManipulator;
        }

        public static void setCacheManipulator(CacheManipulator cacheManipulator) {
            POSIX.cacheManipulator = cacheManipulator;
        }

        /**
         * Return true if the JNI-based native IO extensions are available.
         */
        public static boolean isAvailable() {
            return NativeCodeLoader.isNativeCodeLoaded() && nativeLoaded;
        }

        private static void assertCodeLoaded() throws IOException {
            if (!isAvailable()) {
                throw new IOException("NativeIO was not loaded");
            }
        }

        /**
         * Wrapper around open(2)
         */
        public static native FileDescriptor open(String path, int flags, int mode);

        /**
         * Wrapper around fstat(2)
         */
        private static native Stat fstat(FileDescriptor fd);

        /**
         * Native chmod implementation. On UNIX, it is a wrapper around chmod(2)
         */
        private static native void chmodImpl(String path, int mode);

        public static void chmod(String path, int mode) {
            if (!Shell.WINDOWS) {
                chmodImpl(path, mode);
            } else {
                chmodImpl(path, mode);
            }
        }

        /**
         * Wrapper around posix_fadvise(2)
         */
        static native void posix_fadvise(
                FileDescriptor fd, long offset, long len, int flags);

        /**
         * Wrapper around sync_file_range(2)
         */
        static native void sync_file_range(
                FileDescriptor fd, long offset, long nbytes, int flags);

        /**
         * Call posix_fadvise on the given file descriptor. See the manpage
         * for this syscall for more information. On systems where this
         * call is not available, does nothing.
         */
        static void posixFadviseIfPossible(FileDescriptor fd, long offset, long len, int flags) {
            if (nativeLoaded && fadvisePossible) {
                try {
                    posix_fadvise(fd, offset, len, flags);
                } catch (UnsupportedOperationException uoe) {
                    fadvisePossible = false;
                } catch (UnsatisfiedLinkError ule) {
                    fadvisePossible = false;
                }
            }
        }

        /**
         * Call sync_file_range on the given file descriptor. See the manpage
         * for this syscall for more information. On systems where this
         * call is not available, does nothing.
         */
        public static void syncFileRangeIfPossible(
                FileDescriptor fd, long offset, long nbytes, int flags) {
            if (nativeLoaded && syncFileRangePossible) {
                try {
                    sync_file_range(fd, offset, nbytes, flags);
                } catch (UnsupportedOperationException uoe) {
                    syncFileRangePossible = false;
                } catch (UnsatisfiedLinkError ule) {
                    syncFileRangePossible = false;
                }
            }
        }

        static native void mlock_native(
                ByteBuffer buffer, long len);

        /**
         * Locks the provided direct ByteBuffer into memory, preventing it from
         * swapping out. After a buffer is locked, future accesses will not incur
         * a page fault.
         * <p>
         * See the mlock(2) man page for more information.
         *
         * @throws NativeIOException
         */
        static void mlock(ByteBuffer buffer, long len)
                throws IOException {
            assertCodeLoaded();
            if (!buffer.isDirect()) {
                throw new IOException("Cannot mlock a non-direct ByteBuffer");
            }
            mlock_native(buffer, len);
        }

        /**
         * Unmaps the block from memory. See munmap(2).
         * <p>
         * There isn't any portable way to unmap a memory region in Java.
         * So we use the sun.nio method here.
         * Note that unmapping a memory region could cause crashes if code
         * continues to reference the unmapped code.  However, if we don't
         * manually unmap the memory, we are dependent on the finalizer to
         * do it, and we have no idea when the finalizer will run.
         *
         * @param buffer The buffer to unmap.
         */
        public static void munmap(MappedByteBuffer buffer) {
            buffer.clear();
        }

        /**
         * Linux only methods used for getOwner() implementation
         */
        private static native long getUIDforFDOwnerforOwner(FileDescriptor fd);

        private static native String getUserName(long uid);

        /**
         * Returns the file stat for a file descriptor.
         *
         * @param fd file descriptor.
         * @return the file descriptor file stat.
         */
        public static Stat getFstat(FileDescriptor fd) {
            Stat stat;
            if (!Shell.WINDOWS) {
                stat = fstat(fd);
                stat.owner = getName(IdCache.USER, stat.ownerId);
                stat.group = getName(IdCache.GROUP, stat.groupId);
            } else {
                stat = fstat(fd);
            }
            return stat;
        }

        private static String getName(IdCache domain, int id) {
            Map<Integer, CachedName> idNameCache = (domain == IdCache.USER)
                    ? USER_ID_NAME_CACHE : GROUP_ID_NAME_CACHE;
            String name;
            CachedName cachedName = idNameCache.get(id);
            long now = System.currentTimeMillis();
            if (cachedName != null && (cachedName.timestamp + cacheTimeout) > now) {
                name = cachedName.name;
            } else {
                name = (domain == IdCache.USER) ? getUserName(id) : getGroupName(id);
                if (LOG.isDebugEnabled()) {
                    String type = (domain == IdCache.USER) ? "UserName" : "GroupName";
                    LOG.debug("Got " + type + " " + name + " for ID " + id +
                            " from the native implementation");
                }
                cachedName = new CachedName(name, now);
                idNameCache.put(id, cachedName);
            }
            return name;
        }

        static native String getUserName(int uid);

        static native String getGroupName(int uid);

        public static native long mmap(FileDescriptor fd, int prot,
                                       boolean shared, long length);

        public static native void munmap(long addr, long length);

        private enum IdCache {USER, GROUP}

        /**
         * Used to manipulate the operating system cache.
         */
        @VisibleForTesting
        public static class CacheManipulator {
            public void mlock(String identifier, ByteBuffer buffer,
                              long len) throws IOException {
                POSIX.mlock(buffer, len);
            }

            public long getMemlockLimit() {
                return NativeIO.getMemlockLimit();
            }

            public long getOperatingSystemPageSize() {
                return NativeIO.getOperatingSystemPageSize();
            }

            public void posixFadviseIfPossible(String identifier,
                                               FileDescriptor fd, long offset, long len, int flags) {
                NativeIO.POSIX.posixFadviseIfPossible(fd, offset,
                        len, flags);
            }

            public boolean verifyCanMlock() {
                return NativeIO.isAvailable();
            }
        }

        /**
         * A CacheManipulator used for testing which does not actually call mlock.
         * This allows many tests to be run even when the operating system does not
         * allow mlock, or only allows limited mlocking.
         */
        @VisibleForTesting
        public static class NoMlockCacheManipulator extends CacheManipulator {
            public void mlock(String identifier, ByteBuffer buffer,
                              long len) {
                LOG.info("mlocking " + identifier);
            }

            public long getMemlockLimit() {
                return 1125899906842624L;
            }

            public long getOperatingSystemPageSize() {
                return 4096;
            }

            public boolean verifyCanMlock() {
                return true;
            }
        }

        /**
         * Result type of the fstat call
         */
        public static class Stat {
            // Mode constants
            public static final int S_IFMT = 170000;      /* type of file */
            public static final int S_IFIFO = 10000;  /* named pipe (fifo) */
            public static final int S_IFCHR = 20000;  /* character special */
            public static final int S_IFDIR = 40000;  /* directory */
            public static final int S_IFBLK = 60000;  /* block special */
            public static final int S_IFREG = 100000;  /* regular */
            public static final int S_IFLNK = 120000;  /* symbolic link */
            public static final int S_IFSOCK = 140000;  /* socket */
            public static final int S_IFWHT = 160000;  /* whiteout */
            public static final int S_ISUID = 4000;  /* set user id on execution */
            public static final int S_ISGID = 2000;  /* set group id on execution */
            public static final int S_ISVTX = 1000;  /* save swapped text even after use */
            public static final int S_IRUSR = 400;  /* read permission, owner */
            public static final int S_IWUSR = 200;  /* write permission, owner */
            public static final int S_IXUSR = 100;  /* execute/search permission, owner */
            private int ownerId, groupId;
            private String owner, group;
            private int mode;

            Stat(int ownerId, int groupId, int mode) {
                this.ownerId = ownerId;
                this.groupId = groupId;
                this.mode = mode;
            }

            Stat(String owner, String group, int mode) {
                if (!Shell.WINDOWS) {
                    this.owner = owner;
                } else {
                    this.owner = stripDomain(owner);
                }
                if (!Shell.WINDOWS) {
                    this.group = group;
                } else {
                    this.group = stripDomain(group);
                }
                this.mode = mode;
            }

            @Override
            public String toString() {
                return "Stat(owner='" + owner + "', group='" + group + "'" +
                        ", mode=" + mode + ")";
            }

            public String getOwner() {
                return owner;
            }

            public String getGroup() {
                return group;
            }

            public int getMode() {
                return mode;
            }
        }

        private static class CachedName {
            final long timestamp;
            final String name;

            public CachedName(String name, long timestamp) {
                this.name = name;
                this.timestamp = timestamp;
            }
        }
    }

    public static class Windows {
        // Flags for CreateFile() call on Windows
        public static final long GENERIC_READ = 0x80000000L;
        public static final long GENERIC_WRITE = 0x40000000L;

        public static final long FILE_SHARE_READ = 0x00000001L;
        public static final long FILE_SHARE_WRITE = 0x00000002L;
        public static final long FILE_SHARE_DELETE = 0x00000004L;

        public static final long CREATE_NEW = 1;
        public static final long CREATE_ALWAYS = 2;
        public static final long OPEN_EXISTING = 3;
        public static final long OPEN_ALWAYS = 4;
        public static final long TRUNCATE_EXISTING = 5;

        public static final long FILE_BEGIN = 0;
        public static final long FILE_CURRENT = 1;
        public static final long FILE_END = 2;

        public static final long FILE_ATTRIBUTE_NORMAL = 0x00000080L;

        static {
            if (NativeCodeLoader.isNativeCodeLoaded()) {
                try {
                    initNative();
                    nativeLoaded = true;
                } catch (Throwable t) {
                    // This can happen if the user has an older version of libhadoop.so
                    // installed - in this case we can continue without native IO
                    // after warning
                    PerformanceAdvisory.LOG.debug("Unable to initialize NativeIO libraries", t);
                }
            }
        }

        /**
         * Create a directory with permissions set to the specified mode.  By setting
         * permissions at creation time, we avoid issues related to the user lacking
         * WRITE_DAC rights on subsequent chmod calls.  One example where this can
         * occur is writing to an SMB share where the user does not have Full Control
         * rights, and therefore WRITE_DAC is denied.
         *
         * @param path directory to create
         * @param mode permissions of new directory
         */
        public static void createDirectoryWithMode(File path, int mode) {
            createDirectoryWithMode0(path.getAbsolutePath(), mode);
        }

        /**
         * Wrapper around CreateDirectory() on Windows
         */
        private static native void createDirectoryWithMode0(String path, int mode)
        ;

        /**
         * Wrapper around CreateFile() on Windows
         */
        public static native FileDescriptor createFile(String path,
                                                       long desiredAccess, long shareMode, long creationDisposition)
        ;

        /**
         * Create a file for write with permissions set to the specified mode.  By
         * setting permissions at creation time, we avoid issues related to the user
         * lacking WRITE_DAC rights on subsequent chmod calls.  One example where
         * this can occur is writing to an SMB share where the user does not have
         * Full Control rights, and therefore WRITE_DAC is denied.
         * <p>
         * This method mimics the semantics implemented by the JDK in
         * {@link java.io.FileOutputStream}.  The file is opened for truncate or
         * append, the sharing mode allows other readers and writers, and paths
         * longer than MAX_PATH are supported.  (See io_util_md.c in the JDK.)
         *
         * @param path   file to create
         * @param append if true, then open file for append
         * @param mode   permissions of new directory
         * @return FileOutputStream of opened file
         */
        public static FileOutputStream createFileOutputStreamWithMode(File path,
                                                                      boolean append, int mode) {
            long desiredAccess = GENERIC_WRITE;
            long shareMode = FILE_SHARE_READ | FILE_SHARE_WRITE;
            long creationDisposition = append ? OPEN_ALWAYS : CREATE_ALWAYS;
            return new FileOutputStream(createFileWithMode0(path.getAbsolutePath(),
                    desiredAccess, shareMode, creationDisposition, mode));
        }

        /**
         * Wrapper around CreateFile() with security descriptor on Windows
         */
        private static native FileDescriptor createFileWithMode0(String path,
                                                                 long desiredAccess, long shareMode, long creationDisposition, int mode)
        ;

        /**
         * Wrapper around SetFilePointer() on Windows
         */
        public static native long setFilePointer(FileDescriptor fd,
                                                 long distanceToMove, long moveMethod);

        /**
         * Windows only methods used for getOwner() implementation
         */
        private static native String getOwner(FileDescriptor fd);

        /**
         * Windows only method used to check if the current process has requested
         * access rights on the given path.
         */
        private static native boolean access0(String path, int requestedAccess);

        /**
         * Checks whether the current process has desired access rights on
         * the given path.
         * <p>
         * Longer term this native function can be substituted with JDK7
         * function Files#isReadable, isWritable, isExecutable.
         *
         * @return true if access is allowed
         */
        public static boolean access() {
            /**
             * java.lang.UnsatisfiedLinkError: org.apache.hadoop.io.nativeio.NativeIO$Windows.access0(Ljava/lang/String;I)Z
             * Windows的唯一方法用于检查当前进程的请求，在给定的路径的访问权限 (先修改为 return true)
             *
             * 参考：http://blog.csdn.net/congcong68/article/details/42043093
             */
//      return access0(path, desiredAccess.accessRight());
            return true;
        }

        /**
         * Extends both the minimum and maximum working set size of the current
         * process.  This method gets the current minimum and maximum working set
         * size, adds the requested amount to each and then sets the minimum and
         * maximum working set size to the new values.  Controlling the working set
         * size of the process also controls the amount of memory it can lock.
         *
         * @param delta amount to increment minimum and maximum working set size
         * @see POSIX#mlock(ByteBuffer, long)
         */
        public static native void extendWorkingSetSize(long delta);

        /**
         * Supported list of Windows access right flags
         */
        public static enum AccessRight {
            ACCESS_READ(0x0001),      // FILE_READ_DATA
            ACCESS_WRITE(0x0002),     // FILE_WRITE_DATA
            ACCESS_EXECUTE(0x0020);   // FILE_EXECUTE

            private final int accessRight;

            AccessRight(int access) {
                accessRight = access;
            }

            public int accessRight() {
                return accessRight;
            }
        }
    }

    private static class CachedUid {
        final long timestamp;
        final String username;

        public CachedUid(String username, long timestamp) {
            this.timestamp = timestamp;
            this.username = username;
        }
    }
}
