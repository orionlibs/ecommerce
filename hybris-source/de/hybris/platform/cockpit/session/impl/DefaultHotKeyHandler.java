package de.hybris.platform.cockpit.session.impl;

import de.hybris.platform.cockpit.components.navigationarea.renderer.UndoSectionRenderer;
import de.hybris.platform.cockpit.session.CockpitHotKeyHandler;
import de.hybris.platform.cockpit.session.UICockpitPerspective;
import de.hybris.platform.cockpit.session.UISessionUtils;

public class DefaultHotKeyHandler implements CockpitHotKeyHandler
{
    public void handleHotKey(Object data)
    {
        if("undo".equals(data))
        {
            UndoSectionRenderer.doUndoTask(null, null);
        }
        else if("redo".equals(data))
        {
            UndoSectionRenderer.doRedoTask(null, null);
        }
        else if("toggleWideScreen".equals(data))
        {
            UICockpitPerspective currentPerspective = UISessionUtils.getCurrentSession().getCurrentPerspective();
            if(currentPerspective instanceof BaseUICockpitPerspective)
            {
                ((BaseUICockpitPerspective)currentPerspective).toggleNavAndEditArea();
            }
        }
    }


    public boolean isBusyListener()
    {
        return true;
    }
}
