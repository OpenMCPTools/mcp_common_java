package org.openmcptools.common.client.toolgroup.impl.spring;

import java.util.function.Supplier;

import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.common.McpTransportContext;

public class McpSyncToolGroupClient extends McpSyncClient {

	public McpSyncToolGroupClient(McpAsyncToolGroupClient delegate, Supplier<McpTransportContext> contextProvider) {
		super(delegate, contextProvider);
	}

}
