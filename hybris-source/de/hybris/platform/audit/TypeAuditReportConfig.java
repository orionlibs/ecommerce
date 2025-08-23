package de.hybris.platform.audit;

import com.google.common.base.Preconditions;
import de.hybris.platform.audit.internal.config.AuditConfigService;
import de.hybris.platform.audit.internal.config.AuditReportConfig;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import org.apache.commons.lang3.ObjectUtils;

public class TypeAuditReportConfig
{
    private final String configName;
    private AuditReportConfig config;
    private final PK rootTypePk;
    private final boolean reportOnlyLastState;
    private final Set<String> langIsoCode;
    private final UUID executionId = UUID.randomUUID();


    TypeAuditReportConfig(Builder builder)
    {
        Preconditions.checkArgument(ObjectUtils.anyNotNull(new Object[] {builder.configName, builder.config}, ), "either configName or config must not be null");
        Objects.requireNonNull(builder.rootTypePk, "rootTypePk must not be null");
        this.configName = builder.configName;
        this.config = builder.config;
        this.rootTypePk = builder.rootTypePk;
        this.reportOnlyLastState = builder.reportOnlyLastState;
        this.langIsoCode = builder.langIsoCode;
    }


    public AuditReportConfig getReportConfig()
    {
        if(this.config == null)
        {
            this.config = getReportConfigForName(this.configName);
        }
        return this.config;
    }


    AuditReportConfig getReportConfigForName(String configName)
    {
        return getAuditConfigService().getConfigForName(configName);
    }


    private AuditConfigService getAuditConfigService()
    {
        return (AuditConfigService)Registry.getApplicationContext().getBean("auditConfigService", AuditConfigService.class);
    }


    public PK getRootTypePk()
    {
        return this.rootTypePk;
    }


    public boolean isReportOnlyLastState()
    {
        return this.reportOnlyLastState;
    }


    public Set<String> getLangIsoCodes()
    {
        return this.langIsoCode;
    }


    public UUID getExecutionId()
    {
        return this.executionId;
    }


    public static Builder builder()
    {
        return new Builder();
    }
}
