/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.components;

import org.zkoss.zul.impl.XulElement;

public class ProgressBar extends XulElement
{
    private static final long serialVersionUID = 4329363447045935682L;
    private static final String LAST_UPDATE_PERCENTAGE = "lastUpdatePercentage";
    private static final String MAX_PERCENTAGE = "maxPercentage";
    private static final String TIME_TO_INCREASE_ONE_PERCENT = "timeToIncreaseOnePercent";
    private int lastUpdatePercentage;
    private int maxPercentage;
    private long timeToIncreaseOnePercent;


    @Override
    protected void renderProperties(final org.zkoss.zk.ui.sys.ContentRenderer renderer) throws java.io.IOException
    {
        super.renderProperties(renderer);
        render(renderer, LAST_UPDATE_PERCENTAGE, Integer.valueOf(getLastUpdatePercentage()));
        render(renderer, MAX_PERCENTAGE, Integer.valueOf(getMaxPercentage()));
        render(renderer, TIME_TO_INCREASE_ONE_PERCENT, Long.valueOf(getTimeToIncreaseOnePercent()));
    }


    /**
     * @return The string that is currently displayed by the editor.
     */
    public int getLastUpdatePercentage()
    {
        return lastUpdatePercentage;
    }


    /**
     * @param percentage
     *           The percentage of done job.
     */
    public void setLastUpdatePercentage(final int percentage)
    {
        if(lastUpdatePercentage != percentage)
        {
            this.lastUpdatePercentage = percentage;
            smartUpdate(LAST_UPDATE_PERCENTAGE, getLastUpdatePercentage());
        }
    }


    /**
     * @return The string that is max percentage could be reached.
     */
    public int getMaxPercentage()
    {
        return maxPercentage;
    }


    /**
     * @param percentage
     *           The maximum percentage of the job that could be reached.
     */
    public void setMaxPercentage(final int percentage)
    {
        if(maxPercentage != percentage)
        {
            this.maxPercentage = percentage;
            smartUpdate(MAX_PERCENTAGE, getMaxPercentage());
        }
    }


    /**
     * @return The time in milliseconds which indicates how long lasts 1% in progress bar
     */
    public long getTimeToIncreaseOnePercent()
    {
        return timeToIncreaseOnePercent;
    }


    /**
     * Sets the time in milliseconds which is needed to increase progress bar by 1%
     *
     * @param timeToIncreaseOnePercent
     *           time in milliseconds which is needed to increase progress bar by 1%
     */
    public void setTimeToIncreaseOnePercent(final long timeToIncreaseOnePercent)
    {
        if(this.timeToIncreaseOnePercent != timeToIncreaseOnePercent)
        {
            this.timeToIncreaseOnePercent = timeToIncreaseOnePercent;
            smartUpdate(TIME_TO_INCREASE_ONE_PERCENT, getTimeToIncreaseOnePercent());
        }
    }
}
