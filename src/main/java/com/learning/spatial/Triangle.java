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
public class Triangle extends Convex implements IConvex {

    /**
     *
     * @param p0
     * @param p1
     * @param p2
     */
    public Triangle(Point p0, Point p1, Point p2) {
        super(p0, p1, p2);
    }

    @Override
    public int size() {
        return 3;
    }

    @Override
    public String toString() {
        return "Triangle<" + points + ">";
    }

}
