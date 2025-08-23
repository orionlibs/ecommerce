package de.hybris.platform.warehousing.labels.context;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.cms2.model.contents.ContentCatalogModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.commerceservices.impersonation.ImpersonationContext;
import de.hybris.platform.commerceservices.impersonation.ImpersonationService;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Required;

public class ReturnShippingLabelContext extends CommonReturnDocumentContext
{
    private MediaService mediaService;
    private ImpersonationService impersonationService;
    private String barcodeMediaImageName;


    public String getBarcodeMediaImageURL(AbstractOrderModel order)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("order", order);
        CMSSiteModel cmsSiteModel = (CMSSiteModel)getBaseSite();
        ImpersonationContext context = new ImpersonationContext();
        context.setSite((BaseSiteModel)cmsSiteModel);
        context.setUser(order.getUser());
        context.setCatalogVersions(Collections.emptyList());
        MediaModel barcodeImageMedia = (MediaModel)getImpersonationService().executeInContext(context, () -> {
            List<ContentCatalogModel> contentCatalogs = cmsSiteModel.getContentCatalogs();
            return !contentCatalogs.isEmpty() ? getMediaService().getMedia(((ContentCatalogModel)contentCatalogs.get(0)).getActiveCatalogVersion(), getBarcodeMediaImageName()) : null;
        });
        String path = null;
        if(barcodeImageMedia != null)
        {
            path = barcodeImageMedia.getDownloadURL();
        }
        return path;
    }


    protected String getBarcodeMediaImageName()
    {
        return this.barcodeMediaImageName;
    }


    @Required
    public void setBarcodeMediaImageName(String barcodeMediaImageName)
    {
        this.barcodeMediaImageName = barcodeMediaImageName;
    }


    protected MediaService getMediaService()
    {
        return this.mediaService;
    }


    @Required
    public void setMediaService(MediaService mediaService)
    {
        this.mediaService = mediaService;
    }


    protected ImpersonationService getImpersonationService()
    {
        return this.impersonationService;
    }


    @Required
    public void setImpersonationService(ImpersonationService impersonationService)
    {
        this.impersonationService = impersonationService;
    }
}
