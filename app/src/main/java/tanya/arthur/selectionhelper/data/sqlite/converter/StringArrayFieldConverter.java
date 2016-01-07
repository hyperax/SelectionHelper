package tanya.arthur.selectionhelper.data.sqlite.converter;

import android.content.ContentValues;
import android.database.Cursor;

import nl.qbusict.cupboard.convert.EntityConverter;
import nl.qbusict.cupboard.convert.FieldConverter;
import tanya.arthur.selectionhelper.helpers.ConvertUtils;
import tanya.arthur.selectionhelper.helpers.NpeUtils;

public class StringArrayFieldConverter implements FieldConverter<String[]> {

    @Override
    public String[] fromCursorValue(Cursor cursor, int columnIndex) {
        return cursor.getString(columnIndex).split(",");
    }

    @Override
    public void toContentValue(String[] value, String key, ContentValues values) {
        values.put(key, ConvertUtils.join(NpeUtils.getNonNull(value), ","));
    }

    @Override
    public EntityConverter.ColumnType getColumnType() {
        return EntityConverter.ColumnType.TEXT;
    }
}
