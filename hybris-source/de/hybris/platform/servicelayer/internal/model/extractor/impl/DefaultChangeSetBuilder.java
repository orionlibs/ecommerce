package de.hybris.platform.servicelayer.internal.model.extractor.impl;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableMap;
import de.hybris.platform.core.PK;
import de.hybris.platform.directpersistence.ChangeSet;
import de.hybris.platform.directpersistence.MutableChangeSet;
import de.hybris.platform.directpersistence.impl.DefaultChangeSet;
import de.hybris.platform.directpersistence.record.DefaultRelationChanges;
import de.hybris.platform.directpersistence.record.EntityRecord;
import de.hybris.platform.directpersistence.record.InsertManyToManyRelationRecord;
import de.hybris.platform.directpersistence.record.InsertOneToManyRelationRecord;
import de.hybris.platform.directpersistence.record.LocalizedRelationChanges;
import de.hybris.platform.directpersistence.record.ModificationRecord;
import de.hybris.platform.directpersistence.record.RelationChanges;
import de.hybris.platform.directpersistence.record.RemoveManyToManyRelationsRecord;
import de.hybris.platform.directpersistence.record.RemoveOneToManyRelationsRecord;
import de.hybris.platform.directpersistence.record.impl.DefaultLocalizedRelationChanges;
import de.hybris.platform.directpersistence.record.impl.DefaultNonLocalizedRelationChanges;
import de.hybris.platform.directpersistence.record.impl.DeleteRecord;
import de.hybris.platform.directpersistence.record.impl.PropertyHolder;
import de.hybris.platform.persistence.links.NonNavigableRelationsDAO;
import de.hybris.platform.servicelayer.internal.converter.ModelConverter;
import de.hybris.platform.servicelayer.internal.converter.impl.ItemModelConverter;
import de.hybris.platform.servicelayer.internal.converter.impl.LocalizedAttributesProcessor;
import de.hybris.platform.servicelayer.internal.model.extractor.ChangeSetBuilder;
import de.hybris.platform.servicelayer.internal.model.impl.wrapper.ModelWrapper;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContextImpl;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class DefaultChangeSetBuilder implements ChangeSetBuilder
{
    private static final Logger LOG = Logger.getLogger(DefaultChangeSetBuilder.class);
    private EnumerationDelegate enumerationDelegate;
    private LocalizedAttributesProcessor localizedAttributesProcessor;
    private NonNavigableRelationsDAO nonNavigableRelationsDAO;


    public ChangeSet build(Collection<ModelWrapper> wrappers)
    {
        Preconditions.checkArgument((wrappers != null), "wrappers are required");
        DefaultChangeSet defaultChangeSet = new DefaultChangeSet();
        Map<Object, PK> generatedPks = createMapWithGeneratedPks(wrappers);
        for(ModelWrapper wrapper : wrappers)
        {
            switch(null.$SwitchMap$de$hybris$platform$servicelayer$interceptor$PersistenceOperation[wrapper.getOperationToPerform().ordinal()])
            {
                case 1:
                    appendModificationChanges((MutableChangeSet)defaultChangeSet, Collections.singletonList(wrapper), generatedPks);
                    continue;
                case 2:
                    appendDeletionChanges((MutableChangeSet)defaultChangeSet, Collections.singletonList(wrapper));
                    continue;
            }
            throw new IllegalArgumentException("Unsupported ModelWrapper mode: " + wrapper.getOperationToPerform());
        }
        defaultChangeSet.finish();
        return (ChangeSet)defaultChangeSet;
    }


    public ChangeSet buildForModification(Collection<ModelWrapper> wrappers)
    {
        Preconditions.checkArgument((wrappers != null), "wrappers are required");
        Map<Object, PK> generatedPks = createMapWithGeneratedPks(wrappers);
        DefaultChangeSet defaultChangeSet = new DefaultChangeSet();
        appendModificationChanges((MutableChangeSet)defaultChangeSet, wrappers, generatedPks);
        return (ChangeSet)defaultChangeSet;
    }


    private void appendModificationChanges(MutableChangeSet changeSet, Collection<ModelWrapper> wrappers, Map<Object, PK> generatedPks)
    {
        boolean useJalo = false;
        for(ModelWrapper wrapper : wrappers)
        {
            useJalo |= processWrapper(changeSet, wrapper, generatedPks);
        }
        changeSet.sortAll();
        changeSet.groupOrderInformation();
        changeSet.setJaloWayRecommended(useJalo);
        logDebug((ChangeSet)changeSet);
    }


    private Map<Object, PK> createMapWithGeneratedPks(Collection<ModelWrapper> wrappers)
    {
        Collection<ModelWrapper> newOnes = Collections2.filter(wrappers, (Predicate)new Object(this));
        if(newOnes.isEmpty())
        {
            return Collections.emptyMap();
        }
        Map<Object, PK> resultMap = new LinkedHashMap<>();
        for(ModelWrapper wrap : newOnes)
        {
            resultMap.put(wrap.getModel(), wrap.getGeneratedPk());
        }
        return (Map<Object, PK>)ImmutableMap.copyOf(resultMap);
    }


    private boolean processWrapper(MutableChangeSet changeSet, ModelWrapper wrapper, Map<Object, PK> generatedPks)
    {
        ModelInfoProvider infoProvider = createModelInfoProvider(wrapper);
        DirtyObjectsProcessor nonLocalizedProcessor = new DirtyObjectsProcessor(infoProvider, generatedPks, this.enumerationDelegate);
        nonLocalizedProcessor.process(infoProvider.getHistory().getDirtyAttributes(), null);
        Map<Locale, Set<PropertyHolder>> localizedProperties = new LinkedHashMap<>();
        Set<RelationInfo> localizedRelations = new LinkedHashSet<>();
        boolean useJalo = processLocalizedDirtyObjects(infoProvider, generatedPks, localizedProperties, localizedRelations);
        ModificationRecord modRecord = RecordFactory.createEntityRecord(wrapper, infoProvider, nonLocalizedProcessor
                        .getNonLocalizedProperties(), localizedProperties);
        changeSet.add(new EntityRecord[] {(EntityRecord)modRecord});
        addAllRelationChanges(changeSet, nonLocalizedProcessor.getRelationInfos());
        addAllLocalizedRelationChanges(changeSet, localizedRelations);
        return (useJalo || nonLocalizedProcessor.hasWritableJaloOnlyAttrs());
    }


    private boolean processLocalizedDirtyObjects(ModelInfoProvider infoProvider, Map<Object, PK> generatedPks, Map<Locale, Set<PropertyHolder>> properties, Set<RelationInfo> relations)
    {
        Map<Locale, Set<String>> filteredQualifiers = preProcessLocalizedAttributes(infoProvider.getHistory()
                        .getDirtyLocalizedAttributes());
        DirtyObjectsProcessor processor = new DirtyObjectsProcessor(infoProvider, generatedPks, this.enumerationDelegate);
        for(Map.Entry<Locale, Set<String>> entry : filteredQualifiers.entrySet())
        {
            processor.process(entry.getValue(), entry.getKey());
        }
        properties.putAll(processor.getProperties());
        relations.addAll(processor.getRelationInfos());
        return processor.hasWritableJaloOnlyAttrs();
    }


    Map<Locale, Set<String>> preProcessLocalizedAttributes(Map<Locale, Set<String>> dirtyLocalizedAttributes)
    {
        return this.localizedAttributesProcessor.processQualifiers(dirtyLocalizedAttributes);
    }


    public ChangeSet buildForDelete(Collection<ModelWrapper> wrappers)
    {
        Preconditions.checkArgument((wrappers != null), "wrappers are required");
        DefaultChangeSet defaultChangeSet = new DefaultChangeSet();
        appendDeletionChanges((MutableChangeSet)defaultChangeSet, wrappers);
        return (ChangeSet)defaultChangeSet;
    }


    private void appendDeletionChanges(MutableChangeSet changeSet, Collection<ModelWrapper> wrappers)
    {
        for(ModelWrapper wrapper : wrappers)
        {
            changeSet.add(new EntityRecord[] {extractDeleteRecord(wrapper)});
        }
        changeSet.sortAll();
        logDebug((ChangeSet)changeSet);
    }


    private EntityRecord extractDeleteRecord(ModelWrapper wrapper)
    {
        ModelInfoProvider info = createModelInfoProvider(wrapper);
        DeleteRecord record = new DeleteRecord(wrapper.getPk(), wrapper.getPersistenceType(), info.getContext().getPersistenceVersion());
        addRelationAttributes(record, wrapper);
        return (EntityRecord)record;
    }


    private void addRelationAttributes(DeleteRecord record, ModelWrapper wrapper)
    {
        Preconditions.checkArgument((record != null), "record is required");
        Preconditions.checkArgument((wrapper != null), "wrapper is required");
        ModelConverter converter = wrapper.getConverter();
        if(converter instanceof ItemModelConverter)
        {
            for(ItemModelConverter.ModelAttributeInfo modelAttributeInfo : ((ItemModelConverter)converter).getAllModelAttributes())
            {
                ItemModelConverter.TypeAttributeInfo attrInfo = modelAttributeInfo.getAttributeInfo();
                if(isRelation(attrInfo) && !attrInfo.getRelationType().isOneToMany())
                {
                    record.addRelationName(new String[] {attrInfo.getRelationName()});
                }
            }
        }
        record.addRelationName(getNonNavigableAttributesForType(record.getType()));
    }


    private String[] getNonNavigableAttributesForType(String typeCode)
    {
        Collection<String> nonNavigableRelations = this.nonNavigableRelationsDAO.getNonNavigableRelationCodesForType(typeCode);
        return nonNavigableRelations.<String>toArray(new String[nonNavigableRelations.size()]);
    }


    private boolean isRelation(ItemModelConverter.TypeAttributeInfo typeInfo)
    {
        return typeInfo instanceof ItemModelConverter.RelationTypeAttributeInfo;
    }


    private ModelInfoProvider createModelInfoProvider(ModelWrapper wrapper)
    {
        Object modelObj = wrapper.getModel();
        Preconditions.checkState(modelObj instanceof AbstractItemModel, "Model is not instance of AbstractItemModel");
        AbstractItemModel model = (AbstractItemModel)modelObj;
        ItemModelContextImpl context = (ItemModelContextImpl)model.getItemModelContext();
        ModelConverter wrapperConverter = wrapper.getConverter();
        Preconditions.checkState(wrapperConverter instanceof ItemModelConverter, "ModelConverter is not instance of ItemModelConverter");
        return new ModelInfoProvider(this, model, context, (ItemModelConverter)wrapperConverter, context.getValueHistory(), wrapper
                        .isNew());
    }


    private void addAllRelationChanges(MutableChangeSet changeSet, Collection<RelationInfo> relationInfos)
    {
        for(RelationInfo info : relationInfos)
        {
            DefaultRelationChanges relationChanges = prepareRelationChangeSet(changeSet, info);
            if(info.isOneToMany())
            {
                if(info.isClearOnSource())
                {
                    relationChanges.add(new RemoveOneToManyRelationsRecord[] {RecordFactory.createRemoveOneToManyRelationsRecord(info)});
                    continue;
                }
                relationChanges.add(new InsertOneToManyRelationRecord[] {RecordFactory.createOneToManyRelationRecord(info)});
                continue;
            }
            if(info.isRemove())
            {
                relationChanges.add(new RemoveManyToManyRelationsRecord[] {RecordFactory.createRemoveManyToManyRelationRecord(info)});
                continue;
            }
            relationChanges.add(new InsertManyToManyRelationRecord[] {RecordFactory.createInserManyToManyRelationRecord(info)});
        }
    }


    private void addAllLocalizedRelationChanges(MutableChangeSet changeSet, Collection<RelationInfo> localizedRelationInfos)
    {
        Preconditions.checkArgument((localizedRelationInfos != null), "localizedRelationInfos is required");
        for(RelationInfo info : localizedRelationInfos)
        {
            DefaultRelationChanges relationChanges = prepareLocalizedRelationChangeSet(changeSet, info.getLocale(), info);
            if(info.isRemove())
            {
                relationChanges.add(new RemoveManyToManyRelationsRecord[] {RecordFactory.createRemoveManyToManyRelationRecord(info)});
                continue;
            }
            relationChanges.add(new InsertManyToManyRelationRecord[] {RecordFactory.createInserManyToManyRelationRecord(info)});
        }
    }


    private DefaultRelationChanges prepareRelationChangeSet(MutableChangeSet changeSet, RelationInfo relationInfo)
    {
        DefaultNonLocalizedRelationChanges defaultNonLocalizedRelationChanges;
        Preconditions.checkArgument((changeSet != null), "changeSet is required");
        Preconditions.checkArgument((relationInfo != null), "relationInfo is required");
        DefaultRelationChanges relationChanges = (DefaultRelationChanges)changeSet.getRelationChangesForRelation(relationInfo
                        .getRelationMetaInfo()
                        .getRelationName());
        if(relationChanges == null)
        {
            defaultNonLocalizedRelationChanges = new DefaultNonLocalizedRelationChanges(relationInfo.getRelationMetaInfo());
            changeSet.putRelationChanges(relationInfo.getRelationMetaInfo().getRelationName(), (RelationChanges)defaultNonLocalizedRelationChanges);
        }
        return (DefaultRelationChanges)defaultNonLocalizedRelationChanges;
    }


    private DefaultRelationChanges prepareLocalizedRelationChangeSet(MutableChangeSet changeSet, Locale locale, RelationInfo relationInfo)
    {
        DefaultLocalizedRelationChanges defaultLocalizedRelationChanges;
        DefaultNonLocalizedRelationChanges defaultNonLocalizedRelationChanges;
        Preconditions.checkArgument((locale != null), "locale is required");
        Preconditions.checkArgument((relationInfo != null), "relationInfo is required");
        LocalizedRelationChanges localizedRalationChanges = (LocalizedRelationChanges)changeSet.getRelationChangesForRelation(relationInfo.getRelationMetaInfo().getRelationName());
        if(localizedRalationChanges == null)
        {
            defaultLocalizedRelationChanges = new DefaultLocalizedRelationChanges(relationInfo.getRelationMetaInfo());
            changeSet.putRelationChanges(relationInfo.getRelationMetaInfo().getRelationName(), (RelationChanges)defaultLocalizedRelationChanges);
        }
        DefaultRelationChanges relationChanges = defaultLocalizedRelationChanges.getRelationChangeForLanguage(locale);
        if(relationChanges == null)
        {
            defaultNonLocalizedRelationChanges = new DefaultNonLocalizedRelationChanges(relationInfo.getRelationMetaInfo());
            defaultLocalizedRelationChanges.put(locale, (DefaultRelationChanges)defaultNonLocalizedRelationChanges);
        }
        return (DefaultRelationChanges)defaultNonLocalizedRelationChanges;
    }


    @Required
    public void setLocalizedAttributesProcessor(LocalizedAttributesProcessor localizedAttributesProcessor)
    {
        this.localizedAttributesProcessor = localizedAttributesProcessor;
    }


    @Required
    public void setEnumerationDelegate(EnumerationDelegate enumerationDelegate)
    {
        this.enumerationDelegate = enumerationDelegate;
    }


    @Required
    public void setNonNavigableRelationsDAO(NonNavigableRelationsDAO nonNavigableRelationsDAO)
    {
        this.nonNavigableRelationsDAO = nonNavigableRelationsDAO;
    }


    private void logDebug(ChangeSet changeSet)
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug(changeSet.toString());
        }
    }
}
