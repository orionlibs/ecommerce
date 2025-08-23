package com.hybris.backoffice.excel.template.mapper;

import com.hybris.backoffice.excel.data.ExcelAttribute;
import java.util.Collection;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

public class JoinMapper<INPUT, ATTRIBUTE extends ExcelAttribute> implements ExcelMapper<INPUT, ATTRIBUTE>
{
    private ExcelMapper<INPUT, ATTRIBUTE> mapper1;
    private ExcelMapper<INPUT, ATTRIBUTE> mapper2;


    public Collection<ATTRIBUTE> apply(INPUT input)
    {
        return CollectionUtils.union((Iterable)this.mapper1.apply(input), (Iterable)this.mapper2.apply(input));
    }


    @Required
    public void setMapper1(ExcelMapper<INPUT, ATTRIBUTE> mapper1)
    {
        this.mapper1 = mapper1;
    }


    @Required
    public void setMapper2(ExcelMapper<INPUT, ATTRIBUTE> mapper2)
    {
        this.mapper2 = mapper2;
    }
}
