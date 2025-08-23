package de.hybris.platform.previewwebservices.populators;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.preview.CMSPreviewTicketModel;
import de.hybris.platform.cms2.model.preview.PreviewDataModel;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.previewwebservices.dto.CatalogVersionWsDTO;
import de.hybris.platform.previewwebservices.dto.PreviewTicketWsDTO;
import de.hybris.platform.webservicescommons.util.LocalViewExecutor;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

public class PreviewTicketPopulator implements Populator<CMSPreviewTicketModel, PreviewTicketWsDTO>
{
    private LocalViewExecutor localViewExecutor;


    public void populate(CMSPreviewTicketModel source, PreviewTicketWsDTO target)
    {
        Assert.notNull(source, "Parameter source cannot be null.");
        Assert.notNull(target, "Parameter target cannot be null.");
        this.localViewExecutor.executeWithAllCatalogs(() -> {
            target.setTicketId(source.getId());
            PreviewDataModel previewData = source.getPreviewData();
            if(previewData != null)
            {
                if(previewData.getActiveSite() != null)
                {
                    target.setSiteId(previewData.getActiveSite().getUid());
                }
                if(previewData.getLanguage() != null)
                {
                    target.setLanguage(previewData.getLanguage().getIsocode());
                }
                target.setResourcePath(previewData.getResourcePath());
                target.setTime(previewData.getTime());
                if(previewData.getUser() != null)
                {
                    target.setUser(previewData.getUser().getUid());
                }
                if(previewData.getUserGroup() != null)
                {
                    target.setUserGroup(previewData.getUserGroup().getUid());
                }
                if(previewData.getPage() != null)
                {
                    target.setPageId(previewData.getPage().getUid());
                }
                if(CollectionUtils.isNotEmpty(previewData.getCatalogVersions()))
                {
                    List<CatalogVersionWsDTO> catalogVersions = (List<CatalogVersionWsDTO>)previewData.getCatalogVersions().stream().map(this::buildCatalogVersionWsDTO).collect(Collectors.toList());
                    target.setCatalogVersions(catalogVersions);
                }
                if(previewData.getVersion() != null)
                {
                    target.setVersionId(previewData.getVersion().getUid());
                }
            }
            return null;
        });
    }


    protected CatalogVersionWsDTO buildCatalogVersionWsDTO(CatalogVersionModel catalogVersion)
    {
        CatalogVersionWsDTO result = new CatalogVersionWsDTO();
        result.setCatalog(catalogVersion.getCatalog().getId());
        result.setCatalogVersion(catalogVersion.getVersion());
        return result;
    }


    protected LocalViewExecutor getLocalViewExecutor()
    {
        return this.localViewExecutor;
    }


    @Required
    public void setLocalViewExecutor(LocalViewExecutor localViewExecutor)
    {
        this.localViewExecutor = localViewExecutor;
    }
}
