package de.hybris.bootstrap.typesystem;

import de.hybris.bootstrap.util.LocaleHelper;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;

public abstract class YNamespace
{
    private static final Logger LOG = Logger.getLogger(YNamespace.class);
    private final YTypeSystem system;
    private final Map<String, YType> typesMap = (Map<String, YType>)new LinkedCaseInsensitiveMap();
    private final Map<String, Set<YComposedType>> deployedTypesMap = (Map<String, Set<YComposedType>>)new LinkedCaseInsensitiveMap();
    private final Map<String, Set<String>> typeInheritanceMap = (Map<String, Set<String>>)new LinkedCaseInsensitiveMap();
    private final Map<String, Map<String, YAttributeDescriptor>> attributesMap = (Map<String, Map<String, YAttributeDescriptor>>)new LinkedCaseInsensitiveMap();
    private final Map<String, Map<String, YRelationEnd>> attributes2RelationEndMap = (Map<String, Map<String, YRelationEnd>>)new LinkedCaseInsensitiveMap();
    private final Map<String, Set<YIndex>> typeIndexMap = (Map<String, Set<YIndex>>)new LinkedCaseInsensitiveMap();
    private final Map<String, YDeployment> deploymentsNameMap = (Map<String, YDeployment>)new LinkedCaseInsensitiveMap();
    private final Map<String, YDeployment> deploymentsTableNameMap = (Map<String, YDeployment>)new LinkedCaseInsensitiveMap();
    private final Map<Integer, YDeployment> deploymentsTCMap = new LinkedHashMap<>();
    private final Map<String, Map<String, YAttributeDeployment>> attributeDeploymentsMap = (Map<String, Map<String, YAttributeDeployment>>)new LinkedCaseInsensitiveMap();
    private final Map<String, Map<String, YIndexDeployment>> indexeDeploymentsMap = (Map<String, Map<String, YIndexDeployment>>)new LinkedCaseInsensitiveMap();
    private final Map<String, Map<String, YFinder>> findersMap = (Map<String, Map<String, YFinder>>)new LinkedCaseInsensitiveMap();
    private final Map<String, Set<YEnumValue>> enumValuesMap = (Map<String, Set<YEnumValue>>)new LinkedCaseInsensitiveMap();
    private final Map<String, YDBTypeMapping> dbTypeMappings = (Map<String, YDBTypeMapping>)new LinkedCaseInsensitiveMap();
    private final boolean deploymentCheck = Boolean.parseBoolean(System.getProperty("deployment.check", Boolean.TRUE.toString()));
    private static final Map<String, String> MIGRATED_CORE_DEPLOYMENTS_INFO;

    static
    {
        Map<String, String> tempMap = new HashMap<>();
        tempMap.put("Title", "de.hybris.platform.persistence.user.Title");
        tempMap.put("AbstractMedia", "de.hybris.platform.persistence.media.AbstractMedia");
        tempMap.put("Media", "de.hybris.platform.persistence.media.Media");
        tempMap.put("Product", "de.hybris.platform.persistence.product.Product");
        tempMap.put("Unit", "de.hybris.platform.persistence.product.Unit");
        tempMap.put("Region", "de.hybris.platform.persistence.c2l.Region");
        tempMap.put("Country", "de.hybris.platform.persistence.c2l.Country");
        tempMap.put("Language", "de.hybris.platform.persistence.c2l.Language");
        tempMap.put("Currency", "de.hybris.platform.persistence.c2l.Currency");
        tempMap.put("Order", "de.hybris.platform.persistence.order.Order");
        tempMap.put("Cart", "de.hybris.platform.persistence.order.Cart");
        tempMap.put("CartEntry", "de.hybris.platform.persistence.order.CartEntry");
        tempMap.put("OrderEntry", "de.hybris.platform.persistence.order.OrderEntry");
        tempMap.put("Tax", "de.hybris.platform.persistence.order.price.Tax");
        tempMap.put("Discount", "de.hybris.platform.persistence.order.price.Discount");
        tempMap.put("DeliveryMode", "de.hybris.platform.persistence.order.delivery.DeliveryMode");
        tempMap.put("PaymentMode", "de.hybris.platform.persistence.order.payment.PaymentMode");
        tempMap.put("PaymentInfo", "de.hybris.platform.persistence.order.payment.PaymentInfo");
        tempMap.put("User", "de.hybris.platform.persistence.user.User");
        tempMap.put("UserGroup", "de.hybris.platform.persistence.user.UserGroup");
        tempMap.put("UserRight", "de.hybris.platform.persistence.security.UserRight");
        tempMap.put("Address", "de.hybris.platform.persistence.user.Address");
        MIGRATED_CORE_DEPLOYMENTS_INFO = Collections.unmodifiableMap(tempMap);
    }

    public YNamespace(YTypeSystem sys)
    {
        this.system = sys;
    }


    protected void mergeNamespace(YNamespace other)
    {
        for(Map.Entry<String, YDBTypeMapping> entry : other.dbTypeMappings.entrySet())
        {
            if(this.dbTypeMappings.containsKey(entry.getKey()))
            {
                throw new IllegalArgumentException("cannot merge namespace " + other + " into " + this + " due to duplicate db type mappings for '" + (String)entry
                                .getKey() + "' : " + entry
                                .getValue() + "<>" + this.dbTypeMappings
                                .get(entry.getKey()));
            }
            this.dbTypeMappings.put(entry.getKey(), entry.getValue());
        }
        for(Map.Entry<String, YType> entry : other.typesMap.entrySet())
        {
            if(this.typesMap.containsKey(entry.getKey()))
            {
                throw new IllegalArgumentException("cannot merge namespace " + other + " into " + this + " due to duplicate type code '" + (String)entry
                                .getKey() + "' : " + entry
                                .getValue() + "<>" + this.typesMap
                                .get(entry.getKey()));
            }
            this.typesMap.put(entry.getKey(), entry.getValue());
        }
        for(Map.Entry<String, Set<YComposedType>> entry : other.deployedTypesMap.entrySet())
        {
            Set<YComposedType> types = this.deployedTypesMap.get(entry.getKey());
            if(types == null)
            {
                this.deployedTypesMap.put(entry.getKey(), types = new LinkedHashSet<>());
            }
            types.addAll(entry.getValue());
        }
        for(Map.Entry<String, Set<String>> entry : other.typeInheritanceMap.entrySet())
        {
            Set<String> subtypeCodes = this.typeInheritanceMap.get(entry.getKey());
            if(subtypeCodes == null)
            {
                this.typeInheritanceMap.put(entry.getKey(), subtypeCodes = new LinkedHashSet<>(entry.getValue()));
                continue;
            }
            subtypeCodes.addAll(entry.getValue());
        }
        for(Map.Entry<String, Set<YIndex>> entry : other.typeIndexMap.entrySet())
        {
            Set<YIndex> typeIndexes = this.typeIndexMap.get(entry.getKey());
            if(typeIndexes == null)
            {
                this.typeIndexMap.put(entry.getKey(), typeIndexes = new LinkedHashSet<>(entry.getValue()));
                continue;
            }
            Map<String, YIndex> effectivelyCreatedIndexes = new LinkedHashMap<>(typeIndexes.size());
            for(YIndex rootIdx : typeIndexes)
            {
                effectivelyCreatedIndexes.put(rootIdx.getName().toLowerCase(LocaleHelper.getPersistenceLocale()), rootIdx);
            }
            for(YIndex newIdx : entry.getValue())
            {
                boolean shouldAddIndex = newIdx.isReplace();
                if(effectivelyCreatedIndexes.containsKey(newIdx.getName().toLowerCase(LocaleHelper.getPersistenceLocale())))
                {
                    if(newIdx.isReplace())
                    {
                        LOG.info("\n*******************************************\nRedeclared index found, index " + effectivelyCreatedIndexes
                                        .get(newIdx
                                                        .getName().toLowerCase(LocaleHelper.getPersistenceLocale())) + "\n, is overriden by index " + newIdx);
                        if(newIdx.isRemove())
                        {
                            LOG.info("Previous declaration(s) of the index " + newIdx.getName() + " won't be stored into database.\nRedeclaring index is marked as remove = true.");
                        }
                        LOG.info("*******************************************\n");
                    }
                    else
                    {
                        LOG.warn("\n*******************************************\nRedeclared index found, index " + effectivelyCreatedIndexes
                                        .get(newIdx
                                                        .getName().toLowerCase(LocaleHelper.getPersistenceLocale())) + "\n, is overriden by index " + newIdx + "\n, however it is not declared as replaced.\nSecond index declaration will be skipped.");
                        if(newIdx.isRemove())
                        {
                            throw new IllegalArgumentException("Index " + newIdx + " is declared as removed,\n however it is missing a replace = true attribute.");
                        }
                        LOG.warn("*******************************************\n");
                    }
                }
                else
                {
                    shouldAddIndex = true;
                    if(newIdx.isReplace())
                    {
                        LOG.info("\n*******************************************");
                        LOG.info("Index " + newIdx + " is marked as replace \n, however there is no index (by index name) to be replaced by.");
                        if(newIdx.isRemove())
                        {
                            LOG.info("Index " + newIdx
                                            .getName() + " won't be stored into database it is marked as remove = true.");
                        }
                        LOG.info("*******************************************\n");
                    }
                    else if(newIdx.isRemove())
                    {
                        throw new IllegalArgumentException("Index " + newIdx + " is declared as removed,\n however it is missing a replace = true attribute.");
                    }
                }
                if(shouldAddIndex)
                {
                    effectivelyCreatedIndexes.put(newIdx.getName().toLowerCase(LocaleHelper.getPersistenceLocale()), newIdx);
                }
            }
            this.typeIndexMap.put(entry.getKey(), new LinkedHashSet<>(effectivelyCreatedIndexes.values()));
        }
        for(Map.Entry<String, Map<String, YAttributeDescriptor>> entry : other.attributesMap.entrySet())
        {
            LinkedCaseInsensitiveMap<String, YAttributeDescriptor> linkedCaseInsensitiveMap;
            Map<String, YAttributeDescriptor> attributes = this.attributesMap.get(entry.getKey());
            if(attributes == null)
            {
                this.attributesMap.put(entry.getKey(), linkedCaseInsensitiveMap = new LinkedCaseInsensitiveMap());
                linkedCaseInsensitiveMap.putAll(entry.getValue());
                continue;
            }
            for(Map.Entry<String, YAttributeDescriptor> e2 : (Iterable<Map.Entry<String, YAttributeDescriptor>>)((Map)entry.getValue()).entrySet())
            {
                if(linkedCaseInsensitiveMap.containsKey(e2.getKey()))
                {
                    throw new IllegalArgumentException("cannot merge namespace " + other + " into " + this + " due to duplicate attribute '" + e2
                                    .getValue() + "' vs '" + linkedCaseInsensitiveMap
                                    .get(e2.getKey()) + "'");
                }
                linkedCaseInsensitiveMap.put(e2.getKey(), e2.getValue());
            }
        }
        for(Map.Entry<String, Map<String, YRelationEnd>> entry : other.attributes2RelationEndMap.entrySet())
        {
            LinkedCaseInsensitiveMap<String, YRelationEnd> linkedCaseInsensitiveMap;
            Map<String, YRelationEnd> attributeRelationEnds = this.attributes2RelationEndMap.get(entry.getKey());
            if(attributeRelationEnds == null)
            {
                this.attributes2RelationEndMap.put(entry.getKey(), linkedCaseInsensitiveMap = new LinkedCaseInsensitiveMap());
                linkedCaseInsensitiveMap.putAll(entry.getValue());
                continue;
            }
            for(Map.Entry<String, YRelationEnd> e2 : (Iterable<Map.Entry<String, YRelationEnd>>)((Map)entry.getValue()).entrySet())
            {
                if(linkedCaseInsensitiveMap.containsKey(e2.getKey()))
                {
                    throw new IllegalArgumentException("cannot merge namespace " + other + " into " + this + " due to duplicate attribute2relation end '" + e2
                                    .getValue() + "' vs '" + linkedCaseInsensitiveMap
                                    .get(e2.getKey()) + "'");
                }
                linkedCaseInsensitiveMap.put(e2.getKey(), e2.getValue());
            }
        }
        for(Map.Entry<String, Set<YEnumValue>> entry : other.enumValuesMap.entrySet())
        {
            Set<YEnumValue> enums = this.enumValuesMap.get(entry.getKey());
            if(enums == null)
            {
                this.enumValuesMap.put(entry.getKey(), enums = new LinkedHashSet<>(entry.getValue()));
                continue;
            }
            enums.addAll(entry.getValue());
        }
        for(Map.Entry<String, YDeployment> entry : other.deploymentsNameMap.entrySet())
        {
            if(this.deploymentsNameMap.containsKey(entry.getKey()))
            {
                throw new IllegalArgumentException("cannot merge namespace " + other + " into " + this + " due to duplicate deployment name '" + (String)entry
                                .getKey() + "' : " + entry
                                .getValue() + "<>" + this.deploymentsNameMap
                                .get(entry.getKey()));
            }
            putDeploymentName(entry.getKey(), entry.getValue());
        }
        for(Map.Entry<String, YDeployment> entry : other.deploymentsTableNameMap.entrySet())
        {
            if(this.deploymentsTableNameMap.containsKey(entry.getKey()))
            {
                if(((YDeployment)entry.getValue()).getItemTypeCode() != ((YDeployment)this.deploymentsTableNameMap.get(entry.getKey())).getItemTypeCode())
                {
                    throw new IllegalArgumentException("cannot merge namespace " + other + " into " + this + " due to duplicate deployment table name'" + (String)entry
                                    .getKey() + "' : " + entry
                                    .getValue() + "<>" + this.deploymentsTableNameMap
                                    .get(entry.getKey()) + "Use Maintenance/Deployment page at the hybris administration console (hac) for getting a list of all used table names.");
                }
                continue;
            }
            putDeploymentTable(entry.getKey(), entry.getValue());
        }
        for(Map.Entry<Integer, YDeployment> entry : other.deploymentsTCMap.entrySet())
        {
            if(this.deploymentsTCMap.containsKey(entry.getKey()))
            {
                if(this.system.isBuildMode())
                {
                    throw new IllegalArgumentException("cannot merge namespace " + other + " into " + this + " due to duplicate deployment code '" + entry
                                    .getKey() + "' : " + entry
                                    .getValue() + "<>" + this.deploymentsTCMap
                                    .get(entry.getKey()));
                }
                continue;
            }
            putDeploymentTypeCode(entry.getKey(), entry.getValue());
        }
        for(Map.Entry<String, Map<String, YAttributeDeployment>> entry : other.attributeDeploymentsMap.entrySet())
        {
            LinkedCaseInsensitiveMap<String, YAttributeDeployment> linkedCaseInsensitiveMap;
            String deploymentName = entry.getKey();
            Map<String, YAttributeDeployment> attributeDeployments = entry.getValue();
            Map<String, YAttributeDeployment> attributes = this.attributeDeploymentsMap.get(deploymentName);
            if(attributes == null)
            {
                this.attributeDeploymentsMap.put(deploymentName, linkedCaseInsensitiveMap = new LinkedCaseInsensitiveMap());
            }
            for(Map.Entry<String, YAttributeDeployment> e2 : attributeDeployments.entrySet())
            {
                String persistenceQualifier = e2.getKey();
                if(linkedCaseInsensitiveMap.containsKey(persistenceQualifier))
                {
                    throw new IllegalArgumentException("cannot merge namespace " + other + " into " + this + " due to duplicate attribute deployment '" + e2
                                    .getValue() + "' : " + e2
                                    .getValue() + "<>" + linkedCaseInsensitiveMap
                                    .get(persistenceQualifier));
                }
                linkedCaseInsensitiveMap.put(persistenceQualifier, e2.getValue());
            }
        }
        for(Map.Entry<String, Map<String, YIndexDeployment>> entry : other.indexeDeploymentsMap.entrySet())
        {
            LinkedCaseInsensitiveMap<String, YIndexDeployment> linkedCaseInsensitiveMap;
            Map<String, YIndexDeployment> attributes = this.indexeDeploymentsMap.get(entry.getKey());
            if(attributes == null)
            {
                this.indexeDeploymentsMap.put(entry.getKey(), linkedCaseInsensitiveMap = new LinkedCaseInsensitiveMap(entry.getValue()));
                continue;
            }
            for(Map.Entry<String, YIndexDeployment> e2 : (Iterable<Map.Entry<String, YIndexDeployment>>)((Map)entry.getValue()).entrySet())
            {
                if(linkedCaseInsensitiveMap.containsKey(e2.getKey()))
                {
                    throw new IllegalArgumentException("cannot merge namespace " + other + " into " + this + " due to duplicate index deployment '" + e2
                                    .getValue() + "' : " + e2
                                    .getValue() + "<>" + linkedCaseInsensitiveMap
                                    .get(e2.getKey()));
                }
                linkedCaseInsensitiveMap.put(e2.getKey(), e2.getValue());
            }
        }
        for(Map.Entry<String, Map<String, YFinder>> entry : other.findersMap.entrySet())
        {
            LinkedCaseInsensitiveMap<String, YFinder> linkedCaseInsensitiveMap;
            Map<String, YFinder> attributes = this.findersMap.get(entry.getKey());
            if(attributes == null)
            {
                this.findersMap.put(entry.getKey(), linkedCaseInsensitiveMap = new LinkedCaseInsensitiveMap(entry.getValue()));
                continue;
            }
            for(Map.Entry<String, YFinder> e2 : (Iterable<Map.Entry<String, YFinder>>)((Map)entry.getValue()).entrySet())
            {
                if(linkedCaseInsensitiveMap.containsKey(e2.getKey()))
                {
                    throw new IllegalArgumentException("cannot merge namespace " + other + " into " + this + " due to duplicate finder '" + e2
                                    .getValue() + "' : " + e2
                                    .getValue() + "<>" + linkedCaseInsensitiveMap
                                    .get(e2.getKey()));
                }
                linkedCaseInsensitiveMap.put(e2.getKey(), e2.getValue());
            }
        }
    }


    public YTypeSystem getTypeSystem()
    {
        return this.system;
    }


    protected void assureModifieable()
    {
        if(getTypeSystem().isFinalized())
        {
            throw new IllegalStateException("cannot modify namespace " + this + " since enclosing type system is finalized");
        }
    }


    public void registerTypeSystemElement(YNameSpaceElement element) throws IllegalStateException
    {
        assureModifieable();
        if(element instanceof YType)
        {
            YType type = (YType)element;
            YType existing = getOwnType(type.getCode());
            if(existing != null && !existing.equals(element))
            {
                throw new IllegalStateException("duplicate type code '" + type
                                .getCode() + "' in " + element + " and " + existing);
            }
            this.typesMap.put(type.getCode(), type);
            if(type instanceof YComposedType)
            {
                registerInheritanceRelation((YComposedType)type);
                registerDeployment((YComposedType)type);
            }
            else if(element instanceof YAtomicType)
            {
                registerInheritanceRelation((YAtomicType)type);
            }
            if(element instanceof YRelation)
            {
                LinkedCaseInsensitiveMap<String, YRelationEnd> linkedCaseInsensitiveMap2, linkedCaseInsensitiveMap1;
                YRelationEnd srcEnd = ((YRelation)element).getSourceEnd();
                YRelationEnd tgtEnd = ((YRelation)element).getTargetEnd();
                Map<String, YRelationEnd> typeRelationEnds = this.attributes2RelationEndMap.get(tgtEnd.getTypeCode());
                if(typeRelationEnds == null)
                {
                    this.attributes2RelationEndMap.put(tgtEnd.getTypeCode(), linkedCaseInsensitiveMap2 = new LinkedCaseInsensitiveMap());
                }
                linkedCaseInsensitiveMap2.put(srcEnd.getRole(), srcEnd);
                Map map = this.attributes2RelationEndMap.get(srcEnd.getTypeCode());
                if(map == null)
                {
                    this.attributes2RelationEndMap.put(srcEnd.getTypeCode(), linkedCaseInsensitiveMap1 = new LinkedCaseInsensitiveMap());
                }
                linkedCaseInsensitiveMap1.put(tgtEnd.getRole(), tgtEnd);
            }
        }
        else if(element instanceof YDeployment)
        {
            YDeployment depl = (YDeployment)element;
            YDeployment existing = getOwnDeployment(depl.getName());
            if(existing != null && !existing.equals(element))
            {
                if(this.system.isBuildMode())
                {
                    throw new IllegalStateException("duplicate deployment name '" + depl.getName() + "' in " + element + " and " + existing);
                }
                if(this.deploymentCheck)
                {
                    LOG.error("\n*******************************************\nDuplicate deployment name '" + depl
                                    .getName() + "' in " + element + " and " + existing + ".\nPlease adjust the name of " + element
                                    + " at items.xml or advanced-deployments.xml to a name not used by system\n(Use Maintenance/Deployment page at the hybris administration console (hac) for getting a list of all used names).\n*******************************************\n");
                    return;
                }
            }
            if(depl.getTableName() != null)
            {
                YDeployment existingDeploymentForTableName = this.deploymentsTableNameMap.get(depl.getTableName());
                if(existingDeploymentForTableName != null && !element.equals(existingDeploymentForTableName))
                {
                    if(this.system.isBuildMode())
                    {
                        if(hasPrintDuplicateDeploymentError(existingDeploymentForTableName))
                        {
                            throw new IllegalStateException("duplicate deployment table '" + depl.getTableName() + "' in " + element + " and " + existingDeploymentForTableName + "Use Maintenance/Deployment page at the hybris administration console (hac) for getting a list of all used table names.");
                        }
                    }
                    else if(this.deploymentCheck)
                    {
                        if(hasPrintDuplicateDeploymentError(existingDeploymentForTableName))
                        {
                            LOG.error("\n*******************************************\nDuplicated deployment table <<" + depl
                                            .getTableName() + ">> recognized for a deployment <<" + depl + ">> type code [" + depl
                                            .getItemTypeCode() + "].\nDeployment <<" + depl
                                            .getTableName() + ">> is already asigned to type code [" + ((YDeployment)this.deploymentsTableNameMap
                                            .get(depl.getTableName())).getItemTypeCode() + "], deployment <<" + this.deploymentsTableNameMap
                                            .get(depl.getTableName())
                                            + ">>.\n!!!! Please use other deployment table, such a deployment won't be deployable since platform 5.0 version.!!!!\n (Use Maintenance/Deployment page at the hybris administration console (hac) for getting a list of all used table names).\n*******************************************\n");
                        }
                        return;
                    }
                }
                else
                {
                    putDeploymentTable(depl.getTableName(), depl);
                }
            }
            if(!depl.isNonItemDeployment() && !depl.isAbstract())
            {
                YDeployment existing2 = depl.isNonItemDeployment() ? null : getOwnDeployment(depl.getItemTypeCode());
                if(existing2 != null && !existing2.equals(element))
                {
                    if(this.system.isBuildMode())
                    {
                        throw new IllegalStateException("duplicate deployment code " + depl.getItemTypeCode() + " in " + element + " and " + existing2);
                    }
                    if(this.deploymentCheck && hasPrintDuplicateDeploymentError(existing2))
                    {
                        LOG.error("\n*******************************************\nDuplicate deployment code " + depl
                                        .getItemTypeCode() + " in " + element + " and " + existing2 + ".\nDeployment " + element + " will not be loaded.\nPlease adjust the typecode of " + element
                                        + " at items.xml/advanced-deployments.xml to a typecode not used by system\n(Use Maintenance/Deployment page at the hybris administration console (hac) for getting a list of all used typecodes).\n*******************************************\n");
                    }
                    return;
                }
                putDeploymentTypeCode(Integer.valueOf(depl.getItemTypeCode()), depl);
            }
            putDeploymentName(depl.getName(), depl);
        }
        else if(element instanceof YAttributeDeployment)
        {
            LinkedCaseInsensitiveMap<String, YAttributeDeployment> linkedCaseInsensitiveMap;
            YAttributeDeployment depl = (YAttributeDeployment)element;
            YAttributeDeployment existing = getOwnAttributeDeployment(depl.getDeploymentName(), depl
                            .getPersistenceQualifier());
            if(existing != null && !existing.equals(element))
            {
                throw new IllegalStateException("duplicate column " + depl
                                .getDeploymentName() + "." + depl.getPersistenceQualifier() + " in " + element + " and " + existing);
            }
            Map<String, YAttributeDeployment> colsMap = this.attributeDeploymentsMap.get(depl.getDeploymentName());
            if(colsMap == null)
            {
                this.attributeDeploymentsMap.put(depl.getDeploymentName(), linkedCaseInsensitiveMap = new LinkedCaseInsensitiveMap());
            }
            YAttributeDeployment old = linkedCaseInsensitiveMap.put(depl.getPersistenceQualifier(), depl);
            if(old != null)
            {
                throw new IllegalStateException("cannot register pers.quali '" + depl.getPersistenceQualifier() + "'=" + depl + " since it is already mapped to " + old);
            }
        }
        else if(element instanceof YIndexDeployment)
        {
            LinkedCaseInsensitiveMap<String, YIndexDeployment> linkedCaseInsensitiveMap;
            YIndexDeployment depl = (YIndexDeployment)element;
            YIndexDeployment existing = getOwnIndexDeployment(depl.getDeploymentName(), depl.getIndexName());
            if(existing != null && !existing.equals(element))
            {
                throw new IllegalStateException("duplicate index deployment " + depl
                                .getDeploymentName() + "." + depl.getIndexName() + " in " + element + " and " + existing);
            }
            Map<String, YIndexDeployment> idxMap = this.indexeDeploymentsMap.get(depl.getDeploymentName());
            if(idxMap == null)
            {
                this.indexeDeploymentsMap.put(depl.getDeploymentName(), linkedCaseInsensitiveMap = new LinkedCaseInsensitiveMap());
            }
            linkedCaseInsensitiveMap.put(depl.getIndexName(), depl);
        }
        else if(element instanceof YFinder)
        {
            LinkedCaseInsensitiveMap<String, YFinder> linkedCaseInsensitiveMap;
            YFinder finder = (YFinder)element;
            YDeploymentElement existing = getOwnFinder(finder.getDeploymentName(), finder.getName());
            if(existing != null && !existing.equals(element))
            {
                throw new IllegalStateException("duplicate finder " + finder
                                .getDeploymentName() + "->" + finder.getName() + " in " + element + " and " + existing);
            }
            Map<String, YFinder> fMap = this.findersMap.get(finder.getDeploymentName());
            if(fMap == null)
            {
                this.findersMap.put(finder.getDeploymentName(), linkedCaseInsensitiveMap = new LinkedCaseInsensitiveMap());
            }
            linkedCaseInsensitiveMap.put(finder.getName(), finder);
        }
        else if(element instanceof YDBTypeMapping)
        {
            YDBTypeMapping mapping = (YDBTypeMapping)element;
            YDBTypeMapping existing = getOwnDBTypeMappings(mapping.getDatabaseName());
            if(existing != null && !existing.equals(element))
            {
                throw new IllegalStateException("duplicate database type mapping for " + mapping
                                .getDatabaseName() + " in " + element + " and " + existing);
            }
            this.dbTypeMappings.put(mapping.getDatabaseName(), mapping);
        }
        else if(element instanceof YEnumValue)
        {
            YEnumValue enumValue = (YEnumValue)element;
            Set<YEnumValue> values = this.enumValuesMap.get(enumValue.getEnumTypeCode());
            if(values == null)
            {
                this.enumValuesMap.put(enumValue.getEnumTypeCode(), values = new LinkedHashSet<>());
            }
            values.add(enumValue);
        }
        else if(element instanceof YIndex)
        {
            YIndex index = (YIndex)element;
            Set<YIndex> typeIndexes = this.typeIndexMap.get(index.getEnclosingTypeCode());
            if(typeIndexes == null)
            {
                this.typeIndexMap.put(index.getEnclosingTypeCode(), typeIndexes = new LinkedHashSet<>());
            }
            typeIndexes.add(index);
        }
        else if(element instanceof YAttributeDescriptor)
        {
            LinkedCaseInsensitiveMap<String, YAttributeDescriptor> linkedCaseInsensitiveMap;
            YAttributeDescriptor desc = (YAttributeDescriptor)element;
            YAttributeDescriptor existing = getOwnAttribute(desc.getEnclosingTypeCode(), desc.getQualifier());
            if(existing != null && !existing.equals(element))
            {
                throw new IllegalStateException("duplicate attribute '" + desc.getEnclosingTypeCode() + "." + desc.getQualifier() + "' in " + element + " and " + existing);
            }
            Map<String, YAttributeDescriptor> attrMap = this.attributesMap.get(desc.getEnclosingTypeCode());
            if(attrMap == null)
            {
                this.attributesMap.put(desc.getEnclosingTypeCode(), linkedCaseInsensitiveMap = new LinkedCaseInsensitiveMap());
            }
            linkedCaseInsensitiveMap.put(desc.getQualifier(), desc);
        }
        else
        {
            throw new IllegalArgumentException("unexpected element " + element);
        }
    }


    private void putDeploymentName(String name, YDeployment depl)
    {
        this.deploymentsNameMap.put(name, depl);
    }


    private void putDeploymentTypeCode(Integer typeCode, YDeployment depl)
    {
        this.deploymentsTCMap.put(typeCode, depl);
    }


    private void putDeploymentTable(String table, YDeployment depl)
    {
        this.deploymentsTableNameMap.put(table, depl);
    }


    private boolean hasPrintDuplicateDeploymentError(YDeployment depl)
    {
        if(MIGRATED_CORE_DEPLOYMENTS_INFO.get(depl.getName()) != null)
        {
            return !((String)MIGRATED_CORE_DEPLOYMENTS_INFO.get(depl.getName())).equals(depl.getFullName());
        }
        return true;
    }


    protected void registerDeployment(YComposedType type)
    {
        if(type.getDeploymentName() != null)
        {
            Set<YComposedType> types = this.deployedTypesMap.get(type.getDeploymentName());
            if(types == null)
            {
                this.deployedTypesMap.put(type.getDeploymentName(), types = new LinkedHashSet<>());
            }
            types.add(type);
        }
    }


    protected void registerInheritanceRelation(YComposedType type)
    {
        if(type.getSuperTypeCode() != null)
        {
            Set<String> subTypes = this.typeInheritanceMap.get(type.getSuperTypeCode());
            if(subTypes == null)
            {
                this.typeInheritanceMap.put(type.getSuperTypeCode(), subTypes = new LinkedHashSet<>());
            }
            subTypes.add((type.getCode() != null) ? type.getCode().toLowerCase(LocaleHelper.getPersistenceLocale()) : null);
        }
    }


    protected void registerInheritanceRelation(YAtomicType type)
    {
        if(type.getSuperClassName() != null)
        {
            Set<String> subTypes = this.typeInheritanceMap.get(type.getSuperClassName());
            if(subTypes == null)
            {
                this.typeInheritanceMap.put(type.getSuperClassName(), subTypes = new LinkedHashSet<>());
            }
            subTypes.add(type.getCode().toLowerCase(LocaleHelper.getPersistenceLocale()));
        }
    }


    public YDBTypeMapping getOwnDBTypeMappings(String databaseName)
    {
        return this.dbTypeMappings.get(databaseName);
    }


    public Map<String, YDBTypeMapping> getAllOwnDBTypeMappings()
    {
        return this.dbTypeMappings;
    }


    public Set<? extends YType> getOwnTypes()
    {
        if(this.typesMap.isEmpty())
        {
            return Collections.EMPTY_SET;
        }
        Set<YType> ret = new LinkedHashSet<>();
        for(Map.Entry<String, YType> entry : this.typesMap.entrySet())
        {
            ret.add(entry.getValue());
        }
        return ret;
    }


    public <T> Set<T> getOwnTypes(Class clazz)
    {
        return getOwnTypes(clazz, new Class[] {(Class)null});
    }


    public <T> Set<T> getOwnTypes(Class clazz, Class... excluded)
    {
        if(this.typesMap == null || this.typesMap.isEmpty())
        {
            return Collections.EMPTY_SET;
        }
        Set<T> ret = new LinkedHashSet<>();
        label22:
        for(Map.Entry<String, ? extends YType> entry : this.typesMap.entrySet())
        {
            Class<?> tCl = ((YType)entry.getValue()).getClass();
            if(clazz.isAssignableFrom(tCl))
            {
                if(excluded != null)
                {
                    for(Class excl : excluded)
                    {
                        if(excl != null && excl.isAssignableFrom(tCl))
                        {
                            continue label22;
                        }
                    }
                }
                ret.add((T)entry.getValue());
            }
        }
        return ret;
    }


    public final Set<YComposedType> getOwnTypesByDeployment(String deploymentName)
    {
        Set<YComposedType> ret = this.deployedTypesMap.get(deploymentName);
        return (ret != null) ? ret : Collections.EMPTY_SET;
    }


    public final YType getOwnType(String code)
    {
        return this.typesMap.get(code);
    }


    public Set<YDeployment> getOwnDeployments()
    {
        if(this.deploymentsNameMap == null || this.deploymentsNameMap.isEmpty())
        {
            return Collections.EMPTY_SET;
        }
        Set<YDeployment> ret = new LinkedHashSet<>();
        for(Map.Entry<String, YDeployment> entry : this.deploymentsNameMap.entrySet())
        {
            ret.add(entry.getValue());
        }
        return ret;
    }


    public final YIndexDeployment getOwnIndexDeployment(String deploymentName, String indexName)
    {
        Map<String, YIndexDeployment> idx = this.indexeDeploymentsMap.get(deploymentName);
        return (idx != null) ? idx.get(indexName) : null;
    }


    public final Set<YIndexDeployment> getOwnIndexDeployments(String deploymentName)
    {
        Map<String, YIndexDeployment> idx = this.indexeDeploymentsMap.get(deploymentName);
        return (idx != null) ? new LinkedHashSet<>(idx.values()) : Collections.EMPTY_SET;
    }


    public final Set<YIndexDeployment> getOwnIndexDeployments()
    {
        if(this.indexeDeploymentsMap == null || this.indexeDeploymentsMap.isEmpty())
        {
            return Collections.EMPTY_SET;
        }
        Set<YIndexDeployment> ret = new LinkedHashSet<>();
        for(Map.Entry<String, Map<String, YIndexDeployment>> entry : this.indexeDeploymentsMap.entrySet())
        {
            for(Map.Entry<String, YIndexDeployment> entry2 : (Iterable<Map.Entry<String, YIndexDeployment>>)((Map)entry.getValue()).entrySet())
            {
                ret.add(entry2.getValue());
            }
        }
        return ret;
    }


    public final YAttributeDeployment getOwnAttributeDeployment(String deploymentName, String persistenceQualifier)
    {
        Map<String, YAttributeDeployment> cols = this.attributeDeploymentsMap.get(deploymentName);
        return (cols != null) ? cols.get(persistenceQualifier) : null;
    }


    public final Set<YAttributeDeployment> getOwnAttributeDeployments(String deploymentName)
    {
        Map<String, YAttributeDeployment> columns = this.attributeDeploymentsMap.get(deploymentName);
        return (columns != null) ? new LinkedHashSet<>(columns.values()) : Collections.EMPTY_SET;
    }


    public Set<YAttributeDeployment> getOwnAttributeDeployments()
    {
        if(this.attributeDeploymentsMap == null || this.attributeDeploymentsMap.isEmpty())
        {
            return Collections.EMPTY_SET;
        }
        Set<YAttributeDeployment> ret = new LinkedHashSet<>();
        for(Map.Entry<String, Map<String, YAttributeDeployment>> entry : this.attributeDeploymentsMap.entrySet())
        {
            for(Map.Entry<String, YAttributeDeployment> entry2 : (Iterable<Map.Entry<String, YAttributeDeployment>>)((Map)entry.getValue()).entrySet())
            {
                ret.add(entry2.getValue());
            }
        }
        return ret;
    }


    public final YDeploymentElement getOwnFinder(String deploymentName, String name)
    {
        Map<String, YFinder> finders = this.findersMap.get(deploymentName);
        return (finders != null) ? (YDeploymentElement)finders.get(name) : null;
    }


    public final Set<YFinder> getOwnFinders(String deploymentName)
    {
        Map<String, YFinder> finders = this.findersMap.get(deploymentName);
        return (finders != null) ? new LinkedHashSet<>(finders.values()) : Collections.EMPTY_SET;
    }


    public Set<YFinder> getOwnFinders()
    {
        if(this.findersMap == null || this.findersMap.isEmpty())
        {
            return Collections.EMPTY_SET;
        }
        Set<YFinder> ret = new LinkedHashSet<>();
        for(Map.Entry<String, Map<String, YFinder>> entry : this.findersMap.entrySet())
        {
            for(Map.Entry<String, YFinder> entry2 : (Iterable<Map.Entry<String, YFinder>>)((Map)entry.getValue()).entrySet())
            {
                ret.add(entry2.getValue());
            }
        }
        return ret;
    }


    protected String cutPackageName(String fullDeploymentName)
    {
        int pos = fullDeploymentName.lastIndexOf('.');
        return (pos >= 0 && pos + 1 < fullDeploymentName.length()) ? fullDeploymentName.substring(pos + 1) : fullDeploymentName;
    }


    public final YDeployment getOwnDeployment(String name)
    {
        YDeployment ret = this.deploymentsNameMap.get(name);
        return (ret != null) ? ret : this.deploymentsNameMap.get(cutPackageName(name));
    }


    public final YDeployment getOwnDeployment(int typeCode)
    {
        Integer key = Integer.valueOf(typeCode);
        YDeployment ret = this.deploymentsTCMap.get(key);
        return (ret != null) ? ret : this.deploymentsTCMap.get(key);
    }


    public YRelationEnd getOwnAttributeRelationEnd(String enclosingTypeCode, String qualifier)
    {
        Map<String, YRelationEnd> attributes = this.attributes2RelationEndMap.get(enclosingTypeCode);
        return (attributes != null) ? attributes.get(qualifier) : null;
    }


    public YAttributeDescriptor getOwnAttribute(String enclosingTypeCode, String qualifier)
    {
        Map<String, YAttributeDescriptor> attributes = this.attributesMap.get(enclosingTypeCode);
        return (attributes != null) ? attributes.get(qualifier) : null;
    }


    public Set<YAttributeDescriptor> getOwnAttributes()
    {
        if(this.attributesMap == null || this.attributesMap.isEmpty())
        {
            return Collections.EMPTY_SET;
        }
        Set<YAttributeDescriptor> ret = new LinkedHashSet<>();
        for(Map.Entry<String, Map<String, YAttributeDescriptor>> entry : this.attributesMap.entrySet())
        {
            ret.addAll(((Map)entry.getValue()).values());
        }
        return ret;
    }


    public Set<YAttributeDescriptor> getOwnAttributes(String enclosingTypeCode)
    {
        Set<YAttributeDescriptor> ret = null;
        Map<String, YAttributeDescriptor> map = this.attributesMap.get(enclosingTypeCode);
        if(map != null)
        {
            for(Map.Entry<String, YAttributeDescriptor> entry : map.entrySet())
            {
                if(ret == null)
                {
                    ret = new LinkedHashSet<>(map.size());
                }
                ret.add(entry.getValue());
            }
        }
        return (ret != null) ? ret : Collections.EMPTY_SET;
    }


    public Set<YIndex> getOwnIndexes()
    {
        if(this.typeIndexMap == null || this.typeIndexMap.isEmpty())
        {
            return Collections.EMPTY_SET;
        }
        Set<YIndex> ret = new LinkedHashSet<>();
        for(Map.Entry<String, Set<YIndex>> entry : this.typeIndexMap.entrySet())
        {
            ret.addAll(entry.getValue());
        }
        return ret;
    }


    public Set<YIndex> getOwnIndexes(String enclosingTypeCode)
    {
        Set<YIndex> ret = this.typeIndexMap.get(enclosingTypeCode);
        return (ret != null) ? ret : Collections.EMPTY_SET;
    }


    public Set<YEnumValue> getOwnEnumValues()
    {
        if(this.enumValuesMap == null || this.enumValuesMap.isEmpty())
        {
            return Collections.EMPTY_SET;
        }
        Set<YEnumValue> ret = new LinkedHashSet<>();
        for(Map.Entry<String, Set<YEnumValue>> entry : this.enumValuesMap.entrySet())
        {
            ret.addAll(entry.getValue());
        }
        return ret;
    }


    public Set<YEnumValue> getOwnEnumValues(String enumTypeCode)
    {
        Set<YEnumValue> ret = this.enumValuesMap.get(enumTypeCode);
        return (ret != null) ? ret : Collections.EMPTY_SET;
    }


    public Set<String> getOwnSubtypeCodes(String code)
    {
        Set<String> subTypeCodes = this.typeInheritanceMap.get(code);
        return (subTypeCodes != null) ? Collections.<String>unmodifiableSet(subTypeCodes) : Collections.EMPTY_SET;
    }
}
