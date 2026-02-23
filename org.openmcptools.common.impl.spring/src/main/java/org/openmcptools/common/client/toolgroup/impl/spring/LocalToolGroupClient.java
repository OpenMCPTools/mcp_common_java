package org.openmcptools.common.client.toolgroup.impl.spring;

import java.util.List;

import org.openmcptools.common.model.Tool;
import org.openmcptools.common.toolgroup.client.ToolGroupClientListener;

public interface LocalToolGroupClient<ProviderToolType> {

	List<Tool> updateLocal(List<ProviderToolType> addedTools, List<String> removedTools);

	void setToolGroupClientListeners(List<ToolGroupClientListener> clientListeners);
}
