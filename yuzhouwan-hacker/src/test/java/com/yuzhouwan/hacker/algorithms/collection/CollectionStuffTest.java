package com.yuzhouwan.hacker.algorithms.collection;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

import static com.yuzhouwan.common.util.FileUtils.retryDelete;
import static org.junit.Assert.assertEquals;

/**
 * Copyright @ 2017 yuzhouwan.com
 * All right reserved.
 * Function：CollectionStuff Tester
 *
 * @author Benedict Jin
 * @since 2015/11/30
 */
public class CollectionStuffTest {

    private static final Logger _log = LoggerFactory.getLogger(CollectionStuffTest.class);

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
    public void testListReduplication() throws Exception {
        CollectionStuff<List<ComplexClass>, ComplexClass> collectionStuff = new CollectionStuff<>();
        Collection<ComplexClass> collection = collectionStuff.listReduplication(cs, 1, true);
        collection.forEach(System.out::println);
    }

    private class ComplexClass {
        private Integer i;
        private String s;
        private Object o;

        @Override
        public boolean equals(Object o1) {
            if (this == o1) return true;
            if (!(o1 instanceof ComplexClass)) return false;

            ComplexClass that = (ComplexClass) o1;

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

    @Test
    public void testFastFail() throws Exception {
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
    public void testByteBufferMap() throws Exception {

        byte[] bytesA = {1, 2};
        byte[] bytesB = {2, 3};
        byte[] bytesC = {2, 3};

        ByteBuffer byteBufferA = ByteBuffer.wrap(bytesA);
        ByteBuffer byteBufferB = ByteBuffer.wrap(bytesB);
        ByteBuffer byteBufferC = ByteBuffer.wrap(bytesC);

        HashMap<ByteBuffer, String> map = new HashMap<>();
        map.put(byteBufferA, "A");
        map.put(byteBufferB, "B");
        assertEquals(true, map.containsKey(byteBufferC));
    }

    @Test
    public void testBoxMap() throws Exception {

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
        assertEquals(null, map.get(1));
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
        assertEquals(null, map.keySet().toArray(new String[4])[3]);
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
        _log.info("[Collections.singleton] Spend Time: {}ns = {}ms, Mem: {}/{}/{} MB (diff/free/total)",
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
        _log.info("[Arrays.asList] Spend Time: {}ns = {}ms, Mem: {}/{}/{} MB (diff/free/total)",
                spendTime, spendTime / Math.pow(10, 6), totalMemory - freeMemory, freeMemory, totalMemory);
    }

    @Test
    public void testQueue() throws Exception {
        LinkedBlockingQueue<Byte> lbq = new LinkedBlockingQueue<>();
        lbq.add(Byte.valueOf("1"));
        lbq.add(Byte.valueOf("2"));
        lbq.add(Byte.valueOf("3"));
        assertEquals(true, lbq.peek() == 1);
        assertEquals(true, lbq.peek() == 1);
        assertEquals(true, lbq.peek() == 1);

        Byte[] bufferList = new Byte[lbq.size()];
        Byte[] lbqList = lbq.toArray(bufferList);
        assertEquals(true, Arrays.equals(bufferList, lbqList));
        assertEquals(true, bufferList == lbqList);

        File file = new File("queue.txt");
        try (FileOutputStream fileChannel = new FileOutputStream(file)) {
            byte[] bytes = new byte[3];
            bytes[0] = Byte.valueOf("1");
            bytes[1] = Byte.valueOf("2");
            bytes[2] = Byte.valueOf("3");
            fileChannel.write(bytes);
            fileChannel.flush();
            fileChannel.close();

            try (FileReader fr = new FileReader(file)) {
                char[] chars = new char[3];
                fr.read(chars);
                assertEquals(1, chars[0]);
                assertEquals(2, chars[1]);
                assertEquals(3, chars[2]);

                assertEquals(true, lbq.remove() == 1);
                assertEquals(true, lbq.remove() == 2);
                assertEquals(true, lbq.remove() == 3);
            }
        } finally {
            int count = 3;
            retryDelete(file, count);
        }
    }

}
