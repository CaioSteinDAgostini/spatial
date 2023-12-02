/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.learning.spatial;

import java.util.List;
import java.util.stream.IntStream;

/**
 *
 * @author caio
 */
public enum Orientation {
    
    CLOCKWISE,
    COUNTERCLOCKWISE;
    
    public static Orientation getOrientation2D(List<Point> points) {
            double sum = IntStream.range(0, points.size() ).mapToDouble((index) -> {
                Point p0 = points.get(index);
                Point p1 = points.get((index + 1) % points.size());

                return (p1.getX() - p0.getX()) * (p1.getY() + p0.getY());
            }).sum();
            if (sum < 0) {
                return Orientation.CLOCKWISE;
            } else {
                return Orientation.COUNTERCLOCKWISE;
            }
    }
 
    public static Orientation getOrientation2D(Point... points) {
            double sum = IntStream.range(0, points.length).mapToDouble((index) -> {
                Point p0 = points[index];
                Point p1 = points[(index + 1) % points.length];

                return (p1.getX() - p0.getX()) * (p1.getY() + p0.getY());
            }).sum();

            if (sum < 0) {
                return Orientation.CLOCKWISE;
            } else {
                return Orientation.COUNTERCLOCKWISE;
            }
    }
}
