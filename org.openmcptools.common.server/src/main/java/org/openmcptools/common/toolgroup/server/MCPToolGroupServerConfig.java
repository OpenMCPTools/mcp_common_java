package org.openmcptools.common.toolgroup.server;

import java.util.Map;

import org.openmcptools.transport.server.MCPServerTransportProvider;

public class MCPToolGroupServerConfig<AV, AR, M> extends ToolGroupServerConfig<MCPServerTransportProvider<AV, AR, M>> {

	public MCPToolGroupServerConfig(Map<String, Object> properties) {
		super(properties);
	}

	public MCPToolGroupServerConfig(MCPServerTransportProvider<AV, AR, M> transportProvider) {
		super(transportProvider);
	}

	public MCPToolGroupServerConfig(String serverName, String serverTitle,
			MCPServerTransportProvider<AV, AR, M> transportProvider) {
		super(serverName, serverTitle, transportProvider);
	}

	public MCPToolGroupServerConfig(String serverName, String serverTitle, String serverVersion,
			MCPServerTransportProvider<AV, AR, M> transportProvider, Long requestTimeout, String serverInstructions) {
		super(serverName, serverTitle, serverVersion, transportProvider, requestTimeout, serverInstructions);
	}

	public MCPToolGroupServerConfig(String serverName, String serverTitle, String serverVersion,
			MCPServerTransportProvider<AV, AR, M> transportProvider) {
		super(serverName, serverTitle, serverVersion, transportProvider);
	}

}
