package com.aFeng;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomizedCollection {

    private List<Integer> list = new ArrayList<>();

    /** Initialize your data structure here. */
    public RandomizedCollection() {
    }

    /** Inserts a value to the collection. Returns true if the collection did not already contain the specified element. */
    public boolean insert(int val) {
        boolean a = list.contains(val);
        list.add(val);
        return a;
    }

    /** Removes a value from the collection. Returns true if the collection contained the specified element. */
    public boolean remove(int val) {
        Object a = val;
        return list.remove(a);
    }

    /** Get a random element from the collection. */
    public int getRandom() {
        return list.get(new Random().nextInt(list.size()));
    }
}
