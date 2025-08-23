package com.hybris.backoffice.excel.validators.classification;

import com.hybris.backoffice.excel.data.ImportParameters;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class ExcelUnitUtils
{
    static final String UNIT_KEY = "unit";
    static final String VALUE_KEY = "value";


    private ExcelUnitUtils()
    {
        throw new AssertionError();
    }


    static ImportParameters getImportParametersForValue(ImportParameters importParameters, String cellValue)
    {
        String typeCode = importParameters.getTypeCode();
        String isoCode = importParameters.getIsoCode();
        String entryRef = importParameters.getEntryRef();
        List<Map<String, String>> multiValueParametersWithoutUnits = (List<Map<String, String>>)importParameters.getMultiValueParameters().stream().peek(m -> m.remove("unit")).collect(Collectors.toList());
        return new ImportParameters(typeCode, isoCode, cellValue, entryRef, multiValueParametersWithoutUnits);
    }


    static String extractUnitFromParams(Map<String, String> params)
    {
        return params.getOrDefault("unit", "");
    }


    static String extractValueFromParams(Map<String, String> params)
    {
        return params.getOrDefault("value", "");
    }
}
