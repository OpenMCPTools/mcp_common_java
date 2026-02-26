package org.openmcptools.common.toolgroup.client;

import org.openmcptools.common.client.CallToolRequest;

public interface AsyncToolGroupClient<AsyncType> extends ToolGroupClient {

	AsyncType callTool(CallToolRequest request);

}
