package de.hybris.platform.cockpit.components.contentbrowser;

import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.BrowserModel;
import java.util.Set;
import org.zkoss.zul.Div;

public abstract class AbstractBrowserComponent extends Div implements BrowserComponent
{
    protected static final String CONTENT_BROWSER_GB_SCLASS = "contentBrowserGroupbox";
    protected static final String CONTENT_BROWSER_STICKY_GB_SCLASS = "contentBrowserStickyGroupbox";
    protected static final String BROWSER_TOOLBAR_SCLASS = "query-browser-paging";
    protected static final String MAIN_AREA_BL_SCLASS = "plainBorderlayout query_browser_content";
    protected static final String SEARCH_MAGNIFIER_BTN_IMG = "/cockpit/images/BUTTON_search.png";
    protected static final String ADV_QUERY_BTN_IMG = "/cockpit/images/icon_func_advanced_search.png";
    protected static final String ADV_QUERY_BTN_ACTIVE_IMG = "/cockpit/images/icon_func_advanced_search_white.png";
    protected static final String SAVE_QUERY_BTN_IMG = "/cockpit/images/icon_func_search_save.png";
    protected static final String DUPLICATE_BTN_IMG = "/cockpit/images/duplicate_btn.png";
    protected static final String SPLIT_ACTIVE_BTN_IMG = "/cockpit/images/button_view_splitmode_available_a.png";
    protected static final String SPLIT_INACTIVE_BTN_IMG = "/cockpit/images/button_view_splitmode_available_i.png";
    protected static final String MINIMIZE_BTN_IMG = "/cockpit/images/collapse_btn.gif";
    protected static final String CLOSE_BTN_IMG = "/cockpit/images/close_btn.png";
    private BrowserModel model = null;
    private AbstractContentBrowser contentBrowser = null;
    protected boolean initialized = false;


    public AbstractBrowserComponent(BrowserModel model, AbstractContentBrowser contentBrowser)
    {
        if(model == null)
        {
            throw new IllegalArgumentException("Browser component model can not be null.");
        }
        if(contentBrowser == null)
        {
            throw new IllegalArgumentException("Content browser can not be null.");
        }
        this.model = model;
        this.contentBrowser = contentBrowser;
    }


    public abstract boolean update();


    public abstract void setActiveItem(TypedObject paramTypedObject);


    public abstract void updateActiveItems();


    public abstract void updateSelectedItems();


    public abstract void updateItem(TypedObject paramTypedObject, Set<PropertyDescriptor> paramSet);


    public void updateItem(TypedObject item, Set<PropertyDescriptor> modifiedProperties, Object reason)
    {
        updateItem(item, modifiedProperties);
    }


    public abstract boolean initialize();


    public abstract void resize();


    public BrowserModel getModel()
    {
        return this.model;
    }


    public AbstractContentBrowser getContentBrowser()
    {
        return this.contentBrowser;
    }


    public void setModel(BrowserModel model)
    {
        if(this.model != model)
        {
            this.model = model;
            if(this.model != null)
            {
                initialize();
            }
        }
    }
}
