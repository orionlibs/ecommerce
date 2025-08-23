/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.persistence.impl;

import com.google.common.base.Joiner;
import com.hybris.cockpitng.core.persistence.WidgetAccessResolver;
import com.hybris.cockpitng.core.persistence.impl.jaxb.AccessSettings;
import com.hybris.cockpitng.core.persistence.impl.jaxb.AddAccess;
import com.hybris.cockpitng.core.persistence.impl.jaxb.RemoveAccess;
import com.hybris.cockpitng.core.persistence.impl.jaxb.ReplaceAccess;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

public class DefaultWidgetAccessResolver implements WidgetAccessResolver
{
    @Override
    public String resolveAccess(final String widgetCurrentAccess, final AccessSettings widgetExtensionAccess)
    {
        if(widgetExtensionAccess == null)
        {
            return widgetCurrentAccess;
        }
        final ReplaceAccess replaceAccess = widgetExtensionAccess.getReplace();
        if(shouldReplaceWidgetAccess(replaceAccess))
        {
            final Set<String> newAccess = splitAccessString(replaceAccess.getValue());
            return Joiner.on(',').join(newAccess);
        }
        final Set<String> newAccess = new LinkedHashSet<>();
        if(widgetCurrentAccess != null && StringUtils.isNotBlank(widgetCurrentAccess))
        {
            newAccess.addAll(splitAccessString(widgetCurrentAccess));
        }
        resolveAddingAccess(widgetExtensionAccess, newAccess);
        resolveRemovingAccess(widgetExtensionAccess, newAccess);
        return Joiner.on(',').join(newAccess);
    }


    private static boolean shouldReplaceWidgetAccess(final ReplaceAccess replaceAccess)
    {
        return replaceAccess != null && StringUtils.isNotBlank(replaceAccess.getValue());
    }


    private static void resolveAddingAccess(final AccessSettings widgetExtensionAccess, final Set<String> newAccess)
    {
        final AddAccess addAccess = widgetExtensionAccess.getAdd();
        if(addAccess != null && StringUtils.isNotBlank(addAccess.getValue()))
        {
            newAccess.addAll(splitAccessString(addAccess.getValue()));
        }
    }


    private static void resolveRemovingAccess(final AccessSettings widgetExtensionAccess, final Set<String> newAccess)
    {
        final RemoveAccess removeAccess = widgetExtensionAccess.getRemove();
        if(removeAccess != null && StringUtils.isNotBlank(removeAccess.getValue()))
        {
            final Set<String> removeAccessSplit = splitAccessString(removeAccess.getValue());
            if(!removeAccessSplit.isEmpty())
            {
                newAccess.removeIf(removeAccessSplit::contains);
            }
        }
    }


    private static Set<String> splitAccessString(final String value)
    {
        if(value == null)
        {
            return new LinkedHashSet<>();
        }
        final String[] split = value.split(",");
        return new LinkedHashSet<>(Arrays.asList(split));
    }
}
