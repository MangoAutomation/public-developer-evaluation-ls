/*
 * Copyright (C) 2021  Radix IoT Inc. All rights reserved.
 */
package com.radixiot.example;

public class SimpleEntityNotFoundException extends RuntimeException {

    SimpleEntityNotFoundException(int id) {
        super("Could not find simple entity " + id);
    }
}
