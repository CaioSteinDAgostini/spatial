/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.learning.spatial;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author caio
 * @Deprecated
 */
public interface IConvexSurface extends IConvex {

    public boolean contains(Point p);
    
    /**
     *
     * @return
     */
    public default double area2D(){
        List<Point> points = new ArrayList<>(this.getPoints());
        return Utils.area2D(points);
    }
    
    /**
     *
     * @return
     */
    public List<Triangle> getTriangleList();
    
    public List<IConvexSurface> subtract(IConvexSurface modifier);
    
    public default boolean validateTopology2D(){
        return true;
    }
}
