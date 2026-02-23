package org.openmcptools.common.toolgroup.client.impl.spring;

import java.util.function.Supplier;

import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.common.McpTransportContext;

public class SDKSyncToolGroupClient extends McpSyncClient {

	public SDKSyncToolGroupClient(SDKAsyncToolGroupClient delegate, Supplier<McpTransportContext> contextProvider) {
		super(delegate, contextProvider);
	}

}
