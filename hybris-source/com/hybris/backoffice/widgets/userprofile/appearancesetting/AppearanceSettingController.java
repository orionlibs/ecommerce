/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.userprofile.appearancesetting;

import com.hybris.backoffice.masterdetail.MDDetailLogic;
import com.hybris.backoffice.masterdetail.MasterDetailService;
import com.hybris.backoffice.masterdetail.SettingButton;
import com.hybris.backoffice.masterdetail.SettingButton.TypesEnum;
import com.hybris.backoffice.masterdetail.SettingItem;
import com.hybris.backoffice.model.ThemeModel;
import com.hybris.backoffice.theme.BackofficeThemeLevel;
import com.hybris.backoffice.theme.BackofficeThemeService;
import com.hybris.backoffice.theme.ThemeNotFound;
import com.hybris.cockpitng.util.DefaultWidgetController;
import de.hybris.platform.servicelayer.user.UserService;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;

public class AppearanceSettingController extends DefaultWidgetController implements MDDetailLogic
{
    private static final int POSITION_ORDER = 40;
    private static final Logger LOG = LoggerFactory.getLogger(AppearanceSettingController.class);
    private static final String LOG_ERROR_MESSAGE = "Could not read backoffice theme";
    @WireVariable
    private transient BackofficeThemeService backofficeThemeService;
    private transient MasterDetailService userProfileSettingService;
    private transient UserService userService;
    private boolean isDataChanged = false;
    private transient SettingItem settingItem;
    protected transient ListModel<ThemeModel> availableUserThemes;
    private transient ThemeModel currentUserTheme;
    @Wire
    protected Listbox themeList;


    @Override
    public void preInitialize(final Component comp)
    {
        final List<ThemeModel> availableBackofficeThemes = getBackofficeThemeService().getAvailableThemes();
        currentUserTheme = getBackofficeThemeService().getCurrentUserTheme();
        availableUserThemes = new ListModelList<>(availableBackofficeThemes);
    }


    @Override
    public void initialize(final Component comp)
    {
        if(getBackofficeThemeService().getThemeLevel() == BackofficeThemeLevel.USER)
        {
            getUserProfileSettingService().registerDetail(this);
        }
        super.initialize(comp);
    }


    public void onThemeClick(final Listitem listitem)
    {
        final var selectTheme = (ThemeModel)listitem.getValue();
        if(selectTheme != null && currentUserTheme != null && currentUserTheme.getCode().equals(selectTheme.getCode()))
        {
            return;
        }
        this.isDataChanged = true;
        currentUserTheme = listitem.getValue();
        getUserProfileSettingService().enableSave(true);
    }


    public boolean isSelectedTheme(final String themeCode)
    {
        return themeCode.equals(currentUserTheme.getCode());
    }


    @Override
    public boolean save()
    {
        if(currentUserTheme == null || currentUserTheme.getCode() == null)
        {
            return false;
        }
        try
        {
            getBackofficeThemeService().setCurrentUserTheme(currentUserTheme.getCode());
            return true;
        }
        catch(final ThemeNotFound e)
        {
            LOG.error(LOG_ERROR_MESSAGE, e);
        }
        return false;
    }


    @Override
    public SettingItem getSettingItem()
    {
        if(settingItem == null)
        {
            settingItem = new SettingItem("backoffice-appearance-view", "palette", getLabel("title"),
                            StringUtils.isEmpty(currentUserTheme.getName()) ? currentUserTheme.getCode() : currentUserTheme.getName(), false,
                            Arrays.asList(new SettingButton.Builder().setType(TypesEnum.SAVE).setDisabled(true).build(),
                                            new SettingButton.Builder().setType(TypesEnum.SAVE_AND_CLOSE).setDisabled(true).build(),
                                            new SettingButton.Builder().setType(TypesEnum.CANCEL).build()),
                            POSITION_ORDER);
        }
        return settingItem;
    }


    @Override
    public void reset()
    {
        currentUserTheme = getBackofficeThemeService().getCurrentUserTheme();
        themeList.clearSelection();
        isDataChanged = false;
        final var selectedListItem = themeList.getItems().stream()
                        .filter(item -> ((ThemeModel)item.getValue()).getCode().equals(currentUserTheme.getCode())).findFirst().orElse(null);
        if(selectedListItem != null)
        {
            themeList.setSelectedItem(selectedListItem);
        }
    }


    @Override
    public boolean isDataChanged()
    {
        return isDataChanged;
    }


    @Override
    public boolean needRefreshUI()
    {
        return true;
    }


    public ListModel<ThemeModel> getAvailableUserThemes()
    {
        return availableUserThemes;
    }


    public BackofficeThemeService getBackofficeThemeService()
    {
        return this.backofficeThemeService;
    }


    public void setUserProfileSettingService(final MasterDetailService userProfileSettingService)
    {
        this.userProfileSettingService = userProfileSettingService;
    }


    protected MasterDetailService getUserProfileSettingService()
    {
        return userProfileSettingService;
    }


    protected UserService getUserService()
    {
        return userService;
    }


    public void setUserService(final UserService userService)
    {
        this.userService = userService;
    }
}

