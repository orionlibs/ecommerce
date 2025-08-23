package de.hybris.platform.jalo;

import de.hybris.platform.jalo.user.User;

public interface JaloSessionListener
{
    void beforeSessionClose(JaloSession paramJaloSession);


    void afterSessionCreation(JaloSession paramJaloSession);


    void afterSessionUserChange(JaloSession paramJaloSession, User paramUser);


    void afterSessionAttributeChange(JaloSession paramJaloSession, String paramString, Object paramObject);
}
