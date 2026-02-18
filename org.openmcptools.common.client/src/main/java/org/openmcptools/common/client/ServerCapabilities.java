package org.openmcptools.common.client;

import java.util.Map;

public class ServerCapabilities {

	public static class CompletionCapabilities {
		public CompletionCapabilities() {
		}
	}

	public static class LoggingCapabilities {
		public LoggingCapabilities() {
		}
	}

	public static class PromptCapabilities {
		private final boolean listChanged;

		public PromptCapabilities(boolean listChanged) {
			super();
			this.listChanged = listChanged;
		}

		public boolean getListChanged() {
			return this.listChanged;
		}
	}

	public static class ResourceCapabilities {
		private final boolean subscribe;
		private final boolean listChanged;

		public ResourceCapabilities(boolean subscribe, boolean listChanged) {
			super();
			this.subscribe = subscribe;
			this.listChanged = listChanged;
		}

		public boolean isSubscribe() {
			return subscribe;
		}

		public boolean isListChanged() {
			return listChanged;
		}

	}

	public static class ToolCapabilities {
		private final boolean listChanged;

		public ToolCapabilities(boolean listChanged) {
			super();
			this.listChanged = listChanged;
		}

		public boolean isListChanged() {
			return listChanged;
		}

	}

	private final CompletionCapabilities completions;
	private final Map<String, Object> experimental;
	private final LoggingCapabilities logging;
	private final PromptCapabilities prompts;
	private final ResourceCapabilities resources;
	private final ToolCapabilities tools;

	public ServerCapabilities(CompletionCapabilities completions, Map<String, Object> experimental,
			LoggingCapabilities logging, PromptCapabilities prompts, ResourceCapabilities resources,
			ToolCapabilities tools) {
		super();
		this.completions = completions;
		this.experimental = experimental;
		this.logging = logging;
		this.prompts = prompts;
		this.resources = resources;
		this.tools = tools;
	}

	public CompletionCapabilities getCompletions() {
		return completions;
	}

	public Map<String, Object> getExperimental() {
		return experimental;
	}

	public LoggingCapabilities getLogging() {
		return logging;
	}

	public PromptCapabilities getPrompts() {
		return prompts;
	}

	public ResourceCapabilities getResources() {
		return resources;
	}

	public ToolCapabilities getTools() {
		return tools;
	}

}