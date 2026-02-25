package org.openmcptools.common.impl.spring;

import java.io.IOException;
import java.util.ArrayList;
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
		Map<String, Object> outputMeta = tool.getMeta();
		List<org.openmcptools.extensions.groups.protocol.Group> groups1 = this.groupConverter.convertFrom(tool.getParentGroups());
		if (groups1.size() > 0) {
			outputMeta = outputMeta == null ? new HashMap<String, Object>() : outputMeta;
			outputMeta.put(org.openmcptools.extensions.groups.protocol.GroupsExtensionConfig.EXTENSION_ID, groups1);
		}
		builder.meta(outputMeta);
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

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Tool convertTo(io.modelcontextprotocol.spec.McpSchema.Tool tool) {
		Map<String, Object> toolMeta = tool.meta() == null ? new HashMap<String, Object>() : new HashMap<String, Object>(tool.meta());
		// Get the untyped list for the group extension ID
		List gs = (List) toolMeta
				.remove(org.openmcptools.extensions.groups.protocol.GroupsExtensionConfig.EXTENSION_ID);
		List<Group> parentGroups = new ArrayList<Group>();
		if (gs != null) {
			// Iterate through items.  They will either be of type Group (by converter)
			// or of type Map (by deserialization)
			for (Object g : gs) {
				if (g instanceof Map) {
					parentGroups.add(this.groupConverter.convertTo(org.openmcptools.extensions.groups.protocol.Group
							.convertMapToGroup((Map<String, Object>) g)));
				} else if (g instanceof org.openmcptools.extensions.groups.protocol.Group) {
					parentGroups
							.add(this.groupConverter.convertTo((org.openmcptools.extensions.groups.protocol.Group) g));
				}
			}
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
		if (toolMeta != null && toolMeta.size() > 0) {
			tn.setMeta(toolMeta);
		}
		return tn;
	}

}
