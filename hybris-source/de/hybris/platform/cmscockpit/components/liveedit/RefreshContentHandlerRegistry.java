package de.hybris.platform.cmscockpit.components.liveedit;

import java.util.List;

public interface RefreshContentHandlerRegistry<V extends LiveEditView>
{
    List<RefreshContentHandler<V>> getRefreshContentHandlers();
}
