package com.hybris.backoffice.excel.template.mapper;

import com.hybris.backoffice.excel.data.ExcelAttributeDescriptorAttribute;
import com.hybris.backoffice.excel.template.filter.ExcelFilter;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.fest.assertions.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FromAttributeDescriptorsToExcelAttributesMapperTest extends AbstractExcelMapperTest
{
    @Mock
    private CommonI18NService commonI18NService;
    private FromAttributeDescriptorsToExcelAttributesMapper mapper = new FromAttributeDescriptorsToExcelAttributesMapper();


    @Before
    public void setUp()
    {
        this.mapper.setCommonI18NService(this.commonI18NService);
    }


    @Test
    public void shouldReturnCollectionOfExcelAttributeDescriptors()
    {
        String en = "en";
        String de = "de";
        AttributeDescriptorModel localizedAttributeDescriptor = mockAttributeDescriptorLocalized(true);
        AttributeDescriptorModel unlocalizedAttributeDescriptor = mockAttributeDescriptorLocalized(false);
        mockCommonI18NService(new String[] {"en", "de"});
        Collection<ExcelAttributeDescriptorAttribute> excelAttributes = this.mapper.apply(Lists.newArrayList((Object[])new AttributeDescriptorModel[] {localizedAttributeDescriptor, unlocalizedAttributeDescriptor}));
        Assertions.assertThat(excelAttributes.size()).isEqualTo(3);
        Assertions.assertThat((Collection)excelAttributes.stream().map(ExcelAttributeDescriptorAttribute::getIsoCode).collect(Collectors.toSet()))
                        .containsOnly(new Object[] {"en", "de", null});
    }


    @Test
    public void shouldReturnedCollectionBeFiltered()
    {
        String en = "en";
        String de = "de";
        AttributeDescriptorModel localizedAttributeDescriptor = mockAttributeDescriptorLocalized(true);
        AttributeDescriptorModel unlocalizedAttributeDescriptor = mockAttributeDescriptorLocalized(false);
        mockCommonI18NService(new String[] {"en", "de"});
        ExcelFilter<ExcelAttributeDescriptorAttribute> filter = attr -> StringUtils.equals(attr.getIsoCode(), "en");
        this.mapper.setFilters(Lists.newArrayList((Object[])new ExcelFilter[] {filter}));
        Collection<ExcelAttributeDescriptorAttribute> excelAttributes = this.mapper.apply(Lists.newArrayList((Object[])new AttributeDescriptorModel[] {localizedAttributeDescriptor, unlocalizedAttributeDescriptor}));
        Assertions.assertThat(excelAttributes.size()).isEqualTo(1);
        Assertions.assertThat((Collection)excelAttributes.stream().map(ExcelAttributeDescriptorAttribute::getIsoCode).collect(Collectors.toSet()))
                        .containsOnly(new Object[] {"en"});
    }


    protected void mockCommonI18NService(String... isoCodes)
    {
        List<LanguageModel> languageModels = new ArrayList<>(isoCodes.length);
        Lists.newArrayList((Object[])isoCodes).forEach(isoCode -> {
            LanguageModel language = (LanguageModel)Mockito.mock(LanguageModel.class);
            BDDMockito.given(language.getActive()).willReturn(Boolean.valueOf(true));
            BDDMockito.given(language.getIsocode()).willReturn(isoCode);
            languageModels.add(language);
        });
        BDDMockito.given(this.commonI18NService.getAllLanguages()).willReturn(languageModels);
    }
}
