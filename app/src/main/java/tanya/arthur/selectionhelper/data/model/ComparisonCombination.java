package tanya.arthur.selectionhelper.data.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import nl.qbusict.cupboard.annotation.Column;
import tanya.arthur.selectionhelper.data.model.contract.Contract;
import tanya.arthur.selectionhelper.helpers.NpeUtils;

public class ComparisonCombination {

    @Column(Contract.ID)
    private Long id;

    @Column(Contract.ComparisonCombination.COMPARISON_INFO_ID)
    private long comparisonInfoId;

    @Column(Contract.ComparisonCombination.COMBINATIONS_NUMBER)
    private int combinationsNumber;

    @Column(Contract.ComparisonCombination.COMBINATION)
    private long[][] combinationResultIds;

    @Column(Contract.ComparisonCombination.CURRENT_COMBINATION_INDEX)
    private int combinationIndex;

    public ComparisonCombination() {
        setId(null);
        setComparisonInfoId(0L);
        setCombinationsNumber(0);
        setCombinationResultIds(new long[0][0]);
        setCombinationIndex(0);
    }

    @Nullable
    public Long getId() {
        return id;
    }

    @NonNull
    public ComparisonCombination setId(Long id) {
        this.id = id;
        return this;
    }

    public long getComparisonInfoId() {
        return comparisonInfoId;
    }

    @NonNull
    public ComparisonCombination setComparisonInfoId(long comparisonInfoId) {
        this.comparisonInfoId = comparisonInfoId;
        return this;
    }

    public int getCombinationsNumber() {
        return combinationsNumber;
    }

    @NonNull
    public ComparisonCombination setCombinationsNumber(int combinationsNumber) {
        this.combinationsNumber = combinationsNumber;
        return this;
    }

    @NonNull
    public long[][] getCombinationResultIds() {
        return NpeUtils.getNonNull(combinationResultIds);
    }

    @NonNull
    public ComparisonCombination setCombinationResultIds(long[][] combinationResultIds) {
        this.combinationResultIds = combinationResultIds;
        return this;
    }

    public int getCombinationIndex() {
        return combinationIndex;
    }

    @NonNull
    public ComparisonCombination setCombinationIndex(int combinationIndex) {
        this.combinationIndex = combinationIndex;
        return this;
    }
}
