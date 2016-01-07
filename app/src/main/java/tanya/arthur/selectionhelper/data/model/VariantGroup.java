package tanya.arthur.selectionhelper.data.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import nl.qbusict.cupboard.annotation.Column;
import tanya.arthur.selectionhelper.data.model.contract.Contract;
import tanya.arthur.selectionhelper.helpers.NpeUtils;

public class VariantGroup {

    @Column(Contract.ID)
    private Long id;

    @Column(Contract.VariantGroup.NAME)
    private String name;

    public VariantGroup() {
        setId(null);
        setName(null);
    }

    @Nullable
    public Long getId() {
        return id;
    }

    @NonNull
    public VariantGroup setId(Long id) {
        this.id = id;
        return this;
    }

    @NonNull
    public String getName() {
        return NpeUtils.getNonNull(name);
    }

    @NonNull
    public VariantGroup setName(String name) {
        this.name = name;
        return this;
    }
}
