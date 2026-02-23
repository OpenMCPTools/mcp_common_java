package org.openmcptools.common.toolgroup.server;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Map;
import java.util.Objects;

public class ToolGroupServerConfig<TransportType> {

	public static final String DEFAULT_SERVER_NAME = System
			.getProperty(ToolGroupServerConfig.class.getName() + ".defaultServerName", "Default MCP Server Name");
	public static final String DEFAULT_SERVER_TITLE = System
			.getProperty(ToolGroupServerConfig.class.getName() + ".defaultServerTitle");
	public static final String DEFAULT_SERVER_VERSION = System
			.getProperty(ToolGroupServerConfig.class.getName() + ".defaultServerVersion", "0.0.1");
	public static final Long DEFAULT_SERVER_REQUEST_TIMEOUT = Long.parseLong(
			System.getProperty(ToolGroupServerConfig.class.getName() + ".defaultServerRequestTimeout", "10"));

	protected final String serverName;
	protected final String serverTitle;
	protected final String serverVersion;
	protected final TransportType transport;
	protected final Long requestTimeout;
	protected final String serverInstructions;

	public ToolGroupServerConfig(String serverName, String serverTitle, String serverVersion, TransportType transport,
			Long requestTimeout, String serverInstructions) {
		super();
		Objects.requireNonNull(serverName, "serverName must not be null");
		this.serverName = serverName;
		this.serverTitle = serverTitle;
		Objects.requireNonNull(serverVersion, "serverVersion must not be null");
		this.serverVersion = serverVersion;
		Objects.requireNonNull(transport, "transport must not be null");
		this.transport = transport;
		Objects.requireNonNull(requestTimeout, "requestTimeout must not be null");
		this.requestTimeout = requestTimeout;
		this.serverInstructions = serverInstructions;
	}

	public ToolGroupServerConfig(String serverName, String serverTitle, String serverVersion, TransportType transport) {
		this(serverName, serverTitle, serverVersion, transport, DEFAULT_SERVER_REQUEST_TIMEOUT, null);
	}

	public ToolGroupServerConfig(String serverName, String serverTitle, TransportType transport) {
		this(serverName, null, serverTitle, transport);
	}

	public ToolGroupServerConfig(TransportType transport) {
		this(DEFAULT_SERVER_NAME, DEFAULT_SERVER_VERSION, transport);
	}

	@SuppressWarnings("unchecked")
	public ToolGroupServerConfig(Map<String, Object> properties) {
		this((String) properties.get(ToolGroupServer.SERVER_NAME),
				(String) properties.get(ToolGroupServer.SERVER_TITLE),
				(String) properties.get(ToolGroupServer.SERVER_VERSION),
				(TransportType) properties.get(ToolGroupServer.SERVER_TRANSPORT),
				(Long) properties.get(ToolGroupServer.SERVER_REQUEST_DURATION),
				(String) properties.get(ToolGroupServer.SERVER_INSTRUCTIONS));
	}

	public Dictionary<String, Object> asProperties() {
		Hashtable<String, Object> result = new Hashtable<String, Object>();
		result.put(ToolGroupServer.SERVER_NAME, this.serverName);
		if (this.serverTitle != null) {
			result.put(ToolGroupServer.SERVER_TITLE, this.serverTitle);
		}
		result.put(ToolGroupServer.SERVER_VERSION, this.serverVersion);
		result.put(ToolGroupServer.SERVER_TRANSPORT, this.transport);
		result.put(ToolGroupServer.SERVER_REQUEST_DURATION, this.requestTimeout);
		if (this.serverInstructions != null) {
			result.put(ToolGroupServer.SERVER_INSTRUCTIONS, this.serverInstructions);
		}
		return result;
	}
}
