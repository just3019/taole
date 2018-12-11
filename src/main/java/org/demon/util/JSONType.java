package org.demon.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @param <T> create at 2014年5月21日 上午10:44:02
 * @author Sean.xie <br/>
 */
public abstract class JSONType<T> {
    final Type type;

    /**
     * Constructs a new type literal. Derives represented class from type
     * parameter.
     * <p/>
     * <p/>
     * Clients create an empty anonymous subclass. Doing so embeds the type
     * parameter in the anonymous class's type hierarchy so we can reconstitute
     * it at runtime despite erasure.
     */
    protected JSONType() {
        this.type = getSuperclassTypeParameter(getClass());
    }

    /**
     * Returns the type from super class's type parameter
     */
    Type getSuperclassTypeParameter(Class<?> subclass) {
        return ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    /**
     * Gets underlying {@code Type} instance.
     */
    public final Type getType() {
        return type;
    }

}
