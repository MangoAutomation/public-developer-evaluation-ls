/*
 * Copyright (C) 2021  Radix IoT Inc. All rights reserved.
 */
package com.radixiot.example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SimpleEntityService implements ServiceInterface<SimpleEntityVO> {

	private final SimpleEntityDao dao;
	private final Map<Integer, SimpleEntityTask> tasks = new HashMap<>();
	private final ExecutorService executorService = Executors.newCachedThreadPool();

	@Autowired
	public SimpleEntityService(SimpleEntityDao dao) {
		this.dao = dao;
	}

	@Override
	public List<SimpleEntityVO> getAll() {
		return dao.getAll();
	}

	@Override
	public SimpleEntityVO get(final int id) {
		SimpleEntityVO vo = dao.get(id);
		if (vo == null) {
			throw new SimpleEntityNotFoundException(id);
		} else {
			return vo;
		}
	}

	@Override
	public void insert(SimpleEntityVO vo) {
		dao.insert(vo);
	}

	@Override
	public void update(SimpleEntityVO vo) {
		// Ensure exists
		get(vo.getId());
		dao.update(vo);
	}

	@Override
	public boolean delete(int id) {
		// Ensure exists
		get(id);
		return dao.delete(id);
	}

	public synchronized void start(final AtomicInteger id) throws Exception {
		SimpleEntityTask t = this.tasks.get(id.get());
		if (t == null) {
			t = new SimpleEntityTask();
			executorService.execute(t);
			this.tasks.put(id.get(), t);
		}
		
		System.out.println(tasks.keySet());
	}

	public synchronized void stop(final int id) throws Exception {
		SimpleEntityTask t = this.tasks.remove(id);
		if (t == null) {
			throw new Exception("Task not running");
		}
		t.stop();
	}

	private static class SimpleEntityTask implements Runnable {
		boolean running = true;

		@Override
		public void run() {
			try {
				while (running) {
					Thread.sleep(100);
				}
			} catch (InterruptedException e) {
			}
		}

		public void stop() {
			running = false;
		}
	}
}
