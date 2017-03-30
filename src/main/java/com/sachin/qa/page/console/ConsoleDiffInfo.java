package com.sachin.qa.page.console;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.sachin.qa.app.AppConstants;
import com.sachin.qa.page.Featurable;
import com.sachin.qa.page.PageInfo;
import com.sachin.qa.page.diff.DiffInfo;

public class ConsoleDiffInfo extends DiffInfo {
	private static final long serialVersionUID = 1L;

	public ConsoleDiffInfo(PageInfo preInfo, PageInfo postInfo) {
		super(".jsLog", preInfo, postInfo);
		this.resourceFolder = AppConstants.DIFF_FOLDER + File.separator + "jsLogs";
		new File(this.resourceFolder).mkdirs();
	}

	@Override
	public DiffInfo call() throws Exception {
		File file = new File(this.resourceFolder, super.name + ".jsCommon");
		File pre = new File(this.resourceFolder, super.name + ".jsPreOnly");
		File post = new File(this.resourceFolder, super.name + ".jsPostOnly");
		properties.put("jsCommon", "");
		properties.put("jsPreDiff", "");
		properties.put("jsPostDiff", "");
		String preLogs = getFeatureType(preInfo.getTypes()).getResourcePath();
		String postLogs = getFeatureType(postInfo.getTypes()).getResourcePath();
		List<String> preList = FileUtils.readLines(new File(preLogs), "utf-8");
		List<String> postList = FileUtils.readLines(new File(postLogs), "utf-8");
		List<String> list1 = new ArrayList<>(preList.subList(3, preList.size()));
		List<String> list2 = new ArrayList<>(postList.subList(3, postList.size()));
		if (list1.removeAll(postList)) {
			list1.add(0, preList.get(0));
			list1.add(1, preList.get(1));
			list1.add(2, preList.get(2));
			FileUtils.writeLines(pre, "utf-8", list1);
			properties.put("jsPreDiff", pre.getAbsolutePath());
		}
		if (list2.removeAll(preList)) {
			list2.add(0, preList.get(0));
			list2.add(1, preList.get(1));
			list2.add(2, preList.get(2));
			FileUtils.writeLines(post, "utf-8", list2);
			properties.put("jsPostDiff", post.getAbsolutePath());
		}
		list1 = null;
		list2 = null;
		if (postList.retainAll(preList)) {
			FileUtils.writeLines(file, "utf-8", postList);
			properties.put("jsPostDiff", file.getAbsolutePath());
		}
		return this;
	}

	private Featurable getFeatureType(List<Featurable> types) {
		for (Featurable f : types) {
			if (f.getType().equals(".jsLog"))
				return f;
		}
		return null;
	}

}