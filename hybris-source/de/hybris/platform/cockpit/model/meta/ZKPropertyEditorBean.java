package de.hybris.platform.cockpit.model.meta;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Page;

public interface ZKPropertyEditorBean
{
    public static final String EDITOR_BEAN_ID = "editorBean";


    Component getOrCreateViewComponent(Page paramPage);
}
