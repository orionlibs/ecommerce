package de.hybris.platform.platformbackoffice.actions;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionListener;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;
import com.hybris.cockpitng.actions.CockpitActionRenderer;
import com.hybris.cockpitng.actions.impl.DefaultActionRenderer;
import com.hybris.cockpitng.util.BackofficeSpringUtil;
import com.hybris.cockpitng.util.UITools;
import de.hybris.platform.scripting.engine.ScriptExecutable;
import de.hybris.platform.scripting.engine.ScriptingLanguagesService;
import de.hybris.platform.scripting.engine.exception.ScriptingException;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;

public class ScriptingAction extends DefaultActionRenderer<Object, Object> implements CockpitAction<Object, Object>
{
    private static final Logger LOG = LoggerFactory.getLogger(ScriptingAction.class);
    protected static final String PARAMETER_SCRIPT_URI = "scriptUri";
    protected static final String PARAMETER_LABEL = "label";
    protected static final String ACTION_ICON_URI = "iconUri";
    protected static final String DISABLED_ACTION_ICON_URI = "disabledIconUri";
    protected static final String ACTION_ICON_FALLBACK_NAME = "source-code";
    protected static final String ACTION_ICON_FALLBACK = "/images/icon_func_script_default.png";
    protected static final String DISABLED_ACTION_ICON_FALLBACK = "/images/icon_func_script_disabled.png";
    private static final Pattern protocolPatter = Pattern.compile("^http(s)?://.*");


    public ActionResult<Object> perform(ActionContext<Object> actionContext)
    {
        CockpitAction<Object, Object> dynamicAction = resolveActionScript(actionContext);
        return (dynamicAction != null) ? dynamicAction.perform(actionContext) : new ActionResult("error");
    }


    public boolean canPerform(ActionContext<Object> actionContext)
    {
        CockpitAction<Object, Object> dynamicAction = resolveActionScript(actionContext);
        return (dynamicAction != null && dynamicAction.canPerform(actionContext));
    }


    public boolean needsConfirmation(ActionContext<Object> actionContext)
    {
        CockpitAction<Object, Object> dynamicAction = resolveActionScript(actionContext);
        return (dynamicAction != null && dynamicAction.needsConfirmation(actionContext));
    }


    public String getConfirmationMessage(ActionContext<Object> actionContext)
    {
        CockpitAction<Object, Object> dynamicAction = resolveActionScript(actionContext);
        return (dynamicAction != null) ? dynamicAction.getConfirmationMessage(actionContext) : "";
    }


    public void render(Component component, CockpitAction<Object, Object> cockpitAction, ActionContext<Object> actionContext, boolean updateMode, ActionListener<Object> actionListener)
    {
        CockpitAction<Object, Object> scriptAction = resolveActionScript(actionContext);
        if(scriptAction instanceof CockpitActionRenderer)
        {
            ((CockpitActionRenderer)scriptAction).render(component, cockpitAction, actionContext, updateMode, actionListener);
        }
        else
        {
            super.render(component, cockpitAction, actionContext, updateMode, actionListener);
        }
    }


    protected String getIconUri(ActionContext<Object> context, boolean canPerform)
    {
        String iconURI;
        CockpitAction<Object, Object> dynamicAction = resolveActionScript(context);
        if(dynamicAction == null)
        {
            iconURI = "source-code";
        }
        else if(canPerform)
        {
            iconURI = readIconUri(context, "iconUri", "/images/icon_func_script_default.png");
        }
        else
        {
            iconURI = readIconUri(context, "disabledIconUri", "/images/icon_func_script_disabled.png");
        }
        return iconURI;
    }


    private String readIconUri(ActionContext<Object> context, String parameter, String fallbackIcon)
    {
        String iconURI, uriParameter = (String)context.getParameter(parameter);
        String tempIconURI = StringUtils.isNotBlank(uriParameter) ? uriParameter : context.getIconUri();
        if(!useImage(tempIconURI))
        {
            String iconName = getIconName(context);
            iconURI = StringUtils.isNotBlank(iconName) ? iconName : "source-code";
        }
        else if(StringUtils.isNotBlank(uriParameter))
        {
            if(protocolPatter.matcher(uriParameter).find())
            {
                iconURI = uriParameter;
            }
            else
            {
                iconURI = UITools.adjustURL(uriParameter);
            }
        }
        else
        {
            iconURI = String.format("%s%s", new Object[] {context.getParameter("componentRoot"), fallbackIcon});
        }
        return iconURI;
    }


    protected String getLocalizedName(ActionContext<?> context)
    {
        String ret;
        if(resolveActionScript((ActionContext)context) == null)
        {
            ret = context.getLabel("broken.action");
        }
        else
        {
            ret = String.valueOf(context.getParameter("label"));
        }
        return ret;
    }


    protected CockpitAction<Object, Object> resolveActionScript(ActionContext<Object> actionContext)
    {
        String uri = String.valueOf(actionContext.getParameter("scriptUri"));
        try
        {
            ScriptExecutable executable = getScriptingLanguagesService().getExecutableByURI(uri);
            if(executable != null)
            {
                return (CockpitAction<Object, Object>)executable.getAsInterface(CockpitAction.class);
            }
            LOG.warn("Cannot find executable script for URI: {}", uri);
        }
        catch(ScriptingException e)
        {
            LOG.warn(String.format("%s%s", new Object[] {"Some error occurred while resolving a script at URI: ", uri}), (Throwable)e);
        }
        return null;
    }


    private static ScriptingLanguagesService getScriptingLanguagesService()
    {
        return (ScriptingLanguagesService)BackofficeSpringUtil.getBean("scriptingLanguagesService");
    }
}
