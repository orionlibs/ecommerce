/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config.impl.model;

import com.hybris.cockpitng.core.config.impl.jaxb.Config;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Model class that defines all conditions that context needs to meet
 */
public class ContextSearchRequest
{
    private final Config contextStack;
    private final Map<String, List<String>> attributesNeedle;
    private final ContextSearchRestriction restriction;
    private boolean notEmpty;


    public ContextSearchRequest(final Config contextStack, final Map<String, List<String>> attributesNeedle,
                    final ContextSearchRestriction restriction)
    {
        this.contextStack = contextStack;
        this.restriction = restriction;
        this.attributesNeedle = new LinkedHashMap<>(attributesNeedle);
    }


    /**
     * Checks whether only not empty configurations are requested
     *
     * @return <code>true</code> if only not empty configurations should be returned by search
     */
    public boolean isNotEmpty()
    {
        return notEmpty;
    }


    /**
     * Informs if only not empty configurations are requested
     *
     * @param notEmpty
     *           <code>true</code> if only not empty configurations should be returned by search
     */
    public void setNotEmpty(final boolean notEmpty)
    {
        this.notEmpty = notEmpty;
    }


    /**
     * Configuration model, which contexts will be searched through
     *
     * @return configuration model
     */
    public Config getContextStack()
    {
        return contextStack;
    }


    /**
     * Map of possible attribute values, that needs to match context attribute for it to returned as a search result
     *
     * @return map of attributes in form &lt;name, list of possible values&gt;
     */
    public Map<String, List<String>> getAttributesNeedle()
    {
        return Collections.unmodifiableMap(attributesNeedle);
    }


    /**
     * Gets a restriction for searching parents.
     * <P>
     * Search process, if no exact match can be found, looks for any matching contexts for parent values of current
     * attributes search needle. A restriction stops the process from looking for parents of specified attributes.
     *
     * @return parents search restriction
     */
    public ContextSearchRestriction getParentRestriction()
    {
        return restriction;
    }


    /**
     * Creates new request on basis of this one with other attributes needle.
     *
     * @param newNeedle
     *           new attributes needle
     * @return new reequest
     */
    public ContextSearchRequest createAlternative(final Map<String, List<String>> newNeedle)
    {
        return createAlternative(newNeedle, restriction);
    }


    /**
     * Creates new request on basis of this one with other attributes needle.
     *
     * @param newNeedle
     *           new attributes needle
     * @return new request
     */
    public ContextSearchRequest createAlternative(final Map<String, List<String>> newNeedle, final ContextSearchRestriction restriction)
    {
        final ContextSearchRequest result = new ContextSearchRequest(contextStack, newNeedle, restriction);
        result.setNotEmpty(notEmpty);
        return result;
    }
}
