package de.hybris.platform.persistence.property.loader;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import de.hybris.bootstrap.ddl.PropertiesLoader;
import de.hybris.bootstrap.ddl.tools.persistenceinfo.PersistenceInformation;
import de.hybris.bootstrap.ddl.tools.persistenceinfo.TypeSystemRelatedDeployments;
import de.hybris.platform.core.ItemDeployment;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.core.TenantPropertiesLoader;
import de.hybris.platform.persistence.property.TypeInfoMap;
import de.hybris.platform.persistence.property.internal.DeploymentInfoProvider;
import de.hybris.platform.persistence.property.internal.LoadDeploymentInfoResult;
import de.hybris.platform.persistence.property.loader.internal.BatchTypeInfoRepository;
import de.hybris.platform.persistence.property.loader.internal.ConcreteSubtypeFinder;
import de.hybris.platform.persistence.property.loader.internal.TypePKInfoProvider;
import de.hybris.platform.persistence.property.loader.internal.dto.AttributeDTO;
import de.hybris.platform.persistence.property.loader.internal.dto.ComposedTypeDTO;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.apache.commons.lang.StringUtils;

public class BatchTypeInfoMapLoader
{
    private final Tenant tenant;
    private final TypePKInfoProvider typePKInfoProvider;
    private final BatchTypeInfoRepository batchTypeInfoRepository;
    private LoadDeploymentInfoResult loadDeploymentInfoResult;


    public BatchTypeInfoMapLoader(Tenant tenant)
    {
        this.tenant = tenant;
        TypeSystemRelatedDeployments typeSystemRelatedDeployments = getTypeSystemRelatedDeployments(tenant);
        String attributeDescriptorsTable = typeSystemRelatedDeployments.findByName("AttributeDescriptor").getTableName();
        String composedTypesTable = typeSystemRelatedDeployments.findByName("ComposedType").getTableName();
        String atomicTypesTable = typeSystemRelatedDeployments.findByName("AtomicType").getTableName();
        this.batchTypeInfoRepository = new BatchTypeInfoRepository(tenant, attributeDescriptorsTable, composedTypesTable);
        this.typePKInfoProvider = TypePKInfoProvider.buildForTenant(tenant, atomicTypesTable, composedTypesTable);
    }


    public List<TypeInfoMap> loadAllTypes()
    {
        DeploymentInfoProvider deploymentInfoProvider = new DeploymentInfoProvider();
        this.loadDeploymentInfoResult = deploymentInfoProvider.loadAllDeploymentsForTenant(this.tenant);
        Multimap<PK, AttributeDTO> typePkToAttributesMap = getTypePkToAttributesMap();
        List<ComposedTypeDTO> composedTypes = this.batchTypeInfoRepository.findComposedTypes(this.typePKInfoProvider, this.loadDeploymentInfoResult
                        .getDeploymentInfos());
        enrichWithSubtypeInfo(composedTypes);
        return buildTypeInfoMaps(typePkToAttributesMap, composedTypes);
    }


    private TypeSystemRelatedDeployments getTypeSystemRelatedDeployments(Tenant tenant)
    {
        PersistenceInformation persistenceInformation = new PersistenceInformation((DataSource)tenant.getDataSource(), (PropertiesLoader)new TenantPropertiesLoader(tenant));
        return persistenceInformation.getTypeSystemRelatedDeployments(persistenceInformation.getTypeSystemName());
    }


    private List<TypeInfoMap> buildTypeInfoMaps(Multimap<PK, AttributeDTO> typePkToAttributesMap, List<ComposedTypeDTO> composedTypes)
    {
        List<TypeInfoMap> typeInfoMaps = new ArrayList<>();
        for(ComposedTypeDTO composedType : composedTypes)
        {
            TypeInfoMap typeInfoMap;
            PK pk = PK.fromLong(composedType.getPk());
            PK superTypePK = PK.fromLong(composedType.getSuperTypePk());
            String typeCode = composedType.getTypeCode();
            int tc = composedType.getItemTypeCode();
            Boolean isRelationType = composedType.getRelation();
            int modifiers = composedType.getModifiers();
            ItemDeployment deployment = composedType.getDeployment();
            String itemTableName = (deployment == null) ? null : deployment.getDatabaseTableName();
            String propsTable = (deployment == null) ? null : deployment.getDumpPropertyTableName();
            String auditTableName = (deployment == null) ? null : deployment.getAuditTableName();
            Boolean anAbstract = composedType.getAbstract();
            boolean abstractWithoutConcreteSubTypes = composedType.isAbstractWithoutConcreteSubTypes();
            if(abstractWithoutConcreteSubTypes || (itemTableName == null && !anAbstract.booleanValue()))
            {
                typeInfoMap = new TypeInfoMap(pk, superTypePK, typeCode, isRelationType.booleanValue(), modifiers);
            }
            else
            {
                typeInfoMap = new TypeInfoMap(pk, superTypePK, typeCode, tc, isRelationType.booleanValue(), modifiers, itemTableName, (itemTableName != null) ? (itemTableName + itemTableName) : null, (itemTableName != null) ? (itemTableName + itemTableName) : null, propsTable, auditTableName);
            }
            loadAttributedForType(typePkToAttributesMap, pk, typeInfoMap);
            typeInfoMaps.add(typeInfoMap);
            if(composedType.isPropertyTableStatus().booleanValue())
            {
                typeInfoMap.setTablesInitialized();
            }
        }
        return typeInfoMaps;
    }


    private void loadAttributedForType(Multimap<PK, AttributeDTO> typePkToAttributesMap, PK pk, TypeInfoMap typeInfoMap)
    {
        List<String> parsed = new ArrayList<>();
        List<String> skipped = new ArrayList<>();
        for(AttributeDTO attribute : typePkToAttributesMap.get(pk))
        {
            String qualifier = attribute.getQualifier();
            String columnName = attribute.getColumnName();
            String javaClassName = (String)this.typePKInfoProvider.getAtomicPkToJavaClassName().get(attribute.getPersistenceTypePk());
            Class javaClass = getClazz(javaClassName);
            boolean core = attribute.isCore();
            boolean localized = attribute.isLocalized();
            if(attribute.isEncrypted())
            {
                typeInfoMap.addToEncryptedProperties(qualifier);
            }
            if(columnName == null || attribute.isNotForOptimization())
            {
                skipped.add(qualifier);
                continue;
            }
            parsed.add(qualifier);
            typeInfoMap.add(qualifier, columnName, javaClass, core, localized);
        }
    }


    private void enrichWithSubtypeInfo(List<ComposedTypeDTO> composedTypes)
    {
        ConcreteSubtypeFinder concreteSubtypeFinder = ConcreteSubtypeFinder.buildForComposedTypes(composedTypes);
        for(ComposedTypeDTO composedTypeDTO : composedTypes)
        {
            boolean hasConcreteSubtypeForDeployment = concreteSubtypeFinder.hasConcreteSubtypeForDeployment(Long.valueOf(composedTypeDTO.getPk()), composedTypeDTO.getItemTypeCode());
            if(composedTypeDTO.getAbstract().booleanValue() && composedTypeDTO.getDeployment() != null && !hasConcreteSubtypeForDeployment)
            {
                composedTypeDTO.setAbstractWithoutConcreteSubTypes();
                continue;
            }
            composedTypeDTO.setAbstractWithConcreteSubTypes();
        }
    }


    private Class getClazz(String clazz)
    {
        if(StringUtils.isNotEmpty(clazz))
        {
            try
            {
                return Class.forName(clazz);
            }
            catch(ClassNotFoundException e)
            {
                throw new SystemException("Wrong class", e);
            }
        }
        return null;
    }


    public Multimap<PK, AttributeDTO> getTypePkToAttributesMap()
    {
        List<AttributeDTO> attributeDTOs = this.batchTypeInfoRepository.findAttributes();
        ArrayListMultimap arrayListMultimap = ArrayListMultimap.create();
        for(AttributeDTO attribute : attributeDTOs)
        {
            arrayListMultimap.put(PK.fromLong(attribute.getOwnerPk().longValue()), attribute);
        }
        return (Multimap<PK, AttributeDTO>)arrayListMultimap;
    }


    public LoadDeploymentInfoResult getLoadDeploymentInfoResult()
    {
        return this.loadDeploymentInfoResult;
    }
}
