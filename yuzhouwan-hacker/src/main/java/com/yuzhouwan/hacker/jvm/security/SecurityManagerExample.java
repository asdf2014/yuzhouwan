package com.yuzhouwan.hacker.jvm.security;

/**
 * Copyright @ 2018 yuzhouwan.com
 * All right reserved.
 * Function：SecurityManager Example
 *
 * @author Benedict Jin
 * @since 2018/2/7
 */
public class SecurityManagerExample extends SecurityManager {

    @Override
    public void checkExit(int status) {
        System.out.println("Exit " + status);
    }

    /*
    System securityManager: null
    System securityManager: com.yuzhouwan.hacker.jvm.security.SecurityManagerExample@cc34f4d
    Exit 0
     */
    public static void main(String[] args) {
        SecurityManagerExample sme = new SecurityManagerExample();
        System.out.println("System securityManager: " + System.getSecurityManager());
        System.setSecurityManager(sme);
        System.out.println("System securityManager: " + System.getSecurityManager());
        System.exit(0);
    }
}
