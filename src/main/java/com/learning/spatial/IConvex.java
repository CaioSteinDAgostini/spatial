/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.learning.spatial;

import data.Pair;
import data.Triple;
import data.Tuple;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 *
 * @author caio
 */
public interface IConvex {

    public final double CONVEX_PRECISION = 0.1d;

    /**
     * The vectors that form the external limits of the convex
     *
     * @return
     */
    public List<Vector> getVectors();

    /**
     * returns a reference to the actual list of points. Editing this list will
     * edit the convex's points
     *
     * @return
     */
    public int size();

    /**
     *
     * @param index
     * @return
     */
    public Point get(int index);

    /**
     *
     * @return
     */
    public List<Point> getPoints();

    /**
     *
     * @return
     */
    public default Point calculateCentroid() {
        return Utils.calculateCentroid(this.getPoints());
    }

    /**
     *
     * @param <T>
     * @param s
     * @return
     * @throws InvalidCoordinatesException
     */
    /**
     *
     * @param convex
     * @return
     */
    public default boolean collides(IConvex convex) {
        return GJK.collides(this.getPoints(), convex.getPoints());
    }

    public default boolean collides2D(IConvex convex) {
        return GJK.collides(this.getPoints(), convex.getPoints(), true);
    }

    public default boolean collides(Point point) {
        ArrayList<Point> list = new ArrayList<>(1);
        list.add(point);
        return GJK.collides(this.getPoints(), list);
    }

    public default boolean collides(Vector vector) {
        ArrayList<Point> list = new ArrayList<>(2);
        list.add(vector.getP0());
        list.add(vector.getP1());
        return GJK.collides(this.getPoints(), list);
    }

    public default boolean collides2D(Vector vector) {
        ArrayList<Point> list = new ArrayList<>(2);
        list.add(vector.getP0());
        list.add(vector.getP1());
        return GJK.collides(this.getPoints(), list, true);
    }

    public default boolean collides2D(Point point) {
        ArrayList<Point> list = new ArrayList<>(1);
        list.add(point);
        return GJK.collides(this.getPoints(), list, true);
    }

    public default List<Point> getPointsClockwise() {
        if (this.getOrientation() == Orientation.CLOCKWISE) {
            return this.getPoints();
        } else {
            List<Point> answer = this.getPoints();
            Collections.reverse(answer);
            return answer;
        }
    }

    public default List<Vector> getVectorsClockwise() {
        if (this.getOrientation() == Orientation.CLOCKWISE) {
            return this.getVectors();
        } else {
            List<Vector> answer = this.getVectors();
            answer = answer.stream().map((vector) -> {
                return new Vector(vector.getP1(), vector.getP0());
            }).collect(Collectors.toList());
            Collections.reverse(answer);
            return answer;
        }
    }

    public default List<Point> getPointsCounterClockwise() {
        if (this.getOrientation() == Orientation.COUNTERCLOCKWISE) {
            return this.getPoints();
        } else {
            List<Point> answer = this.getPoints();
            Collections.reverse(answer);
            return answer;
        }
    }

    public default List<Vector> getVectorsCounterClockwise() {
        if (this.getOrientation() == Orientation.COUNTERCLOCKWISE) {
            return this.getVectors();
        } else {
            List<Vector> answer = this.getVectors();
            answer = answer.stream().map((vector) -> {
                return new Vector(vector.getP1(), vector.getP0());
            }).collect(Collectors.toList());
            Collections.reverse(answer);
            return answer;
        }
    }

    public boolean contains(Point point);

    public default double area2D() {
        List<Point> points = this.getPoints();
        return Utils.area2D(points);
    }

    public Orientation getOrientation();

    public static List<List<Point>> breakIntoConvexes(Point... points) {
        return breakIntoConvexes(Arrays.asList(points));
    }

    public static List<List<Point>> breakIntoConvexes(List<Point> polygonPoints) {

        if (polygonPoints.isEmpty()) {
            throw new InvalidParameterException();
        }

        Orientation orientationTmp = Orientation.getOrientation2D(polygonPoints);
        List<Point> points = removeDoublePoints(polygonPoints, true);

        if (validatePoints(points)) {
            return List.of(points);
        }

        if (points.size() > 2) {
            Map<Integer, Vector> bissectors = new HashMap<>(points.size());
            List<Vector> vectors = new ArrayList<>(points.size() - 1);
            List<Integer> concavePointsIndexes = new ArrayList<>(polygonPoints.size());
            final Double length = IntStream.range(0, points.size()).boxed().mapToDouble((index) -> {
                Point previous = points.get((points.size() - 1 + index) % points.size());
                Point current = points.get((index) % points.size());
                Point next = points.get((index + 1) % points.size());

                Vector v0 = new Vector(previous, current);
                Vector v1 = new Vector(current, next);
                Double angle = v0.angle2D(v1);
                if (angle != null) {
                    if ((orientationTmp == Orientation.COUNTERCLOCKWISE && angle > 0.00) || (orientationTmp == Orientation.CLOCKWISE && angle < 0.00)) {
                        Vector bissector;
                        bissector = Vector.bissector2D(previous, current, next);
                        bissector.rotateZ(180);
                        bissectors.put(index, bissector);
                        concavePointsIndexes.add(index);
                    }
                    vectors.add(v0);
                    return v0.getLength2D();
                } else {
                    return 0d;
                }
            }).sum();

            Optional<Triple<Integer, Triple<Point, Point, Integer>, Vector>> collision;
            collision = concavePointsIndexes.stream().map((index) -> {

                Vector bissector = bissectors.get(index);
                Optional<Triple<Point, Integer, Vector>> vectorAndObstacleIndexAndObstacleTriple = adjustToBoundary2D(bissector, length, vectors);
                if (vectorAndObstacleIndexAndObstacleTriple.isPresent()) {
                    Triple<Point, Integer, Vector> triple = vectorAndObstacleIndexAndObstacleTriple.get();
                    return Optional.of(new Triple<Integer, Triple<Point, Point, Integer>, Vector>(index, new Triple<>(bissector.getP0(), triple.getFirst(), triple.getSecond()), triple.getThird()));
                } else {
                    return Optional.<Triple<Integer, Triple<Point, Point, Integer>, Vector>>empty();
                }
            }).filter((optional) -> {
                return optional.isPresent();
            }).map((optional) -> {
                return optional.get();
            }).min((pair0, pair1) -> {
                Point pair00 = pair0.getSecond().getFirst();
                Point pair01 = pair0.getSecond().getSecond();

                Point pair10 = pair1.getSecond().getFirst();
                Point pair11 = pair1.getSecond().getSecond();

                return Double.compare(pair00.distance2D(pair01), pair10.distance2D(pair11));
            });

            if (collision.isPresent()) {

                int concavePointIndex = collision.get().getFirst();
                int obstacleIndex = collision.get().getSecond().getThird();
                Point concavePoint = collision.get().getSecond().getFirst();
                Point newPoint = collision.get().getSecond().getSecond();
                List<Point> part1 = new ArrayList<>(points.size() / 2 + 2);
                List<Point> part2 = new ArrayList<>(points.size() / 2 + 2);

//                a--------b
//                |        |       
//                |        |       
//                |        |       
//                x--------o       
//                |        |       
//                |        |       
//                d________c
//                    obstacle = 4, concavePoint =1
//                  4x_5
//                 / |  \6
//                /  |   |
//               3\  /\  /7
//                 \/ 1\/
//                 2   0
                Point last;
                int index = (obstacleIndex) % points.size();
                boolean goOn = true;
                part1.add(newPoint);
                last = newPoint;
                while (goOn) {
                    Point p = points.get(index);
                    if (!p.equals(last)) {
                        part1.add(p);
                        last = p;
                    }
                    index = (index + 1) % points.size();
                    if (index == concavePointIndex) {
                        goOn = false;
                    }
                }
                if (!concavePoint.equals(newPoint) && (!concavePoint.equals(last))) {
                    part1.add(concavePoint);
                }

                last = null;
                index = concavePointIndex % points.size();
                goOn = true;
                while (goOn) {
                    Point p = points.get(index);
                    if (!p.equals(last)) {
                        part2.add(p);
                        last = p;
                    }
                    index = (index + 1) % points.size();
                    if (index == obstacleIndex) {
                        goOn = false;
                    }
                }
                if (!concavePoint.equals(newPoint) && (!concavePoint.equals(last))) {
                    part2.add(newPoint);
                }

                return List.of(part1, part2).stream().map((recursivePoints) -> {
                    if (!(recursivePoints.containsAll(points) && points.containsAll(recursivePoints))) {
                        return breakIntoConvexes(recursivePoints);
                    } else {
                        return List.<List<Point>>of();
                    }
                }).flatMap(List::stream).distinct().collect(Collectors.toList());

            } else {
                throw new Concave2DException();
            }
        } else {
            return List.of();
        }
    }

    public static List<IConvex> breakIntoNSidedPolygons(final int sides, IConvex convex) {
        if (sides < 3) {
            throw new InvalidParameterException();
        }

        List<Point> polygonPoints = convex.getPointsClockwise();
        List<Point> filteredPoints = IntStream.range(0, polygonPoints.size()).boxed().map((index) -> {
            Point p0 = polygonPoints.get(index);
            Point p1 = polygonPoints.get((index + 1) % polygonPoints.size());
            Point p2 = polygonPoints.get((index + 2) % polygonPoints.size());

            if (p0.equals(p1)) {
                return null;
            } else {
                if (p1.equals(p2)) {
                    return p1;
                }
                Vector v0 = new Vector(p0, p1);
                Vector v1 = new Vector(p1, p2);

                double angle = v0.angle(v1);
                if (angle == 180 || angle == -180) {
                    return null;
                } else {
                    return p1;
                }
            }
        }).filter((point) -> {
            return point != null;
        }).collect(Collectors.toList());

        List<Point> points = removeDoublePoints(filteredPoints, true);

        if (points.size() > sides) {

            Map<Integer, Vector> bissectors = new HashMap<>(points.size());
            List<Vector> vectors = convex.getVectorsClockwise();

            Optional<Triple<Integer, Pair<Vector, Double>, Integer>> optionalLargestSide = IntStream.range(0, points.size()).boxed().map((index) -> {
                Point previous = points.get((points.size() - 1 + index) % points.size());
                Point current = points.get((index) % points.size());
                Point next = points.get((index + 1) % points.size());

                Vector v0 = new Vector(previous, current);
                Vector v1 = new Vector(current, next);
                Double angle = v0.angle2D(v1);
                Vector bissector;
                if (angle != 0) {
                    bissector = Vector.bissector2D(previous, current, next);
                } else {
                    bissector = new Vector(current, next);
                    bissector.rotateZ(90);
                }
                if (angle < 0) {
                    bissector.rotateZ(180);
                }
                bissectors.put(index, bissector);
                Optional<Triple<Point, Integer, Vector>> vectorAndObstacleIndexAndObstaclePair = adjustToBoundary2D(bissector, convex.getPerimeter() * 3, vectors);

                try {
                    Vector adjustedBissector = new Vector(bissector.getP0(), vectorAndObstacleIndexAndObstaclePair.get().getFirst());
                    Integer brokenSideIndex = vectorAndObstacleIndexAndObstaclePair.get().getSecond();

                    return new Triple<Integer, Pair<Vector, Double>, Integer>(index, new Pair<>(adjustedBissector, Math.abs(angle)), brokenSideIndex);
                } catch (Exception e) {
                    System.err.println("CONVEX SIZE " + convex);
                    convex.getPoints().forEach((t) -> {
                        System.err.println(t + ",");
                    });
                    System.err.println("--");
                    throw e;
                }
            }).min((triple0, triple1) -> {
                return Double.compare(triple0.getSecond().getSecond(), triple1.getSecond().getSecond());
            });

            if (optionalLargestSide.isPresent()) {
                int breakPointIndex = optionalLargestSide.get().getFirst();
                Vector bissector = optionalLargestSide.get().getSecond().getFirst();//bissectors.get(concavePointIndex);
                int obstacleIndex = optionalLargestSide.get().getThird();

                Point newPoint = bissector.getP1();//bissector.lineIntersection2D(vectors.get(obstacleIndex));
                List<Point> part1 = new ArrayList<>(points.size() / 2 + 2);
                List<Point> part2 = new ArrayList<>(points.size() / 2 + 2);

                int index = (obstacleIndex) % points.size();
                boolean goOn = true;
                part1.add(newPoint);
                while (goOn) {
                    part1.add(points.get(index));
                    index = (index + 1) % points.size();
                    if (index == breakPointIndex) {
                        goOn = false;
                    }
                }
                part1.add(bissector.getP0());

                index = breakPointIndex % points.size();
                goOn = true;
                while (goOn) {
                    part2.add(points.get(index));
                    index = (index + 1) % points.size();
                    if (index == obstacleIndex) {
                        goOn = false;
                    }
                }
                part2.add(newPoint);

                return List.of(part1, part2).stream().filter((t) -> {
                    return t.size() > 2
                            && (!(t.containsAll(points) && points.containsAll(t)));
                }).map((recursivePoints) -> {
                    return breakIntoNSidedPolygons(sides, new Convex(recursivePoints));
                }).flatMap(List::stream).distinct().collect(Collectors.toList());

            } else {
                return List.of(new Convex(points));
            }
        } else {
            return List.of(new Convex(points));
        }
    }

    //also exists in Hole
    public static List<Point> breakSelfCrossings(List<Point> points) {
//    public static Graph<Point, Vector> breakSelfCrossings(List<Vector> vectors) {
        Set<Point> mergedPoints = new LinkedHashSet<>();

        String POINT = "point";
        String EDGE = "edge";

//        Graph<Point, Vector> graph = new Graph<>();
        data.Graph g = new data.Graph(false);
        g.addNodeProperty(POINT, Point.class, false, null);
        g.setNodeKeyByColumns(POINT, POINT);
        g.addEdgeProperty(EDGE, Vector.class, false, null);

//        List<Vector> filtered = new LinkedList<>();
        Map<Integer, List<Point>> map = new HashMap<>();
        IntStream.range(0, points.size()).boxed().forEach((mainIndex) -> {

            Point mainCurrent = points.get((mainIndex) % points.size());
            Point mainNext = points.get((mainIndex + 1) % points.size());

            Vector main = new Vector(mainCurrent, mainNext);
            map.putIfAbsent(mainIndex, new LinkedList<>());

            Point.mergePoints(main.getP0(), mergedPoints);
            Point.mergePoints(main.getP1(), mergedPoints);
            map.get(mainIndex).add(main.getP0());
            map.get(mainIndex).add(main.getP1());

            IntStream.range(mainIndex + 1, points.size()).boxed().filter((otherIndex) -> {
                return !Objects.equals(mainIndex, otherIndex) && ((((mainIndex + 1) % points.size()) != otherIndex && mainIndex != ((otherIndex + 1) % points.size())));
            }).forEach((otherIndex) -> {

                Point otherCurrent = points.get((otherIndex) % points.size());
                Point otherNext = points.get((otherIndex + 1) % points.size());
                Vector other = new Vector(otherCurrent, otherNext);

                Point.mergePoints(other.getP0(), mergedPoints);
                Point.mergePoints(other.getP1(), mergedPoints);
                map.putIfAbsent(otherIndex, new LinkedList<>());
                map.get(otherIndex).add(other.getP1());

                Point intersection = main.segmentIntersection2D(other);
                Point.mergePoints(intersection, mergedPoints);
                if (intersection != null) {
                    map.get(mainIndex).add(intersection);
                    map.get(otherIndex).add(intersection);
                    Tuple node = g.searchNode(POINT, intersection);
                    if (node == null) {
                        node = g.addNode(intersection);
                    }
                }
            });

        });

        List<Point> answer = new LinkedList<>();
        map.keySet().stream().sorted((index0, index1) -> {
            return Integer.compare(index0, index1); //reverse sorting
        }).forEach((index) -> {

            Point currentP = points.get((index) % points.size());
            Point nextP = points.get((index + 1) % points.size());
            Vector vector = new Vector(currentP, nextP);
            List<Point> list = map.get(index);

            List<Point> reordered = list.stream().sorted((point0, point1) -> {
                return Double.compare(vector.getP0().distance2D(point0), vector.getP0().distance2D(point1));
            }).collect(Collectors.toList());

//            filtered.addAll(
            IntStream.range(0, reordered.size() - 1).boxed().map((pointIndex) -> {
                Point p0 = reordered.get(pointIndex);
                Point p1 = reordered.get((pointIndex + 1) % reordered.size());
                if (p0.equals(p1)) {
                    return null;
                } else {
                    Vector hbv = new Vector(p0, p1);
                    Tuple t0 = g.searchNode(POINT, p0);
                    if (t0 == null) {
                        t0 = g.addNode(p0);
                    }
                    Tuple t1 = g.searchNode(POINT, p1);
                    if (t1 == null) {
                        t1 = g.addNode(p1);
                    }
                    Iterator<Tuple> next = g.getFromToEdges(t0, t1);
                    if (!next.hasNext()) {
                        Tuple edge = g.addEdge(t0, t1);
                        edge.set(EDGE, hbv);

                        answer.add(p0);
//                        answer.add(new Vector(p0, p1));
                    }
                    return hbv;
                }
            }).filter((v) -> {
                return v != null;
            }).collect(Collectors.toList());
//            );
        });

//        filtered.forEach((t) -> {
//            graph.connect(t.getP0(), t.getP1(), t);
//        });
        return answer;
    }

    public static List<Point> removeColinearOverlappinPoints(List<Point> points) {

        List<Integer> toRemove = new ArrayList<>(points.size());
        IntStream.range(0, points.size()).boxed().forEach((index) -> {
            Point previous = points.get((points.size() - 1 + index) % points.size());
            Point current = points.get((index) % points.size());
            Point next = points.get((index + 1) % points.size());

            if (previous.equals(current) || current.equals(next)) {
                toRemove.add(0, index);
            } else {
                Vector v0 = new Vector(previous, current);
                Vector v1 = new Vector(current, next);

                double angle = v0.angle2D(v1);
                if (angle == 0 || angle == -0 || angle == 180 || angle == -180) {
                    toRemove.add(0, index); //add in the beginning to make it reverse
                }
            }
        });

        List<Point> answer = new ArrayList<>(points);

        toRemove.forEach((index) -> {
            answer.remove(index.intValue());
        });

        return answer;

    }

    public static List<Point> removeDoublePoints(List<Point> points, boolean mergeOverlappingPoints) {

        if (points.size() > 1) {
            List<Point> copy = new ArrayList<>(points);

            if (mergeOverlappingPoints) {
                Map<Point, List<Point>> flatennedPointsMap = points.stream().collect(Collectors.groupingBy((point) -> {
                    return point.get2D();
                }));
                flatennedPointsMap.entrySet().stream().filter((entry) -> {
                    return entry.getValue().size() > 1;
                }).forEach((entry) -> {
                    List<Point> values = entry.getValue();
                    OptionalDouble averageZ = values.stream().mapToDouble((point) -> {
                        return point.getZ();
                    }).average();
                    if (averageZ.isPresent()) {
                        values.stream().forEach((point) -> {
                            point.setZ(averageZ.getAsDouble());
                        });
                    }
                });
            }

            List<Point> answer = IntStream.range(0, copy.size()).boxed().map((index) -> {
                Point previous = copy.get((copy.size() - 1 + index) % copy.size());
                Point current = points.get((index) % points.size());

                if (!previous.equals(current)) {
                    return current;
                } else {
                    return null;
                }
            }).filter((point) -> {
                return point != null;
            }).collect(Collectors.toList());

            return answer;
        } else {
//            return List.of();
            return List.copyOf(points);
        }
    }

    //first in the pair is the adjusted vector, the second is the vector it collided with
    private static Optional<Triple<Point, Integer, Vector>> adjustToBoundary2D(Vector vector, double perimeter, List<Vector> sides) {
        if (vector != null) {
            Vector vector2D = vector.get2D();
            Vector normalizedVector = vector.normalized();
            Vector adjustedBissector = normalizedVector.multiplied(perimeter, perimeter, 1);

            Optional<Triple<Point, Integer, Vector>> boundaryIntersection = IntStream.range(0, sides.size()).boxed().map((sideIndex) -> {
                Vector boundaryVector = sides.get(sideIndex).get2D();
                if ((!boundaryVector.getP0().equals(vector2D.getP0())) && (!boundaryVector.getP1().equals(vector2D.getP0()))) { //if it is not the previous or the next vector
                    return new Triple<>(adjustedBissector.segmentIntersection2D(boundaryVector), sideIndex, boundaryVector);
                } else {
                    return null;
                }
            }).filter((triple) -> {
                return triple != null && triple.getFirst() != null;
            }).min((triple0, triple1) -> {
                return Double.compare(triple0.getFirst().distance2D(adjustedBissector.getP0()), triple1.getFirst().distance2D(adjustedBissector.getP0()));
            });

            return boundaryIntersection;
        } else {
            throw new InvalidParameterException();
        }
    }

    public static boolean validatePoints(List<Point> points) {
        if (points.size() < 3) {
            return false;
        }

        int sum = 0;
        int zeros = 0;
        for (int index = 0; index < points.size(); index++) {
            Point previous = points.get((points.size() - 1 + index) % points.size());
            Point current = points.get((index) % points.size());
            Point next = points.get((index + 1) % points.size());

            double dx1 = current.getX() - previous.getX();
            double dy1 = current.getY() - previous.getY();
            double dx2 = next.getX() - current.getX();
            double dy2 = next.getY() - current.getY();

            double cross = dx1 * dy2 - dy1 * dx2;
            cross = (Math.round(cross / Point.RESOLUTIO)) * Point.RESOLUTIO;
//            if (cross == 0) {
//                zeros += 1;
//            }
//            if (cross < 0) {
//                sum -= 1;
//            }
//            if (cross > 0) {
//                sum += 1;
//            }
            if (cross <= CONVEX_PRECISION && cross >= -CONVEX_PRECISION) {
                zeros += 1;
            }
            if (cross < CONVEX_PRECISION) {
                sum -= 1;
            }
            if (cross > -CONVEX_PRECISION) {
                sum += 1;
            }
        }
        return sum + zeros == points.size() || -sum + zeros == points.size();

    }

    public void move(Point translation);

    public IConvex moved(Point translation);

    public Double getPerimeter();
}
