package de.hybris.platform.audit.internal.config;

import de.hybris.platform.core.model.audit.AuditReportConfigModel;
import java.util.List;

public interface AuditConfigService
{
    AuditReportConfigModel storeConfiguration(String paramString1, String paramString2);


    AuditReportConfig getConfigForName(String paramString);


    void storeConfigurations();


    List<AuditReportConfig> getConfigsForRootType(String paramString);
}
