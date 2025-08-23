package com.hybris.backoffice.excel.exporting;

import com.hybris.backoffice.excel.data.ExcelAttribute;
import com.hybris.backoffice.excel.data.ExcelAttributeDescriptorAttribute;
import com.hybris.backoffice.excel.data.ExcelClassificationAttribute;
import com.hybris.backoffice.excel.data.ExcelExportResult;
import de.hybris.platform.core.model.ItemModel;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DefaultExcelExportClassificationWorkbookDecoratorTest
{
    @Spy
    DefaultExcelExportClassificationWorkbookDecorator decorator;


    @Test
    public void shouldExtractClassificationAttributes()
    {
        Workbook workbook = (Workbook)Mockito.mock(Workbook.class);
        ExcelExportResult excelExportResult = (ExcelExportResult)Mockito.mock(ExcelExportResult.class);
        ExcelClassificationAttribute excelClassificationAttribute = (ExcelClassificationAttribute)Mockito.mock(ExcelClassificationAttribute.class);
        ExcelAttributeDescriptorAttribute excelAttributeDescriptorAttribute = (ExcelAttributeDescriptorAttribute)Mockito.mock(ExcelAttributeDescriptorAttribute.class);
        List<ItemModel> itemModels = Collections.singletonList((ItemModel)Mockito.mock(ItemModel.class));
        BDDMockito.given(excelExportResult.getWorkbook()).willReturn(workbook);
        BDDMockito.given(excelExportResult.getSelectedAdditionalAttributes())
                        .willReturn(Arrays.asList(new ExcelAttribute[] {(ExcelAttribute)excelClassificationAttribute, (ExcelAttribute)excelAttributeDescriptorAttribute}));
        BDDMockito.given(excelExportResult.getSelectedItems()).willReturn(itemModels);
        ((DefaultExcelExportClassificationWorkbookDecorator)Mockito.doNothing().when(this.decorator)).decorate((Workbook)Matchers.any(), (Collection)Matchers.any(), (Collection)Matchers.any());
        this.decorator.decorate(excelExportResult);
        ((DefaultExcelExportClassificationWorkbookDecorator)BDDMockito.then(this.decorator).should()).decorate(workbook, Collections.singletonList(excelClassificationAttribute), itemModels);
    }
}
