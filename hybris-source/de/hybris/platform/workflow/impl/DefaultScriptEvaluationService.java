package de.hybris.platform.workflow.impl;

import bsh.EvalError;
import bsh.Interpreter;
import de.hybris.platform.core.BeanShellUtils;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.workflow.ScriptEvaluationService;
import de.hybris.platform.workflow.WorkflowProcessingService;
import de.hybris.platform.workflow.WorkflowService;
import de.hybris.platform.workflow.WorkflowTemplateService;
import de.hybris.platform.workflow.model.WorkflowModel;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

public class DefaultScriptEvaluationService implements ScriptEvaluationService
{
    private static final Logger LOG = Logger.getLogger(DefaultScriptEvaluationService.class);
    private WorkflowService workflowService;
    private WorkflowTemplateService workflowTemplateService;
    private WorkflowProcessingService workflowProcessingService;
    private TypeService typeService;
    private UserService userService;
    private ModelService modelService;


    public WorkflowModel evaluateActivationScripts(ItemModel itemModel, Map currentValues, Map initialValues, String action) throws EvalError
    {
        if(itemModel == null)
        {
            return null;
        }
        Interpreter interpreter = getInterpreter();
        synchronized(interpreter)
        {
            interpreter.set("action", action);
            interpreter.set("item", itemModel);
            interpreter.set("itemType", this.typeService.getComposedTypeForClass(itemModel.getClass()));
            interpreter.set("initialValues", initialValues);
            interpreter.set("currentValues", currentValues);
            interpreter.set("typeService", this.typeService);
            List<WorkflowModel> activatedWorkflows = new ArrayList<>();
            for(WorkflowTemplateModel wft : this.workflowTemplateService.getAllWorkflowTemplates())
            {
                String activationScript = wft.getActivationScript();
                if(activationScript != null)
                {
                    try
                    {
                        Object activate = interpreter.eval(activationScript);
                        if(activate instanceof Boolean && ((Boolean)activate).booleanValue())
                        {
                            WorkflowModel workflow = this.workflowService.createWorkflow(wft, itemModel, this.userService
                                            .getCurrentUser());
                            this.modelService.saveAll(workflow.getActions());
                            this.modelService.save(workflow);
                            this.workflowProcessingService.toggleActions(workflow);
                            activatedWorkflows.add(workflow);
                        }
                    }
                    catch(EvalError e)
                    {
                        LOG.error(e);
                        throw e;
                    }
                }
            }
            if(!activatedWorkflows.isEmpty())
            {
                return activatedWorkflows.get(0);
            }
        }
        return null;
    }


    private Interpreter shellInterpreter = null;


    Interpreter getInterpreter()
    {
        if(this.shellInterpreter == null)
        {
            this.shellInterpreter = BeanShellUtils.createInterpreter();
            this.shellInterpreter.getNameSpace().importPackage("de.hybris.platform.core.model.type");
            this.shellInterpreter.getNameSpace().importPackage("de.hybris.platform.core.model.product");
            this.shellInterpreter.getNameSpace().importPackage("de.hybris.platform.catalog.enums");
            this.shellInterpreter.getNameSpace().importPackage("de.hybris.platform.jalo.type");
            this.shellInterpreter.getNameSpace().importPackage("de.hybris.platform.jalo.product");
        }
        return this.shellInterpreter;
    }


    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }


    public void setWorkflowService(WorkflowService workflowService)
    {
        this.workflowService = workflowService;
    }


    public void setWorkflowTemplateService(WorkflowTemplateService workflowTemplateService)
    {
        this.workflowTemplateService = workflowTemplateService;
    }


    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }


    public void setWorkflowProcessingService(WorkflowProcessingService workflowProcessingService)
    {
        this.workflowProcessingService = workflowProcessingService;
    }


    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }
}
