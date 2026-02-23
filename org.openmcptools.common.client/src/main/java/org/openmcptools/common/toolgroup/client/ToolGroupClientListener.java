package org.openmcptools.common.toolgroup.client;

import java.util.List;

import org.openmcptools.common.model.Tool;

public interface ToolGroupClientListener {

	public enum EventType {
		ADD_TOOLS, UPDATE_TOOLS, REMOVE_TOOLS
	}

	<ClientType> void handleClientUpdateEvent(ClientType client, EventType eventType, List<Tool> tools);
}
