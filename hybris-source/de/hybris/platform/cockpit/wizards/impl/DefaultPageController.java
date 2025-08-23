package de.hybris.platform.cockpit.wizards.impl;

import de.hybris.platform.cockpit.wizards.Transition;
import de.hybris.platform.cockpit.wizards.Wizard;
import de.hybris.platform.cockpit.wizards.WizardPage;
import de.hybris.platform.cockpit.wizards.WizardPageController;
import de.hybris.platform.cockpit.wizards.exception.WizardConfirmationException;
import de.hybris.platform.cockpit.wizards.generic.AbstractGenericItemPage;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;

public class DefaultPageController implements WizardPageController
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultPageController.class.getName());
    protected Map<String, List<Transition>> transitions;


    public void setTransitions(List<Transition> transitions)
    {
        this.transitions = new HashMap<>();
        for(Transition transition : transitions)
        {
            List<Transition> transitionList = this.transitions.get(transition.getSource());
            if(transitionList == null)
            {
                transitionList = new ArrayList<>();
                this.transitions.put(transition.getSource(), transitionList);
            }
            transitionList.add(transition);
        }
    }


    public void cancel(Wizard wizard, WizardPage page)
    {
    }


    public void done(Wizard wizard, WizardPage page) throws WizardConfirmationException
    {
    }


    public WizardPage getFirstPage(Wizard wizard)
    {
        List<WizardPage> pages = wizard.getPages();
        return pages.isEmpty() ? null : pages.get(0);
    }


    public void initPage(Wizard wizard, WizardPage page)
    {
    }


    protected boolean evaluateAttribute(String attribute, Wizard wizard)
    {
        if(wizard.getWizardContext() != null)
        {
            Object attr = wizard.getWizardContext().getAttribute(attribute);
            if(attr instanceof Boolean)
            {
                return ((Boolean)attr).booleanValue();
            }
        }
        try
        {
            String methodTail = String.valueOf(attribute.charAt(0)).toUpperCase() + String.valueOf(attribute.charAt(0)).toUpperCase();
            Method method = ReflectionUtils.findMethod(wizard.getClass(), "is" + methodTail);
            if(method == null)
            {
                method = ReflectionUtils.findMethod(wizard.getClass(), "has" + methodTail);
            }
            if(method == null)
            {
                method = ReflectionUtils.findMethod(wizard.getClass(), "get" + methodTail);
            }
            if(method != null)
            {
                Object value = method.invoke(wizard, (Object[])null);
                return (value instanceof Boolean && Boolean.TRUE.equals(value));
            }
        }
        catch(Exception e)
        {
            LOG.error("Could not evaluate attribute, reason was: ", e);
        }
        return false;
    }


    protected boolean evaluateTransition(Transition transition, Wizard wizard)
    {
        if(transition.getIfAttributesFalse() != null)
        {
            for(String falseAttr : transition.getIfAttributesFalse())
            {
                if(evaluateAttribute(falseAttr, wizard))
                {
                    return false;
                }
            }
        }
        if(transition.getIfAttributesTrue() != null)
        {
            for(String trueAttr : transition.getIfAttributesTrue())
            {
                if(!evaluateAttribute(trueAttr, wizard))
                {
                    return false;
                }
            }
        }
        return true;
    }


    public WizardPage next(Wizard wizard, WizardPage page)
    {
        WizardPage ret = null;
        if(this.transitions != null && this.transitions.get(page.getId()) != null)
        {
            for(Transition transition : this.transitions.get(page.getId()))
            {
                if(evaluateTransition(transition, wizard))
                {
                    return wizard.getPage(transition.getDestination());
                }
            }
        }
        else
        {
            if(page instanceof AbstractGenericItemPage)
            {
                AbstractGenericItemPage decisionPage = (AbstractGenericItemPage)page;
                String nextWizardPageId = decisionPage.getNextPageWizardId();
                if(StringUtils.isNotBlank(nextWizardPageId))
                {
                    ret = wizard.getPage(nextWizardPageId);
                }
            }
            if(ret == null)
            {
                int pageIndex = wizard.getPages().indexOf(page);
                if(pageIndex < wizard.getPages().size() - 1)
                {
                    ret = wizard.getPages().get(pageIndex + 1);
                }
            }
        }
        return ret;
    }


    public WizardPage previous(Wizard wizard, WizardPage page)
    {
        return wizard.getPreviousPage();
    }


    public boolean validate(Wizard wizard, WizardPage page)
    {
        return true;
    }


    public void beforeBack(Wizard wizard, WizardPage page)
    {
    }


    public void beforeNext(Wizard wizard, WizardPage page)
    {
    }
}
