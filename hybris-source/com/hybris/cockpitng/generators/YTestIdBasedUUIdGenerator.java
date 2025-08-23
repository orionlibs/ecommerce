/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.generators;

import static org.apache.commons.lang3.StringUtils.EMPTY;

import com.hybris.cockpitng.engine.impl.DefaultCockpitWidgetEngine;
import com.hybris.cockpitng.util.YTestTools;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.metainfo.Property;
import org.zkoss.zk.ui.sys.IdGenerator;

public class YTestIdBasedUUIdGenerator implements IdGenerator
{
    private static final Logger LOG = LoggerFactory.getLogger(YTestIdBasedUUIdGenerator.class);
    private static final String PATH_SEPARATOR = "___";
    private static final String NAME_PROPERTY_ID = "id";
    private static final String INDEX_SEPARATOR = "_";


    private static String lookupYTestId(final Component comp, final ComponentInfo compInfo)
    {
        return StringUtils.defaultIfEmpty(YTestTools.getYTestId(comp), getComponentId(comp, compInfo));
    }


    private static String getComponentId(final Component comp, final ComponentInfo compInfo)
    {
        if(comp != null && StringUtils.isNotEmpty(comp.getId()))
        {
            return comp.getId();
        }
        else if(compInfo != null)
        {
            final Optional<Property> first = compInfo.getProperties().stream()
                            .filter(prop -> NAME_PROPERTY_ID.equals(prop.getName())).findFirst();
            if(first.isPresent())
            {
                return first.get().getRawValue();
            }
        }
        return null;
    }


    private static String ensureUnique(final Desktop desktop, final String yTestIdPath)
    {
        int count = 0;
        String testId = yTestIdPath.concat(INDEX_SEPARATOR).concat(String.valueOf(count));
        while(true)
        {
            if(desktop.getComponentByUuidIfAny(testId) != null)
            {
                count++;
                LOG.warn("Non unique {} incremented suffix: {} ", DefaultCockpitWidgetEngine.Y_TESTID_PATH, testId);
                testId = yTestIdPath.concat(INDEX_SEPARATOR).concat(String.valueOf(count));
            }
            else
            {
                return testId;
            }
        }
    }


    private static String findClosestYTestIdPath(final Component comp)
    {
        if(comp == null)
        {
            return EMPTY;
        }
        final String pathAttr = (String)comp.getAttribute(DefaultCockpitWidgetEngine.Y_TESTID_PATH);
        return StringUtils.defaultIfEmpty(pathAttr, findClosestYTestIdPath(comp.getParent()));
    }


    @Override
    public String nextComponentUuid(final Desktop desktop, final Component comp, final ComponentInfo compInfo)
    {
        if(comp != null)
        {
            final String ytestId = lookupYTestId(comp, compInfo);
            if(StringUtils.isNotEmpty(ytestId))
            {
                final StringBuilder calculatedId = new StringBuilder(findClosestYTestIdPath(comp));
                if(StringUtils.isNotEmpty(calculatedId))
                {
                    calculatedId.append(PATH_SEPARATOR);
                }
                calculatedId.append(YTestTools.replaceUUIDIllegalChars(ytestId));
                final String uniqueId = ensureUnique(desktop, calculatedId.toString());
                comp.setAttribute(DefaultCockpitWidgetEngine.Y_TESTID_PATH, uniqueId);
                return uniqueId;
            }
        }
        return null;
    }


    @Override
    public String nextPageUuid(final Page page)
    {
        return null;
    }


    @Override
    public String nextDesktopId(final Desktop desktop)
    {
        return null;
    }
}
