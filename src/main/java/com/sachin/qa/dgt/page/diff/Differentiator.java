package com.sachin.qa.dgt.page.diff;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.sachin.qa.dgt.utils.StreamUtils;

public class Differentiator implements Runnable, Serializable {
	private static final long serialVersionUID = 1L;
	private List<DiffInfo> diffInfos;
	private final String name;
	private transient ExecutorService es = Executors.newCachedThreadPool();
	private transient List<Future<DiffInfo>> futures;

	public Differentiator(String name) {
		this.name = name;
		diffInfos = new ArrayList<>();
		futures = new ArrayList<>();
	}

	@Override
	public void run() {
		for (DiffInfo diff : diffInfos) {
			futures.add(es.submit(diff));
		}
		diffInfos.clear();
		for (Future<DiffInfo> diff : futures) {
			try {
				diffInfos.add(diff.get());
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}
		StreamUtils.writeDiffInfo(this);
	}

	public List<DiffInfo> getDiffInfos() {
		return diffInfos;
	}

	public String getName() {
		return name;
	}

	public void register(DiffInfo diffInfo) {
		diffInfos.add(diffInfo);
	}

}
