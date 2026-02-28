package org.openmcptools.common.impl.spring;

import org.openmcptools.common.client.CallToolResult;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import io.modelcontextprotocol.spec.McpSchema;

@Component(immediate = true, service = CallToolResultConverter.class)
public class CallToolResultConverterImpl implements CallToolResultConverter {

	private final ContentConverter contentConverter;

	@Activate
	public CallToolResultConverterImpl(@Reference ContentConverter contentConverter) {
		this.contentConverter = contentConverter;
	}

	@Override
	public CallToolResult convertTo(McpSchema.CallToolResult source) {
		return new CallToolResult(contentConverter.convertTo(source.content()), source.isError(),
				source.structuredContent(), source.meta());
	}

	@Override
	public io.modelcontextprotocol.spec.McpSchema.CallToolResult convertFrom(CallToolResult target) {
		return new McpSchema.CallToolResult(contentConverter.convertFrom(target.getContent()), target.getIsError(),
				target.getStructuredContent(), target.getMeta());
	}

}
