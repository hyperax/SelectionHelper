package tanya.arthur.selectionhelper.data;

import android.content.Context;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import tanya.arthur.selectionhelper.R;
import tanya.arthur.selectionhelper.data.model.ComparisonInfo;
import tanya.arthur.selectionhelper.data.sqlite.DataQuery;

@EBean(scope = EBean.Scope.Singleton)
public class Logic {

    @RootContext
    Context context;

    @Bean
    DataQuery query;

    public ComparisonInfo createComparisonInfo() {
        return new ComparisonInfo()
                .setName(context.getString(R.string.new_comparison));
    }

    public boolean saveComparisonInfo(ComparisonInfo comparisonInfo) {
        return query.put(comparisonInfo) > 0;
    }
}
