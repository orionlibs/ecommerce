package de.hybris.platform.cmscockpit.components.contentbrowser;

import de.hybris.platform.cms2.model.CMSPageTypeModel;
import de.hybris.platform.cmscockpit.components.liveedit.LiveEditView;
import de.hybris.platform.cmscockpit.components.liveedit.impl.DefaultLiveEditViewModel;
import de.hybris.platform.cockpit.components.contentbrowser.AbstractContentBrowser;
import de.hybris.platform.cockpit.components.contentbrowser.AbstractMainAreaBrowserComponent;
import de.hybris.platform.cockpit.components.contentbrowser.ContextAwareMainAreaComponentFactory;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.AdvancedBrowserModel;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.SectionBrowserModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.type.TypeService;
import org.apache.log4j.Logger;
import org.zkoss.spring.SpringUtil;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.util.Clients;

public class CmsPageMainAreaPreviewComponentFactory implements ContextAwareMainAreaComponentFactory
{
    private static final Logger LOG = Logger.getLogger(CmsPageMainAreaPreviewComponentFactory.class);
    protected static final String HYBRIS_PREVIEW_TAB_NAME = "Hybris CM2 Preview";
    public static final String VIEWMODE_ID = "PREVIEW";
    public static final String PREVIEW_FRAME_KEY = "previewFrame";
    private final TypedObject wrappedCurrentPageModel;
    private TypeService typeService;


    public CmsPageMainAreaPreviewComponentFactory(TypedObject wrappedCurrentPageModel)
    {
        this.wrappedCurrentPageModel = wrappedCurrentPageModel;
    }


    protected void openPreviewInNewTab(String previewUrl)
    {
        Clients.evalJavaScript("window.open('" + previewUrl + "','Hybris CM2 Preview')");
    }


    public AbstractMainAreaBrowserComponent createInstance(AdvancedBrowserModel model, AbstractContentBrowser contentBrowser)
    {
        Object object = null;
        if(model instanceof SectionBrowserModel)
        {
            object = new Object(this, (SectionBrowserModel)model, contentBrowser, contentBrowser);
        }
        return (AbstractMainAreaBrowserComponent)object;
    }


    protected DefaultLiveEditViewModel newDefaultLiveEditViewModel()
    {
        return new DefaultLiveEditViewModel();
    }


    protected LiveEditView newDefaultLiveEditView(DefaultLiveEditViewModel liveEditViewModel)
    {
        return new LiveEditView(liveEditViewModel);
    }


    public String getActiveButtonImage()
    {
        return "/cmscockpit/images/button_view_preview_available_a.png";
    }


    public String getButtonLabel()
    {
        return null;
    }


    public String getButtonTooltip()
    {
        return Labels.getLabel("browser.page.preview");
    }


    public String getInactiveButtonImage()
    {
        return "/cmscockpit/images/button_view_preview_available_i.png";
    }


    public String getViewModeID()
    {
        return "PREVIEW";
    }


    public boolean isAvailable(BrowserModel browserModel)
    {
        boolean ret = false;
        TypedObject wrappedPageModel = this.wrappedCurrentPageModel;
        if(wrappedPageModel == null)
        {
            return ret;
        }
        if(wrappedPageModel.getObject() instanceof de.hybris.platform.cms2.model.pages.AbstractPageModel)
        {
            ComposedTypeModel composedType = getTypeService().getComposedTypeForClass(wrappedPageModel.getObject().getClass());
            ret = !((CMSPageTypeModel)composedType).isPreviewDisabled();
        }
        return ret;
    }


    protected TypeService getTypeService()
    {
        if(this.typeService == null)
        {
            this.typeService = (TypeService)SpringUtil.getBean("typeService");
        }
        return this.typeService;
    }


    public TypedObject getWrappedCurrentPageModel()
    {
        return this.wrappedCurrentPageModel;
    }


    public boolean hasOwnModel()
    {
        return false;
    }
}
