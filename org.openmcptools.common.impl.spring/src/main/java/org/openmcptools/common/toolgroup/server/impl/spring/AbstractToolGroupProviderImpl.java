package org.openmcptools.common.toolgroup.server.impl.spring;

import java.lang.reflect.Method;
import java.util.Objects;

import org.openmcptools.common.impl.spring.ToolConverter;
import org.openmcptools.common.toolgroup.server.AbstractToolGroupProvider;
import org.openmcptools.common.toolgroup.server.ToolProvider;
import org.springaicommunity.mcp.method.tool.ReturnMode;

import io.modelcontextprotocol.spec.McpSchema.CallToolRequest;
import io.modelcontextprotocol.spec.McpSchema.Tool;

public abstract class AbstractToolGroupProviderImpl<SpecificationType, ExchangeType, CallToolResultType>
		extends AbstractToolGroupProvider<SpecificationType, Tool, ExchangeType, CallToolRequest, CallToolResultType> {

	protected ToolConverter toolConverter;

	protected AbstractToolGroupProviderImpl(ToolProvider toolProvider, ToolConverter toolConverter,
			boolean generateOutputSchema) {
		super(toolProvider, generateOutputSchema);
		Objects.requireNonNull(toolConverter, "toolConverter must not be null");
		this.toolConverter = toolConverter;
	}

	protected Tool convertTool(org.openmcptools.common.model.Tool tool) {
		return this.toolConverter.convertFrom(tool);
	}

	protected abstract ReturnMode getReturnMode(boolean useStructuredOutput, Method mcpToolMethod);

	protected Class<? extends Throwable> getToolCallException() {
		return Exception.class;
	}

}
