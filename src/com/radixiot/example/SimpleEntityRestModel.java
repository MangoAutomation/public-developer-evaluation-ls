/*
 * Copyright (C) 2021  Radix IoT Inc. All rights reserved.
 */
package com.radixiot.example;

public class SimpleEntityRestModel {

    private int id;
    private String xid;
    private boolean enabled;
    private String name;

    public SimpleEntityRestModel() {}

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

}
