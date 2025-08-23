package com.hybris.backoffice.excel.translators;

import com.google.common.collect.Lists;
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
public class ExcelMediaCollectionUrlTranslatorTest
{
    private static final String MEDIA_TYPE_CODE = "MediaChild";
    @InjectMocks
    @Spy
    private ExcelMediaCollectionUrlTranslator translator;
    @Mock
    private TypeService typeService;


    @Before
    public void setUp()
    {
        Mockito.when(Boolean.valueOf(this.typeService.isAssignableFrom("MediaChild", "Media"))).thenReturn(Boolean.valueOf(true));
    }


    @Test
    public void shouldExportDataBeNullSafe()
    {
        Assertions.assertThat(this.translator.exportData(null).isPresent()).isFalse();
    }


    @Test
    public void shouldExportedDataBeInProperFormat()
    {
        MediaModel media1 = creteMediaModelMock("theCode1", "folder1", "https://a.host/catalog/media1.ext");
        MediaModel media2 = creteMediaModelMock("theCode2", "folder2", "a.host/catalog/media1.ext");
        MediaModel media3 = creteMediaModelMock("theCode3", "folder3", "https://a.host/catalog/media3.ext");
        Assertions.assertThat(this.translator.exportData(Lists.newArrayList((Object[])new MediaModel[] {media1, media2})).isPresent()).isTrue();
        String media1Res = ":theCode1:default:staged:folder1:\"https://a.host/catalog/media1.ext\"";
        String media2Res = ":theCode2:default:staged:folder2:";
        String media3Res = ":theCode3:default:staged:folder3:\"https://a.host/catalog/media3.ext\"";
        Assertions.assertThat(this.translator.exportData(Lists.newArrayList((Object[])new MediaModel[] {media1, media2, media3})).get())
                        .isEqualTo(":theCode1:default:staged:folder1:\"https://a.host/catalog/media1.ext\",:theCode2:default:staged:folder2:,:theCode3:default:staged:folder3:\"https://a.host/catalog/media3.ext\"");
    }


    private MediaModel creteMediaModelMock(String code, String folder, String URL)
    {
        MediaModel media = (MediaModel)Mockito.mock(MediaModel.class);
        ((MediaModel)Mockito.doReturn(code).when(media)).getCode();
        ((MediaModel)Mockito.doReturn(URL).when(media)).getDownloadURL();
        CatalogVersionModel cv = (CatalogVersionModel)Mockito.mock(CatalogVersionModel.class);
        CatalogModel catalog = (CatalogModel)Mockito.mock(CatalogModel.class);
        Mockito.when(catalog.getId()).thenReturn("default");
        Mockito.when(cv.getVersion()).thenReturn("staged");
        Mockito.when(cv.getCatalog()).thenReturn(catalog);
        ((MediaModel)Mockito.doReturn(cv).when(media)).getCatalogVersion();
        MediaFolderModel mediaFolder = (MediaFolderModel)Mockito.mock(MediaFolderModel.class);
        ((MediaModel)Mockito.doReturn(mediaFolder).when(media)).getFolder();
        ((MediaFolderModel)Mockito.doReturn(folder).when(mediaFolder)).getQualifier();
        return media;
    }


    @Test
    public void shouldReturnReferenceFormat()
    {
        Assertions.assertThat(this.translator.referenceFormat(null)).isEqualTo("filePath:code:catalog:version:folder:url");
    }


    @Test
    public void shouldGivenTypeBeHandled()
    {
        AttributeDescriptorModel attributeDescriptor = BackofficeTestUtil.mockCollectionTypeAttributeDescriptor("MediaChild");
        Assertions.assertThat(this.translator.canHandle(attributeDescriptor)).isTrue();
    }
}
