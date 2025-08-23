/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.validation.model;

/**
 *
 *
 */
public interface ValidationInfoWrapper
{
    ValidationInfo getInfo();


    static ValidationInfo getRoot(final ValidationInfo info)
    {
        if(info instanceof ValidationInfoWrapper)
        {
            return getRoot(((ValidationInfoWrapper)info).getInfo());
        }
        else
        {
            return info;
        }
    }
}
