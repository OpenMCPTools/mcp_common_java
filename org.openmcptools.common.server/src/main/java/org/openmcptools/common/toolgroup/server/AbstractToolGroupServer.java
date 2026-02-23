package org.openmcptools.common.toolgroup.server;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import org.openmcptools.common.model.Tool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractToolGroupServer<ServerType, ToolSpecType, ToolType, ExchangeType, CallToolRequestType, CallToolResultType>
		implements ToolGroupServer<ServerType> {

	protected static Logger logger = LoggerFactory.getLogger(AbstractToolGroupServer.class);

	protected final Map<Tool, BiFunction<ExchangeType, CallToolRequestType, CallToolResultType>> toolToBiFunctionMap;
	protected final CopyOnWriteArrayList<ToolSpecType> toolSpecs;

	protected ToolGroupProvider<ToolSpecType, ExchangeType, CallToolRequestType, CallToolResultType> toolGroupProvider;
	protected ServerType server;

	protected AbstractToolGroupServer() {
		this.toolToBiFunctionMap = new ConcurrentHashMap<Tool, BiFunction<ExchangeType, CallToolRequestType, CallToolResultType>>();
		this.toolSpecs = new CopyOnWriteArrayList<ToolSpecType>();
	}

	protected abstract void closeServer();

	@Override
	public void close() throws IOException {
		closeServer();
		this.toolToBiFunctionMap.clear();
		this.toolSpecs.clear();
	}

	abstract protected void addTool(ServerType server, ToolSpecType toolSpec);

	abstract protected void removeTool(ServerType server, String toolName);

	protected void addTools(List<ToolSpecType> toolSpecs) {
		toolSpecs.forEach(s -> addTool(s));
	}

	protected void removeToolsByName(List<String> toolNames) {
		toolNames.forEach(tn -> removeTool(tn));
	}

	protected void addTool(ToolSpecType toolSpec) {
		Objects.requireNonNull(toolSpec, "toolSpec must not be null");
		ServerType s = getServer();
		try {
			addTool(s, toolSpec);
			this.toolSpecs.add(toolSpec);
			if (logger.isDebugEnabled()) {
				logger.debug("added tool specification={} to sync server={}", toolSpec, s);
			}
		} catch (Exception e) {
			handleAddError(toolSpec, e);
			throw e;
		}
	}

	protected void handleAddError(ToolSpecType toolSpec, Exception e) {
		if (logger.isErrorEnabled()) {
			logger.error("Could not add tool specification=" + toolSpec, e);
		}
	}

	protected void handleRemoveError(String toolSpecName, Exception e) {
		if (logger.isErrorEnabled()) {
			logger.error("Could not remove tool specification name=" + toolSpecName, e);
		}
	}

	protected void removeTool(String toolName) {
		Objects.requireNonNull(toolName, "toolName must not be null");
		try {
			removeTool(this.server, toolName);
		} catch (Exception e) {
			handleRemoveError(toolName, e);
			throw e;
		}
	}

	public ServerType getServer() {
		return server;
	}

	protected List<ToolSpecType> getToolSpecs() {
		return toolSpecs;
	}

	protected abstract ToolSpecification<ToolSpecType> getToolSpecification(Tool tool,
			BiFunction<ExchangeType, CallToolRequestType, CallToolResultType> callHandler);

	@Override
	public void removeTools(List<String> toolNames) {
		removeToolsByName(toolNames);
	}

	protected abstract List<Tool> addSpecifications(List<ToolSpecification<ToolSpecType>> specs);

	@Override
	public List<Tool> addToolGroups(Map<Object, Class<?>[]> implementerToTypes) {

		List<ToolSpecification<ToolSpecType>> specs = implementerToTypes.entrySet().stream().map((e) -> {
			return this.toolGroupProvider.getToolGroupSpecifications(e.getKey(), e.getValue());
		}).flatMap(List::stream).collect(Collectors.toList());

		return addSpecifications(specs);
	}

	@Override
	public List<Tool> addToolInvokers(List<ToolImplementation> toolInvokers) {
		List<ToolSpecification<ToolSpecType>> specs = toolInvokers.stream().map(ti -> {
			return this.toolGroupProvider.getToolSpecification(ti.getTool(), ti.getMethod(), ti.getInstance(),
					ti.getOutputSchema());
		}).collect(Collectors.toList());

		return addSpecifications(specs);
	}

}
