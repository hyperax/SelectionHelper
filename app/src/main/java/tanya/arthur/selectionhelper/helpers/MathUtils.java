package tanya.arthur.selectionhelper.helpers;

import android.support.annotation.NonNull;

import java.math.BigDecimal;

public abstract class MathUtils {

    private MathUtils() {
    }

    public static boolean greater(BigDecimal first, BigDecimal second) {
        return first.compareTo(second) == 1;
    }

    public static boolean greaterOrEqual(BigDecimal first, BigDecimal second) {
        return first.compareTo(second) >= 0;
    }

    public static boolean equal(BigDecimal first, BigDecimal second) {
        return first.compareTo(second) == 0;
    }

    public static boolean isPositive(BigDecimal value) {
        return greater(value, BigDecimal.ZERO);
    }

    public static boolean isNegative(BigDecimal value) {
        return greater(BigDecimal.ZERO, value);
    }

    @NonNull
    public static BigDecimal getValue(String plainString) {
        BigDecimal resultValue;
        if (NpeUtils.isEmpty(plainString)) {
            resultValue = BigDecimal.ZERO;
        } else {
            try {
                resultValue = new BigDecimal(plainString);
            } catch (Exception e) {
                resultValue = BigDecimal.ZERO;
            }
        }
        return resultValue;
    }

    public static int getFractionalCount(BigDecimal value) {
        String string = value.stripTrailingZeros().toPlainString();
        int index = string.indexOf(".");
        return index < 0 ? 0 : string.length() - index - 1;
    }

    public static int getIntegerCount(BigDecimal value) {
        int absIntValue = value.abs().intValue();
        return absIntValue > 0? String.valueOf(absIntValue).length() : 0;
    }

    public static boolean isValueMultiple(BigDecimal value, BigDecimal multiplicity) {
        return equal(value.remainder(multiplicity), BigDecimal.ZERO);
    }

    @NonNull
    public static String toPlainString(BigDecimal value) {
        return NpeUtils.getNonNull(value).toPlainString();
    }

    public static int compare(long lhs, long rhs) {
        return lhs < rhs ? -1 : (lhs == rhs ? 0 : 1);
    }

    public static int compare(int lhs, int rhs) {
        return lhs < rhs ? -1 : (lhs == rhs ? 0 : 1);
    }
}
