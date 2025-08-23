/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.masterdetail.impl;

import com.hybris.backoffice.masterdetail.MDDetailLogic;
import com.hybris.backoffice.masterdetail.MDMasterLogic;
import com.hybris.backoffice.masterdetail.MasterDetailService;
import com.hybris.backoffice.masterdetail.SettingItem;
import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This is a default implementation for master detail service interface
 */
public class DefaultMasterDetailSettingService implements MasterDetailService, Serializable
{
    private static final long serialVersionUID = 2848787133200168497L;
    private transient MDMasterLogic master;
    private transient Map<String, MDDetailLogic> detailLogicMap = new HashMap<>();


    /**
     * Register a master view instance into MasterDetail settings service and render all registered detail views into master
     * view
     *
     * @param masterLogic
     *           - the master view logic
     */
    public void registerMaster(final MDMasterLogic masterLogic)
    {
        this.master = masterLogic;
        final List<SettingItem> settingItemList = detailLogicMap.values().stream().map(MDDetailLogic::getSettingItem)
                        .sorted(Comparator.comparingInt(SettingItem::getPosition)).collect(Collectors.toList());
        this.master.addItems(settingItemList);
    }


    /**
     * Register a detail view instance into MasterDetail settings service Please make sure SettingItem data of this detail
     * view ready when registering
     *
     * @param detailLogic
     *           - the detail view logic
     */
    public void registerDetail(final MDDetailLogic detailLogic)
    {
        detailLogicMap.put(detailLogic.getSettingItem().getId(), detailLogic);
    }


    /**
     * Return all registered detail views
     */
    public List<MDDetailLogic> getDetails()
    {
        return detailLogicMap.values().stream().collect(Collectors.toList());
    }


    /**
     * Clean all registered master view and detail views
     */
    public void reset()
    {
        master = null;
        detailLogicMap.clear();
    }


    /**
     * Make the MasterDetail setting view can be saved or not
     *
     * @param enabled
     *           - is the save operation can be executed
     */
    public void enableSave(final boolean enabled)
    {
        if(master != null)
        {
            master.enableSave(enabled);
        }
    }


    /**
     * Trigger save operation for a detail view
     *
     * @param id
     *           - id of the detail view
     * @return true if save success, false if save failure
     */
    public boolean saveDetail(final String id)
    {
        final MDDetailLogic detailLogic = detailLogicMap.get(id);
        if(detailLogic != null)
        {
            return detailLogic.save();
        }
        return false;
    }


    /**
     * Reset a detail view
     *
     * @param id
     *           - id of the detail view
     */
    public void resetDetail(final String id)
    {
        final MDDetailLogic detailLogic = detailLogicMap.get(id);
        if(detailLogic != null)
        {
            detailLogic.reset();
        }
    }


    /**
     * Notify master view that data changed in a detail view
     *
     * @param settingItem
     *           - changed SettingItem data of the detail view
     */
    public void detailDataChanged(final SettingItem settingItem)
    {
        if(master != null)
        {
            master.updateItem(settingItem);
        }
    }


    /**
     * Check if data changed in detail view
     *
     * @param id
     *           - id of the detail view
     * @return true if data changed or false if not
     */
    public boolean isDetailDataChanged(final String id)
    {
        final MDDetailLogic detailLogic = detailLogicMap.get(id);
        if(detailLogic != null)
        {
            return detailLogic.isDataChanged();
        }
        return false;
    }


    /**
     * Check if need to refresh UI after save
     *
     * @param id
     *           - id of the detail view
     * @return true if need to refresh UI after save or false if not
     */
    public boolean needRefreshUI(final String id)
    {
        final MDDetailLogic detailLogic = detailLogicMap.get(id);
        if(detailLogic != null)
        {
            return detailLogic.needRefreshUI();
        }
        return false;
    }
}
