package org.openmcptools.common.impl.spring;

import org.openmcptools.common.client.CallToolResult;
import org.openmcptools.common.model.Converter;

import io.modelcontextprotocol.spec.McpSchema;

public interface CallToolResultConverter extends Converter<CallToolResult, McpSchema.CallToolResult> {

}
