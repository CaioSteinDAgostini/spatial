/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.learning.spatial;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author caio
 */
public class MinkowskiSum {

    /**
     *
     * @param mesh1
     * @param mesh2
     * @return
     */
    public static List<Point> calculate(Collection<Point> mesh1, Collection<Point> mesh2) {
        List<Point> answer = new LinkedList<>();
        
        mesh1.forEach((p1) -> {
            mesh2.forEach((p2) -> {
                answer.add(p1.subtract(p2));
            });
        });
        return answer;
    }
}
