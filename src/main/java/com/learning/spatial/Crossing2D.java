/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.learning.spatial;

import java.util.List;
import java.util.Objects;

public class Crossing2D {

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 11 * hash + Objects.hashCode(this.point);
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
            final Crossing2D other = (Crossing2D) obj;
            if (!Objects.equals(this.point, other.point)) {
                return false;
            }
            return true;
        }

        public final int baseIndex;
        public final int modifierIndex;
        public final Point point;
        List<Vector> baseVectors;
        List<Vector> modifierVectors;

        public Crossing2D(List<Vector> baseVectors, int baseIndex, List<Vector> modifierVectors, int modifierIndex, Point point) {
            this.baseVectors = baseVectors;
            this.modifierVectors = modifierVectors;
            this.baseIndex = baseIndex;
            this.modifierIndex = modifierIndex;
            this.point = point;
        }

        @Override
        public String toString() {
            return point + "@" + baseVectors.get(baseIndex) + "x" + modifierVectors.get(modifierIndex);
        }

    }

