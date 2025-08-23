package de.hybris.platform.ldap.jobs;

import de.hybris.platform.ldap.model.LDIFMediaModel;
import java.util.Map;

public interface LDIFImportStrategy
{
    void executeFileBasedImport(LDIFMediaModel paramLDIFMediaModel) throws LDAPImportException;


    void executeFileSearchBasedImport(Map<String, String> paramMap) throws LDAPImportException;


    void executeFileSearchBasedUserImport(Map<String, String> paramMap) throws LDAPImportException;
}
