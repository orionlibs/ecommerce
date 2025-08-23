package com.hybris.backoffice.excel.template.mapper;

import java.util.Collection;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class JoinMapperTest
{
    private static final String INPUT = "product";
    @InjectMocks
    @Spy
    JoinMapper joinMapper;
    @Mock
    ExcelMapper mapper1;
    @Mock
    ExcelMapper mapper2;


    @Test
    public void shouldNotReturnDuplicatedObjects()
    {
        String firstObject = "object1";
        String secondObject = "object2";
        Mockito.when(this.mapper1.apply("product")).thenReturn(List.of("object1", "object2"));
        Mockito.when(this.mapper2.apply("product")).thenReturn(List.of("object1", "object2"));
        Collection result = this.joinMapper.apply("product");
        Assertions.assertThat(result).containsOnlyOnce(new Object[] {"object1", "object2"});
    }


    @Test
    public void shouldReturnMergedNotDuplicatedObjects()
    {
        String firstObject = "object1";
        String secondObject = "object2";
        String thirdObject = "object3";
        Mockito.when(this.mapper1.apply("product")).thenReturn(List.of("object1", "object2"));
        Mockito.when(this.mapper2.apply("product")).thenReturn(List.of("object3"));
        Collection result = this.joinMapper.apply("product");
        Assertions.assertThat(result).containsOnly(new Object[] {"object1", "object2", "object3"});
    }
}
