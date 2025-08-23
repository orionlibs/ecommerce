package com.hybris.backoffice.excel.template;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import org.fest.assertions.Assertions;
import org.junit.Test;

public class ExcelCollectionFormatterTest
{
    CollectionFormatter excelCollectionFormatter = (CollectionFormatter)new ExcelCollectionFormatter();


    @Test
    public void shouldFormatToCollection()
    {
        Collection<String> elements = this.excelCollectionFormatter.formatToCollection("{one},{two},{three},{four}");
        Assertions.assertThat(elements).containsOnly(new Object[] {"one", "two", "three", "four"});
    }


    @Test
    public void shouldFormatToString()
    {
        List<String> objects = Arrays.asList(new String[] {"apple", "orange", "pineapple"});
        String result = this.excelCollectionFormatter.formatToString(objects);
        Assertions.assertThat(result).isEqualTo("{apple},{orange},{pineapple}");
    }


    @Test
    public void shouldFormatToStringWithoutTrim()
    {
        List<String> objects = Arrays.asList(new String[] {"apple  ", "orange", "pineapple"});
        String result = this.excelCollectionFormatter.formatToString(objects);
        Assertions.assertThat(result).isEqualTo("{apple  },{orange},{pineapple}");
    }


    @Test
    public void shouldFormatToStringFromVarArgs()
    {
        String result = this.excelCollectionFormatter.formatToString(new String[] {"one", "two", "three"});
        Assertions.assertThat(result).isEqualTo("{one},{two},{three}");
    }


    @Test
    public void shouldFormatToCollectionElementsWithCommas()
    {
        Set<String> result = this.excelCollectionFormatter.formatToCollection("{one,two},{three,four}");
        Assertions.assertThat(result).containsOnly(new Object[] {"one,two", "three,four"});
    }
}
