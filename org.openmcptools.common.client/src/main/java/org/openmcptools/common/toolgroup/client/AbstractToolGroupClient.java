package org.openmcptools.common.toolgroup.client;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import org.openmcptools.common.model.Converter;
import org.openmcptools.common.model.Group;
import org.openmcptools.common.model.Tool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractToolGroupClient<ToolType> implements ToolGroupClient {

	protected static Logger logger = LoggerFactory.getLogger(AbstractToolGroupClient.class);

	protected final List<Tool> tools;
	protected Converter<Tool, ToolType> toolConverter;

	public AbstractToolGroupClient() {
		this.tools = new CopyOnWriteArrayList<Tool>();
	}

	public List<Tool> getTools() {
		return List.copyOf(this.tools);
	}

	protected void addToolsLocal(List<Tool> tools) {
		this.tools.addAll(tools);
	}

	protected List<Tool> removeToolsLocal(List<String> toolNames) {
		List<Tool> removableTools = this.tools.stream().filter(t -> toolNames.contains(t.getFullyQualifiedName()))
				.toList();
		this.tools.removeAll(removableTools);
		return removableTools;
	}

	public Converter<Tool, ToolType> getToolConverter() {
		return toolConverter;
	}

	public List<Group> getGroupRoots() {
		return this.tools.stream().map(t -> {
			return t.getParentGroupRoots();
		}).flatMap(List::stream).distinct().collect(Collectors.toList());
	}

	protected abstract void closeClient();

	@Override
	public void close() throws IOException {
		closeClient();
		this.tools.clear();
	}

}
