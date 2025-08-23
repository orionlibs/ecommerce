/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.persistence;

import com.hybris.cockpitng.core.Widget;

/**
 * Service responsible for storing and loading widget tree from a persistent storage like a DB, file etc.
 */
public interface WidgetPersistenceService
{
    /**
     * Loads widget's tree. Widget can consist any number
     * of children which can form a tree.
     *
     * @param widgetId an id of widget's tree root
     * @return a widget tree
     */
    Widget loadWidgetTree(String widgetId);


    /**
     * Stores widget's tree in file
     *
     * @param widget tree to store
     */
    void storeWidgetTree(Widget widget);


    /**
     * Deletes widget's tree.
     *
     * @param widget tree to delete
     */
    void deleteWidgetTree(Widget widget);


    /**
     * Resets to defaults
     */
    void resetToDefaults();
}
