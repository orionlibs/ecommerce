/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.util.js;

/**
 * Interface which allows to store additional data to JsWidgetSessionDTO object.
 */
public interface JsWidgetSessionInfoDecorator
{
    JsWidgetSessionDTO decorate(JsWidgetSessionDTO dto);
}
