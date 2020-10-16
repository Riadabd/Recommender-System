package datastructures;

import java.util.PriorityQueue;

/**
 * Michiel Povre and Riad Abdallah
 * Group 30
 */

/**
 * 
 * FixedSizePriorityQueue<T> is a class responsible for maintaining in order the 'elementsLeft' smallest items.
 * 
 * @author Leen De Baets
 *
 */
public class FixedSizedPriorityQueue extends PriorityQueue<ComparableSimpleEntry> {
    
    /**
     * Determines the remaining free spots in this queue
     */
    private int elementsLeft;

    /**
     * Constructs a new FixedSizedPriorityQueue with a capacity of elementsLeft
     * @param elementsLeft
     */
    public FixedSizedPriorityQueue(int elementsLeft) {
        super();
        this.elementsLeft = elementsLeft;
    }

    /**
     * Overrides the add of a PriorityQueue
     * 
     * - If capacity has not been reached -> delegate the add to the PriorityQueue and decrease the capacity
     * - Else -> Check with the item with the highest distance (should be at the front of the priority queue). If item to be added is smaller
     *           then remove top from queue and add new item. Else do not add item.
     * 
     * @param e The ComparableSimpleEntry contains as key a double (representing e.g., the distance) and as value an object (e.g., a movie). 
     * The key should be used to determine if an element is added or not when the capacity of the fixed sized priority queue is reached.
     */
    @Override
    public boolean add(ComparableSimpleEntry e) {
    	if (elementsLeft > 0) {
    		if (super.peek() == null) {
    			super.add(e);
    			elementsLeft--;
    		} else if (super.peek().getKey() > e.getKey()) {
    			ComparableSimpleEntry temp = super.poll();
    			super.add(e);
    			super.add(temp);
    			elementsLeft--;
    		} else {
    			super.add(e);
    			elementsLeft--;
    		}
    	} else {
    		if (super.peek().getKey() > e.getKey()) {
    			super.poll();
    			super.add(e);
    		}
    	}
        return true;
    }

    @Override
    public String toString() {
        // Do this in such way that the first element printed is the most important one (e.g. the movie with the smallest distances (key))
    	int size = super.size();
        String[] values = new String[size];
        ComparableSimpleEntry[] e_array = new ComparableSimpleEntry[size];
        String result = "";
        
    	for(int i = 0; i < size; i++) {
    		ComparableSimpleEntry e = new ComparableSimpleEntry(super.peek().getKey(), super.peek().getValue());
    		e_array[i] = e;
        	values[i] = super.poll().getValue().toString();
        }
    	
    	// Restore elements to the priority queue
    	for(int i = 0; i < size; i++) {
    		super.add(e_array[i]);
    	}
    	
        for(int i = size - 1; i >= 0; i--) {
        	result += values[i];
        	result += "\n";
        }
        return result;
    }
}
