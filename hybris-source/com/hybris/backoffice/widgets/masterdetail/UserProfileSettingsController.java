/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.masterdetail;

import com.hybris.backoffice.masterdetail.MasterDetailService;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.WireVariable;

public class UserProfileSettingsController extends AbstractMasterDetailController
{
    @WireVariable
    protected MasterDetailService userProfileSettingService;


    @Override
    public void preInitialize(final Component comp)
    {
        getMasterDetailService().reset();
    }


    public void setUserProfileSettingService(final MasterDetailService userProfileSettingService)
    {
        this.userProfileSettingService = userProfileSettingService;
    }


    @Override
    protected MasterDetailService getMasterDetailService()
    {
        return this.userProfileSettingService;
    }
}
