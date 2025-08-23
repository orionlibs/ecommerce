/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.userprofile.localesetting;

import com.hybris.backoffice.masterdetail.MDDetailLogic;
import com.hybris.backoffice.masterdetail.MasterDetailService;
import com.hybris.backoffice.masterdetail.SettingButton;
import com.hybris.backoffice.masterdetail.SettingItem;
import com.hybris.backoffice.widgets.quicktogglelocale.controller.QuickToggleLocaleController;
import java.util.Arrays;
import org.zkoss.zk.ui.Component;

public class LocaleSettingController extends QuickToggleLocaleController implements MDDetailLogic
{
    private static final int POSITION_ORDER = 10;
    private transient MasterDetailService userProfileSettingService;
    private transient SettingItem settingItem;


    @Override
    public void initialize(final Component comp)
    {
        this.refreshModel();
        this.refreshUILocaleModel();
        this.getLocalesList().setItemRenderer(this.createRenderer());
        final var currentLocale = this.getCockpitLocaleService().getCurrentLocale();
        this.getUiLocalesList().setItemRenderer(this.createUILocaleRenderer(currentLocale));
        getUserProfileSettingService().registerDetail(this);
    }


    public SettingItem getSettingItem()
    {
        if(settingItem == null)
        {
            final var currentLocale = getCockpitLocaleService().getCurrentLocale();
            settingItem = new SettingItem("backoffice-language-view", "world", getLabel("title"),
                            currentLocale.getDisplayName(currentLocale), false, Arrays.asList(new SettingButton.Builder().build()), POSITION_ORDER);
        }
        return settingItem;
    }


    @Override
    public boolean save()
    {
        return true;
    }


    @Override
    public void reset()
    {
        // Do nothing because it is needed to implement for MDDetailLogic
    }


    @Override
    public boolean isDataChanged()
    {
        return false;
    }


    @Override
    public boolean needRefreshUI()
    {
        return false;
    }


    public void setUserProfileSettingService(final MasterDetailService userProfileSettingService)
    {
        this.userProfileSettingService = userProfileSettingService;
    }


    protected MasterDetailService getUserProfileSettingService()
    {
        return userProfileSettingService;
    }
}
