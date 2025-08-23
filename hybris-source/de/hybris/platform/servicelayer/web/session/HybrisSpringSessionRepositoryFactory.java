package de.hybris.platform.servicelayer.web.session;

import org.springframework.core.serializer.Deserializer;
import org.springframework.session.SessionRepository;

public interface HybrisSpringSessionRepositoryFactory
{
    SessionRepository createRepository(Deserializer paramDeserializer, String paramString1, String paramString2);
}
