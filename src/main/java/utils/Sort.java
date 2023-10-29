package utils;

import array.Array;

import java.util.Comparator;

/**
 * Provides methods to sort arrays of objects. Sorting requires working memory and this class allows that memory to be reused to
 * avoid allocation. The sorting is otherwise identical to the Arrays.sort methods (uses timsort).<br>
 * <br>
 * Note that sorting primitive arrays with the Arrays.sort methods does not allocate memory (unless sorting large arrays of char,
 * short, or byte).
 */
public class Sort {
    /**
     * A utility class for sorting arrays using TimSort algorithms.
     */
    static private Sort instance;
    /**
     * Internal instance of TimSort for sorting objects that implement {@link Comparable}.
     */
    private TimSort timSort;
    /**
     * Internal instance of ComparableTimSort for sorting objects that implement {@link Comparable}.
     */
    private ComparableTimSort comparableTimSort;

    /**
     * Returns a Sort instance for convenience. Multiple threads must not use this instance at the same time.
     *
     * @return A Sort instance.
     */
    public static Sort instance() {
        if (instance == null) instance = new Sort();
        return instance;
    }

    /**
     * Sorts an array of objects that implement {@link Comparable}.
     *
     * @param a   The array to be sorted.
     * @param <T> The type of elements in the array.
     */
    public <T extends Comparable> void sort(Array<T> a) {
        if (comparableTimSort == null) comparableTimSort = new ComparableTimSort();
        comparableTimSort.doSort(a.items, 0, a.size);
    }

    /**
     * Sorts an array of objects that implement {@link Comparable}.
     *
     * @param a The array to be sorted.
     */
    public void sort(Object[] a) {
        if (comparableTimSort == null) comparableTimSort = new ComparableTimSort();
        comparableTimSort.doSort(a, 0, a.length);
    }

    /**
     * Sorts a portion of an array of objects that implement {@link Comparable}.
     *
     * @param a         The array to be sorted.
     * @param fromIndex The starting index of the range to be sorted.
     * @param toIndex   The ending index of the range to be sorted (exclusive).
     */
    public void sort(Object[] a, int fromIndex, int toIndex) {
        if (comparableTimSort == null) comparableTimSort = new ComparableTimSort();
        comparableTimSort.doSort(a, fromIndex, toIndex);
    }

    /**
     * Sorts an array using a custom comparator.
     *
     * @param a   The array to be sorted.
     * @param c   The comparator used for sorting.
     * @param <T> The type of elements in the array.
     */
    public <T> void sort(Array<T> a, Comparator<? super T> c) {
        if (timSort == null) timSort = new TimSort();
        timSort.doSort(a.items, c, 0, a.size);
    }

    /**
     * Sorts an array using a custom comparator.
     *
     * @param a   The array to be sorted.
     * @param c   The comparator used for sorting.
     * @param <T> The type of elements in the array.
     */
    public <T> void sort(T[] a, Comparator<? super T> c) {
        if (timSort == null) timSort = new TimSort();
        timSort.doSort(a, c, 0, a.length);
    }

    /**
     * Sorts a portion of an array using a custom comparator.
     *
     * @param a         The array to be sorted.
     * @param c         The comparator used for sorting.
     * @param fromIndex The starting index of the range to be sorted.
     * @param toIndex   The ending index of the range to be sorted (exclusive).
     * @param <T>       The type of elements in the array.
     */
    public <T> void sort(T[] a, Comparator<? super T> c, int fromIndex, int toIndex) {
        if (timSort == null) timSort = new TimSort();
        timSort.doSort(a, c, fromIndex, toIndex);
    }
}
