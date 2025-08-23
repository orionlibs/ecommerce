/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.masterdetail;

import java.util.List;

public interface MDMasterLogic
{
    /**
     * Add a list of detail view into master view
     *
     * @param settingItemList
     *           - the list of settingItem data
     */
    void addItems(List<SettingItem> settingItemList);


    /**
     * Update SettingItem data of a detail view in master view
     * e.g. update subtitle in master view
     *
     * @param data
     *           - SettingItem data of the detail view
     */
    void updateItem(SettingItem data);


    /**
     * Enable or disable save buttons in master view
     *
     * @param enabled
     *           - is save buttons enabled
     */
    void enableSave(boolean enabled);
}
