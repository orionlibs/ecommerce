package de.hybris.platform.cockpit.components.navigationarea.model;

import de.hybris.platform.cockpit.components.navigationarea.renderer.WorkflowSectionRenderer;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.workflow.constants.GeneratedWorkflowConstants;
import java.util.List;
import org.zkoss.zul.AbstractTreeModel;

public class MyWorkflowsTreeModel extends AbstractTreeModel
{
    private final List<TypedObject> workflows;
    public static final TreeNodeWrapper dummyNode = new TreeNodeWrapper(null, null);


    public MyWorkflowsTreeModel(Object root, List<TypedObject> workflows)
    {
        super(root);
        this.workflows = workflows;
    }


    public Object getChild(Object parent, int index)
    {
        TypedObject ret = null;
        TreeNodeWrapper parentNode = (TreeNodeWrapper)parent;
        if(parentNode.getParent() == null)
        {
            ret = (this.workflows != null && !this.workflows.isEmpty() && this.workflows.size() - 1 >= index) ? this.workflows.get(index) : null;
        }
        return new TreeNodeWrapper(ret, parentNode);
    }


    public int getChildCount(Object parent)
    {
        int ret = 0;
        if(WorkflowSectionRenderer.rootDummy.equals(parent))
        {
            if(this.workflows != null)
            {
                ret = Math.max(1, this.workflows.size());
            }
        }
        return ret;
    }


    public boolean isLeaf(Object node)
    {
        if(!(node instanceof TreeNodeWrapper))
        {
            throw new IllegalArgumentException(String.format("Unexpected type of node: %s. %s expected", new Object[] {node.getClass(), TreeNodeWrapper.class}));
        }
        TreeNodeWrapper treenode = (TreeNodeWrapper)node;
        if(!treenode.hasLeafInformation())
        {
            boolean leaf = true;
            TypedObject data = treenode.getItem();
            if(data == null || GeneratedWorkflowConstants.TC.WORKFLOW
                            .equals(data.getType().getCode()) || GeneratedWorkflowConstants.TC.WORKFLOWACTION
                            .equals(data.getType().getCode()))
            {
                leaf = true;
            }
            else
            {
                throw new IllegalStateException(String.format("Unexpected type of node data: %s. %s or %s expected", new Object[] {data.getType()
                                .getCode(), GeneratedWorkflowConstants.TC.WORKFLOWACTION, GeneratedWorkflowConstants.TC.WORKFLOW}));
            }
            treenode.setLeaf(leaf);
        }
        return treenode.isLeaf();
    }
}
