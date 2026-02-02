# MCP Common Java API

This repo exposes a dynamic server (or client) model.  This model exposes MCP type-specific meta-data (e.g. title, description, meta, etc) and accessors as specified by the MCP 11-25-2025 schema.

Also included are the enhancements to support hierarchical groups as a new primitive:  Group type that allows members of the other primitives (Tools, Resources, Prompts, etc) into server-defined sets.  These Groups of primitives may optionally be hierarchically organized, with meta-data (title, description, etc) that can be associated with each Group instance for consumption and use by (human or machine) clients.

Each type in the model (e.g. Tool, Resource, Prompt) provides methods for getting meta-data about the instance (e.g. for client access), as well as updating/setting the model meta-data (for server updates).

This library has no dependencies other than the Java 17+ API.
