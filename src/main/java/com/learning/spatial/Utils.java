/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.learning.spatial;

import java.security.InvalidParameterException;
import java.util.List;

/**
 *
 * @author caio
 */
public class Utils {

    /**
     *
     * @param p1
     * @param p2
     * @return
     */
    public static boolean isSameDirection(Point p1, Point p2) {
        return p1.dotProduct(p2) > 0;
    }

    /**
     *
     * @param p
     * @param a
     * @param b
     * @param c
     * @param d
     * @return
     */
    public static boolean isPointInsideTetrahedron2(Point p, Point a, Point b, Point c, Point d) {

        Point intersection;
        intersection = linePlaneIntersection(a, b, c, new Vector(d, p));
        boolean b1 = false;
        if (intersection != null) {
            b1 = p.distance(d) <= intersection.distance(d) + Point.RESOLUTIO;
        }
        intersection = linePlaneIntersection(b, c, d, new Vector(a, p));

        boolean b2 = false;
        if (intersection != null) {
            b2 = p.distance(a) <= intersection.distance(a) + Point.RESOLUTIO;
        }
        intersection = linePlaneIntersection(c, d, a, new Vector(b, p));
        boolean b3 = false;
        if (intersection != null) {
            b3 = p.distance(b) <= intersection.distance(b) + Point.RESOLUTIO;
        }
        intersection = linePlaneIntersection(a, b, d, new Vector(c, p));
        boolean b4 = false;
        if (intersection != null) {
            b4 = p.distance(c) <= intersection.distance(c) + Point.RESOLUTIO;
        }

        return b1 && b2 && b3 && b4;
    }

    /**
     *
     * @param p
     * @param a
     * @param b
     * @param c
     * @param d
     * @return
     */
    public static boolean isPointInsideTetrahedron(Point p, Point a, Point b, Point c, Point d) {
        boolean answer;
        if (p.equals(a) || p.equals(b) || p.equals(c) || p.equals(d)) {
            answer = true;
        } else {
            Point intersection = Utils.linePlaneIntersection(b, c, d, new Vector(a, p));
            if (intersection == null) {
                return false;
            }
            answer = intersection.isOnTriangle(b, c, d);
//       
//                    b
//                p---i------a  X  
//                    c
//        
//                    b
//                    i--p---a  OK
//                    c
//        
//                    b
//                    i------a--p  X
//                    c
        }
        return answer;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @return
     */
    public static Point calculateCentroid(Point a, Point b, Point c) {
        Point answer = new Point(a.getX() + b.getX() + c.getX(), a.getY() + b.getY() + c.getY(), a.getZ() + b.getZ() + c.getZ());
        return answer.multiply(1 / 3f);

    }

    /**
     *
     * @param points
     * @return
     */
    public static Point calculateCentroid(List<Point> points) {
        if(points.isEmpty()){
            throw new InvalidParameterException();
        }
        double x = 0;
        double y = 0;
        double z = 0;

        for (Point p : points) {
            x += p.getX();
            y += p.getY();
            z += p.getZ();
        }

        return new Point(x / points.size(), y / points.size(), z / points.size());
    }

    /**
     *
     * @param points
     * @return
     */
    public static Point calculateCentroid(Point... points) {
        double x = 0;
        double y = 0;
        double z = 0;

        for (Point p : points) {
            x += p.getX();
            y += p.getY();
            z += p.getZ();
        }

        return new Point(x / points.length, y / points.length, z / points.length);
    }

    /**
     *
     * @param points
     * @return
     */
    public static double area2D(List<Point> points) {

        double area = 0; // Accumulates area in the loop
        int j = points.size() - 1;

        for (int i = 0; i < points.size(); i++) {
            area += (points.get(j).getX() + points.get(i).getX()) * -(points.get(j).getZ() - points.get(i).getZ());
            j = i; //j is previous vertex to i
        }
        return area / 2;
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param line
     * @return
     */
    public static Point linePlaneIntersection(Point a, Point b, Point c, Vector line) {
//        ray: p = p0 + tV
//        plane: p.N+d=0   /// N=plane normal
        if (line.getP0().isCoplanar(a, b, c)) {
            return line.getP0();
        }
        if (line.getP1().isCoplanar(a, b, c)) {
            return line.getP1();
        }
        if (a.equals(b)) {// || a.equals(c) || b.equals(c)){
            return line.lineIntersection(new Vector(b, c));
//            return null;
        }
        if (a.equals(c)) {
            return line.lineIntersection(new Vector(a, b));
        }
        if (b.equals(c)) {
            return line.lineIntersection(new Vector(a, b));
        }

        Vector ab = new Vector(a, b);
//        ab.normalize();
        Vector ac = new Vector(a, c);
//        ac.normalize();
        Point cross = ab.size().crossProduct(ac.size());
        double d = -(a.getX() * cross.getX() + a.getY() * cross.getY() + a.getZ() * cross.getZ());

        Vector normal = ab.crossProduct(ac);
        if (normal == null) {
            return null;
        }
        double den = line.dotProduct(normal);
        if (den != 0) {
            double t = -(line.getP0().dotProduct(normal.size()) + d) / den;

            Point p;
            Vector length = new Vector(line);
            if (t != 0) {
                length.multiply(t);
                p = line.getP0().add(length.size());
            } else {
                p = line.getP0();
            }
            return p;
        } else {
            return null;
        }

    }

    /**
     *
     * @param surface
     * @param pointToProject
     * @return
     */
    static public Point projectPoint(IConvexSurface surface, Point pointToProject) {

        List<Triangle> triangles = surface.getTriangleList();

        for (Triangle triangle : triangles) {
            if (pointToProject.isOnTriangle2D(triangle)) {
                return Utils.linePlaneIntersection(triangle.get(0), triangle.get(1), triangle.get(2), new Vector(pointToProject, pointToProject.subtract(0, 10, 0)));
            }
        }
        return null;
    }

}
