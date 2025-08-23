package de.hybris.platform.adaptivesearch.services;

import de.hybris.platform.adaptivesearch.context.AsSearchProfileContext;
import de.hybris.platform.adaptivesearch.data.AsConfigurationHolder;
import de.hybris.platform.adaptivesearch.data.AsSearchProfileActivationGroup;
import de.hybris.platform.adaptivesearch.data.AsSearchProfileResult;
import de.hybris.platform.adaptivesearch.model.AbstractAsSearchProfileModel;
import java.util.List;

public interface AsSearchProfileCalculationService
{
    AsSearchProfileResult createResult(AsSearchProfileContext paramAsSearchProfileContext);


    <T, R> AsConfigurationHolder<T, R> createConfigurationHolder(AsSearchProfileContext paramAsSearchProfileContext, T paramT);


    <T, R> AsConfigurationHolder<T, R> createConfigurationHolder(AsSearchProfileContext paramAsSearchProfileContext, T paramT, Object paramObject);


    AsSearchProfileResult calculate(AsSearchProfileContext paramAsSearchProfileContext, List<AbstractAsSearchProfileModel> paramList);


    AsSearchProfileResult calculate(AsSearchProfileContext paramAsSearchProfileContext, AsSearchProfileResult paramAsSearchProfileResult, List<AbstractAsSearchProfileModel> paramList);


    AsSearchProfileResult calculateGroups(AsSearchProfileContext paramAsSearchProfileContext, List<AsSearchProfileActivationGroup> paramList);


    AsSearchProfileResult calculateGroups(AsSearchProfileContext paramAsSearchProfileContext, AsSearchProfileResult paramAsSearchProfileResult, List<AsSearchProfileActivationGroup> paramList);
}
