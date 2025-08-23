/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.notificationarea.event;

/**
 * @deprecated since 1811
 * @see com.hybris.cockpitng.util.notifications.event.NotificationMessageWithLink
 */
@Deprecated(since = "1811", forRemoval = true)
public class NotificationMessageWithLink
{
    private String prefix;
    private String link;
    private String suffix;
    private Integer index;


    public String getPrefix()
    {
        return prefix;
    }


    public void setPrefix(final String prefix)
    {
        this.prefix = prefix;
    }


    public String getLink()
    {
        return link;
    }


    public void setLink(final String link)
    {
        this.link = link;
    }


    public String getSuffix()
    {
        return suffix;
    }


    public void setSuffix(final String suffix)
    {
        this.suffix = suffix;
    }


    public Integer getIndex()
    {
        return index;
    }


    public void setIndex(final Integer index)
    {
        this.index = index;
    }
}
