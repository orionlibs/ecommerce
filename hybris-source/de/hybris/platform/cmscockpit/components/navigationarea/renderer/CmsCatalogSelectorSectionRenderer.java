package de.hybris.platform.cmscockpit.components.navigationarea.renderer;

import de.hybris.platform.cockpit.components.navigationarea.renderer.DefaultSelectorSectionRenderer;
import de.hybris.platform.servicelayer.model.ModelService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zul.ListitemRenderer;

public class CmsCatalogSelectorSectionRenderer extends DefaultSelectorSectionRenderer
{
    private static final Logger LOG = Logger.getLogger(CmsCatalogSelectorSectionRenderer.class);
    private ListitemRenderer listRenderer = null;
    private ModelService modelService = null;


    public ListitemRenderer getListRenderer()
    {
        if(this.listRenderer == null)
        {
            this.listRenderer = (ListitemRenderer)new MyCmsCatalogSelectorListRenderer(this);
        }
        return this.listRenderer;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }
}
