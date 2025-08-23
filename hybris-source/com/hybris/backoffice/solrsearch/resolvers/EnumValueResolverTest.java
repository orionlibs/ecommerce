package com.hybris.backoffice.solrsearch.resolvers;

import de.hybris.platform.core.HybrisEnumValue;
import de.hybris.platform.core.model.product.ProductModel;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class EnumValueResolverTest
{
    protected static final String UNAPPROVED = "unapproved";
    @InjectMocks
    EnumValueResolver enumValueResolver;
    @Mock
    private ProductModel product;
    @Mock
    private HybrisEnumValue hybrisEnum;


    @Test
    public void shouldWorkOnlyForHybrisEnumValues() throws Exception
    {
        boolean resultForHybrisEnum = this.enumValueResolver.isHybrisEnum(this.hybrisEnum);
        boolean resultForProductModel = this.enumValueResolver.isHybrisEnum(this.product);
        Assertions.assertThat(resultForHybrisEnum).isTrue();
        Assertions.assertThat(resultForProductModel).isFalse();
    }


    @Test
    public void shouldReturnCodeValueForEnum()
    {
        Mockito.when(this.hybrisEnum.getCode()).thenReturn("unapproved");
        String result = this.enumValueResolver.getEnumValue(this.hybrisEnum);
        Assertions.assertThat(result).isEqualTo("unapproved");
    }
}
