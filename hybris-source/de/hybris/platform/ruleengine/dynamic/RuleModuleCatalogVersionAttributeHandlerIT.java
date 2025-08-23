package de.hybris.platform.ruleengine.dynamic;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.ruleengine.dao.RulesModuleDao;
import de.hybris.platform.ruleengine.model.AbstractRulesModuleModel;
import de.hybris.platform.servicelayer.ServicelayerTest;
import java.util.Collection;
import javax.annotation.Resource;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

@IntegrationTest
public class RuleModuleCatalogVersionAttributeHandlerIT extends ServicelayerTest
{
    @Resource
    private RulesModuleDao rulesModuleDao;
    @Resource
    private RuleModuleCatalogVersionAttributeHandler AbstractRulesModule_catalogVersionsAttributeHandler;


    @Before
    public void setUp() throws Exception
    {
        createCoreData();
        importCsv("/ruleengine/test/mappings/mappings-test-data.impex", "UTF-8");
    }


    @Test
    public void testGet()
    {
        AbstractRulesModuleModel previewModule = this.rulesModuleDao.findByName("catmap01-preview-module");
        Collection<CatalogVersionModel> catalogVersionModels = this.AbstractRulesModule_catalogVersionsAttributeHandler.get(previewModule);
        Assertions.assertThat(catalogVersionModels).hasSize(1);
        CatalogVersionModel catalogVersion = catalogVersionModels.iterator().next();
        Assertions.assertThat(catalogVersion.getCatalog().getId()).isEqualTo("catmap01");
        Assertions.assertThat(catalogVersion.getVersion()).isEqualToIgnoringCase("staged");
    }


    @Test
    public void testGetNoMapping()
    {
        AbstractRulesModuleModel previewModule = this.rulesModuleDao.findByName("catmap03-live-module");
        Collection<CatalogVersionModel> catalogVersionModels = this.AbstractRulesModule_catalogVersionsAttributeHandler.get(previewModule);
        Assertions.assertThat(catalogVersionModels).isEmpty();
    }
}
