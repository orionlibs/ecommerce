package com.hybris.backoffice.excel.validators.engine;

import com.hybris.backoffice.daos.BackofficeValidationDao;
import com.hybris.backoffice.excel.validators.engine.converters.ExcelBooleanValueConverter;
import com.hybris.backoffice.excel.validators.engine.converters.ExcelMultiValueConverter;
import com.hybris.backoffice.excel.validators.engine.converters.ExcelNullValueConverter;
import com.hybris.backoffice.excel.validators.engine.converters.ExcelNumberValueConverter;
import com.hybris.backoffice.excel.validators.engine.converters.ExcelStringValueConverter;
import com.hybris.backoffice.excel.validators.engine.converters.ExcelValueConverter;
import com.hybris.backoffice.excel.validators.engine.converters.ExcelValueConverterRegistry;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.validation.enums.Severity;
import de.hybris.platform.validation.exceptions.HybrisConstraintViolation;
import de.hybris.platform.validation.model.constraints.ConstraintGroupModel;
import de.hybris.platform.validation.services.ValidationService;
import de.hybris.platform.validation.services.impl.DefaultHybrisConstraintViolation;
import de.hybris.platform.validation.services.impl.LocalizedHybrisConstraintViolation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public abstract class AbstractValidationEngineAwareStrategyTest
{
    @Mock
    protected TypeService typeService;
    @Mock
    protected ValidationService validationService;
    @Mock
    protected BackofficeValidationDao validationDao;
    protected ExcelValueConverterRegistry converterRegistry = new ExcelValueConverterRegistry();


    @Before
    public void setup()
    {
        List<ExcelValueConverter> converters = new ArrayList<>();
        converters.add(new ExcelBooleanValueConverter());
        converters.add(new ExcelMultiValueConverter());
        converters.add(new ExcelNullValueConverter());
        converters.add(new ExcelNumberValueConverter());
        converters.add(new ExcelStringValueConverter());
        this.converterRegistry.setConverters(converters);
        mockKnownModelClasses();
        mockConstraintGroups(new String[] {"default"});
    }


    protected void mockKnownModelClasses()
    {
        Class<ProductModel> productModelClass = ProductModel.class;
        BDDMockito.given(this.typeService.getModelClass("Product")).willReturn(productModelClass);
    }


    protected void mockConstraintGroups(String... constraintGroups)
    {
        Mockito.lenient().when(this.validationDao.getConstraintGroups(Arrays.asList(constraintGroups)))
                        .thenReturn(prepareConstraintsGroupModels(constraintGroups));
    }


    private Collection<ConstraintGroupModel> prepareConstraintsGroupModels(String... constraintGroups)
    {
        Collection<ConstraintGroupModel> models = new ArrayList<>();
        for(String group : constraintGroups)
        {
            ConstraintGroupModel model = new ConstraintGroupModel();
            model.setId(group);
            models.add(model);
        }
        return models;
    }


    protected void mockValidateValue(String qualifier, Set<HybrisConstraintViolation> violations)
    {
        BDDMockito.given(this.validationService.validateValue((Class)Matchers.eq(ProductModel.class), (String)Matchers.eq(qualifier), Matchers.any(), Matchers.anyCollection()))
                        .willReturn(violations);
    }


    protected HybrisConstraintViolation prepareConstraintViolation(String message, Severity severity)
    {
        DefaultHybrisConstraintViolation violation = (DefaultHybrisConstraintViolation)Mockito.mock(DefaultHybrisConstraintViolation.class);
        BDDMockito.given(violation.getLocalizedMessage()).willReturn(message);
        BDDMockito.given(violation.getViolationSeverity()).willReturn(severity);
        return (HybrisConstraintViolation)violation;
    }


    protected HybrisConstraintViolation prepareLocalizedConstraintViolation(String message, Severity severity, Locale locale)
    {
        LocalizedHybrisConstraintViolation violation = (LocalizedHybrisConstraintViolation)Mockito.mock(LocalizedHybrisConstraintViolation.class);
        BDDMockito.given(violation.getLocalizedMessage()).willReturn(message);
        BDDMockito.given(violation.getViolationSeverity()).willReturn(severity);
        Mockito.lenient().when(violation.getViolationLanguage()).thenReturn(locale);
        return (HybrisConstraintViolation)violation;
    }
}
