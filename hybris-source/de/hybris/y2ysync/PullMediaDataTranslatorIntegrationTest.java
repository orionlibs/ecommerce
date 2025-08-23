package de.hybris.y2ysync;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.impex.ImpExResource;
import de.hybris.platform.servicelayer.impex.ImportConfig;
import de.hybris.platform.servicelayer.impex.ImportResult;
import de.hybris.platform.servicelayer.impex.ImportService;
import de.hybris.platform.servicelayer.impex.impl.ClasspathImpExResource;
import de.hybris.platform.servicelayer.media.MediaService;
import javax.annotation.Resource;
import org.assertj.core.api.Assertions;
import org.junit.Test;

@IntegrationTest
public class PullMediaDataTranslatorIntegrationTest extends ServicelayerBaseTest
{
    @Resource
    private ImportService importService;
    @Resource
    private MediaService mediaService;


    @Test
    public void testSpecialColumnsWithStandardImpexViaImportService() throws Exception
    {
        ImportConfig config = new ImportConfig();
        ClasspathImpExResource impExResource = new ClasspathImpExResource("/test/testPullMediaDataTranslator.csv", "UTF-8");
        config.setScript((ImpExResource)impExResource);
        ImportResult result = this.importService.importData(config);
        Assertions.assertThat(result.isSuccessful()).isTrue();
        Assertions.assertThat(result.hasUnresolvedLines()).isFalse();
        MediaModel media = this.mediaService.getMedia("y2ysync-01");
        checkImportedMediaData(media);
        Assertions.assertThat(media.getRealFileName()).isEqualTo("img_01.jpg");
        Assertions.assertThat(media.getMime()).isEqualTo("image/jpeg");
    }


    private void checkImportedMediaData(MediaModel media)
    {
        Assertions.assertThat(media).isNotNull();
        Assertions.assertThat(this.mediaService.hasData(media)).isTrue();
    }
}
