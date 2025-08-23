package de.hybris.platform.cockpit.daos.impl;

import de.hybris.platform.cockpit.daos.TypeDefinitionDao;
import de.hybris.platform.core.PK;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.springframework.beans.factory.annotation.Required;

public class DefaultTypeDefinitionDao implements TypeDefinitionDao
{
    private FlexibleSearchService flexibleSearchService;


    public List<TypeDefinitionDao.TypeDefinition> findAllTypeDefinitions()
    {
        List<TypeDefinitionDao.TypeDefinition> ret = new ArrayList<>();
        FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery("SELECT {t.pk},{t.code},{t.name:o},{t.supertype} FROM {ComposedType AS t} ORDER BY {t.code}");
        ArrayList<Class<?>> classList = new ArrayList<>();
        classList.add(PK.class);
        classList.add(String.class);
        classList.add(String.class);
        classList.add(PK.class);
        flexibleSearchQuery.setResultClassList(classList);
        SearchResult<Object> searchResult = this.flexibleSearchService.search(flexibleSearchQuery);
        List<Object> result = searchResult.getResult();
        for(Object object : result)
        {
            Iterator<PK> iterator = ((List)object).iterator();
            PK typePk = iterator.next();
            String code = (String)iterator.next();
            String name = (String)iterator.next();
            PK superTypePk = iterator.next();
            ret.add(new DefaultTypeDefinition(code, name, typePk, superTypePk));
        }
        return ret;
    }


    @Required
    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }
}
