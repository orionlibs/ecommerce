package de.hybris.platform.cockpit.session.impl;

import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.cockpit.session.EditorAreaListener;
import de.hybris.platform.cockpit.session.UIBrowserArea;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultEditorAreaListener implements EditorAreaListener
{
    private static final Logger log = LoggerFactory.getLogger(DefaultEditorAreaListener.class);
    private final BaseUICockpitPerspective perspective;


    public DefaultEditorAreaListener(BaseUICockpitPerspective perspective)
    {
        if(perspective == null)
        {
            throw new IllegalArgumentException("Perspective can not be null.");
        }
        this.perspective = perspective;
    }


    public void currentObjectChanged(TypedObject previous, TypedObject current)
    {
        getPerspective().getEditorArea().getEditorAreaController().resetSectionPanelModel();
    }


    public void currentObjectUpdated()
    {
        getPerspective().getEditorArea().getEditorAreaController().resetSectionPanelModel();
    }


    public void valuesStored(ObjectValueContainer valueContainer)
    {
    }


    public void valuesUpdated(ObjectValueContainer valueContainer)
    {
        if(log.isDebugEnabled())
        {
            log.debug("Values updated event ignored.");
        }
    }


    public void nextItemClicked(TypedObject currentObj)
    {
        if(log.isDebugEnabled())
        {
            log.debug("Next item event ignored.");
        }
    }


    public void previousItemClicked(TypedObject currentObj)
    {
        if(log.isDebugEnabled())
        {
            log.debug("Previous item event ignored.");
        }
    }


    public void browseItemClicked(TypedObject currentObj)
    {
        if(log.isDebugEnabled())
        {
            log.debug("Browse item event ignored.");
        }
    }


    public void valueChanged(ObjectValueContainer valueContainer, PropertyDescriptor propertyDescriptor)
    {
    }


    protected UIBrowserArea getBrowserArea()
    {
        return this.perspective.getBrowserArea();
    }


    protected BaseUICockpitPerspective getPerspective()
    {
        return this.perspective;
    }
}
