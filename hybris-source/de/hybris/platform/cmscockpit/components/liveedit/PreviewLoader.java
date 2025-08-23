package de.hybris.platform.cmscockpit.components.liveedit;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.preview.PreviewDataModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import java.util.Collection;

public interface PreviewLoader
{
    void loadValues(PreviewDataModel paramPreviewDataModel, AbstractPageModel paramAbstractPageModel, Collection<CatalogVersionModel> paramCollection, boolean paramBoolean, LanguageModel paramLanguageModel, String paramString);
}
