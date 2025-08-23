package de.hybris.platform.comments.jalo;

import de.hybris.platform.comments.constants.GeneratedCommentsConstants;
import de.hybris.platform.directpersistence.annotation.ForceJALO;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInternalException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearchException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

public class Comment extends GeneratedComment
{
    private static final Logger LOG = Logger.getLogger(Comment.class.getName());


    @ForceJALO(reason = "abstract method implementation")
    public Collection<Item> getRelatedItems(SessionContext ctx)
    {
        StringBuilder buffer = new StringBuilder();
        buffer.append("SELECT {").append("target").append("} ");
        buffer.append("FROM {").append(GeneratedCommentsConstants.Relations.COMMENTITEMRELATION).append("* AS rel} ");
        buffer.append("WHERE {rel:").append("qualifier").append("} = ?quali AND ");
        buffer.append("{rel:").append("source").append("} = ?item AND ");
        buffer.append("{rel:").append("language").append("} IS NULL ");
        buffer.append("ORDER BY ");
        buffer.append("{rel:").append(Item.PK).append("} ASC");
        try
        {
            Map<Object, Object> args = new HashMap<>();
            args.put("item", this);
            args.put("quali", GeneratedCommentsConstants.Relations.COMMENTITEMRELATION);
            return getSession().getFlexibleSearch().search(ctx, buffer.toString(), args, Collections.singletonList(Item.class), true, true, 0, -1)
                            .getResult();
        }
        catch(FlexibleSearchException e)
        {
            throw new JaloInternalException(e, "flexible search error for link search query '" + buffer.toString() + "' (item='" + this + "',quali='" + GeneratedCommentsConstants.Relations.COMMENTITEMRELATION + ")", 0);
        }
    }


    @ForceJALO(reason = "abstract method implementation")
    public void setRelatedItems(SessionContext ctx, Collection<Item> items)
    {
        Collection<Item> oldItems = getRelatedItems();
        Collection<Item> newItems = (items == null) ? Collections.EMPTY_LIST : new ArrayList<>(items);
        for(Item item : oldItems)
        {
            if(!newItems.contains(item))
            {
                removeRelatedItem(item);
                continue;
            }
            newItems.remove(item);
        }
        for(Item item : newItems)
        {
            List<Comment> comments = CommentsManager.getInstance().getComments(item);
            if(!comments.contains(this))
            {
                List<Comment> newComments = new ArrayList<>(comments);
                newComments.add(this);
                CommentsManager.getInstance().setComments(item, newComments);
            }
        }
    }


    private void removeRelatedItem(Item item)
    {
        if(item.isAlive())
        {
            List<Comment> comments = CommentsManager.getInstance().getComments(item);
            if(comments.contains(this))
            {
                List<Comment> newComments = new ArrayList<>(comments);
                newComments.remove(this);
                CommentsManager.getInstance().setComments(item, newComments);
            }
        }
    }
}
