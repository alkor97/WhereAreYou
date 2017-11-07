package info.alkor.whereareyou.ui.comparators;

import java.util.Comparator;

/**
 * Created by Marlena on 2017-11-06.
 */

public abstract class NullAwareComparator<E> implements Comparator<E> {

    public int compare(E a, E b) {
        if (a != null) {
            if (b != null) {
                return doCompare(a, b);
            } else {
                return 1;
            }
        } else {
            return b == null ? 0 : -1;
        }
    }

    protected abstract int doCompare(E a, E b);

    private static class WithComparator<E> extends NullAwareComparator<E> {

        private final Comparator<E> comparator;

        public WithComparator(Comparator<E> comparator) {
            this.comparator = comparator;
        }

        protected int doCompare(E a, E b) {
            return comparator.compare(a, b);
        }
    }

    public static <E> Comparator<E> create(Comparator<E> comparator) {
        return new WithComparator<E>(comparator);
    }

    private static class Adapter<E extends Comparable<E>> extends NullAwareComparator<E> {
        protected int doCompare(E a, E b) {
            return a.compareTo(b);
        }
    };

    public static <E extends Comparable<E>> Comparator<E> create(Class<E> clazz) {
        return create(new Adapter<E>());
    }
}
