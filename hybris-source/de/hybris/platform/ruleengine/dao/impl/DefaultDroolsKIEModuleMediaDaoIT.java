package de.hybris.platform.ruleengine.dao.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.ruleengine.model.DroolsKIEModuleMediaModel;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.Optional;
import javax.annotation.Resource;
import org.fest.assertions.Assertions;
import org.junit.Before;
import org.junit.Test;

@IntegrationTest
public class DefaultDroolsKIEModuleMediaDaoIT extends ServicelayerTest
{
    private static final String RELEASE_ID = "groupId:artifactId:version";
    private static final String KIE_MODULE_NAME = "kieModuleName";
    @Resource(name = "defaultDroolsKIEModuleMediaDao")
    private DefaultDroolsKIEModuleMediaDao defaultDroolsKIEModuleMediaDao;
    @Resource(name = "modelService")
    private ModelService modelService;


    @Before
    public void setUp() throws Exception
    {
        DroolsKIEModuleMediaModel droolsKIEModuleMedia = (DroolsKIEModuleMediaModel)this.modelService.create(DroolsKIEModuleMediaModel.class);
        droolsKIEModuleMedia.setCode("code");
        droolsKIEModuleMedia.setKieModuleName("kieModuleName");
        droolsKIEModuleMedia.setReleaseId("groupId:artifactId:version");
        this.modelService.saveAll(new Object[] {droolsKIEModuleMedia});
    }


    @Test
    public void testFindKIEModuleMedia()
    {
        Optional<DroolsKIEModuleMediaModel> droolsKIEModuleMediaOptional = this.defaultDroolsKIEModuleMediaDao.findKIEModuleMedia("kieModuleName", "groupId:artifactId:version");
        Assertions.assertThat(droolsKIEModuleMediaOptional.isPresent()).isTrue();
        DroolsKIEModuleMediaModel droolsKIEModuleMedia = droolsKIEModuleMediaOptional.get();
        Assertions.assertThat(droolsKIEModuleMedia.getKieModuleName()).isEqualTo("kieModuleName");
        Assertions.assertThat(droolsKIEModuleMedia.getReleaseId()).isEqualTo("groupId:artifactId:version");
    }


    @Test
    public void testFindKIEMMediaNotFound()
    {
        Assertions.assertThat(this.defaultDroolsKIEModuleMediaDao.findKIEModuleMedia("invalid", "groupId:artifactId:version").isPresent()).isFalse();
        Assertions.assertThat(this.defaultDroolsKIEModuleMediaDao.findKIEModuleMedia("kieModuleName", "invalid").isPresent()).isFalse();
    }
}
