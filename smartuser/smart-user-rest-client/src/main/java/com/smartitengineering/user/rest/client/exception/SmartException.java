/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.rest.client.exception;

/**
 *
 * @author modhu7
 */
public class SmartException extends RuntimeException {
    
    private int status;
    
    public SmartException(String message, int status, Throwable cause) {
        super(message, cause);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
    
}
