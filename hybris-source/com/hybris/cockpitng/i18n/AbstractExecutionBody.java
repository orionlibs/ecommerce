/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.i18n;

/**
 * To execute code in a context of a different Locale then the current one you need to implement this class.
 * See {@link CockpitLocaleService#executeWithLocale(AbstractExecutionBody, java.util.Locale)}.
 *
 */
public abstract class AbstractExecutionBody<T>
{
    /**
     * You can override this method if you want to have a result.
     *
     * @return the return value of your desire
     */
    public T execute()
    {
        // can get overridden in subclasses
        executeWithoutResult();
        return null;
    }


    /**
     * You can override this method if you do not need a result.
     */
    public void executeWithoutResult()
    {
        // can get overridden in subclasses
    }
}
