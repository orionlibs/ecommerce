/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.lazyloading;

/**
 * Wrapper class for handling success/failure result LAZY_DATA
 * @param <LAZY_DATA> type of result
 */
public interface LazyTaskResult<LAZY_DATA>
{
    /**
     * Getter method for result
     * @return Nullable LAZY_DATA
     */
    LAZY_DATA get();


    /**
     * @return Success when result is valid
     */
    boolean isSuccess();
}
