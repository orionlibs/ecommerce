package de.hybris.platform.adaptivesearchbackoffice.components;

import com.hybris.cockpitng.components.Actions;

public class ActionsMenu extends Actions
{
    public Object createDefaultRenderer()
    {
        return new DefaultActionsMenuRenderer();
    }
}
