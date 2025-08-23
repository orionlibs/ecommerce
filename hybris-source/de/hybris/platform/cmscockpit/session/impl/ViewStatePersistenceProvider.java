package de.hybris.platform.cmscockpit.session.impl;

import java.util.Map;

public interface ViewStatePersistenceProvider
{
    Map<String, Object> getViewStateForComponent(ViewStatePersisteable paramViewStatePersisteable);


    void clearViewStateForComponent(ViewStatePersisteable paramViewStatePersisteable);
}
