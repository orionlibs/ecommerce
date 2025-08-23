package de.hybris.platform.cockpit.model.listview.impl;

import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.cockpit.model.gridview.impl.GridValueHolder;
import de.hybris.platform.cockpit.model.listview.ValueHandler;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.config.GridViewConfiguration;
import de.hybris.platform.cockpit.services.config.UIConfigurationService;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.services.values.ValueHandlerException;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.workflow.WorkflowService;
import de.hybris.platform.workflow.WorkflowTemplateService;
import de.hybris.platform.workflow.enums.WorkflowActionStatus;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowItemAttachmentModel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import org.zkoss.spring.SpringUtil;

public class WorkflowValueHandler implements ValueHandler
{
    public static final String COLUMN_WORKFLOW = "Workflow";
    public static final String COLUMN_STEP = "Current step";
    public static final String COLUMN_START = "Start";
    public static final String COLUMN_ITEM_TYPE = "ItemType";
    public static final String COLUMN_CODE = "Code";
    public static final String COLUMN_NAME = "Name";
    public static final String COLUMN_ASSIGNED_USER = "Assigned user";
    public static final String COLUMN_SINCE = "Since";
    public static final String COLUMN_ACTIONS = "Actions";
    public static final String WORKFLOW_NEWEST_SERVICE_BEAN_ID = "newestWorkflowService";
    public static final String WORKFLOW_ACTION_SERVICE_BEAN_ID = "workflowActionService";
    public static final String WORKFLOW_TEMPLATE_SERVICE_BEAN_ID = "workflowTemplateService";
    private final UIConfigurationService uiConfService = (UIConfigurationService)SpringUtil.getBean("uiConfigurationService");
    private final TypeService typeService = UISessionUtils.getCurrentSession().getTypeService();
    private WorkflowService workflowService;
    private WorkflowTemplateService workflowTemplateService;
    private final String qualifier;


    public WorkflowValueHandler(String qualifier)
    {
        this.qualifier = qualifier;
    }


    public Object getValue(TypedObject item) throws ValueHandlerException
    {
        return getValue(item, UISessionUtils.getCurrentSession().getLanguageIso());
    }


    public Object getValue(TypedObject item, String languageIso) throws ValueHandlerException
    {
        Locale locale = new Locale(languageIso);
        GridViewConfiguration gridViewConfig = (GridViewConfiguration)this.uiConfService.getComponentConfiguration(this.typeService.getBestTemplate(item), "gridView", GridViewConfiguration.class);
        GridValueHolder gridValueHolder = new GridValueHolder(gridViewConfig, item);
        Object obj = item.getObject();
        if(obj instanceof WorkflowItemAttachmentModel)
        {
            if("Code".equalsIgnoreCase(this.qualifier))
            {
                ItemModel itemModel = ((WorkflowItemAttachmentModel)obj).getItem();
                if(itemModel instanceof ProductModel)
                {
                    return ((ProductModel)itemModel).getCode();
                }
                return gridValueHolder.getLabel();
            }
            if("Start".equalsIgnoreCase(this.qualifier))
            {
                Date startTime = getWorkflowService().getStartTime(((WorkflowItemAttachmentModel)obj).getWorkflow());
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                return (startTime == null) ? "" : dateFormat.format(startTime);
            }
            if("Current step".equalsIgnoreCase(this.qualifier))
            {
                String step = "";
                List<WorkflowActionModel> actions = ((WorkflowItemAttachmentModel)obj).getWorkflow().getActions();
                for(WorkflowActionModel workflowActionModel : actions)
                {
                    if(workflowActionModel.getStatus().equals(WorkflowActionStatus.IN_PROGRESS) && workflowActionModel
                                    .getAttachments().contains(obj))
                    {
                        step = step + step + ", ";
                    }
                }
                if(step.endsWith(", "))
                {
                    step = step.substring(0, step.length() - 2);
                }
                return step.isEmpty() ? "-" : step;
            }
            if("Workflow".equalsIgnoreCase(this.qualifier))
            {
                return ((WorkflowItemAttachmentModel)obj).getWorkflow().getName(locale);
            }
            if("ItemType".equalsIgnoreCase(this.qualifier))
            {
                return ((WorkflowItemAttachmentModel)obj).getItem().getItemtype();
            }
            if("Name".equalsIgnoreCase(this.qualifier))
            {
                ItemModel itemModel = ((WorkflowItemAttachmentModel)obj).getItem();
                if(itemModel instanceof ProductModel)
                {
                    return ((ProductModel)itemModel).getName(locale);
                }
                if(itemModel instanceof CatalogModel)
                {
                    return ((CatalogModel)itemModel).getName(locale);
                }
                return gridValueHolder.getLabel();
            }
            if("Assigned user".equalsIgnoreCase(this.qualifier))
            {
                String assignedUserName = "";
                List<WorkflowActionModel> actions = ((WorkflowItemAttachmentModel)obj).getWorkflow().getActions();
                EmployeeModel adhocDummyOwner = getWorkflowTemplateService().getAdhocWorkflowTemplateDummyOwner();
                Set<PrincipalModel> assignedUsers = new HashSet<>();
                for(WorkflowActionModel workflowActionModel : actions)
                {
                    if(workflowActionModel.getStatus().equals(WorkflowActionStatus.IN_PROGRESS) && workflowActionModel
                                    .getAttachments().contains(obj))
                    {
                        PrincipalModel assignedUser = workflowActionModel.getPrincipalAssigned();
                        if(!adhocDummyOwner.equals(assignedUser))
                        {
                            if(assignedUsers.add(assignedUser))
                            {
                                assignedUserName = assignedUserName + assignedUserName + ", ";
                            }
                        }
                    }
                }
                if(assignedUserName.endsWith(", "))
                {
                    assignedUserName = assignedUserName.substring(0, assignedUserName.length() - 2);
                }
                return assignedUserName.isEmpty() ? "-" : assignedUserName;
            }
            if("Since".equalsIgnoreCase(this.qualifier))
            {
                Date since = null;
                List<WorkflowActionModel> actions = ((WorkflowItemAttachmentModel)obj).getWorkflow().getActions();
                for(WorkflowActionModel workflowActionModel : actions)
                {
                    if(workflowActionModel.getStatus().equals(WorkflowActionStatus.IN_PROGRESS) && workflowActionModel
                                    .getAttachments().contains(obj))
                    {
                        since = workflowActionModel.getFirstActivated();
                        break;
                    }
                }
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                return (since == null) ? "-" : dateFormat.format(since);
            }
        }
        return "";
    }


    private WorkflowService getWorkflowService()
    {
        if(this.workflowService == null)
        {
            this.workflowService = (WorkflowService)Registry.getApplicationContext().getBean("newestWorkflowService");
        }
        return this.workflowService;
    }


    private WorkflowTemplateService getWorkflowTemplateService()
    {
        if(this.workflowTemplateService == null)
        {
            this.workflowTemplateService = (WorkflowTemplateService)Registry.getApplicationContext().getBean("workflowTemplateService");
        }
        return this.workflowTemplateService;
    }


    public void setValue(TypedObject item, Object value) throws ValueHandlerException
    {
    }


    public void setValue(TypedObject item, Object value, String languageIso) throws ValueHandlerException
    {
    }
}
