package io.lhysin.mybatis.ddd.spec;

import io.lhysin.mybatis.ddd.domain.TypedExample;

/**
 * The interface Example.
 *
 * @param <T> the type parameter
 */
public interface Example<T> {

	/**
	 * The enum Null handler.
	 */
	enum NullHandler {
		/**
		 * Include null handler.
		 */
		INCLUDE,
		/**
		 * Ignore null handler.
		 */
		IGNORE
	}

	/**
	 * Gets probe.
	 *
	 * @return the probe
	 */
	T getProbe();

	/**
	 * Is ignore null values boolean.
	 *
	 * @return the boolean
	 */
	boolean isIgnoreNullValues();

	/**
	 * Gets probe type.
	 *
	 * @return the probe type
	 */
	default Class<?> getProbeType() {
		return getProbe().getClass();
	}

	/**
	 * Of example.
	 *
	 * @param <T>   the type parameter
	 * @param probe the probe
	 * @return the example
	 */
	static <T> Example<T> of(T probe) {
		return Example.ofIgnoreNullValues(probe);
	}

	/**
	 * Of include null values example.
	 *
	 * @param <T>   the type parameter
	 * @param probe the probe
	 * @return the example
	 */
	static <T> Example<T> ofIncludeNullValues(T probe) {
		return new TypedExample<>(probe, NullHandler.INCLUDE);
	}

	/**
	 * Of ignore null values example.
	 *
	 * @param <T>   the type parameter
	 * @param probe the probe
	 * @return the example
	 */
	static <T> Example<T> ofIgnoreNullValues(T probe) {
		return new TypedExample<>(probe, NullHandler.IGNORE);
	}
}
