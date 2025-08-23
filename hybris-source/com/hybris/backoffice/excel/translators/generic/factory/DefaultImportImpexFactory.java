package com.hybris.backoffice.excel.translators.generic.factory;

import com.hybris.backoffice.excel.data.Impex;
import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.translators.generic.RequiredAttribute;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Required;

public class DefaultImportImpexFactory implements ImportImpexFactory
{
    private List<ImportImpexFactoryStrategy> strategies;


    public Impex create(RequiredAttribute rootUniqueAttribute, ImportParameters importParameters)
    {
        Optional<ImportImpexFactoryStrategy> foundStrategy = this.strategies.stream().filter(strategy -> strategy.canHandle(rootUniqueAttribute, importParameters)).findFirst();
        return foundStrategy.<Impex>map(strategy -> strategy.create(rootUniqueAttribute, importParameters)).orElse(new Impex());
    }


    public List<ImportImpexFactoryStrategy> getStrategies()
    {
        return this.strategies;
    }


    @Required
    public void setStrategies(List<ImportImpexFactoryStrategy> strategies)
    {
        this.strategies = strategies;
    }
}
