package de.hybris.platform.audit;

import de.hybris.platform.audit.internal.config.AtomicAttribute;
import de.hybris.platform.audit.internal.config.AuditConfigService;
import de.hybris.platform.audit.internal.config.AuditReportConfig;
import de.hybris.platform.audit.internal.config.ReferenceAttribute;
import de.hybris.platform.audit.internal.config.RelationAttribute;
import de.hybris.platform.audit.internal.config.ResolvesBy;
import de.hybris.platform.audit.internal.config.Type;
import de.hybris.platform.audit.internal.config.VirtualAttribute;
import de.hybris.platform.audit.internal.config.validation.AuditReportConfigValidatorTest;
import de.hybris.platform.catalog.model.CatalogUnawareMediaModel;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.persistence.audit.AuditType;
import de.hybris.platform.persistence.audit.gateway.AuditRecord;
import de.hybris.platform.persistence.audit.gateway.WriteAuditGateway;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.testframework.seed.TestDataCreator;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import org.apache.commons.io.IOUtils;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.Condition;
import org.assertj.core.groups.Tuple;

public class AuditTestHelper
{
    private final Collection<Object> models = new HashSet();


    public UserModel prepareTestDataForIntegrationTest() throws InterruptedException
    {
        ModelService modelService = getModelService();
        TestDataCreator creator = new TestDataCreator(modelService);
        UserModel user1 = creator.createUser("adam", "Adam");
        this.models.add(user1);
        TitleModel title1 = creator.createTitle("Mr", "Mister");
        this.models.add(title1);
        TitleModel title2 = creator.createTitle("Engr.", "Engineer");
        this.models.add(title2);
        AddressModel address1 = creator.createAddress("Sosnowiec", "Moniuszki", (ItemModel)user1);
        this.models.add(address1);
        AddressModel address2 = creator.createAddress("Tokyo", "Konichiwa", (ItemModel)user1);
        this.models.add(address2);
        AddressModel address3 = creator.createAddress("New York", "55th St.", (ItemModel)user1);
        this.models.add(address3);
        address1.setTitle(title1);
        address2.setTitle(title2);
        address3.setTitle(title1);
        user1.setDefaultPaymentAddress(address1);
        user1.setDefaultShipmentAddress(address3);
        MediaModel testMedia = (MediaModel)getModelService().create(CatalogUnawareMediaModel.class);
        this.models.add(testMedia);
        testMedia.setCode("nice profile picture of me");
        user1.setProfilePicture(testMedia);
        modelService.save(title1);
        modelService.save(title2);
        modelService.save(address1);
        modelService.save(address2);
        modelService.save(address3);
        modelService.save(testMedia);
        modelService.save(user1);
        WriteAuditGateway readAuditRecordsDAO = getWriteAuditGateway();
        readAuditRecordsDAO.removeAuditRecordsForType("User");
        readAuditRecordsDAO.removeAuditRecordsForType("Address");
        readAuditRecordsDAO.removeAuditRecordsForType("CatalogUnawareMedia");
        readAuditRecordsDAO.removeAuditRecordsForType("Title");
        user1.setName("SomeBetterNameForUser");
        modelService.save(user1);
        Thread.sleep(50L);
        address1.setStreetname("Chopina");
        modelService.save(address1);
        testMedia.setCode("ugly picture of me");
        modelService.save(testMedia);
        UserModel user2 = creator.createUser("tom", "Tommy");
        this.models.add(user2);
        AddressModel address4 = creator.createAddress("Warsaw", "Krakowskie Przedmiescie", (ItemModel)user2);
        this.models.add(address4);
        address4.setStreetnumber("1");
        modelService.save(address4);
        TitleModel historicalTitle = creator.createTitle("Sir", "Sir");
        this.models.add(historicalTitle);
        AddressModel historicalAddress = (AddressModel)modelService.create(AddressModel.class);
        this.models.add(historicalAddress);
        historicalAddress.setTitle(historicalTitle);
        historicalAddress.setTown("Krakow");
        historicalAddress.setStreetname("Rynek");
        historicalAddress.setOwner((ItemModel)user1);
        modelService.save(historicalAddress);
        modelService.remove(historicalTitle);
        modelService.remove(historicalAddress);
        modelService.remove(address3);
        return user1;
    }


    public AuditReportConfig createTestConfigForIntegrationTest()
    {
        Type title = Type.builder().withCode("Title").withAtomicAttributes(new AtomicAttribute[] {AtomicAttribute.builder().withQualifier("code").build(), AtomicAttribute.builder().withQualifier("name").build()}).build();
        Type address = Type.builder().withCode("Address").withAtomicAttributes(new AtomicAttribute[] {AtomicAttribute.builder().withQualifier("town").build(), AtomicAttribute.builder().withQualifier("streetname").build()})
                        .withReferenceAttributes(new ReferenceAttribute[] {ReferenceAttribute.builder().withQualifier("title").withType(title).withResolvesBy(ResolvesBy.builder().withResolverBeanId("typeReferencesResolver").withExpression("title").build()).build()}).build();
        Type media = Type.builder().withCode("Media").withAtomicAttributes(new AtomicAttribute[] {AtomicAttribute.builder().withQualifier("code").build()}).build();
        Type userGroup = Type.builder().withCode("UserGroup").build();
        Type user = Type.builder().withCode("User").withAtomicAttributes(new AtomicAttribute[] {AtomicAttribute.builder().withQualifier("uid").build(), AtomicAttribute.builder().withQualifier("name").build()}).withReferenceAttributes(
                                        new ReferenceAttribute[] {ReferenceAttribute.builder().withQualifier("defaultPaymentAddress").withType(address).withResolvesBy(ResolvesBy.builder().withResolverBeanId("typeReferencesResolver").withExpression("defaultPaymentAddress").build()).build(),
                                                        ReferenceAttribute.builder().withQualifier("defaultShipmentAddress").withType(address).withResolvesBy(ResolvesBy.builder().withResolverBeanId("typeReferencesResolver").withExpression("defaultShipmentAddress").build()).build(),
                                                        ReferenceAttribute.builder().withQualifier("profilepicture").withType(media).withResolvesBy(ResolvesBy.builder().withResolverBeanId("typeReferencesResolver").withExpression("profilepicture").build()).build()})
                        .withRelationAttributes(new RelationAttribute[] {RelationAttribute.builder().withTargetType(userGroup).withRelation("PrincipalGroupRelation").withResolvesBy(ResolvesBy.builder().withResolverBeanId("manyToManyReferencesResolver").build()).build()}).build();
        AuditReportConfig reportConfig = AuditReportConfig.builder().withGivenRootType(user).withName("testConfig").withAuditRecordsProvider("auditRecordsProvider").withTypes(new Type[] {user, address, title, media, userGroup}).build();
        return reportConfig;
    }


    public AuditReportConfig createTestConfigWithVirtualAttributeForIntegrationTest()
    {
        Type title = Type.builder().withCode("Title").withAtomicAttributes(new AtomicAttribute[] {AtomicAttribute.builder().withQualifier("code").build(), AtomicAttribute.builder().withQualifier("name").build()}).build();
        Type address = Type.builder().withCode("Address").withAtomicAttributes(new AtomicAttribute[] {AtomicAttribute.builder().withQualifier("town").build(), AtomicAttribute.builder().withQualifier("streetname").build()})
                        .withReferenceAttributes(new ReferenceAttribute[] {ReferenceAttribute.builder().withQualifier("title").withType(title).withResolvesBy(ResolvesBy.builder().withResolverBeanId("typeReferencesResolver").withExpression("title").build()).build()}).build();
        Type media = Type.builder().withCode("Media").withAtomicAttributes(new AtomicAttribute[] {AtomicAttribute.builder().withQualifier("code").build()}).build();
        Type userGroup = Type.builder().withCode("UserGroup").build();
        Type user = Type.builder().withCode("User").withAtomicAttributes(new AtomicAttribute[] {AtomicAttribute.builder().withQualifier("uid").build(), AtomicAttribute.builder().withQualifier("name").build()})
                        .withReferenceAttributes(new ReferenceAttribute[] {ReferenceAttribute.builder().withQualifier("profilepicture").withType(media).withResolvesBy(ResolvesBy.builder().withResolverBeanId("typeReferencesResolver").withExpression("profilepicture").build()).build()})
                        .withVirtualAttributes(new VirtualAttribute[] {VirtualAttribute.builder().withExpression("ownedAddresses").withMany(Boolean.TRUE).withType(address).withResolvesBy(ResolvesBy.builder().withExpression("owner").withResolverBeanId("virtualReferencesResolver").build()).build()})
                        .withRelationAttributes(new RelationAttribute[] {RelationAttribute.builder().withTargetType(userGroup).withRelation("PrincipalGroupRelation").withResolvesBy(ResolvesBy.builder().withResolverBeanId("manyToManyReferencesResolver").build()).build()}).build();
        AuditReportConfig reportConfig = AuditReportConfig.builder().withGivenRootType(user).withName("testConfig").withAuditRecordsProvider("auditRecordsProvider").withTypes(new Type[] {user, address, title, media, userGroup}).build();
        return reportConfig;
    }


    public void clearAuditDataForTypes(String... types)
    {
        WriteAuditGateway writeAuditGateway = getWriteAuditGateway();
        for(String type : types)
        {
            writeAuditGateway.removeAuditRecordsForType(type);
        }
    }


    private static ModelService getModelService()
    {
        return (ModelService)Registry.getApplicationContext().getBean("modelService", ModelService.class);
    }


    private static WriteAuditGateway getWriteAuditGateway()
    {
        return (WriteAuditGateway)Registry.getApplicationContext().getBean("writeAuditGateway", WriteAuditGateway.class);
    }


    public static Map<String, Object> getAuditRecordsAttributes(AuditRecord auditRecord1, String langIsoCode)
    {
        if(auditRecord1.getAuditType() == AuditType.DELETION || auditRecord1.getAuditType() == AuditType.CURRENT)
        {
            return auditRecord1.getAttributesBeforeOperation(langIsoCode);
        }
        return auditRecord1.getAttributesAfterOperation(langIsoCode);
    }


    public static Map<String, Object> getAuditRecordsAttributes(AuditRecord auditRecord1)
    {
        if(auditRecord1.getAuditType() == AuditType.DELETION || auditRecord1.getAuditType() == AuditType.CURRENT)
        {
            return auditRecord1.getAttributesBeforeOperation();
        }
        return auditRecord1.getAttributesAfterOperation();
    }


    public static Condition<List<? extends Map<String, Object>>> noDuplicatedReportEntries()
    {
        return (Condition<List<? extends Map<String, Object>>>)new DuplicatedEntriesCondition();
    }


    public <T> T createItem(Class<T> itemClass)
    {
        T item = (T)getModelService().create(itemClass);
        this.models.add(item);
        return item;
    }


    public <T> T createItem(Supplier<T> supplier)
    {
        T item = supplier.get();
        this.models.add(item);
        return item;
    }


    public void removeCreatedItems()
    {
        getModelService().removeAll(this.models);
    }


    public static Function<Map<String, Object>, Object> extractingRecursiveMapAttribute(String... attributePath)
    {
        return stringObjectMap -> {
            Map<String, Object> current = stringObjectMap;
            for(int i = 0; i < attributePath.length; i++)
            {
                if(current == null)
                {
                    return null;
                }
                String attribute = attributePath[i];
                if(i == attributePath.length - 1)
                {
                    return current.get(attribute);
                }
                current = (Map<String, Object>)current.get(attribute);
            }
            return null;
        };
    }


    @SafeVarargs
    public static Tuple extractRecursiveMapAttributes(Map<String, Object> title, List<String>... attributePaths)
    {
        List<Object> values = new ArrayList();
        for(List<String> attributePath : attributePaths)
        {
            Map<String, Object> current = title;
            for(int i = 0; i < attributePath.size(); i++)
            {
                String attribute = attributePath.get(i);
                if(i == attributePath.size() - 1)
                {
                    values.add(current.get(attribute));
                }
                else
                {
                    current = (Map<String, Object>)current.get(attribute);
                }
            }
        }
        return Assertions.tuple(values.toArray());
    }


    public static <T extends AuditRecord> List<T> sortRecords(List<T> auditRecords)
    {
        List<T> sortedAuditRecords = new ArrayList<>(auditRecords);
        sortedAuditRecords.sort(Comparator.comparing(AuditRecord::getAuditType, AuditTestHelper::compareAuditType)
                        .thenComparing(AuditRecord::getTimestamp).thenComparing(AuditRecord::getVersion));
        return sortedAuditRecords;
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


    public AuditReportConfig loadConfigFromFile(String file, String configName)
    {
        try
        {
            InputStream resourceAsStream = AuditReportConfigValidatorTest.class.getClassLoader().getResourceAsStream(file);
            try
            {
                String xml = IOUtils.toString(resourceAsStream, StandardCharsets.UTF_8);
                AuditConfigService auditConfigService = getAuditConfigService();
                auditConfigService.storeConfiguration(configName, xml);
                AuditReportConfig auditReportConfig = auditConfigService.getConfigForName(configName);
                if(resourceAsStream != null)
                {
                    resourceAsStream.close();
                }
                return auditReportConfig;
            }
            catch(Throwable throwable)
            {
                if(resourceAsStream != null)
                {
                    try
                    {
                        resourceAsStream.close();
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
            throw new RuntimeException(e);
        }
    }


    public static AuditConfigService getAuditConfigService()
    {
        return (AuditConfigService)Registry.getApplicationContext().getBean("auditConfigService", AuditConfigService.class);
    }
}
