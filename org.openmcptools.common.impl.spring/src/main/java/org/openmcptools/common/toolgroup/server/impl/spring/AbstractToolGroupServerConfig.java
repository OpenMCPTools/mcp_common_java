package org.openmcptools.common.toolgroup.server.impl.spring;

import java.util.Dictionary;
import java.util.Map;

import org.openmcptools.common.toolgroup.server.ToolGroupServer;
import org.openmcptools.common.toolgroup.server.ToolGroupServerConfig;

import io.modelcontextprotocol.json.McpJsonMapper;
import io.modelcontextprotocol.json.schema.JsonSchemaValidator;
import io.modelcontextprotocol.spec.McpSchema.ServerCapabilities;
import io.modelcontextprotocol.spec.McpServerTransportProvider;
import io.modelcontextprotocol.util.McpUriTemplateManagerFactory;

public class AbstractToolGroupServerConfig extends ToolGroupServerConfig<McpServerTransportProvider> {

	protected ServerCapabilities serverCapabilities;
	protected McpJsonMapper jsonMapper;
	protected McpUriTemplateManagerFactory uriTemplateManagerFactory;
	protected JsonSchemaValidator jsonSchemaValidator;

	public AbstractToolGroupServerConfig(McpServerTransportProvider transport) {
		super(transport);
	}

	public AbstractToolGroupServerConfig(String serverName, String serverTitle, McpServerTransportProvider transport) {
		super(serverName, serverTitle, transport);
	}

	public AbstractToolGroupServerConfig(String serverName, String serverTitle, String serverVersion,
			McpServerTransportProvider transport, Long requestTimeout, String serverInstructions) {
		super(serverName, serverTitle, serverVersion, transport, requestTimeout, serverInstructions);
	}

	public AbstractToolGroupServerConfig(String serverName, String serverTitle, String serverVersion,
			McpServerTransportProvider transport) {
		super(serverName, serverTitle, serverVersion, transport);
	}

	public AbstractToolGroupServerConfig(Map<String, Object> properties) {
		super(properties);
		this.serverCapabilities = (ServerCapabilities) properties.get(ToolGroupServer.SERVER_CAPABILITIES);
		if (serverCapabilities == null) {
			serverCapabilities = ServerCapabilities.builder().tools(true).build();
		} else {
			serverCapabilities = serverCapabilities.mutate().tools(true).build();
		}
	}

	public AbstractToolGroupServerConfig setServerCapabilities(ServerCapabilities serverCapabilities) {
		this.serverCapabilities = serverCapabilities;
		return this;
	}

	public AbstractToolGroupServerConfig setJsonMapper(McpJsonMapper jsonMapper) {
		this.jsonMapper = jsonMapper;
		return this;
	}

	public AbstractToolGroupServerConfig setUriTemplateManagerFactory(
			McpUriTemplateManagerFactory uriTemplateManagerFactory) {
		this.uriTemplateManagerFactory = uriTemplateManagerFactory;
		return this;
	}

	public AbstractToolGroupServerConfig setJsonSchemaValidator(JsonSchemaValidator jsonSchemaValidator) {
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
