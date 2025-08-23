package de.hybris.platform.util.localization.jdbc;

import de.hybris.platform.core.PK;
import java.util.Collection;

public interface LocalizationInfo
{
    Collection<PK> getLanguagePKs();


    String getLocalizedProperty(PK paramPK, String paramString);


    String getLocalizedPropertyFromHierarchy(PK paramPK, String paramString1, Type paramType, String paramString2);
}
