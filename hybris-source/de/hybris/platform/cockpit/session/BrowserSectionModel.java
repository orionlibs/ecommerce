package de.hybris.platform.cockpit.session;

import de.hybris.platform.cockpit.events.CockpitEventAcceptor;

public interface BrowserSectionModel extends SectionModel, CockpitEventAcceptor
{
    void setBrowserSectionRenderer(BrowserSectionRenderer paramBrowserSectionRenderer);


    BrowserSectionRenderer getBrowserSectionRenderer();


    void setSectionBrowserModel(SectionBrowserModel paramSectionBrowserModel);


    SectionBrowserModel getSectionBrowserModel();
}
