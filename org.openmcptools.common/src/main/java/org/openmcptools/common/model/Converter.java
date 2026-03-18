package org.openmcptools.common.model;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * A generic interface for converting objects between two types.
 *
 * @param <ToType>   the type to convert to
 * @param <FromType> the type to convert from
 */
public interface Converter<ToType, FromType> {

	/**
	 * Converts a list of source objects to a list of target objects.
	 *
	 * @param sources the list of source objects to convert
	 * @return a list of converted target objects
	 * @throws NullPointerException if sources is null
	 */
	default List<ToType> convertTo(List<FromType> sources) {
		Objects.requireNonNull(sources, "sources must not be null");
		return sources.stream().map(s -> {
			return convertTo(s);
		}).collect(Collectors.toList());
	}

	/**
	 * Converts a single source object to the target type.
	 *
	 * @param source the source object to convert
	 * @return the converted object
	 */
	ToType convertTo(FromType source);

	/**
	 * Converts a list of target objects back to a list of source objects.
	 *
	 * @param targets the list of target objects to convert
	 * @return a list of converted source objects
	 * @throws NullPointerException if targets is null
	 */
	default List<FromType> convertFrom(List<ToType> targets) {
		Objects.requireNonNull(targets, "targets must not be null");
		return targets.stream().map(s -> {
			return convertFrom(s);
		}).collect(Collectors.toList());

	}

	/**
	 * Converts a single target object back to the source type.
	 *
	 * @param target the target object to convert
	 * @return the converted source object
	 */
	FromType convertFrom(ToType target);

}