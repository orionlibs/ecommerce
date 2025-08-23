package de.hybris.platform.cms2.servicelayer.services;

import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.data.PagePreviewCriteriaData;
import de.hybris.platform.cms2.model.preview.CMSPreviewTicketModel;
import de.hybris.platform.cms2.model.preview.PreviewDataModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import java.util.Collection;

public interface CMSPreviewService
{
    CMSPreviewTicketModel createPreviewTicket(PreviewDataModel paramPreviewDataModel);


    CMSPreviewTicketModel getPreviewTicket(String paramString);


    String storePreviewTicket(CMSPreviewTicketModel paramCMSPreviewTicketModel);


    PreviewDataModel clonePreviewData(PreviewDataModel paramPreviewDataModel);


    Collection<CatalogModel> getEditableCatalogs(CMSSiteModel paramCMSSiteModel, CatalogVersionModel paramCatalogVersionModel);


    PagePreviewCriteriaData getPagePreviewCriteria();


    boolean isVersioningPreview();


    CatalogVersionModel getPreviewContentCatalogVersion();
}
