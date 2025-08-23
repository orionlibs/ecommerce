package de.hybris.platform.previewpersonalizationweb.populator;

import de.hybris.platform.cms2.model.preview.CMSPreviewTicketModel;
import de.hybris.platform.cms2.model.preview.PreviewDataModel;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.personalizationservices.data.CxVariationKey;
import de.hybris.platform.personalizationservices.model.CxVariationModel;
import de.hybris.platform.previewwebservices.dto.PreviewTicketWsDTO;
import de.hybris.platform.webservicescommons.conversion.ConversionUtils;
import de.hybris.platform.webservicescommons.util.LocalViewExecutor;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

public class PersonalizationPreviewTicketPopulator implements Populator<CMSPreviewTicketModel, PreviewTicketWsDTO>
{
    private LocalViewExecutor localViewExecutor;


    public void populate(CMSPreviewTicketModel source, PreviewTicketWsDTO target)
    {
        Assert.notNull(source, "Parameter source cannot be null.");
        Assert.notNull(target, "Parameter target cannot be null.");
        this.localViewExecutor.executeWithAllCatalogs(() -> {
            PreviewDataModel previewData = source.getPreviewData();
            if(previewData != null)
            {
                target.setVariations(ConversionUtils.convert(source.getPreviewData().getVariations(), ()));
                target.setSegments(ConversionUtils.convert(source.getPreviewData().getSegments(), ()));
            }
            return null;
        });
    }


    protected CxVariationKey createKey(CxVariationModel variation)
    {
        CxVariationKey key = new CxVariationKey();
        key.setVariationCode(variation.getCode());
        key.setCustomizationCode(variation.getCustomization().getCode());
        if(variation.getCatalogVersion() != null)
        {
            key.setCatalog(variation.getCatalogVersion().getCatalog().getId());
            key.setCatalogVersion(variation.getCatalogVersion().getVersion());
        }
        return key;
    }


    @Required
    public void setLocalViewExecutor(LocalViewExecutor localViewExecutor)
    {
        this.localViewExecutor = localViewExecutor;
    }


    public LocalViewExecutor getLocalViewExecutor()
    {
        return this.localViewExecutor;
    }
}
