package de.hybris.platform.cockpit.wizards.script;

import bsh.EvalError;
import bsh.Interpreter;
import bsh.TargetError;
import de.hybris.platform.cockpit.wizards.exception.WizardConfirmationException;
import de.hybris.platform.core.BeanShellUtils;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;

public class WizardBeanShellScriptStrategy implements WizardScriptStrategy
{
    private static final Logger LOG = LoggerFactory.getLogger(WizardBeanShellScriptStrategy.class.getName());


    public void evaluateScript(String scriptContent, Map<String, Object> context)
    {
        Interpreter interpreter = BeanShellUtils.createInterpreter();
        try
        {
            interpreter.set("context", context);
            Object returnedObject = interpreter.eval(scriptContent);
            LOG.info("{}", returnedObject);
        }
        catch(TargetError e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Executing bean shell script <<" + scriptContent + ">> failed, bean shell exception on runtime ", (Throwable)e);
            }
            WizardConfirmationException wizardException = new WizardConfirmationException((Throwable)e);
            wizardException.setFrontendeLocalizedMessage(Labels.getLabel("wizard.evaluation.scrpit.error"));
            throw wizardException;
        }
        catch(EvalError e)
        {
            LOG.info("Executing bean shell script <<" + scriptContent + ">> failed, bean shell exception on runtime ", (Throwable)e);
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Executing bean shell script <<" + scriptContent + ">> failed, bean shell exception on runtime ", (Throwable)e);
            }
            WizardConfirmationException wizardException = new WizardConfirmationException((Throwable)e);
            wizardException.setFrontendeLocalizedMessage(Labels.getLabel("wizard.evaluation.scrpit.error"));
            throw wizardException;
        }
    }
}
