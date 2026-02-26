package org.openmcptools.common.toolgroup.server;

import java.util.List;
import java.util.Map;

import org.openmcptools.common.model.Tool;

public abstract class AbstractToolGroupServer<ToolSpecType, ToolType, ExchangeType, CallToolRequestType, CallToolResultType>
		implements ToolGroupServer<ToolSpecType> {

	protected ToolGroupProvider<ToolSpecType, ExchangeType, CallToolRequestType, CallToolResultType> toolGroupProvider;

	@Override
	public List<Tool> addToolGroups(Map<Object, Class<?>[]> implementerToTypes) {
		return implementerToTypes.entrySet().stream().map(e -> {
			return addToolSpecifications(this.toolGroupProvider.getToolGroupSpecifications(e.getKey(), e.getValue()));
		}).flatMap(List::stream).toList();
	}

	@Override
	public Tool addToolImpl(ToolImpl toolImpl) {
		return addToolSpecification(this.toolGroupProvider.getToolSpecification(toolImpl.getTool(),
				toolImpl.getMethod(), toolImpl.getInstance(), toolImpl.getOutputSchema()));
	}

}
