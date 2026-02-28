package org.openmcptools.common.impl.spring;

import java.util.List;
import java.util.Objects;

import org.openmcptools.common.model.Annotations;
import org.openmcptools.common.model.content.AudioContent;
import org.openmcptools.common.model.content.BlobResourceContents;
import org.openmcptools.common.model.content.Content;
import org.openmcptools.common.model.content.EmbeddedResource;
import org.openmcptools.common.model.content.ImageContent;
import org.openmcptools.common.model.content.ResourceContents;
import org.openmcptools.common.model.content.ResourceLink;
import org.openmcptools.common.model.content.Role;
import org.openmcptools.common.model.content.TextContent;
import org.openmcptools.common.model.content.TextResourceContents;
import org.osgi.service.component.annotations.Component;

import io.modelcontextprotocol.spec.McpSchema;

@Component(immediate = true)
public class ContentConverterImpl implements ContentConverter {

	@Override
	public Content convertTo(io.modelcontextprotocol.spec.McpSchema.Content source) {
		return convertContentTo(source);
	}

	@Override
	public io.modelcontextprotocol.spec.McpSchema.Content convertFrom(Content target) {
		return convertContentFrom(target);
	}

	private Content convertContentTo(McpSchema.Content c) {
		switch (c.type()) {
		case "text":
			return convertText((McpSchema.TextContent) c);
		case "image":
			return convertImage((McpSchema.ImageContent) c);
		case "audio":
			return convertAudio((McpSchema.AudioContent) c);
		case "resource":
			return convertResourceEmbedded((McpSchema.EmbeddedResource) c);
		case "resource_link":
			return convertResourceLink((McpSchema.ResourceLink) c);
		}
		return null;
	}

	private Content convertResourceEmbedded(McpSchema.EmbeddedResource c) {
		return new EmbeddedResource(convertResourceContents(c.resource()), convertAnnotations(c.annotations()),
				c.meta());
	}

	private ResourceContents convertResourceContents(McpSchema.ResourceContents resource) {
		if (resource instanceof McpSchema.TextResourceContents) {
			return convertTextResourceContents((McpSchema.TextResourceContents) resource);
		} else if (resource instanceof McpSchema.BlobResourceContents) {
			return convertBlobResourceContents((McpSchema.BlobResourceContents) resource);
		}
		return null;
	}

	private ResourceContents convertBlobResourceContents(McpSchema.BlobResourceContents resource) {
		return new BlobResourceContents(resource.uri(), resource.mimeType(), resource.blob(), resource.meta());
	}

	private ResourceContents convertTextResourceContents(McpSchema.TextResourceContents r) {
		return new TextResourceContents(r.uri(), r.mimeType(), r.text(), r.meta());
	}

	private McpSchema.Content convertContentFrom(Content c) {
		switch (c.getType()) {
		case TEXT:
			return convertText((TextContent) c);
		case IMAGE:
			return convertImage((ImageContent) c);
		case AUDIO:
			return convertAudio((AudioContent) c);
		case RESOURCE:
			return convertResourceEmbedded((EmbeddedResource) c);
		case RESOURCE_LINK:
			return convertResourceLink((ResourceLink) c);
		}
		return null;
	}

	private io.modelcontextprotocol.spec.McpSchema.Content convertResourceLink(ResourceLink c) {
		return new McpSchema.ResourceLink(c.getName(), c.getTitle(), c.getUri(), c.getDescription(), c.getMimeType(),
				c.getSize(), convertAnnotations(c.getAnnotation()), c.getMeta());
	}

	private McpSchema.EmbeddedResource convertResourceEmbedded(EmbeddedResource c) {
		return new McpSchema.EmbeddedResource(convertAnnotations(c.getAnnotations()),
				convertResourceContents(c.getResource()), c.getMeta());
	}

	private io.modelcontextprotocol.spec.McpSchema.ResourceContents convertResourceContents(ResourceContents r) {
		if (r instanceof BlobResourceContents) {
			return convertBlobResourceContents((BlobResourceContents) r);
		} else if (r instanceof TextResourceContents) {
			return convertTextResourceContents((TextResourceContents) r);
		}
		return null;
	}

	private io.modelcontextprotocol.spec.McpSchema.ResourceContents convertTextResourceContents(
			TextResourceContents r) {
		return new McpSchema.TextResourceContents(r.getUri(), r.getMimeType(), r.getText(), r.getMeta());
	}

	private io.modelcontextprotocol.spec.McpSchema.ResourceContents convertBlobResourceContents(
			BlobResourceContents r) {
		return new McpSchema.BlobResourceContents(r.getUri(), r.getMimeType(), r.getBlob(), r.getMeta());
	}

	private ResourceLink convertResourceLink(McpSchema.ResourceContent c) {
		return new ResourceLink(c.name(), c.title(), c.uri(), c.mimeType(), c.description(), c.size(),
				convertAnnotations(c.annotations()), c.meta());
	}

	private AudioContent convertAudio(McpSchema.AudioContent c) {
		return new AudioContent(c.data(), convertAnnotations(c.annotations()), c.mimeType(), c.meta());
	}

	private TextContent convertText(McpSchema.TextContent c) {
		return new TextContent(c.text(), convertAnnotations(c.annotations()), c.meta());
	}

	private Annotations convertAnnotations(io.modelcontextprotocol.spec.McpSchema.Annotations annotations) {
		if (annotations == null) {
			return null;
		}
		return new Annotations(convertAudienceTo(annotations.audience()), annotations.priority(),
				annotations.lastModified());
	}

	private List<Role> convertAudienceTo(List<io.modelcontextprotocol.spec.McpSchema.Role> audience) {
		if (audience == null) {
			return null;
		}
		return audience.stream().map(r -> {
			if (r == McpSchema.Role.USER) {
				return Role.USER;
			} else if (r == McpSchema.Role.ASSISTANT) {
				return Role.ASSISTANT;
			} else {
				return null;
			}
		}).filter(Objects::nonNull).toList();
	}

	private ImageContent convertImage(McpSchema.ImageContent c) {
		if (c == null) {
			return null;
		}
		return new ImageContent(c.data(), convertAnnotations(c.annotations()), c.mimeType(), c.meta());
	}

	private McpSchema.AudioContent convertAudio(AudioContent c) {
		return new McpSchema.AudioContent(convertAnnotations(c.getAnnotations()), c.getData(), c.getMimeType(),
				c.getMeta());
	}

	private McpSchema.TextContent convertText(TextContent c) {
		return new McpSchema.TextContent(convertAnnotations(c.getAnnotations()), c.getText(), c.getMeta());
	}

	private McpSchema.Annotations convertAnnotations(Annotations annotations) {
		if (annotations == null) {
			return null;
		}
		return new McpSchema.Annotations(convertAudienceFrom(annotations.getAudience()), annotations.getPriority(),
				annotations.getLastModified());
	}

	private List<McpSchema.Role> convertAudienceFrom(List<Role> audience) {
		if (audience == null) {
			return null;
		}
		return audience.stream().map(r -> {
			if (r == Role.USER) {
				return McpSchema.Role.USER;
			} else if (r == Role.ASSISTANT) {
				return McpSchema.Role.ASSISTANT;
			} else {
				return null;
			}
		}).filter(Objects::nonNull).toList();
	}

	private McpSchema.ImageContent convertImage(ImageContent c) {
		if (c == null) {
			return null;
		}
		return new McpSchema.ImageContent(convertAnnotations(c.getAnnotations()), c.getData(), c.getMimeType(),
				c.getMeta());
	}

}
