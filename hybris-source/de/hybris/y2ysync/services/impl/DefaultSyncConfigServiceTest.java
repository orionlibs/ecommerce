package de.hybris.y2ysync.services.impl;

import com.google.common.collect.Sets;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalBaseTest;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.y2ysync.ColumnDefinitionsAssert;
import de.hybris.y2ysync.model.Y2YColumnDefinitionModel;
import de.hybris.y2ysync.model.Y2YStreamConfigurationContainerModel;
import de.hybris.y2ysync.model.Y2YStreamConfigurationModel;
import de.hybris.y2ysync.services.SyncConfigService;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import javax.annotation.Resource;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

public class DefaultSyncConfigServiceTest extends ServicelayerTransactionalBaseTest
{
    @Resource
    private TypeService typeService;
    @Resource(name = "syncConfigService")
    private SyncConfigService service;
    @Resource
    private ModelService modelService;
    @Resource
    private CommonI18NService commonI18NService;
    private LanguageModel currentLang;


    @Before
    public void setUp() throws Exception
    {
        this.currentLang = this.commonI18NService.getCurrentLanguage();
    }


    @Test
    public void shouldCreateColumnDefintionsBasedOnUnlocalizedAttributeDescriptor() throws Exception
    {
        AttributeDescriptorModel attributeDescriptor = this.typeService.getAttributeDescriptor("Title", "code");
        HashSet<AttributeDescriptorModel> attrDescriptors = Sets.newHashSet((Object[])new AttributeDescriptorModel[] {attributeDescriptor});
        Y2YStreamConfigurationContainerModel container = this.service.createStreamConfigurationContainer("testId");
        Y2YStreamConfigurationModel configuration = this.service.createStreamConfiguration(container, "Title", attrDescriptors,
                        Collections.emptySet());
        this.modelService.save(configuration);
        Assertions.assertThat(configuration).isNotNull();
        ColumnDefinitionsAssert.assertThat(configuration.getColumnDefinitions()).hasSize(1)
                        .containsDefintionFor(attributeDescriptor)
                        .withoutLanguage()
                        .withColumnName("code")
                        .withImpexHeader("code[unique=true]");
    }


    @Test
    public void shouldCreateColumnDefintionsBasedOnLocalizedAttributeDescriptor() throws Exception
    {
        AttributeDescriptorModel attributeDescriptor = this.typeService.getAttributeDescriptor("Title", "name");
        HashSet<AttributeDescriptorModel> attrDescriptors = Sets.newHashSet((Object[])new AttributeDescriptorModel[] {attributeDescriptor});
        Y2YStreamConfigurationContainerModel container = this.service.createStreamConfigurationContainer("testId");
        Y2YStreamConfigurationModel configuration = this.service.createStreamConfiguration(container, "Title", attrDescriptors,
                        Collections.emptySet());
        this.modelService.save(configuration);
        Assertions.assertThat(configuration).isNotNull();
        ColumnDefinitionsAssert.assertThat(configuration.getColumnDefinitions()).hasSize(1)
                        .containsDefintionFor(attributeDescriptor)
                        .withLanguage(this.currentLang)
                        .withColumnName("name_en")
                        .withImpexHeader("name[lang=en]");
    }


    @Test
    public void shouldCreateDefaultColumnDefintionsForComposedType() throws Exception
    {
        ComposedTypeModel titleComposedType = this.typeService.getComposedTypeForClass(TitleModel.class);
        List<Y2YColumnDefinitionModel> defaultColumnDefinitions = this.service.createDefaultColumnDefinitions(titleComposedType);
        ColumnDefinitionsAssert.assertThat(defaultColumnDefinitions).hasSize(5);
        ColumnDefinitionsAssert.assertThat(defaultColumnDefinitions).containsDefintionFor(titleDescriptor("owner"))
                        .withoutLanguage()
                        .withColumnName("owner")
                        .withImpexHeader("owner()");
        ColumnDefinitionsAssert.assertThat(defaultColumnDefinitions).containsDefintionFor(titleDescriptor("creationtime"))
                        .withoutLanguage()
                        .withColumnName("creationtime")
                        .withImpexHeader("creationtime[dateformat=dd.MM.yyyy hh:mm:ss]");
        ColumnDefinitionsAssert.assertThat(defaultColumnDefinitions).containsDefintionFor(titleDescriptor("name"))
                        .withLanguage(this.currentLang)
                        .withColumnName("name_en")
                        .withImpexHeader("name[lang=en]");
        ColumnDefinitionsAssert.assertThat(defaultColumnDefinitions).containsDefintionFor(titleDescriptor("modifiedtime"))
                        .withoutLanguage()
                        .withColumnName("modifiedtime")
                        .withImpexHeader("modifiedtime[dateformat=dd.MM.yyyy hh:mm:ss]");
        ColumnDefinitionsAssert.assertThat(defaultColumnDefinitions).containsDefintionFor(titleDescriptor("code"))
                        .withoutLanguage()
                        .withColumnName("code")
                        .withImpexHeader("code[unique=true]");
    }


    private AttributeDescriptorModel titleDescriptor(String qualifier)
    {
        return this.typeService.getAttributeDescriptor("Title", qualifier);
    }
}
