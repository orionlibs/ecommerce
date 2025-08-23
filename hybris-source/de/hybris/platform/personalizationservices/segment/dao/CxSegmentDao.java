package de.hybris.platform.personalizationservices.segment.dao;

import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.personalizationservices.model.CxSegmentModel;
import de.hybris.platform.servicelayer.internal.dao.Dao;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

public interface CxSegmentDao extends Dao
{
    Optional<CxSegmentModel> findSegmentByCode(String paramString);


    SearchPageData<CxSegmentModel> findSegments(Map<String, String> paramMap, SearchPageData<?> paramSearchPageData);


    default Collection<CxSegmentModel> findSegmentsByCodes(Collection<String> codes)
    {
        return Collections.emptyList();
    }
}
