/*
 * Copyright (C) 2021  Radix IoT Inc. All rights reserved.
 */
package com.radixiot.example;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SimpleEntityRestController {

	private final SimpleEntityService service;
	private final Function<SimpleEntityVO, SimpleEntityRestModel> map;
	private final Function<SimpleEntityRestModel, SimpleEntityVO> unmap;

	@Autowired
	SimpleEntityRestController(SimpleEntityService service) {
		this.service = service;
		this.map = vo -> {
			SimpleEntityRestModel model = new SimpleEntityRestModel();
			model.setId(vo.getId());
			model.setXid(vo.getXid());
			model.setEnabled(vo.isEnabled());
			model.setName(vo.getName());
			return model;
		};

		this.unmap = model -> {
			SimpleEntityVO vo = new SimpleEntityVO();
			vo.setId(model.getId());
			vo.setXid(model.getXid());
			vo.setEnabled(model.isEnabled());
			vo.setName(model.getName());
			return vo;
		};
	}

	@RequestMapping(method = RequestMethod.GET, value = "/simple-entities")
	List<SimpleEntityRestModel> all() {
		return service.getAll().stream().map(map).collect(Collectors.toList());
	}

	@RequestMapping(method = RequestMethod.POST, value = "/simple-entities")
	SimpleEntityRestModel newSimpleEntity(@RequestBody SimpleEntityRestModel entity) {
		SimpleEntityVO vo = unmap.apply(entity);
		service.insert(vo);
		return map.apply(vo);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/simple-entities/{id}")
	SimpleEntityRestModel get(@PathVariable int id) {
		return map.apply(service.get(id));
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/simple-entities/{id}")
	SimpleEntityRestModel put(@RequestBody SimpleEntityRestModel newEntity, @PathVariable int id) {
		newEntity.setId(id);
		service.update(unmap.apply(newEntity));
		return newEntity;
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/simple-entities/{id}")
	void delete(@PathVariable int id) {
		service.delete(id);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/simple-entities/start/{id}")
	void start(@PathVariable final int id) throws Exception {
		service.start(new AtomicInteger(id));
	}

	@RequestMapping(method = RequestMethod.POST, value = "/simple-entities/stop/{id}")
	void stop(@PathVariable final int id) throws Exception {
		service.stop(id);
	}
}
