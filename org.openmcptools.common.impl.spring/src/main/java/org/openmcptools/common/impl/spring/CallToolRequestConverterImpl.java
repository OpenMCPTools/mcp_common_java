package org.openmcptools.common.impl.spring;

import org.openmcptools.common.client.CallToolRequest;
import org.osgi.service.component.annotations.Component;

import io.modelcontextprotocol.spec.McpSchema;

@Component(immediate = true, service = CallToolRequestConverter.class)
public class CallToolRequestConverterImpl implements CallToolRequestConverter {

	@Override
	public CallToolRequest convertTo(io.modelcontextprotocol.spec.McpSchema.CallToolRequest source) {
		return new CallToolRequest(source.name(), source.arguments(), source.meta());
	}

	@Override
	public io.modelcontextprotocol.spec.McpSchema.CallToolRequest convertFrom(CallToolRequest target) {
		return new McpSchema.CallToolRequest(target.getToolName(), target.getArguments(), target.getMeta());
	}

}
