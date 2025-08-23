package com.hybris.datahub.adapter;

import java.util.Set;

public interface AdapterServiceRegistry
{
    AdapterService findAdapterService(String paramString);


    Set<String> findAllAdapterTypes();
}
