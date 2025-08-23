package com.hybris.backoffice.solrsearch.resolvers;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.type.TypeService;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ItemtypeLabelValueResolverTest
{
    @InjectMocks
    private ItemtypeLabelValueResolver itemtypeLabelValueResolver;
    @Mock
    private TypeService typeService;
    @Mock
    private ItemModel source;


    @Test
    public void shouldProvideModel()
    {
        this.itemtypeLabelValueResolver.setTypeService(this.typeService);
        String itemtypeCode = "testItemtype";
        ComposedTypeModel itemtypeModel = (ComposedTypeModel)Mockito.mock(ComposedTypeModel.class);
        Mockito.when(this.source.getItemtype()).thenReturn("testItemtype");
        Mockito.when(this.typeService.getComposedTypeForCode("testItemtype")).thenReturn(itemtypeModel);
        Assertions.assertThat(this.itemtypeLabelValueResolver.provideModel(this.source)).isEqualTo(itemtypeModel);
    }
}
