package de.hybris.platform.comments.jalo;

import de.hybris.platform.directpersistence.annotation.ForceJALO;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collection;
import java.util.Collections;
import org.apache.log4j.Logger;

public class Component extends GeneratedComponent
{
    private static final Logger LOG = Logger.getLogger(Component.class.getName());


    @ForceJALO(reason = "abstract method implementation")
    public Collection<CommentType> getAvailableCommentTypes(SessionContext ctx)
    {
        Collection<CommentType> commentTypes = null;
        Domain domain = getDomain(ctx);
        if(domain != null)
        {
            commentTypes = domain.getCommentTypes();
        }
        return (commentTypes == null) ? Collections.EMPTY_LIST : Collections.<CommentType>unmodifiableCollection(commentTypes);
    }
}
