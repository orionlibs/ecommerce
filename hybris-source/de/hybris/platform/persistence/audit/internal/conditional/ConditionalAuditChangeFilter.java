package de.hybris.platform.persistence.audit.internal.conditional;

import de.hybris.platform.core.Registry;
import de.hybris.platform.directpersistence.cache.SLDDataContainerProvider;
import de.hybris.platform.persistence.audit.AuditChangeFilter;
import de.hybris.platform.persistence.audit.AuditableChange;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.util.config.ConfigIntf;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.IOUtils;

public class ConditionalAuditChangeFilter implements AuditChangeFilter
{
    private final ConditionalAuditConfig conditionalAuditConfig;
    private final List<ConditionEvaluator> evaluators = new ArrayList<>();


    public ConditionalAuditChangeFilter(String conditionalAuditConfigXml, SLDDataContainerProvider sldDataContainerProvider, TypeService typeService)
    {
        this(conditionalAuditConfigXml, sldDataContainerProvider, typeService, Registry.getCurrentTenant().getConfig());
    }


    public ConditionalAuditChangeFilter(String conditionalAuditConfigXml, SLDDataContainerProvider sldDataContainerProvider, TypeService typeService, ConfigIntf tenantConfig)
    {
        ConditionalAuditConfigReader reader = new ConditionalAuditConfigReader();
        this.conditionalAuditConfig = reader.fromClasspathXml(conditionalAuditConfigXml);
        this.conditionalAuditConfig.getGroups()
                        .forEach(i -> this.evaluators.add(new ConditionEvaluator(i, sldDataContainerProvider, typeService, tenantConfig)));
    }


    private ConditionalAuditChangeFilter(InputStream configInputStream, SLDDataContainerProvider sldDataContainerProvider, TypeService typeService, ConfigIntf tenantConfig)
    {
        ConditionalAuditConfigReader reader = new ConditionalAuditConfigReader();
        this.conditionalAuditConfig = reader.fromXmlStream(configInputStream);
        this.conditionalAuditConfig.getGroups()
                        .forEach(i -> this.evaluators.add(new ConditionEvaluator(i, sldDataContainerProvider, typeService, tenantConfig)));
    }


    public static ConditionalAuditChangeFilter fromConfigText(String configuration, SLDDataContainerProvider sldDataContainerProvider, TypeService typeService, ConfigIntf tenantConfig)
    {
        try
        {
            InputStream is = IOUtils.toInputStream(configuration, Charset.defaultCharset());
            try
            {
                ConditionalAuditChangeFilter conditionalAuditChangeFilter = new ConditionalAuditChangeFilter(is, sldDataContainerProvider, typeService, tenantConfig);
                if(is != null)
                {
                    is.close();
                }
                return conditionalAuditChangeFilter;
            }
            catch(Throwable throwable)
            {
                if(is != null)
                {
                    try
                    {
                        is.close();
                    }
                    catch(Throwable throwable1)
                    {
                        throwable.addSuppressed(throwable1);
                    }
                }
                throw throwable;
            }
        }
        catch(IOException e)
        {
            throw new ConditionalAuditException(e);
        }
    }


    public boolean ignoreAudit(AuditableChange change)
    {
        return this.evaluators.stream().anyMatch(i -> i.ignoreAudit(change));
    }
}
