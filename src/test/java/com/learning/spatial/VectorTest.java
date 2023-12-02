package com.learning.spatial;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import com.learning.spatial.Point;
import com.learning.spatial.Vector;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author caio
 */
public class VectorTest {


    public VectorTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getMiddlePoint method, of class Vector.
     */
    @Test
    public void testGetMiddlePoint() {
        Vector instance = new Vector(10, 10, 10);
        Point expResult = new Point(5, 5, 5);
        Point result = instance.getMiddlePoint();
        assertEquals(expResult, result);
    }

    /**
     * Test of size method, of class Vector.
     */
    @Test
    public void testSize() {
        Vector instance = new Vector(10, 10, 10);
        Point expResult = new Point(10, 10, 10);
        Point result = instance.size();
        assertEquals(expResult, result);
    }

    /**
     * Test of isColinear method, of class Vector.
     */
    @Test
    public void testIsColinear() {
        Vector v = new Vector(new Point(-10, 0, 0), new Point(10, 0, 0));
        Vector instance = new Vector(new Point(0, 0, 0), new Point(1, 0, 0));
        boolean expResult = true;
        boolean result = instance.isColinear(v);
        assertEquals(expResult, result);

        instance = new Vector(new Point(0, 0, 0), new Point(1, 1, 0));
        expResult = false;
        result = instance.isColinear(v);
        assertEquals(expResult, result);
    }

    /**
     * Test of isColinearY method, of class Vector.
     */
    @Test
    public void testIsColinear2D() {
        Vector v = new Vector(new Point(-10, 0, 0), new Point(10, 0, 0));
        Vector instance = new Vector(new Point(0, 0, 0), new Point(1, 0, 10));
        boolean expResult = true;
        boolean result = instance.isColinear2D(v);
        assertEquals(expResult, result);

        instance = new Vector(new Point(0, 0, 0), new Point(1, 0, 1));
        expResult = true;
        result = instance.isColinear2D(v);
        assertEquals(expResult, result);

        instance = new Vector(new Point(0, 0, 0), new Point(1, 1, 1));
        expResult = false;
        result = instance.isColinear2D(v);
        assertEquals(expResult, result);

    }

    /**
     * Test of interpolate method, of class Vector.
     */
    @Test
    public void testInterpolate() {
        Double d = 0d;
        Vector instance = new Vector(new Point(0, 0, 0), new Point(10, 10, 10));
        Point expResult = new Point(0, 0, 0);
        Point result = instance.interpolate(d);
        assertEquals(expResult, result);

        d = 0.5d;
        expResult = new Point(5, 5, 5);
        result = instance.interpolate(d);
        assertEquals(expResult, result);
    }

    /**
     * Test of multiply method, of class Vector.
     */
    @Test
    public void testMultiply_double() {
        double d = 2.0;
        Vector instance = new Vector(10, 10, 10);
        instance.multiply(d);
        assertTrue(instance.getX() == 20 && instance.getY() == 20 && instance.getZ() == 20);
    }

    /**
     * Test of multiply method, of class Vector.
     */
    @Test
    public void testMultiply_3args() {
        double dx = 2.0;
        double dy = 3.0;
        double dz = 4.0;
        Vector instance = new Vector(10, 10, 10);
        instance.multiply(dx, dy, dz);
        assertTrue(instance.getX() == 20 && instance.getY() == 30 && instance.getZ() == 40);

    }

    /**
     * Test of normalize method, of class Vector.
     */
    @Test
    public void testNormalize() {
        Vector instance = new Vector(10, 0, 0);
        instance.normalize();
        assertTrue(instance.getX() == 1 && instance.getY() == 0 && instance.getZ() == 0);
    }

    /**
     * Test of angleY method, of class Vector.
     */
    @Test
    public void testAngle2D() {
        {
            Vector vector = new Vector(10, 0, 0);
            Vector instance = new Vector(0, 5, 10);
            double expResult = -90.0;
            double result = instance.angle2D(vector);
            assertEquals(expResult, result, 0.0);
        }

        {
            Vector vector = new Vector(new Point(0, 0, 0), new Point(0, 10, 0));
            Vector instance = new Vector(new Point(0, 10, 0), new Point(0, 20, 0));
            double expResult = 00.00;
            double result = instance.angle2D(vector);
            assertEquals(expResult, result, 0.0);
        }

        {
            Vector vector = new Vector(new Point(0, 0, 0), new Point(0, 10, 0));
            Vector instance = new Vector(new Point(0, 10, 0), new Point(10, 10, 0));
            double expResult = 90.00;
            double result = instance.angle2D(vector);
            assertEquals(expResult, result, 0.0);
        }
    }

    /**
     * Test of angle method, of class Vector.
     */
    @Test
    public void testAngle() {
        Vector vector = new Vector(10, 0, 0);
        Vector instance = new Vector(0, 10, 0);
        double expResult = -90.0;
        double result = instance.angle(vector);
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of rotateX method, of class Vector.
     */
    @Test
    public void testRotateX() {
        double degrees = 90.0;
        Vector instance = new Vector(0, 0, -10);
        instance.rotateX(degrees);
        assertTrue(10 == instance.getY());
    }

    /**
     * Test of rotateY method, of class Vector.
     */
    @Test
    public void testRotateY() {
        double degrees = 90.0;
        Vector instance = new Vector(10, 0, 0);
        instance.rotateY(degrees);
        assertTrue(-10 == instance.getZ());
    }

    /**
     * Test of rotateZ method, of class Vector.
     */
    @Test
    public void testRotateZ() {
        double degrees = 90.0;
        Vector instance = new Vector(0, 10, 0);
        instance.rotateZ(degrees);
        assertTrue(-10 == instance.getX());
    }

    /**
     * Test of getX method, of class Vector.
     */
    @Test
    public void testGetX() {
        Vector instance = new Vector(10, 11, 12);
        double expResult = 10;
        double result = instance.getX();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getY method, of class Vector.
     */
    @Test
    public void testGetY() {
        Vector instance = new Vector(10, 11, 12);
        double expResult = 11;
        double result = instance.getY();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getZ method, of class Vector.
     */
    @Test
    public void testGetZ() {
        Vector instance = new Vector(10, 11, 12);
        double expResult = 12;
        double result = instance.getZ();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getP0 method, of class Vector.
     */
    @Test
    public void testGetP0() {
        Vector instance = new Vector(10, 10, 10);
        Point expResult = Point.zero();
        Point result = instance.getP0();
        assertEquals(expResult, result);

        instance = new Vector(new Point(1, 1, 1), new Point(2, 2, 2));
        expResult = new Point(1, 1, 1);
        result = instance.getP0();
        assertEquals(expResult, result);
    }

    /**
     * Test of getP1 method, of class Vector.
     */
    @Test
    public void testGetP1() {
        Vector instance = new Vector(10, 10, 10);
        Point expResult = new Point(10, 10, 10);
        Point result = instance.getP1();
        assertEquals(expResult, result);

        instance = new Vector(new Point(1, 1, 1), new Point(2, 2, 2));
        expResult = new Point(2, 2, 2);
        result = instance.getP1();
        assertEquals(expResult, result);
    }

    /**
     * Test of shift method, of class Vector.
     */
    @Test
    public void testShift_Vector() {
        Vector v = new Vector(10, 10, 10);
        Vector instance = new Vector(2, 2, 2);
        instance.shift(v);
        assertTrue(instance.getP0().equals(new Point(10, 10, 10)) && instance.getP1().equals(new Point(12, 12, 12)));
    }

    /**
     * Test of shift method, of class Vector.
     */
    @Test
    public void testShift_3args() {
        double x = 1.0;
        double y = 2.0;
        double z = 3.0;
        Vector instance = new Vector(10, 10, 10);
        instance.shift(x, y, z);
        assertTrue(instance.getP0().equals(new Point(1, 2, 3)) && instance.getP1().equals(new Point(11, 12, 13)));

    }

    /**
     * Test of segmentIntersectionY method, of class Vector.
     */
    @Test
    public void testSegmentIntersection2D() {
        Vector otherSegment = new Vector(new Point(0, 0, 0), new Point(0, 10, 0));
        Vector instance = new Vector(new Point(-5, 5, 5), new Point(5, 5, 5));
        Point expResult = new Point(0, 5, 5);
        Point result = instance.segmentIntersection2D(otherSegment);
        assertEquals(expResult, result);

        instance = new Vector(new Point(-5, 5, 5), new Point(0, 5, 5));
        expResult = new Point(0, 5, 5);
        result = instance.segmentIntersection2D(otherSegment);
        assertEquals(expResult, result);

        instance = new Vector(new Point(-5, 0, 5), new Point(0, 0, 5));
        expResult = new Point(0, 0, 5);
        result = instance.segmentIntersection2D(otherSegment);
        assertEquals(expResult, result);

        instance = new Vector(new Point(-5, 0, 10), new Point(0, 0, 10));
        expResult = new Point(0, 0, 10);
        result = instance.segmentIntersection2D(otherSegment);
        assertEquals(expResult, result);

        instance = new Vector(new Point(-5, 0, 10), new Point(0, 10, 0));
        expResult = new Point(0, 10, 0);
        result = instance.segmentIntersection2D(otherSegment);
        assertEquals(expResult, result);
    }

    /**
     * Test of getLength method, of class Vector.
     */
    @Test
    public void testGetLength() {
        Vector instance = new Vector(10, 10, 10);
        Double expResult = Math.sqrt(300);
        Double result = instance.getLength();
        assertEquals(expResult, result);
    }

    /**
     * Test of equals method, of class Vector.
     */
    @Test
    public void testEquals() {
        Object obj = new Vector(10, 10, 10);
        Vector instance = new Vector(10, 10, 10);
        boolean expResult = true;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);

        obj = new Vector(new Point(0, 0, 0), new Point(10, 10, 10));
        instance = new Vector(new Point(1, 1, 1), new Point(11, 11, 11));
        expResult = false;
        result = instance.equals(obj);
        assertEquals(expResult, result);
    }

}
