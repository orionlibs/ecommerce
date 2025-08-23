package de.hybris.platform.commerceservices.backoffice.dropdownprovider;

import java.util.List;

public interface DropdownProvider
{
    List<? extends Object> getAllValues();


    String getName(Object paramObject);
}
