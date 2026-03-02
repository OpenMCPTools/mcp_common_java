package org.openmcptools.common.toolgroup.client.impl.spring;

import java.util.List;

import org.openmcptools.common.toolgroup.client.ToolGroupClientListener;

public interface LocalToolGroupClient<ProviderToolType> {

	void updateLocal(List<String> removedTools, List<ProviderToolType> addedTools);

	void setToolGroupClientListeners(List<ToolGroupClientListener> clientListeners);
}
