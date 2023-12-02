/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.learning.spatial;

/**
 *
 * @author caio
 * @param <T>
 * @param <N>
 */
public interface IInterpolable<T, N extends Number> {
    
     /**
     *
     * @param p
     * @return
     */
    T interpolate(N p);
    
    /**
     *
     * @return
     */
    N getLength();
}
