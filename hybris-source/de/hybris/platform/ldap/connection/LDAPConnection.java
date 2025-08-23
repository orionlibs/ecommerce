package de.hybris.platform.ldap.connection;

import de.hybris.platform.ldap.exception.LDAPOperationException;
import de.hybris.platform.ldap.exception.LDAPUnavailableException;
import java.util.Collection;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;

public interface LDAPConnection
{
    void close();


    Collection searchBaseEntry(String paramString1, String paramString2, int paramInt1, int paramInt2) throws LDAPOperationException, LDAPUnavailableException, NamingException;


    Collection searchBaseEntry(String paramString1, String paramString2, int paramInt1, int paramInt2, String[] paramArrayOfString) throws LDAPOperationException, LDAPUnavailableException, NamingException;


    Collection searchSubTree(String paramString1, String paramString2, int paramInt1, int paramInt2) throws LDAPOperationException, LDAPUnavailableException, NamingException;


    Collection<LDAPGenericObject> searchSubTree(String paramString1, String paramString2, int paramInt1, int paramInt2, String[] paramArrayOfString) throws LDAPOperationException, LDAPUnavailableException, NamingException;


    Attributes read(String paramString) throws LDAPOperationException, LDAPUnavailableException, NamingException;


    boolean checkPassword(String paramString1, String paramString2, char[] paramArrayOfchar);


    void changePassword(DirContext paramDirContext, String paramString1, String paramString2, String paramString3) throws NamingException;
}
