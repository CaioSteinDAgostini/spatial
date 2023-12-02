/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.learning.spatial;

import java.util.List;

/**
 *
 * @author caio
 */
public class Concave2DException extends InvalidGeometryException{
    
    public List<Point> points;

    public Concave2DException() {
    }

    public Concave2DException(List<Point> points) {
        this.points = points;
    }
    
}
