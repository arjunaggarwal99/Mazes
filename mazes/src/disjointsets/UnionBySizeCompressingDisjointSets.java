package disjointsets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

/**
 * A quick-union-by-size data structure with path compression.
 *
 * @see DisjointSets for more documentation.
 */
public class UnionBySizeCompressingDisjointSets<T> implements DisjointSets<T> {
    // Do NOT rename or delete this field. We will be inspecting it directly in our private tests.
    List<Integer> pointers;
    Map<T, Integer> map;
    private int size;


    /*
    However, feel free to add more fields and private helper methods. You will probably need to
    add one or two more fields in order to successfully implement this class.
    */

    public UnionBySizeCompressingDisjointSets() {
        pointers = new ArrayList<>();
        this.size = 0;
        //Create a map to have info about what item belongs to what index
        map = new HashMap<>();
    }

    @Override
    public void makeSet(T item) {
        if (map.containsKey(item)) {
            throw new IllegalArgumentException();
        }
        //Add -1 to pointers as the tree is empty and the
        //element is always a root node
        pointers.add(-1);
        map.put(item, size);
        //Create a new set
        Set<T> set = new HashSet<>();
        //Add the item to the empty set
        set.add(item);
        //Increment the size
        size++;
    }

    private int findSet(int index) {
        //So that the while loop runs at least once
        //We get the index of the particular item
        int returnIndex = index;
        //We have to climb up the (virtual) tree till we reach OverallRoot
        //We reach the OverallRoot when the value at the index is negative
        while (pointers.get(returnIndex) >= 0) {
            returnIndex = pointers.get(returnIndex);
        }
        int root = returnIndex;
        returnIndex = index;
        while (pointers.get(returnIndex) >= 0) {
            int newRequired = pointers.get(returnIndex);
            pointers.set(returnIndex, root);
            returnIndex = newRequired;
        }
        return root;
    }

    @Override
    public int findSet(T item) {
        //if `item` is not contained in any of these sets
        // throw IllegalArgumentException()
        if (!map.containsKey(item)) {
            throw new IllegalArgumentException();
        }
        return this.findSet(map.get(item));
    }


    @Override
    public boolean union(T item1, T item2) {
        if (!map.containsKey(item1) || !map.containsKey(item2)) {
            throw new IllegalArgumentException();
        }
        //Find indices of the sets the items belong to
        int index1 = findSet(item1);
        int index2 = findSet(item2);

        //If the items belong to same set return false
        if (index1 == index2) {
            return false;
        }
        int value1 = pointers.get(index1);
        int value2 = pointers.get(index2);
        int weight1 = Math.abs(value1);
        int weight2 = Math.abs(value2);
        if (weight1 >= weight2) {
            //Change parents of item2
            pointers.set(index1, value1 + value2);
            pointers.set(index2, index1);
        } else {
            //Change parents of item1
            pointers.set(index2, value1 + value2);
            pointers.set(index1, index2);
        }
        return true;
    }
}
