package com.hybris.backoffice.excel.template.populator;

import com.google.common.collect.ImmutableMap;
import com.hybris.backoffice.excel.data.ExcelAttribute;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;

public class DefaultExcelAttributeContext<ATTRIBUTE extends ExcelAttribute> implements ExcelAttributeContext<ATTRIBUTE>
{
    private final Map<String, Object> map;
    public static final String EXCEL_ATTRIBUTE = "excelAttribute";


    private DefaultExcelAttributeContext(Map<String, Object> map)
    {
        this.map = map;
    }


    public static <T extends ExcelAttribute> ExcelAttributeContext<T> ofExcelAttribute(T excelAttribute)
    {
        Map<String, Object> map = new HashMap<>();
        map.put("excelAttribute", excelAttribute);
        return new DefaultExcelAttributeContext<>(map);
    }


    public static <T extends ExcelAttribute> ExcelAttributeContext<T> ofMap(T excelAttribute, Map<String, Object> map)
    {
        if(!map.containsKey("excelAttribute"))
        {
            ImmutableMap immutableMap = ImmutableMap.builder().putAll(map).put("excelAttribute", excelAttribute).build();
            return new DefaultExcelAttributeContext<>((Map<String, Object>)immutableMap);
        }
        return new DefaultExcelAttributeContext<>((Map<String, Object>)ImmutableMap.copyOf(map));
    }


    public <TYPE> TYPE getAttribute(@Nonnull String name, @Nonnull Class<TYPE> type)
    {
        return type.cast(this.map.get(name));
    }


    public ATTRIBUTE getExcelAttribute(@Nonnull Class<ATTRIBUTE> type)
    {
        return (ATTRIBUTE)getAttribute("excelAttribute", type);
    }
}
