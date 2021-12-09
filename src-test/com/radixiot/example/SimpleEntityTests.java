/*
 * Copyright (C) 2021  Radix IoT Inc. All rights reserved.
 */
package com.radixiot.example;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SimpleEntityTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    private void cleanDatabase() {
        //Remove our initialized sample data created at startup or previous tests
        SimpleEntityRestModel[] models = this.restTemplate.getForObject("http://localhost:" + port + "/simple-entities/", SimpleEntityRestModel[].class);
        for(SimpleEntityRestModel model : models) {
            this.restTemplate.delete("http://localhost:" + port + "/simple-entities/" + model.getId());
        }
    }

    @Test
    public void assertGet() {
        SimpleEntityRestModel created = createEntity(3);
        SimpleEntityRestModel model = this.restTemplate.getForObject("http://localhost:" + port + "/simple-entities/" + created.getId(), SimpleEntityRestModel.class);
        assertThat(model).hasNoNullFieldsOrProperties();
        assertThat(model).hasFieldOrPropertyWithValue("name", created.getName());
        assertThat(model).hasFieldOrPropertyWithValue("enabled", created.isEnabled());
        assertThat(model).hasFieldOrPropertyWithValue("xid", created.getXid());
    }

    @Test
    public void assertGetAll() {
        SimpleEntityRestModel one = createEntity(1);
        SimpleEntityRestModel two = createEntity(2);
        SimpleEntityRestModel[] models = this.restTemplate.getForObject("http://localhost:" + port + "/simple-entities/", SimpleEntityRestModel[].class);
        assertThat(models[0]).hasFieldOrPropertyWithValue("id", one.getId());
        assertThat(models[0]).hasFieldOrPropertyWithValue("name", one.getName());
        assertThat(models[0]).hasFieldOrPropertyWithValue("enabled", one.isEnabled());
        assertThat(models[0]).hasFieldOrPropertyWithValue("xid", one.getXid());

        assertThat(models[1]).hasFieldOrPropertyWithValue("id", two.getId());
        assertThat(models[1]).hasFieldOrPropertyWithValue("name", two.getName());
        assertThat(models[1]).hasFieldOrPropertyWithValue("enabled", two.isEnabled());
        assertThat(models[1]).hasFieldOrPropertyWithValue("xid", two.getXid());
    }

    @Test
    public void assertPost() {
        SimpleEntityVO vo3 = new SimpleEntityVO();
        vo3.setXid("XID_3");
        vo3.setName("Name 3");
        vo3.setEnabled(true);

        SimpleEntityRestModel model = this.restTemplate.postForObject("http://localhost:" + port + "/simple-entities/", vo3, SimpleEntityRestModel.class);
        assertThat(model).hasNoNullFieldsOrProperties();
        assertThat(model).hasFieldOrPropertyWithValue("name", "Name 3");
        assertThat(model).hasFieldOrPropertyWithValue("enabled", true);
        assertThat(model).hasFieldOrPropertyWithValue("xid", "XID_3");

        model = this.restTemplate.getForObject("http://localhost:" + port + "/simple-entities/" + model.getId(), SimpleEntityRestModel.class);
        assertThat(model).hasFieldOrPropertyWithValue("name", "Name 3");
        assertThat(model).hasFieldOrPropertyWithValue("enabled", true);
        assertThat(model).hasFieldOrPropertyWithValue("xid", "XID_3");
    }

    @Test
    public void assertPut() {
        SimpleEntityRestModel one = createEntity(1);

        SimpleEntityRestModel model = this.restTemplate.getForObject("http://localhost:" + port + "/simple-entities/" + one.getId(), SimpleEntityRestModel.class);
        assertThat(model).hasNoNullFieldsOrProperties();
        assertThat(model).hasFieldOrPropertyWithValue("name", one.getName());
        assertThat(model).hasFieldOrPropertyWithValue("enabled", one.isEnabled());
        assertThat(model).hasFieldOrPropertyWithValue("xid", one.getXid());

        //Modify
        model.setName("Changed");
        model.setXid("XID_99");
        model.setEnabled(!model.isEnabled());

        this.restTemplate.put("http://localhost:" + port + "/simple-entities/" + model.getId(), model);
        SimpleEntityRestModel modified = this.restTemplate.getForObject("http://localhost:" + port + "/simple-entities/" + model.getId(), SimpleEntityRestModel.class);
        assertThat(modified).hasFieldOrPropertyWithValue("id", model.getId());
        assertThat(modified).hasFieldOrPropertyWithValue("name", model.getName());
        assertThat(modified).hasFieldOrPropertyWithValue("enabled", model.isEnabled());
        assertThat(modified).hasFieldOrPropertyWithValue("xid", model.getXid());
    }

    @Test
    public void assertDelete() {
        SimpleEntityRestModel one = createEntity(1);

        this.restTemplate.delete("http://localhost:" + port + "/simple-entities/" + one.getId());
        ResponseEntity<String> response = this.restTemplate.getForEntity("http://localhost:" + port + "/simple-entities/" + one.getId(), String.class);
        assertThat(response).hasFieldOrPropertyWithValue("status", 404);
    }

    private SimpleEntityRestModel createEntity(int number) {
        SimpleEntityVO vo = newEntityVO(number);
        SimpleEntityRestModel model = new SimpleEntityRestModel();
        model.setName(vo.getName());
        model.setXid(vo.getXid());
        model.setEnabled(vo.isEnabled());
        return this.restTemplate.postForObject("http://localhost:" + port + "/simple-entities/", model, SimpleEntityRestModel.class);
    }

    private SimpleEntityVO newEntityVO(int number) {
        SimpleEntityVO vo = new SimpleEntityVO();
        vo.setXid(UUID.randomUUID().toString());
        vo.setName("Name " + number);
        vo.setEnabled(true);
        return vo;
    }
}
