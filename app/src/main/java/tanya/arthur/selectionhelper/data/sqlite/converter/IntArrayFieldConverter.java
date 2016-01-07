package tanya.arthur.selectionhelper.data.sqlite.converter;

import android.content.ContentValues;
import android.database.Cursor;

import nl.qbusict.cupboard.convert.EntityConverter;
import nl.qbusict.cupboard.convert.FieldConverter;
import tanya.arthur.selectionhelper.helpers.ConvertUtils;
import tanya.arthur.selectionhelper.helpers.NpeUtils;

public class IntArrayFieldConverter implements FieldConverter<int[]> {

    @Override
    public int[] fromCursorValue(Cursor cursor, int columnIndex) {
        String[] stringArray = cursor.getString(columnIndex).split(",");
        int[] intArray = new int[stringArray.length];
        int size = stringArray.length;
        for (int i = 0; i < size; i++) {
            try {
                intArray[i] = Integer.parseInt(stringArray[i]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        return intArray;
    }

    @Override
    public void toContentValue(int[] value, String key, ContentValues values) {
        values.put(key, ConvertUtils.join(NpeUtils.getNonNull(value), ","));
    }

    @Override
    public EntityConverter.ColumnType getColumnType() {
        return EntityConverter.ColumnType.INTEGER;
    }
}
