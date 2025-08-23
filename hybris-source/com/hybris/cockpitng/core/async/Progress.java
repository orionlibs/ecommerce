/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.async;

public interface Progress
{
    enum ProgressType
    {
        MANAGED, FAKED, NONE
    }


    void set(int value);


    int get();


    void requestCancel();


    boolean isCancelRequested();
}
