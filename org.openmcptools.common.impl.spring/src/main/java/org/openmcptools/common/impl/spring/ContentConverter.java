package org.openmcptools.common.impl.spring;

import org.openmcptools.common.model.Converter;
import org.openmcptools.common.model.content.Content;

import io.modelcontextprotocol.spec.McpSchema;

public interface ContentConverter extends Converter<Content, McpSchema.Content> {

}
