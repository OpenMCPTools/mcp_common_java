package org.openmcptools.common.toolgroup.client.impl.spring;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.openmcptools.common.toolgroup.client.ToolGroupClientListener;
import org.openmcptools.extensions.update.FieldValueUpdate;
import org.openmcptools.extensions.update.PrimitiveUpdateConfig;
import org.openmcptools.extensions.update.PrimitiveUpdateEvent;
import org.openmcptools.extensions.update.PrimitiveUpdateEvent.EventType;

import io.modelcontextprotocol.client.McpAsyncClient;
import io.modelcontextprotocol.client.McpClientFeatures.Async;
import io.modelcontextprotocol.json.schema.JsonSchemaValidator;
import io.modelcontextprotocol.spec.McpClientSession;
import io.modelcontextprotocol.spec.McpClientSession.NotificationHandler;
import io.modelcontextprotocol.spec.McpClientSession.RequestHandler;
import io.modelcontextprotocol.spec.McpClientTransport;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.Tool;
import io.modelcontextprotocol.spec.McpSchema.ToolAnnotations;
import reactor.core.publisher.Mono;
import reactor.util.context.ContextView;

public class SDKAsyncToolGroupClient extends McpAsyncClient {

	private List<ToolGroupClientListener> listeners = new CopyOnWriteArrayList<ToolGroupClientListener>();
	private final LocalToolGroupClient<Tool> localToolGroupClient;

	public SDKAsyncToolGroupClient(McpClientTransport transport, Duration requestTimeout,
			Duration initializationTimeout, JsonSchemaValidator jsonSchemaValidator, Async features,
			List<ToolGroupClientListener> listeners, LocalToolGroupClient<Tool> localToolGroupClient) {
		super(transport, requestTimeout, initializationTimeout, jsonSchemaValidator, features);
		if (listeners != null) {
			this.listeners.addAll(listeners);
		}
		this.localToolGroupClient = localToolGroupClient;
	}

	@Override
	protected McpClientSession buildClientSession(Duration requestTimeout, McpClientTransport transport,
			Map<String, RequestHandler<?>> requestHandlers, Map<String, NotificationHandler> notificationHandlers,
			ContextView ctx) {
		notificationHandlers.put("notification/tools/updated", new ToolGroupNotificationHandler());
		return super.buildClientSession(requestTimeout, transport, requestHandlers, notificationHandlers, ctx);
	}

	@SuppressWarnings("unchecked")
	Tool createTool(PrimitiveUpdateEvent event) {
		Tool.Builder toolBuilder = Tool.builder();
		toolBuilder.name(event.primitiveName);
		List<FieldValueUpdate> fieldValueUpdates = event.fieldValueUpdates;
		if (fieldValueUpdates != null) {
			fieldValueUpdates.forEach(fvu -> {
				if (fvu.fieldName == "title") {
					toolBuilder.title((String) fvu.fieldValue);
				} else if (fvu.fieldName == "description") {
					toolBuilder.description((String) fvu.fieldValue);
				} else if (fvu.fieldName == "annotations") {
					toolBuilder.annotations((ToolAnnotations) fvu.fieldValue);
				} else if (fvu.fieldName == "inputSchema") {
					toolBuilder.inputSchema((McpSchema.JsonSchema) fvu.fieldValue);
				} else if (fvu.fieldName == "outputSchema") {
					toolBuilder.outputSchema((Map<String, Object>) fvu.fieldValue);
				} else if (fvu.fieldName == "meta") {
					toolBuilder.meta((Map<String, Object>) fvu.fieldValue);
				}
			});
		}
		return toolBuilder.build();
	}

	class ToolGroupNotificationHandler implements NotificationHandler {

		@Override
		@SuppressWarnings("unchecked")
		public Mono<Void> handle(Object params) {
			if (params != null) {
				List<Tool> addedMcpTools = new ArrayList<Tool>();
				List<String> removedTools = new ArrayList<String>();
				Map<String, Object> notificationMap = (Map<String, Object>) params;
				List<Map<String, Object>> eventMaps = (List<Map<String, Object>>) notificationMap
						.get(PrimitiveUpdateConfig.PRIMITIVE_UPDATE_EVENTS_KEY);
				try {
					if (eventMaps != null) {
						eventMaps.forEach(m -> {
							PrimitiveUpdateEvent e = PrimitiveUpdateEvent.fromMap(m);
							if (e.eventType == EventType.PUT) {
								addedMcpTools.add(createTool(e));
							} else if (e.eventType == EventType.DELETE) {
								removedTools.add(e.primitiveName);
							}
						});
						localToolGroupClient.updateLocal(addedMcpTools, removedTools);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return Mono.empty();
		}
	}

}