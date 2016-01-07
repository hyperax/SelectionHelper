package tanya.arthur.selectionhelper.data.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.SimpleArrayMap;

import nl.qbusict.cupboard.annotation.Column;
import tanya.arthur.selectionhelper.data.model.contract.Contract;
import tanya.arthur.selectionhelper.helpers.DataUtils;
import tanya.arthur.selectionhelper.helpers.NpeUtils;

public class ComparisonScore {

    @Column(Contract.ID)
    private Long id;

    @Column(Contract.ComparisonScore.COMPARISON_ID)
    private long comparisonId;

    @Column(Contract.ComparisonScore.VARIANT_ID)
    private long variantId;

    @Column(Contract.ComparisonScore.CRITERIA_IDS)
    private long[] criteriaIds;

    @Column(Contract.ComparisonScore.SCORES)
    private int[] scores;

    public ComparisonScore() {
        setId(null);
        setVariantId(0L);
        setScores(null);
    }


    @Nullable
    public Long getId() {
        return id;
    }

    @NonNull
    public ComparisonScore setId(Long id) {
        this.id = id;
        return this;
    }

    public long getComparisonId() {
        return comparisonId;
    }

    public ComparisonScore setComparisonId(long comparisonId) {
        this.comparisonId = comparisonId;
        return this;
    }

    public long getVariantId() {
        return variantId;
    }

    @NonNull
    public ComparisonScore setVariantId(long variantId) {
        this.variantId = variantId;
        return this;
    }

    public int getScore(long criteriaId) {
        int result = 0;
        int index = DataUtils.indexOf(criteriaIds, criteriaId);
        if (index >= 0) {
            result = scores[index];
        }
        return result;
    }

    @NonNull
    public ComparisonScore putScore(long criteriaId, int score) {
        int index = DataUtils.indexOf(criteriaIds, criteriaId);
        if (index >= 0) {
            scores[index] = score;
        }
        return this;
    }

    @NonNull
    public SimpleArrayMap<Long, Integer> getScores() {
        SimpleArrayMap<Long, Integer> resultMap = new SimpleArrayMap<>();
        for (int i = 0; i < criteriaIds.length; i++) {
            resultMap.put(criteriaIds[i], scores[i]);
        }
        return resultMap;
    }

    @NonNull
    public ComparisonScore setScores(SimpleArrayMap<Long, Integer> scoreMap) {
        if (scoreMap != null) {
            int size = scoreMap.size();
            for (int i = 0; i < size; i++) {
                criteriaIds[i] = scoreMap.keyAt(i);
                scores[i] = scoreMap.valueAt(i);
            }
        } else {
            this.criteriaIds = NpeUtils.EMPTY_LONG_ARRAY;
            this.scores = NpeUtils.EMPTY_INT_ARRAY;
        }
        return this;
    }
}
