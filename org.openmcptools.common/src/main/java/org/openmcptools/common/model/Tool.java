package org.openmcptools.common.model;

public class Tool extends AbstractLeaf {

	protected String inputSchema;

	protected String outputSchema;

	protected ToolAnnotations toolAnnotations;

	public Tool(String name) {
		super(name);
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
		return "ToolNode [name=" + name + ", fqName=" + getFullyQualifiedName() + ", title=" + title + ", description="
				+ description + ", meta=" + meta + ", inputSchema=" + inputSchema + ", outputSchema=" + outputSchema
				+ ", toolAnnotation=" + toolAnnotations + "]";
	}

}
