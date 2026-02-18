package org.openmcptools.common.server.toolgroup.impl.spring;

import io.modelcontextprotocol.server.McpSyncServer;

public class McpSyncToolGroupServer extends McpSyncServer {

	public McpSyncToolGroupServer(McpAsyncToolGroupServer asyncServer) {
		super(asyncServer);
	}

	public McpSyncToolGroupServer(McpAsyncToolGroupServer asyncServer, boolean immediate) {
		super(asyncServer, immediate);
	}

	public void startToolsUpdate() {
		((McpAsyncToolGroupServer) this.asyncServer).startToolsUpdate();
	}

	public void endToolsUpdate() {
		((McpAsyncToolGroupServer) this.asyncServer).endToolsUpdate();
	}

}