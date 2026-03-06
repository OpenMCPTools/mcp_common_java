package org.openmcptools.common.model;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.openmcptools.common.model.content.Icon;

public abstract class AbstractBase {

	public static final String DEFAULT_SEPARATOR = ".";

	protected final String nameSeparator;

	protected final String name;

	protected String title;

	protected String description;

	protected Map<String, Object> meta;

	protected List<Icon> icons;

	protected AbstractBase(String name) {
		this(name, DEFAULT_SEPARATOR);
	}

	protected AbstractBase(String name, String nameSeparator) {
		if (name == null || name.isEmpty() || name.isBlank()) {
			throw new IllegalArgumentException("name must not be null, empty, or blank");
		}
		this.name = name;
		if (name == null || name.isEmpty() || name.isBlank()) {
			throw new IllegalArgumentException("nameSeparator must not be null, empty, or blank");
		}
		this.nameSeparator = nameSeparator;
	}

	public String getNameSeparator() {
		return this.nameSeparator;
	}

	public String getName() {
		return name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Icon> getIcons() {
		return icons;
	}

	public void setIcons(List<Icon> icons) {
		this.icons = icons;
	}

	public Map<String, Object> getMeta() {
		return this.meta;
	}

	public void setMeta(Map<String, Object> meta) {
		this.meta = meta;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractBase other = (AbstractBase) obj;
		return Objects.equals(name, other.name);
	}

	public abstract String getFullyQualifiedName();
}
