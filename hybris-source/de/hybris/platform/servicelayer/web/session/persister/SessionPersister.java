package de.hybris.platform.servicelayer.web.session.persister;

import de.hybris.platform.servicelayer.web.session.PersistedSession;
import org.springframework.core.serializer.Deserializer;

public interface SessionPersister
{
    void persist(PersistedSession paramPersistedSession);


    void remove(String paramString);


    PersistedSession load(String paramString, Deserializer paramDeserializer);
}
