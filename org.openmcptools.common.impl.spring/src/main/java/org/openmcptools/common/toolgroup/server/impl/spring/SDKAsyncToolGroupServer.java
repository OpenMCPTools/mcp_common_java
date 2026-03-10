package org.openmcptools.common.toolgroup.server.impl.spring;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import org.openmcptools.extensions.update.FieldValueUpdate;
import org.openmcptools.extensions.update.PrimitiveUpdateConfig;
import org.openmcptools.extensions.update.PrimitiveUpdateEvent;
import org.openmcptools.extensions.update.PrimitiveUpdateEvent.EventType;
import org.openmcptools.transport.server.MCPServerTransportProvider;
import org.openmcptools.transport.spring.MCPServerSessionImpl;

import io.modelcontextprotocol.json.McpJsonMapper;
import io.modelcontextprotocol.json.schema.JsonSchemaValidator;
import io.modelcontextprotocol.server.McpAsyncServer;
import io.modelcontextprotocol.server.McpNotificationHandler;
import io.modelcontextprotocol.server.McpRequestHandler;
import io.modelcontextprotocol.server.McpServerFeatures.Async;
import io.modelcontextprotocol.server.McpServerFeatures.AsyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema.JSONRPCMessage;
import io.modelcontextprotocol.spec.McpSchema.Tool;
import io.modelcontextprotocol.spec.McpServerTransportProvider;
import io.modelcontextprotocol.spec.McpStreamableServerTransportProvider;
import io.modelcontextprotocol.util.McpUriTemplateManagerFactory;
import reactor.core.publisher.Mono;

public class SDKAsyncToolGroupServer extends McpAsyncServer {

	private final List<PrimitiveUpdateEvent> toolUpdateEvents = new CopyOnWriteArrayList<PrimitiveUpdateEvent>();

	SDKAsyncToolGroupServer(MCPServerTransportProvider<Mono<Void>, Mono<?>, JSONRPCMessage> transportProvider,
			Duration requestTimeout, McpJsonMapper jsonMapper, JsonSchemaValidator jsonSchemaValidator, Async features,
			McpUriTemplateManagerFactory uriTemplateManagerFactory) {
		super((McpServerTransportProvider) transportProvider, jsonMapper, jsonSchemaValidator, features,
				uriTemplateManagerFactory);

		Map<String, McpRequestHandler<?>> requestHandlers = prepareRequestHandlers();
		Map<String, McpNotificationHandler> notificationHandlers = prepareNotificationHandlers(features);

		transportProvider.initServerSessionFactory(transport -> new MCPServerSessionImpl(UUID.randomUUID().toString(),
				requestTimeout, transport, this::asyncInitializeRequestHandler, requestHandlers,
				notificationHandlers));
	}

	public SDKAsyncToolGroupServer(McpServerTransportProvider mcpTransportProvider, McpJsonMapper jsonMapper,
			Async features, Duration requestTimeout, McpUriTemplateManagerFactory uriTemplateManagerFactory,
			JsonSchemaValidator jsonSchemaValidator) {
		super(mcpTransportProvider, jsonMapper, features, requestTimeout, uriTemplateManagerFactory,
				jsonSchemaValidator);
	}

	public SDKAsyncToolGroupServer(McpStreamableServerTransportProvider mcpTransportProvider, McpJsonMapper jsonMapper,
			Async features, Duration requestTimeout, McpUriTemplateManagerFactory uriTemplateManagerFactory,
			JsonSchemaValidator jsonSchemaValidator) {
		super(mcpTransportProvider, jsonMapper, features, requestTimeout, uriTemplateManagerFactory,
				jsonSchemaValidator);
	}

	@Override
	public Mono<Void> notifyToolsListChanged() {
		if (toolUpdateEvents.size() > 0) {
			List<PrimitiveUpdateEvent> toolUpdateEventsCopy = List.copyOf(toolUpdateEvents);
			toolUpdateEvents.clear();
			return this.mcpTransportProvider.notifyClients(PrimitiveUpdateConfig.NOTIFICATION_TOPIC,
					Map.of(PrimitiveUpdateConfig.PRIMITIVE_UPDATE_EVENTS_KEY, toolUpdateEventsCopy));
		} else {
			return super.notifyToolsListChanged();
		}
	}

	private PrimitiveUpdateEvent createAddEvent(Tool tool) {
		PrimitiveUpdateEvent event = new PrimitiveUpdateEvent();
		event.primitiveName = tool.name();
		event.eventType = EventType.PUT;
		List<FieldValueUpdate> fieldValueUpdates = new ArrayList<FieldValueUpdate>();
		// title
		FieldValueUpdate titleUpdate = new FieldValueUpdate();
		titleUpdate.fieldName = "title";
		titleUpdate.fieldValue = tool.title();
		fieldValueUpdates.add(titleUpdate);
		// description
		FieldValueUpdate descriptionUpdate = new FieldValueUpdate();
		descriptionUpdate.fieldName = "description";
		descriptionUpdate.fieldValue = tool.description();
		fieldValueUpdates.add(descriptionUpdate);
		// annotations
		FieldValueUpdate annUpdate = new FieldValueUpdate();
		annUpdate.fieldName = "annotations";
		annUpdate.fieldValue = tool.annotations();
		fieldValueUpdates.add(annUpdate);
		// inputSchema
		FieldValueUpdate isUpdate = new FieldValueUpdate();
		isUpdate.fieldName = "inputSchema";
		isUpdate.fieldValue = tool.inputSchema();
		fieldValueUpdates.add(isUpdate);
		// outputSchema
		FieldValueUpdate osUpdate = new FieldValueUpdate();
		osUpdate.fieldName = "outputSchema";
		osUpdate.fieldValue = tool.outputSchema();
		fieldValueUpdates.add(osUpdate);
		// meta
		FieldValueUpdate metaUpdate = new FieldValueUpdate();
		metaUpdate.fieldName = "meta";
		metaUpdate.fieldValue = tool.meta();
		fieldValueUpdates.add(metaUpdate);
		event.fieldValueUpdates = fieldValueUpdates;

		return event;
	}

	@Override
	public Mono<Void> addTool(AsyncToolSpecification toolSpecification) {
		toolUpdateEvents.add(createAddEvent(toolSpecification.tool()));
		return super.addTool(toolSpecification);
	}

	@Override
	public Mono<Void> removeTool(String toolName) {
		toolUpdateEvents.add(createDeleteEvent(toolName));
		return super.removeTool(toolName);
	}

	private PrimitiveUpdateEvent createDeleteEvent(String toolName) {
		PrimitiveUpdateEvent event = new PrimitiveUpdateEvent();
		event.primitiveName = toolName;
		event.eventType = EventType.DELETE;
		return event;
	}

}
