package org.openmcptools.common.toolgroup.server.impl.spring;

import java.util.Dictionary;
import java.util.Map;

import org.openmcptools.common.toolgroup.server.MCPToolGroupServerConfig;
import org.openmcptools.common.toolgroup.server.ToolGroupServer;
import org.openmcptools.transport.server.MCPServerTransportProvider;

import io.modelcontextprotocol.json.McpJsonMapper;
import io.modelcontextprotocol.json.schema.JsonSchemaValidator;
import io.modelcontextprotocol.spec.McpSchema.JSONRPCMessage;
import io.modelcontextprotocol.spec.McpSchema.ServerCapabilities;
import io.modelcontextprotocol.util.McpUriTemplateManagerFactory;
import reactor.core.publisher.Mono;

public class AbstractMCPToolGroupServerConfig extends MCPToolGroupServerConfig<Mono<Void>, Mono<?>, JSONRPCMessage> {

	protected ServerCapabilities serverCapabilities;
	protected McpJsonMapper jsonMapper;
	protected McpUriTemplateManagerFactory uriTemplateManagerFactory;
	protected JsonSchemaValidator jsonSchemaValidator;

	public AbstractMCPToolGroupServerConfig(
			MCPServerTransportProvider<Mono<Void>, Mono<?>, JSONRPCMessage> transportProvider) {
		super(transportProvider);
	}

	public AbstractMCPToolGroupServerConfig(String serverName, String serverTitle,
			MCPServerTransportProvider<Mono<Void>, Mono<?>, JSONRPCMessage> transportProvider) {
		super(serverName, serverTitle, transportProvider);
	}

	public AbstractMCPToolGroupServerConfig(String serverName, String serverTitle, String serverVersion,
			MCPServerTransportProvider<Mono<Void>, Mono<?>, JSONRPCMessage> transportProvider, Long requestTimeout,
			String serverInstructions) {
		super(serverName, serverTitle, serverVersion, transportProvider, requestTimeout, serverInstructions);
	}

	public AbstractMCPToolGroupServerConfig(String serverName, String serverTitle, String serverVersion,
			MCPServerTransportProvider<Mono<Void>, Mono<?>, JSONRPCMessage> transportProvider) {
		super(serverName, serverTitle, serverVersion, transportProvider);
	}

	public AbstractMCPToolGroupServerConfig(Map<String, Object> properties) {
		super(properties);
		this.serverCapabilities = (ServerCapabilities) properties.get(ToolGroupServer.SERVER_CAPABILITIES);
		if (serverCapabilities == null) {
			serverCapabilities = ServerCapabilities.builder().tools(true).build();
		} else {
			serverCapabilities = serverCapabilities.mutate().tools(true).build();
		}
	}

	public AbstractMCPToolGroupServerConfig setServerCapabilities(ServerCapabilities serverCapabilities) {
		this.serverCapabilities = serverCapabilities;
		return this;
	}

	public AbstractMCPToolGroupServerConfig setJsonMapper(McpJsonMapper jsonMapper) {
		this.jsonMapper = jsonMapper;
		return this;
	}

	public AbstractMCPToolGroupServerConfig setUriTemplateManagerFactory(
			McpUriTemplateManagerFactory uriTemplateManagerFactory) {
		this.uriTemplateManagerFactory = uriTemplateManagerFactory;
		return this;
	}

	public AbstractMCPToolGroupServerConfig setJsonSchemaValidator(JsonSchemaValidator jsonSchemaValidator) {
		this.jsonSchemaValidator = jsonSchemaValidator;
		return this;
	}

	@Override
	public Dictionary<String, Object> asProperties() {
		Dictionary<String, Object> d = super.asProperties();
		if (serverCapabilities == null) {
			serverCapabilities = ServerCapabilities.builder().build();
		}
		d.put(ToolGroupServer.SERVER_CAPABILITIES, serverCapabilities);
		if (jsonMapper != null) {
			d.put(ToolGroupServer.SERVER_JSONMAPPER, jsonMapper);
		}
		if (uriTemplateManagerFactory != null) {
			d.put(ToolGroupServer.SERVER_URI_TEMPLATE_MANAGER_FACTORY, uriTemplateManagerFactory);
		}
		if (jsonSchemaValidator != null) {
			d.put(ToolGroupServer.SERVER_JSONSCHEMAVALIDATOR, jsonSchemaValidator);
		}
		return d;
	}

}
