package com.yuzhouwan.hacker.async;

import com.stumbleupon.async.Deferred;
import org.junit.Test;

import java.util.LinkedList;

import static org.junit.Assert.*;

/**
 * Copyright @ 2020 yuzhouwan.com
 * All right reserved.
 * Function：AsyncTest
 *
 * @author Benedict Jin
 * @since 2018/9/17
 */
public class AsyncTest {

    @Test
    public void testSimple() throws InterruptedException {
        final String msg = "success";
        final Deferred<Object> deferred = new Deferred<>();
        final boolean[] enter = {false};
        final Object[] result = {""};
        deferred.addCallback(cb -> {
            enter[0] = true;
            result[0] = cb;
            return cb;
        });
        new Thread(() -> deferred.callback(msg)).start();
        assertFalse(enter[0]);
        assertNotEquals(msg, result[0].toString());
        Thread.sleep(10);
        assertTrue(enter[0]);
        assertEquals(msg, result[0].toString());
    }

    @Test
    public void testErrorBack() throws InterruptedException {
        final String err = "runtime error";
        final Deferred<Object> deferred = new Deferred<>();
        final boolean[] enter = {false};
        final Object[] result = {new Exception()};
        deferred.addErrback(eb -> {
            enter[0] = true;
            result[0] = eb;
            return eb;
        });
        new Thread(() -> deferred.callback(new RuntimeException(err))).start();
        assertFalse(enter[0]);
        assertNull(((Exception) result[0]).getMessage());
        Thread.sleep(10);
        assertTrue(enter[0]);
        assertTrue(((Exception) result[0]).getMessage().endsWith(err));
    }

    @Test
    public void testGroup() throws Exception {
        LinkedList<Deferred<byte[]>> deferredList = new LinkedList<>();
        deferredList.add(buildDeferred("1"));
        deferredList.add(buildDeferred("2"));
        deferredList.add(buildDeferred("3"));
        Deferred.group(deferredList).joinUninterruptibly(10);
    }

    private Deferred<byte[]> buildDeferred(String s) {
        System.out.println(System.currentTimeMillis() + " : " + s);
        return Deferred.fromResult(s.getBytes());
    }
}
