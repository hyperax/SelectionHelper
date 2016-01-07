package tanya.arthur.selectionhelper.data.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.math.BigDecimal;

import nl.qbusict.cupboard.Cupboard;
import nl.qbusict.cupboard.CupboardBuilder;
import nl.qbusict.cupboard.CupboardFactory;
import tanya.arthur.selectionhelper.data.model.ComparisonCombination;
import tanya.arthur.selectionhelper.data.model.ComparisonInfo;
import tanya.arthur.selectionhelper.data.model.ComparisonScore;
import tanya.arthur.selectionhelper.data.model.Criteria;
import tanya.arthur.selectionhelper.data.model.CriteriaGroup;
import tanya.arthur.selectionhelper.data.model.Variant;
import tanya.arthur.selectionhelper.data.model.VariantGroup;
import tanya.arthur.selectionhelper.data.sqlite.converter.BigDecimalFieldConverter;
import tanya.arthur.selectionhelper.data.sqlite.converter.IntArrayFieldConverter;
import tanya.arthur.selectionhelper.data.sqlite.converter.LongArrayFieldConverter;
import tanya.arthur.selectionhelper.data.sqlite.converter.LongMatrixFieldConverter;
import tanya.arthur.selectionhelper.data.sqlite.converter.StringArrayFieldConverter;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

public class SHSQLiteOpenHelper extends SQLiteOpenHelper{

    private static final String DB_FILE = "selection_helper_data.db";
    private static final int DB_VERSION = 1;

    static {
        Cupboard cupboard = new CupboardBuilder()
                .useAnnotations()
                .registerFieldConverter(int[].class, new IntArrayFieldConverter())
                .registerFieldConverter(long[].class, new LongArrayFieldConverter())
                .registerFieldConverter(long[][].class, new LongMatrixFieldConverter())
                .registerFieldConverter(String[].class, new StringArrayFieldConverter())
                .registerFieldConverter(BigDecimal.class, new BigDecimalFieldConverter())
                .build();

        cupboard.register(ComparisonCombination.class);
        cupboard.register(ComparisonInfo.class);
        cupboard.register(ComparisonScore.class);
        cupboard.register(Criteria.class);
        cupboard.register(CriteriaGroup.class);
        cupboard.register(Variant.class);
        cupboard.register(VariantGroup.class);

        CupboardFactory.setCupboard(cupboard);
    }

    public SHSQLiteOpenHelper(Context context) {
        super(context, DB_FILE, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        cupboard().withDatabase(db).createTables();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        cupboard().withDatabase(db).upgradeTables();
    }

}
