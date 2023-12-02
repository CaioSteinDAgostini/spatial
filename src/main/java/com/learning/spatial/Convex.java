/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.learning.spatial;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

/**
 *
 * @author caio
 */
public class Convex implements IConvex {

    List<Point> points;
    Orientation orientation;
    Double perimeter;

    public Convex(Point... points) {
        this.points = IConvex.removeDoublePoints(List.of(points), false);
    }

    public Convex(List<Point> points) {
        this.points = IConvex.removeDoublePoints(points, false);
        this.init();
    }

    private void verifySelfCrossing() {
        List<Vector> vs = this.getVectors();

        vs.stream().forEach((vi) -> {
            vs.stream().forEach((vj) -> {

                Point intersection = vi.segmentIntersection2D(vj);
                if (intersection != null) {

                    if ((intersection.equals(vj.getP0()) || intersection.equals(vj.getP1())) && (intersection.equals(vi.getP0()) || intersection.equals(vi.getP1()))) {

                    } else {
                        throw new InvalidGeometryException();
                    }
                }
            });
        });

    }

    private void init() {
//        if (!IConvex.validatePoints(this.points)) {
//            throw new Concave2DException();
//        }
        this.perimeter = IntStream.range(0, this.points.size()).boxed().mapToDouble((index) -> {
            Point p0 = this.points.get(index);
            Point p1 = this.points.get((index + 1) % this.points.size());

            return p0.distance(p1);
        }).sum();
        verifySelfCrossing();
    }

    @Override
    public List<Vector> getVectors() {
        List<Vector> answer = new ArrayList<>(this.points.size());
        for (int index = 0; index < this.points.size() - 1; index++) {
            answer.add(new Vector(points.get(index), points.get(index + 1)));
        }
        answer.add(new Vector(points.get(this.points.size() - 1), points.get(0)));
        return answer;
    }

    @Override
    public int size() {
        return this.points.size();
    }

    @Override
    public Point get(int index) {
        return this.points.get(index);
    }

    @Override
    public List<Point> getPoints() {
        List<Point> answer = new ArrayList<>();
        answer.addAll(points);
        return answer;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + Objects.hashCode(this.points);
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
        final Convex other = (Convex) obj;
        if (!Objects.equals(this.points, other.points)) {
            return false;
        }
        return true;
    }

    @Override
    public Orientation getOrientation() {
        if (this.orientation == null) {
            this.orientation = Orientation.getOrientation2D(points);
        }
        return this.orientation;
    }

    @Override
    public boolean contains(Point point) {
        if (points.stream().anyMatch(p -> (p.equals(point)))) {
            return true;
        }
        return false;
    }

    @Override
    public void move(Point translation) {
        points.forEach(p -> {
            p.set(p.add(translation));
        });
    }

    @Override
    public Convex moved(Point translation) {
        Convex answer = new Convex(this.getPoints());
        answer.move(translation);
        return answer;
    }

    @Override
    public Double getPerimeter() {
        return this.perimeter;
    }

}
