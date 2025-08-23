package de.hybris.platform.cms2.common.service;

import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;

public interface CollectionHelper
{
    Map<String, Object> mergeMaps(Map<String, Object> paramMap1, Map<String, Object> paramMap2, BinaryOperator<Object> paramBinaryOperator);


    Map<String, Object> mergeMaps(List<Map<String, Object>> paramList, BinaryOperator<Object> paramBinaryOperator);


    Map<String, Object> mergeMaps(List<Map<String, Object>> paramList);


    BinaryOperator<Object> defaultDeepMergeFunction();
}
