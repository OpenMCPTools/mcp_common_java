package org.openmcptools.common.server.toolgroup.impl.spring;

import java.lang.reflect.Method;

import org.openmcptools.common.model.ToolConverter;
import org.openmcptools.common.server.toolgroup.AbstractToolGroupProvider;
import org.springaicommunity.mcp.method.tool.ReturnMode;

import io.modelcontextprotocol.spec.McpSchema.CallToolRequest;
import io.modelcontextprotocol.spec.McpSchema.Tool;

public abstract class AbstractSpringToolGroupProvider<SpecificationType, ExchangeType, CallToolResultType>
		extends AbstractToolGroupProvider<SpecificationType, Tool, ExchangeType, CallToolRequest, CallToolResultType> {

	protected ToolConverter<Tool> toolConverter;

	protected Tool convertToolNode(org.openmcptools.common.model.Tool toolNode) {
		return this.toolConverter.convertFromTool(toolNode);
	}

	protected abstract ReturnMode getReturnMode(boolean useStructuredOutput, Method mcpToolMethod);

	protected Class<? extends Throwable> getToolCallException() {
		return Exception.class;
	}

}
