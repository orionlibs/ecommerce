package de.hybris.platform.cockpit.services;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.user.UserModel;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public interface SystemService
{
    Set<LanguageModel> getAvailableLanguages();


    Set<String> getAvailableLanguageIsos();


    Set<LanguageModel> getAllReadableLanguages();


    Set<String> getAllReadableLanguageIsos();


    Set<LanguageModel> getAllWriteableLanguages();


    Set<String> getAllWriteableLanguageIsos();


    LanguageModel getLanguageForLocale(Locale paramLocale);


    LanguageModel getCurrentLanguage();


    List<String> getUsersByName(String paramString1, String paramString2);


    UserModel getUserByUID(String paramString);


    UserModel getUserByName(String paramString);


    boolean checkPermissionOn(String paramString1, String paramString2);


    boolean checkAttributePermissionOn(String paramString1, String paramString2, String paramString3);


    void setSessionLanguage(LanguageModel paramLanguageModel);


    UserModel getCurrentUser();


    CatalogVersionModel getCatalogVersion(TypedObject paramTypedObject);


    boolean itemExist(PK paramPK);
}
