package de.hybris.platform.processengine.definition;

public final class ActionDefinitionContextHolder
{
    private static final ThreadLocal<ActionDefinitionContext> actionDefinitionContext = new ThreadLocal<>();


    public static ActionDefinitionContext getActionDefinitionContext()
    {
        return actionDefinitionContext.get();
    }


    static void setActionDefinitionContext(ActionDefinitionContext context)
    {
        actionDefinitionContext.set(context);
    }


    static void unsetActionDefinitionContext()
    {
        actionDefinitionContext.remove();
    }
}
