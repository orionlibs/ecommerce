package de.hybris.platform.jalo;

import java.util.Enumeration;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface ContextMap<KEY, VALUE> extends Map<KEY, VALUE>
{
    public static final Object REMOVED = (new UUID(0L, 0L)).toString();
    public static final Object NULL = (new UUID(1L, 1L)).toString();
    public static final int INITIAL_CAPACITY = 32;
    public static final int CONCURRENCY_LEVEL_LOCAL = 1;
    public static final int CONCURRENCY_LEVEL_GLOBAL = 16;


    boolean isAttributeHoldingItems(Object paramObject);


    Set<KEY> getAttributesContainingItems();


    Enumeration<KEY> keys();


    VALUE putNoItemCheck(KEY paramKEY, VALUE paramVALUE);
}
