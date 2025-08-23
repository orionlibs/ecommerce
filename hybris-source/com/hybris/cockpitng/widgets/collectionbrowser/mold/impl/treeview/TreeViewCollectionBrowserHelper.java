/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.collectionbrowser.mold.impl.treeview;

import com.hybris.cockpitng.core.config.CockpitConfigurationException;
import com.hybris.cockpitng.core.config.impl.DefaultConfigContext;
import com.hybris.cockpitng.core.config.impl.jaxb.treecollection.TreeCollectionNodes;
import com.hybris.cockpitng.dataaccess.facades.permissions.PermissionFacade;
import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.dataaccess.facades.type.exceptions.TypeNotFoundException;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.type.ObjectValueService;
import com.hybris.cockpitng.widgets.collectionbrowser.context.DefaultTreeCollectionContextStrategy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TreeViewCollectionBrowserHelper
{
    private static final Logger LOG = LoggerFactory.getLogger(TreeViewCollectionBrowserHelper.class);
    private static final String COLLECTION_BROWSER_COMPONENT = "collection-browser-tree";
    private static final String SETTING_CONFIG_COMPONENT = "treeViewConfigCtx";
    private static final String TREE_CONTEXT_KEY = "tree-context";
    private final WidgetInstanceManager widgetInstanceManager;
    private final TypeFacade typeFacade;
    private final PermissionFacade permissionFacade;
    private final ObjectValueService objectValueService;


    public TreeViewCollectionBrowserHelper(final WidgetInstanceManager widgetInstanceManager, final TypeFacade typeFacade,
                    final PermissionFacade permissionFacade, final ObjectValueService objectValueService)
    {
        this.widgetInstanceManager = widgetInstanceManager;
        this.typeFacade = typeFacade;
        this.permissionFacade = permissionFacade;
        this.objectValueService = objectValueService;
    }


    /**
     * @return list of attribute values for given <code>parentObject</code> if <code>object</code> is an instance of
     *         {@link DataAttribute} or list of {@link DataAttribute}s if it's not
     */
    public List<Object> getAttributesOrValues(final Object object, final Object parentObject)
    {
        if(isObjectInstanceOfDataAttribute(object))
        {
            return getDataAttributeValue((DataAttribute)object, parentObject);
        }
        else
        {
            return new ArrayList<>(loadDataAttributes(object));
        }
    }


    private Collection<DataAttribute> loadDataAttributes(final Object object)
    {
        final String typeCode = typeFacade.getType(object);
        try
        {
            final DataType type = typeFacade.load(typeCode);
            return type.getAttributes();
        }
        catch(final TypeNotFoundException e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.warn("Couldn't load type for typecode ".concat(typeCode), e);
            }
            else
            {
                LOG.warn("Couldn't load type for typecode ".concat(typeCode));
            }
            return Collections.emptyList();
        }
    }


    private List<Object> getDataAttributeValue(final DataAttribute attribute, final Object parentObject)
    {
        final Object value = objectValueService.getValue(attribute.getQualifier(), parentObject);
        return getValueAsList(value);
    }


    private List<Object> getValueAsList(final Object value)
    {
        final List<Object> resultList = new ArrayList<>();
        if(value instanceof Collection)
        {
            resultList.addAll((Collection)value);
        }
        else if(value != null)
        {
            resultList.add(value);
        }
        return resultList;
    }


    /**
     * @return configuration for given tree node. If there is no explicit configuration, fallback configuration is returned
     */
    public TreeCollectionNodes loadTreeConfiguration(final AbstractTreeViewNode node)
    {
        if(node.getParent() == null)
        {
            final String type = typeFacade.getType(node.getData());
            return loadTreeConfiguration(type, DefaultTreeCollectionContextStrategy.ROOT_CONTEXT);
        }
        AbstractTreeViewNode currentNode = node;
        final StringBuilder fullPath = new StringBuilder();
        while(currentNode.getParent() != null)
        {
            currentNode = currentNode.getParent();
            if(isObjectInstanceOfDataAttribute(currentNode.getData()))
            {
                fullPath.insert(0, ((DataAttribute)currentNode.getData()).getQualifier() + ".");
            }
            else if(currentNode.isSkipOnlyAttributes())
            {
                fullPath.insert(0, currentNode.getSkippedDataAttribute().getQualifier() + ".");
            }
        }
        if(StringUtils.isNotBlank(fullPath) && fullPath.charAt(fullPath.length() - 1) == '.')
        {
            fullPath.deleteCharAt(fullPath.length() - 1);
        }
        final String rootType = typeFacade.getType(currentNode.getData());
        final String type = typeFacade.getType(node.getData());
        return loadTreeConfiguration(type, String.format("%s.%s", rootType, fullPath.toString()));
    }


    TreeCollectionNodes loadTreeConfiguration(final String type, final String treeContext)
    {
        final Object setting = widgetInstanceManager.getWidgetSettings().getOrDefault(SETTING_CONFIG_COMPONENT,
                        COLLECTION_BROWSER_COMPONENT);
        final DefaultConfigContext context = new DefaultConfigContext(ObjectUtils.toString(setting), type);
        context.addAttribute(TREE_CONTEXT_KEY, treeContext);
        try
        {
            return widgetInstanceManager.loadConfiguration(context, TreeCollectionNodes.class);
        }
        catch(final CockpitConfigurationException exception)
        {
            LOG.error("Cannot load cockpit configuration for TreeCollection", exception);
            throw new IllegalStateException(exception);
        }
    }


    public boolean hasReadAccess(final Object reference, final Object data)
    {
        if(isObjectInstanceOfDataAttribute(reference))
        {
            return permissionFacade.canReadInstanceProperty(data, ((DataAttribute)reference).getQualifier());
        }
        else
        {
            return permissionFacade.canReadInstance(reference);
        }
    }


    public boolean isNodeSelectable(final AbstractTreeViewNode node)
    {
        return node.isTopLevel() && !isObjectInstanceOfDataAttribute(node.getData());
    }


    public boolean isObjectInstanceOfDataAttribute(final Object object)
    {
        return object instanceof DataAttribute;
    }
}
