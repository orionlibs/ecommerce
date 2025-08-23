/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.admin;

import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.core.persistence.packaging.CockpitResourceCache;
import com.hybris.cockpitng.util.CockpitZulCache;
import com.hybris.cockpitng.util.ViewAnnotationAwareComposer;
import java.util.ArrayList;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Div;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.SimpleListModel;

/**
 * Compose the page with monitoring capabilities
 */
public class MonitorWindowComposer extends ViewAnnotationAwareComposer
{
    private transient CockpitResourceCache cockpitResourceCache;
    private transient CockpitZulCache cockpitZulCache;
    private Listbox resList;
    private Listbox zulList;
    private Div zulCache;
    private Div resourceCache;
    public final int timerDelay = 1000;
    private Integer[] cacheLastVals = new Integer[50];
    private Integer[] zulLastVals = new Integer[50];
    private int cacheMaxVal = 100;
    private int zulMaxVal = 100;


    @ViewEvent(componentID = "showZulPaths", eventName = Events.ON_CLICK)
    public void showZulPaths()
    {
        zulList.setModel(new SimpleListModel(new ArrayList(cockpitZulCache.getDefinitionPaths())));
    }


    @ViewEvent(componentID = "showResourceCache", eventName = Events.ON_CLICK)
    public void resetResourceCache()
    {
        resList.setModel(new SimpleListModel(new ArrayList(cockpitResourceCache.getKeys())));
    }


    @ViewEvent(componentID = "resetZulCache", eventName = Events.ON_CLICK)
    public void cleareZulCache()
    {
        cockpitZulCache.reset();
    }


    @ViewEvent(componentID = "resetResourceCache", eventName = Events.ON_CLICK)
    public void cleareResourceCache()
    {
        cockpitResourceCache.reset();
    }


    @ViewEvent(componentID = "timer", eventName = Events.ON_TIMER)
    public synchronized void renderGraph()
    {
        drawGraphs();
    }


    protected synchronized void drawGraphs()
    {
        final int resVal = (int)cockpitResourceCache.getSize();
        cacheMaxVal = resVal > cacheMaxVal ? calcMax(resVal) : cacheMaxVal;
        final int zulVal = cockpitZulCache.getDefinitionPaths().size();
        zulMaxVal = zulVal > zulMaxVal ? calcMax(zulVal) : zulMaxVal;
        cacheLastVals = addValue(resVal, cacheLastVals);
        zulLastVals = addValue(zulVal, zulLastVals);
        Clients.evalJavaScript("drawGraph(" + getVals(cacheLastVals) + ",\"" + resourceCache.getUuid() + "_canvas\"," + cacheMaxVal
                        + ");" + "drawGraph(" + getVals(zulLastVals) + ",\"" + zulCache.getUuid() + "_canvas\"," + zulMaxVal + ");");
    }


    protected Integer[] shiftVals(final Integer[] lastVals)
    {
        final int size = lastVals.length;
        final Integer[] newLastVals = new Integer[size];
        for(int i = 0; i < size - 1; i++)
        {
            newLastVals[i] = lastVals[i + 1];
        }
        return newLastVals;
    }


    protected Integer[] addValue(final int val, final Integer[] vals)
    {
        final Integer[] newVals = shiftVals(vals);
        newVals[newVals.length - 1] = val;
        return newVals;
    }


    private int calcMax(final int val)
    {
        return (int)Math.floor(val * 1.2);
    }


    private String createValString(final Integer val)
    {
        return "{ Y: " + val + " }";
    }


    private String getVals(final Integer[] vals)
    {
        final StringBuilder ret = new StringBuilder("{ values:[");
        ret.append(createValString(vals[0]));
        for(int i = 1; i < vals.length; i++)
        {
            ret.append(",").append(createValString(vals[i]));
        }
        ret.append("]}");
        return ret.toString();
    }


    @Override
    public synchronized void doAfterCompose(final Component comp) throws Exception
    {
        super.doAfterCompose(comp);
        drawGraphs();
    }


    public int getTimerDelay()
    {
        return timerDelay;
    }
}
