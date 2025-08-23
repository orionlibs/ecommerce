/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.service;

/**
 * The resolver to decide if the notification message link is needed to be ignored for the exception.
 */
public interface ExceptionLinkIgnoreResolver
{
    boolean isMatch(Throwable exception);
}
