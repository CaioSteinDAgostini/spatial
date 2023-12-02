/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.learning.spatial;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author caio
 */
public class Simplex {

    LinkedList<Point> simplex = new LinkedList<>();
    Point zero = Point.zero();
    Point v0 = Point.zero();
    Point v1 = Point.zero();

    /**
     *
     */
    public Simplex() {
        simplex.clear();
    }

    Simplex(Point b) {
        simplex.clear();
        simplex.push(b);
    }

    Simplex(Point b, Point c) {
        simplex.clear();
        simplex.push(b);
        simplex.push(c);
    }

    Simplex(Point b, Point c, Point d) {
        simplex.clear();
        simplex.push(b);
        simplex.push(c);
        simplex.push(d);
    }

    Simplex(Point b, Point c, Point d, Point a) {
        simplex.clear();
        simplex.push(b);
        simplex.push(c);
        simplex.push(d);
        simplex.push(a);
    }

    /**
     *
     * @param a
     * @return
     */
    public Simplex add(Point a) {
//        this.simplex.push(a);
        this.simplex.add(a);
        return this;
    }

    /**
     *
     * @return
     */
    public Point getLast() {
        return this.simplex.getLast();
    }

    /**
     *
     * @return
     */
    public Point getB() {
        return this.simplex.get(0);
    }

    /**
     *
     * @return
     */
    public Point getC() {
        return this.simplex.get(1);
    }

    /**
     *
     * @param direction
     * @return
     */
    public boolean containsOrigin(Vector direction) {
        v0.set(direction.getP0());
        v1.set(direction.getP1());
        // get the last point added to the simplex
        if (simplex.size() == 4) {

            Point a = this.getLast();
            Point b = simplex.get(0);
            Point c = simplex.get(1);
            Point d = simplex.get(2);
            // compute AO (same thing as -A)
            Point ao = a.multiply(-1);

            if (Utils.isPointInsideTetrahedron2(Point.zero(), simplex.get(0), simplex.get(1), simplex.get(2), simplex.get(3))) {
                return true;
            }

            Triangle closest = null;
            Point ab = b.subtract(a);
            Point ac = c.subtract(a);
            Point ad = d.subtract(a);

            boolean frontOfAbc = ab.crossProduct(ac).dotProduct(ao) > 0;
            boolean frontOfAcd = ac.crossProduct(ad).dotProduct(ao) > 0;
            boolean frontOfAdb = ad.crossProduct(ab).dotProduct(ao) > 0;

            if (frontOfAbc) {
                // origin is in front of triangle abc, simplex is already what it needs to be
                v1.set(ab.crossProduct(ac));
                closest = new Triangle(a, b, c);
            }

            if (frontOfAcd) {
                // origin is in front of triangle acd, simplex is set to this triangle
                v1.set(ac.crossProduct(ad));
                closest = new Triangle(a, c, d);

            }

            if (frontOfAdb) {
                //origin is in front of triangle adb, simplex is set to this triangle
                v1.set(ad.crossProduct(ab));
                closest = new Triangle(a, d, b);

            }

            if (closest == null) {
                return false;
            }
            simplex.clear();
            simplex.addAll(closest.getPoints());
            return false;
        }

        if (simplex.size() == 3) {

            Point a = this.getLast();
            Point b = simplex.get(0);
            Point c = simplex.get(1);
            // compute AO (same thing as -A)
            Point ao = a.multiply(-1);

            // then its the triangle case
            // get b and c
            // compute the edges
            Point ab = b.subtract(a);
            Point ac = c.subtract(a);
            // compute the normals
            Point abPerp = ac.crossProduct(ab).crossProduct(ab);//tripleProduct(ac, ab, ab);
            Point acPerp = ab.crossProduct(ac).crossProduct(ac);//tripleProduct(ab, ac, ac);
            // is the origin in R4
            if (abPerp.dotProduct(ao) > 0) {
                // remove point c
                simplex.remove(c);
                // set the new direction to abPerp
                v1.set(abPerp);
            } else {
                // is the origin in R3
                if (acPerp.dotProduct(ao) > 0) {

                    // remove point b
                    simplex.remove(b);
                    // set the new direction to acPerp
                    v1.set(acPerp);
                } else {
                    
                    if(ab.crossProduct(ac).dotProduct(ao)>0){
                        v1.set(ab.crossProduct(ac));
                        return false;
                    }
                    if(ab.crossProduct(ac).dotProduct(ao)<0){
                        simplex.clear();
                        simplex.add(c);
                        simplex.add(b);
                        simplex.add(a);
                        v1.set(ac.crossProduct(ab));
                        return false;
                    }
                    // otherwise we know its in R5 so we can return true
                    return true;
                }
            }
        }

        if (simplex.size() == 2) {

            Point a = simplex.get(1);
            Point b = simplex.get(0);
            // compute AO (same thing as -A)
            Point ao = a.multiply(-1);
            Point ab = b.subtract(a);

            if (zero.isInSegment(a, b)) {
                return true;
            }
            if (ao.dotProduct(ab) > 0) {

                // then its the line segment case
                // compute AB
                // get the perp to AB in the direction of the origin
                Point abPerp = ab.crossProduct(ao).crossProduct(ab);//tripleProduct(ab, ao, ab);
                // set the direction to abPerp
                v1.set(abPerp);
            } else {
                simplex.clear();
                simplex.add(a);
                v0.set(a);
                v1.set(zero);
            }
            return false;
        }
        return false;
    }

}
