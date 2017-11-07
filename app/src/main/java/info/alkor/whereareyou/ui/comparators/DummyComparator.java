package info.alkor.whereareyou.ui.comparators;

import java.util.Comparator;

/**
 * Created by Marlena on 2017-11-06.
 */

public class DummyComparator<E> implements Comparator<E> {

    final int result;

    public DummyComparator(int result) {
        this.result = result;
    }

    @Override
    public int compare(E a, E b) {
        return result;
    }

    public static <E> Comparator<E> create(Class<E> clazz, int result) {
        return new DummyComparator<>(result);
    }
}
