/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.admin.impl;

import com.hybris.cockpitng.admin.BreadboardSnippetService;
import com.hybris.cockpitng.breadboard.BreadboardSnippet;
import java.util.Collections;
import java.util.List;

/**
 * Default implementation of service in order to get a list of snippets of type BreadboardSnippet
 */
public class DefaultBreadboardSnippetService implements BreadboardSnippetService
{
    private List<BreadboardSnippet> snippets;


    /**
     * @return the snippets
     */
    @Override
    public List<BreadboardSnippet> getSnippets()
    {
        return snippets == null ? Collections.<BreadboardSnippet>emptyList() : snippets;
    }


    /**
     * @param snippets the snippets to set
     */
    public void setSnippets(final List<BreadboardSnippet> snippets)
    {
        this.snippets = snippets;
    }
}
