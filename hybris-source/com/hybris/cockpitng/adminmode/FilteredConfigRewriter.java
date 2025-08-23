/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.adminmode;

import com.hybris.cockpitng.adminmode.exception.ContextModificationException;
import com.hybris.cockpitng.core.config.impl.jaxb.Config;
import com.hybris.cockpitng.core.config.impl.jaxb.Context;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.zkoss.util.resource.Labels;

/**
 * Utility class which copy from filtered config to original config. It is responsible for finding modified context in
 * original context. Has validation capability: check if user doesn't add new contexts.
 */
public class FilteredConfigRewriter
{
    private static final String LABEL_CONTEXT_MODIFICATION_EXCEPTION = "context.modification.exception";


    private FilteredConfigRewriter()
    {
        //NOOP Utility class
    }


    public static void applyChangesInFilteredConfig(final Config originalConfig, final Config filteredConfig)
    {
        final Set<Integer> alreadyModifiedIndexes = new HashSet<>();
        for(final Context modifiedCtx : filteredConfig.getContext())
        {
            final Object any = modifiedCtx.getAny();
            final int i = originalConfig.getContext().indexOf(modifiedCtx);
            if(i >= 0)
            {
                if(!alreadyModifiedIndexes.add(i))
                {
                    throw new ContextModificationException(new ReflectionToStringBuilder(modifiedCtx).toString(),
                                    Labels.getLabel(LABEL_CONTEXT_MODIFICATION_EXCEPTION));
                }
                final Context originalContext = originalConfig.getContext().get(i);
                originalContext.setAny(any);
                if(!modifiedCtx.getContext().isEmpty())
                {
                    updateNestedContexts(originalContext, modifiedCtx);
                }
            }
            else
            {
                throw new ContextModificationException(new ReflectionToStringBuilder(modifiedCtx).toString(),
                                Labels.getLabel(LABEL_CONTEXT_MODIFICATION_EXCEPTION));
            }
        }
    }


    protected static void updateNestedContexts(final Context originalContext, final Context modifiedContext)
    {
        final Set<Integer> alreadyModifiedIndexes = new HashSet<>();
        for(final Context modifiedSubContext : modifiedContext.getContext())
        {
            final int i = originalContext.getContext().indexOf(modifiedSubContext);
            if(i >= 0)
            {
                if(!alreadyModifiedIndexes.add(i))
                {
                    throw new ContextModificationException(new ReflectionToStringBuilder(modifiedSubContext).toString(),
                                    Labels.getLabel(LABEL_CONTEXT_MODIFICATION_EXCEPTION));
                }
                originalContext.getContext().get(i).setAny(modifiedSubContext.getAny());
                if(!modifiedSubContext.getContext().isEmpty())
                {
                    updateNestedContexts(originalContext.getContext().get(i), modifiedSubContext);
                }
            }
            else
            {
                throw new ContextModificationException(new ReflectionToStringBuilder(modifiedSubContext).toString(),
                                Labels.getLabel(LABEL_CONTEXT_MODIFICATION_EXCEPTION));
            }
        }
    }
}
