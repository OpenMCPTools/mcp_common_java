package org.openmcptools.common.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Tool extends AbstractLeaf {

	protected String inputSchema;

	protected String outputSchema;

	protected ToolAnnotations toolAnnotations;

	public Tool(String name) {
		super(name);
	}

	public static Builder builder(String name) {
		return new Builder(name);
	}

	public static class Builder {

		private String name;

		private String title;

		private String description;

		private String inputSchema;

		private String outputSchema;

		private ToolAnnotations annotations;

		private Map<String, Object> meta;

		private List<Group> parents;

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

		public Builder addParent(Group g) {
			if (g != null) {
				this.parents.add(g);
			}
			return this;
		}

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

	public String getInputSchema() {
		return inputSchema;
	}

	public void setInputSchema(String inputSchema) {
		this.inputSchema = inputSchema;
	}

	public String getOutputSchema() {
		return outputSchema;
	}

	public void setOutputSchema(String outputSchema) {
		this.outputSchema = outputSchema;
	}

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
