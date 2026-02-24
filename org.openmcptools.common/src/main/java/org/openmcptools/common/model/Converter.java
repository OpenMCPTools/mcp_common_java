package org.openmcptools.common.model;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public interface Converter<ToType, FromType> {

	default List<ToType> convertTo(List<FromType> sources) {
		Objects.requireNonNull(sources, "sources must not be null");
		return sources.stream().map(s -> {
			return convertTo(s);
		}).collect(Collectors.toList());
	}

	ToType convertTo(FromType source);

	default List<FromType> convertFrom(List<ToType> targets) {
		Objects.requireNonNull(targets, "targets must not be null");
		return targets.stream().map(s -> {
			return convertFrom(s);
		}).collect(Collectors.toList());

	}

	FromType convertFrom(ToType target);

}
