package com.yuzhouwan.hacker.io;

/**
 * Copyright @ 2020 yuzhouwan.com
 * All right reserved.
 * Function：TryWithResources
 *
 * @author Benedict Jin
 * @since 2020/11/11
 */
public class TryWithResources {

    /**
     * InnerStream created
     * ----------- dividing line -----------
     * InnerStream created
     * InnerStream released
     * Crash!
     * Crash!
     */
    public static void main(String[] args) {
        // Bad
        try (OuterStream outer = new OuterStream(new InnerStream(), new RuntimeException("Crash!"))) {
            System.out.println(outer);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        System.out.println("----------- dividing line -----------");

        // Good
        try (InnerStream inner = new InnerStream();
             OuterStream outer = new OuterStream(inner, new RuntimeException("Crash!"))) {
            System.out.println(outer);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}

class InnerStream implements AutoCloseable {

    public InnerStream() {
        System.out.println("InnerStream created");
    }

    public InnerStream(final RuntimeException e) {
        throw e;
    }

    @Override
    public void close() {
        System.out.println("InnerStream released");
    }

    @Override
    public String toString() {
        return "InnerStream";
    }
}

class OuterStream implements AutoCloseable {

    private final InnerStream inner;

    public OuterStream(final InnerStream inner) {
        System.out.println("OuterStream created");
        this.inner = inner;
    }

    public OuterStream(final InnerStream inner, final RuntimeException e) {
        this.inner = inner;
        throw e;
    }

    @Override
    public void close() {
        System.out.println("OuterStream released");
    }

    @Override
    public String toString() {
        return "OuterStream";
    }
}
