package de.hybris.platform.cms2.services.impl;

import de.hybris.platform.cms2.services.SortQueryData;
import de.hybris.platform.cms2.services.SortQueryDataRegistry;
import de.hybris.platform.servicelayer.internal.dao.Dao;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

public class DefaultSortQueryDataRegistry implements SortQueryDataRegistry, InitializingBean
{
    @Autowired
    private Set<SortQueryData> allSortQueries;
    private final Map<Class<? extends Dao>, List<SortQueryData>> sortQueryDataByType = new HashMap<>();


    public Optional<SortQueryData> getSortQueryData(Class<? extends Dao> typeClass, String sortCode)
    {
        return ((List<SortQueryData>)this.sortQueryDataByType.get(typeClass)).stream().filter(data -> data.getSortCode().equals(sortCode)).findFirst();
    }


    public Optional<SortQueryData> getDefaultSortQueryData(Class<? extends Dao> typeClass)
    {
        return ((List<SortQueryData>)this.sortQueryDataByType.get(typeClass)).stream().filter(data -> data.isDefault()).findFirst();
    }


    public Collection<SortQueryData> getAllSortQueryData()
    {
        return (Collection<SortQueryData>)this.sortQueryDataByType.values().stream().flatMap(queries -> queries.stream()).collect(Collectors.toList());
    }


    public void afterPropertiesSet() throws Exception
    {
        getAllSortQueries().stream().forEach(queryData -> putOrUpdateSortQueryData(queryData));
    }


    private void putOrUpdateSortQueryData(SortQueryData queryData)
    {
        List<SortQueryData> sortQueries = this.sortQueryDataByType.get(queryData.getTypeClass());
        if(Objects.isNull(sortQueries))
        {
            sortQueries = new ArrayList<>();
        }
        sortQueries.add(queryData);
        this.sortQueryDataByType.put(queryData.getTypeClass(), sortQueries);
    }


    protected Set<SortQueryData> getAllSortQueries()
    {
        return this.allSortQueries;
    }


    public void setAllSortQueries(Set<SortQueryData> allSortQueries)
    {
        this.allSortQueries = allSortQueries;
    }
}
