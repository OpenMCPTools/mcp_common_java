package org.openmcptools.common.server.toolgroup.impl.spring;

import java.lang.reflect.Method;
import java.util.function.BiFunction;
import java.util.stream.Stream;

import org.openmcptools.common.model.Tool;
import org.openmcptools.common.server.impl.spring.SpringToolNodeProvider;
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

public class AsyncToolGroupProvider
		extends AbstractSpringToolGroupProvider<AsyncToolSpecification, McpAsyncServerExchange, Mono<CallToolResult>> {

	public AsyncToolGroupProvider() {
		this.toolProvider = new SpringToolNodeProvider.Async();
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
	protected ToolSpecification<AsyncToolSpecification> getToolNodeSpecification(Tool toolNode,
			BiFunction<McpAsyncServerExchange, CallToolRequest, Mono<CallToolResult>> callHandler) {
		AsyncToolSpecification.Builder specBuilder = AsyncToolSpecification.builder().tool(convertToolNode(toolNode))
				.callHandler(callHandler);
		return new ToolSpecification<AsyncToolSpecification>(toolNode, specBuilder.build());
	}

}
