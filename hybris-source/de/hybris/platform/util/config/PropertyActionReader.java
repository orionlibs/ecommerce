package de.hybris.platform.util.config;

import de.hybris.bootstrap.util.LocaleHelper;
import de.hybris.platform.core.Registry;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class PropertyActionReader
{
    private final Map<String, Set<String>> configuredDisabledTypesForAction = new ConcurrentHashMap<>();
    private static final String ACTION_DISABLED_FOR_TYPE = "%s\\.disabled\\.for\\.type\\.(.*)";


    public void clearConfiguration()
    {
        this.configuredDisabledTypesForAction.clear();
    }


    public boolean isActionDisabledForType(String propertyAction, String typeCode)
    {
        return ((Set)this.configuredDisabledTypesForAction
                        .computeIfAbsent(propertyAction, action -> getFinalTypesDisabledForPropertyAction(action)))
                        .contains(typeCode.toLowerCase(LocaleHelper.getPersistenceLocale()));
    }


    private Set<String> getFinalTypesDisabledForPropertyAction(String propertyAction)
    {
        Set<String> disabledTypes = new HashSet<>();
        Map<String, String> individualTypesConfiguration = getConfigurationForIndividualTypes(propertyAction);
        for(Map.Entry<String, String> type : individualTypesConfiguration.entrySet())
        {
            boolean typeIsDisabled = Boolean.parseBoolean(type.getValue());
            if(typeIsDisabled)
            {
                disabledTypes.add(((String)type.getKey()).toLowerCase(LocaleHelper.getPersistenceLocale()));
                continue;
            }
            disabledTypes.remove(((String)type.getKey()).toLowerCase(LocaleHelper.getPersistenceLocale()));
        }
        return disabledTypes;
    }


    private Map<String, String> getConfigurationForIndividualTypes(String propertyAction)
    {
        ConfigIntf cfg = Registry.getCurrentTenant().getConfig();
        return cfg.getParametersMatching(
                        String.format("%s\\.disabled\\.for\\.type\\.(.*)", new Object[] {propertyAction}), true);
    }


    public static PropertyActionReader getPropertyActionReader()
    {
        return (PropertyActionReader)Registry.getCoreApplicationContext().getBean("propertyActionReader", PropertyActionReader.class);
    }
}
