package com.hybris.datahub.service.spel;

import com.hybris.datahub.model.BaseDataItem;

public interface TransformationExpressionProcessor
{
    Object transform(BaseDataItem paramBaseDataItem, String paramString);
}
