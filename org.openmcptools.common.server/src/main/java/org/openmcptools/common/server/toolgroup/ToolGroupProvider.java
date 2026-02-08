package org.openmcptools.common.server.toolgroup;

import java.lang.reflect.Method;
import java.util.List;

import org.openmcptools.common.model.Tool;

public interface ToolGroupProvider<SpecificationType, ExchangeType, CallRequestType, CallResultType> {

	List<ToolSpecification<SpecificationType>> getToolGroupSpecifications(Object toolGroupObject,
			Class<?>... classes);

	List<ToolSpecification<SpecificationType>> getToolGroupSpecifications(List<Object> toolGroupObjects,
			Class<?>... classes);

	public ToolSpecification<SpecificationType> getToolSpecification(Tool tool, Method toolMethod, Object instance, boolean outputSchema);
}
