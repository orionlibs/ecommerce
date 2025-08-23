package com.hybris.backoffice.excel.translators;

import com.hybris.backoffice.BackofficeTestUtil;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.media.MediaFolderModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.servicelayer.type.TypeService;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ExcelMediaUrlTranslatorTest
{
    private static final String MEDIA_TYPE_CODE = "MediaChild";
    @InjectMocks
    @Spy
    private ExcelMediaUrlTranslator translator;
    @Mock
    private TypeService typeService;


    @Before
    public void setUp()
    {
        Mockito.when(Boolean.valueOf(this.typeService.isAssignableFrom("Media", "MediaChild"))).thenReturn(Boolean.valueOf(true));
    }


    @Test
    public void shouldExportDataBeNullSafe()
    {
        Assertions.assertThat(this.translator.exportData(null).isPresent()).isFalse();
    }


    @Test
    public void shouldExportedDataBeInProperFormat()
    {
        CatalogVersionModel cv = (CatalogVersionModel)Mockito.mock(CatalogVersionModel.class);
        CatalogModel catalog = (CatalogModel)Mockito.mock(CatalogModel.class);
        Mockito.when(catalog.getId()).thenReturn("default");
        Mockito.when(cv.getVersion()).thenReturn("staged");
        Mockito.when(cv.getCatalog()).thenReturn(catalog);
        MediaModel media = (MediaModel)Mockito.mock(MediaModel.class);
        Mockito.when(media.getCode()).thenReturn("theCode");
        Mockito.when(media.getDownloadURL()).thenReturn("https://a.host/catalog/media.ext");
        Mockito.when(media.getCatalogVersion()).thenReturn(cv);
        MediaFolderModel mediaFolder = (MediaFolderModel)Mockito.mock(MediaFolderModel.class);
        ((MediaModel)Mockito.doReturn(mediaFolder).when(media)).getFolder();
        ((MediaFolderModel)Mockito.doReturn("folder").when(mediaFolder)).getQualifier();
        Assertions.assertThat(this.translator.exportData(media).isPresent()).isTrue();
        Assertions.assertThat(this.translator.exportData(media).get())
                        .isEqualTo(":theCode:default:staged:folder:\"https://a.host/catalog/media.ext\"");
    }


    @Test
    public void shouldReturnReferenceFormat()
    {
        Assertions.assertThat(this.translator.referenceFormat(null)).isEqualTo("filePath:code:catalog:version:folder:url");
    }


    @Test
    public void shouldGivenTypeBeHandled()
    {
        AttributeDescriptorModel attributeDescriptor = BackofficeTestUtil.mockAttributeDescriptor("MediaChild");
        Assertions.assertThat(this.translator.canHandle(attributeDescriptor)).isTrue();
    }
}
