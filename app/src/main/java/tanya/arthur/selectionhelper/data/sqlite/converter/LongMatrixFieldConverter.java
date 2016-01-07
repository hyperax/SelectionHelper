package tanya.arthur.selectionhelper.data.sqlite.converter;

import android.content.ContentValues;
import android.database.Cursor;

import nl.qbusict.cupboard.convert.EntityConverter;
import nl.qbusict.cupboard.convert.FieldConverter;
import tanya.arthur.selectionhelper.helpers.ConvertUtils;
import tanya.arthur.selectionhelper.helpers.NpeUtils;

public class LongMatrixFieldConverter implements FieldConverter<long[][]> {

    private final static String arraysSplitter = ";";
    private final static String valuesSplitter = ",";

    @Override
    public long[][] fromCursorValue(Cursor cursor, int columnIndex) {
        String[] string1Array = cursor.getString(columnIndex).split(arraysSplitter);
        int size1 = string1Array.length;
        long[][] longArray = new long[size1][];
        for (int i = 0; i < size1; i++) {
            String[] string2Array = string1Array[i].split(valuesSplitter);
            int size2 = string2Array.length;
            longArray[i] = new long[size2];
            for (int j = 0; j < size2; j++) {
                try {
                    longArray[i][j] = Long.parseLong(string2Array[j]);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }
        return longArray;
    }

    @Override
    public void toContentValue(long[][] value, String key, ContentValues values) {
       if (value == null) {
           values.put(key, NpeUtils.EMPTY_STRING);
       } else {
           int size = value.length;
           String[] stringArray = new String[size];
           for (int i = 0; i < size; i++) {
               stringArray[i] = ConvertUtils.join(value[i], valuesSplitter);
           }
           values.put(key, ConvertUtils.join(stringArray,arraysSplitter));
       }
    }

    @Override
    public EntityConverter.ColumnType getColumnType() {
        return EntityConverter.ColumnType.TEXT;
    }
}
