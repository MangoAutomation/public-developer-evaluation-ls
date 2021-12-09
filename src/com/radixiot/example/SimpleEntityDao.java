/*
 * Copyright (C) 2021  Radix IoT Inc. All rights reserved.
 */
package com.radixiot.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Select;
import org.jooq.exception.NoDataFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.radixiot.example.db.tables.SimpleEntity;

@Repository
public class SimpleEntityDao {

    private final DSLContext create;
    private final SimpleEntity table;

    @Autowired
    public SimpleEntityDao(DSLContext create) {
        this.create = create;
        this.table = SimpleEntity.SIMPLE_ENTITY;
    }

    public SimpleEntityVO get(int id) {
        return this.create
                .select(getSelectFields())
                .from(table)
                .where(table.id.eq(id))
                .limit(1)
                .fetchOne(this::mapRecord);
    }

    public List<SimpleEntityVO> getAll() {
        List<SimpleEntityVO> items = new ArrayList<>();
        getAll(items::add);
        return items;
    }

    public void getAll(Consumer<SimpleEntityVO> callback) {
        Select<Record> select = this.create.select(getSelectFields()).from(table);
        try(Stream<Record> stream = select.stream()) {
            stream.map(this::mapRecord)
                    .filter(Objects::nonNull)
                    .forEach(callback);
        }
    }

    public void insert(SimpleEntityVO vo) {
        int id = create
                .insertInto(table)
                .set(toRecord(vo))
                .returningResult(table.id)
                .fetchOptional()
                .orElseThrow(NoDataFoundException::new)
                .value1();
        vo.setId(id);
    }

    public void update(SimpleEntityVO vo) {
        create.update(table).set(toRecord(vo)).where(table.id.eq(vo.getId())).execute();
    }

    /**
     *
     * @param id
     * @return true if deleted, false otherwise
     */
    public boolean delete(int id) {
        int deleted = create.deleteFrom(table).where(table.id.eq(id)).execute();
        return deleted > 0;
    }

    public List<Field<?>> getSelectFields() {
        return Arrays.stream(table.fields()).collect(Collectors.toCollection(ArrayList::new));
    }

    protected Record toRecord(SimpleEntityVO vo) {
        Record record = table.newRecord();
        record.set(table.xid, vo.getXid());
        record.set(table.enabled, boolToChar(vo.isEnabled()));
        return record;
    }

    protected SimpleEntityVO mapRecord(Record record) {
        SimpleEntityVO vo = new SimpleEntityVO();
        vo.setId(record.get(table.id));
        vo.setXid(record.get(table.xid));
        vo.setEnabled(charToBool(record.get(table.enabled)));
        return vo;
    }

    public static final String Y = "Y";
    public static final String N = "N";

    public static boolean charToBool(String s) {
        return Y.equals(s);
    }
    public static String boolToChar(boolean b) {
        return b ? Y : N;
    }
}
