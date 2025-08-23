package de.hybris.platform.cmscockpit.services.workflow.impl;

import bsh.Interpreter;
import de.hybris.platform.workflow.services.impl.WorkflowServiceImpl;

public class CmsWorkflowServiceImpl extends WorkflowServiceImpl
{
    private Interpreter shellInterpreter = null;


    protected Interpreter getInterpreter()
    {
        if(this.shellInterpreter == null)
        {
            this.shellInterpreter = super.getInterpreter();
            this.shellInterpreter.getNameSpace().importPackage("de.hybris.platform.cms2.jalo.pages");
        }
        return this.shellInterpreter;
    }
}
