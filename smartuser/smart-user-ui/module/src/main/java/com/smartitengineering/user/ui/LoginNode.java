/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.ui;

/**
 *
 * @author modhu7
 */
public class LoginNode{

    private String username;
    private String password;

    
    LoginNode(String username,  String password){
        setUsername(username);
        setPassword(password);
    }

    public LoginNode() {
        setUsername("");
        setPassword("");
    }



    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


}
