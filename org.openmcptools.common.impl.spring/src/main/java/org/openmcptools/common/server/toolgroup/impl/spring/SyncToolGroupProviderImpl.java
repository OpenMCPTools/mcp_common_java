package org.openmcptools.common.server.toolgroup.impl.spring;

import java.lang.reflect.Method;
import java.util.function.BiFunction;
import java.util.stream.Stream;

import org.openmcptools.common.model.Tool;
import org.openmcptools.common.model.ToolConverter;
import org.openmcptools.common.server.toolgroup.ToolProvider;
import org.openmcptools.common.server.toolgroup.ToolSpecification;
import org.springaicommunity.mcp.McpPredicates;
import org.springaicommunity.mcp.method.tool.ReturnMode;
import org.springaicommunity.mcp.method.tool.SyncMcpToolMethodCallback;

import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.server.McpSyncServerExchange;
import io.modelcontextprotocol.spec.McpSchema.CallToolRequest;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;

public class SyncToolGroupProviderImpl
		extends AbstractToolGroupProviderImpl<SyncToolSpecification, McpSyncServerExchange, CallToolResult> {

	public SyncToolGroupProviderImpl(ToolProvider toolProvider,
			ToolConverter<io.modelcontextprotocol.spec.McpSchema.Tool> toolConverter, boolean generateOutputSchema) {
		super(toolProvider, toolConverter, generateOutputSchema);
	}

	@Override
	protected Stream<Method> filterMethodStream(Stream<Method> inputStream) {
		return inputStream.filter(McpPredicates.filterReactiveReturnTypeMethod());
	}

	@Override
	protected ReturnMode getReturnMode(boolean useStructuredOutput, Method mcpToolMethod) {
		Class<?> returnType = mcpToolMethod.getReturnType();
		return useStructuredOutput ? ReturnMode.STRUCTURED
				: (returnType == Void.TYPE || returnType == void.class ? ReturnMode.VOID : ReturnMode.TEXT);
	}

	@Override
	protected BiFunction<McpSyncServerExchange, CallToolRequest, CallToolResult> getCallHandler(Method mcpToolMethod,
			Object toolObject, boolean useStructuredOutput) {
		return new SyncMcpToolMethodCallback(getReturnMode(useStructuredOutput, mcpToolMethod), mcpToolMethod,
				toolObject, getToolCallException());

	}

	@Override
	protected ToolSpecification<SyncToolSpecification> getToolNodeSpecification(Tool tool,
			BiFunction<McpSyncServerExchange, CallToolRequest, CallToolResult> callHandler) {
		SyncToolSpecification.Builder specBuilder = SyncToolSpecification.builder().tool(convertTool(tool))
				.callHandler(callHandler);
		return new ToolSpecification<SyncToolSpecification>(tool, specBuilder.build());
	}

}
