/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.masterdetail;

import java.util.List;

public class SettingItem
{
    private String id;
    private String icon;
    private String title;
    private String subtitle;
    private boolean isActive;
    private List<SettingButton> buttons;
    private int position;


    public SettingItem(final String id, final String icon, final String title, final String subtitle, final boolean isActive,
                    final List<SettingButton> buttons, final int position)
    {
        this.id = id;
        this.icon = icon;
        this.title = title;
        this.subtitle = subtitle;
        this.isActive = isActive;
        this.buttons = buttons;
        this.position = position;
    }


    public String getId()
    {
        return id;
    }


    public String getIcon()
    {
        return icon;
    }


    public String getTitle()
    {
        return title;
    }


    public String getSubtitle()
    {
        return subtitle;
    }


    public boolean isActive()
    {
        return isActive;
    }


    public int getPosition()
    {
        return position;
    }


    public List<SettingButton> getButtons()
    {
        return buttons;
    }
}
