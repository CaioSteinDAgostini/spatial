/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.learning.spatial;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author caio
 */
public class CircularList<T> extends ArrayList<T>{

    public CircularList() {
        super();
    }
    
    public CircularList(int initialCapacity){
        super(initialCapacity);
    }
    
    public CircularList(List<T> list){
        super(list);
    }
    
    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        fromIndex = fromIndex%this.size();
        toIndex = toIndex%this.size();
        if(fromIndex<toIndex && fromIndex<this.size() && toIndex<this.size()){
            return super.subList(fromIndex, toIndex); //To change body of generated methods, choose Tools | Templates.
        }
        else{
            List<T> answer = new ArrayList<>();
            answer.addAll(super.subList(fromIndex, this.size()));
            answer.addAll(super.subList(0, toIndex));
            return answer;
        }
    }

    
    @Override
    public T get(int index) {
        index = index%this.size();
        if(index<0){
            index = this.size()+index;
        }
            return super.get(index);
    }
    
    
    
}
