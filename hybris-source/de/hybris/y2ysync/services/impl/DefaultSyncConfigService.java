package de.hybris.y2ysync.services.impl;

import com.google.common.collect.ImmutableList;
import de.hybris.deltadetection.model.StreamConfigurationContainerModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.util.logging.Logs;
import de.hybris.y2ysync.impex.typesystem.ImpexHeaderBuilder;
import de.hybris.y2ysync.model.Y2YColumnDefinitionModel;
import de.hybris.y2ysync.model.Y2YStreamConfigurationContainerModel;
import de.hybris.y2ysync.model.Y2YStreamConfigurationModel;
import de.hybris.y2ysync.services.SyncConfigService;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class DefaultSyncConfigService implements SyncConfigService
{
    private static final Logger LOG = Logger.getLogger(DefaultSyncConfigService.class);
    private ModelService modelService;
    private FlexibleSearchService flexibleSearchService;
    private TypeService typeService;
    private CommonI18NService commonI18NService;
    private ImpexHeaderBuilder impexHeaderBuilder;


    public Y2YStreamConfigurationContainerModel createStreamConfigurationContainer(String id)
    {
        return createStreamConfigurationContainer(id, null);
    }


    public Y2YStreamConfigurationContainerModel createStreamConfigurationContainer(String id, CatalogVersionModel catalogVersion)
    {
        Y2YStreamConfigurationContainerModel container = (Y2YStreamConfigurationContainerModel)this.modelService.create(Y2YStreamConfigurationContainerModel.class);
        container.setId(id);
        container.setCatalogVersion(catalogVersion);
        return container;
    }


    public Y2YStreamConfigurationContainerModel getStreamConfigurationContainerById(String id)
    {
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery("SELECT {pk} FROM {Y2YStreamConfigurationContainer} WHERE {id}=?id");
        fQuery.addQueryParameter("id", id);
        return (Y2YStreamConfigurationContainerModel)this.flexibleSearchService.searchUnique(fQuery);
    }


    public Y2YColumnDefinitionModel createUntypedColumnDefinition(String impexHeader, String columnName)
    {
        return createColumnDefinition(null, impexHeader, columnName, null);
    }


    private Y2YColumnDefinitionModel createColumnDefinition(AttributeDescriptorModel attributeDescriptor, String impexHeader, String columnName, LanguageModel lang)
    {
        Y2YColumnDefinitionModel columnDefinition = (Y2YColumnDefinitionModel)this.modelService.create(Y2YColumnDefinitionModel.class);
        columnDefinition.setAttributeDescriptor(attributeDescriptor);
        columnDefinition.setImpexHeader(impexHeader);
        columnDefinition.setLanguage(lang);
        columnDefinition.setColumnName(columnName);
        return columnDefinition;
    }


    public Y2YStreamConfigurationModel createStreamConfiguration(Y2YStreamConfigurationContainerModel container, String typeCode, Set<Y2YColumnDefinitionModel> columnDefinitions)
    {
        return createStreamConfiguration(container, typeCode, null, null, Collections.emptySet(), columnDefinitions);
    }


    public Y2YStreamConfigurationModel createStreamConfiguration(Y2YStreamConfigurationContainerModel container, String typeCode, Set<AttributeDescriptorModel> attributeDescriptors, Set<Y2YColumnDefinitionModel> untypedColumnDefs)
    {
        return createStreamConfiguration(container, typeCode, null, null, attributeDescriptors, untypedColumnDefs);
    }


    public Y2YStreamConfigurationModel createStreamConfiguration(Y2YStreamConfigurationContainerModel container, String typeCode, CatalogVersionModel catalogVersion, Set<Y2YColumnDefinitionModel> columnDefinitions)
    {
        return createStreamConfiguration(container, typeCode, null, catalogVersion, columnDefinitions);
    }


    public Y2YStreamConfigurationModel createStreamConfiguration(Y2YStreamConfigurationContainerModel container, String typeCode, CatalogVersionModel catalogVersion, Set<AttributeDescriptorModel> attributeDescriptors, Set<Y2YColumnDefinitionModel> untypedColumnDefs)
    {
        return createStreamConfiguration(container, typeCode, null, catalogVersion, attributeDescriptors, untypedColumnDefs);
    }


    public Y2YStreamConfigurationModel createStreamConfiguration(Y2YStreamConfigurationContainerModel container, String typeCode, String whereClause, Set<Y2YColumnDefinitionModel> columnDefinitions)
    {
        return createStreamConfiguration(container, typeCode, whereClause, null, Collections.emptySet(), columnDefinitions);
    }


    public Y2YStreamConfigurationModel createStreamConfiguration(Y2YStreamConfigurationContainerModel container, String typeCode, String whereClause, Set<AttributeDescriptorModel> attributeDescriptors, Set<Y2YColumnDefinitionModel> untypedColumnDefs)
    {
        return createStreamConfiguration(container, typeCode, whereClause, null, attributeDescriptors, untypedColumnDefs);
    }


    public Y2YStreamConfigurationModel createStreamConfiguration(Y2YStreamConfigurationContainerModel container, String typeCode, String whereClause, CatalogVersionModel catalogVersion, Set<Y2YColumnDefinitionModel> columnDefinitions)
    {
        return createStreamConfiguration(container, typeCode, whereClause, catalogVersion, Collections.emptySet(), columnDefinitions);
    }


    public Y2YStreamConfigurationModel createStreamConfiguration(Y2YStreamConfigurationContainerModel container, String typeCode, String whereClause, CatalogVersionModel catalogVersion, Set<AttributeDescriptorModel> attributeDescriptors, Set<Y2YColumnDefinitionModel> untypedColumnDefs)
    {
        Set<Y2YColumnDefinitionModel> columnDefinitions = (Set<Y2YColumnDefinitionModel>)attributeDescriptors.stream().map(this::createColumnDefinitions).flatMap(Collection::stream).collect(Collectors.toCollection(java.util.LinkedHashSet::new));
        columnDefinitions.addAll(untypedColumnDefs);
        Y2YStreamConfigurationModel configuration = (Y2YStreamConfigurationModel)this.modelService.create(Y2YStreamConfigurationModel.class);
        configuration.setItemTypeForStream(this.typeService.getComposedTypeForCode(typeCode));
        configuration.setStreamId(UUID.randomUUID().toString());
        configuration.setWhereClause(whereClause);
        configuration.setCatalogVersion(catalogVersion);
        configuration.setContainer((StreamConfigurationContainerModel)container);
        configuration.setColumnDefinitions(columnDefinitions);
        adjustPositionsAndConfiguration(columnDefinitions, configuration);
        Logs.debug(LOG, () -> "Creating Stream Configuration >>> itemType: " + configuration.getItemtype());
        return configuration;
    }


    private Collection<Y2YColumnDefinitionModel> createColumnDefinitions(AttributeDescriptorModel attributeDescriptor)
    {
        ImmutableList.Builder<Y2YColumnDefinitionModel> result = ImmutableList.builder();
        if(isLocalized(attributeDescriptor))
        {
            List<LanguageModel> allLanguages = this.commonI18NService.getAllLanguages();
            for(LanguageModel lang : allLanguages)
            {
                String impexHeader = this.impexHeaderBuilder.getHeaderFor(attributeDescriptor, lang.getIsocode());
                String columnName = String.format("%s_%s", new Object[] {attributeDescriptor.getQualifier(), lang.getIsocode()});
                result.add(createColumnDefinition(attributeDescriptor, impexHeader, columnName, lang));
            }
        }
        else
        {
            String columnName = attributeDescriptor.getQualifier();
            result.add(
                            createColumnDefinition(attributeDescriptor, this.impexHeaderBuilder.getHeaderFor(attributeDescriptor), columnName, null));
        }
        return (Collection<Y2YColumnDefinitionModel>)result.build();
    }


    private boolean isLocalized(AttributeDescriptorModel attributeDescriptor)
    {
        return attributeDescriptor.getLocalized().booleanValue();
    }


    private void adjustPositionsAndConfiguration(Set<Y2YColumnDefinitionModel> columnDefinitions, Y2YStreamConfigurationModel configuration)
    {
        int pos = 0;
        for(Y2YColumnDefinitionModel def : columnDefinitions)
        {
            def.setPosition(Integer.valueOf(pos++));
            def.setStreamConfiguration(configuration);
        }
    }


    public List<Y2YColumnDefinitionModel> createDefaultColumnDefinitions(ComposedTypeModel composedType)
    {
        ImmutableList.Builder<Y2YColumnDefinitionModel> result = ImmutableList.builder();
        Set<AttributeDescriptorModel> attributeDescriptors = this.typeService.getAttributeDescriptorsForType(composedType);
        attributeDescriptors.stream()
                        .filter(this::isExportable)
                        .forEach(attribute -> result.addAll(createColumnDefinitions(attribute)));
        return (List<Y2YColumnDefinitionModel>)result.build();
    }


    private boolean isExportable(AttributeDescriptorModel ad)
    {
        return ((!(ad instanceof de.hybris.platform.core.model.type.RelationDescriptorModel) || ad.getProperty().booleanValue()) &&
                        !"pk".equals(ad.getQualifier()) && (ad
                        .getInitial().booleanValue() || ad.getWritable().booleanValue()) &&
                        !"itemtype".equals(ad.getQualifier()));
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
    public void setCommonI18NService(CommonI18NService commonI18NService)
    {
        this.commonI18NService = commonI18NService;
    }


    @Required
    public void setImpexHeaderBuilder(ImpexHeaderBuilder impexHeaderBuilder)
    {
        this.impexHeaderBuilder = impexHeaderBuilder;
    }


    @Required
    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }
}
