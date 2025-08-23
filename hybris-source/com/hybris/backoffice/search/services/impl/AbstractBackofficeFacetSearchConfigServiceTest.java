package com.hybris.backoffice.search.services.impl;

import com.google.common.collect.Lists;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AbstractBackofficeFacetSearchConfigServiceTest
{
    @Mock
    private AbstractBackofficeFacetSearchConfigService abstractBackofficeFacetSearchConfigService;


    @Test
    public void shouldGetSuperTypeCodes()
    {
        ComposedTypeModel composedType = (ComposedTypeModel)Mockito.mock(ComposedTypeModel.class);
        List<ComposedTypeModel> typeCodes = Lists.newArrayList();
        typeCodes.add(composedType);
        typeCodes.addAll(composedType.getAllSuperTypes());
        ((AbstractBackofficeFacetSearchConfigService)Mockito.doCallRealMethod().when(this.abstractBackofficeFacetSearchConfigService)).getWithSuperTypeCodes(composedType);
        Assert.assertEquals(typeCodes, this.abstractBackofficeFacetSearchConfigService.getWithSuperTypeCodes(composedType));
    }
}
