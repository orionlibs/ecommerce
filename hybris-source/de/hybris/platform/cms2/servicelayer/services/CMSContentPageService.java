package de.hybris.platform.cms2.servicelayer.services;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.data.PagePreviewCriteriaData;
import de.hybris.platform.cms2.enums.CmsPageStatus;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import java.util.Collection;
import java.util.List;

public interface CMSContentPageService
{
    ContentPageModel getDefaultPageForLabel(String paramString, CatalogVersionModel paramCatalogVersionModel) throws CMSItemNotFoundException;


    ContentPageModel getHomepage();


    ContentPageModel getHomepage(PagePreviewCriteriaData paramPagePreviewCriteriaData);


    ContentPageModel getPageForLabelAndPreview(String paramString, PagePreviewCriteriaData paramPagePreviewCriteriaData) throws CMSItemNotFoundException;


    ContentPageModel getPageForLabelAndStatuses(String paramString, List<CmsPageStatus> paramList) throws CMSItemNotFoundException;


    ContentPageModel getPageForLabelOrIdAndMatchType(String paramString, boolean paramBoolean) throws CMSItemNotFoundException;


    ContentPageModel getPageForLabelOrIdAndMatchType(String paramString, PagePreviewCriteriaData paramPagePreviewCriteriaData, boolean paramBoolean) throws CMSItemNotFoundException;


    List<String> findLabelVariations(String paramString, boolean paramBoolean);


    List<AbstractPageModel> findPagesForBestLabelMatch(Collection<AbstractPageModel> paramCollection, List<String> paramList);
}
