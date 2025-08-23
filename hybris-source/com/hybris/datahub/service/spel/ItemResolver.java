package com.hybris.datahub.service.spel;

import com.hybris.datahub.model.BaseDataItem;
import com.hybris.datahub.runtime.domain.DataHubPool;
import org.springframework.expression.EvaluationContext;

public interface ItemResolver
{
    Object resolve(EvaluationContext paramEvaluationContext, BaseDataItem paramBaseDataItem, String paramString, String... paramVarArgs);


    Object resolve(EvaluationContext paramEvaluationContext, BaseDataItem paramBaseDataItem, String paramString, DataHubPool paramDataHubPool, String... paramVarArgs);
}
