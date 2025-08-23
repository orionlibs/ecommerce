package com.hybris.backoffice.solrsearch.setup.impl;

import com.hybris.backoffice.search.setup.BackofficeSearchSystemSetupConfig;
import com.hybris.backoffice.search.setup.impl.FileBasedImpExResourceFactory;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.cronjob.CronJobService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.impex.ImportConfig;
import de.hybris.platform.servicelayer.impex.ImportResult;
import de.hybris.platform.servicelayer.impex.ImportService;
import de.hybris.platform.servicelayer.impex.impl.FileBasedImpExResource;
import de.hybris.platform.servicelayer.model.ModelService;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.configuration.Configuration;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DefaultBackofficeSolrSearchImpexImportSystemSetupTest
{
    private static final String DE_CODE = "de";
    private static final String EN_CODE = "en";
    private static final String COMMA = ",";
    private static final String UNDERSCORE = "_";
    private static final String UTF_8 = "UTF-8";
    private static final String LOCALIZED_ROOT = "/test/test";
    private static final String NON_LOCALIZED_ROOT = "test.impex";
    private static final String TEST_TEST_EN_IMPEX = "test_en.impex";
    private static final String TEST_TEST_DE_IMPEX = "test_de.impex";
    @Mock
    private ImportService importService;
    @Mock
    private CommonI18NService commonI18NService;
    @Mock
    private ModelService modelService;
    @Mock
    private CronJobService cronJobService;
    @Mock
    private Configuration configuration;
    @Mock
    private ConfigurationService configurationService;
    @Mock
    private FileBasedImpExResourceFactory fileBasedImpExResourceFactory;
    @Mock
    private BackofficeSearchSystemSetupConfig backofficeSearchSystemSetupConfig;
    @InjectMocks
    @Spy
    private DefaultBackofficeSolrSearchImpexImportSystemSetup systemSetup;


    @Before
    public void setUp()
    {
        mockDefaultConfig();
        mockDefaultBehaviour();
    }


    @Test
    public void shouldImportConfiguredImpexFiles()
    {
        mockNonLocalizedFilesConfig();
        ArgumentCaptor<File> filePathsCaptor = ArgumentCaptor.forClass(File.class);
        ArgumentCaptor<String> encodingCaptor = ArgumentCaptor.forClass(String.class);
        int twice = 2;
        Mockito.when(this.configurationService.getConfiguration()).thenReturn(this.configuration);
        Mockito.when(this.configurationService.getConfiguration().getString(Matchers.anyString(), Matchers.anyString())).thenReturn("");
        this.systemSetup.importImpex();
        ((FileBasedImpExResourceFactory)Mockito.verify(this.fileBasedImpExResourceFactory, Mockito.times(2))).createFileBasedImpExResource((File)filePathsCaptor.capture(), (String)encodingCaptor
                        .capture());
        Assertions.assertThat((List)filePathsCaptor.getAllValues().stream().map(file -> file.getName()).collect(Collectors.toList()))
                        .containsExactly((Object[])new String[] {"test_en.impex", "test_de.impex"});
    }


    @Test
    public void shouldAdjustCronjob()
    {
        String nodeGroup = "TEST";
        CronJobModel cronJob = (CronJobModel)Mockito.spy(new CronJobModel());
        Mockito.when(this.cronJobService.getCronJob("update-backofficeIndex-CronJob")).thenReturn(cronJob);
        Mockito.when(this.configurationService.getConfiguration()).thenReturn(this.configuration);
        Mockito.when(this.configuration.getString("backofficesearch.cronjob.nodegroup", "")).thenReturn("TEST");
        this.systemSetup.adjustIndexUpdatingCronjob();
        ((CronJobModel)Mockito.verify(cronJob)).setNodeGroup("TEST");
        ((ModelService)Mockito.verify(this.modelService)).save(cronJob);
        Assertions.assertThat(cronJob.getNodeGroup()).isEqualTo("TEST");
    }


    @Test
    public void shouldNotSetNodeGroupInCronJoBWhenNodeGroupIsEmpty()
    {
        String nodeGroup = "";
        CronJobModel cronJob = (CronJobModel)Mockito.spy(new CronJobModel());
        Mockito.when(this.cronJobService.getCronJob("update-backofficeIndex-CronJob")).thenReturn(cronJob);
        Mockito.when(this.configurationService.getConfiguration()).thenReturn(this.configuration);
        this.systemSetup.adjustIndexUpdatingCronjob();
        ((CronJobModel)Mockito.verify(cronJob, Mockito.times(0))).setNodeGroup(Matchers.anyString());
        ((ModelService)Mockito.verify(this.modelService, Mockito.times(0))).save(cronJob);
        Assertions.assertThat(cronJob.getNodeGroup()).isNull();
    }


    private void mockDefaultConfig()
    {
        Mockito.when(this.backofficeSearchSystemSetupConfig.getFileEncoding()).thenReturn("UTF-8");
        Mockito.when(this.backofficeSearchSystemSetupConfig.getRootNameLanguageSeparator()).thenReturn("_");
        Mockito.when(this.backofficeSearchSystemSetupConfig.getLocalizedRootNames()).thenReturn(Arrays.asList(new String[] {"/test/test"}));
    }


    private void mockNonLocalizedFilesConfig()
    {
        Mockito.when(this.backofficeSearchSystemSetupConfig.getNonLocalizedRootNames()).thenReturn(Arrays.asList(new String[] {"test.impex"}));
    }


    private void mockDefaultBehaviour()
    {
        ImportConfig importConfig = (ImportConfig)Mockito.mock(ImportConfig.class);
        FileBasedImpExResource fileBasedImpExResource = (FileBasedImpExResource)Mockito.mock(FileBasedImpExResource.class);
        Mockito.when(this.fileBasedImpExResourceFactory.createFileBasedImpExResource((File)Matchers.any(), (String)Matchers.any())).thenReturn(fileBasedImpExResource);
        Mockito.when(this.importService.importData((ImportConfig)Matchers.any(ImportConfig.class))).thenReturn(Mockito.mock(ImportResult.class));
        LanguageModel languageModelEn = (LanguageModel)Mockito.mock(LanguageModel.class);
        Mockito.when(languageModelEn.getIsocode()).thenReturn("en");
        LanguageModel languageModelDe = (LanguageModel)Mockito.mock(LanguageModel.class);
        Mockito.when(languageModelDe.getIsocode()).thenReturn("de");
        Mockito.when(this.commonI18NService.getAllLanguages()).thenReturn(Arrays.asList(new LanguageModel[] {languageModelEn, languageModelDe}));
    }
}
