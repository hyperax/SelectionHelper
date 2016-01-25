package tanya.arthur.selectionhelper.data.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import nl.qbusict.cupboard.annotation.Column;
import tanya.arthur.selectionhelper.data.model.contract.Contract;
import tanya.arthur.selectionhelper.helpers.NpeUtils;
import tanya.arthur.selectionhelper.view.adapters.choice.NamedItem;

public class CriteriaGroup implements NamedItem{
    @Column(Contract.ID)
    private Long id;

    @Column(Contract.CriteriaGroup.NAME)
    private String name;

    public CriteriaGroup() {
        setId(null);
        setName(null);
    }

    @Nullable
    public Long getId() {
        return id;
    }

    @NonNull
    public CriteriaGroup setId(Long id) {
        this.id = id;
        return this;
    }

    @NonNull
    public String getName() {
        return NpeUtils.getNonNull(name);
    }

    @NonNull
    public CriteriaGroup setName(String name) {
        this.name = name;
        return this;
    }
}
