package tanya.arthur.selectionhelper.data.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import nl.qbusict.cupboard.annotation.Column;
import tanya.arthur.selectionhelper.data.model.contract.Contract;
import tanya.arthur.selectionhelper.helpers.NpeUtils;

public class ComparisonInfo {

    @Column(Contract.ID)
    private Long id;

    @Column(Contract.ComparisonInfo.NAME)
    private String name;

    @Column(Contract.ComparisonInfo.DATE_START)
    private long dateStartMills;

    @Column(Contract.ComparisonInfo.DATE_END)
    private long dateEndMills;

    @Column(Contract.ComparisonInfo.CRITERIA_GROUP_ID)
    private long criteriaGroupId;

    @Column(Contract.ComparisonInfo.VARIANT_GROUP_ID)
    private long variantGroupId;

    public ComparisonInfo() {
        setId(null);
        setName(null);
        setDateStartMills(0L);
        setDateEndMills(0L);
        setCriteriaGroupId(0L);
        setVariantGroupId(0L);
    }

    @Nullable
    public Long getId() {
        return id;
    }

    @NonNull
    public ComparisonInfo setId(Long id) {
        this.id = id;
        return this;
    }

    @NonNull
    public String getName() {
        return NpeUtils.getNonNull(name);
    }

    @NonNull
    public ComparisonInfo setName(String name) {
        this.name = name;
        return this;
    }

    public long getDateStartMills() {
        return dateStartMills;
    }

    @NonNull
    public ComparisonInfo setDateStartMills(long dateStartMills) {
        this.dateStartMills = dateStartMills;
        return this;
    }

    public long getDateEndMills() {
        return dateEndMills;
    }

    @NonNull
    public ComparisonInfo setDateEndMills(long dateEndMills) {
        this.dateEndMills = dateEndMills;
        return this;
    }

    public long getCriteriaGroupId() {
        return criteriaGroupId;
    }

    @NonNull
    public ComparisonInfo setCriteriaGroupId(long criteriaGroupId) {
        this.criteriaGroupId = criteriaGroupId;
        return this;
    }

    public long getVariantGroupId() {
        return variantGroupId;
    }

    @NonNull
    public ComparisonInfo setVariantGroupId(long variantGroupId) {
        this.variantGroupId = variantGroupId;
        return this;
    }

}
