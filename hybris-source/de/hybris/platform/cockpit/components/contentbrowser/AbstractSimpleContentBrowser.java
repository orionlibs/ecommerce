package de.hybris.platform.cockpit.components.contentbrowser;

import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import java.util.Set;

public abstract class AbstractSimpleContentBrowser extends AbstractContentBrowser
{
    public boolean update()
    {
        if(this.initialized)
        {
            return true;
        }
        return initialize();
    }


    public void resize()
    {
    }


    public void updateActivation()
    {
    }


    public void updateActiveItems()
    {
    }


    public void updateCaption()
    {
    }


    public void updateContextArea()
    {
    }


    public void updateItem(TypedObject item, Set<PropertyDescriptor> modifiedProperties, Object reason)
    {
    }


    public void updateMainArea()
    {
    }


    public void updateSelectedItems()
    {
    }


    public void updateToolbar()
    {
    }


    public void updateViewMode()
    {
    }
}
