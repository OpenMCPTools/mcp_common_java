package org.openmcptools.common.toolgroup.client;

import java.util.Map;

import org.openmcptools.transport.client.MCPClientTransport;

public class MCPToolGroupClientConfig<AV, ARCQ, ARS, M>
		extends ToolGroupClientConfig<MCPClientTransport<AV, ARCQ, ARS, M>> {

	public MCPToolGroupClientConfig(Map<String, Object> properties) {
		super(properties);
	}

	public MCPToolGroupClientConfig(MCPClientTransport<AV, ARCQ, ARS, M> transport, Long requestTimeout) {
		super(transport, requestTimeout);
	}

	public MCPToolGroupClientConfig(String clientName, String clientVersion,
			MCPClientTransport<AV, ARCQ, ARS, M> transport, Long requestTimeout) {
		super(clientName, clientVersion, transport, requestTimeout);
	}

	public MCPToolGroupClientConfig(String clientName, String clientTitle, String clientVersion,
			MCPClientTransport<AV, ARCQ, ARS, M> transport, Long requestTimeout) {
		super(clientName, clientTitle, clientVersion, transport, requestTimeout);
	}

}
