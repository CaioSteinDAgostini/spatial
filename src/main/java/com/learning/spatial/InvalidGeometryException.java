/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.learning.spatial;

/**
 *
 * @author caio
 */
public class InvalidGeometryException extends RuntimeException {

    /**
     * Creates a new instance of <code>InvalidGeometryException</code> without
     * detail message.
     */
    public InvalidGeometryException() {
    }

    /**
     * Constructs an instance of <code>InvalidGeometryException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public InvalidGeometryException(String msg) {
        super(msg);
    }

    /**
     *
     * @param point
     */
    public InvalidGeometryException(Point point) {
        super(point.toString());
    }
    
    public InvalidGeometryException(Throwable ex){
        super(ex);
    }
}
