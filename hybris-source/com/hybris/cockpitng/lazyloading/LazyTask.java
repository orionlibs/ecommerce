/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.lazyloading;

public interface LazyTask
{
    /**
     * Method for executing task. Execution lasts much time usually.
     */
    void executeTask();


    /**
     * Executes callback. Should be called when <b>executeTask</b> method is finished
     */
    void executeOnDone();
}
