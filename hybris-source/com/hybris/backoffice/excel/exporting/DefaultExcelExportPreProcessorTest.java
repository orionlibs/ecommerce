package com.hybris.backoffice.excel.exporting;

import com.hybris.backoffice.excel.data.ExcelAttribute;
import com.hybris.backoffice.excel.data.ExcelExportParams;
import com.hybris.backoffice.excel.data.SelectedAttribute;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.UnaryOperator;
import org.fest.assertions.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class DefaultExcelExportPreProcessorTest
{
    DefaultExcelExportPreProcessor excelExportPreProcessor;


    @Before
    public void setUp()
    {
        this.excelExportPreProcessor = new DefaultExcelExportPreProcessor();
    }


    @Test
    public void shouldAccumulateProcessedExcelExportParams()
    {
        ExcelExportParamsDecorator selectedAttributesAppendingDecorator = createADecorator(params -> {
            params.getSelectedAttributes().add((SelectedAttribute)Mockito.mock(SelectedAttribute.class));
            return params;
        });
        ExcelExportParamsDecorator additionalAttributesAppendingDecorator = createADecorator(params -> {
            params.getAdditionalAttributes().add((ExcelAttribute)Mockito.mock(ExcelAttribute.class));
            return params;
        });
        this.excelExportPreProcessor
                        .setDecorators(Arrays.asList(new ExcelExportParamsDecorator[] {selectedAttributesAppendingDecorator, additionalAttributesAppendingDecorator}));
        ExcelExportParams result = this.excelExportPreProcessor.process(new ExcelExportParams(new ArrayList(), new ArrayList(), new ArrayList()));
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getSelectedAttributes()).hasSize(1);
        Assertions.assertThat(result.getAdditionalAttributes()).hasSize(1);
    }


    @Test
    public void shouldRespectOrderOfInjectedDecorators()
    {
        ExcelExportParamsDecorator leastImportantDecorator = createADecorator(0);
        ExcelExportParamsDecorator mostImportantDecorator = createADecorator(100);
        ExcelExportParamsDecorator notSoImportantDecorator = createADecorator(50);
        List<ExcelExportParamsDecorator> decorators = Arrays.asList(new ExcelExportParamsDecorator[] {leastImportantDecorator, mostImportantDecorator, notSoImportantDecorator});
        this.excelExportPreProcessor.setDecorators(decorators);
        Assertions.assertThat(this.excelExportPreProcessor.getDecorators())
                        .containsExactly(new Object[] {leastImportantDecorator, notSoImportantDecorator, mostImportantDecorator});
    }


    ExcelExportParamsDecorator createADecorator(UnaryOperator<ExcelExportParams> decorationFunc)
    {
        int defaultOrder = 0;
        return createADecorator(decorationFunc, 0);
    }


    ExcelExportParamsDecorator createADecorator(int order)
    {
        UnaryOperator<ExcelExportParams> exceptionThrowingDecorationFunc = params -> {
            throw new AssertionError("expected decorate method not to be called");
        };
        return createADecorator(exceptionThrowingDecorationFunc, order);
    }


    ExcelExportParamsDecorator createADecorator(UnaryOperator<ExcelExportParams> decorationFunc, int order)
    {
        return (ExcelExportParamsDecorator)new Object(this, decorationFunc, order);
    }
}
