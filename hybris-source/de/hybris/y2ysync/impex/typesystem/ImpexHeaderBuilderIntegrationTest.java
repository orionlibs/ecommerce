package de.hybris.y2ysync.impex.typesystem;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import javax.annotation.Resource;
import org.assertj.core.api.Assertions;
import org.junit.Test;

@IntegrationTest
public class ImpexHeaderBuilderIntegrationTest extends ServicelayerBaseTest
{
    @Resource(name = "impexHeaderBuilder")
    private ImpexHeaderBuilder builder;


    @Test
    public void shouldReturnHeaderForSimpleAtomicAttribute() throws Exception
    {
        String typeCode = "ProductFeature";
        String qualifier = "valuePosition";
        String header = this.builder.getHeaderFor("ProductFeature", "valuePosition");
        Assertions.assertThat(header).isEqualTo("valuePosition[unique=true]");
    }


    @Test
    public void shouldReturnHeaderForReferenceAttributeMappingItsUniqueAttributes() throws Exception
    {
        String typeCode = "ProductFeature";
        String qualifier = "product";
        String header = this.builder.getHeaderFor("ProductFeature", "product");
        Assertions.assertThat(header).isEqualTo("product(catalogVersion(catalog(id),version),code)[unique=true]");
    }


    @Test
    public void shouldReturnHeaderForLocalizedRelation() throws Exception
    {
        String typeCode = "Keyword";
        String qualifier = "products";
        String header = this.builder.getHeaderFor("Keyword", "products");
        Assertions.assertThat(header).isEqualTo("products(key(isocode),value(catalogVersion(catalog(id),version),code))");
    }


    @Test
    public void shouldReturnHeaderForNonLocalizedRelation() throws Exception
    {
        String typeCode = "MediaContainer";
        String qualifier = "medias";
        String header = this.builder.getHeaderFor("MediaContainer", "medias");
        Assertions.assertThat(header).isEqualTo("medias(catalogVersion(catalog(id),version),code)");
    }


    @Test
    public void shouldReturnHeaderForDateAttributeWithProperFormat() throws Exception
    {
        String typeCode = "Item";
        String qualifier = "modifiedtime";
        String header = this.builder.getHeaderFor("Item", "modifiedtime");
        Assertions.assertThat(header).isEqualTo("modifiedtime[dateformat=dd.MM.yyyy hh:mm:ss]");
    }
}
