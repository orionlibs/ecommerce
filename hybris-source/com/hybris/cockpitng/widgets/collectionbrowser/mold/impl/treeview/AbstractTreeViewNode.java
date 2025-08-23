/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.collectionbrowser.mold.impl.treeview;

import com.google.common.collect.Lists;
import com.hybris.cockpitng.core.config.impl.jaxb.treecollection.TreeCollectionNodes;
import com.hybris.cockpitng.core.config.impl.jaxb.treecollection.TreeNode;
import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.collections.CollectionUtils;

/**
 * Abstract tree node class for Collection Browser widget. Parametrized with type of parent/children nodes.
 */
public abstract class AbstractTreeViewNode<NODE extends AbstractTreeViewNode<?>> implements Serializable
{
    public static final int TOP_LEVEL = 0;
    protected static final int LEAF_CHILDREN_COUNT = 0;
    private final NODE parent;
    private final transient Object data;
    private final int level;
    private final boolean accessible;
    private final transient TreeViewCollectionBrowserHelper helper;
    protected transient List<NODE> children;
    private boolean skipOnlyAttributes = false;
    private transient DataAttribute skippedDataAttribute;


    /**
     * @param helper
     *           helper class to be used for data extraction; cannot be <code>null</code>
     * @param data
     *           underlying object of a tree node; cannot be <code>null</code>
     * @param accessible
     *           if user has access to the underlying object
     */
    public AbstractTreeViewNode(final TreeViewCollectionBrowserHelper helper, final Object data, final boolean accessible)
    {
        this(helper, data, accessible, null);
    }


    /**
     * @param helper
     *           helper class to be used for data extraction; cannot be <code>null</code>
     * @param data
     *           underlying object of a tree node; cannot be <code>null</code>
     * @param accessible
     *           if user has access to the underlying object
     * @param parent
     *           parent tree node; <code>null</code> means it's a top-level node
     */
    public AbstractTreeViewNode(final TreeViewCollectionBrowserHelper helper, final Object data, final boolean accessible,
                    final NODE parent)
    {
        this(helper, data, accessible, parent, null);
    }


    /**
     * @param helper
     *           helper class to be used for data extraction; cannot be <code>null</code>
     * @param data
     *           underlying object of a tree node; cannot be <code>null</code>
     * @param accessible
     *           if user has access to the underlying object
     * @param parent
     *           parent tree node; <code>null</code> means it's a top-level node
     * @param children
     *           list of children nodes; <code>null</code> will cause load on demand
     */
    public AbstractTreeViewNode(final TreeViewCollectionBrowserHelper helper, final Object data, final boolean accessible,
                    final NODE parent, final List<NODE> children)
    {
        this.helper = helper;
        this.data = data;
        this.parent = parent;
        this.children = children;
        if(parent == null)
        {
            level = TOP_LEVEL;
        }
        else
        {
            level = parent.getLevel() + 1;
        }
        this.accessible = accessible;
    }


    /**
     * @return tree node's underlying data object
     */
    public <T> T getData()
    {
        return (T)data;
    }


    /**
     * @return tree node's parent or <code>null</code> if it's a top-level node
     */
    public NODE getParent()
    {
        return parent;
    }


    /**
     * This method implements the load-on-demand mechanism, as it dynamically loads and caches the children when necessary
     *
     * @return tree node's children or an empty list if there is none
     */
    public List<NODE> getChildren()
    {
        if(children == null)
        {
            loadChildren();
        }
        return children;
    }


    /**
     * @return tree node's children from cache; empty list if the cache wasn't initialized yet or if there are no children
     */
    public List<NODE> getCachedChildren()
    {
        return children != null ? children : Lists.newArrayListWithCapacity(0);
    }


    /**
     * Load children of this tree node if user has read access to its underlying object
     */
    protected void loadChildren()
    {
        children = new ArrayList<>();
        if(accessible)
        {
            List<Object> references = helper.getAttributesOrValues(data, parent != null ? parent.getData() : null);
            references = loadAndApplyConfiguration(references);
            if(references.size() == 1 && helper.isObjectInstanceOfDataAttribute(references.get(0)))
            {
                final TreeCollectionNodes config = helper.loadTreeConfiguration(this);
                if(config.isSkipOnlyAttribute())
                {
                    final DataAttribute dataAttribute = (DataAttribute)references.get(0);
                    references = helper.getAttributesOrValues(dataAttribute, data);
                    setSkipOnlyAttributes(true);
                    setSkippedDataAttribute(dataAttribute);
                }
            }
            for(final Object object : references)
            {
                children.addAll(createChildren(helper, object));
            }
        }
    }


    List<Object> loadAndApplyConfiguration(final List<Object> dataAttributes)
    {
        List<Object> result = dataAttributes;
        if(!helper.isObjectInstanceOfDataAttribute(this.data) && CollectionUtils.isNotEmpty(dataAttributes))
        {
            final TreeCollectionNodes configuration = helper.loadTreeConfiguration(this);
            result = applyConfiguration(dataAttributes, configuration);
        }
        return result;
    }


    protected List<Object> applyConfiguration(final List<Object> dataAttributes, final TreeCollectionNodes configuration)
    {
        final Map<String, DataAttribute> attrNameToDataAttributeMap = new HashMap<>();
        for(final Object dataAttribute : dataAttributes)
        {
            if(helper.isObjectInstanceOfDataAttribute(dataAttribute))
            {
                final DataAttribute attribute = (DataAttribute)dataAttribute;
                attrNameToDataAttributeMap.put(attribute.getQualifier(), attribute);
            }
        }
        final List<Object> result = new ArrayList<>();
        for(final TreeNode treeNode : configuration.getNode())
        {
            if(attrNameToDataAttributeMap.containsKey(treeNode.getAttribute()))
            {
                result.add(attrNameToDataAttributeMap.get(treeNode.getAttribute()));
            }
        }
        return result;
    }


    protected List<NODE> createChildren(final TreeViewCollectionBrowserHelper treeViewHelper, final Object dataObject)
    {
        if(dataObject instanceof Map)
        {
            return createChildrenFromMap(treeViewHelper, dataObject);
        }
        else if(dataObject instanceof Collection)
        {
            return createChildrenFromCollection(treeViewHelper, dataObject);
        }
        else
        {
            return Collections.singletonList(checkPermissionsAndCreateChild(treeViewHelper, dataObject, null));
        }
    }


    private List<NODE> createChildrenFromMap(final TreeViewCollectionBrowserHelper treeViewHelper, final Object dataObject)
    {
        final List<NODE> childrenFromMap = new ArrayList<>();
        for(final Entry<?, ?> mapEntry : ((Map<?, ?>)dataObject).entrySet())
        {
            final List<NODE> childList = new ArrayList<>();
            final var node = checkPermissionsAndCreateChild(treeViewHelper, mapEntry.getKey(), childList);
            childList.addAll((List<NODE>)node.createChildren(treeViewHelper, mapEntry.getValue()));
            childrenFromMap.add(node);
        }
        return childrenFromMap;
    }


    private List<NODE> createChildrenFromCollection(final TreeViewCollectionBrowserHelper treeViewHelper, final Object dataObject)
    {
        final List<NODE> childrenFromCollection = new ArrayList<>();
        for(final Object childDataObject : ((Collection<?>)dataObject))
        {
            childrenFromCollection.addAll(createChildren(treeViewHelper, childDataObject));
        }
        return childrenFromCollection;
    }


    private NODE checkPermissionsAndCreateChild(final TreeViewCollectionBrowserHelper helper, final Object data,
                    final List<NODE> children)
    {
        return createChild(helper, data, helper.hasReadAccess(data, this.data), children);
    }


    /**
     * @return new child node of current node with given underlying data and optional children
     */
    protected abstract NODE createChild(final TreeViewCollectionBrowserHelper helper, final Object data, final boolean accessible,
                    final List<NODE> children);


    /**
     * @return child at given index
     * @throws IndexOutOfBoundsException
     *            if the index is out of range
     */
    public NODE getChildAtIndex(final int index)
    {
        return getChildren().get(index);
    }


    /**
     * @return index of given child or <code>-1</code> if it's not a child of this node
     */
    public int indexOfChild(final NODE child)
    {
        return getChildren().indexOf(child);
    }


    /**
     * @return children count
     */
    public int getChildrenCount()
    {
        return getChildren().size();
    }


    /**
     * @return node's nesting level
     */
    public int getLevel()
    {
        return level;
    }


    /**
     * @return if current user has read access to the underlying data object
     */
    public boolean isAccessible()
    {
        return accessible;
    }


    /**
     * @return if node is a leaf
     */
    public boolean isLeaf()
    {
        return getChildrenCount() == LEAF_CHILDREN_COUNT;
    }


    public boolean isTopLevel()
    {
        return getLevel() == TOP_LEVEL;
    }


    /**
     * @return if node can be selected
     */
    public boolean isSelectable()
    {
        return helper.isNodeSelectable(this);
    }


    /**
     * @return true if the only data attribute should be skipped
     */
    public boolean isSkipOnlyAttributes()
    {
        return skipOnlyAttributes;
    }


    /**
     * @param skipOnlyAttributes
     *           true if the only data attribute should be skipped
     */
    public void setSkipOnlyAttributes(final boolean skipOnlyAttributes)
    {
        this.skipOnlyAttributes = skipOnlyAttributes;
    }


    /**
     * @return data attribute that was skipped or null otherwise
     */
    public DataAttribute getSkippedDataAttribute()
    {
        return skippedDataAttribute;
    }


    /**
     * @param skippedDataAttribute
     *           data attribute that was skipped
     */
    public void setSkippedDataAttribute(final DataAttribute skippedDataAttribute)
    {
        this.skippedDataAttribute = skippedDataAttribute;
    }
}
