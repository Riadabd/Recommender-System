/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datastructures;

import java.util.AbstractMap;

/**
 *
 * @author Leen De Baets
 */
public class ComparableSimpleEntry extends AbstractMap.SimpleEntry<Double, Object> implements Comparable<ComparableSimpleEntry> {

    public ComparableSimpleEntry(Double key, Object value) {
        super(key, value);
    }

    @Override
    public int compareTo(ComparableSimpleEntry o) {
        return Double.compare(o.getKey(),this.getKey());
    }
}
