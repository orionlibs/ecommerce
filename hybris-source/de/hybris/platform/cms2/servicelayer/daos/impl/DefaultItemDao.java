package de.hybris.platform.cms2.servicelayer.daos.impl;

import de.hybris.platform.cms2.servicelayer.daos.ItemDao;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.type.TypeService;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Required;

public class DefaultItemDao extends AbstractItemDao implements ItemDao
{
    private TypeService typeService;


    public <T extends de.hybris.platform.core.model.ItemModel> Optional<T> getItemByUniqueAttributesValues(String typeCode, Map<String, Object> attributeValues)
    {
        String rootType = getTypeService().getUniqueModelRootType(typeCode);
        Set<String> uniqueAttributes = getTypeService().getUniqueAttributes(rootType);
        String filter = uniqueAttributes.stream().map(qualifier -> String.format("{%1$s} =?%1$s ", new Object[] {qualifier})).collect(Collectors.joining(" AND "));
        String query = String.format("SELECT {pk} FROM {%s} WHERE %s", new Object[] {typeCode, filter});
        FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(query);
        Map<String, Object> uniqueAttributeValues = (Map<String, Object>)attributeValues.entrySet().stream().filter(entry -> uniqueAttributes.contains(entry.getKey())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        uniqueAttributes.forEach(attribute -> {
            if(!uniqueAttributeValues.containsKey(attribute))
            {
                throw new IllegalArgumentException("The ItemModel can not be retrieved for typeCode " + typeCode + ". Reason: the value is not provided for unique attribute \"" + attribute + "\"");
            }
        });
        flexibleSearchQuery.addQueryParameters(uniqueAttributeValues);
        SearchResult<T> result = search(flexibleSearchQuery);
        return result.getResult().stream().filter(Objects::nonNull).findFirst();
    }


    protected TypeService getTypeService()
    {
        return this.typeService;
    }


    @Required
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }
}
