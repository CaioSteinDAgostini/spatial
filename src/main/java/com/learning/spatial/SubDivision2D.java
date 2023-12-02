/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.learning.spatial;

import com.learning.spatial.ConvexBooleanOperation2D.Intersection;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author caio
 */
public class SubDivision2D {

    private List<Point> kept;
    private List<Point> removed;

    private Intersection in;
    private Intersection out;

    public SubDivision2D(Intersection in, Intersection out) {
        this.in = in;
        this.out = out;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 61 * hash + Objects.hashCode(this.in);
        hash = 61 * hash + Objects.hashCode(this.out);
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
        final SubDivision2D other = (SubDivision2D) obj;
        if (!Objects.equals(this.in, other.in)) {
            return false;
        }
        if (!Objects.equals(this.out, other.out)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {

        return "out: " + getOut() + "\n"
                + "outBaseIndex " + getOutBaseIndex() + "\n"
                + "outModifierIndex " + getOutModifierIndex() + "\n"
                //                    + "kept: " + getKept() + "\n"
                //                    + "removed: " + getRemoved() + "\n";;
                + "in: " + getIn() + "\n"
                + "inBaseIndex " + getInBaseIndex() + "\n"
                + "inModifierIndex " + getInModifierIndex();
//                    + "cut " + getCut() + "\n";
    }

    /**
     * @return the kept
     */
    public List<Point> getKept() {
        return kept;
    }

    /**
     * @param kept the kept to set
     */
    public void setKept(List<Point> kept) {
        this.kept = kept;
    }

    /**
     * @return the removed
     */
    public List<Point> getRemoved() {
        return removed;
    }

    /**
     * @param removed the removed to set
     */
    public void setRemoved(List<Point> removed) {
        this.removed = removed;
    }

    /**
     * @return the in
     */
    public Point getIn() {
        return in.intersection;
    }


    /**
     * @return the out
     */
    public Point getOut() {
        return out.intersection;
    }

    /**
     * @return the inBaseIndex
     */
    public Integer getInBaseIndex() {
        return in.baseIndex;
    }


    public Vector getInBase() {
        return this.in.baseVector;
    }

    /**
     * @return the outBaseIndex
     */
    public Integer getOutBaseIndex() {
        return this.out.baseIndex;
    }

    public Vector getOutBase() {
        return this.out.baseVector;
    }

    /**
     * @return the inModifierIndex
     */
    public Integer getInModifierIndex() {
        return this.in.modifierIndex;
    }
    
        public Vector getInModifier() {
        return this.in.modifierVector;
    }

    /**
     * @return the outModifierIndex
     */
    public Integer getOutModifierIndex() {
        return this.out.modifierIndex;
    }

    public Vector getOutModifier() {
        return this.out.modifierVector;
    }

    public boolean isInM0() {
        return this.getIn().equals(in.modifierVector.getP0());
    }

    public boolean isInM1() {
        return this.getIn().equals(in.modifierVector.getP1());
    }

    public boolean isOutM0() {
        return this.getOut().equals(in.modifierVector.getP0());
    }

    public boolean isOutM1() {
        return this.getOut().equals(in.modifierVector.getP1());
    }

    public boolean isInB0() {
        return this.getIn().equals(in.baseVector.getP0());
    }

    public boolean isInB1() {
        return this.getIn().equals(in.baseVector.getP1());
    }

    public boolean isOutB0() {
        return this.getOut().equals(in.baseVector.getP0());
    }

    public boolean isOutB1() {
        return this.getOut().equals(in.baseVector.getP1());
    }

}
