package de.hybris.platform.personalizationcms.model;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.enums.ActionType;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.Date;
import javax.annotation.Resource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DynamicAttributeTests extends ServicelayerTest
{
    private static final String CONTAINER_ID = "containerId";
    private static final String COMPONENT_ID = "componentId";
    private static final String AFFECTED_OBJECT_ID = "containerId_componentId";
    @Resource
    private ModelService modelService;
    @Resource
    private CatalogVersionService catalogVersionService;


    @Before
    public void setUp() throws Exception
    {
        createCoreData();
        createDefaultCatalog();
    }


    @Test
    public void shouldCalculateAffectedObjectKey()
    {
        CxCmsActionModel action = new CxCmsActionModel();
        action.setComponentId("componentId");
        action.setContainerId("containerId");
        action.setCode("code" + (new Date()).getTime());
        action.setTarget("target");
        action.setType(ActionType.PLAIN);
        action.setCatalogVersion(this.catalogVersionService.getCatalogVersion("testCatalog", "Online"));
        this.modelService.save(action);
        Assert.assertEquals("containerId_componentId", action.getAffectedObjectKey());
    }
}
