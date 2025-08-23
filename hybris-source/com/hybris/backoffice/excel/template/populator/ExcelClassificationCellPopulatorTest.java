package com.hybris.backoffice.excel.template.populator;

import java.util.function.Function;
import org.fest.assertions.Assertions;
import org.junit.Test;

public class ExcelClassificationCellPopulatorTest
{
    @Test
    public void shouldBeAFunctionalInterface()
    {
        Assertions.assertThat(context -> null).isInstanceOf(Function.class);
    }
}
