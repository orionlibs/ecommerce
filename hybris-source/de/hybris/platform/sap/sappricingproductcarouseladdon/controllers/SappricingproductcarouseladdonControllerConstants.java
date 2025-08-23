/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.sap.sappricingproductcarouseladdon.controllers;

import de.hybris.platform.sap.sappricingproductcarouseladdon.model.SapProductCarouselComponentModel;

/**
 */
public interface SappricingproductcarouseladdonControllerConstants
{
    // implement here controller constants used by this extension
    /**
     * Controller request mapping view
     */
    public static final String VIEW = "/view/" + SapProductCarouselComponentModel._TYPECODE + "Controller";
    /**
     * Controller
     */
    public static final String CONTROLLER = SapProductCarouselComponentModel._TYPECODE + "Controller";
}
