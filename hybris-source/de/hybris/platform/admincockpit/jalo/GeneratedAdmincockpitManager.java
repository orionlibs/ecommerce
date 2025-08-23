package de.hybris.platform.admincockpit.jalo;

import de.hybris.platform.admincockpit.constants.GeneratedAdmincockpitConstants;
import de.hybris.platform.admincockpit.jalo.cronjob.RemoveOrphanedFilesCronJob;
import de.hybris.platform.admincockpit.jalo.cronjob.RemoveOrphanedFilesJob;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.extension.Extension;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedAdmincockpitManager extends Extension
{
    protected static final Map<String, Map<String, Item.AttributeMode>> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Map<String, Item.AttributeMode>> ttmp = new HashMap<>();
        DEFAULT_INITIAL_ATTRIBUTES = ttmp;
    }

    public Map<String, Item.AttributeMode> getDefaultAttributeModes(Class<? extends Item> itemClass)
    {
        Map<String, Item.AttributeMode> ret = new HashMap<>();
        Map<String, Item.AttributeMode> attr = DEFAULT_INITIAL_ATTRIBUTES.get(itemClass.getName());
        if(attr != null)
        {
            ret.putAll(attr);
        }
        return ret;
    }


    public RemoveOrphanedFilesCronJob createRemoveOrphanedFilesCronJob(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedAdmincockpitConstants.TC.REMOVEORPHANEDFILESCRONJOB);
            return (RemoveOrphanedFilesCronJob)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating RemoveOrphanedFilesCronJob : " + e.getMessage(), 0);
        }
    }


    public RemoveOrphanedFilesCronJob createRemoveOrphanedFilesCronJob(Map attributeValues)
    {
        return createRemoveOrphanedFilesCronJob(getSession().getSessionContext(), attributeValues);
    }


    public RemoveOrphanedFilesJob createRemoveOrphanedFilesJob(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedAdmincockpitConstants.TC.REMOVEORPHANEDFILESJOB);
            return (RemoveOrphanedFilesJob)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating RemoveOrphanedFilesJob : " + e.getMessage(), 0);
        }
    }


    public RemoveOrphanedFilesJob createRemoveOrphanedFilesJob(Map attributeValues)
    {
        return createRemoveOrphanedFilesJob(getSession().getSessionContext(), attributeValues);
    }


    public String getName()
    {
        return "admincockpit";
    }
}
