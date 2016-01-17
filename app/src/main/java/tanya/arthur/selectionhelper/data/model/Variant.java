package tanya.arthur.selectionhelper.data.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.Serializable;

import nl.qbusict.cupboard.annotation.Column;
import tanya.arthur.selectionhelper.data.model.contract.Contract;
import tanya.arthur.selectionhelper.helpers.NpeUtils;

public class Variant implements Serializable {

    @Column(Contract.ID)
    private Long id;

    @Column(Contract.Variant.GROUP_ID)
    private long groupId;

    @Column(Contract.Variant.NAME)
    private String name;

    @Column(Contract.Variant.SORT_ORDER)
    private int sortOrder;

    public Variant() {
        setId(null);
        setGroupId(0L);
        setName(null);
        setSortOrder(0);
    }

    @Nullable
    public Long getId() {
        return id;
    }

    @NonNull
    public Variant setId(Long id) {
        this.id = id;
        return this;
    }

    public long getGroupId() {
        return groupId;
    }

    @NonNull
    public Variant setGroupId(long groupId) {
        this.groupId = groupId;
        return this;
    }

    @NonNull
    public String getName() {
        return NpeUtils.getNonNull(name);
    }

    @NonNull
    public Variant setName(String name) {
        this.name = name;
        return this;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public Variant setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
        return this;
    }
}
