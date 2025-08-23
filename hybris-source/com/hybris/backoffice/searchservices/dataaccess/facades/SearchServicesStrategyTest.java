package com.hybris.backoffice.searchservices.dataaccess.facades;

import com.hybris.backoffice.search.services.BackofficeFacetSearchConfigService;
import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.searchservices.enums.SnFieldType;
import de.hybris.platform.searchservices.model.SnFieldModel;
import de.hybris.platform.searchservices.model.SnIndexTypeModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class SearchServicesStrategyTest
{
    @Mock
    private BackofficeFacetSearchConfigService backofficeFacetSearchConfigService;
    @InjectMocks
    private SearchServicesStrategy searchServicesStrategy;
    private static final String TEST_TYPE_CODE = "typeCode";
    private static final String TEST_FIELD_NAME = "testFieldName";
    private static final String TEST_TYPE = "java.lang.String";


    @Before
    public void setUp()
    {
        Map<String, String> typeMappings = new HashMap<>();
        typeMappings.put("TEXT", "java.lang.String");
        typeMappings.put("BOOLEAN", "java.lang.Boolean");
        this.searchServicesStrategy.setTypeMappings(typeMappings);
    }


    @Test
    public void checkGetFieldTypeAndIsLocalized() throws Exception
    {
        SnIndexTypeModel indexTypeModel = createIndexTypeModel();
        Mockito.when(this.backofficeFacetSearchConfigService.getIndexedTypeModel("typeCode")).thenReturn(indexTypeModel);
        Assertions.assertThat(this.searchServicesStrategy.getFieldType("typeCode", "testFieldName")).isEqualTo("java.lang.String");
        Assertions.assertThat(this.searchServicesStrategy.isLocalized("typeCode", "testFieldName")).isEqualTo(true);
    }


    private SnIndexTypeModel createIndexTypeModel()
    {
        SnIndexTypeModel indexTypeModel = (SnIndexTypeModel)Mockito.mock(SnIndexTypeModel.class);
        SnFieldModel snFieldModel = (SnFieldModel)Mockito.mock(SnFieldModel.class);
        List<SnFieldModel> fieldModelList = new ArrayList<>();
        fieldModelList.add(snFieldModel);
        Mockito.when(snFieldModel.getId()).thenReturn("testFieldName");
        Mockito.when(snFieldModel.getFieldType()).thenReturn(SnFieldType.TEXT);
        Mockito.when(snFieldModel.getLocalized()).thenReturn(Boolean.valueOf(true));
        Mockito.when(indexTypeModel.getFields()).thenReturn(fieldModelList);
        return indexTypeModel;
    }
}
