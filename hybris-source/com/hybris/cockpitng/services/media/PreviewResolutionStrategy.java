/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.services.media;

public interface PreviewResolutionStrategy<T>
{
    boolean canResolve(Object target);


    String resolvePreviewUrl(T target);


    String resolveMimeType(T target);
}
