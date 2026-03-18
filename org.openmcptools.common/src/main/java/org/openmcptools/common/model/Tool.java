package org.openmcptools.common.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a tool that can be called by a model.
 * A tool defines input and output schemas and can belong to parent groups.
 */
public class Tool extends AbstractLeaf {

	protected String inputSchema;
	protected String outputSchema;
	protected ToolAnnotations toolAnnotations;

	/**
	 * Constructs a Tool with the given name.
	 * 
	 * @param name the name of the tool
	 */
	public Tool(String name) {
		super(name);
	}

	/**
	 * Returns a new builder for creating a Tool.
	 * 
	 * @param name the name of the tool
	 * @return the builder
	 */
	public static Builder builder(String name) {
		return new Builder(name);
	}

	/**
	 * Builder class for constructing Tool instances.
	 */
	public static class Builder {

		private String name;
		private String title;
		private String description;
		private String inputSchema;
		private String outputSchema;
		private ToolAnnotations annotations;
		private Map<String, Object> meta;
		private List<Group> parents;

		/**
		 * Constructs a Builder with the required name.
		 * 
		 * @param name the tool name
		 * @throws NullPointerException if name is null
		 */
		public Builder(String name) {
			Objects.requireNonNull(name, "name must not be null");
			this.name = name;
			this.parents = new ArrayList<Group>();
		}

		public Builder title(String title) {
			this.title = title;
			return this;
		}

		public Builder description(String description) {
			this.description = description;
			return this;
		}

		public Builder inputSchema(String inputSchema) {
			this.inputSchema = inputSchema;
			return this;
		}

		public Builder outputSchema(String outputSchema) {
			this.outputSchema = outputSchema;
			return this;
		}

		public Builder annotations(ToolAnnotations annotations) {
			this.annotations = annotations;
			return this;
		}

		public Builder meta(Map<String, Object> meta) {
			this.meta = meta;
			return this;
		}

		/**
		 * Adds a parent group to the tool being built.
		 * 
		 * @param g the group to add
		 * @return this builder
		 */
		public Builder addParent(Group g) {
			if (g != null) {
				this.parents.add(g);
			}
			return this;
		}

		/**
		 * Builds the Tool instance.
		 * 
		 * @return the new Tool
		 */
		public Tool build() {
			Tool t = new Tool(name);
			t.setDescription(description);
			t.setTitle(title);
			t.setInputSchema(inputSchema);
			t.setOutputSchema(outputSchema);
			t.setToolAnnotations(annotations);
			this.parents.forEach(pg -> t.addParentGroup(pg));
			t.setMeta(meta);
			return t;
		}
	}

	/**
	 * Gets the JSON schema for tool inputs.
	 * 
	 * @return the input schema
	 */
	public String getInputSchema() {
		return inputSchema;
	}

	public void setInputSchema(String inputSchema) {
		this.inputSchema = inputSchema;
	}

	/**
	 * Gets the JSON schema for tool outputs.
	 * 
	 * @return the output schema
	 */
	public String getOutputSchema() {
		return outputSchema;
	}

	public void setOutputSchema(String outputSchema) {
		this.outputSchema = outputSchema;
	}

	/**
	 * Gets the tool-specific annotations/hints.
	 * 
	 * @return the tool annotations
	 */
	public ToolAnnotations getToolAnnotations() {
		return toolAnnotations;
	}

	public void setToolAnnotations(ToolAnnotations toolAnnotations) {
		this.toolAnnotations = toolAnnotations;
	}

	@Override
	public String toString() {
		return "Tool [name=" + name + ", fqName=" + getFullyQualifiedName() + ", title=" + title + ", description="
				+ description + ", meta=" + meta + ", inputSchema=" + inputSchema + ", outputSchema=" + outputSchema
				+ ", toolAnnotation=" + toolAnnotations + "]";
	}
}

