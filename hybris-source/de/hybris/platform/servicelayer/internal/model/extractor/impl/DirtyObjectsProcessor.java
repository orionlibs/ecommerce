package de.hybris.platform.servicelayer.internal.model.extractor.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import de.hybris.platform.core.HybrisEnumValue;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.directpersistence.record.impl.PropertyHolder;
import de.hybris.platform.directpersistence.record.impl.RelationMetaInfo;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.RelationDescriptor;
import de.hybris.platform.jalo.type.RelationType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.servicelayer.internal.converter.impl.ItemModelConverter;
import de.hybris.platform.servicelayer.internal.model.LocMap;
import de.hybris.platform.servicelayer.internal.model.impl.ModelValueHistory;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.util.ItemPropertyValue;
import de.hybris.platform.util.encryption.ValueEncryptor;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.apache.log4j.Logger;

class DirtyObjectsProcessor
{
    private static final Logger LOG = Logger.getLogger(DirtyObjectsProcessor.class.getName());
    private final DefaultChangeSetBuilder.ModelInfoProvider infoProvider;
    private final Map<Object, PK> generatedPks;
    private final EnumerationDelegate enumerationDelegate;
    private final Map<Locale, Set<PropertyHolder>> properties;
    private final Set<RelationInfo> relationInfos;
    private final Set<AbstractItemModel> partOfRefsToRemove;
    private boolean writableJaloOnlyAttrs = false;
    private final ValueEncryptor valueEncryptor;


    public DirtyObjectsProcessor(DefaultChangeSetBuilder.ModelInfoProvider infoProvider, Map<Object, PK> preGeneratedPks, EnumerationDelegate enumerationDelegate)
    {
        this.infoProvider = infoProvider;
        this.generatedPks = preGeneratedPks;
        this.enumerationDelegate = enumerationDelegate;
        this.properties = new LinkedHashMap<>();
        this.relationInfos = new LinkedHashSet<>();
        this.partOfRefsToRemove = new HashSet<>();
        this.valueEncryptor = Registry.getMasterTenant().getValueEncryptor();
    }


    Set<AbstractItemModel> getPartOfRefsToRemove()
    {
        return this.partOfRefsToRemove;
    }


    Map<Locale, Set<PropertyHolder>> getProperties()
    {
        return this.properties;
    }


    Set<PropertyHolder> getNonLocalizedProperties()
    {
        return this.properties.get(null);
    }


    Set<RelationInfo> getRelationInfos()
    {
        return this.relationInfos;
    }


    public void process(Set<String> dirtyAttributes, Locale locale)
    {
        for(String attr : dirtyAttributes)
        {
            ItemModelConverter.ModelAttributeInfo attrInfo = this.infoProvider.getConverter().getInfo(attr);
            if(attrInfo != null && attrInfo.getAttributeInfo().isWritable())
            {
                ItemModelConverter.TypeAttributeInfo typeInfo = attrInfo.getAttributeInfo();
                if(!typeInfo.isRelation() && typeInfo.isJaloOnly())
                {
                    if(LOG.isDebugEnabled())
                    {
                        LOG.debug("####################################################");
                        LOG.debug("Setting writable jalo attrs (" + this.writableJaloOnlyAttrs + ") " + typeInfo);
                        LOG.debug("####################################################");
                    }
                    this.writableJaloOnlyAttrs = true;
                }
                Object value = getValueForAttribute(locale, attr);
                Object originalValue = getOriginalValueForAttribute(locale, attr);
                if(!Objects.equals(value, originalValue))
                {
                    if(typeInfo.isReference())
                    {
                        processReference(locale, attr, typeInfo, value);
                        continue;
                    }
                    if(value != null && originalValue != null && bothValuesTypeIsBigDecimalAndAreBothEqual(attrInfo, value, originalValue))
                    {
                        continue;
                    }
                    processAtomic(locale, attr, encryptValueIfNecessary(value, typeInfo));
                }
            }
        }
    }


    private boolean bothValuesTypeIsBigDecimalAndAreBothEqual(ItemModelConverter.ModelAttributeInfo attrInfo, Object value, Object originalValue)
    {
        return (bothValuesTypeIsBigDecimal(attrInfo, value, originalValue) && ((BigDecimal)value).compareTo((BigDecimal)originalValue) == 0);
    }


    private boolean bothValuesTypeIsBigDecimal(ItemModelConverter.ModelAttributeInfo attrInfo, Object value, Object originalValue)
    {
        return (BigDecimal.class.equals(attrInfo.getFieldType()) || (isAttrInfoFieldNotBigDecimal(attrInfo) &&
                        areValuesInstanceofBigdecimal(value, originalValue)));
    }


    private boolean areValuesInstanceofBigdecimal(Object v1, Object v2)
    {
        return (v1 instanceof BigDecimal && v2 instanceof BigDecimal);
    }


    private boolean isAttrInfoFieldNotBigDecimal(ItemModelConverter.ModelAttributeInfo attrInfo)
    {
        return (Object.class.equals(attrInfo.getFieldType()) || attrInfo.getFieldType() == null);
    }


    private Object encryptValueIfNecessary(Object value, ItemModelConverter.TypeAttributeInfo typeInfo)
    {
        Object ret = value;
        if(value instanceof String && typeInfo.isEncrypted())
        {
            try
            {
                ret = this.valueEncryptor.encrypt((String)value);
            }
            catch(Exception e)
            {
                throw new IllegalStateException(e.getMessage(), e);
            }
        }
        return ret;
    }


    private void processAtomic(Locale locale, String attr, Object value)
    {
        if(!this.infoProvider.isNew() || value != null)
        {
            addProperty(locale, new PropertyHolder(attr, getReferenceValue(value)));
        }
    }


    private void processReference(Locale locale, String attr, ItemModelConverter.TypeAttributeInfo typeInfo, Object value)
    {
        if(isOneToMany(typeInfo))
        {
            AbstractItemModel sourceModel = this.infoProvider.getModel();
            PK sourcePk = getPkForModel(sourceModel);
            boolean isSrcToTgt = isRelationSource((ItemModelConverter.RelationTypeAttributeInfo)typeInfo);
            if(isSrcToTgt)
            {
                RelationMetaInfo relMetaInfo = createOneToManyRelationMetaInfo(typeInfo);
                if(value == null || (value instanceof Collection && ((Collection)value).isEmpty()))
                {
                    RelationInfo.Builder relInfoBuilder = RelationInfo.builder().withMetaInfo(relMetaInfo).sourceToTarget(isSrcToTgt).clearOnSource().sourcePk(sourcePk).forLocale(locale);
                    this.relationInfos.add(relInfoBuilder.build());
                }
                else if(value instanceof Collection)
                {
                    int targetPos = 0;
                    for(Object target : value)
                    {
                        PK targetPK = getPkForObject(target);
                        RelationInfo.Builder relInfoBuilder = RelationInfo.builder().withMetaInfo(relMetaInfo).sourceToTarget(isSrcToTgt).sourcePk(sourcePk).targetPk(targetPK).forLocale(locale);
                        if(relMetaInfo.isSourceOrdered())
                        {
                            relInfoBuilder.onPosition(Integer.valueOf(targetPos++));
                        }
                        this.relationInfos.add(relInfoBuilder.build());
                    }
                }
                else
                {
                    addProperty(locale, new PropertyHolder(attr, getReferenceValue(value)));
                }
            }
            else
            {
                addProperty(locale, new PropertyHolder(attr, getReferenceValue(value)));
            }
        }
        else if(isManyToMany(typeInfo))
        {
            this.relationInfos.addAll(createLocalizedManyToManyRelationInfos(typeInfo, value, locale));
        }
        else
        {
            collectChangedPartOfReference(attr, typeInfo, value, locale);
            addProperty(locale, new PropertyHolder(attr, getReferenceValue(value)));
        }
    }


    private PK getPkForObject(Object target)
    {
        PK targetPK;
        if(target instanceof HybrisEnumValue)
        {
            targetPK = getPkForEnum((HybrisEnumValue)target);
        }
        else
        {
            targetPK = getPkForModel((AbstractItemModel)target);
        }
        return targetPK;
    }


    private void addProperty(Locale locale, PropertyHolder property)
    {
        Set<PropertyHolder> propertyForLocale = this.properties.get(locale);
        if(propertyForLocale == null)
        {
            propertyForLocale = new LinkedHashSet<>();
            this.properties.put(locale, propertyForLocale);
        }
        propertyForLocale.add(property);
    }


    private void collectChangedPartOfReference(String attr, ItemModelConverter.TypeAttributeInfo typeInfo, Object value, Locale locale)
    {
        if(!this.infoProvider.isNew())
        {
            ComposedType composedType = TypeManager.getInstance().getComposedType(typeInfo.getEnclosingTypeQualifier());
            AttributeDescriptor attributeDescriptor = composedType.getAttributeDescriptor(attr);
            ModelValueHistory history = this.infoProvider.getHistory();
            Object originalValue = (locale == null) ? history.getOriginalValue(attr) : history.getOriginalValue(attr, locale);
            if(attributeDescriptor.isPartOf() && originalValue != null && !originalValue.equals(value))
            {
                if(originalValue instanceof Collection)
                {
                    this.partOfRefsToRemove.addAll(collectionDifference((Collection<? extends AbstractItemModel>)originalValue, (Collection<? extends AbstractItemModel>)value));
                }
                else if(originalValue instanceof AbstractItemModel)
                {
                    this.partOfRefsToRemove.add((AbstractItemModel)originalValue);
                }
                else
                {
                    throw new IllegalArgumentException("Not supported original value " + originalValue);
                }
            }
        }
    }


    private <T> Collection<T> collectionDifference(Collection<T> col1, Collection<T> col2)
    {
        Set<T> result = new HashSet<>();
        for(T el : col1)
        {
            if(!col2.contains(el))
            {
                result.add(el);
            }
        }
        return result;
    }


    private Object getValueForAttribute(Locale locale, String attr)
    {
        Object value;
        if(locale == null)
        {
            value = this.infoProvider.getConverter().getDirtyAttributeValue(this.infoProvider.getModel(), attr);
        }
        else
        {
            value = getAttrValueForLocale(this.infoProvider.getContext().getCombinedLocalizedValuesMap(), attr, locale);
        }
        return value;
    }


    private Object getOriginalValueForAttribute(Locale locale, String attr)
    {
        Object value;
        if(locale == null)
        {
            value = this.infoProvider.getContext().isLoaded(attr) ? this.infoProvider.getContext().getOriginalValue(attr) : this.infoProvider.getContext().loadOriginalValue(attr);
        }
        else
        {
            value = this.infoProvider.getContext().isLoaded(attr, locale) ? this.infoProvider.getContext().getOriginalValue(attr, locale) : this.infoProvider.getContext().loadOriginalValue(attr, locale);
        }
        return value;
    }


    private Object getReferenceValue(Object value)
    {
        Object result;
        if(value instanceof AbstractItemModel)
        {
            result = new ItemPropertyValue(getPkForModel((AbstractItemModel)value));
        }
        else if(value instanceof Collection)
        {
            result = (new CollectionTypeDeterminingBuilder(this, (Collection)value)).build();
        }
        else if(value instanceof Map)
        {
            if(((Map)value).isEmpty())
            {
                result = Collections.EMPTY_MAP;
            }
            else
            {
                result = new HashMap<>();
            }
            for(Map.Entry entry : ((Map)value).entrySet())
            {
                ((Map<Object, Object>)result).put(getReferenceValue(entry.getKey()), getReferenceValue(entry.getValue()));
            }
        }
        else if(value instanceof HybrisEnumValue)
        {
            result = new ItemPropertyValue(this.enumerationDelegate.getPK((HybrisEnumValue)value));
        }
        else
        {
            result = value;
        }
        return result;
    }


    private RelationMetaInfo createOneToManyRelationMetaInfo(ItemModelConverter.TypeAttributeInfo attrInfo)
    {
        ComposedType targetType, sourceType;
        RelationType relationType = attrInfo.getRelationType();
        RelationDescriptor targetDescriptor = relationType.getTargetAttributeDescriptor();
        RelationDescriptor sourceDescriptor = relationType.getSourceAttributeDescriptor();
        if(attrInfo.isRedeclared())
        {
            sourceType = ((ItemModelConverter.RelationTypeAttributeInfo)attrInfo).getConcreteSourceSideType();
            targetType = ((ItemModelConverter.RelationTypeAttributeInfo)attrInfo).getConcreteTargetSideType();
        }
        else
        {
            sourceType = relationType.getSourceType();
            targetType = relationType.getTargetType();
        }
        RelationMetaInfo.Builder builder = RelationMetaInfo.builder().relationName(attrInfo.getRelationName()).oneToMany().targetTypeCode(targetType.getCode()).sourceTypeCode(sourceType.getCode());
        if(targetDescriptor != null)
        {
            builder.fkOnTarget(getForeignKeyQualifier(relationType));
            builder.targetOrdered(targetDescriptor.isOrdered());
            builder.targetPartOf(targetDescriptor.isPartOf());
        }
        if(sourceDescriptor != null)
        {
            builder.sourceOrdered(sourceDescriptor.isOrdered());
            builder.sourcePartOf(sourceDescriptor.isPartOf());
        }
        return builder.build();
    }


    private String getForeignKeyQualifier(RelationType relationType)
    {
        RelationDescriptor descriptor = null;
        if(relationType.isSourceTypeOne())
        {
            descriptor = relationType.getTargetAttributeDescriptor();
        }
        else if(relationType.isTargetTypeOne())
        {
            descriptor = relationType.getSourceAttributeDescriptor();
        }
        return (descriptor == null) ? null : descriptor.getQualifier();
    }


    private RelationMetaInfo createManyToManyRelationMetaInfo(ItemModelConverter.TypeAttributeInfo typeInfo)
    {
        RelationType relationType = typeInfo.getRelationType();
        RelationDescriptor targetDescriptor = relationType.getTargetAttributeDescriptor();
        RelationDescriptor sourceDescriptor = relationType.getSourceAttributeDescriptor();
        RelationMetaInfo.Builder builder = RelationMetaInfo.builder().relationName(typeInfo.getRelationName()).targetTypeCode(relationType.getTargetType().getCode());
        if(targetDescriptor != null)
        {
            builder.targetOrdered(targetDescriptor.isOrdered());
            builder.targetPartOf(targetDescriptor.isPartOf());
        }
        if(sourceDescriptor != null)
        {
            builder.sourceOrdered(sourceDescriptor.isOrdered());
            builder.sourcePartOf(sourceDescriptor.isPartOf());
        }
        return builder.build();
    }


    private Collection<RelationInfo> createLocalizedManyToManyRelationInfos(ItemModelConverter.TypeAttributeInfo typeInfo, Object target, Locale locale)
    {
        Collection<RelationInfo> result;
        AbstractItemModel sourceModel = this.infoProvider.getModel();
        PK sourcePk = getPkForModel(sourceModel);
        Preconditions.checkState((sourcePk != null), "Source item PK must not be null");
        boolean isSrcToTgt = isRelationSource((ItemModelConverter.RelationTypeAttributeInfo)typeInfo);
        if(target == null)
        {
            result = getRelationInfos(typeInfo, locale, sourcePk, isSrcToTgt);
        }
        else if(target instanceof AbstractItemModel)
        {
            result = getRelationInfos(typeInfo, (AbstractItemModel)target, locale, sourcePk, isSrcToTgt);
        }
        else if(target instanceof Collection)
        {
            Collection<Object> col = (Collection<Object>)target;
            if(col.isEmpty())
            {
                result = getRelationInfos(typeInfo, locale, sourcePk, isSrcToTgt);
            }
            else
            {
                result = getRelationInfos(typeInfo, (Collection)target, locale, sourcePk, isSrcToTgt);
            }
        }
        else
        {
            result = Collections.EMPTY_LIST;
        }
        return result;
    }


    private Collection<RelationInfo> getRelationInfos(ItemModelConverter.TypeAttributeInfo typeInfo, Collection target, Locale locale, PK sourcePk, boolean srcToTgt)
    {
        Collection<RelationInfo> relationInfos = new LinkedList<>();
        int pos = 0;
        for(Object element : target)
        {
            PK targetPK = getPkForObject(element);
            RelationInfo relInfo = RelationInfo.builder().withMetaInfo(createManyToManyRelationMetaInfo(typeInfo)).sourcePk(sourcePk).targetPk(targetPK).sourceToTarget(srcToTgt).onPosition(Integer.valueOf(pos++)).forLocale(locale).build();
            relationInfos.add(relInfo);
        }
        return (Collection<RelationInfo>)ImmutableList.copyOf(relationInfos);
    }


    private Collection<RelationInfo> getRelationInfos(ItemModelConverter.TypeAttributeInfo typeInfo, AbstractItemModel target, Locale locale, PK sourcePk, boolean srcToTgt)
    {
        RelationInfo relInfo = RelationInfo.builder().withMetaInfo(createManyToManyRelationMetaInfo(typeInfo)).sourcePk(sourcePk).targetPk(getPkForModel(target)).sourceToTarget(srcToTgt).onPosition(Integer.valueOf(1)).forLocale(locale).build();
        return (Collection<RelationInfo>)ImmutableList.builder().add(relInfo).build();
    }


    private Collection<RelationInfo> getRelationInfos(ItemModelConverter.TypeAttributeInfo typeInfo, Locale locale, PK sourcePk, boolean srcToTgt)
    {
        RelationInfo relInfo = RelationInfo.builder().withMetaInfo(createManyToManyRelationMetaInfo(typeInfo)).sourcePk(sourcePk).toRemove().sourceToTarget(srcToTgt).forLocale(locale).build();
        return (Collection<RelationInfo>)ImmutableList.builder().add(relInfo).build();
    }


    private boolean isRelationSource(ItemModelConverter.RelationTypeAttributeInfo typeInfo)
    {
        return typeInfo.isSource();
    }


    private PK getPkForModel(AbstractItemModel model)
    {
        Preconditions.checkArgument((model != null), "model cannot be null");
        PK pk = (model.getPk() != null) ? model.getPk() : this.generatedPks.get(model);
        Preconditions.checkState((pk != null), "Cannot find pk for model: " + model + " in generated pk pool");
        return pk;
    }


    private PK getPkForEnum(HybrisEnumValue enumValue)
    {
        Preconditions.checkArgument((enumValue != null), "model cannot be null");
        PK pk = (this.enumerationDelegate.getPK(enumValue) != null) ? this.enumerationDelegate.getPK(enumValue) : this.generatedPks.get(enumValue);
        Preconditions.checkState((pk != null), "Cannot find pk for model: " + enumValue + " in generated pk pool");
        return pk;
    }


    private Object getAttrValueForLocale(Map<String, LocMap<Locale, Object>> localizedValueMaps, String attr, Locale locale)
    {
        return ((LocMap)localizedValueMaps.get(attr)).get(locale);
    }


    private boolean isOneToMany(ItemModelConverter.TypeAttributeInfo typeInfo)
    {
        return (typeInfo.isRelation() && typeInfo.getRelationType().isOneToMany());
    }


    private boolean isManyToMany(ItemModelConverter.TypeAttributeInfo typeInfo)
    {
        return (typeInfo.isRelation() && !typeInfo.getRelationType().isOneToMany());
    }


    boolean hasWritableJaloOnlyAttrs()
    {
        return this.writableJaloOnlyAttrs;
    }
}
