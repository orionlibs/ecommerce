package de.hybris.y2ysync.deltadetection;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.type.TypeService;
import javax.annotation.Resource;
import org.assertj.core.api.AbstractCharSequenceAssert;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

@IntegrationTest
public class InfoExpressionGeneratorTest extends ServicelayerBaseTest
{
    @Resource
    private TypeService typeService;
    private InfoExpressionGenerator generator;


    @Before
    public void setUp()
    {
        this.generator = new InfoExpressionGenerator(this.typeService);
    }


    @Test
    public void shouldGenerateProperInfoExpressionForCatalog()
    {
        String expected = "#{getId()}";
        String actual = this.generator.getInfoExpressionForType("Catalog");
        ((AbstractCharSequenceAssert)Assertions.assertThat(actual).isNotNull()).isNotEmpty().isEqualTo("#{getId()}");
    }


    @Test
    public void shouldGenerateProperInfoExpressionForCatalogVersion()
    {
        String expected = "#{getCatalog().getId()}|#{getVersion()}";
        String actual = this.generator.getInfoExpressionForType("CatalogVersion");
        ((AbstractCharSequenceAssert)Assertions.assertThat(actual).isNotNull()).isNotEmpty().isEqualTo("#{getCatalog().getId()}|#{getVersion()}");
    }


    @Test
    public void shouldGenerateProperInfoExpressionForCategory()
    {
        String expected = "#{getCatalogVersion().getCatalog().getId()}:#{getCatalogVersion().getVersion()}|#{getCode()}";
        String actual = this.generator.getInfoExpressionForType("Category");
        ((AbstractCharSequenceAssert)Assertions.assertThat(actual).isNotNull()).isNotEmpty().isEqualTo("#{getCatalogVersion().getCatalog().getId()}:#{getCatalogVersion().getVersion()}|#{getCode()}");
    }


    @Test
    public void shouldGenerateProperInfoExpressionForProduct()
    {
        String expected = "#{getCatalogVersion().getCatalog().getId()}:#{getCatalogVersion().getVersion()}|#{getCode()}";
        String actual = this.generator.getInfoExpressionForType("Product");
        ((AbstractCharSequenceAssert)Assertions.assertThat(actual).isNotNull()).isNotEmpty().isEqualTo("#{getCatalogVersion().getCatalog().getId()}:#{getCatalogVersion().getVersion()}|#{getCode()}");
    }
}
