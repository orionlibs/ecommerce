package de.hybris.platform.persistence.audit.impl;

import com.google.common.base.Splitter;
import de.hybris.bootstrap.util.LocaleHelper;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.type.TypeService;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.lang.StringUtils;
import org.assertj.core.util.Lists;

public class BlacklistedAttributesHelper
{
    public static final String BLACKLISTED_ATTRIBUTES = "blacklistedAttributes";
    private static final String DEFAULT_BLACKLISTED_ATTR_OBFUSCATION_VALUE = "****";
    private static final String BLACKLISTED_ATTR_OBFUSCATION_VALUE_PROPERTY = "audit.blacklistedProperties.obfuscationValue";
    private static final String BLACKLISTED_ATTR_OBFUSCATION_VALUE = Registry.getCurrentTenant().getConfig()
                    .getString("audit.blacklistedProperties.obfuscationValue", "****");
    private static final Map<String, Set<String>> blacklistedProperties = new ConcurrentHashMap<>();


    public static Set<String> getBlacklistedAttributesForTypeHierarchy(String type)
    {
        return blacklistedProperties.computeIfAbsent(type.toLowerCase(LocaleHelper.getPersistenceLocale()), k -> {
            Set<String> blacklistedPropertiesForType = new HashSet<>(getBlacklistedAttributesForType(k));
            TypeService typeService = (TypeService)Registry.getApplicationContext().getBean("typeService", TypeService.class);
            ComposedTypeModel composedType = (ComposedTypeModel)typeService.getTypeForCode(type);
            Collection<ComposedTypeModel> allSuperTypes = composedType.getAllSuperTypes();
            for(ComposedTypeModel ctm : allSuperTypes)
            {
                String superTypeCode = StringUtils.lowerCase(ctm.getCode(), LocaleHelper.getPersistenceLocale());
                blacklistedPropertiesForType.addAll(getBlacklistedAttributesForType(superTypeCode));
            }
            return blacklistedPropertiesForType;
        });
    }


    private static Collection<String> getBlacklistedAttributesForType(String type)
    {
        String property = Registry.getCurrentTenant().getConfig().getString("audit." + type + ".blacklistedProperties", "");
        return extractBlacklistedAttributes(property);
    }


    private static Collection<String> extractBlacklistedAttributes(String property)
    {
        if(property.isEmpty())
        {
            return Collections.emptyList();
        }
        return Lists.newArrayList(Splitter.on(",").trimResults().omitEmptyStrings().split(property));
    }


    public static Map<String, Object> createContext(Map<String, Boolean> type)
    {
        Map<String, Object> context = new ConcurrentHashMap<>();
        context.put("blacklistedAttributes", type);
        return context;
    }


    public static void clearBlacklistedProperties()
    {
        blacklistedProperties.clear();
    }


    public static String getBlacklistedAttrObfuscationValue()
    {
        return BLACKLISTED_ATTR_OBFUSCATION_VALUE;
    }
}
