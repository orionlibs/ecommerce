/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config.impl.jaxb.treecollection;

import javax.xml.bind.annotation.XmlRegistry;

/**
 * This object contains factory methods for each Java content interface and Java element interface generated in the
 * com.hybris.cockpitng.core.config.impl.jaxb.treecollection package.
 * <p>
 * An ObjectFactory allows you to programatically construct new instances of the Java representation for XML content.
 * The Java representation of XML content can consist of schema derived interfaces and classes representing the binding
 * of schema type definitions, element declarations and model groups. Factory methods for each of these are provided in
 * this class.
 */
@XmlRegistry
public class ObjectFactory
{
    /**
     * Create an instance of {@link TreeCollectionNodes }
     */
    public TreeCollectionNodes createTreeCollectionNodes()
    {
        return new TreeCollectionNodes();
    }


    /**
     * Create an instance of {@link TreeNode }
     */
    public TreeNode createTreeNode()
    {
        return new TreeNode();
    }
}
