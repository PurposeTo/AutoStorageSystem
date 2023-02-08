package com.chain.autostoragesystem.utils.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class CollectionUtils {

    public static <T extends Comparable<? super T>> boolean equalLists(List<T> one, List<T> two) {
        if (one == null && two == null) {
            return true;
        }

        if (one == null || two == null || one.size() != two.size()) {
            return false;
        }

        //to avoid messing the order of the lists we will use a copy
        one = new ArrayList<>(one);
        two = new ArrayList<>(two);

        Collections.sort(one);
        Collections.sort(two);
        return one.equals(two);
    }

    public static <T> boolean equalObjectsReferences(Collection<T> one, Collection<T> two) {
        if (one == null && two == null) {
            return true;
        }

        if (one == null || two == null || one.size() != two.size()) {
            return false;
        }

        one = new ArrayList<>(one);
        two = new ArrayList<>(two);

        two.removeAll(one);
        return two.isEmpty();
    }
}
