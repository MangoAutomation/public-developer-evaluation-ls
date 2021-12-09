/*
 * Copyright (C) 2021  Radix IoT Inc. All rights reserved.
 */
package com.radixiot.example;

import java.util.List;

public interface ServiceInterface<T> {

    List<T> getAll();

    T get(int id);

    void insert(T entity);

    void update(T entity);

    boolean delete(int id);

}
