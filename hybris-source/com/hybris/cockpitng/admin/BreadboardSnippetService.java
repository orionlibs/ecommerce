/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.admin;

import com.hybris.cockpitng.breadboard.BreadboardSnippet;
import java.util.List;

/**
 * Service in order to get a list of snippets of type BreadboardSnippet
 */
public interface BreadboardSnippetService
{
    /**
     * @return a list of BreadboardSnippet
     */
    List<BreadboardSnippet> getSnippets();
}
