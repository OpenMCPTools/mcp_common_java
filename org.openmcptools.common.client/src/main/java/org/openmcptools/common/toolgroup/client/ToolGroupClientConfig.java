package org.openmcptools.common.toolgroup.client;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Map;
import java.util.Objects;

public class ToolGroupClientConfig<TransportType> {

	public static final String DEFAULT_CLIENT_NAME = System
			.getProperty(ToolGroupClientConfig.class.getName() + ".defaultClientName", "Default MCP Client Name");
	public static final String DEFAULT_CLIENT_TITLE = System
			.getProperty(ToolGroupClientConfig.class.getName() + ".defaultClientTitle");
	public static final String DEFAULT_CLIENT_VERSION = System
			.getProperty(ToolGroupClientConfig.class.getName() + ".defaultClientVersion", "0.0.1");
	public static final Long DEFAULT_CLIENT_REQUEST_TIMEOUT = Long.parseLong(
			System.getProperty(ToolGroupClientConfig.class.getName() + ".defaultClientRequestTimeout", "10"));

	protected final String clientName;
	protected final String clientTitle;
	protected final String clientVersion;
	protected final TransportType transport;
	protected final Long requestTimeout;

	public ToolGroupClientConfig(String clientName, String clientTitle, String clientVersion, TransportType transport,
			Long requestTimeout) {
		super();
		this.clientName = clientName != null ? clientName : DEFAULT_CLIENT_NAME;
		this.clientTitle = clientTitle;
		this.clientVersion = clientVersion != null ? clientVersion : DEFAULT_CLIENT_VERSION;
		Objects.requireNonNull(transport, "transport must not be null");
		this.transport = transport;
		if (requestTimeout != null) {
			this.requestTimeout = requestTimeout;
		} else {
			this.requestTimeout = DEFAULT_CLIENT_REQUEST_TIMEOUT;
		}
	}

	public ToolGroupClientConfig(String clientName, String clientVersion, TransportType transport,
			Long requestTimeout) {
		this(clientName, null, clientVersion, transport, requestTimeout);
	}

	public ToolGroupClientConfig(TransportType transport, Long requestTimeout) {
		this(DEFAULT_CLIENT_NAME, DEFAULT_CLIENT_TITLE, DEFAULT_CLIENT_VERSION, transport, requestTimeout);
	}

	@SuppressWarnings("unchecked")
	public ToolGroupClientConfig(Map<String, Object> properties) {
		this((String) properties.get(ToolGroupClient.CLIENT_NAME),
				(String) properties.get(ToolGroupClient.CLIENT_TITLE),
				(String) properties.get(ToolGroupClient.CLIENT_VERSION),
				(TransportType) properties.get(ToolGroupClient.CLIENT_TRANSPORT),
				(Long) properties.get(ToolGroupClient.CLIENT_REQUEST_TIMEOUT));
	}

	public Dictionary<String, Object> asProperties() {
		Hashtable<String, Object> result = new Hashtable<String, Object>();
		result.put(ToolGroupClient.CLIENT_NAME, this.clientName);
		if (clientTitle != null) {
			result.put(ToolGroupClient.CLIENT_TITLE, clientTitle);
		}
		result.put(ToolGroupClient.CLIENT_VERSION, this.clientVersion);
		result.put(ToolGroupClient.CLIENT_TRANSPORT, this.transport);
		if (requestTimeout != null) {
			result.put(ToolGroupClient.CLIENT_REQUEST_TIMEOUT, requestTimeout);
		}
		return result;
	}
}
