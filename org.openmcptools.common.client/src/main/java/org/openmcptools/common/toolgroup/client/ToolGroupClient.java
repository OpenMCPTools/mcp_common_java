package org.openmcptools.common.toolgroup.client;

import java.io.Closeable;
import java.util.List;

import org.openmcptools.common.client.InitializeResult;
import org.openmcptools.common.model.Group;
import org.openmcptools.common.model.Tool;

public interface ToolGroupClient extends Closeable {

	InitializeResult initialize();

	List<Tool> getTools();

	List<Group> getToolGroupRoots();
	
	void refresh();

}
