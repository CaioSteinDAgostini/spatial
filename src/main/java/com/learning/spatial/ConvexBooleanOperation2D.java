package com.learning.spatial;

import data.Pair;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 *
 * @author caio
 */
public class ConvexBooleanOperation2D  {

    IConvex base;
    IConvex modifier;
    List<Vector> modifierVectors;
    List<Vector> baseVectors;

    List<Point> basePoints;
    List<Point> modifierPoints;

    public ConvexBooleanOperation2D(List<Point> base, List<Point> modifier) throws InvalidGeometryException {
        this.base = new Convex(base);
        this.modifier = new Convex(modifier);
        modifierVectors = this.modifier.getVectorsCounterClockwise();
        baseVectors = this.base.getVectorsClockwise();

        basePoints = this.base.getPointsClockwise();
        modifierPoints = this.modifier.getPointsCounterClockwise();
    }

    public ConvexBooleanOperation2D(IConvex base, IConvex modifier) throws InvalidGeometryException {
        this.base = base;
        this.modifier = modifier;
        modifierVectors = this.modifier.getVectorsCounterClockwise();
        baseVectors = this.base.getVectorsClockwise();

        basePoints = this.base.getPointsClockwise();
        modifierPoints = this.modifier.getPointsCounterClockwise();
    }

    

    enum IntersectionType {
        CROSSING,
        NONE,
        OUT,
        VOID
    }

    public List<List<Point>> subtractPoints(List<SubDivision2D> subdivisions) {
        List<List<Point>> answer = new ArrayList<>();

        CircularList<Point> modifierList = new CircularList<>(modifier.getPointsCounterClockwise());
        CircularList<Point> baseList = new CircularList<>(base.getPointsClockwise());

        if (subdivisions.isEmpty()) {
            return List.of(this.basePoints);
        }
        if (subdivisions.size() == 1) {
            SubDivision2D sd = subdivisions.get(0);
            Point in = sd.getIn();
            Point out = sd.getOut();
            if (sd.getInModifierIndex().equals(sd.getOutModifierIndex())) {
                List<Point> list = new ArrayList<>();
                list.add(in);
                list.add(out);
                list.addAll(
                        baseList.subList(sd.getOutBaseIndex() + 1, sd.getInBaseIndex())
                );
                list.add(baseList.get(sd.getInBaseIndex()));
                answer.add(list);
            } else {
                List<Point> list = new ArrayList<>();
                list.add(in);
                list.addAll(modifierList.subList(sd.getInModifierIndex() + 1, sd.getOutModifierIndex() + 1));
                list.add(out);
                list.addAll(
                        baseList.subList(sd.getOutBaseIndex() + 1, sd.getInBaseIndex())
                );
                list.add(baseList.get(sd.getInBaseIndex()));
                answer.add(list);

            }
        }
        if (subdivisions.size() > 1) {
            for (int i = 0; i < subdivisions.size(); i++) {
                List<Point> list = new ArrayList<>();
                SubDivision2D s0 = subdivisions.get(i);

                if (s0.getInModifierIndex().equals(s0.getOutModifierIndex())) {
                    list.add(s0.getIn());
                    list.add(s0.getOut());
                    if (!s0.getInBaseIndex().equals(s0.getOutBaseIndex())) {
                        list.addAll(
                                baseList.subList(s0.getOutBaseIndex() + 1, s0.getInBaseIndex() + 1)
                        );
                    }
                } else {
                    list.add(s0.getIn());
                    list.addAll(
                            modifierList.subList(s0.getInModifierIndex() + 1, s0.getOutModifierIndex() + 1)
                    );
                    list.add(s0.getOut());
                    if (!s0.getInBaseIndex().equals(s0.getOutBaseIndex())) {
                        list.addAll(
                                baseList.subList(s0.getOutBaseIndex() + 1, s0.getInBaseIndex() + 1)
                        );
                    }
                }
                answer.add(list);
            }

        }
        return answer;

    }

    public List<SubDivision2D> getInAndOutIntersection() {
        List<SubDivision2D> answer = new ArrayList<>();

        List<Intersection> intersections = new ArrayList<>();

        Map<Point, List<Intersection>> map = new HashMap<>(); //the key should be the 'p0' point of a modifier vector
        for (int modifierIndex = 0; modifierIndex < modifierPoints.size(); modifierIndex++) {
            Point previousM = modifierPoints.get((modifierPoints.size() - 1 + modifierIndex) % modifierPoints.size()).get2D();
            Point m0 = modifierPoints.get(modifierIndex).get2D();
            Point m1 = modifierPoints.get((modifierIndex + 1) % modifierPoints.size()).get2D();
            Point nextM = modifierPoints.get((modifierIndex + 2) % modifierPoints.size()).get2D();
            Vector modifierVector = new Vector(m0, m1);
            Vector previousModifierVector = new Vector(previousM, m0);
            Vector nextModifierVector = new Vector(m1, nextM);
            boolean isM0Inside = base.collides2D(m0);
            boolean isM1Inside = base.collides2D(m1);

            Set<Intersection> intersectionsWithModifier = new HashSet<>();
            Set<Point> merge = new HashSet<>();

            for (int baseIndex = 0; baseIndex < basePoints.size(); baseIndex++) {

                Point b0 = basePoints.get(baseIndex).get2D();
                Point b1 = basePoints.get((baseIndex + 1) % basePoints.size()).get2D();
                Point previousB = basePoints.get((basePoints.size() - 1 + baseIndex) % basePoints.size()).get2D();
                Point nextB = basePoints.get((baseIndex + 2) % basePoints.size()).get2D();
                Vector baseVector = new Vector(b0, b1);
                Vector previousBaseVector = new Vector(previousB, b0);
                Vector nextBaseVector = new Vector(b1, nextB);

                List<Intersection> intersectionsList = new LinkedList<>();
                if (modifierVector.isColinear2D(baseVector)
                        || (b0.equals(m0) && Math.abs(previousBaseVector.angle2D(modifierVector)) == 180)
                        || (b1.equals(m1) && Math.abs(nextBaseVector.angle2D(modifierVector)) == 180)) {
                } else {
                    Intersection intersection = new Intersection(baseIndex, baseVector, modifierIndex, modifierVector);
                    intersection.merge(merge);
                    intersectionsList.add(intersection);
                }

                intersectionsList.forEach((intersection) -> {
                    if (intersection.isValid(previousModifierVector, nextModifierVector, previousBaseVector, nextBaseVector)) {
                        if (!intersectionsWithModifier.contains(intersection)) {
                            intersectionsWithModifier.add(intersection);
                            map.putIfAbsent(m0, new ArrayList<>());
                            map.get(m0).add(intersection);
                        }
                    }
                });

            }

            List<Intersection> sortedIntersections = intersectionsWithModifier.stream().sorted((s1, s2) -> {
                return Double.compare(m0.distance2D(s1.intersection), m0.distance2D(s2.intersection));
            }).collect(Collectors.toList());

            if (!sortedIntersections.isEmpty()) {
                int previousM0Index = (modifierIndex == 0) ? modifierPoints.size() - 1 : modifierIndex - 1;
                Point previousM0 = modifierPoints.get(previousM0Index);

                int nextM1Index = (modifierIndex == modifierPoints.size() - 1) ? 0 : modifierIndex + 1;
                Point nextM1 = modifierPoints.get(nextM1Index);

                long count = sortedIntersections.stream().map((intersection) -> {
                    return intersection.intersection;
                }).distinct().count();

                if (count == 1) {
                    Intersection intersection = sortedIntersections.get(0);
                    Point point = intersection.intersection;
                    if (point.equals(m0) || point.equals(m1)) {

                        Vector baseVector = baseVectors.get(intersection.baseIndex);

                        if (point.equals(m0)) {
                            //searches for the intersection that is closes to the end of the previous modifier (or beginning of current modifier)
                            Optional<Intersection> optionalPreviousIntersection = map.getOrDefault(previousM0, List.of()).stream().min((intersection1, intersection2) -> {
                                return Double.compare(m0.distance2D(intersection1.intersection), m0.distance2D(intersection2.intersection));
                            });
                            Point previousEvalPoint = (optionalPreviousIntersection.isPresent()) ? optionalPreviousIntersection.get().intersection : previousM0;
                            boolean isPreviousEvalPointInside = base.collides2D(previousEvalPoint);

                            //searches for the intersection that is closes to the end of the previous modifier (or beginning of current modifier)
                            Optional<Intersection> optionalNextIntersection = map.getOrDefault(m0, List.of()).stream().min((intersection1, intersection2) -> {
                                return Double.compare(m0.distance2D(intersection1.intersection), m0.distance2D(intersection2.intersection));
                            });
                            Point nextEvalPoint = (optionalNextIntersection.isPresent()) ? optionalNextIntersection.get().intersection : m1;
                            boolean isNextEvalPointInside = base.collides2D(nextEvalPoint);

                            if (isPreviousEvalPointInside == isNextEvalPointInside) {
                                if (previousModifierVector.isColinear2D(baseVector)) {
                                    //do nothing
                                } else {
                                    if (nextEvalPoint.equals(point)) {
                                        if (isM1Inside) {
                                            if (previousModifierVector.isColinear2D(baseVector)) {
                                                intersection.in();
                                            }

                                        } else {
                                            intersection.out();
                                        }
                                    }
                                }
                            } else {
                                if (isPreviousEvalPointInside) {
                                    intersection.out();
                                } else {
                                    intersection.in();
                                }
                            }

                        }
                        if (point.equals(m1)) {
                            //searches for the intersection that is closes to the end of the previous modifier (or beginning of current modifier)
                            Optional<Intersection> optionalPreviousIntersection = map.getOrDefault(m1, List.of()).stream().min((intersection1, intersection2) -> {
                                return Double.compare(m1.distance2D(intersection1.intersection), m1.distance2D(intersection2.intersection));
                            });
                            Point previousEvalPoint = (optionalPreviousIntersection.isPresent()) ? optionalPreviousIntersection.get().intersection : m1;
                            boolean isPreviousEvalPointInside = base.collides2D(previousEvalPoint);

                            //searches for the intersection that is closes to the end of the previous modifier (or beginning of current modifier)
                            Optional<Intersection> optionalNextIntersection = map.getOrDefault(nextM1Index, List.of()).stream().min((intersection1, intersection2) -> {
                                return Double.compare(m1.distance2D(intersection1.intersection), m1.distance2D(intersection2.intersection));
                            });
                            Point nextEvalPoint = (optionalNextIntersection.isPresent()) ? optionalNextIntersection.get().intersection : nextM1;
                            boolean isNextEvalPointInside = base.collides2D(nextEvalPoint);

                            if (isPreviousEvalPointInside == isNextEvalPointInside) {
                                if (nextModifierVector.isColinear2D(baseVector)) {
                                    //do nothing
                                } else {
                                    if (nextEvalPoint.equals(point)) {
                                        if (isM1Inside) {
                                            if (nextModifierVector.isColinear2D(baseVector)) {
                                                intersection.out();
                                            }
                                        } else {
                                            intersection.in();
                                        }
                                    }
                                }
                            } else {
                                if (isPreviousEvalPointInside) {
                                    intersection.out();
                                } else {
                                    intersection.in();
                                }
                            }
                            //the intersection should never be stored intersecting with m1. It would be stored as m0 from the following modifier
                        }

                        if (isM0Inside && isM1Inside) {                            //that happens when the modifier has a sequence of vectors with the same points as the base
                            //do nothing
                            Integer baseIndex = intersection.baseIndex;
                            Point previousB = basePoints.get((basePoints.size() - 1 + baseIndex) % basePoints.size()).get2D();
                            Point b0 = basePoints.get(baseIndex).get2D();
                            Point b1 = basePoints.get((baseIndex + 1) % basePoints.size()).get2D();
                            Point nextB = basePoints.get((baseIndex + 2) % basePoints.size()).get2D();

                            Vector previousBaseVector = new Vector(previousB, b0);
                            Vector nextBaseVector = new Vector(b1, nextB);

                            if ((!modifierVector.isColinear2D(nextBaseVector)) && nextModifierVector.isColinear2D(baseVector)) {
                                intersection.out();
                            }

                            if ((!modifierVector.isColinear2D(previousBaseVector)) && previousModifierVector.isColinear2D(baseVector)) {
                                intersection.in();
                            }
                        }
                    } else {
                        if (isM0Inside && !isM1Inside) {
                            intersection.out();
                        }
                        if (isM1Inside && !isM0Inside) {
                            intersection.in();
                        }
                        if (!isM0Inside && !isM1Inside) {
                            //the modifier vectors is touched by a convex corder of the base
                            //do nothing
                        }
                        if (isM0Inside && isM1Inside) {
                            //do nothing
                        }
                    }

                }

                if (count == 2) {
//                    //it is already sorted, this first is the one entering
                    Intersection.Placement place = Intersection.Placement.IN;
                    for (Intersection inter : sortedIntersections) {

                        if (place == Intersection.Placement.IN) {
                            inter.in();
                            place = Intersection.Placement.OUT;
                        } else {
                            inter.out();
                            place = Intersection.Placement.IN;
                        }
                    }

                }
            } else {
                //do nothing
            }
            intersections.addAll(
                    sortedIntersections
            );

        }

        if (intersections.size() == 2 && intersections.get(0).getIntersection().equals(intersections.get(1).getIntersection())) {//there is just 1 single point touching, a corder, touching the other shape
            return List.of();
        }

        List<Intersection> sortedByModifier = new ArrayList<>(intersections);

        List<Integer> inAndOutIndexes = IntStream.range(0, sortedByModifier.size()).boxed().filter((index) -> {
            Intersection current = sortedByModifier.get(index);
            Intersection next = sortedByModifier.get((index + 1) % sortedByModifier.size());
            return (current.placement == next.placement && (current.placement == Intersection.Placement.IN || current.placement == Intersection.Placement.OUT));
        }).collect(Collectors.toList());
        Collections.reverse(inAndOutIndexes);

        int delta = 0;
        for (Integer index : inAndOutIndexes) {
            Intersection next = sortedByModifier.get(delta + (index + 1) % sortedByModifier.size());

            Intersection intermediary = new Intersection(next.intersection, next.baseIndex, next.baseVector, next.modifierIndex, next.modifierVector);
            if (next.placement == Intersection.Placement.IN) {
                intermediary.placement = Intersection.Placement.OUT;
            }
            if (next.placement == Intersection.Placement.OUT) {
                intermediary.placement = Intersection.Placement.IN;
            }
            int newIndex = (index + 1);
            if (newIndex >= sortedByModifier.size()) {
                delta++;
            }
            sortedByModifier.add(delta + (index + 1) % sortedByModifier.size(), intermediary);
        }

        sortedByModifier.sort((intersection1, intersection2) -> {
            int modifierIndex1 = intersection1.modifierIndex;
            int modifierIndex2 = intersection2.modifierIndex;

            if (modifierIndex1 != modifierIndex2) {
                return Integer.compare(modifierIndex1, modifierIndex2);
            } else {
                Point modifierPoint = modifierPoints.get(intersection1.modifierIndex);
                return Double.compare(intersection1.intersection.distance2D(modifierPoint), intersection2.intersection.distance2D(modifierPoint));
            }
        });

        if ((!sortedByModifier.isEmpty()) && sortedByModifier.get(0).placement == Intersection.Placement.OUT) {
            sortedByModifier.add(0, sortedByModifier.get(sortedByModifier.size() - 1));
        }

        for (int i = 0; i < sortedByModifier.size() - 1; i = i + 2) {
            Intersection i0 = sortedByModifier.get(i);
            Intersection i1 = sortedByModifier.get(i + 1);

            SubDivision2D s = new SubDivision2D(i0, i1);
            answer.add(s);
        }

        return answer;
    }

    List<Point> intersectPoints(List<SubDivision2D> subdivisions) {
        List<Point> answer = new ArrayList<>();

        CircularList<Point> modifierList = new CircularList<>(modifier.getPointsCounterClockwise());
        CircularList<Point> baseList = new CircularList<>(base.getPointsClockwise());

        long numberOfModifierCollidingPoints = modifier.getPoints().stream().filter(modifierPoint -> base.collides2D(modifierPoint)).count();
        long numberOfBaseCollidingPoints = base.getPoints().stream().filter(modifierPoint -> modifier.collides2D(modifierPoint)).count();

        if (subdivisions.isEmpty()) {
            if (numberOfBaseCollidingPoints == this.basePoints.size()) {
                answer.addAll(this.basePoints);
            }
            if (numberOfModifierCollidingPoints == this.modifierPoints.size()) {
                answer.addAll(this.modifierPoints);
            }
            return answer;
        }

        if (subdivisions.size() == 1) {

            SubDivision2D sd = subdivisions.get(0);
            Point in = sd.getIn();
            Point out = sd.getOut();
            if (sd.getInModifierIndex().equals(sd.getOutModifierIndex())) {

                answer.add(in);
                answer.addAll(baseList.subList(sd.getInBaseIndex() + 1, sd.getOutBaseIndex() + 1));
                answer.add(out);
            } else {
//                -->-- mod
//                |    |
//                |  __|__
//                |_|__|  |
//                  |___>_| base      
//                        
                answer.add(in);
                answer.addAll(modifierList.subList(sd.getInModifierIndex() + 1, sd.getOutModifierIndex() + 1));
                answer.add(out);

                if (!sd.getInBaseIndex().equals(sd.getOutBaseIndex())) {
                    List<Point> tmp = baseList.subList(sd.getInBaseIndex() + 1, sd.getOutBaseIndex() + 1);
                    Collections.reverse(tmp);
                    answer.addAll(tmp);
                }

            }
        }
        if (subdivisions.size() > 1) {
            for (int i = 0; i < subdivisions.size(); i++) {

                SubDivision2D s0 = subdivisions.get(i);
                SubDivision2D s1 = subdivisions.get((i + 1) % subdivisions.size());
//        
//                _>_
//               |   |mod
//        ----<--|-x-|----<----
//        |      |   |        |
//        |      |   |   base |
//        ---->--|-x-|---->----
//               |_<_|

                if (s0.getInModifierIndex().equals(s0.getOutModifierIndex())) {
                    answer.add(s0.getIn());
                    answer.add(s0.getOut());
                } else {
                    answer.add(s0.getIn());
                    List<Point> tmp;
                    tmp = modifierList.subList(s0.getInModifierIndex() + 1, s0.getOutModifierIndex() + 1);
                    answer.addAll(tmp);
                    answer.add(s0.getOut());
                }
                if (s0.getOutBaseIndex().equals(s1.getInBaseIndex())) {
                    //there is no base point to added
                } else {
                    List<Point> tmp;
                    tmp = baseList.subList(s1.getInBaseIndex() + 1, s0.getOutBaseIndex() + 1);
                    Collections.reverse(tmp);
                    answer.addAll(tmp);
                }

            }

        }
        return answer;
    }

    public Optional<List<IConvex>> intersect() throws InvalidGeometryException {

        if (base.collides2D(modifier)) {
            long numberOfModifierCollidingPoints = modifier.getPoints().stream().filter(modifierPoint -> base.collides2D(modifierPoint)).count();
            if (numberOfModifierCollidingPoints == modifier.size()) {
                return Optional.of(List.of(modifier));
            }

            long numberOfBaseCollidingPoints = base.getPoints().stream().filter(basePoint -> modifier.collides2D(basePoint)).count();
            if (numberOfBaseCollidingPoints == base.size()) {
                return Optional.of(List.of(base));
            }

            /////////////////////
            List<SubDivision2D> modifierOnBaseSubdivisions = this.getInAndOutIntersection();
            //there is no intersect
            if (modifierOnBaseSubdivisions.isEmpty()) {
                return Optional.empty();
            } else {
                List<Point> answer = this.intersectPoints(modifierOnBaseSubdivisions);
                return Optional.of(IConvex.breakIntoConvexes(answer).stream().map((convexPoints) -> {
                    return new Convex(convexPoints);
                }).collect(Collectors.toList())
                );
            }
        } else {
            return Optional.empty();
        }

    }

    List<List<Point>> joinPoints(List<SubDivision2D> subdivisions) {
        List<List<Point>> answer = new ArrayList<>();

        CircularList<Point> modifierList = new CircularList<>(modifier.getPointsCounterClockwise());
        CircularList<Point> baseList = new CircularList<>(base.getPointsClockwise());

        long numberOfModifierCollidingPoints = modifier.getPoints().stream().filter(modifierPoint -> base.collides2D(modifierPoint)).count();
        long numberOfBaseCollidingPoints = base.getPoints().stream().filter(modifierPoint -> modifier.collides2D(modifierPoint)).count();

        if (subdivisions.isEmpty()) {
            if (numberOfBaseCollidingPoints == this.basePoints.size()) {
                answer.add(List.copyOf(modifierList));
            }
            if (numberOfModifierCollidingPoints == this.modifierPoints.size()) {
                answer.add(List.copyOf(this.basePoints));
            }
            return answer;
        }

        List<Point> list = new LinkedList();
        if (subdivisions.size() == 1) {
//TODO HERE
            SubDivision2D sd = subdivisions.get(0);
            Point in = sd.getIn();
            Point out = sd.getOut();

            System.err.println("IN " + in);
            System.err.println("OUT " + out);
            if (sd.getInModifierIndex().equals(sd.getOutModifierIndex())) {

//                -->----mod
//                |   __|____   
//                |  |  |    |
//                |  |__|__>_|base   
//                |_____|        
//                        
                modifierList.subList(sd.getOutModifierIndex() + 1, sd.getInModifierIndex() + 1).forEach((t) -> {
                    System.err.println(t);
                });
                System.err.println("=======");
                list.addAll(modifierList.subList(sd.getOutModifierIndex() + 1, sd.getInModifierIndex() + 1));
                list.add(in);
                List<Point> tmp = baseList.subList(sd.getOutBaseIndex() + 1, sd.getInBaseIndex() + 1);
                Collections.reverse(tmp);
                list.addAll(tmp);
                list.add(out);

//                --<----base
//                |   __|____   
//                |  |  |    |
//                |  |__|_<__|mod   
//                |_____|        
            } else {
//                -->-- mod
//                |    |
//                |  __|__
//                |_|__|  |
//                  |___>_| base      
//                        
//                        

                list.add(in);
                List<Point> tmp = baseList.subList(sd.getOutBaseIndex() + 1, sd.getInBaseIndex() + 1);
                Collections.reverse(tmp);
                list.addAll(tmp);
                list.add(out);
                list.addAll(modifierList.subList(sd.getOutModifierIndex() + 1, sd.getInModifierIndex() + 1));

            }
        }
        if (subdivisions.size() > 1) {
            System.err.println("***********88");
            System.err.println("subdivisions "+subdivisions.size());
            for (int i = 0; i < subdivisions.size(); i++) {
                System.err.println("i "+i);
                SubDivision2D s0 = subdivisions.get(i);
                SubDivision2D s1 = subdivisions.get((i + 1) % subdivisions.size());
//        
//                _>_
//               |   |base
//        ----<--|s1-|----<----
//        |      |   |        |
//        |      |   |   mod  |
//        ---->--|s0-|---->----
//               |_<_|

                list.add(s1.getOut());
                list.addAll(baseList.subList(s1.getOutBaseIndex() + 1, s1.getInBaseIndex() + 1));
                list.add(s1.getIn());
                
                List<Point> tmp = modifierList.subList(s0.getOutModifierIndex() + 1, s1.getInModifierIndex() + 1);
                Collections.reverse(tmp);
                list.addAll(tmp);
            }
        }
        answer.add(list);
        return answer;
    }

    public Optional<List<IConvex>> join() throws InvalidGeometryException {

        if (base.collides2D(modifier)) {
            long numberOfModifierCollidingPoints = modifier.getPoints().stream().filter(modifierPoint -> base.collides2D(modifierPoint)).count();
            if (numberOfModifierCollidingPoints == modifier.size()) {
                return Optional.of(List.of(base));
            }

            long numberOfBaseCollidingPoints = base.getPoints().stream().filter(basePoint -> modifier.collides2D(basePoint)).count();
            if (numberOfBaseCollidingPoints == base.size()) {
                return Optional.of(List.of(modifier));
            }

            /////////////////////
            List<SubDivision2D> modifierOnBaseSubdivisions = this.getInAndOutIntersection();
            //there is no intersect
            if (modifierOnBaseSubdivisions.isEmpty()) {
                return Optional.of(List.of(base, modifier));
            } else {
                List<List<Point>> join = this.joinPoints(modifierOnBaseSubdivisions);
                List<IConvex> answer = join.stream().filter((list) -> {
                    return list.size() > 2;
                }).map((list) -> {
                    return IConvex.breakIntoConvexes(list);
                }).flatMap(List::stream).map((list) -> {
                    return new Convex(list);
                }).collect(Collectors.toList());

                return Optional.of(answer);
            }
        } else {
            return Optional.empty();
        }

    }

    /**
     * returns null if there is no subtraction. If there is, returns the list of
     * points and a pair with the in and the out point
     *
     * @return
     * @throws com.accliva.cratis.geometry.exception.InvalidGeometryException
     */
    public Optional<List<IConvex>> subtract() throws InvalidGeometryException {
        if (base.collides2D(modifier)) {
            long numberOfBasePointsInsideModifier = base.getPoints().stream().filter((basePoint) -> modifier.collides2D(basePoint)).count(); //the modifier is larger and all around the base
            if (numberOfBasePointsInsideModifier == basePoints.size()) {
                return Optional.empty();
            }

            long numberOfCollidingPoints = modifier.getPoints().stream().filter(modifierPoint -> base.collides2D(modifierPoint)).count();
            List<SubDivision2D> modifierOnBaseSubdivisions = this.getInAndOutIntersection();

            if (numberOfCollidingPoints == modifier.size() && modifierOnBaseSubdivisions.isEmpty()) {
                return Optional.of(this.makeHole());
            } else {
                //there is no intersect
                if (modifierOnBaseSubdivisions.isEmpty()) {
                    return Optional.empty();
                } else {
                    List<List<Point>> subtraction = this.subtractPoints(modifierOnBaseSubdivisions);

                    List<IConvex> answer = subtraction.stream().filter((list) -> {
                        return list.size() > 2;
                    }).map((list) -> {
                        return IConvex.breakIntoConvexes(list);
                    }).flatMap(List::stream).map((list) -> {
                        return new Convex(list);
                    }).collect(Collectors.toList());

                    return Optional.of(answer);
                }
            }
        } else {
            List<IConvex> answer = new ArrayList<>();
            answer.add(base);
            return Optional.of(answer);
        }

    }

    private List<IConvex> makeHole() {
        List<Point> clockwisePoints = base.getPointsClockwise();
        List<IConvex> answer = new ArrayList<>();
        Map<Vector, List<Point>> baseSegments = new HashMap<>();
        Map<Point, Point> baseToModifier = new HashMap<>();
        List<Vector> baseToModifiers = new ArrayList<>();

        clockwisePoints.stream().forEach((basePoint) -> {

            Optional<Pair<Point, Vector>> closestModifier = modifierVectors.stream().map((modifierVector) -> {
                return new Pair<>(basePoint.getClosestPointOnSegment2D(modifierVector), modifierVector);
            }).min((pair1, pair2) -> {
                double distance1 = basePoint.distance2D(pair1.getFirst());
                double distance2 = basePoint.distance2D(pair2.getFirst());

                return Double.compare(distance1, distance2);
            });
            Point point = closestModifier.get().getFirst();
            if (!point.equals(basePoint)) {
                baseToModifier.put(basePoint, point);
                baseToModifiers.add(new Vector(basePoint, point));
            }

        });

        modifier.getPoints().stream().forEach((modifierPoint) -> {
            Optional<Pair<Point, Vector>> closestBase = baseVectors.stream().map((baseVector) -> {
                return new Pair<Point, Vector>(modifierPoint.getClosestPointOnSegment2D(baseVector), baseVector);
            }).filter((pair) -> {
                if (pair.getFirst().equals(modifierPoint)) {
                    return true;
                }
                boolean condition1 = (!baseToModifiers.stream().anyMatch((vector) -> {
                    return vector.segmentIntersection2D(new Vector(pair.getFirst(), modifierPoint)) != null;
                }));
                boolean condition2 = (!modifierVectors.stream().anyMatch((vector) -> {
                    Point p = vector.segmentIntersection2D(new Vector(pair.getFirst(), modifierPoint));
                    return p != null && !p.equals(modifierPoint);
                }));
                return condition1 && condition2;
            }).min((pair1, pair2) -> {
                double distance1 = modifierPoint.distance2D(pair1.getFirst());
                double distance2 = modifierPoint.distance2D(pair2.getFirst());

                return Double.compare(distance1, distance2);
            });
            if (closestBase.isPresent()) {
                Point point = closestBase.get().getFirst();
                Vector baseVector = closestBase.get().getSecond();
                baseToModifier.put(point, modifierPoint);
                if (!baseSegments.containsKey(baseVector)) {
                    baseSegments.put(baseVector, new ArrayList<>());
                }
                baseSegments.get(baseVector).add(point);
            }
        });

        baseSegments.entrySet().stream().forEach((entry) -> {
            final Vector baseVector = entry.getKey();
            List<Point> list = entry.getValue();
            Collections.sort(list, (point1, point2) -> {
                return Double.compare(baseVector.getP0().distance2D(point1), baseVector.getP0().distance2D(point2));
            });
        });

        List<Vector> replacements = new ArrayList<>();

        baseVectors.forEach(baseVector -> {
            if (baseSegments.containsKey(baseVector)) {
                List<Point> points = baseSegments.get(baseVector);
                Point start = new Point(baseVector.getP0());
                for (Point point : points) {
                    if (!start.equals(point)) {
                        Vector replacement = new Vector(start, point);
                        replacements.add(replacement);
                        start = point;
                    }

                }
                if (!start.equals(baseVector.getP1())) {
                    Vector replacement = new Vector(start, baseVector.getP1());
                    replacements.add(replacement);
                }
            } else {
                replacements.add(baseVector);
            }
        });

        replacements.stream().forEach((vector) -> {
            List<Point> points = new ArrayList<>(4);
            Point p0 = vector.getP0();
            if (p0 != null) {
                points.add(p0);
            }
            Point p1 = vector.getP1();
            if (p1 != null) {
                points.add(p1);
            }
            Point p2 = baseToModifier.get(p1);
            if (p2 != null) {
                points.add(p2);
            }
            Point p3 = baseToModifier.get(p0);
            if (p3 != null) {
                points.add(p3);
            }
            if (points.size() > 2) {
                answer.addAll(IConvex.breakIntoConvexes(points).stream().map((brokenPoints) -> {
                    return new Convex(brokenPoints);
                }).collect(Collectors.toList()));
//                IConvex convex = new Convex(points);
//                answer.add(convex);
            } else {
                System.err.println("booleanOperation2D.makeHole verify this");
            }
        });

        return answer;

    }

    public static class Intersection {

        enum Placement {
            OUT,
            IN,
            IN_AND_OUT,
            TOUCH,
            UNDEFINED
        };

        Integer baseIndex;
        Vector baseVector;
        Integer modifierIndex;
        Vector modifierVector;
        Point intersection;
        Placement placement;

        public Intersection(Integer baseIndex, Vector baseVector, Integer modifierIndex, Vector modifierVector) {
            this.baseIndex = baseIndex;
            this.baseVector = baseVector.get2D();
            this.modifierIndex = modifierIndex;
            this.modifierVector = modifierVector.get2D();
            this.intersection = this.baseVector.segmentIntersection(modifierVector);
            this.placement = Placement.UNDEFINED;
        }

        public Intersection(Point intersection, Integer baseIndex, Vector baseVector, Integer modifierIndex, Vector modifierVector) {
            this.baseIndex = baseIndex;
            this.baseVector = baseVector.get2D();
            this.modifierIndex = modifierIndex;
            this.modifierVector = modifierVector.get2D();
            this.intersection = intersection;
            this.placement = Placement.UNDEFINED;
        }

        public Vector getBase(){
            return this.baseVector;
        }
        
        public Vector getModifier(){
            return this.modifierVector;
        }
        
        public Point getIntersection() {
            return this.intersection;
        }

        public void out() {
            this.placement = Placement.OUT;
        }

        public void in() {
            this.placement = Placement.IN;
        }

        public void inAndOut() {
            this.placement = Placement.IN_AND_OUT;
        }

        public void touch() {
            this.placement = Placement.TOUCH;
        }

        public void undefined() {
            this.placement = Placement.UNDEFINED;
        }

        public void merge(Set<Point> points) {
            if (this.intersection != null) {
                Point.mergePoints(this.intersection, points);
            }
        }

        public boolean isValid(Vector previousModifier, Vector nextModifier, Vector previousBase, Vector nextBase) {
            if (this.intersection == null) {
                return false;
            }
//
            boolean remove0 = (!previousModifier.isColinear2D(baseVector)) && intersection.equals(modifierVector.getP0()) && (previousModifier.isColinear2D(nextBase));
            if (remove0) {
                return false;
            }

            boolean remove1 = (modifierVector.isColinear2D(nextBase)) && modifierVector.getP1().equals(nextBase.getP0());
            if (remove1) {
                return false;
            }

            boolean remove2 = modifierVector.getP1().equals(baseVector.getP1()) && modifierVector.getP1().equals(intersection) && (nextModifier.isColinear2D(previousBase));
            if (remove2) {
                return false;
            }
//
//            boolean condition = (!intersection.equals(modifierVector.getP0())) || (!intersection.equals(baseVector.getP1()));

            return ((!intersection.equals(modifierVector.getP0())) || (!intersection.equals(baseVector.getP1()))) && ((!intersection.equals(modifierVector.getP1())) || (!intersection.equals(baseVector.getP0())));
//            return  (!intersection.equals(modifierVector.getP0())) &&  (!intersection.equals(modifierVector.getP1()));
//            return condition;
        }

        @Override
        public String toString() {
            return "Intersection{" + "\n    baseIndex=" + baseIndex + ", \n    modifierIndex=" + modifierIndex + ", \n    intersection=" + intersection + ", \n    placement=" + placement + "\n}";
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 59 * hash + Objects.hashCode(this.intersection);
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
            final Intersection other = (Intersection) obj;
            if (!Objects.equals(this.intersection, other.intersection)) {
                return false;
            }
            return true;
        }

    }

}
