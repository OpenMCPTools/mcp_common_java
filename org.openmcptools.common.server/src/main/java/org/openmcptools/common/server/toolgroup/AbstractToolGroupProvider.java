package org.openmcptools.common.server.toolgroup;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.stream.Stream;

import org.openmcptools.annotation.McpTool;
import org.openmcptools.annotation.McpToolGroup;
import org.openmcptools.common.model.AbstractBase;
import org.openmcptools.common.model.Group;
import org.openmcptools.common.model.Tool;
import org.openmcptools.common.util.StringUtils;

public abstract class AbstractToolGroupProvider<SpecificationType, ToolType, ExchangeType, CallRequestType, CallResultType>
		implements ToolGroupProvider<SpecificationType, ExchangeType, CallRequestType, CallResultType> {

	protected boolean generateOutputSchema = true;
	protected ToolProviderImpl toolProvider;

	protected AbstractToolGroupProvider() {
	}

	protected Group createGroup(String name, String title, String description, Group parent,
			Map<String, Object> meta) {
		Group Group = new Group(name);
		Group.setTitle(StringUtils.cleanAnnotationString(title));
		Group.setDescription(StringUtils.cleanAnnotationString(description));
		if (parent != null) {
			parent.addChildGroup(Group);
		}
		return Group;
	}

	protected Group getToolGroup(McpToolGroup annotation, Class<?> clazz) {
		// First look for McpToolGroup annotations on package hierarchy
		Group parentGroup = getToolGroupFromPackage(clazz.getPackage(), clazz.getClassLoader(), "");

		String parentGroupName = annotation.name();
		if (!StringUtils.hasText(parentGroupName)) {
			if (parentGroup != null) {
				parentGroupName = clazz.getSimpleName();
			} else {
				parentGroupName = clazz.getName();
			}
		}
		return createGroup(parentGroupName, annotation.title(), annotation.description(), parentGroup, null);
	}

	protected Package getParentPackage(String packageName, ClassLoader classLoader) {
		String packageInfoClassname = packageName + ".package-info";
		try {
			return classLoader.loadClass(packageInfoClassname).getPackage();
		} catch (ClassNotFoundException e) {
			return null;
		}
	}

	protected String[] splitPackageName(String packageName) {
		String parentPackageName = null;
		String childPackageName = null;
		int lastDotIndex = packageName.lastIndexOf(AbstractBase.DEFAULT_SEPARATOR);
		if (lastDotIndex > 0 && lastDotIndex < packageName.length()) {
			parentPackageName = packageName.substring(0, lastDotIndex);
			childPackageName = packageName.substring(lastDotIndex + 1);
		}
		return new String[] { parentPackageName, childPackageName };
	}

	protected Group getToolGroupFromPackage(Package p, ClassLoader classloader, String nameSuffix) {
		Group parentGroup = null;
		String[] splitPackageName = splitPackageName(p.getName());
		if (StringUtils.hasText(splitPackageName[0])) {
			Package parentPackage = getParentPackage(splitPackageName[0], classloader);
			if (StringUtils.hasText(splitPackageName[1])) {
				nameSuffix = splitPackageName[1] + (StringUtils.hasText(nameSuffix) ? AbstractBase.DEFAULT_SEPARATOR + nameSuffix : "");
			}
			if (parentPackage != null) {
				parentGroup = getToolGroupFromPackage(parentPackage, classloader, nameSuffix);
			}
		}
		String packageGroupName = (parentGroup == null) ? p.getName() : nameSuffix;

		McpToolGroup packageAnnotation = p.getAnnotation(McpToolGroup.class);
		if (packageAnnotation != null) {
			// Has annotation, get metadata from annotation
			String annotationName = packageAnnotation.name();
			packageGroupName = StringUtils.hasText(annotationName) ? annotationName : packageGroupName;
			return createGroup(packageGroupName, packageAnnotation.title(), packageAnnotation.description(),
					parentGroup, null);
		} else if (parentGroup != null) {
			// Doesn't have annotation, but has a parent, so create a Group
			return createGroup(packageGroupName, null, null, parentGroup, null);
		}
		// No group node found
		return null;
	}

	protected Method[] getMethodsForClass(Class<?> toolClass) {
		// For interfaces, getMethods() gets super interface methods
		if (toolClass.isInterface()) {
			return toolClass.getMethods();
		} else {
			// Get only the declared methods
			return toolClass.getDeclaredMethods();
		}
	}

	protected Group getToolGroup(Class<?> clazz) {
		McpToolGroup tgAnnotation = clazz.getAnnotation(McpToolGroup.class);
		return tgAnnotation != null ? getToolGroup(tgAnnotation, clazz) : null;
	}

	protected Class<?>[] getClassesForObject(Object toolObject, Class<?>[] classes) {
		return (classes.length == 0) ? new Class[] { toolObject.getClass() } : classes;
	}

	protected abstract ToolSpecification<SpecificationType> getToolNodeSpecification(Tool tool,
			BiFunction<ExchangeType, CallRequestType, CallResultType> callHandler);

	protected McpTool getToolJavaAnnotation(Method mcpToolMethod) {
		return mcpToolMethod.getAnnotation(McpTool.class);
	}

	protected Tool getTool(McpTool toolJavaAnnotation, Method mcpToolMethod, Group toolGroup) {
		return this.toolProvider.getTool(toolJavaAnnotation, mcpToolMethod, toolGroup,
				this.generateOutputSchema);
	}

	protected abstract BiFunction<ExchangeType, CallRequestType, CallResultType> getCallHandler(Method mcpToolMethod,
			Object toolObject, boolean useStructuredOutput);

	protected ToolSpecification<SpecificationType> getToolGroupSpecification(Object toolObject,
			Method mcpToolMethod, Group toolGroup) {
		// Get annotation
		McpTool toolJavaAnnotation = getToolJavaAnnotation(mcpToolMethod);
		// Get ToolNode for annotation, method, and toolGroup
		Objects.requireNonNull(toolJavaAnnotation,
				"No java annotation found for annotated method=" + mcpToolMethod.getName());
		Tool tool = getTool(toolJavaAnnotation, mcpToolMethod, toolGroup);
		// Get callhandler from callHandlerProvider
		BiFunction<ExchangeType, CallRequestType, CallResultType> callHandler = getCallHandler(mcpToolMethod,
				toolObject, tool.getOutputSchema() != null);
		// Build specification with Tool and callHandler
		return getToolNodeSpecification(tool, callHandler);
	}

	protected abstract Stream<Method> filterMethodStream(Stream<Method> inputStream);

	protected List<ToolSpecification<SpecificationType>> getToolGroupSpecifications(Object toolObject,
			Class<?> toolClass, Group toolGroup) {
		return filterMethodStream(Stream.of(getMethodsForClass(toolClass))
				// first filter for the McpTool annotation
				.filter(method -> method.isAnnotationPresent(McpTool.class)))
				// After the the sub-class specific impl of filterMethodStream returns
				// then sort call getToolGroupSpecification for given object, mcpToolMethod and
				.sorted((m1, m2) -> m1.getName().compareTo(m2.getName())).map(mcpToolMethod -> {
					return (ToolSpecification<SpecificationType>) getToolGroupSpecification(toolObject,
							mcpToolMethod, toolGroup);
				}).toList();
	}

	public List<ToolSpecification<SpecificationType>> getToolGroupSpecifications(List<Object> toolObjects,
			Class<?>... classes) {
		return toolObjects.stream().map(toolObject -> {
			return Stream.of(getClassesForObject(toolObject, classes)).map(toolClass -> {
				Group toolGroup = getToolGroup(toolClass);
				return getToolGroupSpecifications(toolObject, toolClass, toolGroup);
			}).flatMap(List::stream).toList();
		}).flatMap(List::stream).toList();
	}

	@Override
	public List<ToolSpecification<SpecificationType>> getToolGroupSpecifications(Object toolGroupObject,
			Class<?>... classes) {
		return getToolGroupSpecifications(List.of(toolGroupObject), classes);
	}

	@Override
	public ToolSpecification<SpecificationType> getToolSpecification(Tool tool, Method toolMethod,
			Object instance, boolean outputSchema) {
		BiFunction<ExchangeType, CallRequestType, CallResultType> callHandler = getCallHandler(toolMethod, instance, outputSchema);
		return getToolNodeSpecification(tool, callHandler);
	}


}
