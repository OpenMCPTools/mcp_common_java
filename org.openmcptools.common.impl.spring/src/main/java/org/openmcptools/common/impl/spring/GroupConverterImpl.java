package org.openmcptools.common.impl.spring;

import java.util.HashMap;
import java.util.Map;

import org.openmcptools.common.model.Group;
import org.openmcptools.common.model.GroupConverter;
import org.osgi.service.component.annotations.Component;

@Component(immediate=true, service = org.openmcptools.common.model.GroupConverter.class)
public class GroupConverterImpl implements GroupConverter<org.openmcptools.extensions.groups.protocol.Group> {

	private static final Map<String, Group> groupNodeCache = new HashMap<String, Group>();

	@Override
	public org.openmcptools.extensions.groups.protocol.Group convertFromGroup(Group group) {
		org.openmcptools.extensions.groups.protocol.Group g = new org.openmcptools.extensions.groups.protocol.Group(group.getName());
		g.title = group.getTitle();
		g.description = group.getDescription();
		g.meta = group.getMeta();
		Group parent = group.getParent();
		if (parent != null) {
			g.parent = convertFromGroup(parent);
		}
		return g;
	}

	@Override
	public Group convertToGroup(org.openmcptools.extensions.groups.protocol.Group group) {
		String groupName = group.name;
		Group gtn = groupNodeCache.get(groupName);
		if (gtn == null) {
			gtn = new Group(groupName);
			groupNodeCache.put(groupName, gtn);
		}
		gtn.setTitle(group.title);
		gtn.setDescription(group.description);
		gtn.setMeta(group.meta);
		org.openmcptools.extensions.groups.protocol.Group parent = group.parent;
		Group convertedParent = null;
		if (parent != null) {
			convertedParent = convertToGroup(parent);
			convertedParent.addChildGroup(gtn);
		}
		return gtn;
	}

}
