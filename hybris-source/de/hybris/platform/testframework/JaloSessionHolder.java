package de.hybris.platform.testframework;

import de.hybris.platform.jalo.JaloSession;

public interface JaloSessionHolder
{
    void establishJaloSession(JaloSession paramJaloSession);


    JaloSession takeJaloSession();
}
