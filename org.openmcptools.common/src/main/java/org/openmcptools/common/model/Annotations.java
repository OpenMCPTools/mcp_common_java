package org.openmcptools.common.model;

import java.util.List;

public class Annotations {

	private List<Role> audience;

	private Double priority;

	private String lastModified;

	public Annotations(List<Role> audience, Double priority, String lastModified) {
		this.audience = audience;
		this.priority = priority;
		this.lastModified = lastModified;
	}

	public List<Role> getAudience() {
		return audience;
	}

	public void setAudience(List<Role> audience) {
		this.audience = audience;
	}

	public Double getPriority() {
		return priority;
	}

	public void setPriority(Double priority) {
		this.priority = priority;
	}

	public String getLastModified() {
		return lastModified;
	}

	public void setLastModified(String lastModified) {
		this.lastModified = lastModified;
	}

	@Override
	public String toString() {
		return "AnnotationsNode [audience=" + audience + ", priority=" + priority + ", lastModified=" + lastModified
				+ "]";
	}

}
