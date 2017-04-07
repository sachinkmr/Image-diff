package com.sapient.unilever.d2.qa.dgt.report;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.relevantcodes.extentreports.LogStatus;

public class TestCase {
	private LogStatus status;
	private String id;
	private String name;
	private Date time, endedTime;
	private List<String> cats = new ArrayList<>();
	private Set<LogStatus> logStatus = new HashSet<>();
	private String runDuration;
	private String desc;

	public Set<LogStatus> getLogStatus() {
		return logStatus;
	}

	public void setLogStatus(Set<LogStatus> logStatus) {
		this.logStatus = logStatus;
	}

	public TestCase(String name) {
		this.name = name;
	}

	public String getDesc() {
		return this.desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getRunDuration() {
		return this.runDuration;
	}

	public void setRunDuration(String runDuration) {
		this.runDuration = runDuration;
	}

	public Date getEndedTime() {
		return this.endedTime;
	}

	public void setEndedTime(Date endedTime) {
		this.endedTime = endedTime;
	}

	public List<String> getCats() {
		return this.cats;
	}

	public void setCats(String cat) {
		if (!cats.contains(cat))
			cats.add(cat);
	}

	public LogStatus getStatus() {
		return this.status;
	}

	public void setStatus(LogStatus status) {
		this.status = status;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getTime() {
		return this.time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TestCase other = (TestCase) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return name;
	}

}
