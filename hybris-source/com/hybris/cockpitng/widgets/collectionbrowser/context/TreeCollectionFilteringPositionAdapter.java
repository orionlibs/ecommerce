/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.collectionbrowser.context;

import com.hybris.cockpitng.core.config.CockpitConfigurationAdapter;
import com.hybris.cockpitng.core.config.CockpitConfigurationException;
import com.hybris.cockpitng.core.config.ConfigContext;
import com.hybris.cockpitng.core.config.impl.DefaultConfigContext;
import com.hybris.cockpitng.core.config.impl.adapters.flow.PositionAware;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.commonconfig.Positioned;
import com.hybris.cockpitng.core.config.impl.jaxb.treecollection.TreeCollectionNodes;
import com.hybris.cockpitng.core.config.impl.jaxb.treecollection.TreeNode;
import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.dataaccess.facades.type.exceptions.TypeNotFoundException;
import com.hybris.cockpitng.dataaccess.services.PositionedSort;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

/**
 * Configuration adapter for TreeContext. It is responsible for proper order of nodes and for filtering out
 * non-reference nodes
 */
public class TreeCollectionFilteringPositionAdapter implements CockpitConfigurationAdapter<TreeCollectionNodes>
{
    private TypeFacade typeFacade;
    private PositionedSort<Positioned> positionedSort;
    private static final Logger LOG = LoggerFactory.getLogger(TreeCollectionFilteringPositionAdapter.class);


    @Required
    public void setTypeFacade(final TypeFacade typeFacade)
    {
        this.typeFacade = typeFacade;
    }


    @Required
    public void setPositionedSort(final PositionedSort<Positioned> positionedSort)
    {
        this.positionedSort = positionedSort;
    }


    @Override
    public Class<TreeCollectionNodes> getSupportedType()
    {
        return TreeCollectionNodes.class;
    }


    @Override
    public TreeCollectionNodes adaptAfterLoad(final ConfigContext context, final TreeCollectionNodes treeConfig)
                    throws CockpitConfigurationException
    {
        final TreeCollectionNodes result = filter(context, treeConfig);
        sort(result);
        return result;
    }


    protected TreeCollectionNodes filter(final ConfigContext context, final TreeCollectionNodes treeConfig)
    {
        final DataType dataType = getDataType(context);
        if(dataType == null)
        {
            return treeConfig;
        }
        return filterTreeCollectionNodes(treeConfig, dataType);
    }


    protected DataType getDataType(final ConfigContext context)
    {
        DataType dataType = null;
        final String type = context.getAttribute(DefaultConfigContext.CONTEXT_TYPE);
        if(StringUtils.isNotBlank(type))
        {
            try
            {
                dataType = typeFacade.load(type);
            }
            catch(final TypeNotFoundException e)
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug(String.format("Type %s not found", type), e);
                }
                else
                {
                    LOG.warn(String.format("Type %s not found", type));
                }
            }
        }
        return dataType;
    }


    private TreeCollectionNodes filterTreeCollectionNodes(final TreeCollectionNodes treeConfig, final DataType dataType)
    {
        final TreeCollectionNodes result = new TreeCollectionNodes();
        result.setSkipOnlyAttribute(treeConfig.isSkipOnlyAttribute());
        for(final TreeNode treeNode : treeConfig.getNode())
        {
            final DataAttribute dataAttribute = dataType.getAttribute(treeNode.getAttribute());
            if(dataAttribute != null && dataAttribute.getValueType() != null && !dataAttribute.getValueType().isAtomic()
                            && !dataAttribute.getValueType().isEnum())
            {
                result.getNode().add(treeNode);
            }
        }
        return result;
    }


    protected void sort(final TreeCollectionNodes treeConfig)
    {
        if(CollectionUtils.isNotEmpty(treeConfig.getNode()))
        {
            final List<Positioned> listToSort = new ArrayList<>();
            for(final TreeNode node : treeConfig.getNode())
            {
                listToSort.add(createPositionedObject(node));
            }
            positionedSort.sort(listToSort);
            treeConfig.getNode().clear();
            for(final Positioned positioned : listToSort)
            {
                final PositionAware<TreeNode> positionAware = (PositionAware<TreeNode>)positioned;
                treeConfig.getNode().add(positionAware.getObject());
            }
        }
    }


    protected PositionAware<TreeNode> createPositionedObject(final TreeNode node)
    {
        return new PositionAware<>(node, node.getPosition());
    }


    @Override
    public TreeCollectionNodes adaptBeforeStore(final ConfigContext context, final TreeCollectionNodes treeConfig)
                    throws CockpitConfigurationException
    {
        return treeConfig;
    }
}
