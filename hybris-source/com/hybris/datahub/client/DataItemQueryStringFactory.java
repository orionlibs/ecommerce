package com.hybris.datahub.client;

import com.hybris.datahub.dto.filter.DataItemFilterDto;

public interface DataItemQueryStringFactory
{
    String createFrom(DataItemFilterDto paramDataItemFilterDto);
}
