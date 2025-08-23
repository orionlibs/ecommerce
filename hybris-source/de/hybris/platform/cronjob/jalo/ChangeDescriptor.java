package de.hybris.platform.cronjob.jalo;

import de.hybris.platform.directpersistence.annotation.ForceJALO;

public class ChangeDescriptor extends GeneratedChangeDescriptor
{
    @ForceJALO(reason = "something else")
    protected void removeLinks()
    {
    }
}
