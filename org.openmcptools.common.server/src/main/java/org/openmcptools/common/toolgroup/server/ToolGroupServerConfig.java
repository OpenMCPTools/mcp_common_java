package org.openmcptools.common.toolgroup.server;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Map;
import java.util.Objects;

public class ToolGroupServerConfig<TP> {

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
	protected final TP transportProvider;
	protected final Long requestTimeout;
	protected final String serverInstructions;

	public ToolGroupServerConfig(String serverName, String serverTitle, String serverVersion, TP transportProvider,
			Long requestTimeout, String serverInstructions) {
		super();
		Objects.requireNonNull(serverName, "serverName must not be null");
		this.serverName = serverName;
		this.serverTitle = serverTitle;
		Objects.requireNonNull(serverVersion, "serverVersion must not be null");
		this.serverVersion = serverVersion;
		Objects.requireNonNull(transportProvider, "transportProvider must not be null");
		this.transportProvider = transportProvider;
		Objects.requireNonNull(requestTimeout, "requestTimeout must not be null");
		this.requestTimeout = requestTimeout;
		this.serverInstructions = serverInstructions;
	}

	public ToolGroupServerConfig(String serverName, String serverTitle, String serverVersion, TP transportProvider) {
		this(serverName, serverTitle, serverVersion, transportProvider, DEFAULT_SERVER_REQUEST_TIMEOUT, null);
	}

	public ToolGroupServerConfig(String serverName, String serverTitle, TP transportProvider) {
		this(serverName, null, serverTitle, transportProvider);
	}

	public ToolGroupServerConfig(TP transportProvider) {
		this(DEFAULT_SERVER_NAME, DEFAULT_SERVER_VERSION, transportProvider);
	}

	@SuppressWarnings("unchecked")
	public ToolGroupServerConfig(Map<String, Object> properties) {
		this((String) properties.get(ToolGroupServer.SERVER_NAME),
				(String) properties.get(ToolGroupServer.SERVER_TITLE),
				(String) properties.get(ToolGroupServer.SERVER_VERSION),
				(TP) properties.get(ToolGroupServer.SERVER_TRANSPORT_PROVIDER),
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
		result.put(ToolGroupServer.SERVER_TRANSPORT_PROVIDER, this.transportProvider);
		result.put(ToolGroupServer.SERVER_REQUEST_DURATION, this.requestTimeout);
		if (this.serverInstructions != null) {
			result.put(ToolGroupServer.SERVER_INSTRUCTIONS, this.serverInstructions);
		}
		return result;
	}
}
