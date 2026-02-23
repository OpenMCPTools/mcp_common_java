package org.openmcptools.common.toolgroup.server.impl.spring;

import io.modelcontextprotocol.server.McpSyncServer;

public class SDKSyncToolGroupServer extends McpSyncServer {

	public SDKSyncToolGroupServer(SDKAsyncToolGroupServer asyncServer) {
		super(asyncServer);
	}

	public SDKSyncToolGroupServer(SDKAsyncToolGroupServer asyncServer, boolean immediate) {
		super(asyncServer, immediate);
	}

}