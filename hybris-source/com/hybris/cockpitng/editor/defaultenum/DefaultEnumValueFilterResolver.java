/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editor.defaultenum;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

public class DefaultEnumValueFilterResolver implements EnumValueFilterResolver
{
    /**
     * filters values by textQuery and returns a new list containing only matching elements and respects java.lang.Enum data
     * type only
     *
     * @return filtered list of values
     */
    @Override
    public <T> List<T> filterEnumValues(final List<T> values, final String textQuery)
    {
        if(CollectionUtils.isEmpty(values))
        {
            return Collections.emptyList();
        }
        final ArrayList<T> list = Lists.newArrayList(values);
        list.removeIf(input -> {
            if(StringUtils.isBlank(textQuery))
            {
                return false;
            }
            else if(input instanceof Enum)
            {
                return !((Enum<?>)input).name().toLowerCase(Locale.getDefault())
                                .startsWith(textQuery.toLowerCase(Locale.getDefault()));
            }
            return false;
        });
        return list;
    }
}
