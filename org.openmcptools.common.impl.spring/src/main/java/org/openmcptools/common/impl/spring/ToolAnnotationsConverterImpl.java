package org.openmcptools.common.impl.spring;

import org.osgi.service.component.annotations.Component;

import io.modelcontextprotocol.spec.McpSchema.ToolAnnotations;

@Component(immediate = true, service = ToolAnnotationsConverter.class)
public class ToolAnnotationsConverterImpl
		implements ToolAnnotationsConverter {

	@Override
	public ToolAnnotations convertFrom(org.openmcptools.common.model.ToolAnnotations tool) {
		return new ToolAnnotations(tool.getTitle(), tool.getReadOnlyHint(), tool.getDestructiveHint(),
				tool.getDestructiveHint(), tool.getIdempotentHint(), tool.getReturnDirect());
	}

	@Override
	public org.openmcptools.common.model.ToolAnnotations convertTo(ToolAnnotations ta) {
		org.openmcptools.common.model.ToolAnnotations rta = new org.openmcptools.common.model.ToolAnnotations();
		rta.setTitle(ta.title());
		rta.setDestructiveHint(ta.destructiveHint());
		rta.setIdempotentHint(ta.idempotentHint());
		rta.setOpenWorldHint(ta.openWorldHint());
		return rta;
	}

}
