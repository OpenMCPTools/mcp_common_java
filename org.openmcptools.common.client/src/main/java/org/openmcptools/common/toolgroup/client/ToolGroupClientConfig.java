package org.openmcptools.common.toolgroup.client;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

public class ToolGroupClientConfig<TransportType> {

	public static final String DEFAULT_CLIENT_NAME = System
			.getProperty(ToolGroupClientConfig.class.getName() + ".defaultClientName", "Default MCP Client Name");
	public static final String DEFAULT_CLIENT_TITLE = System
			.getProperty(ToolGroupClientConfig.class.getName() + ".defaultClientTitle");
	public static final String DEFAULT_CLIENT_VERSION = System
			.getProperty(ToolGroupClientConfig.class.getName() + ".defaultClientVersion", "0.0.1");
	public static final Long DEFAULT_CLIENT_REQUEST_TIMEOUT = Long.parseLong(
			System.getProperty(ToolGroupClientConfig.class.getName() + ".defaultClientRequestTimeout", "10"));

	public static final String TOOL_GROUP_CLIENT_LISTENERS = ToolGroupClientConfig.class.getName() + ".clientListeners";

	public static final String CLIENT_PREFIX = ToolGroupClient.class.getName();
	public static final String CLIENT_NAME = CLIENT_PREFIX + ".clientName";
	public static final String CLIENT_TITLE = CLIENT_PREFIX + ".clientTitle";
	public static final String CLIENT_VERSION = CLIENT_PREFIX + ".clientVersion";
	public static final String CLIENT_TRANSPORT = CLIENT_PREFIX + ".clientTransport";
	public static final String CLIENT_LISTENERS = CLIENT_PREFIX + ".clientListeners";
	public static final String CLIENT_REQUEST_TIMEOUT = CLIENT_PREFIX + ".clientRequestTimeout";

	protected final String clientName;
	protected final String clientTitle;
	protected final String clientVersion;
	protected final TransportType transport;
	protected final Long requestTimeout;
	protected final List<ToolGroupClientListener> clientListeners = new CopyOnWriteArrayList<ToolGroupClientListener>();

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
		this((String) properties.get(CLIENT_NAME), (String) properties.get(CLIENT_TITLE),
				(String) properties.get(CLIENT_VERSION), (TransportType) properties.get(CLIENT_TRANSPORT),
				(Long) properties.get(CLIENT_REQUEST_TIMEOUT));
		List<ToolGroupClientListener> clientListeners = (List<ToolGroupClientListener>) properties
				.get(ToolGroupClientConfig.TOOL_GROUP_CLIENT_LISTENERS);
		if (clientListeners != null) {
			this.clientListeners.addAll(clientListeners);
		}
	}

	public ToolGroupClientConfig<TransportType> addToolGroupClientListener(ToolGroupClientListener listener) {
		this.clientListeners.add(listener);
		return this;
	}

	public Dictionary<String, Object> asProperties() {
		Hashtable<String, Object> result = new Hashtable<String, Object>();
		result.put(CLIENT_NAME, this.clientName);
		if (clientTitle != null) {
			result.put(CLIENT_TITLE, clientTitle);
		}
		result.put(CLIENT_VERSION, this.clientVersion);
		result.put(CLIENT_TRANSPORT, this.transport);
		if (requestTimeout != null) {
			result.put(CLIENT_REQUEST_TIMEOUT, requestTimeout);
		}
		if (this.clientListeners.size() > 0) {
			result.put(ToolGroupClientConfig.TOOL_GROUP_CLIENT_LISTENERS, this.clientListeners);
		}
		return result;
	}
}
