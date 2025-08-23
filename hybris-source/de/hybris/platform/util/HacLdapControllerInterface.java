package de.hybris.platform.util;

import java.util.Map;

public interface HacLdapControllerInterface
{
    Map ldapData();


    Map ldapCheck(String paramString1, String paramString2);


    Map ldapImpex(String paramString1, String paramString2);


    Map ldapSearch(String paramString1, String paramString2, String paramString3, Integer paramInteger);
}
