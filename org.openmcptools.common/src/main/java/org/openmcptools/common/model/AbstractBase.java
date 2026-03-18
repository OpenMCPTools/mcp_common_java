package org.openmcptools.common.model;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Abstract base class for MCP (Model Context Protocol) entities.
 * Provides common properties like name, title, description, and metadata.
 */
public abstract class AbstractBase {

	/**
	 * Default separator used for fully qualified names.
	 */
	public static final String DEFAULT_SEPARATOR = ".";

	/**
	 * Separator used for building the fully qualified name.
	 */
	protected final String nameSeparator;

	/**
	 * The unique name of the entity within its context.
	 */
	protected final String name;

	/**
	 * A human-readable title for the entity.
	 */
	protected String title;

	/**
	 * A detailed description of the entity's purpose.
	 */
	protected String description;

	/**
	 * Additional metadata associated with the entity.
	 */
	protected Map<String, Object> meta;

	/**
	 * List of icons associated with the entity.
	 */
	protected List<Icon> icons;

	/**
	 * Constructs an AbstractBase with the given name and the default separator.
	 * 
	 * @param name the name of the entity
	 */
	protected AbstractBase(String name) {
		this(name, DEFAULT_SEPARATOR);
	}

	/**
	 * Constructs an AbstractBase with the given name and name separator.
	 * 
	 * @param name the name of the entity
	 * @param nameSeparator the separator used for fully qualified names
	 * @throws IllegalArgumentException if name or nameSeparator is null, empty, or blank
	 */
	protected AbstractBase(String name, String nameSeparator) {
		if (name == null || name.isEmpty() || name.isBlank()) {
			throw new IllegalArgumentException("name must not be null, empty, or blank");
		}
		this.name = name;
		if (nameSeparator == null || nameSeparator.isEmpty() || nameSeparator.isBlank()) {
			throw new IllegalArgumentException("nameSeparator must not be null, empty, or blank");
		}
		this.nameSeparator = nameSeparator;
	}

	/**
	 * Returns the name separator.
	 * 
	 * @return the name separator
	 */
	public String getNameSeparator() {
		return this.nameSeparator;
	}

	/**
	 * Returns the name of the entity.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the title of the entity.
	 * 
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the title of the entity.
	 * 
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Returns the description of the entity.
	 * 
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description of the entity.
	 * 
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Returns the list of icons.
	 * 
	 * @return the icons
	 */
	public List<Icon> getIcons() {
		return icons;
	}

	/**
	 * Sets the list of icons.
	 * 
	 * @param icons the icons to set
	 */
	public void setIcons(List<Icon> icons) {
		this.icons = icons;
	}

	/**
	 * Returns the metadata map.
	 * 
	 * @return the meta map
	 */
	public Map<String, Object> getMeta() {
		return this.meta;
	}

	/**
	 * Sets the metadata map.
	 * 
	 * @param meta the meta map to set
	 */
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

	/**
	 * Returns the fully qualified name of the entity.
	 * 
	 * @return the fully qualified name
	 */
	public abstract String getFullyQualifiedName();
}
