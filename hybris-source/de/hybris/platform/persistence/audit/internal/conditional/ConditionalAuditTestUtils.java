package de.hybris.platform.persistence.audit.internal.conditional;

import com.google.common.collect.Lists;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.directpersistence.cache.SLDDataContainer;
import de.hybris.platform.directpersistence.cache.SLDDataContainerProvider;
import de.hybris.platform.persistence.audit.AuditChangeFilter;
import de.hybris.platform.persistence.audit.gateway.AuditRecord;
import de.hybris.platform.persistence.audit.gateway.AuditSearchQuery;
import de.hybris.platform.persistence.audit.gateway.ReadAuditGateway;
import de.hybris.platform.persistence.property.TypeInfoMap;
import de.hybris.platform.servicelayer.type.TypeService;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.mockito.BDDMockito;
import org.mockito.Mockito;

public class ConditionalAuditTestUtils
{
    private static final PK typePk = PK.createFixedUUIDPK(2, 2L);


    public static List<AuditRecord> getAuditRecordsFor(String type, PK pk)
    {
        ReadAuditGateway readAuditGateway = (ReadAuditGateway)Registry.getApplicationContext().getBean("readAuditGateway", ReadAuditGateway.class);
        return (List<AuditRecord>)readAuditGateway.search(AuditSearchQuery.forType(type).withPkSearchRules(new PK[] {pk}).build()).collect(Collectors.toList());
    }


    public static AuditChangeFilter auditTitlesNotEndingWithAudit1andAudit2(SLDDataContainerProvider sldDataContainerProvider, TypeService typeService)
    {
        return (AuditChangeFilter)new ConditionalAuditChangeFilter("audit.test/conditional-audit-address-title.xml", sldDataContainerProvider, typeService,
                        Registry.getCurrentTenant().getConfig());
    }


    public static SLDDataContainer createDataContainer(String typeCode, PK pk, Map<String, Object> attributes)
    {
        TypeInfoMap typeInfoMap = (TypeInfoMap)Mockito.mock(TypeInfoMap.class);
        BDDMockito.given(typeInfoMap.getCode()).willReturn(typeCode);
        List<SLDDataContainer.AttributeValue> containerAttributes = (List<SLDDataContainer.AttributeValue>)attributes.entrySet().stream().map(i -> newAttribute((String)i.getKey(), i.getValue())).collect(Collectors.toList());
        SLDDataContainer container = SLDDataContainer.builder().withPk(pk).withTypePk(typePk).withTypeInfoMap(typeInfoMap).withAttributes(Lists.newArrayList(containerAttributes)).withVersion(Long.valueOf(1L).longValue()).build();
        return container;
    }


    private static SLDDataContainer.AttributeValue newAttribute(String attr, Object value)
    {
        return new SLDDataContainer.AttributeValue(attr, value);
    }
}
