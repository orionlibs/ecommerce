package de.hybris.platform.adaptivesearch.strategies.impl;

import de.hybris.platform.adaptivesearch.strategies.AsUidGenerator;
import java.util.UUID;

public class DefaultAsUidGenerator implements AsUidGenerator
{
    public String generateUid()
    {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
}
