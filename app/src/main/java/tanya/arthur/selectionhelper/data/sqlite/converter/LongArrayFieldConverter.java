package tanya.arthur.selectionhelper.data.sqlite.converter;

import android.content.ContentValues;
import android.database.Cursor;

import nl.qbusict.cupboard.convert.EntityConverter;
import nl.qbusict.cupboard.convert.FieldConverter;
import tanya.arthur.selectionhelper.helpers.ConvertUtils;
import tanya.arthur.selectionhelper.helpers.NpeUtils;

public class LongArrayFieldConverter implements FieldConverter<long[]> {

    @Override
    public long[] fromCursorValue(Cursor cursor, int columnIndex) {
        String[] stringArray = cursor.getString(columnIndex).split(",");
        long[] longArray = new long[stringArray.length];
        int size = stringArray.length;
        for (int i = 0; i < size; i++) {
            try {
                longArray[i] = Long.parseLong(stringArray[i]);
            } catch (NumberFormatException e) {
                // ignore
            }
        }

        return longArray;
    }

    @Override
    public void toContentValue(long[] value, String key, ContentValues values) {
        values.put(key, ConvertUtils.join(NpeUtils.getNonNull(value), ","));
    }

    @Override
    public EntityConverter.ColumnType getColumnType() {
        return EntityConverter.ColumnType.INTEGER;
    }
}
