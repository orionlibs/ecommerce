package de.hybris.platform.persistence.audit.internal;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import de.hybris.bootstrap.util.LocaleHelper;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.config.ConfigIntf;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuditEnablementService
{
    private static final Logger LOG = LoggerFactory.getLogger(AuditEnablementService.class);
    static final String AUDITING_ENABLED = "auditing.enabled";
    static final String AUDITING_BLACKLIST = "auditing.blacklist";
    static final String AUDITING_FOR_ALL_TYPES = "auditing.alltypes.enabled";
    private Set<String> blacklistedTypes;
    private String blacklistTypesStr;
    private Set<String> configuredEnabledTypes;
    private final Set<PK> enabledTypesCache = new HashSet<>();
    private final Set<PK> disabledTypesCache = new HashSet<>();


    @PostConstruct
    public void refreshConfiguredAuditTypes()
    {
        String blacklistTypesCfg = Config.getString("auditing.blacklist", "");
        if(this.blacklistTypesStr == null || !this.blacklistTypesStr.equals(blacklistTypesCfg))
        {
            this.blacklistTypesStr = blacklistTypesCfg;
            this.blacklistedTypes = loadAuditBlacklist(this.blacklistTypesStr);
        }
        ConfigIntf cfg = Registry.getCurrentTenant().getConfig();
        Map<String, String> auditTypes = cfg.getParametersMatching("audit\\.(.*)\\.enabled", true);
        checkConfiguration(auditTypes);
        Set<String> configuredTypes = new HashSet<>();
        for(Map.Entry<String, String> entry : auditTypes.entrySet())
        {
            String typeCode = entry.getKey();
            Boolean enabled = BooleanUtils.toBooleanObject(entry.getValue());
            if(isTypeNotBlacklisted(typeCode) && BooleanUtils.isTrue(enabled))
            {
                configuredTypes.add(typeCode.toLowerCase(LocaleHelper.getPersistenceLocale()));
            }
        }
        this.configuredEnabledTypes = configuredTypes;
        this.enabledTypesCache.clear();
        this.disabledTypesCache.clear();
    }


    private void checkConfiguration(Map<String, String> settings)
    {
        Set<String> violations = new HashSet<>();
        for(String setting : settings.keySet())
        {
            if(!setting.toLowerCase(LocaleHelper.getPersistenceLocale()).equals(setting))
            {
                violations.add(setting);
            }
        }
        if(!violations.isEmpty())
        {
            Set<String> violatingProperties = (Set<String>)violations.stream().map(i -> "audit." + i + ".enabled").collect(Collectors.toSet());
            String errorMsg = buildConfigurationErrorMessage(violatingProperties);
            LOG.error(errorMsg);
            throw new AuditConfigurationException(errorMsg, violatingProperties);
        }
    }


    private String buildConfigurationErrorMessage(Set<String> violations)
    {
        return "Failed to load audit configuration. Following properties are not lowercase: " + Joiner.on(",").join(violations);
    }


    private static Set<String> loadAuditBlacklist(String blackedTypes)
    {
        if(blackedTypes.length() == 0)
        {
            return Collections.emptySet();
        }
        Set<String> returnTokens = new HashSet<>();
        Splitter.on(",")
                        .trimResults()
                        .omitEmptyStrings()
                        .split(blackedTypes)
                        .forEach(token -> returnTokens.add(token.toLowerCase(LocaleHelper.getPersistenceLocale())));
        return Collections.unmodifiableSet(returnTokens);
    }


    public boolean isAuditEnabledGlobally()
    {
        return Config.getBoolean("auditing.enabled", false);
    }


    public boolean isAuditEnabledForType(String typeCode)
    {
        return isAuditEnabledForType(getComposedTypePK(typeCode));
    }


    public boolean isAuditEnabledForType(PK typePk)
    {
        Preconditions.checkNotNull(typePk, "type is required");
        if(isAuditDisabledGlobally())
        {
            return false;
        }
        if(this.enabledTypesCache.contains(typePk))
        {
            return true;
        }
        if(this.disabledTypesCache.contains(typePk))
        {
            return false;
        }
        ComposedType typeToCheck = (ComposedType)JaloSession.lookupItem(typePk);
        String typeCode = typeToCheck.getCode();
        if(isTypeBlacklisted(typeCode))
        {
            this.disabledTypesCache.add(typePk);
            return false;
        }
        Set<String> superTypes = (Set<String>)typeToCheck.getAllSuperTypes().stream().map(st -> st.getCode().toLowerCase(LocaleHelper.getPersistenceLocale())).collect(Collectors.toSet());
        Collection<String> blackListedSuperTypes = CollectionUtils.intersection(superTypes, this.blacklistedTypes);
        if(CollectionUtils.isNotEmpty(blackListedSuperTypes))
        {
            blackListedSuperTypes.forEach(tc -> this.disabledTypesCache.add(getComposedTypePK(tc)));
            this.disabledTypesCache.add(typePk);
            return false;
        }
        if(isAuditEnabledForAllTypes())
        {
            return true;
        }
        if(isTypeEnabledForAudit(typeCode))
        {
            this.enabledTypesCache.add(typeToCheck.getPK());
            return true;
        }
        Collection<String> configuredSuperTypes = CollectionUtils.intersection(superTypes, this.configuredEnabledTypes);
        if(CollectionUtils.isNotEmpty(configuredSuperTypes))
        {
            configuredSuperTypes.forEach(tc -> this.enabledTypesCache.add(getComposedTypePK(tc)));
            this.enabledTypesCache.add(typeToCheck.getPK());
            return true;
        }
        return false;
    }


    private boolean isAuditDisabledGlobally()
    {
        return !isAuditEnabledGlobally();
    }


    private boolean isTypeBlacklisted(String typeCode)
    {
        return this.blacklistedTypes.contains(typeCode.toLowerCase(LocaleHelper.getPersistenceLocale()));
    }


    private boolean isTypeNotBlacklisted(String typeCode)
    {
        return !isTypeBlacklisted(typeCode);
    }


    private boolean isTypeEnabledForAudit(String typeCode)
    {
        return this.configuredEnabledTypes.contains(typeCode.toLowerCase(LocaleHelper.getPersistenceLocale()));
    }


    public boolean isAuditEnabledForAllTypes()
    {
        return Config.getBoolean("auditing.alltypes.enabled", false);
    }


    private PK getComposedTypePK(String composedTypeCode)
    {
        return TypeManager.getInstance().getComposedType(composedTypeCode).getPK();
    }
}
