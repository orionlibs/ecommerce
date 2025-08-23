package de.hybris.platform.cockpit.session;

import de.hybris.platform.cockpit.components.sectionpanel.Section;
import de.hybris.platform.cockpit.components.sectionpanel.SectionPanelModel;
import de.hybris.platform.cockpit.components.sectionpanel.SectionRenderer;
import de.hybris.platform.cockpit.model.query.impl.UICollectionQuery;
import de.hybris.platform.cockpit.model.query.impl.UIDynamicQuery;
import de.hybris.platform.cockpit.model.query.impl.UIQuery;
import de.hybris.platform.cockpit.model.query.impl.UISavedQuery;
import de.hybris.platform.cockpit.services.ObjectCollectionService;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.services.query.DynamicQueryService;
import de.hybris.platform.cockpit.services.query.SavedQueryService;
import java.util.Collection;
import java.util.List;

public interface UINavigationArea extends UICockpitArea, FocusablePerspectiveArea
{
    void addAreaListener(NavigationAreaListener paramNavigationAreaListener);


    void removeAreaListener(NavigationAreaListener paramNavigationAreaListener);


    void setSections(List<Section> paramList);


    void addSection(Section paramSection);


    void addSections(List<Section> paramList);


    void removeSection(Section paramSection);


    void removeSections(Collection<Section> paramCollection);


    void removeAllSections();


    SectionPanelModel getSectionModel();


    SectionRenderer getSectionRenderer();


    void showAllSections();


    void resetContext();


    void setSelectedBrowserTask(BrowserModel paramBrowserModel);


    BrowserModel getSelectedBrowserTask();


    void setSelectedQuery(UIQuery paramUIQuery);


    UISavedQuery getSelectedSavedQuery();


    UIDynamicQuery getSelectedDynamicQuery();


    UICollectionQuery getSelectedCollection();


    TypeService getTypeService();


    ObjectCollectionService getObjectCollectionService();


    SavedQueryService getSavedQueryService();


    DynamicQueryService getDynamicQueryService();


    String getHeaderURI();


    void setWidth(String paramString);


    String getWidth();
}
