/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.components;

import java.io.IOException;
import java.util.Date;
import java.util.function.Supplier;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.sys.ContentRenderer;
import org.zkoss.zul.Label;

/**
 * A component which displays an elapsed time. The time is updated every second on a client side. <br>
 * By default time is displayed in following way: hh:mm:ss. But it can be modified by settings:
 * <ul>
 * <li>{@link #setDisplayDays(boolean)}</li>
 * <li>{@link #setDaysDelimiter(String)}</li>
 * <li>{@link #setDisplayEmptyHours(boolean)}</li>
 * <li>{@link #setTimeDelimiter(String)}</li>
 * </ul>
 * It gives us following format: d(d lt 0 and displayDays==true){daysDelimiter}hh(h lt 0 or d lt
 * 0||displayEmptyHours==true){timeDelimiter}mm{timeDelimiter}ss
 * <p>
 * If {@link #getStartTime()} and {@link #getStopTime()} are set and stopwatch is not started then it displays
 * difference between these two points in time.
 * </p>
 * This component should be used display time in the UI.
 */
public class Stopwatch extends Label
{
    private static final long serialVersionUID = 2507073571272357759L;
    public static final int TIME_NOT_SET = -1;
    public static final String START_TIME = "startTime";
    public static final String STOP_TIME = "stopTime";
    public static final String LABEL_RUNNING = "running";
    public static final String DISPLAY_EMPTY_HOURS = "displayEmptyHours";
    public static final String DISPLAY_DAYS = "displayDays";
    public static final String TIME_DELIMITER = "timeDelimiter";
    public static final String DAYS_DELIMITER = "daysDelimiter";
    public static final String LABEL_RESET = "reset";
    private long startTime = TIME_NOT_SET;
    private long stopTime = TIME_NOT_SET;
    private boolean isRunning;
    private boolean displayEmptyHours;
    private transient Supplier<Long> currentTimeSupplier;
    private boolean displayDays = false;
    private String timeDelimiter = Labels.getLabel("stopwatch.delimiter.time");
    private String daysDelimiter = Labels.getLabel("stopwatch.delimiter.days");


    public Stopwatch()
    {
        currentTimeSupplier = () -> new Date().getTime();
    }


    public long getStopTime()
    {
        return stopTime;
    }


    /**
     * Starts stopwatch - if {@link #getStartTime()} is set then it will count elapsed time from that moment. If
     * {@link #getStopTime()} was set then it will be removed.
     */
    public void start()
    {
        if(!isRunning)
        {
            isRunning = true;
            if(startTime <= TIME_NOT_SET)
            {
                setStartTime(getCurrentTime());
            }
            if(stopTime > TIME_NOT_SET)
            {
                setStopTime(TIME_NOT_SET);
            }
            smartUpdate(LABEL_RUNNING, isRunning);
        }
    }


    /**
     * Stops timer (counting elapsed time in the UI) and sets current time as stop time.
     */
    public void stop()
    {
        if(isRunning)
        {
            isRunning = false;
            setStopTime(getCurrentTime());
            smartUpdate(LABEL_RUNNING, isRunning);
        }
    }


    /**
     * Stops timer and resets start and stop time
     */
    public void reset()
    {
        this.isRunning = false;
        smartUpdate(LABEL_RESET, true);
        setStartTime(TIME_NOT_SET);
        setStopTime(TIME_NOT_SET);
    }


    /**
     * Tells if stopwatch is running.
     *
     * @return true is stopwatch is running.
     */
    public boolean isRunning()
    {
        return isRunning;
    }


    public long getStartTime()
    {
        return startTime;
    }


    public void setStartTime(final long startTime)
    {
        if(this.startTime != startTime)
        {
            this.startTime = startTime;
            smartUpdate(START_TIME, startTime);
        }
    }


    public void setStopTime(final long stopTime)
    {
        if(this.stopTime != stopTime)
        {
            this.stopTime = stopTime;
            smartUpdate(STOP_TIME, stopTime);
        }
    }


    public boolean isDisplayDays()
    {
        return displayDays;
    }


    /**
     * Defines if hours should be rounded to 23 and days displayed (default:false)
     *
     * @param displayDays
     *           - true if days should be displayed.
     */
    public void setDisplayDays(final boolean displayDays)
    {
        if(this.displayDays != displayDays)
        {
            this.displayDays = displayDays;
            smartUpdate(DISPLAY_DAYS, displayDays);
        }
    }


    public String getTimeDelimiter()
    {
        return timeDelimiter;
    }


    /**
     * Defines delimiter between hours, minutes and seconds (default:':' defined in global label stopwatch.delimiter.time)
     *
     * @param timeDelimiter
     *           - timeDelimiter.
     */
    public void setTimeDelimiter(final String timeDelimiter)
    {
        if(this.timeDelimiter == null || !this.timeDelimiter.equals(timeDelimiter))
        {
            this.timeDelimiter = timeDelimiter;
            smartUpdate(TIME_DELIMITER, timeDelimiter);
        }
    }


    public String getDaysDelimiter()
    {
        return daysDelimiter;
    }


    /**
     * Defines delimiter between number of days and hours (default:'d' defined in global label stopwatch.delimiter.days)
     *
     * @param daysDelimiter
     *           - days delimiter.
     */
    public void setDaysDelimiter(final String daysDelimiter)
    {
        if(this.daysDelimiter == null || !this.daysDelimiter.equals(daysDelimiter))
        {
            this.daysDelimiter = daysDelimiter;
            smartUpdate(DAYS_DELIMITER, daysDelimiter);
        }
    }


    public boolean isDisplayEmptyHours()
    {
        return displayEmptyHours;
    }


    /**
     * Defines if empty hours should be displayed (default:false).
     *
     * @param displayEmptyHours
     *           if true then 00:45:26 or 45:26 when false.
     */
    public void setDisplayEmptyHours(final boolean displayEmptyHours)
    {
        if(displayEmptyHours != this.displayEmptyHours)
        {
            this.displayEmptyHours = displayEmptyHours;
            smartUpdate(DISPLAY_EMPTY_HOURS, displayEmptyHours);
        }
    }


    protected long getCurrentTime()
    {
        return getCurrentTimeSupplier().get().longValue();
    }


    /**
     * Sets current time supplier which will be used to obtain current time in milliseconds.
     *
     * @param timeSupplier
     *           the supplier
     */
    public void setCurrentTimeSupplier(final Supplier<Long> timeSupplier)
    {
        this.currentTimeSupplier = timeSupplier;
    }


    public Supplier<Long> getCurrentTimeSupplier()
    {
        return currentTimeSupplier;
    }


    @Override
    protected void renderProperties(final ContentRenderer renderer) throws IOException
    {
        super.renderProperties(renderer);
        render(renderer, START_TIME, startTime);
        render(renderer, STOP_TIME, stopTime);
        render(renderer, LABEL_RUNNING, isRunning);
        render(renderer, DISPLAY_EMPTY_HOURS, displayEmptyHours);
        render(renderer, DISPLAY_DAYS, displayDays);
        render(renderer, TIME_DELIMITER, timeDelimiter);
        render(renderer, DAYS_DELIMITER, daysDelimiter);
    }
}
