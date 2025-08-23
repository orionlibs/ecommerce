package de.hybris.platform.personalizationservices.segment.dao.impl;

import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.personalizationservices.dao.impl.AbstractCxDao;
import de.hybris.platform.personalizationservices.model.CxSegmentModel;
import de.hybris.platform.personalizationservices.segment.dao.CxSegmentDao;
import de.hybris.platform.personalizationservices.segment.dao.CxSegmentDaoStrategy;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;

public class DefaultCxSegmentDao extends AbstractCxDao<CxSegmentModel> implements CxSegmentDao
{
    private List<CxSegmentDaoStrategy> cxSegmentDaoStrategies = Collections.emptyList();


    public DefaultCxSegmentDao()
    {
        super("CxSegment");
    }


    public Optional<CxSegmentModel> findSegmentByCode(String code)
    {
        ServicesUtil.validateParameterNotNull(code, "segment code must not be null");
        String query = "SELECT {pk} FROM {CxSegment} WHERE {code} LIKE ?code ";
        Map<String, Object> values = Collections.singletonMap("code", code);
        return querySingle("SELECT {pk} FROM {CxSegment} WHERE {code} LIKE ?code ", values);
    }


    public Collection<CxSegmentModel> findSegmentsByCodes(Collection<String> codes)
    {
        ServicesUtil.validateParameterNotNull(codes, "segment codes must not be null");
        String query = "SELECT {pk} FROM {CxSegment} WHERE {code} IN ( ?codes ) ";
        Map<String, Object> values = Collections.singletonMap("codes", codes);
        return queryList("SELECT {pk} FROM {CxSegment} WHERE {code} IN ( ?codes ) ", values);
    }


    public SearchPageData<CxSegmentModel> findSegments(Map<String, String> params, SearchPageData<?> pagination)
    {
        String query = "SELECT {pk} FROM {CxSegment} ORDER BY {code} ASC ";
        return queryList("SELECT {pk} FROM {CxSegment} ORDER BY {code} ASC ", Collections.emptyMap(), this.cxSegmentDaoStrategies, params, pagination);
    }


    @Autowired(required = false)
    public void setCxSegmentDaoStrategies(List<CxSegmentDaoStrategy> cxSegmentDaoStrategies)
    {
        this.cxSegmentDaoStrategies = cxSegmentDaoStrategies;
    }
}
