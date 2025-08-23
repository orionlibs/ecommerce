package de.hybris.platform.servicelayer.user;

public interface UserAuditLoginStrategy
{
    void auditUserOnWrongCredentials(String paramString);


    void auditUserOnCorrectCredentials(String paramString);
}
