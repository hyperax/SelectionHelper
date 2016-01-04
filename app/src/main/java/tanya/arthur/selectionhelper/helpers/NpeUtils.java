package tanya.arthur.selectionhelper.helpers;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class NpeUtils {

    public static final String[] EMPTY_STRING_ARRAY = new String[0];
    public static final long[] EMPTY_LONG_ARRAY = new long[0];
    public static final int[] EMPTY_INT_ARRAY = new int[0];
    public static final String EMPTY_STRING = "";

    @NonNull
    public static <T> T getNonNull(T value, @NonNull T ifNullValue) {
        return value == null? ifNullValue : value;
    }

    @NonNull
    public static <T> List<T> getNonNull(List<T> value) {
        return value == null? Collections.<T>emptyList() : value;
    }

    @NonNull
    public static String getNonNull(String value) {
        return getNonNull(value, EMPTY_STRING);
    }

    @NonNull
    public static long[] getNonNull(long[] value) {
        return getNonNull(value, EMPTY_LONG_ARRAY);
    }

    @NonNull
    public static String[] getNonNull(String[] value) {
        return getNonNull(value, EMPTY_STRING_ARRAY);
    }

    @NonNull
    public static int[] getNonNull(int[] value) {
        return getNonNull(value, EMPTY_INT_ARRAY);
    }

    @NonNull
    public static BigDecimal getNonNull(BigDecimal value) {
        return getNonNull(value, BigDecimal.ZERO);
    }

    public static <T> boolean isEmpty(T[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(int[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(long[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(Collection collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isEmpty(String string) {
        return TextUtils.isEmpty(string);
    }

    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    public static boolean equals(Object a, Object b) {
        return (a == null) ? (b == null) : a.equals(b);
    }

    @NonNull
    public static Class getListTypeClass(List<?> list) {
        Class elementType;

        if (isEmpty(list)) {
            final Class<? extends List> listClass = list.getClass();
            final ParameterizedType genericSuperclass = (ParameterizedType) listClass.getGenericSuperclass();
            elementType = (Class) genericSuperclass.getActualTypeArguments()[0];
        } else {
            elementType = list.get(0).getClass();
        }

        return elementType;
    }
}
