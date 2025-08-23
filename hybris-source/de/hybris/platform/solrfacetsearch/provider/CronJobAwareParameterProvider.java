package de.hybris.platform.solrfacetsearch.provider;

import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import java.util.Map;

public interface CronJobAwareParameterProvider
{
    Map<String, Object> createParameters(CronJobModel paramCronJobModel, IndexConfig paramIndexConfig, IndexedType paramIndexedType);
}
