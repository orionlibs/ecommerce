package com.hybris.backoffice.excel.template.mapper;

import com.hybris.backoffice.excel.data.ExcelExportResult;
import com.hybris.backoffice.excel.template.filter.ExcelFilter;
import com.hybris.backoffice.excel.template.sheet.ExcelSheetService;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import java.util.Collection;
import java.util.stream.IntStream;
import org.apache.poi.ss.usermodel.Workbook;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FromExcelResultToAttributeDescriptorsMapperTest extends AbstractExcelMapperTest
{
    @Mock
    private ExcelMapper<String, AttributeDescriptorModel> helperMapper;
    @Mock
    private ExcelSheetService sheetService;
    private FromExcelResultToAttributeDescriptorsMapper mapper = new FromExcelResultToAttributeDescriptorsMapper();


    @Before
    public void setUp()
    {
        this.mapper.setMapper(this.helperMapper);
        this.mapper.setExcelSheetService(this.sheetService);
    }


    @Test
    public void shouldReturnCollectionOfAttributeDescriptors()
    {
        String sheet1 = "sheet1";
        String sheet2 = "sheet2";
        Workbook workbook = mockWorkbook(new String[] {"sheet1", "sheet2"});
        ((ExcelSheetService)Mockito.doAnswer(invocationOnMock -> invocationOnMock.getArguments()[1]).when(this.sheetService)).findTypeCodeForSheetName((Workbook)Matchers.any(), (String)Matchers.any());
        BDDMockito.given(this.helperMapper.apply("sheet1")).willReturn(Lists.newArrayList((Object[])new AttributeDescriptorModel[] {(AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class)}));
        BDDMockito.given(this.helperMapper.apply("sheet2"))
                        .willReturn(Lists.newArrayList((Object[])new AttributeDescriptorModel[] {(AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class), (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class)}));
        Collection<AttributeDescriptorModel> attributeDescriptors = this.mapper.apply(new ExcelExportResult(workbook));
        Assertions.assertThat(attributeDescriptors.size()).isEqualTo(3);
    }


    @Test
    public void shouldReturnedCollectionBeFiltered()
    {
        String sheet1 = "sheet1";
        String sheet2 = "sheet2";
        Workbook workbook = mockWorkbook(new String[] {"sheet1", "sheet2"});
        ((ExcelSheetService)Mockito.doAnswer(invocationOnMock -> invocationOnMock.getArguments()[1]).when(this.sheetService)).findTypeCodeForSheetName((Workbook)Matchers.any(), (String)Matchers.any());
        AttributeDescriptorModel uniqueAttributeDescriptor1 = mockAttributeDescriptorUnique(true);
        BDDMockito.given(this.helperMapper.apply("sheet1")).willReturn(Lists.newArrayList((Object[])new AttributeDescriptorModel[] {uniqueAttributeDescriptor1}));
        AttributeDescriptorModel uniqueAttributeDescriptor2 = mockAttributeDescriptorUnique(true);
        AttributeDescriptorModel nonUniqueAttributeDescriptor = mockAttributeDescriptorUnique(false);
        BDDMockito.given(this.helperMapper.apply("sheet2")).willReturn(Lists.newArrayList((Object[])new AttributeDescriptorModel[] {uniqueAttributeDescriptor2, nonUniqueAttributeDescriptor}));
        this.mapper.setFilters(Lists.newArrayList((Object[])new ExcelFilter[] {getUniqueFilter()}));
        Collection<AttributeDescriptorModel> attributeDescriptors = this.mapper.apply(new ExcelExportResult(workbook));
        Assertions.assertThat(attributeDescriptors.size()).isEqualTo(2);
        Assertions.assertThat(attributeDescriptors).containsOnly((Object[])new AttributeDescriptorModel[] {uniqueAttributeDescriptor1, uniqueAttributeDescriptor2});
        Assertions.assertThat(attributeDescriptors).doesNotContain((Object[])new AttributeDescriptorModel[] {nonUniqueAttributeDescriptor});
    }


    protected Workbook mockWorkbook(String... sheetNames)
    {
        Workbook workbook = (Workbook)Mockito.mock(Workbook.class);
        IntStream.range(0, sheetNames.length).forEach(idx -> BDDMockito.given(workbook.getSheetName(idx)).willReturn(sheetNames[idx]));
        BDDMockito.given(Integer.valueOf(workbook.getNumberOfSheets())).willReturn(Integer.valueOf(sheetNames.length));
        return workbook;
    }
}
