package org.openmcptools.common.impl.spring;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openmcptools.common.model.Group;
import org.openmcptools.common.model.Tool;
import org.openmcptools.common.model.ToolAnnotations;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import io.modelcontextprotocol.json.McpJsonDefaults;
import io.modelcontextprotocol.json.McpJsonMapper;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.JsonSchema;

@Component(immediate = true, service = ToolConverter.class)
public class ToolConverterImpl implements ToolConverter {

	private final McpJsonMapper jsonMapper;

	private final ToolAnnotationsConverter toolAnnotationsConverter;
	private GroupConverter groupConverter;

	@SuppressWarnings("static-access")
	@Activate
	public ToolConverterImpl(@Reference McpJsonDefaults jsonDefaults,
			@Reference ToolAnnotationsConverter toolAnnotationsConverter, @Reference GroupConverter groupConverter) {
		this.jsonMapper = jsonDefaults.getMapper();
		this.toolAnnotationsConverter = toolAnnotationsConverter;
		this.groupConverter = groupConverter;
	}

	protected Map<String, Object> convertCommonGroupsToMeta(List<Group> commonGroups) {
		List<org.openmcptools.extensions.groups.protocol.Group> groups = this.groupConverter.convertFrom(commonGroups);
		Map<String, Object> result = new HashMap<String, Object>();
		if (groups.size() > 0) {
			result.put(org.openmcptools.extensions.groups.protocol.GroupsExtensionConfig.EXTENSION_ID, groups);
		}
		return result;
	}

	@Override
	public io.modelcontextprotocol.spec.McpSchema.Tool convertFrom(Tool tool) {
		McpSchema.Tool.Builder builder = new McpSchema.Tool.Builder();
		builder.name(tool.getFullyQualifiedName());
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
		Map<String, Object> groups = convertCommonGroupsToMeta(tool.getParentGroups());
		Map<String, Object> toolMeta = tool.getMeta();
		if (groups.size() > 0) {
			toolMeta = toolMeta == null ? new HashMap<String, Object>(groups) : Map.copyOf(toolMeta);
			toolMeta.putAll(groups);
		}
		builder.meta(toolMeta);
		ToolAnnotations tan = tool.getToolAnnotations();
		builder.annotations((tan != null) ? this.toolAnnotationsConverter.convertFrom(tan) : null);
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

	protected List<Group> convertMetaToCommonGroups(Map<String, Object> toolMeta) {
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> groupMaps = (List<Map<String, Object>>) toolMeta
				.remove(org.openmcptools.extensions.groups.protocol.GroupsExtensionConfig.EXTENSION_ID);
		if (groupMaps != null) {
			return groupMaps.stream().map(m -> {
				return this.groupConverter
						.convertTo(org.openmcptools.extensions.groups.protocol.Group.convertMapToGroup(m));
			}).toList();
		}
		return null;
	}

	@Override
	public Tool convertTo(io.modelcontextprotocol.spec.McpSchema.Tool tool) {
		Map<String, Object> toolMeta = tool.meta();
		List<Group> parentGroups = null;
		if (toolMeta != null) {
			toolMeta = new HashMap<String, Object>(toolMeta);
			parentGroups = convertMetaToCommonGroups(toolMeta);
		}
		String toolName = tool.name();
		if (parentGroups != null && parentGroups.size() > 0) {
			// Get first and use as primary
			Group primaryParentGroup = parentGroups.get(0);
			if (toolName.startsWith(primaryParentGroup.getFullyQualifiedName())) {
				toolName = toolName.substring(toolName.lastIndexOf(".") + 1);
			}
		}
		Tool tn = new Tool(toolName);
		tn.setTitle(tool.title());
		tn.setDescription(tool.description());
		tn.setInputSchema(generateInputSchema(tool.inputSchema()));
		tn.setOutputSchema(generateOutputSchema(tool.outputSchema()));
		McpSchema.ToolAnnotations a = tool.annotations();
		if (a != null) {
			tn.setToolAnnotations(toolAnnotationsConverter.convertTo(a));
		}
		// add as parents
		if (parentGroups != null) {
			parentGroups.forEach(pg -> {
				pg.addChildTool(tn);
			});
		}
		tn.setMeta(toolMeta);
		return tn;
	}

}
