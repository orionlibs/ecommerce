package de.hybris.platform.masterserver;

import de.hybris.platform.masterserver.impl.StatisticsPayload;

public interface StatisticsPayloadEncryptor
{
    StatisticsPayload encrypt(String paramString1, String paramString2);
}
