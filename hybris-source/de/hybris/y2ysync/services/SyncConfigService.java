package de.hybris.y2ysync.services;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.y2ysync.model.Y2YColumnDefinitionModel;
import de.hybris.y2ysync.model.Y2YStreamConfigurationContainerModel;
import de.hybris.y2ysync.model.Y2YStreamConfigurationModel;
import java.util.List;
import java.util.Set;

public interface SyncConfigService
{
    Y2YStreamConfigurationContainerModel createStreamConfigurationContainer(String paramString);


    Y2YStreamConfigurationContainerModel createStreamConfigurationContainer(String paramString, CatalogVersionModel paramCatalogVersionModel);


    Y2YStreamConfigurationContainerModel getStreamConfigurationContainerById(String paramString);


    Y2YColumnDefinitionModel createUntypedColumnDefinition(String paramString1, String paramString2);


    Y2YStreamConfigurationModel createStreamConfiguration(Y2YStreamConfigurationContainerModel paramY2YStreamConfigurationContainerModel, String paramString, Set<Y2YColumnDefinitionModel> paramSet);


    Y2YStreamConfigurationModel createStreamConfiguration(Y2YStreamConfigurationContainerModel paramY2YStreamConfigurationContainerModel, String paramString, Set<AttributeDescriptorModel> paramSet, Set<Y2YColumnDefinitionModel> paramSet1);


    Y2YStreamConfigurationModel createStreamConfiguration(Y2YStreamConfigurationContainerModel paramY2YStreamConfigurationContainerModel, String paramString, CatalogVersionModel paramCatalogVersionModel, Set<Y2YColumnDefinitionModel> paramSet);


    Y2YStreamConfigurationModel createStreamConfiguration(Y2YStreamConfigurationContainerModel paramY2YStreamConfigurationContainerModel, String paramString, CatalogVersionModel paramCatalogVersionModel, Set<AttributeDescriptorModel> paramSet, Set<Y2YColumnDefinitionModel> paramSet1);


    Y2YStreamConfigurationModel createStreamConfiguration(Y2YStreamConfigurationContainerModel paramY2YStreamConfigurationContainerModel, String paramString1, String paramString2, Set<Y2YColumnDefinitionModel> paramSet);


    Y2YStreamConfigurationModel createStreamConfiguration(Y2YStreamConfigurationContainerModel paramY2YStreamConfigurationContainerModel, String paramString1, String paramString2, Set<AttributeDescriptorModel> paramSet, Set<Y2YColumnDefinitionModel> paramSet1);


    Y2YStreamConfigurationModel createStreamConfiguration(Y2YStreamConfigurationContainerModel paramY2YStreamConfigurationContainerModel, String paramString1, String paramString2, CatalogVersionModel paramCatalogVersionModel, Set<Y2YColumnDefinitionModel> paramSet);


    Y2YStreamConfigurationModel createStreamConfiguration(Y2YStreamConfigurationContainerModel paramY2YStreamConfigurationContainerModel, String paramString1, String paramString2, CatalogVersionModel paramCatalogVersionModel, Set<AttributeDescriptorModel> paramSet,
                    Set<Y2YColumnDefinitionModel> paramSet1);


    List<Y2YColumnDefinitionModel> createDefaultColumnDefinitions(ComposedTypeModel paramComposedTypeModel);
}
