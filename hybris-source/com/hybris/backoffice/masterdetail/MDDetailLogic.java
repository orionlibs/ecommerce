/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.masterdetail;

/**
 * Logic for a Detail view in MasterDetail settings view
 */
public interface MDDetailLogic
{
    /**
     * Returns the setting item data of this detail view
     * @return a SettingItem data of detail view
     */
    SettingItem getSettingItem();


    /**
     * Perform save operation
     * @return true if save success, false if save failure
     */
    boolean save();


    /**
     * Resets the detail view to the initial state
     */
    void reset();


    /**
     * Check if data changed in detail view
     * @return true if data changed or false if not
     */
    boolean isDataChanged();


    /**
     * Check if need to refresh UI after save
     * @return true if need to refresh UI after save or false if not
     */
    boolean needRefreshUI();
}
