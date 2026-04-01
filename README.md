# MCP Common Java API

### NEW (3/31/2026) - Bndtools Project Templates

Bndtools is a [toolset](https://bndtools.org/installation.html) that supports OSGi server (and client) development.  Bndtools has templates to make building OSGi-based applications easier.

Project templates for creating MCP Toolgroups servers and clients have now been added as part of the [ECF Remote Services workspace template](https://github.com/ECF/bndtools.workspace/). These templates create MCP servers and client. 

Please see the [Readme.md for the workspace template repo](https://github.com/ECF/bndtools.workspace) for instructions to create projects and launch the example MCP servers and clients.

### What's in here

The project defines a tree-based data model to organize **tools**, **prompts**, and **resources** into **hierarchical groups** — like folders inside folders.

#### Core model

The [org.openmcptools.common project](https://github.com/OpenMCPTools/mcp_common_java/tree/main/org.openmcptools.common) defines the core MCP model

- **Group** — a tree node. Can contain other groups, tools, prompts, and resources. Each group knows its parent and computes its fully qualified name (e.g. `com.example.api`).
- **Tool** — an MCP tool. Can belong to multiple groups at once.
- **Prompt** — an MCP prompt with typed arguments.
- **Resource** — an MCP resource (URI, size, MIME type).
- **Converter** — generic interface to convert between the internal model and any external format.
This repo exposes a dynamic server (or client) model.  This model exposes MCP type-specific meta-data (e.g. title, description, meta, etc) and accessors as specified by the MCP 11-25-2025 schema.

## MCP Common Server Java API

The [org.openmcptools.common.server project](https://github.com/OpenMCPTools/mcp_common_java/tree/main/org.openmcptools.common.server) exposes API intended specifically for MCP server development. This includes support for runtime processing of [framework-independent annotation types](https://github.com/OpenMCPTools/mcp_annotations_java), the use of [groups/grouping extensions](https://github.com/OpenMCPTools/mcp_extensions_java/tree/main/org.openmcptools.extensions.groups) for [defining groups of tools](https://github.com/OpenMCPTools/mcp_common_java/tree/main/org.openmcptools.common.server/src/main/java/org/openmcptools/common/server/toolgroup).

This API depends (only) upon the MCP Common Java API described above, and the MCP Annotation types from [mcp_annotations_java](https://github.com/OpenMCPTools/mcp_annotations_java).

## Implementation of the Common and Common Server APIs via the MCP Java SDK/Spring

The [org.openmcptools.common.impl.spring](https://github.com/OpenMCPTools/mcp_common_java/tree/main/org.openmcptools.common.impl.spring) project provides a complete and functional implementation of both the MCP Common and MCP Common Server APIs, and uses/depends upon the [MCP Java SDK](https://github.com/modelcontextprotocol/java-sdk) and the [Spring mcp_annotation](https://github.com/scottslewis/mcp-annotations/tree/main/mcp-annotations) projects to provide this impl.

[Here is an example application](https://github.com/ECF/MCPToolGroups) that uses the MCP Common API, MCP Common Server API, and the Spring implementation above to deliver dynamic toolgroups (added dynamically to the server at runtime).
