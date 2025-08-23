package de.hybris.platform.audit.demo;

import de.hybris.platform.directpersistence.audit.internal.AuditEnablementService;
import de.hybris.platform.testframework.PropertyConfigSwitcher;
import java.util.HashMap;
import java.util.Map;

public class AuditTestConfigManager
{
    private final Map<String, PropertyConfigSwitcher> auditedTypes = new HashMap<>();
    private final AuditEnablementService auditEnablementService;


    @Deprecated(since = "6.6", forRemoval = true)
    public AuditTestConfigManager(AuditEnablementService auditEnablementService)
    {
        this(auditEnablementService.getDelegate());
    }


    public AuditTestConfigManager(AuditEnablementService auditEnablementService)
    {
        this.auditEnablementService = auditEnablementService;
    }


    public void enableAuditingForTypes(String... types)
    {
        for(String type : types)
        {
            PropertyConfigSwitcher switcher = getPropertyConfigSwitcher(type.toLowerCase());
            switcher.switchToValue("true");
        }
        this.auditEnablementService.refreshConfiguredAuditTypes();
    }


    public void disableAuditingForTypes(String... types)
    {
        for(String type : types)
        {
            PropertyConfigSwitcher switcher = getPropertyConfigSwitcher(type.toLowerCase());
            switcher.switchToValue("false");
        }
        this.auditEnablementService.refreshConfiguredAuditTypes();
    }


    private PropertyConfigSwitcher getPropertyConfigSwitcher(String type)
    {
        PropertyConfigSwitcher switcher;
        if(this.auditedTypes.containsKey(type))
        {
            switcher = this.auditedTypes.get(type);
        }
        else
        {
            switcher = new PropertyConfigSwitcher("audit." + type + ".enabled");
            this.auditedTypes.put(type, switcher);
        }
        return switcher;
    }


    public void resetAuditConfiguration()
    {
        for(Map.Entry<String, PropertyConfigSwitcher> entry : this.auditedTypes.entrySet())
        {
            ((PropertyConfigSwitcher)entry.getValue()).switchBackToDefault();
        }
        this.auditEnablementService.refreshConfiguredAuditTypes();
    }
}
