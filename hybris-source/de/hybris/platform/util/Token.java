package de.hybris.platform.util;

import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;
import java.util.UUID;

public class Token
{
    private final String expectedValue;


    private Token(String expectedValue)
    {
        this.expectedValue = expectedValue;
    }


    public static Token generateNew()
    {
        Hasher hasher = Hashing.sha512().newHasher();
        for(int i = 0; i < 10; i++)
        {
            UUID uuid = UUID.randomUUID();
            hasher.putLong(uuid.getLeastSignificantBits());
            hasher.putLong(uuid.getMostSignificantBits());
        }
        return new Token(hasher.hash().toString());
    }


    public String stringValue()
    {
        return this.expectedValue;
    }


    public boolean verify(String resumeToken)
    {
        return this.expectedValue.equals(resumeToken);
    }
}
