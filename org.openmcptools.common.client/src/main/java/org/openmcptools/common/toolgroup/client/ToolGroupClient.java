package org.openmcptools.common.toolgroup.client;

import java.io.Closeable;
import java.util.List;

import org.openmcptools.common.client.InitializeResult;
import org.openmcptools.common.model.Group;
import org.openmcptools.common.model.Tool;

public interface ToolGroupClient<ClientType> extends Closeable {

	static final String CLIENT_PREFIX = ToolGroupClient.class.getName();
	public static final String CLIENT_NAME = CLIENT_PREFIX + ".clientName";
	public static final String CLIENT_DEFAULT_NAME = "Default ToolGroupClient Name";
	public static final String CLIENT_VERSION = CLIENT_PREFIX + ".clientVersion";
	public static final String CLIENT_DEFAULT_VERSION = "0.0.1";
	public static final String CLIENT_TRANSPORT = CLIENT_PREFIX + ".clientTransport";
	public static final String CLIENT_CAPABILITIES = CLIENT_PREFIX + ".clientCapabilities";
	public static final String CLIENT_JSONSCHEMAVALIDATOR = CLIENT_PREFIX + ".jsonSchemaValidator";
	public static final String CLIENT_CONTEXTPROVIDER = CLIENT_PREFIX + ".clientContextProvider";
	public static final String CLIENT_LISTENERS = CLIENT_PREFIX + ".clientListeners";

	InitializeResult initialize();

	List<Tool> getTools();

	List<Group> getGroupRoots();

	ClientType getClient();
}
