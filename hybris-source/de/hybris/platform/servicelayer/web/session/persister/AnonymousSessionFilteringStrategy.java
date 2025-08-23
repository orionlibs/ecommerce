package de.hybris.platform.servicelayer.web.session.persister;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.servicelayer.web.session.PersistedSession;
import java.util.Objects;

public class AnonymousSessionFilteringStrategy implements SessionFilteringStrategy
{
    public boolean shouldPersist(PersistedSession session)
    {
        Objects.requireNonNull(session, "session cannot be null.");
        return !isAnonymousUser(session);
    }


    private boolean isAnonymousUser(PersistedSession session)
    {
        JaloSession js = (JaloSession)session.getAttribute("jalosession");
        return (js != null && js.getUser() != null && js.getUser().isAnonymousCustomer());
    }
}
