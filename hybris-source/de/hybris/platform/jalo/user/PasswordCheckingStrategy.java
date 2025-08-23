package de.hybris.platform.jalo.user;

public interface PasswordCheckingStrategy
{
    boolean checkPassword(User paramUser, String paramString);
}
