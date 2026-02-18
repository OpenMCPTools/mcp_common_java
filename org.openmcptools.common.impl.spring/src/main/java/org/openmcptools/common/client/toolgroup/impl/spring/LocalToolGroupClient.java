package org.openmcptools.common.client.toolgroup.impl.spring;

import java.util.List;

import org.openmcptools.common.model.Tool;

public interface LocalToolGroupClient<ProviderToolType> {

	List<Tool> updateLocal(List<ProviderToolType> addedTools, List<String> removedTools);
}
