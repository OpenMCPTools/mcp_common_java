package org.openmcptools.common.server.toolgroup.impl.spring;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.openmcptools.extensions.update.FieldValueUpdate;
import org.openmcptools.extensions.update.PrimitiveUpdateEvent;
import org.openmcptools.extensions.update.PrimitiveUpdateEvent.EventType;

import io.modelcontextprotocol.json.McpJsonMapper;
import io.modelcontextprotocol.json.schema.JsonSchemaValidator;
import io.modelcontextprotocol.server.McpAsyncServer;
import io.modelcontextprotocol.server.McpServerFeatures.Async;
import io.modelcontextprotocol.server.McpServerFeatures.AsyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema.Tool;
import io.modelcontextprotocol.spec.McpServerTransportProvider;
import io.modelcontextprotocol.spec.McpStreamableServerTransportProvider;
import io.modelcontextprotocol.util.McpUriTemplateManagerFactory;
import reactor.core.publisher.Mono;

public class McpAsyncToolGroupServer extends McpAsyncServer {

	private final List<PrimitiveUpdateEvent> events;
	private boolean toolUpdateInProgress = false;

	public McpAsyncToolGroupServer(McpServerTransportProvider mcpTransportProvider, McpJsonMapper jsonMapper,
			Async features, Duration requestTimeout, McpUriTemplateManagerFactory uriTemplateManagerFactory,
			JsonSchemaValidator jsonSchemaValidator) {
		super(mcpTransportProvider, jsonMapper, features, requestTimeout, uriTemplateManagerFactory,
				jsonSchemaValidator);
		this.events = new CopyOnWriteArrayList<PrimitiveUpdateEvent>();
	}

	public McpAsyncToolGroupServer(McpStreamableServerTransportProvider mcpTransportProvider, McpJsonMapper jsonMapper,
			Async features, Duration requestTimeout, McpUriTemplateManagerFactory uriTemplateManagerFactory,
			JsonSchemaValidator jsonSchemaValidator) {
		super(mcpTransportProvider, jsonMapper, features, requestTimeout, uriTemplateManagerFactory,
				jsonSchemaValidator);
		this.events = new CopyOnWriteArrayList<PrimitiveUpdateEvent>();
	}

	@Override
	public Mono<Void> notifyToolsListChanged() {
		if (toolUpdateInProgress) {
			return Mono.empty();
		} else {
			return super.notifyToolsListChanged();
		}
	}

	public void startToolsUpdate() {
		this.toolUpdateInProgress = true;
	}

	public void endToolsUpdate() {
		this.mcpTransportProvider.notifyClients("notification/tools/updated",
				Map.of("notification/tools/updated", events));
		events.clear();
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
		events.add(createAddEvent(toolSpecification.tool()));
		return super.addTool(toolSpecification);
	}

	@Override
	public Mono<Void> removeTool(String toolName) {
		events.add(createDeleteEvent(toolName));
		return super.removeTool(toolName);
	}

	private PrimitiveUpdateEvent createDeleteEvent(String toolName) {
		PrimitiveUpdateEvent event = new PrimitiveUpdateEvent();
		event.primitiveName = toolName;
		event.eventType = EventType.DELETE;
		return event;
	}

}
