package de.hybris.platform.cmscockpit.components.liveedit.impl;

import de.hybris.platform.cmscockpit.components.liveedit.LiveEditView;
import de.hybris.platform.cmscockpit.components.liveedit.RefreshContentHandler;
import de.hybris.platform.cmscockpit.components.liveedit.RefreshContentHandlerRegistry;
import java.util.List;
import org.springframework.beans.factory.annotation.Required;

public class DefaultRefreshContentHandlerRegistry implements RefreshContentHandlerRegistry<LiveEditView>
{
    private List<RefreshContentHandler<LiveEditView>> handlers;


    public List<RefreshContentHandler<LiveEditView>> getRefreshContentHandlers()
    {
        return this.handlers;
    }


    @Required
    public void setHandlers(List<RefreshContentHandler<LiveEditView>> handlers)
    {
        this.handlers = handlers;
    }
}
