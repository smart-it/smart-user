/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.rest.client.login;

/**
 *
 * @author modhu7
 */
public class LoginCenter {
    private static String username;
    private static String password;

    private LoginCenter() {
        throw new AssertionError();
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        LoginCenter.password = password;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        LoginCenter.username = username;
    }    
}
