package org.openmcptools.common.server.toolgroup;

import java.lang.reflect.Method;
import java.util.Objects;

import org.openmcptools.annotation.McpTool;
import org.openmcptools.annotation.McpTool.McpAnnotations;
import org.openmcptools.annotation.McpToolGroup;
import org.openmcptools.common.model.Group;
import org.openmcptools.common.model.Tool;
import org.openmcptools.common.model.ToolAnnotations;
import org.openmcptools.common.server.InputSchemaGenerator;
import org.openmcptools.common.server.OutputSchemaGenerator;
import org.openmcptools.common.util.StringUtils;

public class ToolProviderImpl implements ToolProvider {

	protected InputSchemaGenerator inputSchemaGenerator;
	protected OutputSchemaGenerator outputSchemaGenerator;

	public ToolProviderImpl(InputSchemaGenerator inputSchemaGenerator) {
		this(inputSchemaGenerator, null);
	}

	public ToolProviderImpl(InputSchemaGenerator inputSchemaGenerator, OutputSchemaGenerator outputSchemaGenerator) {
		Objects.requireNonNull(inputSchemaGenerator, "inputSchemaGenerator must not be null");
		this.inputSchemaGenerator = inputSchemaGenerator;
		this.outputSchemaGenerator = outputSchemaGenerator;
	}

	protected Group createGroup(String name, String title, String description, Group parent) {
		Group result = new Group(StringUtils.cleanAnnotationString(name));
		result.setTitle(StringUtils.cleanAnnotationString(title));
		result.setDescription(StringUtils.cleanAnnotationString(description));
		result.setParent(parent);
		return result;
	}

	protected Package getParentPackage(String packageName, ClassLoader classLoader) {
		String packageInfoClassname = packageName + ".package-info";
		try {
			return classLoader.loadClass(packageInfoClassname).getPackage();
		} catch (ClassNotFoundException e) {
			return null;
		}
	}

	protected Group doGetToolGroup(McpToolGroup annotation, Class<?> clazz) {
		// First look for McpToolGroup annotations on package hierarchy
		Group parentGroup = getGroupFromPackage(clazz.getPackage(), clazz.getClassLoader());

		String parentGroupName = annotation.name();
		if (!StringUtils.hasText(parentGroupName)) {
			if (parentGroup != null) {
				parentGroupName = getFullyQualifiedGroupName(parentGroup) + SEPARATOR + clazz.getSimpleName();
			} else {
				parentGroupName = clazz.getName();
			}
		}
		return createGroup(parentGroupName, annotation.title(), annotation.description(), parentGroup);
	}

	protected Group getGroupFromPackage(Package p, ClassLoader classloader) {
		McpToolGroup packageAnnotation = getGroupAnnotation(p);
		// Get parent package
		if (packageAnnotation != null) {
			Group parentGroup = null;
			String currentPackageName = p.getName();
			String parentPackageName = null;
			String childPackageName = null;
			int lastDotIndex = currentPackageName.lastIndexOf(SEPARATOR);
			if (lastDotIndex > 0 && lastDotIndex < currentPackageName.length()) {
				parentPackageName = currentPackageName.substring(0, lastDotIndex);
				childPackageName = currentPackageName.substring(lastDotIndex + 1);
			}

			if (parentPackageName != null) {
				Package parentPackage = getParentPackage(parentPackageName, classloader);
				if (parentPackage != null) {
					parentGroup = getGroupFromPackage(parentPackage, classloader);
				}
			}

			String packageGroupName = packageAnnotation.name();
			if (!StringUtils.hasText(packageGroupName)) {
				if (parentGroup != null) {
					packageGroupName = getFullyQualifiedGroupName(parentGroup) + SEPARATOR + childPackageName;
				} else {
					packageGroupName = currentPackageName;
				}
			}
			return createGroup(packageGroupName, packageAnnotation.title(), packageAnnotation.description(),
					parentGroup);
		}
		return null;
	}

	protected String getFullyQualifiedGroupName(Group parentGroup) {
		return parentGroup.getFullyQualifiedName();
	}

	protected McpToolGroup getGroupAnnotation(Package p) {
		return p.getAnnotation(McpToolGroup.class);
	}

	protected McpToolGroup getGroupAnnotation(Class<?> toolObjectClass) {
		return toolObjectClass.getAnnotation(McpToolGroup.class);
	}

	protected String qualifyToolName(String toolName, Group group) {
		return (group == null) ? toolName : group.getFullyQualifiedName() + SEPARATOR + toolName;
	}

	protected ToolAnnotations getToolAnnotations(McpAnnotations mcpToolAnnotations, Tool tool) {
		if (mcpToolAnnotations != null) {
			ToolAnnotations toolAnnotations = new ToolAnnotations();
			toolAnnotations.setTitle(StringUtils.cleanAnnotationString(mcpToolAnnotations.title()));
			toolAnnotations.setDestructiveHint(mcpToolAnnotations.destructiveHint());
			toolAnnotations.setIdempotentHint(mcpToolAnnotations.idempotentHint());
			toolAnnotations.setOpenWorldHint(mcpToolAnnotations.openWorldHint());
			toolAnnotations.setReadOnlyHint(mcpToolAnnotations.readOnlyHint());
			// if no title already set, set it to toolAnnotationsNode title
			if (tool.getTitle() == null) {
				tool.setTitle(toolAnnotations.getTitle());
			}
			return toolAnnotations;
		}
		return null;
	}

	@Override
	public Tool getTool(McpTool mcpToolAnnotation, Method mcpToolMethod, Group group, boolean generateOutputSchema) {
		String name = StringUtils.cleanAnnotationString(mcpToolAnnotation.name());
		if (name == null) {
			name = mcpToolMethod.getName();
		}
		String fqName = qualifyToolName(name, group);

		Tool result = new Tool(fqName);
		if (group != null) {
			result.addParentGroup(group);
		}
		result.setDescription(StringUtils.cleanAnnotationString(mcpToolAnnotation.description()));
		result.setTitle(StringUtils.cleanAnnotationString(mcpToolAnnotation.title()));
		result.setToolAnnotations(getToolAnnotations(mcpToolAnnotation.annotations(), result));
		result.setInputSchema(this.inputSchemaGenerator.generateInputSchema(mcpToolMethod));
		if (generateOutputSchema && this.outputSchemaGenerator != null) {
			result.setOutputSchema(this.outputSchemaGenerator.generateOutputSchema(mcpToolMethod));
		}
		return result;
	}

}
