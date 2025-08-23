package de.hybris.platform.cockpit.session.impl;

import de.hybris.platform.cockpit.session.PushController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PushControllerCreator
{
    private static final Logger LOG = LoggerFactory.getLogger(PushControllerCreator.class);


    public static PushController createPushController(PushCreationContainer creationContainer)
    {
        PushController pushController = null;
        if(creationContainer == null)
        {
            throw new IllegalArgumentException("Creation container can not be null.");
        }
        if(creationContainer.getClassName() == null)
        {
            throw new IllegalArgumentException("No class name specified.");
        }
        try
        {
            Class<?> pushCtrlClass = Class.forName(creationContainer.getClassName());
            pushController = (PushController)pushCtrlClass.newInstance();
            pushController.setParameters(creationContainer.getParameters());
        }
        catch(Exception e)
        {
            LOG.error("Creation of push controller failed.", e);
        }
        return pushController;
    }
}
