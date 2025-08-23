package com.hybris.backoffice.excel.validators;

import com.hybris.backoffice.excel.validators.data.ValidationMessage;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.configuration.Configuration;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

@RunWith(Parameterized.class)
public class ExcelMediaFilenameExtensionValidatorTest
{
    @Mock
    public ConfigurationService configurationService;
    @InjectMocks
    private final ExcelMediaFilenameExtensionValidator validator = new ExcelMediaFilenameExtensionValidator();
    @Rule
    public MockitoRule rule = MockitoJUnit.rule();
    @Parameter(0)
    public String input;
    @Parameter(1)
    public boolean output;


    @Before
    public void setUp()
    {
        Configuration configuration = (Configuration)Mockito.mock(Configuration.class);
        BDDMockito.given(configuration.getString("excel.available.media.extensions", ""))
                        .willReturn("jpg,PNG,gif, BMP,jpeg");
        BDDMockito.given(this.configurationService.getConfiguration()).willReturn(configuration);
    }


    @Parameters
    public static Collection<Object[]> data()
    {
        return Arrays.asList(new Object[][] {{"lkjlkj.xml",
                        Boolean.valueOf(false)}, {"lkj.jpg",
                        Boolean.valueOf(true)}, {"rlk3ji.png",
                        Boolean.valueOf(true)}, {"kjhj.bmp",
                        Boolean.valueOf(true)}, {"lekjk.JPEG",
                        Boolean.valueOf(true)}});
    }


    @Test
    public void should()
    {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("filePath", this.input);
        List<ValidationMessage> validationMessages = this.validator.validateSingleValue(null, parameters);
        Assertions.assertThat(validationMessages.isEmpty()).isEqualTo(this.output);
    }
}
