package de.hybris.platform.configurablebundlebackoffice.widgets.populator;

import com.hybris.backoffice.navigation.NavigationNode;
import com.hybris.cockpitng.core.context.CockpitContext;
import com.hybris.cockpitng.core.context.impl.DefaultCockpitContext;
import com.hybris.cockpitng.labels.LabelService;
import com.hybris.cockpitng.tree.node.DynamicNode;
import com.hybris.cockpitng.tree.node.DynamicNodePopulator;
import de.hybris.platform.configurablebundleservices.model.BundleTemplateModel;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.assertj.core.util.Lists;
import org.assertj.core.util.Objects;
import org.zkoss.zul.DefaultTreeNode;
import org.zkoss.zul.TreeNode;

public class BundleTemplatesTreeModelPopulator implements DynamicNodePopulator
{
    private static final String DYNAMIC_NODE_SELECTION_CONTEXT_PARAMETER = "dynamicNodeSelectionContext";
    private LabelService labelService;
    private ModelService modelService;


    public List<NavigationNode> getChildren(NavigationNode node)
    {
        if(!(node instanceof DynamicNode))
        {
            throw new IllegalArgumentException("Only Dynamic Nodes are supported");
        }
        return findChildrenNavigationNodes((DynamicNode)node);
    }


    protected List<NavigationNode> findChildrenNavigationNodes(DynamicNode node)
    {
        if(node.isRootNode())
        {
            Iterator<Object> selectionContextIterator = getSelectionContext((NavigationNode)node).iterator();
            if(selectionContextIterator.hasNext())
            {
                Object nodeData = selectionContextIterator.next();
                if(nodeData instanceof BundleTemplateModel)
                {
                    refreshNodeData(nodeData);
                    return prepareNavigationNodes(node, (BundleTemplateModel)nodeData);
                }
            }
        }
        else if(node.getData() instanceof BundleTemplateModel)
        {
            return prepareNavigationNodes(node, (BundleTemplateModel)node.getData());
        }
        return Collections.emptyList();
    }


    protected void refreshNodeData(Object nodeData)
    {
        if(!getModelService().isNew(nodeData))
        {
            getModelService().refresh(nodeData);
        }
    }


    protected Collection<Object> getSelectionContext(NavigationNode node)
    {
        Collection<Object> selectionContext = (Collection<Object>)Objects.castIfBelongsToType(node
                        .getContext().getParameter("dynamicNodeSelectionContext"), Collection.class);
        return CollectionUtils.emptyIfNull(selectionContext);
    }


    protected List<NavigationNode> prepareNavigationNodes(DynamicNode node, BundleTemplateModel bundleTemplate)
    {
        List<BundleTemplateModel> bundleTemplates = node.isRootNode() ? Lists.newArrayList((Object[])new BundleTemplateModel[] {bundleTemplate}) : bundleTemplate.getChildTemplates();
        return (List<NavigationNode>)bundleTemplates.stream().map(c -> new DefaultTreeNode(c)).<DynamicNode>map(getRegularNodeCreatorFunction(node))
                        .collect(Collectors.toList());
    }


    protected Function<TreeNode<BundleTemplateModel>, DynamicNode> getRegularNodeCreatorFunction(DynamicNode node)
    {
        return treeNode -> {
            String label = getLabelService().getObjectLabel(treeNode.getData());
            return createDynamicNode(node, treeNode, label);
        };
    }


    protected DynamicNode createDynamicNode(DynamicNode node, TreeNode<BundleTemplateModel> treeNode, String label)
    {
        int index = node.getIndexingDepth() - 1;
        DynamicNode dynamicNode = new DynamicNode(createDynamicNodeId(node, label), this, index);
        dynamicNode.setData(treeNode.getData());
        dynamicNode.setName(label);
        dynamicNode.setContext(createCockpitContext((NavigationNode)node));
        dynamicNode.setSelectable(true);
        dynamicNode.setExpandedByDefault(true);
        return dynamicNode;
    }


    protected String createDynamicNodeId(DynamicNode node, String postFix)
    {
        String prefix = node.isRootNode() ? node.getId() : createParentNodesIdPrefix(node);
        return prefix + prefix;
    }


    protected String createParentNodesIdPrefix(DynamicNode node)
    {
        StringBuilder prefix = new StringBuilder();
        for(DynamicNode dynamicNode = node; dynamicNode != null; navigationNode = dynamicNode.getParent())
        {
            NavigationNode navigationNode;
            String id = dynamicNode.getId();
            if(id != null && !isParentIdAppended(id + "_", prefix))
            {
                prefix.insert(0, '_');
                prefix.insert(0, id.toLowerCase(Locale.ROOT));
            }
        }
        return prefix.toString();
    }


    protected boolean isParentIdAppended(String parentId, StringBuilder childId)
    {
        return (childId.length() >= parentId.length() && parentId.equals(childId.substring(0, parentId.length())));
    }


    protected CockpitContext createCockpitContext(NavigationNode node)
    {
        DefaultCockpitContext defaultCockpitContext = new DefaultCockpitContext();
        defaultCockpitContext.addAllParameters(node.getContext());
        return (CockpitContext)defaultCockpitContext;
    }


    protected LabelService getLabelService()
    {
        return this.labelService;
    }


    public void setLabelService(LabelService labelService)
    {
        this.labelService = labelService;
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }
}
