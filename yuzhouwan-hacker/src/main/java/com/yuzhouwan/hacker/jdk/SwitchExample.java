//package com.yuzhouwan.hacker.jdk;
//
///**
// * Copyright @ 2023 yuzhouwan.com
// * All right reserved.
// * Function：Switch Example
// *
// * @author Benedict Jin
// * @since 2023/8/9
// */
//public class SwitchExample {
//
//    public static void main(String[] args) {
//        Object obj = "yuzhouwan";
//        String s = formatter(obj);
//        System.out.println(s);
//    }
//
//    static String formatter(Object obj) {
//        String formatted = "unknown";
//        if (obj instanceof Integer i) {
//            formatted = String.format("int %d", i);
//        } else if (obj instanceof Long l) {
//            formatted = String.format("long %d", l);
//        } else if (obj instanceof Double d) {
//            formatted = String.format("double %f", d);
//        } else if (obj instanceof String s) {
//            formatted = String.format("String %s", s);
//        }
//        return formatted;
//    }
//
//    static String formatterPatternSwitch(Object obj) {
//        return switch (obj) {
//            case Integer i -> String.format("int %d", i);
//            case Long l    -> String.format("long %d", l);
//            case Double d  -> String.format("double %f", d);
//            case String s  -> String.format("String %s", s);
//            default        -> obj.toString();
//        };
//    }
//}
