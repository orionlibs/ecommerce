package de.hybris.platform.adaptivesearch.services;

import de.hybris.platform.adaptivesearch.data.AsRankChange;
import de.hybris.platform.adaptivesearch.model.AbstractAsConfigurationModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import java.util.List;
import java.util.Optional;

public interface AsConfigurationService
{
    <T extends AbstractAsConfigurationModel> Optional<T> getConfigurationForUid(Class<T> paramClass, CatalogVersionModel paramCatalogVersionModel, String paramString);


    void refreshConfiguration(AbstractAsConfigurationModel paramAbstractAsConfigurationModel);


    <T extends AbstractAsConfigurationModel> T createConfiguration(Class<T> paramClass);


    <T extends AbstractAsConfigurationModel> T cloneConfiguration(T paramT);


    void saveConfiguration(AbstractAsConfigurationModel paramAbstractAsConfigurationModel);


    void removeConfiguration(AbstractAsConfigurationModel paramAbstractAsConfigurationModel);


    boolean isValid(AbstractAsConfigurationModel paramAbstractAsConfigurationModel);


    boolean moveConfiguration(AbstractAsConfigurationModel paramAbstractAsConfigurationModel, String paramString1, String paramString2, String paramString3);


    List<AsRankChange> rankBeforeConfiguration(AbstractAsConfigurationModel paramAbstractAsConfigurationModel, String paramString1, String paramString2, String... paramVarArgs);


    List<AsRankChange> rankAfterConfiguration(AbstractAsConfigurationModel paramAbstractAsConfigurationModel, String paramString1, String paramString2, String... paramVarArgs);


    AsRankChange rerankConfiguration(AbstractAsConfigurationModel paramAbstractAsConfigurationModel, String paramString1, String paramString2, int paramInt);
}
