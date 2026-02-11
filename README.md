# MCP Common Java API

This repo exposes a dynamic server (or client) model.  This model exposes MCP type-specific meta-data (e.g. title, description, meta, etc) and accessors as specified by the MCP 11-25-2025 schema.

Also included are the enhancements to support hierarchical grouping as a server extension:  Group type that allows members of the other primitives (Tools, Resources, Prompts, etc) into server-defined sets.  These Groups of primitives may optionally be hierarchically organized, with meta-data (title, description, etc) that can be associated with each Group instance for consumption and use by (human or machine) clients.

Each type in the model (e.g. Tool, Resource, Prompt) provides methods for getting meta-data about the instance (e.g. for client access), as well as updating/setting the model meta-data (for server updates).

The common model is in the [org.openmcptools.common project](https://github.com/OpenMCPTools/mcp_common_java/tree/main/org.openmcptools.common).  This project has no dependencies except for Java 17+ API.

## MCP Common Server Java API

The [org.openmcptools.common.server project](https://github.com/OpenMCPTools/mcp_common_java/tree/main/org.openmcptools.common.server) exposes API intended specifically for MCP server development. This includes support for runtime processing of [framework-independent annotation types](https://github.com/OpenMCPTools/mcp_annotations_java), the use of [groups/grouping extensions](https://github.com/OpenMCPTools/mcp_extensions_java/tree/main/org.openmcptools.extensions.groups) for [defining groups of tools](https://github.com/OpenMCPTools/mcp_common_java/tree/main/org.openmcptools.common.server/src/main/java/org/openmcptools/common/server/toolgroup).

This API depends (only) upon the MCP Common Java API described above, and the MCP Annotation types from [mcp_annotations_java](https://github.com/OpenMCPTools/mcp_annotations_java).

## Implementation of the Common and Common Server APIs via the MCP Java SDK/Spring

The [org.openmcptools.common.impl.spring](https://github.com/OpenMCPTools/mcp_common_java/tree/main/org.openmcptools.common.impl.spring) project provides a complete and functional implementation of both the MCP Common and MCP Common Server APIs, and uses/depends upon the [MCP Java SDK](https://github.com/modelcontextprotocol/java-sdk) and the [Spring mcp_annotation](https://github.com/scottslewis/mcp-annotations/tree/main/mcp-annotations) projects to provide this impl.

[Here is an example application](https://github.com/ECF/MCPToolGroups) that uses the MCP Common API, MCP Common Server API, and the Spring implementation above to deliver dynamic toolgroups (added dynamically to the server at runtime).
