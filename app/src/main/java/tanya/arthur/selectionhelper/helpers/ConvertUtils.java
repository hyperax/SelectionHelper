package tanya.arthur.selectionhelper.helpers;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConvertUtils {
    private ConvertUtils() {
    }

    @NonNull
    public static long[] toArray(List<Long> list) {
        int size = NpeUtils.getNonNull(list).size();
        long[] array = new long[size];
        for (int i = 0; i < size; i++) {
            array[i] = list.get(i);
        }
        return array;
    }

    @NonNull
    public static String[] toStrArray(List<String> list) {
        int size = NpeUtils.getNonNull(list).size();
        String[] array = new String[size];
        for (int i = 0; i < size; i++) {
            array[i] = list.get(i);
        }
        return array;
    }

    @NonNull
    public static <T> List<T> toList(T[] array) {
        List<T> list;
        if (NpeUtils.isEmpty(array)) {
            list = new ArrayList<>();
        } else {
            list = Arrays.asList(array);
        }
        return list;
    }

    @NonNull
    public static List<Long> toList(long[] array) {
        int size = NpeUtils.getNonNull(array).length;
        List<Long> list = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            list.add(array[i]);
        }
        return list;
    }

    @NonNull
    public static String toMd5(String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest.getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    @NonNull
    public static String join(Iterable iterable, CharSequence delimiter) {
        return TextUtils.join(delimiter, iterable);
    }

    @NonNull
    public static String join(Object[] array, CharSequence delimiter) {
        return TextUtils.join(delimiter, array);
    }

    @NonNull
    public static String join(long[] array, CharSequence delimiter) {
        String result = "";
        for (int i = 0; i < array.length; i++) {
            result += array[i];
            if (i != array.length - 1) {
                result += delimiter;
            }
        }
        return result;
    }

    @NonNull
    public static String join(int[] array, CharSequence delimiter) {
        String result = "";
        for (int i = 0; i < array.length; i++) {
            result += array[i];
            if (i != array.length - 1) {
                result += delimiter;
            }
        }
        return result;
    }

    public static long safeParseLong(String s) {
        long result;

        try {
            result = Long.parseLong(s);
        } catch (Exception e) {
            result = 0L;
        }

        return result;
    }
}
