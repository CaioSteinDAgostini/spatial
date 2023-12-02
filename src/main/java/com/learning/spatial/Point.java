/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.learning.spatial;

import java.io.Serializable;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 *
 * @author caio
 */
public class Point implements Serializable {

    /**
     *
     */
    static public double RESOLUTIO = 0.0001;

    /**
     *
     * @return
     */
    
    public static Point zero() {
        return new Point(0, 0, 0);
    }

    public static Point Point(Double x, Double y, Double z) {
        return new Point(x, y, z);
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @return
     */
    public static Point calculateCentroid(Point a, Point b, Point c) {
        Point answer = new Point(a.x + b.x + c.x, a.y + b.y + c.y, a.z + b.z + c.z);
        return answer.multiply(1 / 3f);
    }

    /**
     *
     * @param p1
     * @param p2
     * @return
     */
    public static boolean isSameDirection(Point p1, Point p2) {
        return p1.dotProduct(p2) > 0;
    }

    private double x;
    private double y;
    private double z;

    /**
     *
     * @param x
     * @param y
     * @param z
     */
    public Point(double x, double y, double z) {
        if (Double.isNaN(x) || Double.isNaN(y) || Double.isNaN(z)) {
            System.err.println("x " + x + " y " + y + " z " + z);
            throw new InvalidParameterException();
        }
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     *
     * @param x
     * @param y
     * @param z
     */
    public Point(int x, int y, int z) {
        if (Double.isNaN(x) || Double.isNaN(y) || Double.isNaN(z)) {
            throw new InvalidParameterException();
        }
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     *
     * @param otherPoint
     */
    public Point(Point otherPoint) {
        this.x = otherPoint.x;
        this.y = otherPoint.y;
        this.z = otherPoint.z;
    }

    /**
     *
     * @param otherPoint
     * @return
     */
    public Point set(Point otherPoint) {
        this.x = otherPoint.x;
        this.y = otherPoint.y;
        this.z = otherPoint.z;
        return this;
    }

    /**
     *
     * @param x
     * @param y
     * @param z
     * @return
     */
    public Point set(double x, double y, double z) {
        if (Double.isNaN(x) || Double.isNaN(y) || Double.isNaN(z)) {
            throw new InvalidParameterException();
        }
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    /**
     *
     * @param x
     * @return
     */
    public Point setX(double x) {
        if (Double.isNaN(x)) {
            throw new InvalidParameterException();
        }
        this.x = x;
        return this;
    }

    /**
     *
     * @param y
     * @return
     */
    public Point setY(double y) {
        if (Double.isNaN(y)) {
            throw new InvalidParameterException();
        }
        this.y = y;
        return this;
    }

    /**
     *
     * @param z
     * @return
     */
    public Point setZ(double z) {
        if (Double.isNaN(z)) {
            throw new InvalidParameterException();
        }
        this.z = z;
        return this;
    }

    /**
     *
     * @param point
     * @return
     */
    public double distance(Point point) {
        double dx = this.x - point.x;
        double dy = this.y - point.y;
        double dz = this.z - point.z;

        return (Math.round(Math.sqrt(dx * dx + dy * dy + dz * dz) / RESOLUTIO)* RESOLUTIO) ;
    }

    /**
     *
     * @param point
     * @return
     */
    public double distance2D(Point point) {
        double dx = this.x - point.x;
        double dy = this.y - point.y;

        return (Math.round(Math.sqrt(dx * dx + dy * dy) / RESOLUTIO)* RESOLUTIO) ;
    }

    /**
     *
     * @param dx
     * @param dy
     * @param dz
     * @return
     */
    public Point add(double dx, double dy, double dz) {
        return new Point(x + dx, y + dy, z + dz);
    }

    /**
     *
     * @param dx
     * @param dy
     * @param dz
     * @return
     */
    public void added(double dx, double dy, double dz) {
        this.x = this.x + dx;
        this.y = this.y + dy;
        this.z = this.z + dz;
    }

    /**
     *
     * @param p
     * @return
     */
    public Point add(Point p) {
        return this.add(p.x, p.y, p.z);
    }

    public void added(Point p) {
        this.x = x + p.x;
        this.y = y + p.y;
        this.z = z + p.z;
    }

    /**
     *
     * @param dx
     * @param dy
     * @param dz
     * @return
     */
    public Point subtract(double dx, double dy, double dz) {
        return new Point(x - dx, y - dy, z - dz);
    }

    /**
     *
     * @param dx
     * @param dy
     * @param dz
     * @return
     */
    public void subtracted(double dx, double dy, double dz) {
        this.x = this.x - dx;
        this.y = this.y - dy;
        this.z = this.z - dz;
    }

    /**
     *
     * @param p
     * @return
     */
    public Point subtract(Point p) {
        return this.subtract(p.x, p.y, p.z);
    }

    public void subtracted(Point p) {
        this.x = x - p.x;
        this.y = y - p.y;
        this.z = z - p.z;
    }

    /**
     *
     * @param value
     * @return
     */
    public Point multiply(double value) {
        return new Point(x * value, y * value, z * value);
    }

    public Point multiply(double valueX, double valueY, double valueZ) {
        return new Point(x * valueX, y * valueY, z * valueZ);
    }

    public void multiplied(double value) {
        this.x = x * value;
        this.y = y * value;
        this.z = z * value;
    }

    public void multiplied(double valueX, double valueY, double valueZ) {
        this.x = x * valueX;
        this.y = y * valueY;
        this.z = z * valueZ;
    }

    /**
     *
     * @param p
     * @return
     */
    public double dotProduct(Point p) {
        return this.x * p.x + this.y * p.y + this.z * p.z;
    }

    /**
     *
     * @param x
     * @param y
     * @param z
     * @return
     */
    public double dotProduct(double x, double y, double z) {
        return this.x * x + this.y * y + this.z * z;
    }

    /**
     *
     * @param p
     * @return
     */
    public Point crossProduct(Point p) {
        return new Point(this.getY() * p.getZ() - this.getZ() * p.getY(),
                this.getZ() * p.getX() - this.getX() * p.getZ(),
                this.getX() * p.getY() - this.getY() * p.getX()
        );
//        return new Point(this.y*p.z - this.z*p.y, -(this.z*p.x - this.x*p.y), this.x*p.y - this.y*p.x);
    }

    /**
     *
     * @param p
     * @return
     */
    public Point midpoint(Point p) {
        return new Point((this.x + p.x) / 2f, (this.y + p.y) / 2f, (this.z + p.z) / 2f);
    }

    /**
     *
     * @return
     */
    public Point normalize() {
        if (this.isZero()) {
            return this;
        } else {
            Double length = Math.sqrt(x * x + y * y + z * z);
            return new Point(x / length, y / length, z / length);
        }
    }

    @Override
    public String toString() {
        return "Point(" + x + ", " + y + ", " + z + ")";
    }

    /**
     *
     * @return
     */
    
    public boolean isZero() {
        return this.x == 0 && this.y == 0 && this.z == 0;
    }

    public boolean isColinear2D(Vector line) {
        return this.isColinear2D(line.getP0(), line.getP1());
    }

    /**
     *
     * @param a
     * @param b
     * @return
     */
    public boolean isColinear2D(Point a, Point b) {
        Point flat = new Point(this);
        flat.setZ(0);
        Point flatA = new Point(a);
        flatA.setZ(0);
        Point flatB = new Point(b);
        flatB.setZ(0);

        return flat.isColinear(flatA, flatB);
    }

    public boolean isColinear(Vector line) {
        return this.isColinear(line.getP0(), line.getP1());
    }

    /**
     *
     * @param a
     * @param b
     * @return
     */
    public boolean isColinear(Point a, Point b) {
        if (a.equals(this) || b.equals(this)) {
            return true;
        }
        return b.distanceToLine(a, this) < Point.RESOLUTIO && a.distanceToLine(b, this) < Point.RESOLUTIO && this.distanceToLine(a, b) < Point.RESOLUTIO;
    }

    /**
     *
     * @param a
     * @param b
     * @return
     */
    public boolean isInSegment(Point a, Point b) {

        if (this.equals(a) || this.equals(b)) {
            return true;
        }

        if (isColinear(a, b)) {
            double distance = a.distance(b);
            return this.distance(a) <= distance
                    && this.distance(b) <= distance;
        } else {
            return false;
        }
    }

    public boolean isInSegment(Vector vector) {

        return this.isInSegment(vector.getP1(), vector.getP1());
    }

    /**
     *
     * @param a
     * @param b
     * @return
     */
    public boolean isInSegment2D(Point a, Point b) {

        Point flatA = a.get2D();
        Point flatB = b.get2D();
        Point flatThis = this.get2D();

        return flatThis.isInSegment(flatA, flatB);
    }

    public boolean isInSegment2D(Vector vector) {

        return this.isInSegment2D(vector.getP0(), vector.getP1());
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @return
     */
    public boolean isOnTriangle2D(Point a, Point b, Point c) {
        Point flatA = new Point(a.getX(), a.getY(), 0);
        Point flatB = new Point(b.getX(), b.getY(), 0);
        Point flatC = new Point(c.getX(), c.getY(), 0);

        Point flatThis = new Point(this.getX(), this.getY(), 0);

        return flatThis.isOnTriangle(flatA, flatB, flatC) || this.isInSegment(flatA, flatB) || this.isInSegment(flatB, flatC) || this.isInSegment(flatC, flatA);
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @return
     */
    public boolean isInsideTriangle2D(Point a, Point b, Point c) {
        Point flatA = new Point(a.getX(), a.getY(), 0);
        Point flatB = new Point(b.getX(), b.getY(), 0);
        Point flatC = new Point(c.getX(), c.getY(), 0);

        Point flatThis = new Point(this.getX(), this.getY(), 0);

        return flatThis.isInsideTriangle(flatA, flatB, flatC);
    }

    /**
     *
     * @param triangle
     * @return
     */
    public boolean isOnTriangle2D(Triangle triangle) {
        return this.isOnTriangle2D(triangle.get(0), triangle.get(1), triangle.get(2));
    }

    /**
     *
     * @param triangle
     * @return
     */
    public boolean isInsideTriangle2D(Triangle triangle) {
        return this.isInsideTriangle2D(triangle.get(0), triangle.get(1), triangle.get(2));
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @return
     */
    public boolean isOnTriangle(Point a, Point b, Point c) {
        if (!this.isCoplanar(a, b, c)) {
            return false;
        }

        if (this.isInSegment(a, b)
                || this.isInSegment(b, c)
                || this.isInSegment(c, a)) {
            return true;
        }

        Vector ab = new Vector(a, b);
        Vector ac = new Vector(a, c);
        Vector abc = ab.crossProduct(ac);
        Vector ao = new Vector(a, this);
        boolean b1 = abc.crossProduct(ab).dotProduct(ao) > 0;

        Vector bc = new Vector(b, c);
        Vector ba = new Vector(b, a);
        Vector bca = bc.crossProduct(ba);
        Vector bo = new Vector(b, this);
        boolean b2 = bca.crossProduct(bc).dotProduct(bo) > 0;

        Vector ca = new Vector(c, a);
        Vector cb = new Vector(c, b);
        Vector cab = ca.crossProduct(cb);
        Vector co = new Vector(c, this);
        boolean b3 = cab.crossProduct(ca).dotProduct(co) > 0;

        return (b1 && b2 && b3);
    }

    /**
     * inside but not over the perimeter
     *
     * @param a
     * @param b
     * @param c
     * @return
     */
    public boolean isInsideTriangle(Point a, Point b, Point c) {
        if (!this.isCoplanar(a, b, c)) {
            return false;
        }

        if (this.isInSegment(a, b)
                || this.isInSegment(b, c)
                || this.isInSegment(c, a)) {
            return false;
        }

        Vector ab = new Vector(a, b);
        Vector ac = new Vector(a, c);
        Vector abc = ab.crossProduct(ac);
        Vector ao = new Vector(a, this);
        boolean b1 = abc.crossProduct(ab).dotProduct(ao) > 0;

        Vector bc = new Vector(b, c);
        Vector ba = new Vector(b, a);
        Vector bca = bc.crossProduct(ba);
        Vector bo = new Vector(b, this);
        boolean b2 = bca.crossProduct(bc).dotProduct(bo) > 0;

        Vector ca = new Vector(c, a);
        Vector cb = new Vector(c, b);
        Vector cab = ca.crossProduct(cb);
        Vector co = new Vector(c, this);
        boolean b3 = cab.crossProduct(ca).dotProduct(co) > 0;

        return (b1 && b2 && b3);
    }

    /**
     *
     * @param triangle
     * @return
     */
    public boolean isOnTriangle(Triangle triangle) {
        return this.isOnTriangle(triangle.get(0), triangle.get(1), triangle.get(2));
    }

    /**
     *
     * @param triangle
     * @return
     */
    public boolean isInsideTriangle(Triangle triangle) {
        return this.isInsideTriangle(triangle.get(0), triangle.get(1), triangle.get(2));
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @return
     */
    public boolean isCoplanar(Point a, Point b, Point c) {
        if (a.equals(b) || b.equals(c) || c.equals(a)) {
//            if(this.equals(a) || this.equals(b) || this.equals(c)){
//                return true;
//            }
            return false;
        }
        return c.subtract(a).dotProduct(b.subtract(a).crossProduct(this.subtract(c))) == 0;
    }

    /**
     *
     * @param a
     * @param b
     * @return
     */
    public double distanceToSegment(Point a, Point b) {

        Point v = b.subtract(a);
        Point w = this.subtract(a);

        double c1 = w.dotProduct(v);
        double c2 = v.dotProduct(v);

        if (c1 <= 0) {
            return this.distance(a);
        }
        if (c2 <= c1) {
            return this.distance(b);
        }

        double _b = c1 / c2;
        Point pb = a.add(v.multiply(_b));
        return this.distance(pb);
    }

    /**
     *
     * @param a
     * @param b
     * @return
     */
    public double distanceToSegment2D(Point a, Point b) {

        Point v = b.subtract(a).setZ(0);
        Point w = this.subtract(a).setZ(0);

        double c1 = w.dotProduct(v);
        double c2 = v.dotProduct(v);

        if (c1 <= 0) {
            return this.distance2D(a);
        }
        if (c2 <= c1) {
            return this.distance2D(b);
        }

        double _b = c1 / c2;
        Point pb = a.add(v.multiply(_b)).setZ(0);
        return this.distance2D(pb);
    }

    public Point getClosestPointOnSegment(Vector segment) {
        return this.getClosestPointOnSegment(segment.getP0(), segment.getP1());
    }

    public Point getClosestPointOnLine(Vector segment) {
        return this.getClosestPointOnLine(segment.getP0(), segment.getP1());
    }

    /**
     *
     * @param a
     * @param b
     * @return
     */
    public Point getClosestPointOnSegment(Point a, Point b) {
        Point v = b.subtract(a);
        Point w = this.subtract(a);

        double c1 = w.dotProduct(v);
        double c2 = v.dotProduct(v);

        if (c1 <= 0) {
            return a;
        }
        if (c2 <= c1) {
            return b;
        }

        double _b = c1 / c2;
        return a.add(v.multiply(_b));
    }

    public Point getClosestPointOnLine(Point a, Point b) {
        Point v = b.subtract(a);
        Point w = this.subtract(a);

        double c1 = w.dotProduct(v);
        double c2 = v.dotProduct(v);

        double _b = c1 / c2;
        return a.add(v.multiply(_b));
    }

    public Point getClosestPointOnLine2D(Vector line) {
        return getClosestPointOnLine2D(line.getP0(), line.getP1());
    }

    public Point getClosestPointOnLine2D(Point a, Point b) {
        Point v = b.get2D().subtract(a.get2D());
        Point w = this.get2D().subtract(a.get2D());

        double c1 = w.dotProduct(v);
        double c2 = v.dotProduct(v);

        double _b = c1 / c2;
        return a.add(v.multiply(_b));
    }

    public Point getClosestPointOnSegment2D(Vector segment) {
        return this.getClosestPointOnSegment2D(segment.getP0(), segment.getP1());
    }

    public Point getClosestPointOnSegment2D(Point a, Point b) {

        Point flat_a = a.get2D();
        Point flat_b = b.get2D();
        Point flat_this = this.get2D();

        Point v = flat_b.subtract(flat_a);
        Point w = flat_this.subtract(flat_a);

        double c1 = w.dotProduct(v);
        double c2 = v.dotProduct(v);

        if (c1 <= 0) {
            return flat_a;
        }
        if (c2 <= c1) {
            return flat_b;
        }

        double _b = c1 / c2;
        Point newPoint = flat_a.add(v.multiply(_b));
        double length = flat_a.distance(flat_b);
        double fraction = newPoint.distance(flat_a) / length;

        Vector vector = new Vector(flat_a, flat_b);
        return vector.interpolate(fraction).setZ(0);
    }

    /**
     *
     * @param l1
     * @param l2
     * @return
     */
    public double distanceToLine(Point l1, Point l2) {
        if (l1.equals(l2)) {
            return this.distance(l1);
        }
        Point v = l2.subtract(l1);
        Point w = this.subtract(l1);

        double c1 = w.dotProduct(v);
        double c2 = v.dotProduct(v);
        double b = c1 / c2;
        Point pb = l1.add(v.multiply(b));
        return this.distance(pb);
    }

    /**
     *
     * @param l1
     * @param l2
     * @return
     */
    public double distanceToLine2D(Point l1, Point l2) {
        if (l1.equals(l2)) {
            return this.distance2D(l1);
        }
        Point flat = new Point(this.x, this.y, 0);
        Point flatA = new Point(l1.x, l1.y, 0);
        Point flatB = new Point(l2.x, l2.y, 0);

        return flat.distanceToLine(flatA, flatB);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Point) {
            Point p = (Point) obj;
            return this.distance(p) < RESOLUTIO;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        Integer hashX = ((int) (x / RESOLUTIO));
        Integer hashY = ((int) (y / RESOLUTIO));
        Integer hashZ = ((int) (z / RESOLUTIO));

        int hash = 5;
        hash = 37 * hash + hashX;
        hash = 37 * hash + hashY;
        hash = 37 * hash + hashZ;
        return hash;

    }

    /**
     *
     * @return
     */
    
    public float getXf() {
        return (float) this.x;
    }

    /**
     *
     * @return
     */
    
    public float getYf() {
        return (float) this.y;
    }

    /**
     *
     * @return
     */
    
    public float getZf() {
        return (float) this.z;
    }

    /**
     *
     * @return
     */
    public double getX() {
        return this.x;
    }

    /**
     *
     * @return
     */
    public double getY() {
        return this.y;
    }

    /**
     *
     * @return
     */
    public double getZ() {
        return this.z;
    }

    
    public Point get2D() {
        return new Point(x, y, 0);
    }

    public static void mergePoints(Point point, Set<Point> points) {
        if (point == null) {
            return;
        }
        Optional<Point> closest = points.stream().min((otherPoint0, otherPoint1) -> {
            return Double.compare(otherPoint0.distance2D(point), otherPoint1.distance2D(point));
        });

        if (closest.isPresent() && closest.get().distance2D(point) < Point.RESOLUTIO) {
            point.set(closest.get());
        } else {
            points.add(point);
        }

    }

    public static List<Point> rejoin2D(List<Point> points) {
        return IntStream.range(0, points.size()).boxed().map((index) -> {

            Point previous = points.get((points.size() - 1 + index) % points.size());
            Point current = points.get(index);
            Point next = points.get((index + 1) % points.size());

            Vector v0 = new Vector(previous, current);
            Vector v1 = new Vector(current, next);

            double angle = v0.angle2D(v1);
            if (angle != 0d) {
                return current;
            } else {
                return null;
            }
        }).filter((point) -> {
            return point != null;
        }).collect(Collectors.toCollection(ArrayList::new));

    }

}
