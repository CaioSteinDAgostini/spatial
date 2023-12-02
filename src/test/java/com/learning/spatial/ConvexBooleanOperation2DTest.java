package com.learning.spatial;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import com.learning.spatial.Convex;
import com.learning.spatial.ConvexBooleanOperation2D;
import static com.learning.spatial.Point.Point;
import com.learning.spatial.SubDivision2D;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author caio
 */
public class ConvexBooleanOperation2DTest {

    public ConvexBooleanOperation2DTest() {
    }

    @Test
    public void testSomeMethod() {

        {
            Convex base99 = new Convex(
                    Point(5d, 5d, 0d),
                    Point(10d, 5d, 0d),
                    Point(15d, 5d, 0d),
                    Point(15d, 10d, 0d),
                    Point(15d, 15d, 0d),
                    Point(10d, 15d, 0d),
                    Point(5d, 15d, 0d),
                    Point(5d, 10d, 0d)
            );
            Convex modifier99 = new Convex(
                    Point(10d, 3d, 0d),
                    Point(3d, 10d, 0d),
                    Point(10d, 17d, 0d),
                    Point(17d, 10d, 0d)
            );
            ConvexBooleanOperation2D bo99 = new ConvexBooleanOperation2D(base99, modifier99);
            {
                List<Point> expected = List.of(
                        Point(8.0, 5.0, 0.0),
                        Point(5.0, 8.0, 0.0),
                        Point(5.0, 10.0, 0.0),
                        Point(5.0, 12.0, 0.0),
                        Point(8.0, 15.0, 0.0),
                        Point(10.0, 15.0, 0.0),
                        Point(12.0, 15.0, 0.0),
                        Point(15.0, 12.0, 0.0),
                        Point(15.0, 10.0, 0.0),
                        Point(15.0, 8.0, 0.0),
                        Point(12.0, 5.0, 0.0),
                        Point(10.0, 5.0, 0.0)
                );
                Assert.assertEquals(expected, bo99.intersectPoints(bo99.getInAndOutIntersection()));
            }
        }

        {
            Convex base99 = new Convex(
                    Point(5d, 5d, 0d),
                    Point(15d, 5d, 0d),
                    Point(15d, 15d, 0d),
                    Point(5d, 15d, 0d)
            );
            Convex modifier99 = new Convex(
                    Point(10d, 3d, 0d),
                    Point(3d, 10d, 0d),
                    Point(10d, 17d, 0d),
                    Point(17d, 10d, 0d)
            );
            ConvexBooleanOperation2D bo99 = new ConvexBooleanOperation2D(base99, modifier99);
            {
                List<Point> expected = List.of(
                        Point(8.0, 5.0, 0.0),
                        Point(5.0, 8.0, 0.0),
                        Point(5.0, 12.0, 0.0),
                        Point(8.0, 15.0, 0.0),
                        Point(12.0, 15.0, 0.0),
                        Point(15.0, 12.0, 0.0),
                        Point(15.0, 8.0, 0.0),
                        Point(12.0, 5.0, 0.0)
                );
                Assert.assertEquals(expected, bo99.intersectPoints(bo99.getInAndOutIntersection()));
            }
        }

        {
            System.err.println("---------------------------------");
            System.err.println("1");
            Convex modLeftTop = new Convex(
                    Point(150d, 120d, 0d),
                    Point(240d, 120d, 0d),
                    Point(250d, 130d, 0d),
                    Point(240d, 140d, 0d),
                    Point(150d, 140d, 0d)
            );
            Convex base0 = new Convex(
                    Point(200d, 100d, 0d),
                    Point(300d, 100d, 0d),
                    Point(300d, 200d, 0d),
                    Point(200d, 200d, 0d)
            );

            ConvexBooleanOperation2D bo0 = new ConvexBooleanOperation2D(base0, modLeftTop);
            {
                SubDivision2D s = bo0.getInAndOutIntersection().get(0);

                Assert.assertEquals(Point(200d, 120d, 0d), s.getOut());
                Assert.assertEquals(Point(200d, 140d, 0d), s.getIn());

                List<Point> expectedSubtraction = List.of(
                        Point(200d, 140d, 0d),
                        Point(240d, 140d, 0d),
                        Point(250d, 130d, 0d),
                        Point(240d, 120d, 0d),
                        Point(200d, 120d, 0d),
                        Point(200d, 100d, 0d),
                        Point(300d, 100d, 0d),
                        Point(300d, 200d, 0d),
                        Point(200d, 200d, 0d)
                );
                Assert.assertEquals(expectedSubtraction, bo0.subtractPoints(List.of(s)).get(0));

                List<Point> expectedIntersection = List.of(
                        Point(200d, 140d, 0d),
                        Point(240d, 140d, 0d),
                        Point(250d, 130d, 0d),
                        Point(240d, 120d, 0d),
                        Point(200d, 120d, 0d)
                );
                {
                    List<Point> results = bo0.intersectPoints(List.of(s));
                    Assert.assertEquals(expectedIntersection, results);
                }

            }
            ConvexBooleanOperation2D bo0Inverse = new ConvexBooleanOperation2D(modLeftTop, base0);
            {
                List<Point> expectedIntersection = List.of(
                        Point(200d, 120d, 0d),
                        Point(240d, 120d, 0d),
                        Point(250d, 130d, 0d),
                        Point(240d, 140d, 0d),
                        Point(200d, 140d, 0d)
                );
                SubDivision2D s = bo0Inverse.getInAndOutIntersection().get(0);
                List<Point> results = bo0Inverse.intersectPoints(List.of(s));
                Assert.assertEquals(expectedIntersection, results);
            }
        }

        {
            System.err.println("---------------------------------");
            System.err.println("2");

            Convex base3 = new Convex(
                    Point(500d, 100d, 0d),
                    Point(600d, 100d, 0d),
                    Point(600d, 200d, 0d),
                    Point(500d, 200d, 0d)
            );
            Convex modRightTop = new Convex(
                    Point(650d, 120d, 0d),
                    Point(560d, 120d, 0d),
                    Point(550d, 130d, 0d),
                    Point(560d, 140d, 0d),
                    Point(650d, 140d, 0d)
            );
            ConvexBooleanOperation2D bo3 = new ConvexBooleanOperation2D(base3, modRightTop);
            {
                SubDivision2D s = bo3.getInAndOutIntersection().get(0);

                Assert.assertEquals(Point(600d, 120d, 0d), s.getIn());
                Assert.assertEquals(Point(600d, 140d, 0d), s.getOut());

                List<Point> expected = List.of(
                        Point(600d, 120d, 0d),
                        Point(560d, 120d, 0d),
                        Point(550d, 130d, 0d),
                        Point(560d, 140d, 0d),
                        Point(600d, 140d, 0d),
                        Point(600d, 200d, 0d),
                        Point(500d, 200d, 0d),
                        Point(500d, 100d, 0d),
                        Point(600d, 100d, 0d)
                );

                Assert.assertEquals(expected, bo3.subtractPoints(List.of(s)).get(0));
            }

        }

        {
            System.err.println("---------------------------------");
            System.err.println("3");

            Convex base6 = new Convex(
                    Point(200d, 300d, 0d),
                    Point(300d, 300d, 0d),
                    Point(300d, 400d, 0d),
                    Point(200d, 400d, 0d)
            );
            Convex modTopLeft = new Convex(
                    Point(220d, 250d, 0d),
                    Point(240d, 250d, 0d),
                    Point(240d, 340d, 0d),
                    Point(230d, 350d, 0d),
                    Point(220d, 340d, 0d)
            );
            ConvexBooleanOperation2D bo6 = new ConvexBooleanOperation2D(base6, modTopLeft);
            {
                SubDivision2D s = bo6.getInAndOutIntersection().get(0);

                Assert.assertEquals(Point(220d, 300d, 0d), s.getIn());
                Assert.assertEquals(Point(240d, 300d, 0d), s.getOut());

                List<Point> expected = List.of(
                        Point(220d, 300d, 0d),
                        Point(220d, 340d, 0d),
                        Point(230d, 350d, 0d),
                        Point(240d, 340d, 0d),
                        Point(240d, 300d, 0d),
                        Point(300d, 300d, 0d),
                        Point(300d, 400d, 0d),
                        Point(200d, 400d, 0d),
                        Point(200d, 300d, 0d)
                );

                Assert.assertEquals(expected, bo6.subtractPoints(List.of(s)).get(0));
            }
        }
        {
            System.err.println("---------------------------------");
            System.err.println("4");
//
            Convex modLongHorizonal = new Convex(
                    Point(150d, 520d, 0d),
                    Point(800d, 520d, 0d),
                    Point(850d, 530d, 0d),
                    Point(800d, 540d, 0d),
                    Point(150d, 540d, 0d)
            );
            Convex base12 = new Convex(
                    Point(200d, 500d, 0d),
                    Point(300d, 500d, 0d),
                    Point(300d, 600d, 0d),
                    Point(200d, 600d, 0d)
            );
            ConvexBooleanOperation2D bo12 = new ConvexBooleanOperation2D(base12, modLongHorizonal);
            {
                List<SubDivision2D> subdivisions = bo12.getInAndOutIntersection();
                SubDivision2D s0 = subdivisions.get(0);
                SubDivision2D s1 = subdivisions.get(1);

                Assert.assertEquals(Point(200d, 540d, 0d), s0.getIn());
                Assert.assertEquals(Point(300d, 540d, 0d), s0.getOut());

                Assert.assertEquals(Point(300d, 520d, 0d), s1.getIn());
                Assert.assertEquals(Point(200d, 520d, 0d), s1.getOut());

                List<List<Point>> results = bo12.subtractPoints(List.of(s0, s1));
                Assert.assertEquals(2, results.size());

                List<Point> expected1 = List.of(
                        Point(200d, 540d, 0d),
                        Point(300d, 540d, 0d),
                        Point(300d, 600d, 0d),
                        Point(200d, 600d, 0d)
                );
                List<Point> expected2 = List.of(
                        Point(200d, 520d, 0d),
                        Point(300d, 520d, 0d),
                        Point(300d, 500d, 0d),
                        Point(200d, 500d, 0d)
                );

                Assert.assertTrue(expected1.containsAll(results.get(0)) && results.get(0).containsAll(expected1));
                Assert.assertTrue(expected2.containsAll(results.get(1)) && results.get(1).containsAll(expected2));

                List<Point> expectedIntersection = List.of(
                        Point(200.0, 540.0, 0.0),
                        Point(300.0, 540.0, 0.0),
                        Point(300.0, 520.0, 0.0),
                        Point(200.0, 520.0, 0.0)
                );

                Assert.assertEquals(expectedIntersection, bo12.intersectPoints(subdivisions));
            }
        }
        {
            System.err.println("---------------------------------");
            System.err.println("5");

            Convex base = new Convex(Point(200.0, 0.0, -0.0), Point(200.0, 100.0, -0.0), Point(100.0, 100.0, -0.0), Point(100.0, 0.0, -0.0));

            Convex modifier = new Convex(
                    Point(196.55401273743276, 52.88151699394873, 0.0),
                    Point(211.28350114117755, 66.410824861092115, 0.0),
                    Point(173.904588797036, 107.10560846156879, -71.05427357601002E-15),
                    Point(159.1751003932911, 93.57630059442554, -71.05427357601002E-15)
            );
            ConvexBooleanOperation2D bo = new ConvexBooleanOperation2D(base, modifier);
            {
                SubDivision2D s0 = bo.getInAndOutIntersection().get(0);
                SubDivision2D s1 = bo.getInAndOutIntersection().get(1);

                Assert.assertEquals(Point(200.0, 56.04672010919568, 0.0), s0.getIn());
                Assert.assertEquals(Point(166.1686441009731, 100.0, 0.0), s0.getOut());

                Assert.assertEquals(Point(180.43122175432882, 100.0, 0.0), s1.getIn());
                Assert.assertEquals(Point(200.0, 78.69528174866444, 0.0), s1.getOut());

                List<List<Point>> results = bo.subtractPoints(List.of(s0, s1));
                Assert.assertEquals(2, results.size());

                List<Point> expected1 = List.of(
                        Point(200.0, 56.04672010919568, 0.0),
                        Point(196.55401273743276, 52.88151699394873, 0.0),
                        Point(159.1751003932911, 93.57630059442554, 0.0),
                        Point(166.1686441009731, 100.0, 0.0),
                        Point(100d, 100d, 0d),
                        Point(100d, 0d, 0d),
                        Point(200d, 0d, 0d)
                );
                List<Point> expected2 = List.of(
                        Point(180.43122175432882, 100.0, 0.0),
                        Point(200.0, 78.69528174866444, 0.0),
                        Point(200d, 100d, 0d)
                );

                results.get(0).forEach((t) -> {
                    System.err.println(t);
                });
                Assert.assertTrue(expected1.containsAll(results.get(0)) && results.get(0).containsAll(expected1));
                Assert.assertTrue(expected2.containsAll(results.get(1)) && results.get(1).containsAll(expected2));

            }
        }

        {
            System.err.println("---------------------------------");
            System.err.println("6");

            Convex base = new Convex(Point(166.66666666666666, 531.556638036695, 0.0),
                    Point(180.51132028455075, 458.33333333332484, 0.0),
                    Point(298.14164423266885, 458.3333333333249, 0.0),
                    Point(334.93594617544795, 581.7307204913039, 0.0));

            Convex modifier = new Convex(
                    Point(166.66666666666666, 385.1100286299718, 0.0),
                    Point(204.1456223588238, 583.3333333333301, 0.0),
                    Point(268.86261648338495, 556.5266766509163, 0.0),
                    Point(334.9359461754573, 334.9359461754573, 0.0)
            );
            ConvexBooleanOperation2D bo = new ConvexBooleanOperation2D(base, modifier);
            {
                SubDivision2D s0 = bo.getInAndOutIntersection().get(0);
                SubDivision2D s1 = bo.getInAndOutIntersection().get(1);

                Assert.assertEquals(Point(180.51132028455075, 458.33333333332484, 0.0), s0.getIn());
                Assert.assertEquals(Point(196.01030056242755, 540.3062440901691, 0.0), s0.getOut());

                Assert.assertEquals(Point(261.1386607759931, 559.7260438600872, 0.0), s1.getIn());
                Assert.assertEquals(Point(298.14164423273655, 458.3333333333249, 0.0), s1.getOut());

                List<List<Point>> results = bo.subtractPoints(List.of(s0, s1));
                Assert.assertEquals(2, results.size());

                List<Point> expected1 = List.of(
                        Point(180.51132028455075, 458.33333333332484, 0.0),
                        Point(196.01030056242755, 540.3062440901691, 0.0),
                        Point(166.66666666666666, 531.556638036695, 0.0)
                );
                List<Point> expected2 = List.of(
                        Point(261.1386607759931, 559.7260438600872, 0.0),
                        Point(268.86261648338495, 556.5266766509163, 0.0),
                        Point(298.14164423273655, 458.3333333333249, 0.0),
                        Point(298.14164423266885, 458.3333333333249, 0.0),
                        Point(334.93594617544795, 581.7307204913039, 0.0)
                );
                Assert.assertTrue(expected1.containsAll(results.get(0)) && results.get(0).containsAll(expected1));
                Assert.assertTrue(expected2.containsAll(results.get(1)) && results.get(1).containsAll(expected2));

            }
        }

        {
            System.err.println("---------------------------------");
            System.err.println("7");

            Convex base = new Convex(Point(166.66666666666666, 791.6666666666666, 0.0),
                    Point(125.0, 750.0, 0.0),
                    Point(166.66666666666666, 708.3333333333333, 0.0),
                    Point(245.7497260816573, 729.6507049717515, 0.0),
                    Point(269.3562822431892, 749.1312351799947, 0.0));

            Convex modifier = new Convex(
                    Point(208.33333333333331, 750.0, 0.0),
                    Point(166.66666666666666, 708.3333333333333, 0.0),
                    Point(125.0, 750.0, 0.0),
                    Point(166.66666666666666, 791.6666666666666, 0.0)
            );
            ConvexBooleanOperation2D bo = new ConvexBooleanOperation2D(base, modifier);
            {
                SubDivision2D s0 = bo.getInAndOutIntersection().get(0);

                Assert.assertEquals(Point(166.66666666666666, 791.6666666666666, 0.0), s0.getIn());
                Assert.assertEquals(Point(166.66666666666666, 708.3333333333333, 0.0), s0.getOut());

                List<List<Point>> results = bo.subtractPoints(List.of(s0));
                Assert.assertEquals(1, results.size());

                List<Point> expected1 = List.of(
                        Point(166.66666666666666, 791.6666666666666, 0.0),
                        Point(208.33333333333331, 750.0, 0.0),
                        Point(166.66666666666666, 708.3333333333333, 0.0),
                        Point(166.66666666666666, 708.3333333333333, 0.0),
                        Point(245.7497260816573, 729.6507049717515, 0.0),
                        Point(269.3562822431892, 749.1312351799947, 0.0),
                        Point(166.66666666666666, 791.6666666666666, 0.0)
                );

                Assert.assertTrue(expected1.containsAll(results.get(0)) && results.get(0).containsAll(expected1));

            }
        }

        {
            System.err.println("---------------------------------");
            System.err.println("8");

            Convex base = new Convex(Point(0.0, 0.0, 0.0),
                    Point(100.0, 0.0, 0.0),
                    Point(100.0, 100.0, 0.0),
                    Point(0.0, 100.0, 0.0));

            Convex modifier = new Convex(
                    Point(50.0, 25.0, 0.0),
                    Point(150.0, 25.0, 0.0),
                    Point(150.0, 50.0, 0.0),
                    Point(50.0, 50.0, 0.0)
            );
            ConvexBooleanOperation2D bo = new ConvexBooleanOperation2D(base, modifier);
            {
                SubDivision2D s0 = bo.getInAndOutIntersection().get(0);

                Assert.assertEquals(Point(100.0, 25.0, 0.0), s0.getIn());
                Assert.assertEquals(Point(100.0, 50.0, 0.0), s0.getOut());

                List<List<Point>> results = bo.joinPoints(List.of(s0));
                System.err.println("RESULTS");
                results.get(0).forEach((t) -> {
                    System.err.println(t);
                });
                System.err.println("---");
//                Assert.assertEquals(1, results.size());
//
//                List<Point> expected1 = List.of(
//                        Point(0.0, 0.0, 0.0),
//                        Point(100.0, 0.0, 0.0),
//                        Point(100.0, 25.0, 0.0),
//                        Point(150.0, 25.0, 0.0),
//                        Point(150.0, 50.0, 0.0),
//                        Point(100.0, 50.0, 0.0),
//                        Point(100.0, 100.0, 0.0),
//                        Point(0.0, 100.0, 0.0)
//                );
//
//                Assert.assertTrue(expected1.containsAll(results.get(0)) && results.get(0).containsAll(expected1));

            }
        }
        {
            System.err.println("---------------------------------");
            System.err.println("9");

            Convex modifier = new Convex(Point(0.0, 0.0, 0.0),
                    Point(100.0, 0.0, 0.0),
                    Point(100.0, 100.0, 0.0),
                    Point(0.0, 100.0, 0.0));

            Convex base = new Convex(
                    Point(50.0, 25.0, 0.0),
                    Point(150.0, 25.0, 0.0),
                    Point(150.0, 50.0, 0.0),
                    Point(50.0, 50.0, 0.0)
            );
            ConvexBooleanOperation2D bo = new ConvexBooleanOperation2D(base, modifier);
            {
                SubDivision2D s0 = bo.getInAndOutIntersection().get(0);

                Assert.assertEquals(Point(100.0, 25.0, 0.0), s0.getOut());
                Assert.assertEquals(Point(100.0, 50.0, 0.0), s0.getIn());

                List<List<Point>> results = bo.joinPoints(List.of(s0));
                System.err.println("RESULTS");
                results.get(0).forEach((t) -> {
                    System.err.println(t);
                });
                System.err.println("---");
                Assert.assertEquals(1, results.size());

                List<Point> expected1 = List.of(
                        Point(0.0, 0.0, 0.0),
                        Point(100.0, 0.0, 0.0),
                        Point(100.0, 25.0, 0.0),
                        Point(150.0, 25.0, 0.0),
                        Point(150.0, 50.0, 0.0),
                        Point(100.0, 50.0, 0.0),
                        Point(100.0, 100.0, 0.0),
                        Point(0.0, 100.0, 0.0)
                );

                Assert.assertTrue(expected1.containsAll(results.get(0)) && results.get(0).containsAll(expected1));

            }
        }

        {
            System.err.println("---------------------------------");
            System.err.println("10");

            Convex base = new Convex(Point(0.0, 0.0, 0.0),
                    Point(100.0, 0.0, 0.0),
                    Point(100.0, 100.0, 0.0),
                    Point(0.0, 100.0, 0.0));

            Convex modifier = new Convex(
                    Point(-50.0, 25.0, 0.0),
                    Point(150.0, 25.0, 0.0),
                    Point(150.0, 50.0, 0.0),
                    Point(-50.0, 50.0, 0.0)
            );
            ConvexBooleanOperation2D bo = new ConvexBooleanOperation2D(base, modifier);
            {
                SubDivision2D s0 = bo.getInAndOutIntersection().get(0);

                Assert.assertEquals(Point(0.0, 50.0, 0.0), s0.getIn());
                Assert.assertEquals(Point(100.0, 50.0, 0.0), s0.getOut());

                SubDivision2D s1 = bo.getInAndOutIntersection().get(1);

                Assert.assertEquals(Point(0.0, 25.0, 0.0), s1.getOut());
                Assert.assertEquals(Point(100.0, 25.0, 0.0), s1.getIn());

                List<List<Point>> results = bo.joinPoints(List.of(s0, s1));
                Assert.assertEquals(1, results.size());
////
                List<Point> expected1 = List.of(
                        Point(0.0, 0.0, 0.0),
                        Point(100.0, 0.0, 0.0),
                        Point(100.0, 25.0, 0.0),
                        Point(150.0, 25.0, 0.0),
                        Point(150.0, 50.0, 0.0),
                        Point(100.0, 50.0, 0.0),
                        Point(100.0, 100.0, 0.0),
                        Point(0.0, 100.0, 0.0),
                        Point(0.0, 50.0, 0.0),
                        Point(-50.0, 50.0, 0.0),
                        Point(-50.0, 25.0, 0.0),
                        Point(0.0, 25.0, 0.0)
                );
//
                Assert.assertTrue(expected1.containsAll(results.get(0)) && results.get(0).containsAll(expected1));

            }
        }

    }

};
