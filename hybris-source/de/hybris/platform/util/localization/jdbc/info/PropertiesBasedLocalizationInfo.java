package de.hybris.platform.util.localization.jdbc.info;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import de.hybris.bootstrap.util.LocaleHelper;
import de.hybris.platform.core.PK;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.util.localization.jdbc.LocalizationInfo;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.lang.StringUtils;

public class PropertiesBasedLocalizationInfo implements LocalizationInfo
{
    private final ImmutableMap<PK, Properties> localizedProperties;
    private final Map<Long, String> pkToTypeCode;


    public PropertiesBasedLocalizationInfo(Map<Language, Properties> localization, Map<Long, String> pkToTypeCode)
    {
        Preconditions.checkNotNull(localization, "localization can't be null");
        Preconditions.checkNotNull(pkToTypeCode, "pkToTypeCode mapping can't be null");
        ImmutableMap.Builder<PK, Properties> localizedPropertiesBuilder = ImmutableMap.builder();
        for(Map.Entry<Language, Properties> localizationEntry : localization.entrySet())
        {
            localizedPropertiesBuilder.put(((Language)localizationEntry.getKey()).getPK(), localizationEntry.getValue());
        }
        this.localizedProperties = localizedPropertiesBuilder.build();
        this.pkToTypeCode = pkToTypeCode;
    }


    public Collection<PK> getLanguagePKs()
    {
        return (Collection<PK>)this.localizedProperties.keySet();
    }


    public String getLocalizedProperty(PK languagePK, String propertyKey)
    {
        Properties properties = (Properties)this.localizedProperties.get(languagePK);
        if(properties == null)
        {
            return null;
        }
        String result = properties.getProperty(propertyKey);
        return StringUtils.isBlank(result) ? null : result;
    }


    private List<String> splitAndReverseInheritancePKs(String inheritancePKs)
    {
        Iterable<String> inheritancePath = Splitter.on(",").trimResults().omitEmptyStrings().split(inheritancePKs);
        return Lists.reverse(Lists.newArrayList(inheritancePath));
    }


    public String getLocalizedPropertyFromHierarchy(PK languagePK, String qualifier, LocalizationInfo.Type propType, String inheritancePKs)
    {
        List<String> bottomToTopInheritance = splitAndReverseInheritancePKs(inheritancePKs);
        for(String typePK : bottomToTopInheritance)
        {
            String typeCode = this.pkToTypeCode.get(Long.valueOf(typePK));
            if(StringUtils.isNotBlank(typeCode))
            {
                String property = getLocalizedProperty(languagePK, "type." + typeCode
                                .toLowerCase(LocaleHelper.getPersistenceLocale()) + "." + qualifier.toLowerCase(LocaleHelper.getPersistenceLocale()) + "." + propType
                                .getType());
                if(StringUtils.isNotBlank(property))
                {
                    return property;
                }
            }
        }
        return null;
    }
}
