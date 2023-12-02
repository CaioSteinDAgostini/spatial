/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.learning.spatial;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 *
 * @author caio //based on http://vec3.ca/gjk/implementation/ and
 * http://allenchou.net/2013/12/game-physics-collision-detection-gjk/
 */
public class GJK {

    static public int MAX_IT_FOR_DETECTING_COLLISION = 10;

    //http://www.cs.sjsu.edu/faculty/pollett/masters/Semesters/Spring12/josh/GJK.html
    /**
     *
     * @param mesh1
     * @param mesh2
     * @param v
     * @return
     */
    public static Point support(List<Point> mesh1, List<Point> mesh2, Vector v) {
        // d is a vector direction (doesn'getT have to be normalized)
        // get points on the edge of the shapes in opposite directions
        Point p1 = furthestAlongAxis(v, mesh1);//getFarPoint(mesh1, v);

        Vector negativeVector = new Vector(v.getP0(), v.getP1());
        negativeVector.multiply(-1);
        Point p2 = furthestAlongAxis(negativeVector, mesh2);//getFarPoint(mesh2, negativeVector);

        if (p1 != null && p2 != null) {
            // perform the Minkowski Difference
            Point p3 = p1.subtract(p2);
            return p3;
        }
        return null;
    }

    /**
     *
     * @param p
     * @param q
     * @return
     */
    public static boolean collides(List<Point> p, List<Point> q) {
        return collides(p, q, false);
    }

    //http://www.dyn4j.org/2010/04/gjk-gilbert-johnson-keerthi/
    /**
     *
     * @param p
     * @param q
     * @param flattenZ
     * @return
     */
    public static boolean collides(List<Point> p, List<Point> q, boolean flattenZ) {

        List<List<Point>> pConvexes;
        if (p.size() > 3) {
            pConvexes = IConvex.breakIntoConvexes(p);
        } else {
            pConvexes = List.of(p);
        }
        List<List<Point>> qConvexes;
        if (q.size() > 3) {
            qConvexes = IConvex.breakIntoConvexes(q);
        } else {
            qConvexes = List.of(q);
        }

        return IntStream.range(0, pConvexes.size()).boxed().anyMatch((pIndex) -> {
            List<Point> pConvex = pConvexes.get(pIndex);
            return IntStream.range(0, qConvexes.size()).boxed().anyMatch((qIndex) -> {

                List<Point> qConvex = qConvexes.get(qIndex);
                return detectCollision(pConvex, qConvex, flattenZ);
            });
        });

//        return detectCollision(p, q, flattenZ);
    }

    private static boolean detectCollision(List<Point> p, List<Point> q, boolean flattenZ) {
        if (p.stream()
                .distinct()
                .filter(q::contains).findAny().isPresent()) {
            return true;
        }

        if (p.size() == 1 && q.size() > 1) {
            Point pPoint = p.get(0);
            boolean collides = IntStream.range(0, q.size()).boxed().anyMatch((index) -> {
                Point p0 = q.get(index);
                Point p1 = q.get((index + 1) % q.size());
                if (flattenZ) {
                    return pPoint.isInSegment2D(p0, p1);
                } else {
                    return pPoint.isInSegment(p0, p1);
                }

            });
            if (collides) {
                return true;
            }
        }

        if (q.size() == 1 && p.size() > 1) {
            Point qPoint = q.get(0);
            boolean collides = IntStream.range(0, p.size()).boxed().anyMatch((index) -> {
                Point p0 = p.get(index);
                Point p1 = p.get((index + 1) % p.size());
                if (flattenZ) {
                    return qPoint.isInSegment2D(p0, p1);
                } else {
                    return qPoint.isInSegment(p0, p1);
                }
            });
            if (collides) {
                return true;
            }
        }

        if (p.size() == 1 && q.size() == 1) {
            if (flattenZ) {
                return p.get(0).get2D().equals(q.get(0).get2D());
            } else {
                return p.get(0).equals(q.get(0));
            }
        }

        List<Point> newP;
        List<Point> newQ;

        if (flattenZ) {
            newP = p.stream().map(point -> point.get2D()).collect(Collectors.toList());
            newQ = q.stream().map(point -> point.get2D()).collect(Collectors.toList());
        } else {
            newP = p;
            newQ = q;
        }

        Vector d = new Vector(100, 0, 0);
        Simplex s = new Simplex();

// get the first Minkowski Difference point
        Point first = support(newP, newQ, d);
        s.add(first);
// negate d for the next point
        d.multiply(-1);
// start looping
        int count = MAX_IT_FOR_DETECTING_COLLISION;

        List<Point> previousSimplexPoints = List.of();
        while (count-- > 0) {
            // added a new point to the simplex because we haven't terminated yet
            Point newPoint = support(newP, newQ, d);
            s.add(newPoint);
            // make sure that the last point we added actually passed the origin
            Point last = s.getLast();
            if (last == null || last.dotProduct(d.size()) <= 0) {
                // if the point added last was not past the origin in the direction of d
                // then the Minkowski Sum cannot possibly contain the origin since
                // the last point added is on the edge of the Minkowski Difference
                return false;
            } else {
                // otherwise we need to determine if the origin is in
                // the current simplex
                if (s.containsOrigin(d)) {
                    // if it does then we know there is a collision
                    return true;
                } else {
                    if (s.v0.equals(s.v1)) {
                        return false;
                    } else {
                        d.set(s.v0, s.v1);
                        d.normalize();
                    }
                }
            }
        }
        return false;
    }

    static private Point furthestAlongAxis(Vector axis, List<Point> simplex) {
        double currentMax = -Double.MAX_VALUE;
        Point furthestPoint = null;
        for (Point pto : simplex) {
            double projection;

            projection = pto.dotProduct(axis.getX(), axis.getY(), axis.getZ()); //dot(point,axis);
            if (projection > currentMax) {
                currentMax = projection;
                furthestPoint = pto;
            }

        }
        return furthestPoint;
    }
}
