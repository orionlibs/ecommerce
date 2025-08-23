package de.hybris.platform.cmscockpit.components.liveedit;

import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.preview.PreviewDataModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;

public interface LiveEditViewModel
{
    boolean isLiveEditModeEnabled();


    void setLiveEditModeEnabled(boolean paramBoolean);


    CMSSiteModel getSite();


    void setSite(CMSSiteModel paramCMSSiteModel);


    AbstractPageModel getPage();


    void setPage(AbstractPageModel paramAbstractPageModel);


    void setPagePreview(boolean paramBoolean);


    String getCurrentUrl();


    void setCurrentUrl(String paramString);


    PreviewDataModel getCurrentPreviewData();


    void setCurrentPreviewData(PreviewDataModel paramPreviewDataModel);


    String computeFinalUrl();


    boolean isPreviewDataValid();
}
