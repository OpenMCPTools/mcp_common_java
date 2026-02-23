package org.openmcptools.common.impl.spring;

import java.util.HashMap;
import java.util.Map;

import org.openmcptools.common.model.Group;
import org.osgi.service.component.annotations.Component;

@Component(immediate = true, service = GroupConverter.class)
public class GroupConverterImpl implements GroupConverter {

	private static final Map<String, Group> groupCache = new HashMap<String, Group>();

	@Override
	public org.openmcptools.extensions.groups.protocol.Group convertFrom(Group group) {
		org.openmcptools.extensions.groups.protocol.Group g = new org.openmcptools.extensions.groups.protocol.Group(
				group.getName());
		g.title = group.getTitle();
		g.description = group.getDescription();
		g.meta = group.getMeta();
		Group parent = group.getParent();
		if (parent != null) {
			g.parent = convertFrom(parent);
		}
		return g;
	}

	@Override
	public Group convertTo(org.openmcptools.extensions.groups.protocol.Group group) {
		String groupName = group.name;
		Group gtn = groupCache.get(groupName);
		if (gtn == null) {
			gtn = new Group(groupName);
			groupCache.put(groupName, gtn);
		}
		gtn.setTitle(group.title);
		gtn.setDescription(group.description);
		gtn.setMeta(group.meta);
		org.openmcptools.extensions.groups.protocol.Group parent = group.parent;
		Group convertedParent = null;
		if (parent != null) {
			convertedParent = convertTo(parent);
			convertedParent.addChildGroup(gtn);
		}
		return gtn;
	}

}
