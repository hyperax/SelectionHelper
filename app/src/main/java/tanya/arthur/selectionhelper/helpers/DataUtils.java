package tanya.arthur.selectionhelper.helpers;

import android.support.annotation.NonNull;

import com.annimon.stream.Optional;

import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class DataUtils {

    private static final Random random = new Random();

    @NonNull
    public static <T> T getFirst(List<T> list, @NonNull T ifEmptyValue) {
        T firstItem = null;
        if (!NpeUtils.isEmpty(list)) {
            firstItem = list.get(0);
        }
        return NpeUtils.getNonNull(firstItem, ifEmptyValue);
    }

    @NonNull
    public static <T> Optional<T> getFirst(List<T> list) {
        T firstItem = null;
        if (!NpeUtils.isEmpty(list)) {
            firstItem = list.get(0);
        }
        return Optional.ofNullable(firstItem);
    }

    @NonNull
    public static <T> T getLast(List<T> list, @NonNull T ifEmptyValue) {
        T lastItem = null;
        if (!NpeUtils.isEmpty(list)) {
            lastItem = list.get(list.size() - 1);
        }
        return NpeUtils.getNonNull(lastItem, ifEmptyValue);
    }

    @NonNull
    public static <T> Optional<T> getLast(List<T> list) {
        T lastItem = null;
        if (!NpeUtils.isEmpty(list)) {
            lastItem = list.get(list.size() - 1);
        }
        return Optional.ofNullable(lastItem);
    }

    @NonNull
    public static <T> T getRandom(T[] array, @NonNull T ifEmptyValue) {
        T result = null;

        if (!NpeUtils.isEmpty(array)) {
            result = array[random.nextInt(array.length)];
        }
        return NpeUtils.getNonNull(result, ifEmptyValue);
    }

    @NonNull
    public static <T> Optional<T> getRandom(T[] array) {
        T result = null;

        if (!NpeUtils.isEmpty(array)) {
            result = array[random.nextInt(array.length)];
        }
        return Optional.ofNullable(result);
    }

    @NonNull
    public static <T> T getRandom(Collection<T> collection, @NonNull T ifEmptyValue) {
        T result = null;

        if (!NpeUtils.isEmpty(collection)) {
            int randomIndex = random.nextInt(collection.size());
            int counter = 0;
            for (T item: collection) {
                if (counter == randomIndex) {
                    result = item;
                    break;
                }
                ++counter;
            }
        }
        return NpeUtils.getNonNull(result, ifEmptyValue);
    }

    @NonNull
    public static <T> Optional<T> getRandom(Collection<T> collection) {
        T result = null;

        if (!NpeUtils.isEmpty(collection)) {
            int randomIndex = random.nextInt(collection.size());
            int counter = 0;
            for (T item: collection) {
                if (counter == randomIndex) {
                    result = item;
                    break;
                }
                ++counter;
            }
        }
        return Optional.ofNullable(result);
    }

    @NonNull
    public static <T> T getRandom(List<T> list, @NonNull T ifEmptyValue) {
        T result = null;

        if (!NpeUtils.isEmpty(list)) {
            result = list.get(random.nextInt(list.size()));
        }

        return NpeUtils.getNonNull(result, ifEmptyValue);
    }

    @NonNull
    public static <T> Optional<T> getRandom(List<T> list) {
        T result = null;

        if (!NpeUtils.isEmpty(list)) {
            result = list.get(random.nextInt(list.size()));
        }

        return Optional.ofNullable(result);
    }

    @NonNull
    public static String getUUID() {
        return UUID.randomUUID().toString();
    }

    public static int indexOf(long[] array, long value) {
        int result = -1;
        int size = NpeUtils.getNonNull(array).length;
        for (int i = 0; i < size; i++) {
            if (array[i] == value) {
                result = i;
                break;
            }
        }
        return result;
    }

    public static int indexOf(int[] array, int value) {
        int result = -1;
        int size = NpeUtils.getNonNull(array).length;
        for (int i = 0; i < size; i++) {
            if (array[i] == value) {
                result = i;
                break;
            }
        }
        return result;
    }
}
