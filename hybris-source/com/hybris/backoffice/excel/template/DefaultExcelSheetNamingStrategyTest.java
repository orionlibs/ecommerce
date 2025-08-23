package com.hybris.backoffice.excel.template;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DefaultExcelSheetNamingStrategyTest
{
    @InjectMocks
    private DefaultExcelSheetNamingStrategy strategy;
    @Mock
    private Workbook workbook;


    @Test
    public void verifyNameHasCorrectLength()
    {
        String maxCharName = RandomStringUtils.randomAlphabetic(31);
        String name = this.strategy.generateName(this.workbook, maxCharName);
        Assertions.assertThat(name).hasSize(31);
        Assertions.assertThat(name).isEqualTo(maxCharName);
    }


    @Test
    public void verifyTooLongNameTruncated()
    {
        String maxCharName = RandomStringUtils.randomAlphabetic(35);
        String firstTrySuffix = "_".concat("1");
        String name = this.strategy.generateName(this.workbook, maxCharName);
        Assertions.assertThat(name).hasSize(31);
        int endOfOriginalName = 31 - firstTrySuffix.length();
        Assertions.assertThat(name).isEqualTo(maxCharName.substring(0, endOfOriginalName).concat(firstTrySuffix));
    }


    @Test
    public void verifyNameIsCheckedInWorkbook()
    {
        String maxCharName = RandomStringUtils.randomAlphabetic(35);
        String firstTrySuffix = "_".concat("1");
        int endOfOriginalName = 31 - firstTrySuffix.length();
        String firstTryName = maxCharName.substring(0, endOfOriginalName).concat(firstTrySuffix);
        Sheet sheet = (Sheet)Mockito.mock(Sheet.class);
        Mockito.when(this.workbook.getSheet(firstTryName)).thenReturn(sheet);
        String name = this.strategy.generateName(this.workbook, maxCharName);
        Assertions.assertThat(name).hasSize(31);
        String secondTrySuffix = "_".concat("2");
        int endOfOriginalNameSecondTry = 31 - secondTrySuffix.length();
        Assertions.assertThat(name).isEqualTo(maxCharName.substring(0, endOfOriginalNameSecondTry).concat(secondTrySuffix));
    }


    @Test
    public void verifyNameWithSuffixExists()
    {
        String maxCharName = RandomStringUtils.randomAlphabetic(30);
        Sheet sheet = (Sheet)Mockito.mock(Sheet.class);
        Mockito.when(this.workbook.getSheet(maxCharName)).thenReturn(sheet);
        String name = this.strategy.generateName(this.workbook, maxCharName);
        Assertions.assertThat(name).hasSize(31);
        String secondTrySuffix = "_".concat("1");
        int endOfOriginalNameSecondTry = 31 - secondTrySuffix.length();
        Assertions.assertThat(name).isEqualTo(maxCharName.substring(0, endOfOriginalNameSecondTry).concat(secondTrySuffix));
    }


    @Test
    public void verifyShortNameWhichExists()
    {
        String firstTrySuffix = "_".concat("1");
        Sheet sheet = (Sheet)Mockito.mock(Sheet.class);
        Mockito.when(this.workbook.getSheet("Product")).thenReturn(sheet);
        String name = this.strategy.generateName(this.workbook, "Product");
        String expectedName = "Product".concat(firstTrySuffix);
        Assertions.assertThat(name).hasSize(expectedName.length());
        Assertions.assertThat(name).isEqualTo(expectedName);
    }
}
