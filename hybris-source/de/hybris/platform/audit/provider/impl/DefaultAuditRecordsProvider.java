package de.hybris.platform.audit.provider.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.google.common.collect.Streams;
import de.hybris.platform.audit.TypeAuditReportConfig;
import de.hybris.platform.audit.internal.ModelAuditRecordConverter;
import de.hybris.platform.audit.internal.config.AbstractTypedAttribute;
import de.hybris.platform.audit.internal.config.Type;
import de.hybris.platform.audit.provider.AuditRecordsProvider;
import de.hybris.platform.audit.provider.internal.resolver.AuditRecordInternalIndex;
import de.hybris.platform.audit.provider.internal.resolver.ReferencesResolver;
import de.hybris.platform.audit.provider.internal.resolver.impl.AuditTypeContext;
import de.hybris.platform.core.PK;
import de.hybris.platform.directpersistence.cache.SLDDataContainer;
import de.hybris.platform.directpersistence.cache.SLDDataContainerProvider;
import de.hybris.platform.persistence.audit.AuditType;
import de.hybris.platform.persistence.audit.gateway.AuditRecord;
import de.hybris.platform.persistence.audit.gateway.AuditSearchQuery;
import de.hybris.platform.persistence.audit.gateway.ReadAuditGateway;
import de.hybris.platform.persistence.audit.internal.AuditEnablementService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultAuditRecordsProvider implements AuditRecordsProvider
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultAuditRecordsProvider.class);
    private ReadAuditGateway readAuditGateway;
    private SLDDataContainerProvider sldDataContainerProvider;
    private ModelAuditRecordConverter converter;
    private AuditEnablementService auditEnablementService;


    public Stream<AuditRecord> getRecords(TypeAuditReportConfig config)
    {
        Map<Type, Integer> typesPriority = createTypesPriority(config);
        checkAuditingEnabledForConfiguredTypes(config, typesPriority);
        AuditRecordProviderController controller = new AuditRecordProviderController(this, typesPriority);
        Type givenRootType = config.getReportConfig().getGivenRootType();
        controller.queueOperation((Operation)new InitialOperation(givenRootType, config.getRootTypePk()));
        while(controller.hasNextType())
        {
            Pair<Type, Set<Operation>> nextType = controller.getNextType();
            Type type = (Type)nextType.getLeft();
            Set<Operation> operations = (Set<Operation>)nextType.getRight();
            LOG.debug("processing type {} for {} operations", type.getCode(), Integer.valueOf(operations.size()));
            processOperations(controller, type, config, operations);
            searchForReferencedPks(controller, type);
        }
        List<AuditRecord> result = new ArrayList<>(controller.getRecords());
        result.sort(Comparator.comparing(AuditRecord::getAuditType, DefaultAuditRecordsProvider::compareAuditType)
                        .thenComparing(AuditRecord::getTimestamp)
                        .thenComparing(AuditRecord::getVersion)
                        .thenComparing(AuditRecord::getType));
        return result.stream();
    }


    private void checkAuditingEnabledForConfiguredTypes(TypeAuditReportConfig config, Map<Type, Integer> typesPriority)
    {
        if(!config.isReportOnlyLastState())
        {
            for(Type t : typesPriority.keySet())
            {
                if(!this.auditEnablementService.isAuditEnabledForType(t.getCode()))
                {
                    LOG.warn("Executing report {} with full audit enabled, but type {} has auditing disabled!", config
                                    .getReportConfig().getName(), t.getCode());
                }
            }
        }
    }


    private void processOperations(AuditRecordProviderController controller, Type type, TypeAuditReportConfig typeConfig, Set<Operation> operations)
    {
        Map<Operation.OperationType, Set<Operation>> collect = (Map<Operation.OperationType, Set<Operation>>)operations.stream().collect(Collectors.groupingBy(Operation::getType, java.util.TreeMap::new,
                        Collectors.toSet()));
        for(Map.Entry<Operation.OperationType, Set<Operation>> operationsPerOperationType : collect.entrySet())
        {
            Operation.OperationType operationType = operationsPerOperationType.getKey();
            if(operationType == Operation.OperationType.INITIAL)
            {
                processInitialOperations(controller, type, operationsPerOperationType.getValue());
                continue;
            }
            Map<ReferencesResolver, Set<Operation>> operationsPerResolversMap = (Map<ReferencesResolver, Set<Operation>>)((Set)operationsPerOperationType.getValue()).stream().collect(
                            Collectors.groupingBy(o -> o.getAttribute().getResolvesBy().getResolverBean(),
                                            Collectors.toSet()));
            for(Map.Entry<ReferencesResolver, Set<Operation>> operationsPerResolver : operationsPerResolversMap.entrySet())
            {
                ReferencesResolver resolverBean = operationsPerResolver.getKey();
                processOperationsWithResolver(controller, operationsPerResolver.getValue(), typeConfig, resolverBean);
            }
        }
    }


    private void processInitialOperations(AuditRecordProviderController controller, Type type, Set<Operation> operation)
    {
        Set<PK> basePks = (Set<PK>)operation.stream().map(Operation::getBasePks).collect(HashSet::new, Set::addAll, Set::addAll);
        LOG.debug("processing operation for type {}, operationType {}, pks count: {}", new Object[] {type.getCode(), Operation.OperationType.INITIAL,
                        Integer.valueOf(basePks.size())});
        getAuditRecords(controller, type, basePks);
    }


    private void processOperationsWithResolver(AuditRecordProviderController controller, Set<Operation> operations, TypeAuditReportConfig typeConfig, ReferencesResolver resolver)
    {
        Map<AbstractTypedAttribute, AuditTypeContext<AuditRecord>> attributes = new HashMap<>();
        Set<AuditRecord> baseAuditRecords = new HashSet<>();
        for(Operation o : operations)
        {
            attributes.computeIfAbsent(o.getAttribute(), abstractTypedAttribute -> new AuditTypeContext((AuditRecordInternalIndex)controller, o.getTargetType().getCode(), o.getBaseType().getCode(), o.getBasePks(), typeConfig.getLangIsoCodes()));
            Objects.requireNonNull(controller);
            Objects.requireNonNull(baseAuditRecords);
            o.getBasePks().stream().map(controller::getRecords).forEach(baseAuditRecords::addAll);
        }
        Collection<ReferencesResolver.ResolveResult> resolveResults = resolver.resolve(typeConfig, attributes, baseAuditRecords);
        resolveResults.forEach(res -> processResolverResult(controller, res));
    }


    private Map<Type, Integer> createTypesPriority(TypeAuditReportConfig config)
    {
        Map<Type, Integer> p = new HashMap<>();
        getTypeLevel(config.getReportConfig().getGivenRootType(), 0, new HashSet<>(), p);
        return Collections.unmodifiableMap(p);
    }


    private void getTypeLevel(Type type, int level, Set<Type> typesChecked, Map<Type, Integer> priorities)
    {
        typesChecked.add(type);
        priorities.compute(type, (t, i) -> Integer.valueOf((i == null) ? level : Math.max(i.intValue(), level)));
        Stream.<List>of(new List[] {type.getReferenceAttributes(), type.getVirtualAttributes(), type.getRelationAttributes()}).flatMap(Collection::stream).map(AbstractTypedAttribute::getType).distinct()
                        .filter(type1 -> !typesChecked.contains(type1))
                        .forEach(type1 -> getTypeLevel(type1, level + 1, typesChecked, priorities));
        typesChecked.remove(type);
    }


    private void getAuditRecords(AuditRecordProviderController controller, Type type, Set<PK> pks)
    {
        Set<PK> pksWithoutRecords = (Set<PK>)pks.stream().filter(pk -> controller.getRecords(pk).isEmpty()).collect(Collectors.toSet());
        controller.addRecords(getCurrentAuditRecords(type, pksWithoutRecords));
        controller.addRecords(getHistoricalAuditRecord(type, pksWithoutRecords));
    }


    private List<AuditRecord> getCurrentAuditRecords(Type type, Set<PK> pks)
    {
        List<SLDDataContainer> sldDataContainer = this.sldDataContainerProvider.getAll(new ArrayList<>(pks));
        if(sldDataContainer == null)
        {
            return Collections.emptyList();
        }
        return (List<AuditRecord>)sldDataContainer.stream().filter(sdc -> !Objects.isNull(sdc)).map(sdc -> this.converter.toAuditRecord(sdc, type))
                        .collect(Collectors.toList());
    }


    private List<AuditRecord> getHistoricalAuditRecord(Type type, Set<PK> pks)
    {
        return (List<AuditRecord>)this.readAuditGateway.search(AuditSearchQuery.forType(type).withPkSearchRules((PK[])Iterables.toArray(pks, PK.class)).build())
                        .collect(Collectors.toList());
    }


    private void searchForReferencedPks(AuditRecordProviderController controller, Type baseType)
    {
        Set<PK> pks = controller.getCollectedPksForType(baseType);
        Set<PK> uncheckedPks = (Set<PK>)pks.stream().filter(pk -> !controller.hasPkBeenChecked(pk)).collect(Collectors.toSet());
        LOG.debug("checking attributes for type {} with pks {} (filtered from {})", new Object[] {baseType.getCode(), uncheckedPks, pks});
        if(uncheckedPks.isEmpty())
        {
            return;
        }
        Stream<Operation> typeRefOps = checkAttributes(baseType, baseType.getReferenceAttributes(), uncheckedPks, Operation.OperationType.REFERENCE);
        Stream<Operation> virtualRefOps = checkAttributes(baseType, baseType.getVirtualAttributes(), uncheckedPks, Operation.OperationType.VIRTUAL_REFERENCE);
        Stream<Operation> relationRefOps = checkAttributes(baseType, baseType.getRelationAttributes(), uncheckedPks, Operation.OperationType.RELATION_REFERENCE);
        Objects.requireNonNull(controller);
        Streams.concat(new Stream[] {typeRefOps, virtualRefOps, relationRefOps}).forEach(controller::queueOperation);
        controller.markPksAsChecked(uncheckedPks);
    }


    private static Stream<Operation> checkAttributes(Type baseType, List<? extends AbstractTypedAttribute> attributes, Set<PK> basePks, Operation.OperationType operationType)
    {
        return attributes.stream().map(attr -> {
            Preconditions.checkNotNull(attr.getType(), "No correct type configured for attribute: '" + attr.getDefaultName());
            return new Operation(attr.getType(), baseType, basePks, attr, operationType);
        });
    }


    private void processResolverResult(AuditRecordProviderController controller, ReferencesResolver.ResolveResult res)
    {
        Collection<AuditRecord> records = res.getRecords();
        controller.addRecords(records);
    }


    private static int compareAuditType(AuditType o1, AuditType o2)
    {
        if(o1 != o2)
        {
            if(o1 == AuditType.CURRENT)
            {
                return 1;
            }
            if(o2 == AuditType.CURRENT)
            {
                return -1;
            }
        }
        return 0;
    }


    @Required
    public void setConverter(ModelAuditRecordConverter converter)
    {
        this.converter = converter;
    }


    @Required
    public void setSldDataContainerProvider(SLDDataContainerProvider sldDataContainerProvider)
    {
        this.sldDataContainerProvider = sldDataContainerProvider;
    }


    @Required
    public void setReadAuditGateway(ReadAuditGateway readAuditGateway)
    {
        this.readAuditGateway = readAuditGateway;
    }


    @Required
    public void setAuditEnablementService(AuditEnablementService auditEnablementService)
    {
        this.auditEnablementService = auditEnablementService;
    }
}
