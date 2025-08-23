/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.async;

import com.hybris.cockpitng.core.async.Progress.ProgressType;
import com.hybris.cockpitng.core.model.Identifiable;

public interface Operation extends Identifiable
{
    @Override
    default Object getId()
    {
        return Integer.valueOf(System.identityHashCode(this));
    }


    ProgressType getProgressType();


    Object execute(Progress progress);


    String getLabel();


    boolean isTerminable();
}
