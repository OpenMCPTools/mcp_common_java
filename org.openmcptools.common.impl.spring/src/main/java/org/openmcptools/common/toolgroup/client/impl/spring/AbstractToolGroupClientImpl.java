package org.openmcptools.common.toolgroup.client.impl.spring;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.openmcptools.common.client.ServerCapabilities;
import org.openmcptools.common.toolgroup.client.AbstractToolGroupClient;
import org.openmcptools.common.toolgroup.client.ToolGroupClientListener;
import org.openmcptools.common.toolgroup.client.ToolGroupClientListener.EventType;

import io.modelcontextprotocol.spec.McpSchema.Tool;

public abstract class AbstractToolGroupClientImpl<ClientType> extends AbstractToolGroupClient<ClientType, Tool> {

	protected SpringLocalToolGroupClient localClient;

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

		protected final List<ToolGroupClientListener> clientListeners;

		public SpringLocalToolGroupClient() {
			this.clientListeners = new CopyOnWriteArrayList<ToolGroupClientListener>();
		}

		@Override
		public void setToolGroupClientListeners(List<ToolGroupClientListener> clientListeners) {
			this.clientListeners.addAll(clientListeners);
		}

		protected void fireToolGroupClientAddEvent(List<org.openmcptools.common.model.Tool> tools) {
			List.copyOf(clientListeners).forEach(tgcl -> {
				if (tools.size() > 0) {
					tgcl.handleClientUpdateEvent(getClient(), EventType.ADD_TOOLS, tools);
				}
			});
		}

		protected void fireToolGroupClientRemoveEvent(List<org.openmcptools.common.model.Tool> tools) {
			List.copyOf(clientListeners).forEach(tgcl -> {
				if (tools.size() > 0) {
					tgcl.handleClientUpdateEvent(getClient(), EventType.REMOVE_TOOLS, tools);
				}
			});
		}

		@Override
		public List<org.openmcptools.common.model.Tool> updateLocal(List<Tool> addedTools, List<String> removedTools) {
			List<org.openmcptools.common.model.Tool> aTools = toolConverter.convertTo(addedTools);
			addToolsLocal(aTools);
			fireToolGroupClientAddEvent(aTools);
			List<org.openmcptools.common.model.Tool> rTools = removeToolsLocal(removedTools);
			fireToolGroupClientRemoveEvent(rTools);
			return rTools;
		}

	}

}
