/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.learning.spatial;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 *
 * @author caio
 */
public class Vector implements IInterpolable<Point, Double> {
    
    static double ANGLE_RESOLUTION = 100d;

    private Point p0;
    private Point p1;
    private double x;
    private double y;
    private double z;

    /**
     *
     * @param v
     */
    public Vector(Vector v) {
        if (v == null) {
            throw new InvalidGeometryException();
        }
        this.p0 = v.getP0();
        this.p1 = v.getP1();
        this.x = p1.getX() - p0.getX();
        this.y = p1.getY() - p0.getY();
        this.z = p1.getZ() - p0.getZ();
    }

    /**
     *
     * @param x
     * @param y
     * @param z
     */
    public Vector(double x, double y, double z) {
        if (x == 0 && y == 0 && z == 0) {
            throw new InvalidGeometryException();
        }
        this.p0 = new Point(0, 0, 0);
        this.p1 = new Point(x, y, z);
        this.x = p1.getX() - p0.getX();
        this.y = p1.getY() - p0.getY();
        this.z = p1.getZ() - p0.getZ();
    }

    /**
     *
     * @param p0
     * @param p1
     */
    public Vector(Point p0, Point p1) {
        if (p0 == null) {
            throw new InvalidGeometryException();
        }
        if (p1 == null) {
            throw new InvalidGeometryException();
        }
        if (p0.equals(p1)) {
            throw new InvalidGeometryException();
        }
        this.p0 = new Point(p0);
        this.p1 = new Point(p1);
        this.x = p1.getX() - p0.getX();
        this.y = p1.getY() - p0.getY();
        this.z = p1.getZ() - p0.getZ();

    }

    public double getXfor(double y, double z) {
        double t;
        if (this.y != 0) {
            t = (y - p0.getY()) / (this.y);
        } else {
            t = (z - p0.getZ()) / (this.z);
        }
        double x = p0.getX() + (this.x * t);
        return x;
    }

    public double getYfor(double x, double z) {
        double t;
        if (this.x != 0) {
            t = (x - p0.getX()) / (this.x);
        } else {
            t = (z - p0.getZ()) / (this.z);
        }
        double y = p0.getY() + (this.y * t);
        return y;
    }

    public double getZfor(double x, double y) {
        double t;
        if (this.x != 0) {
            t = (x - p0.getX()) / (this.x);
        } else {
            t = (y - p0.getY()) / (this.y);
        }
        double z = p0.getZ() + (this.z * t);
        return z;
    }

    public Vector reversed() {
        return new Vector(this.getP1(), this.getP0());
    }

    public void reverse() {
        this.set(this.getP1(), this.getP0());
    }

    /**
     *
     * @param x
     * @param y
     * @param z
     */
    public void set(double x, double y, double z) {
        if (x == 0 && y == 0 && z == 0) {
            throw new InvalidGeometryException();
        }
        this.x = x;
        this.y = y;
        this.z = z;
        this.p0 = Point.zero();
        this.p1 = new Point(x, y, z);
    }

    /**
     *
     * @param p0
     * @param p1
     */
    public void set(Point p0, Point p1) {
        if (p0 == null || p1 == null) {
            throw new InvalidGeometryException();
        }
        if (p0.equals(p1)) {
            throw new InvalidGeometryException();
        }
        this.p0 = p0;
        this.p1 = p1;
        this.x = p1.getX() - p0.getX();
        this.y = p1.getY() - p0.getY();
        this.z = p1.getZ() - p0.getZ();
    }

    /**
     *
     * @param v
     */
    public void set(Vector v) {
        if (v == null) {
            throw new InvalidGeometryException();
        }
        this.p0 = v.getP0();
        this.p1 = v.getP1();
        this.x = p1.getX() - p0.getX();
        this.y = p1.getY() - p0.getY();
        this.z = p1.getZ() - p0.getZ();
    }

    /**
     *
     * @return
     */
    public Point getMiddlePoint() {
        return new Point((getP0().getX() + getP1().getX()) / 2, (getP0().getY() + getP1().getY()) / 2, (getP0().getZ() + getP1().getZ()) / 2);
    }

    /**
     *
     * @return
     */
    public Point size() {
        return new Point(getP1().getX() - getP0().getX(), getP1().getY() - getP0().getY(), getP1().getZ() - getP0().getZ());
    }

    /**
     *
     * @param v
     * @return
     */
    public boolean isColinear(Vector v) {
        return this.getP0().isColinear(v.p0, v.p1) && this.getP1().isColinear(v.p0, v.p1);
    }

    /**
     *
     * @param v
     * @return
     */
    public boolean isColinear2D(Vector v) {
        return p0.distanceToLine2D(v.getP0(), v.getP1()) < Point.RESOLUTIO && p1.distanceToLine2D(v.getP0(), v.getP1()) < Point.RESOLUTIO;
    }

    @Override
    public Point interpolate(Double d) {
        if (d < 0d) {
            d = 0d;
        } else {
            if (d > 1d) {
                d = 1d;
            }
        }
        Point size = this.size();
        return getP0().add(new Point(size.getX() * d, size.getY() * d, size.getZ() * d));
    }

    /**
     *
     * @param d
     */
    public void multiply(double d) {
        if (d == 0) {
            throw new InvalidParameterException();
        }
        this.x = getX() * d;
        this.y = getY() * d;
        this.z = getZ() * d;
        this.p1 = this.p0.add(x, y, z);
    }

    public Vector multiplied(double d) {
        if (d == 0) {
            throw new InvalidParameterException();
        }
        Vector answer = new Vector(new Point(this.p0), new Point(this.p1));
        answer.multiply(d);
        return answer;
    }

    public Vector multiplied(double dx, double dy, double dz) {
        Vector answer = new Vector(new Point(this.p0), new Point(this.p1));
        answer.multiply(dx, dy, dz);
        return answer;
    }

    /**
     *
     * @param dx
     * @param dy
     * @param dz
     */
    public void multiply(double dx, double dy, double dz) {
        if (dx == 0 && dy == 0 && dz == 0) {
            throw new InvalidGeometryException();
        }
        this.x = getX() * dx;
        this.y = getY() * dy;
        this.z = getZ() * dz;
        this.p1 = this.p0.add(x, y, z);
    }

    /**
     *
     */
    public boolean normalize() {
        Double length = Math.sqrt(getX() * getX() + getY() * getY() + getZ() * getZ());
        Point newPoint = this.p0.add(getX() / length, getY() / length, getZ() / length);
        if (this.p0.equals(newPoint)) {
            return false;
        } else {
            this.set(this.p0, newPoint);
            return true;
        }
    }

    public Vector normalized() {
        Vector answer = new Vector(new Point(this.p0), new Point(this.p1));
        answer.normalize();
        return answer;
    }

    /**
     *
     * @param vector
     * @return
     */
    public double dotProduct(Vector vector) {
        return this.getX() * vector.getX() + this.getY() * vector.getY() + this.getZ() * vector.getZ();
    }

    /**
     *
     * @param vector
     * @return
     */
    public Vector crossProduct(Vector vector) {
        double dx = this.getY() * vector.getZ() - this.getZ() * vector.getY();
        double dy = this.getZ() * vector.getX() - this.getX() * vector.getZ();
        double dz = this.getX() * vector.getY() - this.getY() * vector.getX();
        if (dx == 0 && dy == 0 && dz == 0) {
            return null;
        }
        return new Vector(dx, dy, dz);
    }

    public Vector added(Vector vector) {
        Point p1 = new Point(vector.getP1()).add(this.size());
        return new Vector(this.getP0(), p1);
    }

    public void add(double length) {
        double ratio = 1 + length / this.getLength();
        this.multiply(ratio);
    }

    public Vector added(double length) {
        double ratio = 1 + length / this.getLength();
        return this.multiplied(ratio);
    }

    /**
     *
     * @return
     */
    public double magnitude() {
        return Math.sqrt(this.getX() * this.getX() + this.getY() * this.getY() + this.getZ() * this.getZ());
    }

    public double magnitude2D() {
        return Math.sqrt(this.getX() * this.getX() + this.getY() * this.getY());
    }

    /**
     *
     * @param vector
     * @return
     */
    public Double angle2D(Vector vector) {
        if (vector == null) {
            throw new InvalidParameterException();
        }
        Vector this2D = this.get2D();
        Vector vector2D = vector.get2D();
        if (this2D == null || vector2D == null) {
            return null;
        }
        return this2D.angle(vector2D);
    }

    /**
     *
     * @param vector
     * @return
     */
    public double angle(Vector vector) {
        if (vector == null) {
            throw new InvalidParameterException();
        }
        if (this.getP1().equals(vector.getP0())) {
            if (this.getP1().isInSegment(this.getP0(), vector.getP1())) {
                return 0;
            }
            if (vector.getP1().isInSegment(this.getP0(), this.getP1()) || this.getP0().isInSegment(vector.getP0(), vector.getP1())) {
                return 180;
            }
        }
        double crossX, crossY, crossZ, cross, dot;
        crossX = this.y * vector.z - this.z * vector.y;
        crossY = this.z * vector.x - this.x * vector.z;
        crossZ = this.x * vector.y - this.y * vector.x;
        cross = Math.sqrt(crossX * crossX
                + crossY * crossY + crossZ * crossZ);
        dot = this.x * vector.x + this.y * vector.y + this.z + vector.z;
        double angle = Math.atan2(cross, dot);
        if (crossZ < 0) {
            angle = -angle;
        }
        return (Math.round(Math.toDegrees(angle) * ANGLE_RESOLUTION)) / (ANGLE_RESOLUTION);
    }

    @Override
    public String toString() {
        return this.getP0() + ", " + this.getP1();
    }

    //http://en.wikipedia.org/wiki/Rotation_matrix
    /**
     *
     * @param degrees
     */
    public void rotateX(double degrees) {
        double rad = Math.toRadians(degrees);
        this.set(getP0(), new Point(
                getP0().getX() + getX(),
                getP0().getY() + Math.cos(rad) * this.getY() + -Math.sin(rad) * this.getZ(),
                getP0().getZ() + Math.sin(rad) * this.getY() + Math.cos(rad) * this.getZ()
        )
        );

    }

    /**
     *
     * @param degrees
     */
    public void rotateY(double degrees) {
        double rad = Math.toRadians(degrees);
        double newX = getP0().getX() + Math.cos(rad) * this.getX() + Math.sin(rad) * this.getZ();
        double newY = getP0().getY() + getY();
        double newZ = getP0().getZ() + -Math.sin(rad) * this.getX() + Math.cos(rad) * this.getZ();

        this.set(getP0(), new Point(
                newX,
                newY,
                newZ
        )
        );

    }

    /**
     *
     * @param degrees
     */
    public void rotateZ(double degrees) {
        double rad = Math.toRadians(degrees);
        this.set(getP0(), new Point(
                getP0().getX() + Math.cos(rad) * this.getX() + -Math.sin(rad) * this.getY(),
                getP0().getY() + Math.sin(rad) * this.getX() + Math.cos(rad) * this.getY(),
                getP0().getZ() + getZ()
        )
        );

    }

    public Vector rotatedX(double degrees) {
        Vector answer = new Vector(new Point(this.p0), new Point(this.p1));
        answer.rotateX(degrees);
        return answer;
    }

    public Vector rotatedY(double degrees) {
        Vector answer = new Vector(new Point(this.p0), new Point(this.p1));
        answer.rotateY(degrees);
        return answer;
    }

    public Vector rotatedZ(double degrees) {
        Vector answer = new Vector(new Point(this.p0), new Point(this.p1));
        answer.rotateZ(degrees);
        return answer;
    }

    /**
     * @return the x
     */
    
    public double getX() {
        return x;
    }

    /**
     * @return the y
     */
    
    public double getY() {
        return y;
    }

    /**
     * @return the z
     */
    
    public double getZ() {
        return z;
    }

    /**
     * @return the p0
     */
    public Point getP0() {
        return p0;
    }

    public void setP0(Point p0) {
        this.set(p0, p1);
    }

    /**
     * @return the p1
     */
    public Point getP1() {
        return p1;
    }

    public void setP1(Point p1) {
        this.set(p0, p1);
    }

    /**
     *
     * @param v
     */
    public void shift(Vector v) {
        this.shift(v.x, v.y, v.z);
    }

    /**
     *
     * @param x
     * @param y
     * @param z
     */
    public void shift(double x, double y, double z) {
        this.p0 = this.p0.add(x, y, z);
        this.p1 = this.p1.add(x, y, z);
    }

    public Vector shifted(double x, double y, double z) {
        Vector answer = new Vector(new Point(this.p0), new Point(this.p1));
        answer.shift(x, y, z);
        return answer;
    }

    public void shift(Point point) {
        this.shift(point.getX(), point.getY(), point.getZ());
    }

    public Vector shifted(Point point) {
        return this.shifted(point.getX(), point.getY(), point.getZ());
    }

    /**
     *
     * @param u
     * @return
     */
    public Vector projectedOn(Vector u) {
        Vector answer = new Vector(u.x, u.y, u.z);
        answer.multiply(this.dotProduct(u) / Math.pow(u.magnitude(), 2));
        return answer;
    }

//http://www.lucidarme.me/?p=1872 ->theory
//inspired on https://gist.github.com/hanigamal/6556506
    /**
     *
     * @param otherLine
     * @return
     */
    private Point lineIntersection(Vector otherLine, boolean inSegment) {
        double angle = Math.abs(this.angle2D(otherLine));
        if (angle == 0 || angle == 180 || this.isColinear(otherLine)) {
            return null;
        }
        if ((!inSegment) && this.getP0().isColinear(otherLine) && this.getP1().isColinear(otherLine)) {
            return null;
        }

        if (this.p0.isInSegment(otherLine)) {
            return new Point(this.p0);

        }
        if (this.p1.isInSegment(otherLine)) {
            return new Point(this.p1);
        }
        if (otherLine.p0.isInSegment(this)) {
            return new Point(otherLine.p0);
        }
        if (otherLine.p1.isInSegment(this)) {
            return new Point(otherLine.p1);
        }
        Vector a = this;

        Point da = a.getP1().subtract(a.getP0());
        Point db = otherLine.getP1().subtract(otherLine.getP0());
        Point dc = otherLine.getP0().subtract(a.getP0());

        if (dc.dotProduct(da.crossProduct(db)) != 0.0) {
            return null;
        }

        Point cross = da.crossProduct(db);
        double dem = cross.getX() * cross.getX() + cross.getY() * cross.getY() + cross.getZ() * cross.getZ();

        double t = dc.crossProduct(db).dotProduct(da.crossProduct(db)) / dem;
//        double tOther = dc.crossProduct(this.size()).dotProduct(da.crossProduct(db)) / dem;
        if (Double.isNaN(t)) {
            return null;
        }
        Point point = a.getP0().add(da.multiply(t));
        if (inSegment) {

            boolean isInsideThis = this.p0.distance(point) <= this.getLength()
                    && this.p1.distance(point) <= this.getLength();
            boolean isInsideOther = otherLine.p0.distance(point) <= otherLine.getLength()
                    && otherLine.p1.distance(point) <= otherLine.getLength();
            boolean isOnThisEnd = this.p0.equals(point) || this.p1.equals(point);
            boolean isOnOtherEnd = otherLine.p0.equals(point) || otherLine.p1.equals(point);
            if ((isOnThisEnd && (isInsideOther || isOnOtherEnd))
                    || (isOnOtherEnd && (isInsideThis || isOnThisEnd))
                    || (isInsideThis && isInsideOther)) {

                return point;
            }
//            if(t >= 0.0 && t <= 1.0 && tOther >= 0.0 && tOther <= 1.0){
//                return point;
//            }
        } else {
            return point;
        }

        return null;
    }

    public Point lineIntersection(Vector otherLine) {

        return lineIntersection(otherLine, false);

    }

    
    public Vector get2D() {
        Point p0_2D = this.getP0().get2D();
        Point p1_2D = this.getP1().get2D();
        if (p0_2D.equals(p1_2D)) {
            return null;
        }
        return new Vector(p0_2D, p1_2D);
    }

    //returns the Z from the this Vector's equation
    private Point lineIntersection2D(Vector otherLine, boolean inSegment) {
        Vector this2D = this.get2D();
        Vector other2D = otherLine.get2D();
        if (this2D != null && other2D != null) {
            Point intersection = this2D.lineIntersection(other2D, inSegment);
            if (intersection != null) {
                double intersectionZ = this.getZfor(intersection.getX(), intersection.getY());
                intersection.setZ(intersectionZ);
                if (intersection.equals(otherLine.getP0())) {
                    intersection.set(otherLine.getP0());
                }
                if (intersection.equals(otherLine.getP1())) {
                    intersection.set(otherLine.getP1());
                }
            }

            return intersection;
        } else {
            return null;
        }
    }

    /**
     * @param otherLine
     * @return the Z from the this Vector's equation
     */
    public Point lineIntersection2D(Vector otherLine) {
        return this.lineIntersection2D(otherLine, false);
    }

    public Point segmentIntersection(Vector otherSegment) {
        return lineIntersection(otherSegment, true);
    }

    /**
     *
     * @param otherSegment
     * @return the Z from the this Vector's equation
     */
    public Point segmentIntersection2D(Vector otherSegment) {
        return this.lineIntersection2D(otherSegment, true);
    }

    public Point rayIntersection(Vector otherRay) {
        Point intersection = this.lineIntersection(otherRay);
        if (intersection == null) {
            return null;
        }
        if (intersection.equals(this.getP0()) || intersection.equals(otherRay.getP0())) {
            return intersection;
        }
        Vector direction = new Vector(this.getP0(), intersection);
        Vector otherDirection = new Vector(otherRay.getP0(), intersection);
        if (Math.abs(direction.angle(this)) == 0.0 && Math.abs(otherDirection.angle(otherRay)) == 0.0) {
            return intersection;
        } else {
            return null;
        }

    }

    public Point rayIntersection2D(Vector otherRay) {
        return this.get2D().rayIntersection(otherRay.get2D());
    }

    /**
     *
     * @return
     */
    
    @Override
    public Double getLength() {
        return this.magnitude();
    }

    
    public Double getLength2D() {
        return this.magnitude2D();
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 31 * hash + Objects.hashCode(this.p0);
        hash = 31 * hash + Objects.hashCode(this.p1);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Vector other = (Vector) obj;
        if (!Objects.equals(this.p0, other.p0)) {
            return false;
        }
        if (!Objects.equals(this.p1, other.p1)) {
            return false;
        }
        return true;
    }

    /**
     * Collision between a LINE and the plane, not between a SEGMENT and the
     * plane
     *
     * @param a
     * @param b
     * @param c
     * @return
     */
    public Point planeIntersection(Point a, Point b, Point c) {
        return Utils.linePlaneIntersection(a, b, c, this);
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @return
     */
    public boolean isCoplanar(Point a, Point b, Point c) {
        return this.p0.isCoplanar(a, b, c) && this.p1.isCoplanar(a, b, c);
    }

    public boolean overlaps2D(Vector other) {
        return this.get2D().overlaps(other.get2D());
    }

    public boolean overlaps(Vector other) {

//        return this.isColinear(other) && (this.p0.isInSegment(other) || this.p1.isInSegment(other) || other.getP0().isInSegment(this) || other.getP1().isInSegment(this) );
        return this.segmentIntersection(other) == null && this.isColinear(other) && (this.p0.isInSegment(other) || this.p1.isInSegment(other) || other.getP0().isInSegment(this) || other.getP1().isInSegment(this));
    }

    public Vector bissector(Vector other) {
        Point intersection = this.lineIntersection(other);
        Point a;
        Point b;
        {
            double angle = new Vector(this.getP0(), intersection).angle(this);
            if (angle == 0 || angle == -0) {
                a = this.getP0();
            } else {
                a = this.getP1();
            }
        }

        {
            double angle = new Vector(intersection, other.getP1()).angle(other);
            if (angle == 0 || angle == -0) {
                b = other.getP1();
            } else {
                b = other.getP0();
            }
        }

        return Vector.bissector(a, intersection, b);
    }

    public Vector bissector2D(Vector other) {
        return this.get2D().bissector(other.get2D());
    }

    public static Vector bissector2D(Point previous, Point current, Point next) {
        Point previous2D = previous.get2D();
        Point current2D = current.get2D();
        Point next2D = next.get2D();
        if (previous2D.equals(current2D) || current2D.equals(next2D)) {
            return null;
        }
        return bissector(previous2D, current2D, next2D);
    }

    public static Vector bissector(Point previous, Point current, Point next) {
        Vector v0 = new Vector(current, previous).normalized().multiplied(1/Point.RESOLUTIO);
        Vector v1 = new Vector(current, next).normalized().multiplied(1/Point.RESOLUTIO);
        Vector answer = new Vector(current, v0.getP1().midpoint(v1.getP1()));
        return answer;
    }

    static public List<Vector> simplify2D(List<? extends Vector> list) {
        List<Point> points = IntStream.range(0, list.size()).boxed().map((index) -> {

            Vector previousVector = list.get((list.size() - 1 + index) % list.size());
            Point previous = previousVector.getP0().get2D();
            Vector currentVector = list.get(index);
            Point current = currentVector.getP0().get2D();
            Point next = currentVector.getP1().get2D();

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

        return IntStream.range(0, points.size()).boxed().map((index) -> {
            Point p0 = points.get(index);
            Point p1 = points.get((index + 1) % points.size());

            return new Vector(p0, p1);
        }).collect(Collectors.toList());
    }

    public boolean approaches2D(Point p) {

        if (this.getP0().get2D().equals(p.get2D())) {
            return false;
        }
        if (this.getP1().get2D().equals(p.get2D())) {
            return true;
        }
//        if (p.isInSegment2D(this)) {
//            return false;
//        }
        double angle = new Vector(this.getP0(), p).angle2D(this);
        return angle == 0 || angle == -0;
    }

    public boolean distances2D(Point p) {

        if (this.getP1().get2D().equals(p.get2D())) {
            return false;
        }
        if (this.getP0().get2D().equals(p.get2D())) {
            return true;
        }
//        if (p.isInSegment2D(this)) {
//            return true;
//        }
        double angle = new Vector(p, this.getP1()).angle2D(this);
        return angle == 0 || angle == -0;
    }

}
