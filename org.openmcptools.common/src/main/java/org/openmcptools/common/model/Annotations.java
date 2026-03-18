package org.openmcptools.common.model;

import java.util.List;

/**
 * Represents annotations for MCP entities, including audience roles and priority.
 */
public class Annotations {

	private List<Role> audience;
	private Double priority;
	private String lastModified;

	/**
	 * Constructs Annotations with audience, priority, and last modified date.
	 * 
	 * @param audience the roles targeted by these annotations
	 * @param priority the priority value
	 * @param lastModified the last modified timestamp string
	 */
	public Annotations(List<Role> audience, Double priority, String lastModified) {
		this.audience = audience;
		this.priority = priority;
		this.lastModified = lastModified;
	}

	/**
	 * Constructs Annotations with audience and priority.
	 * 
	 * @param audience the roles targeted by these annotations
	 * @param priority the priority value
	 */
	public Annotations(List<Role> audience, Double priority) {
		this(audience, priority, null);
	}

	/**
	 * Gets the audience roles.
	 * 
	 * @return the list of roles
	 */
	public List<Role> getAudience() {
		return audience;
	}

	/**
	 * Sets the audience roles.
	 * 
	 * @param audience the list of roles
	 */
	public void setAudience(List<Role> audience) {
		this.audience = audience;
	}

	/**
	 * Gets the priority.
	 * 
	 * @return the priority
	 */
	public Double getPriority() {
		return priority;
	}

	/**
	 * Sets the priority.
	 * 
	 * @param priority the priority value
	 */
	public void setPriority(Double priority) {
		this.priority = priority;
	}

	/**
	 * Gets the last modified timestamp.
	 * 
	 * @return the timestamp string
	 */
	public String getLastModified() {
		return lastModified;
	}

	/**
	 * Sets the last modified timestamp.
	 * 
	 * @param lastModified the timestamp string
	 */
	public void setLastModified(String lastModified) {
		this.lastModified = lastModified;
	}

	@Override
	public String toString() {
		return "Annotations [audience=" + audience + ", priority=" + priority + ", lastModified=" + lastModified + "]";
	}
}

