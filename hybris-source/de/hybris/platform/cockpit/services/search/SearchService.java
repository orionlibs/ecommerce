package de.hybris.platform.cockpit.services.search;

import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.search.SearchParameterDescriptor;
import de.hybris.platform.cockpit.model.search.SearchType;

public interface SearchService
{
    SearchType getSearchType(ObjectType paramObjectType);


    SearchType getSearchType(ObjectTemplate paramObjectTemplate);


    SearchType getSearchType(String paramString);


    boolean isSortable(PropertyDescriptor paramPropertyDescriptor);


    SearchParameterDescriptor getSearchDescriptor(PropertyDescriptor paramPropertyDescriptor);


    SearchParameterDescriptor getSearchDescriptor(PropertyDescriptor paramPropertyDescriptor, boolean paramBoolean);


    SearchProvider getSearchProvider();
}
