package org.openmcptools.common.impl.spring;

import org.openmcptools.common.client.CallToolRequest;
import org.openmcptools.common.model.Converter;

import io.modelcontextprotocol.spec.McpSchema;

public interface CallToolRequestConverter extends Converter<CallToolRequest, McpSchema.CallToolRequest> {

}
