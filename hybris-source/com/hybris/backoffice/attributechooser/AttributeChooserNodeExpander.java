/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.attributechooser;

import org.zkoss.zul.Tree;

/**
 * Node expander which opens nodes matching to filter text.
 */
public interface AttributeChooserNodeExpander
{
    /**
     * Opens only that nodes which matching to filter text
     *
     * @param tree
     *           representation of nodes structure
     * @param config
     *           configuration of Attribute Chooser
     * @param filterText
     *           text from {@link org.zkoss.zul.Textbox} which filters nodes
     */
    void filterTree(final Tree tree, final AttributesChooserConfig config, final String filterText);
}
