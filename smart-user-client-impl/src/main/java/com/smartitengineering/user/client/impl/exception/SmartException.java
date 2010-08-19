/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.client.impl.exception;

/**
 *
 * @author russel
 */
public class SmartException extends RuntimeException {

    private int status;
    private ExceptionElement exceptionElement;

    public SmartException(ExceptionElement exceptionElement, int status,
            Throwable cause) {
        super(exceptionElement.getGroup(), cause);
        this.exceptionElement = exceptionElement;
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public ExceptionElement getExceptionElement() {
        return exceptionElement;
    }
}
