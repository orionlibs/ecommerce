package com.hybris.backoffice.excel.importing.parser;

import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DefaultParserRegistryTest
{
    @Mock
    private ImportParameterParser parser;
    private final DefaultParserRegistry registry = new DefaultParserRegistry();


    @Before
    public void setUp()
    {
        this.registry.setParsers(Lists.newArrayList((Object[])new ImportParameterParser[] {this.parser}));
    }


    @Test
    public void shouldParserBeReturnedWhenReferenceFormatMatchesAnyParser()
    {
        String referenceFormat = "xx:xx";
        BDDMockito.given(Boolean.valueOf(this.parser.matches("xx:xx"))).willReturn(Boolean.valueOf(true));
        ImportParameterParser returnedParser = this.registry.getParser("xx:xx");
        Assertions.assertThat(returnedParser).isEqualTo(this.parser);
    }


    @Test
    public void shouldRuntimeExceptionBeThrownWhenNoParserCanBeFound()
    {
        BDDMockito.given(Boolean.valueOf(this.parser.matches((String)Matchers.any()))).willReturn(Boolean.valueOf(false));
        Assertions.assertThatThrownBy(() -> this.registry.getParser("any")).isInstanceOf(RuntimeException.class);
    }
}
