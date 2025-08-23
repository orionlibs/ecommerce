package de.hybris.platform.cms2.cloning.service.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.cloning.service.CMSModelCloningContextFactory;
import de.hybris.platform.cms2.cloning.service.preset.AttributePresetHandler;
import de.hybris.platform.cms2.cloning.service.preset.impl.ComponentForCatalogVersionAttributePresetHandler;
import de.hybris.platform.cms2.cloning.service.preset.impl.MediaContainerForCatalogVersionAttributePresetHandler;
import de.hybris.platform.cms2.cloning.service.preset.impl.MediaForCatalogVersionAttributePresetHandler;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultCMSModelCloningContextFactory implements CMSModelCloningContextFactory
{
    private ObjectFactory<CMSModelCloningContext> cmsModelCloningContextObjectFactory;


    public CMSModelCloningContext createCloningContextWithCatalogVersionPredicates(CatalogVersionModel targetCatalogVersion)
    {
        CMSModelCloningContext cloningContext = (CMSModelCloningContext)getCmsModelCloningContextObjectFactory().getObject();
        cloningContext.addPresetValuePredicate((AttributePresetHandler)new ComponentForCatalogVersionAttributePresetHandler(targetCatalogVersion));
        cloningContext.addPresetValuePredicate((AttributePresetHandler)new MediaForCatalogVersionAttributePresetHandler(targetCatalogVersion));
        cloningContext.addPresetValuePredicate((AttributePresetHandler)new MediaContainerForCatalogVersionAttributePresetHandler(targetCatalogVersion));
        return cloningContext;
    }


    protected ObjectFactory<CMSModelCloningContext> getCmsModelCloningContextObjectFactory()
    {
        return this.cmsModelCloningContextObjectFactory;
    }


    @Required
    public void setCmsModelCloningContextObjectFactory(ObjectFactory<CMSModelCloningContext> cmsModelCloningContextObjectFactory)
    {
        this.cmsModelCloningContextObjectFactory = cmsModelCloningContextObjectFactory;
    }
}
