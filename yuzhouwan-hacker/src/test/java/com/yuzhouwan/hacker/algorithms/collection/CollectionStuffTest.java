package com.yuzhouwan.hacker.algorithms.collection;

import com.google.common.collect.Lists;
import com.yuzhouwan.common.util.ThreadUtils;
import org.junit.Before;
import org.junit.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;

import static com.yuzhouwan.common.util.FileUtils.retryDelete;
import static org.junit.Assert.*;

/**
 * Copyright @ 2023 yuzhouwan.com
 * All right reserved.
 * Function：CollectionStuff Tester
 *
 * @author Benedict Jin
 * @since 2015/11/30
 */
public class CollectionStuffTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(CollectionStuffTest.class);

    private final Vector<Long> v = new Vector<>();
    private List<ComplexClass> cs;

    @Before
    public void init() {
        ComplexClass c0 = new ComplexClass();
        c0.setI(1);
        c0.setS("1");
        c0.setO(new CollectionStuffTest());

        ComplexClass c = new ComplexClass();
        c.setI(1);
        c.setS("1");
        c.setO(new CollectionStuffTest());

        cs = new ArrayList<>();
        cs.add(c0);
        cs.add(c);
    }

    /**
     * Method: listReduplication(C c, int fieldIndex, boolean isDeclared)
     */
    @Test
    public void testListReduplication() {
        CollectionStuff<List<ComplexClass>, ComplexClass> collectionStuff = new CollectionStuff<>();
        Collection<ComplexClass> collection = collectionStuff.listReduplication(cs, 1, true);
        collection.forEach(System.out::println);
    }

    @Test
    public void testFastFail() {
        LinkedList<Integer> list = new LinkedList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        while (list.size() > 0) {
            System.out.println(list.removeFirst());
        }
        assertEquals(0, list.size());
    }

    @Test
    public void testByteBufferMap() {

        byte[] bytesA = {1, 2};
        byte[] bytesB = {2, 3};
        byte[] bytesC = {2, 3};

        ByteBuffer byteBufferA = ByteBuffer.wrap(bytesA);
        ByteBuffer byteBufferB = ByteBuffer.wrap(bytesB);
        ByteBuffer byteBufferC = ByteBuffer.wrap(bytesC);

        HashMap<ByteBuffer, String> map = new HashMap<>();
        map.put(byteBufferA, "A");
        map.put(byteBufferB, "B");
        assertTrue(map.containsKey(byteBufferC));

        HashMap<byte[], String> mapBytes = new HashMap<>();
        mapBytes.put(bytesA, "A");
        mapBytes.put(bytesB, "B");
        assertTrue(mapBytes.containsKey(bytesA));
        assertTrue(mapBytes.containsKey(bytesB));
        assertFalse(mapBytes.containsKey(bytesC));
    }

    @Test
    public void testBoxMap() {

        HashMap<Long, String> map = new HashMap<>();
        map.put(1L, "1L");
        map.put(2L, "2L");
        map.put(3L, "3L");
        map.put(4L, "4L");

        long key = 2;
        class A {
            public long a;
        }
        A a = new A();
        a.a = 4;
        assertNull(map.get(1));
        assertEquals("2L", map.get(key));
        assertEquals("3L", map.get(3L));
        assertEquals("4L", map.get(a.a));
    }

    @Test
    public void testMap2Array() {
        HashMap<String, Integer> map = new HashMap<>(3);
        map.put("a", 1);
        map.put("b", 2);
        map.put("c", 3);
        assertEquals("c", map.keySet().toArray(new String[0])[2]);
        assertEquals("c", map.keySet().toArray(new String[3])[2]);
        assertNull(map.keySet().toArray(new String[4])[3]);
        assertEquals("c", map.keySet().toArray()[2]);
    }

    // -ea -Xmx512M -Xms512M -Xmn256M -XX:+AlwaysPreTouch
    @Test
    public void testSingletonListPerformance() {
        String site = "yuzhouwan.com";
        int count = 1_0000_0000;
        long spendTime = 0;
        while (count > 0) {
            long startTime = System.nanoTime();
            Collections.singleton(site);
            long endTime = System.nanoTime();
            count--;
            spendTime += endTime - startTime;
        }
        Runtime runtime = Runtime.getRuntime();
        long totalMemory = runtime.totalMemory() / 1024 / 1024;
        long freeMemory = runtime.freeMemory() / 1024 / 1024;
        // [Collections.singleton] Spend Time: 1370786852ns = 1370.786852ms, Mem: 39/441/480 MB (diff/free/total)
        LOGGER.info("[Collections.singleton] Spend Time: {}ns = {}ms, Mem: {}/{}/{} MB (diff/free/total)",
            spendTime, spendTime / Math.pow(10, 6), totalMemory - freeMemory, freeMemory, totalMemory);
    }

    // -ea -Xmx512M -Xms512M -Xmn256M -XX:+AlwaysPreTouch
    @Test
    public void testArraysAsListPerformance() {
        String site = "yuzhouwan.com";
        int count = 1_0000_0000;
        long spendTime = 0;
        while (count > 0) {
            long startTime = System.nanoTime();
            Arrays.asList(site);
            long endTime = System.nanoTime();
            count--;
            spendTime += endTime - startTime;
        }
        Runtime runtime = Runtime.getRuntime();
        long totalMemory = runtime.totalMemory() / 1024 / 1024;
        long freeMemory = runtime.freeMemory() / 1024 / 1024;
        // [Arrays.asList] Spend Time: 1549508768ns = 1549.508768ms, Mem: 195/312/507 MB (diff/free/total)
        LOGGER.info("[Arrays.asList] Spend Time: {}ns = {}ms, Mem: {}/{}/{} MB (diff/free/total)",
            spendTime, spendTime / Math.pow(10, 6), totalMemory - freeMemory, freeMemory, totalMemory);
    }

    @Test
    public void testQueue() throws Exception {
        LinkedBlockingQueue<Byte> lbq = new LinkedBlockingQueue<>();
        lbq.add(Byte.valueOf("1"));
        lbq.add(Byte.valueOf("2"));
        lbq.add(Byte.valueOf("3"));
        assertEquals(1, (byte) lbq.peek());
        assertEquals(1, (byte) lbq.peek());
        assertEquals(1, (byte) lbq.peek());

        Byte[] bufferList = new Byte[lbq.size()];
        Byte[] lbqList = lbq.toArray(bufferList);
        assertArrayEquals(bufferList, lbqList);
        assertSame(bufferList, lbqList);

        File file = new File("queue.txt");
        try (FileOutputStream fileChannel = new FileOutputStream(file)) {
            byte[] bytes = new byte[3];
            bytes[0] = Byte.parseByte("1");
            bytes[1] = Byte.parseByte("2");
            bytes[2] = Byte.parseByte("3");
            fileChannel.write(bytes);
            fileChannel.flush();
            fileChannel.close();

            try (FileReader fr = new FileReader(file)) {
                char[] chars = new char[3];
                assertEquals(3, fr.read(chars));
                assertEquals(1, chars[0]);
                assertEquals(2, chars[1]);
                assertEquals(3, chars[2]);

                assertEquals(1, (byte) lbq.remove());
                assertEquals(2, (byte) lbq.remove());
                assertEquals(3, (byte) lbq.remove());
            }
        } finally {
            retryDelete(file, 3);
        }
    }

    @Test
    public void testVector() throws Exception {
        long count = 0, size = 100, thread = 10;
        ExecutorService es = ThreadUtils.buildExecutorService("testVector");
        List<Long> list = new LinkedList<>();
        long timeSync = 0, time = 0;
        while (count < 1_0000) {
            list.clear();
            for (long i = 0; i < size; i++) v.add(i);
            for (int i = 0; i < thread; i++) {
                es.submit(() -> {
                    v.add(size);
                    v.remove(size);
                });
            }
            long start, end;
            long startSync = System.nanoTime();
            synchronized (v) {
                start = System.nanoTime();
                list.addAll(v);
                v.clear();
                end = System.nanoTime();
            }
            long endSync = System.nanoTime();
            timeSync += (endSync - startSync);
            time += (end - start);
            count++;
        }
        // Sync Time: 17.851833 ms, Time: 16.120418 ms, Subtract: 1.731415 ms, Count: 10000, Size: 100, Thread: 10
        LOGGER.info("Sync Time: {} ms, Time: {} ms, Subtract: {} ms, Count: {}, Size: {}, Thread: {}",
            timeSync / Math.pow(10, 6), time / Math.pow(10, 6), (timeSync - time) / Math.pow(10, 6),
            count, size, thread);
        es.shutdown();
        while (!es.isTerminated()) Thread.sleep(10);
        assertEquals(0, v.size());
        assertEquals(size, list.size());
    }

    @Test
    @SuppressWarnings("all")
    public void testSingletonList() {
        boolean failed = false;
        try {
            List<Boolean[]> booleans = Arrays.asList(
                (Boolean[]) Collections.singletonList(Boolean.FALSE).toArray(),
                (Boolean[]) Collections.singletonList(Boolean.TRUE).toArray()
            );
            booleans.forEach(System.out::print);
        } catch (Exception e) {
            failed = true;
            assertEquals("ClassCastException", e.getClass().getSimpleName());
        }
        assertTrue(failed);

        failed = false;
        try {
            List<Boolean[]> booleans = Arrays.asList(
                (Boolean[]) Arrays.asList(Boolean.FALSE).toArray(),
                (Boolean[]) Arrays.asList(Boolean.TRUE).toArray()
            );
            booleans.forEach(System.out::print);
        } catch (Exception e) {
            failed = true;
            assertEquals("ClassCastException", e.getClass().getSimpleName());
        }
        assertTrue(failed);
    }

    @Test
    public void testCME() {
        {
            Properties p = new Properties();

            p.put("1", "1");
            p.put("2", "2");
            p.put("3", "3");
            p.put("4", "4");
            p.put("5", "5");

            boolean hasCME = false;
            try {
                p.forEach((k, v) -> {
                    if ("3".equals(k)) p.clear();
                });
            } catch (ConcurrentModificationException cme) {
                hasCME = true;
            }
            assertFalse(hasCME);
        }
        {
            ConcurrentHashMap<String, Object> map = new ConcurrentHashMap<>();

            map.put("1", "1");
            map.put("2", "2");
            map.put("3", "3");
            map.put("4", "4");
            map.put("5", "5");

            boolean hasCME = false;
            try {
                map.forEach((k, v) -> {
                    if ("3".equals(k)) map.remove(k);
                });
            } catch (ConcurrentModificationException cme) {
                hasCME = true;
            }
            assertFalse(hasCME);
        }
    }

    @Test
    @SuppressWarnings("StatementWithEmptyBody")
    public void testRemoveAllOfNullsFromList() {
        final List<String> list = Lists.newArrayList(null, "yuzhouwan", null, null, "com", null);
        while (list.remove(null)) ;
        assertEquals(2, list.size());
        assertEquals("yuzhouwan", list.get(0));
        assertEquals("com", list.get(1));
    }

    @Test
    public void testDescendingTreeSet() {
        // ascending
        final TreeSet<Integer> ids = new TreeSet<>();
        ids.add(1);
        ids.add(3);
        ids.add(2);
        assertEquals(1, ids.first().intValue());
        assertEquals(3, ids.last().intValue());
        // descending
        final NavigableSet<Integer> descendingIds = ids.descendingSet();
        final Iterator<Integer> iter = descendingIds.iterator();
        assertEquals(3, iter.next().intValue());
        assertEquals(2, iter.next().intValue());
        assertEquals(1, iter.next().intValue());
    }

    private static class ComplexClass {
        private Integer i;
        private String s;
        private Object o;

        @Override
        public boolean equals(Object o1) {
            if (this == o1) return true;
            if (!(o1 instanceof ComplexClass that)) return false;

            if (!getI().equals(that.getI())) return false;
            if (!getS().equals(that.getS())) return false;
            return getO().equals(that.getO());

        }

        @Override
        public int hashCode() {
            int result = getI().hashCode();
            result = 31 * result + getS().hashCode();
            result = 31 * result + getO().hashCode();
            return result;
        }

        public Integer getI() {
            return i;
        }

        public void setI(Integer i) {
            this.i = i;
        }

        public String getS() {
            return s;
        }

        public void setS(String s) {
            this.s = s;
        }

        public Object getO() {
            return o;
        }

        public void setO(Object o) {
            this.o = o;
        }
    }
}
