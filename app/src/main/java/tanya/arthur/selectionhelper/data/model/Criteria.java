package tanya.arthur.selectionhelper.data.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import nl.qbusict.cupboard.annotation.Column;
import tanya.arthur.selectionhelper.data.model.contract.Contract;
import tanya.arthur.selectionhelper.helpers.NpeUtils;

public class Criteria {

    @Column(Contract.ID)
    private Long id;

    @Column(Contract.Criteria.GROUP_ID)
    private long groupId;

    @Column(Contract.Criteria.NAME)
    private String name;

    public Criteria() {
        setId(null);
        setGroupId(0L);
        setName(null);
    }

    @Nullable
    public Long getId() {
        return id;
    }

    @NonNull
    public Criteria setId(Long id) {
        this.id = id;
        return this;
    }

    public long getGroupId() {
        return groupId;
    }

    @NonNull
    public Criteria setGroupId(long groupId) {
        this.groupId = groupId;
        return this;
    }

    @NonNull
    public String getName() {
        return NpeUtils.getNonNull(name);
    }

    @NonNull
    public Criteria setName(String name) {
        this.name = name;
        return this;
    }
}
