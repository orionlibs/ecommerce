package de.hybris.platform.comments.jalo;

import de.hybris.platform.core.Registry;
import org.apache.log4j.Logger;

public class CommentsManager extends GeneratedCommentsManager
{
    private static final Logger LOG = Logger.getLogger(CommentsManager.class.getName());


    public static CommentsManager getInstance()
    {
        return (CommentsManager)Registry.getCurrentTenant().getJaloConnection().getExtensionManager().getExtension("comments");
    }
}
