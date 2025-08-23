package de.hybris.platform.cmscockpit.components.liveedit;

import de.hybris.platform.cmscockpit.session.impl.LiveEditBrowserArea;
import de.hybris.platform.cmscockpit.session.impl.LiveEditBrowserModel;
import de.hybris.platform.cmscockpit.session.impl.LiveEditContentBrowser;
import org.zkoss.zul.Hbox;

public interface LiveEditCaptionButtonHandler
{
    void createButton(LiveEditBrowserArea paramLiveEditBrowserArea, LiveEditBrowserModel paramLiveEditBrowserModel, LiveEditContentBrowser paramLiveEditContentBrowser, Hbox paramHbox);
}
