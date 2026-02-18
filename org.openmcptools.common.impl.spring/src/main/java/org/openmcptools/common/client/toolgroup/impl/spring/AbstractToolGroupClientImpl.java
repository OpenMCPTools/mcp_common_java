package org.openmcptools.common.client.toolgroup.impl.spring;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.openmcptools.common.client.ServerCapabilities;
import org.openmcptools.common.toolgroup.client.AbstractToolGroupClient;
import org.openmcptools.common.toolgroup.client.ToolGroupClientListener;

import io.modelcontextprotocol.spec.McpSchema.Tool;

public abstract class AbstractToolGroupClientImpl<ClientType> extends AbstractToolGroupClient<ClientType, Tool> {

	protected List<ToolGroupClientListener> clientListeners = new CopyOnWriteArrayList<ToolGroupClientListener>();

	protected class SpringServerCapabilities extends ServerCapabilities {

		public SpringServerCapabilities(io.modelcontextprotocol.spec.McpSchema.ServerCapabilities sc) {
			super(sc.completions() != null ? new CompletionCapabilities() : null, sc.experimental(),
					sc.logging() != null ? new LoggingCapabilities() : null,
					sc.prompts() != null ? new PromptCapabilities(sc.prompts().listChanged()) : null,
					sc.resources() != null
							? new ResourceCapabilities(sc.resources().subscribe(), sc.resources().listChanged())
							: null,
					sc.tools() != null ? new ToolCapabilities(sc.tools().listChanged()) : null);
		}
	}

	protected class SpringLocalToolGroupClient implements LocalToolGroupClient<Tool> {

		@Override
		public List<org.openmcptools.common.model.Tool> updateLocal(List<Tool> addedTools, List<String> removedTools) {
			toolConverter.convertToTools(addedTools);
			addToolsLocal(toolConverter.convertToTools(addedTools));
			return removeToolsLocal(removedTools);
		}

	}

}
