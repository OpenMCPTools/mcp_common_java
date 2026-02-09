package org.openmcptools.common.impl.spring;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.openmcptools.common.model.Group;
import org.openmcptools.common.model.GroupConverter;
import org.openmcptools.common.model.Tool;
import org.openmcptools.common.model.ToolAnnotations;
import org.openmcptools.common.model.ToolAnnotationsConverter;
import org.openmcptools.common.model.ToolConverter;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import io.modelcontextprotocol.json.McpJsonDefaults;
import io.modelcontextprotocol.json.McpJsonMapper;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.JsonSchema;

@Component(immediate = true, service = ToolConverter.class)
public class ToolConverterImpl implements ToolConverter<io.modelcontextprotocol.spec.McpSchema.Tool> {

	private final McpJsonMapper jsonMapper;

	private final ToolAnnotationsConverter<io.modelcontextprotocol.spec.McpSchema.ToolAnnotations> toolAnnotationsConverter;
	private GroupConverter<org.openmcptools.extensions.groups.protocol.Group> groupConverter;

	@Activate
	public ToolConverterImpl(
			@Reference ToolAnnotationsConverter<io.modelcontextprotocol.spec.McpSchema.ToolAnnotations> toolAnnotationsConverter,
			@Reference GroupConverter<org.openmcptools.extensions.groups.protocol.Group> groupConverter) {
		this.jsonMapper = McpJsonDefaults.getDefaultMcpJsonMapper();
		this.toolAnnotationsConverter = toolAnnotationsConverter;
		this.groupConverter = groupConverter;
	}

	@Override
	public io.modelcontextprotocol.spec.McpSchema.Tool convertFromTool(Tool tool) {
		McpSchema.Tool.Builder builder = new McpSchema.Tool.Builder();
		builder.name(tool.getName());
		builder.title(tool.getTitle());
		builder.description(tool.getDescription());
		String inputSchema = tool.getInputSchema();
		if (inputSchema != null) {
			builder.inputSchema(jsonMapper, inputSchema);
		}
		String outputSchema = tool.getOutputSchema();
		if (outputSchema != null) {
			builder.outputSchema(jsonMapper, outputSchema);
		}
		List<Group> parentGroups = tool.getParentGroups();
		Map<String, Object> meta = tool.getMeta();
		if (parentGroups != null) {
			List<org.openmcptools.extensions.groups.protocol.Group> groups = parentGroups.stream().map(pgn -> {
				return this.groupConverter.convertFromGroup(pgn);
			}).collect(Collectors.toList());
			if (meta == null) {
				meta = new HashMap<String, Object>();
			}
			// Now add to meta
			meta.put(getMetaGroupsName(), groups);
		}
		builder.meta(meta);
		ToolAnnotations tan = tool.getToolAnnotations();
		builder.annotations((tan != null) ? this.toolAnnotationsConverter.convertFromToolAnnotations(tan) : null);
		return builder.build();
	}

	protected String generateInputSchema(JsonSchema inputSchema) {
		if (inputSchema == null)
			return null;
		try {
			return jsonMapper.writeValueAsString(inputSchema);
		} catch (IOException e) {
			throw new IllegalArgumentException("Invalid input schema: " + inputSchema, e);
		}
	}

	protected String generateOutputSchema(Map<String, Object> outputSchema) {
		if (outputSchema == null)
			return null;
		try {
			return jsonMapper.writeValueAsString(outputSchema);
		} catch (IOException e) {
			throw new IllegalArgumentException("Invalid output schema: " + outputSchema, e);
		}
	}

	String getMetaGroupsName() {
		return org.openmcptools.common.impl.spring.ToolConverterImpl.class.getName() + ".groups";
	}

	@SuppressWarnings("unchecked")
	protected org.openmcptools.extensions.groups.protocol.Group convertGroup(Map<String, Object> groupMap) {
		org.openmcptools.extensions.groups.protocol.Group result = new org.openmcptools.extensions.groups.protocol.Group((String) groupMap.get("name"));
		result.title = (String) groupMap.get("title");
		result.description = (String) groupMap.get("description");
		Map<String, Object> parentMap = (Map<String, Object>) groupMap.get("parent");
		if (parentMap != null) {
			result.parent = convertGroup(parentMap);
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	protected List<org.openmcptools.extensions.groups.protocol.Group> convertGroupsFromTool(Map<String, Object> meta) {
		if (meta != null) {
			List<Map<String, Object>> groupMaps = (List<Map<String, Object>>) meta
					.remove(getMetaGroupsName());
			if (groupMaps != null) {
				return groupMaps.stream().map(gm -> {
					return convertGroup(gm);
				}).collect(Collectors.toList());
			}
		}
		return null;
	}

	@Override
	public Tool convertToTool(io.modelcontextprotocol.spec.McpSchema.Tool tool) {
		Tool tn = new Tool(tool.name());
		tn.setTitle(tool.title());
		tn.setDescription(tool.description());
		tn.setInputSchema(generateInputSchema(tool.inputSchema()));
		tn.setOutputSchema(generateOutputSchema(tool.outputSchema()));
		McpSchema.ToolAnnotations a = tool.annotations();
		if (a != null) {
			tn.setToolAnnotations(toolAnnotationsConverter.convertToToolAnnotations(a));
		}
		List<org.openmcptools.extensions.groups.protocol.Group> groups = convertGroupsFromTool(tool.meta());
		if (groups != null) {
			groups.forEach(pg -> {
				groupConverter.convertToGroup(pg).addChildTool(tn);
			});
		}
		tn.setMeta(tool.meta());
		return tn;
	}

}
