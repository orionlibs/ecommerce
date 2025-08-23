package com.hybris.backoffice.excel.translators;

import com.hybris.backoffice.excel.data.ImpexValue;
import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.template.filter.ExcelFilter;
import de.hybris.platform.catalog.CatalogTypeService;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.servicelayer.type.TypeService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ExcelCatalogVersionTypeTranslatorTest
{
    @Mock
    private ExcelFilter<AttributeDescriptorModel> uniqueFilter;
    @Mock
    private ExcelFilter<AttributeDescriptorModel> mandatoryFilter;
    @Mock
    private TypeService typeService;
    @Mock
    public CatalogTypeService catalogTypeService;
    @InjectMocks
    private ExcelCatalogVersionTypeTranslator translator;


    @Before
    public void setUp()
    {
        ((ExcelFilter)Mockito.doAnswer(inv -> ((AttributeDescriptorModel)inv.getArguments()[0]).getUnique()).when(this.uniqueFilter)).test(Matchers.any());
        Mockito.when(this.catalogTypeService.getCatalogVersionContainerAttribute((String)Matchers.any())).thenReturn("catalogVersion");
    }


    @Test
    public void shouldExportDataBeNullSafe()
    {
        Assertions.assertThat(this.translator.exportData(null).isPresent()).isTrue();
    }


    @Test
    public void shouldExportedDataBeInProperFormat()
    {
        String id = "defaultcatalog";
        String version = "Staged";
        CatalogVersionModel catalogVersion = (CatalogVersionModel)Mockito.mock(CatalogVersionModel.class);
        CatalogModel catalog = (CatalogModel)Mockito.mock(CatalogModel.class);
        BDDMockito.given(catalog.getId()).willReturn("defaultcatalog");
        BDDMockito.given(catalogVersion.getVersion()).willReturn("Staged");
        BDDMockito.given(catalogVersion.getCatalog()).willReturn(catalog);
        Objects.requireNonNull(String.class);
        String output = this.translator.exportData(catalogVersion).map(String.class::cast).get();
        Assertions.assertThat(output).isEqualTo(String.format("%s:%s", new Object[] {"defaultcatalog", "Staged"}));
    }


    @Test
    public void shouldImportCatalogVersion()
    {
        AttributeDescriptorModel attributeDescriptor = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        BDDMockito.given(attributeDescriptor.getQualifier()).willReturn("catalogVersion");
        List<Map<String, String>> parameters = new ArrayList<>();
        Map<String, String> catalogVersionParams = new HashMap<>();
        catalogVersionParams.put("catalog", "Clothing");
        catalogVersionParams.put("version", "Online");
        parameters.add(catalogVersionParams);
        ImportParameters importParameters = new ImportParameters("Product", null, null, UUID.randomUUID().toString(), parameters);
        ImpexValue impexValue = this.translator.importValue(attributeDescriptor, importParameters);
        Assertions.assertThat(impexValue.getValue()).isEqualTo("Online:Clothing");
        Assertions.assertThat(impexValue.getHeaderValue().getName()).isEqualTo("catalogVersion(version,catalog(id))");
    }
}
