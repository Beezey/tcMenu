/*
 * Copyright (c)  2016-2019 https://www.thecoderscorner.com (Nutricherry LTD).
 * This product is licensed under an Apache license, see the LICENSE file in the top-level directory.
 *
 */

package com.thecoderscorner.menu.domain;

import com.thecoderscorner.menu.domain.state.MenuState;
import com.thecoderscorner.menu.domain.state.StringListMenuState;
import com.thecoderscorner.menu.domain.util.MenuItemVisitor;

import java.util.List;
import java.util.Objects;

public class RuntimeListMenuItem extends MenuItem<List<String>> {
    private final int initialRows;
    public RuntimeListMenuItem() {
        super("", 0, 0, "", false, false, true);
        initialRows = 0;
    }

    public RuntimeListMenuItem(String name, int id, int eepromAddress, String functionName, boolean readOnly,
                               boolean localOnly, boolean visible, int initialRows) {
        super(name, id, eepromAddress, functionName, readOnly, localOnly, visible);
        this.initialRows = initialRows;
    }

    public int getInitialRows() {
        return initialRows;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RuntimeListMenuItem that = (RuntimeListMenuItem) o;
        return getInitialRows() == that.getInitialRows() &&
                getId() == that.getId() &&
                getEepromAddress() == that.getEepromAddress() &&
                isReadOnly() == that.isReadOnly() &&
                isLocalOnly() == that.isLocalOnly() &&
                Objects.equals(getName(), that.getName()) &&
                Objects.equals(getFunctionName(), that.getFunctionName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getInitialRows(), getName(), getId(), getEepromAddress(), getFunctionName(), isReadOnly());
    }

    @Override
    public MenuState<List<String>> newMenuState(List<String> value, boolean changed, boolean active) {
        return new StringListMenuState(this, changed, active, value);
    }

    @Override
    public void accept(MenuItemVisitor visitor) {
        visitor.visit(this);
    }
}
