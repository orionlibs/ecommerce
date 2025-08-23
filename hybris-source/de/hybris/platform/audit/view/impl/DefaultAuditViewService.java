package de.hybris.platform.audit.view.impl;

import de.hybris.platform.audit.TypeAuditReportConfig;
import de.hybris.platform.audit.internal.config.AuditReportConfig;
import de.hybris.platform.audit.internal.config.Type;
import de.hybris.platform.audit.provider.AuditRecordsProvider;
import de.hybris.platform.audit.provider.internal.resolver.VirtualReferenceExpressionParser;
import de.hybris.platform.audit.view.AuditEventProvider;
import de.hybris.platform.audit.view.AuditReportItemNameResolver;
import de.hybris.platform.audit.view.AuditViewService;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.persistence.audit.gateway.AuditRecord;
import de.hybris.platform.servicelayer.exceptions.ModelLoadingException;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.servicelayer.internal.converter.ConverterRegistry;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Stream;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultAuditViewService implements AuditViewService
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultAuditViewService.class);
    public static final String DEFAULT_CHANGING_USER = "system";
    public static final String CHANGED_OBFUSCATED_ATTRIBUTES_LIST = "changedObfuscatedAttributes";
    private ModelService modelService;
    private TypeService typeService;
    private AuditEventProvider auditEventProvider;
    private ConverterRegistry converterRegistry;
    private VirtualReferenceExpressionParser virtualReferenceExpressionParser;
    private AuditReportItemNameResolver auditReportItemNameResolver;
    private boolean loggingRecordsAndEvents = false;


    public Stream<ReportView> getViewOn(TypeAuditReportConfig config)
    {
        Objects.requireNonNull(config, "config is required");
        try
        {
            validateInputItemForReport(config.getReportConfig(), config.getRootTypePk());
            Stream<AuditEvent> events = getStreamOfAuditEvents(config);
            AuditViewProducer auditViewProducer = new AuditViewProducer(events, config, this.typeService, this.modelService, this.converterRegistry, this.virtualReferenceExpressionParser, this.auditReportItemNameResolver);
            return auditViewProducer.getView();
        }
        catch(Exception e)
        {
            LOG.error(e.getMessage(), e);
            throw e;
        }
    }


    @Deprecated(since = "ages", forRemoval = true)
    public void setLoggingRecordsAndEvents(boolean loggingRecordsAndEvents)
    {
        this.loggingRecordsAndEvents = loggingRecordsAndEvents;
    }


    private Stream<AuditEvent> getStreamOfAuditEvents(TypeAuditReportConfig config)
    {
        AuditRecordsProvider provider = getProviderForConfig(config.getReportConfig());
        Stream<AuditRecord> records = provider.getRecords(config);
        if(this.loggingRecordsAndEvents)
        {
            records = records.peek(auditRecord -> LOG.info("record for type {}, timestamp {}", auditRecord.getType(), Long.valueOf(auditRecord.getTimestamp().toInstant().toEpochMilli())));
        }
        Stream<AuditEvent> eventsForAuditRows = this.auditEventProvider.getEventsForAuditRows(records);
        if(this.loggingRecordsAndEvents)
        {
            eventsForAuditRows = eventsForAuditRows.peek(auditEvent -> LOG.info("event for type {}, timestamp {}", auditEvent.getType(), Long.valueOf(auditEvent.getTimestamp().toInstant().toEpochMilli())));
        }
        return eventsForAuditRows;
    }


    private void validateInputItemForReport(AuditReportConfig config, PK rootTypePk)
    {
        Type reportRootType = config.getGivenRootType();
        ItemModel targetItemForReport = tryToLoadFromModelService(rootTypePk);
        if(targetItemForReport == null)
        {
            return;
        }
        String targetItemType = this.modelService.getModelType(targetItemForReport);
        if(!targetItemType.equals(reportRootType.getCode()) &&
                        targetItemSuperTypeNotMatchesConfiguration(reportRootType, targetItemType))
        {
            throw new SystemException("Failed to execute '" + config.getName() + "' audit report for item with pk: " + rootTypePk + " of type: '" + targetItemType + "'. Report requires '" + reportRootType
                            .getCode() + "' root type.");
        }
    }


    private ItemModel tryToLoadFromModelService(PK targetPk)
    {
        try
        {
            return (ItemModel)this.modelService.get(targetPk);
        }
        catch(ModelLoadingException e)
        {
            LOG.debug("Model for pk: " + targetPk + " not found", (Throwable)e);
            return null;
        }
    }


    private boolean targetItemSuperTypeNotMatchesConfiguration(Type configuredRootType, String rootItemType)
    {
        ComposedTypeModel typeForCode = this.typeService.getComposedTypeForCode(rootItemType);
        Collection<ComposedTypeModel> allSuperTypes = typeForCode.getAllSuperTypes();
        return allSuperTypes.stream().map(TypeModel::getCode).noneMatch(t -> t.equals(configuredRootType.getCode()));
    }


    private AuditRecordsProvider getProviderForConfig(AuditReportConfig config)
    {
        String providerBeanId = config.getAuditRecordsProvider();
        if(StringUtils.isEmpty(providerBeanId))
        {
            throw new IllegalStateException("Report cannot be generated. AudiRecordsProvider not set in the configuration.");
        }
        return (AuditRecordsProvider)Registry.getApplicationContext().getBean(providerBeanId, AuditRecordsProvider.class);
    }


    private static boolean shouldDisplayEventInReport(AuditEvent auditEvent)
    {
        return (auditEvent.getEventType() != EventType.DELETION);
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    @Required
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }


    @Required
    public void setAuditEventProvider(AuditEventProvider auditEventProvider)
    {
        this.auditEventProvider = auditEventProvider;
    }


    @Required
    public void setConverterRegistry(ConverterRegistry converterRegistry)
    {
        this.converterRegistry = converterRegistry;
    }


    @Required
    public void setVirtualReferenceExpressionParser(VirtualReferenceExpressionParser virtualReferenceExpressionParser)
    {
        this.virtualReferenceExpressionParser = virtualReferenceExpressionParser;
    }


    @Required
    public void setAuditReportItemNameResolver(AuditReportItemNameResolver auditReportItemNameResolver)
    {
        this.auditReportItemNameResolver = auditReportItemNameResolver;
    }
}
