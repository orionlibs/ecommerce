package de.hybris.platform.cockpit.events.impl;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommentCockpitEvent extends InfoboxCockpitEvent
{
    private final List<TypedObject> userComments = new ArrayList<>();


    public CommentCockpitEvent(Object source, List<TypedObject> userComments)
    {
        super(source);
        if(userComments != null)
        {
            this.userComments.addAll(userComments);
        }
    }


    public List<TypedObject> getUserComments()
    {
        return Collections.unmodifiableList(this.userComments);
    }


    public List<TypedObject> getRelatedItems()
    {
        return Collections.unmodifiableList(this.userComments);
    }
}
