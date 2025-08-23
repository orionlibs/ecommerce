package de.hybris.platform.persistence.c2l;

import de.hybris.platform.core.PK;
import de.hybris.platform.persistence.ExtensibleItemRemote;
import de.hybris.platform.persistence.property.ItemLocalizedPropertyCacheKey;
import de.hybris.platform.util.ItemPropertyValue;
import de.hybris.platform.util.ItemPropertyValueCollection;
import java.util.Map;
import java.util.Set;

public interface LocalizableItemRemote extends ExtensibleItemRemote
{
    void hintPropertyCache(ItemLocalizedPropertyCacheKey paramItemLocalizedPropertyCacheKey);


    void copyLocalizablePropertiesFrom(LocalizableItemRemote paramLocalizableItemRemote);


    Map getAllLocalizedProperties(PK paramPK);


    Set getLocalizedPropertyNames(PK paramPK);


    Object getLocalizedProperty(String paramString, PK paramPK);


    Object setLocalizedProperty(String paramString, PK paramPK, Object paramObject);


    Object removeLocalizedProperty(String paramString, PK paramPK);


    Map<ItemPropertyValue, Object> setAllLocalizedProperties(String paramString, Map<ItemPropertyValue, Object> paramMap);


    Map<ItemPropertyValue, Object> getAllLocalizedProperties(String paramString, ItemPropertyValueCollection paramItemPropertyValueCollection);
}
