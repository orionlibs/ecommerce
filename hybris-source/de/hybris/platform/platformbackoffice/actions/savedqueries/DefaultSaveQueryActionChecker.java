package de.hybris.platform.platformbackoffice.actions.savedqueries;

import com.hybris.backoffice.widgets.advancedsearch.impl.AdvancedSearchData;
import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.dataaccess.facades.type.exceptions.TypeNotFoundException;
import de.hybris.platform.platformbackoffice.services.converters.BackofficeSavedQueryValueConverter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class DefaultSaveQueryActionChecker implements SaveQueryActionChecker
{
    private TypeFacade typeFacade;
    private List<BackofficeSavedQueryValueConverter> converters = new ArrayList<>();


    public Collection<SaveQueryInvalidCondition> check(AdvancedSearchData advancedSearchData) throws TypeNotFoundException
    {
        List<SaveQueryInvalidCondition> invalidConditions = new ArrayList<>();
        DataType dataType = this.typeFacade.load(advancedSearchData.getTypeCode());
        for(String fieldName : advancedSearchData.getSearchFields())
        {
            List<SaveQueryInvalidCondition> invalidFieldConditions = (List<SaveQueryInvalidCondition>)advancedSearchData.getConditions(fieldName).stream().map(condition -> dataType.getAttribute(condition.getFieldType().getName())).filter(attr -> !canConvert(attr))
                            .map(attribute -> new SaveQueryInvalidCondition(attribute.getQualifier())).collect(Collectors.toList());
            invalidConditions.addAll(invalidFieldConditions);
        }
        return invalidConditions;
    }


    private boolean canConvert(DataAttribute dataAttribute)
    {
        return this.converters.stream().anyMatch(converter -> converter.canHandle(dataAttribute));
    }


    public void setTypeFacade(TypeFacade typeFacade)
    {
        this.typeFacade = typeFacade;
    }


    public void setConverters(List<BackofficeSavedQueryValueConverter> converters)
    {
        this.converters = converters;
    }
}
