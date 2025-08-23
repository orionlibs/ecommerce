/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.util.js;

/**
 * Class which is responsible for gathering session related information (current user, current locale) and store it in
 * JsWidgetSessionDTO class.
 * DTO class could be amended by AbstractJsWidgetSessionInfoDecorator
 */
public interface JsWidgetSessionInfoCollector
{
    JsWidgetSessionDTO gatherSessionInfo();
}