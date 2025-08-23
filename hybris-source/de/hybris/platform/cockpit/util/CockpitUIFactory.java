package de.hybris.platform.cockpit.util;

import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.Richlet;
import org.zkoss.zk.ui.http.SimpleUiFactory;
import org.zkoss.zk.ui.metainfo.PageDefinition;
import org.zkoss.zk.ui.sys.RequestInfo;

public class CockpitUIFactory extends SimpleUiFactory
{
    public Page newPage(RequestInfo requestInfo, PageDefinition pagedef, String path)
    {
        return (Page)new CockpitPage(pagedef);
    }


    public Page newPage(RequestInfo requestInfo, Richlet richlet, String path)
    {
        return (Page)new CockpitPage(richlet, path);
    }
}
