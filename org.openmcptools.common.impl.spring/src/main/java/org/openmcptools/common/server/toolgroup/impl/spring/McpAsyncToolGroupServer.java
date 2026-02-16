package org.openmcptools.common.server.toolgroup.impl.spring;

import java.time.Duration;

import io.modelcontextprotocol.json.McpJsonMapper;
import io.modelcontextprotocol.json.schema.JsonSchemaValidator;
import io.modelcontextprotocol.server.McpAsyncServer;
import io.modelcontextprotocol.server.McpServerFeatures.Async;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpServerTransportProvider;
import io.modelcontextprotocol.spec.McpStreamableServerTransportProvider;
import io.modelcontextprotocol.util.McpUriTemplateManagerFactory;
import reactor.core.publisher.Mono;

public class McpAsyncToolGroupServer extends McpAsyncServer {

	public McpAsyncToolGroupServer(McpServerTransportProvider mcpTransportProvider, McpJsonMapper jsonMapper,
			Async features, Duration requestTimeout, McpUriTemplateManagerFactory uriTemplateManagerFactory,
			JsonSchemaValidator jsonSchemaValidator) {
		super(mcpTransportProvider, jsonMapper, features, requestTimeout, uriTemplateManagerFactory,
				jsonSchemaValidator);
	}

	public McpAsyncToolGroupServer(McpStreamableServerTransportProvider mcpTransportProvider, McpJsonMapper jsonMapper,
			Async features, Duration requestTimeout, McpUriTemplateManagerFactory uriTemplateManagerFactory,
			JsonSchemaValidator jsonSchemaValidator) {
		super(mcpTransportProvider, jsonMapper, features, requestTimeout, uriTemplateManagerFactory,
				jsonSchemaValidator);
	}

	@Override
	public Mono<Void> notifyToolsListChanged() {
		// XXX create params
		// 
		return this.mcpTransportProvider.notifyClients(McpSchema.METHOD_NOTIFICATION_TOOLS_LIST_CHANGED, null);
	}
}
