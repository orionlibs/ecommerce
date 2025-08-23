/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.masterdetail;

import java.util.List;

/**
 * This is a interface which used for master detail service
 */
public interface MasterDetailService
{
    /**
     * Register a master view instance into MasterDetail settings service and render all registered detail views into master
     * view
     *
     * @param masterLogic
     *           - the master view logic
     */
    void registerMaster(MDMasterLogic masterLogic);


    /**
     * Register a detail view instance into MasterDetail settings service Please make sure SettingItem data of this detail
     * view ready when registering
     *
     * @param detailLogic
     *           - the detail view logic
     */
    void registerDetail(MDDetailLogic detailLogic);


    /**
     * Return all registered detail views
     */
    List<MDDetailLogic> getDetails();


    /**
     * Clean all registered master view and detail views
     */
    void reset();


    /**
     * Make the MasterDetail setting view can be saved or not
     *
     * @param enabled
     *           - is the save operation can be executed
     */
    void enableSave(boolean enabled);


    /**
     * Trigger save operation for a detail view
     *
     * @param id
     *           - id of the detail view
     * @return true if save success, false if save failure
     */
    boolean saveDetail(String id);


    /**
     * Reset a detail view
     *
     * @param id
     *           - id of the detail view
     */
    void resetDetail(String id);


    /**
     * Notify master view that data changed in a detail view
     *
     * @param settingItem
     *           - changed SettingItem data of the detail view
     */
    void detailDataChanged(SettingItem settingItem);


    /**
     * Check if data changed in detail view
     *
     * @param id
     *           - id of the detail view
     * @return true if data changed or false if not
     */
    boolean isDetailDataChanged(final String id);


    /**
     * Check if need to refresh UI after save
     *
     * @param id
     *           - id of the detail view
     * @return true if need to refresh UI after save or false if not
     */
    boolean needRefreshUI(final String id);
}
