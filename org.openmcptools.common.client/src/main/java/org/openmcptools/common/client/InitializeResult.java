package org.openmcptools.common.client;

import java.util.Map;

public class InitializeResult {

	private final String protocolVersion;
	private final String serverName;
	private final String serverVersion;
	private final String instructions;
	private final Map<String, Object> meta;
	private final ServerCapabilities serverCapabilities;

	public InitializeResult(String protocolVersion, String serverName, String serverVersion, String instructions,
			Map<String, Object> meta, ServerCapabilities serverCapabilities) {
		super();
		this.protocolVersion = protocolVersion;
		this.serverName = serverName;
		this.serverVersion = serverVersion;
		this.instructions = instructions;
		this.meta = meta;
		this.serverCapabilities = serverCapabilities;
	}

	public String getProtocolVersion() {
		return protocolVersion;
	}

	public String getServerName() {
		return serverName;
	}

	public String getServerVersion() {
		return serverVersion;
	}

	public String getInstructions() {
		return instructions;
	}

	public Map<String, Object> getMeta() {
		return meta;
	}

	public ServerCapabilities getServerCapabilities() {
		return serverCapabilities;
	}
}
