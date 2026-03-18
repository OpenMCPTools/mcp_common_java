# MCP Common API

A core Java library providing a hierarchical object model for the **Model Context Protocol (MCP)**.

## Overview

OpenMCPTools Common API is designed to simplify the development of MCP-compliant applications by providing a structured, type-safe representation of core protocol entities. It includes support for Tools, Resources, Prompts, and complex hierarchical organization.

## Features

- **Hierarchical Asset Management**: Organize tools and resources into logical groups using a folder-like structure.
- **Fluent Builders**: Create complex objects easily with the built-in Builder pattern.
- **Multi-Modal Content**: Unified support for Text, Image, Audio, and Resource-based content.
- **Metadata Support**: Every entity includes a `meta` map for custom extensions.
- **Thread-Safe**: Designed for concurrent environments using thread-safe collection implementations.

## Project Structure

```text
org.openmcptools.common
├── model           # Core MCP entities (Tool, Resource, Prompt, Group, etc.)
└── util            # Common utilities (String manipulation, etc.)
```

## Getting Started

### Creating a Tool
```java
Tool weatherTool = Tool.builder("get_weather")
    .title("Weather Reporter")
    .description("Returns the current weather for a location")
    .inputSchema("{\"type\": \"object\", ...}")
    .build();
```

### Organizing with Groups
```java
Group pluginGroup = Group.builder("utility_plugins").build();
pluginGroup.addChildTool(weatherTool);

// Fully Qualified Name: utility_plugins.get_weather
System.out.println(weatherTool.getFullyQualifiedName());
```

w ImageContent(base64Data, "image/png");
```

## Requirements
- Java 17 or higher
