package com.minexd.zoot.util;

public interface MethodAccessor<T> {

    T invoke(Object instance, Object... args);

}