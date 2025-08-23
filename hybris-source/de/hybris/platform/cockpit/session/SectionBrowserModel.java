package de.hybris.platform.cockpit.session;

import de.hybris.platform.cockpit.events.impl.SectionModelListener;
import java.util.List;

public interface SectionBrowserModel extends AdvancedBrowserModel
{
    void initialize();


    void setBrowserSectionModels(List<? extends BrowserSectionModel> paramList);


    List<BrowserSectionModel> getBrowserSectionModels();


    BrowserSectionModel getBrowserSectionModel(int paramInt);


    SectionModelListener getSectionModelListener();


    void setSectionModelListener(SectionModelListener paramSectionModelListener);
}
