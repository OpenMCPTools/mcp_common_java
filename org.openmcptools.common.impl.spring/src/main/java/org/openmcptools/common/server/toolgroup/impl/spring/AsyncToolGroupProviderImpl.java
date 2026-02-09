package org.openmcptools.common.server.toolgroup.impl.spring;

import java.lang.reflect.Method;
import java.util.function.BiFunction;
import java.util.stream.Stream;

import org.openmcptools.common.model.Tool;
import org.openmcptools.common.model.ToolConverter;
import org.openmcptools.common.server.toolgroup.ToolProvider;
import org.openmcptools.common.server.toolgroup.ToolSpecification;
import org.springaicommunity.mcp.McpPredicates;
import org.springaicommunity.mcp.method.tool.AsyncMcpToolMethodCallback;
import org.springaicommunity.mcp.method.tool.ReactiveUtils;
import org.springaicommunity.mcp.method.tool.ReturnMode;

import io.modelcontextprotocol.server.McpAsyncServerExchange;
import io.modelcontextprotocol.server.McpServerFeatures.AsyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema.CallToolRequest;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import reactor.core.publisher.Mono;

public class AsyncToolGroupProviderImpl
		extends AbstractToolGroupProviderImpl<AsyncToolSpecification, McpAsyncServerExchange, Mono<CallToolResult>> {

	public AsyncToolGroupProviderImpl(ToolProvider toolProvider,
			ToolConverter<io.modelcontextprotocol.spec.McpSchema.Tool> toolConverter, boolean generateOutputSchema) {
		super(toolProvider, toolConverter, generateOutputSchema);
	}

	@Override
	protected Stream<Method> filterMethodStream(Stream<Method> inputStream) {
		return inputStream.filter(McpPredicates.filterNonReactiveReturnTypeMethod());
	}

	@Override
	protected ReturnMode getReturnMode(boolean useStructuredOutput, Method mcpToolMethod) {
		return useStructuredOutput ? ReturnMode.STRUCTURED
				: ReactiveUtils.isReactiveReturnTypeOfVoid(mcpToolMethod) ? ReturnMode.VOID : ReturnMode.TEXT;
	}

	@Override
	protected BiFunction<McpAsyncServerExchange, CallToolRequest, Mono<CallToolResult>> getCallHandler(
			Method mcpToolMethod, Object toolObject, boolean useStructuredOutput) {
		return new AsyncMcpToolMethodCallback(getReturnMode(useStructuredOutput, mcpToolMethod), mcpToolMethod,
				toolObject, getToolCallException());
	}

	@Override
	protected ToolSpecification<AsyncToolSpecification> getToolSpecification(Tool tool,
			BiFunction<McpAsyncServerExchange, CallToolRequest, Mono<CallToolResult>> callHandler) {
		AsyncToolSpecification.Builder specBuilder = AsyncToolSpecification.builder().tool(convertTool(tool))
				.callHandler(callHandler);
		return new ToolSpecification<AsyncToolSpecification>(tool, specBuilder.build());
	}

}
