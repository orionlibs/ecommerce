package com.hybris.backoffice.excel.data;

import java.util.Collections;
import org.assertj.core.api.AbstractThrowableAssert;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class ExcelExportParamsTest
{
    @Test
    public void shouldAcceptEmptyListsAsParameters()
    {
        ExcelExportParams excelExportParams = new ExcelExportParams(Collections.emptyList(), Collections.emptyList(), Collections.emptyList());
        Assertions.assertThat(excelExportParams).isNotNull();
    }


    @Test
    public void shouldNotAcceptNullItemsToExport()
    {
        NullPointerException caughtException = null;
        try
        {
            new ExcelExportParams(null, Collections.emptyList(), Collections.emptyList());
        }
        catch(NullPointerException e)
        {
            caughtException = e;
        }
        ((AbstractThrowableAssert)Assertions.assertThat(caughtException).isNotNull()).hasMessage("ItemsToExport collection cannot be null");
    }


    @Test
    public void shouldNotAcceptNullSelectedAttributes()
    {
        NullPointerException caughtException = null;
        try
        {
            new ExcelExportParams(Collections.emptyList(), null, Collections.emptyList());
        }
        catch(NullPointerException e)
        {
            caughtException = e;
        }
        ((AbstractThrowableAssert)Assertions.assertThat(caughtException).isNotNull()).hasMessage("SelectedAttributes collection cannot be null");
    }


    @Test
    public void shouldNotAcceptNullAdditionalAttributes()
    {
        NullPointerException caughtException = null;
        try
        {
            new ExcelExportParams(Collections.emptyList(), Collections.emptyList(), null);
        }
        catch(NullPointerException e)
        {
            caughtException = e;
        }
        ((AbstractThrowableAssert)Assertions.assertThat(caughtException).isNotNull()).hasMessage("AdditionalAttributes collection cannot be null");
    }
}
