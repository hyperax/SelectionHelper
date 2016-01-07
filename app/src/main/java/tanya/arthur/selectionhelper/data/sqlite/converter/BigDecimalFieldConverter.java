package tanya.arthur.selectionhelper.data.sqlite.converter;

import android.content.ContentValues;
import android.database.Cursor;

import java.math.BigDecimal;

import nl.qbusict.cupboard.convert.EntityConverter;
import nl.qbusict.cupboard.convert.FieldConverter;
import tanya.arthur.selectionhelper.helpers.MathUtils;

public class BigDecimalFieldConverter implements FieldConverter<BigDecimal> {
    @Override
    public BigDecimal fromCursorValue(Cursor cursor, int columnIndex) {
        return MathUtils.getValue(cursor.getString(columnIndex));
    }

    @Override
    public void toContentValue(BigDecimal value, String key, ContentValues contentValues) {
        contentValues.put(key, MathUtils.toPlainString(value));
    }

    @Override
    public EntityConverter.ColumnType getColumnType() {
        return EntityConverter.ColumnType.TEXT;
    }
}
