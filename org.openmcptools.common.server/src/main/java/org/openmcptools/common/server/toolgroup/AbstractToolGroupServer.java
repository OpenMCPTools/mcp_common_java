package org.openmcptools.common.server.toolgroup;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.BiFunction;

import org.openmcptools.common.model.Tool;
import org.openmcptools.common.model.ToolConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractToolGroupServer<ServerType, ToolSpecType, ToolType, ExchangeType, CallToolRequestType, CallToolResultType>
		implements ToolGroupServer {

	private static Logger logger = LoggerFactory.getLogger(AbstractToolGroupServer.class);

	protected final Map<Tool, BiFunction<ExchangeType, CallToolRequestType, CallToolResultType>> toolToBiFunctionMap;
	protected final CopyOnWriteArrayList<ToolSpecType> toolSpecs;

	protected ToolConverter<ToolType> toolConverter;
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

	protected ServerType getServer() {
		return server;
	}

	protected List<ToolSpecType> getToolSpecs() {
		return toolSpecs;
	}

	protected abstract ToolSpecification<ToolSpecType> getToolSpecification(Tool tool,
			BiFunction<ExchangeType, CallToolRequestType, CallToolResultType> callHandler);

	@Override
	public List<Tool> addToolGroup(Object instance, Class<?>... classes) {
		List<ToolSpecification<ToolSpecType>> specs = this.toolGroupProvider.getToolGroupSpecifications(instance,
				classes);
		specs.forEach(s -> {
			addTool(this.server, s.getSpecification());
		});
		return specs.stream().map(sp -> {
			return sp.getTool();
		}).toList();
	}

	@Override
	public void addTool(Tool tool, Method toolMethod, Object instance) {
		this.toolGroupProvider.getToolSpecification(tool, toolMethod, instance, tool.getOutputSchema() != null);
	}

}
