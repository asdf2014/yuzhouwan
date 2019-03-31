package com.yuzhouwan.hacker.design.pattern;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Copyright @ 2019 yuzhouwan.com
 * All right reserved.
 * Function: Exception Catch Tester
 *
 * @author Benedict Jin
 * @since 2016/9/1
 */
public class ExceptionCatchTest {

    private void r() {
        throw new RuntimeException("RuntimeException");
    }

    private void e() throws FileNotFoundException {
        r();
        throw new FileNotFoundException("FileNotFoundException");
    }

    // 受检异常后又捕捉非受捡异常会出现什么问题? fail to figure out..
    /*
    Catch 2: RuntimeException
    finally
     */
    @Test
    public void catchE() {
        String err = "";
        try {
            e();
        } catch (FileNotFoundException e) {
            err = "Catch 1: " + e.getMessage();
            System.out.println(err);
        } catch (RuntimeException e) {
            err = "Catch 2: " + e.getMessage();
            System.out.println(err);
        } finally {
            System.out.println("finally");
        }
        assertEquals("Catch 2: RuntimeException", err);
    }
}
