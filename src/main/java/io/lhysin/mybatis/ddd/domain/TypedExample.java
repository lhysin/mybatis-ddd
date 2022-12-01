package io.lhysin.mybatis.ddd.domain;

import io.lhysin.mybatis.ddd.spec.Example;

import java.util.Objects;

/**
 * The type Typed example.
 *
 * @param <T> the type parameter
 */
public class TypedExample<T> implements Example<T> {

    private final T probe;
    private final NullHandler nullHandler;

    public TypedExample(T probe){
        this.probe = Objects.requireNonNull(probe);
        this.nullHandler = NullHandler.IGNORE;
    }

    public TypedExample(T probe, NullHandler nullHandler){
        this.probe = Objects.requireNonNull(probe);
        this.nullHandler = nullHandler;
    }

    public T getProbe() {
        return this.probe;
    }

    public boolean isIgnoreNullValues(){
        return NullHandler.IGNORE.equals(this.nullHandler);
    }
}
