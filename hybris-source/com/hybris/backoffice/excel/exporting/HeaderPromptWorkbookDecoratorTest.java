package com.hybris.backoffice.excel.exporting;

import com.hybris.backoffice.excel.data.ExcelAttribute;
import com.hybris.backoffice.excel.data.ExcelAttributeDescriptorAttribute;
import com.hybris.backoffice.excel.data.ExcelExportResult;
import com.hybris.backoffice.excel.template.ClassificationIncludedHeaderPromptPopulator;
import com.hybris.backoffice.excel.template.mapper.ExcelMapper;
import com.hybris.backoffice.excel.template.populator.ExcelSheetPopulator;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import java.util.Collections;
import org.apache.poi.ss.usermodel.Workbook;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.BDDMockito;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class HeaderPromptWorkbookDecoratorTest
{
    @Mock
    private ExcelMapper<ExcelExportResult, ExcelAttributeDescriptorAttribute> mapper;
    @Spy
    private ClassificationIncludedHeaderPromptPopulator populator;
    private HeaderPromptWorkbookDecorator decorator = new HeaderPromptWorkbookDecorator();


    @Before
    public void setUp()
    {
        this.decorator.setMapper(this.mapper);
        this.decorator.setHeaderPromptPopulator((ExcelSheetPopulator)this.populator);
    }


    @Test
    public void shouldCurrentAvailableAttributesBeMergedWithItemsAttributes()
    {
        ExcelAttributeDescriptorAttribute excelAttributeDescriptorAttribute = new ExcelAttributeDescriptorAttribute((AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class), null);
        ExcelExportResult result = new ExcelExportResult((Workbook)Mockito.mock(Workbook.class), Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), Lists.newArrayList((Object[])new ExcelAttribute[] {(ExcelAttribute)excelAttributeDescriptorAttribute}));
        BDDMockito.given(this.mapper.apply(result)).willReturn(Lists.newArrayList((Object[])new ExcelAttributeDescriptorAttribute[] {(ExcelAttributeDescriptorAttribute)Mockito.mock(ExcelAttributeDescriptorAttribute.class)}));
        ((ClassificationIncludedHeaderPromptPopulator)Mockito.doNothing().when(this.populator)).populate((ExcelExportResult)Matchers.any());
        this.decorator.decorate(result);
        Object object = new Object(this);
        ((ClassificationIncludedHeaderPromptPopulator)Mockito.verify(this.populator)).populate((ExcelExportResult)Mockito.argThat((ArgumentMatcher)object));
    }
}
