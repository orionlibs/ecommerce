package de.hybris.platform.servicelayer.user.listener;

import java.util.Objects;

public class PasswordChangeEvent
{
    private final String userId;


    public PasswordChangeEvent(String userId)
    {
        this.userId = Objects.<String>requireNonNull(userId, "userId cannot be null.");
    }


    public String getUserId()
    {
        return this.userId;
    }
}
