package org.openmcptools.common.toolgroup.client;

import org.openmcptools.common.client.CallToolRequest;
import org.openmcptools.common.client.CallToolResult;

public interface SyncToolGroupClient extends ToolGroupClient {

	CallToolResult callTool(CallToolRequest request);

}
