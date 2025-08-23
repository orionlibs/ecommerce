package de.hybris.platform.cockpit.components.navigationarea.model;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.core.Registry;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.workflow.WorkflowTemplateService;
import de.hybris.platform.workflow.constants.GeneratedWorkflowConstants;
import de.hybris.platform.workflow.model.WorkflowActionTemplateModel;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;
import java.util.List;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.AbstractTreeModel;

public class WorkflowTemplateTreeModel extends AbstractTreeModel
{
    private TypeService typeService;
    private final List<TypedObject> workflowTemplates;
    private WorkflowTemplateService workflowTemplateService;
    private UserService userService;


    public WorkflowTemplateTreeModel(Object root)
    {
        super(root);
        this.workflowTemplates = getTypeService().wrapItems(
                        getWorkflowTemplateService().getAllVisibleWorkflowTemplatesForUser(getUserService().getCurrentUser()));
    }


    public Object getChild(Object parent, int index)
    {
        TypedObject ret = null;
        TreeNodeWrapper parentNode = (TreeNodeWrapper)parent;
        TypedObject item = parentNode.getItem();
        if(parentNode.getParent() == null)
        {
            ret = (this.workflowTemplates != null && !this.workflowTemplates.isEmpty() && this.workflowTemplates.size() - 1 >= index) ? this.workflowTemplates.get(index) : null;
        }
        else
        {
            Object object = item.getObject();
            if(object instanceof WorkflowTemplateModel)
            {
                WorkflowTemplateModel workflowTemplateModel = (WorkflowTemplateModel)object;
                if(workflowTemplateModel.getActions() != null && !workflowTemplateModel.getActions().isEmpty() && workflowTemplateModel
                                .getActions().size() - 1 >= index)
                {
                    ret = UISessionUtils.getCurrentSession().getTypeService().wrapItem(workflowTemplateModel.getActions().get(index));
                }
            }
        }
        return new TreeNodeWrapper(ret, parentNode);
    }


    public int getChildCount(Object parent)
    {
        int ret = 0;
        TreeNodeWrapper parentNode = (TreeNodeWrapper)parent;
        TypedObject item = parentNode.getItem();
        if(parentNode.getParent() == null)
        {
            ret = (this.workflowTemplates == null) ? 0 : this.workflowTemplates.size();
        }
        else
        {
            Object object = item.getObject();
            if(object instanceof WorkflowTemplateModel)
            {
                WorkflowTemplateModel workflowTemplateModel = (WorkflowTemplateModel)object;
                if(workflowTemplateModel.getActions() != null)
                {
                    ret = workflowTemplateModel.getActions().size();
                }
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
            if(data == null)
            {
                throw new IllegalStateException("Node data unexpectedly null");
            }
            Object object = data.getObject();
            if(object instanceof WorkflowTemplateModel)
            {
                List<WorkflowActionTemplateModel> actions = ((WorkflowTemplateModel)object).getActions();
                leaf = (actions != null && actions.isEmpty());
            }
            else if(object instanceof WorkflowActionTemplateModel)
            {
                leaf = true;
            }
            else
            {
                throw new IllegalStateException(String.format("Unexpected type of node data: %s. %s or %s expected", new Object[] {data.getType()
                                .getCode(), GeneratedWorkflowConstants.TC.WORKFLOWACTIONTEMPLATE, GeneratedWorkflowConstants.TC.WORKFLOWTEMPLATE}));
            }
            treenode.setLeaf(leaf);
        }
        return treenode.isLeaf();
    }


    public WorkflowTemplateService getWorkflowTemplateService()
    {
        if(this.workflowTemplateService == null)
        {
            this.workflowTemplateService = (WorkflowTemplateService)Registry.getApplicationContext().getBean("workflowTemplateService");
        }
        return this.workflowTemplateService;
    }


    private TypeService getTypeService()
    {
        if(this.typeService == null)
        {
            this.typeService = (TypeService)SpringUtil.getBean("cockpitTypeService");
        }
        return this.typeService;
    }


    private UserService getUserService()
    {
        if(this.userService == null)
        {
            this.userService = (UserService)SpringUtil.getBean("userService");
        }
        return this.userService;
    }
}
