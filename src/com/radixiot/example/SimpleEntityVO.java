/*
 * Copyright (C) 2021  Radix IoT Inc. All rights reserved.
 */
package com.radixiot.example;

import java.util.Objects;

public class SimpleEntityVO {
    private int id;
    private String xid;
    private boolean enabled;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getXid() {
        return xid;
    }

    public void setXid(String xid) {
        this.xid = xid;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimpleEntityVO that = (SimpleEntityVO) o;
        return id == that.id && enabled == that.enabled && Objects.equals(xid, that.xid) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, xid, enabled, name);
    }

    @Override
    public String toString() {
        return "SimpleEntityVO{" +
                "id=" + id +
                ", xid='" + xid + '\'' +
                ", enabled=" + enabled +
                ", name='" + name + '\'' +
                '}';
    }
}
