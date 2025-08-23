package de.hybris.platform.cockpit.components.contentbrowser;

import de.hybris.platform.cockpit.model.advancedsearch.AdvancedSearchModelListener;
import de.hybris.platform.cockpit.model.advancedsearch.AdvancedSearchViewListener;
import de.hybris.platform.cockpit.model.advancedsearch.UIAdvancedSearchView;
import de.hybris.platform.cockpit.model.advancedsearch.impl.DefaultAdvancedSearchModel;
import de.hybris.platform.cockpit.model.misc.ComponentController;
import de.hybris.platform.cockpit.session.SearchBrowserModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultBrowserAdvancedSearchController implements ComponentController
{
    private static final Logger log = LoggerFactory.getLogger(DefaultBrowserAdvancedSearchController.class);
    private final SearchBrowserModel browser;
    private final DefaultAdvancedSearchModel model;
    private final transient UIAdvancedSearchView view;
    private AdvancedSearchModelListener modelListener = null;
    private AdvancedSearchViewListener viewListener = null;


    public DefaultBrowserAdvancedSearchController(SearchBrowserModel browser, DefaultAdvancedSearchModel model, UIAdvancedSearchView view)
    {
        this.browser = browser;
        this.model = model;
        this.view = view;
    }


    public void initialize()
    {
        this.modelListener = createModelListener(this.browser, this.view);
        this.viewListener = createViewListener(this.browser, this.model);
        this.model.addAdvancedSearchModelListener(this.modelListener);
        this.view.addAdvancedSearchViewListener(this.viewListener);
    }


    protected AdvancedSearchModelListener createModelListener(SearchBrowserModel browser, UIAdvancedSearchView view)
    {
        return (AdvancedSearchModelListener)new DefaultBrowserAdvancedSearchModelListener(browser, view);
    }


    protected AdvancedSearchViewListener createViewListener(SearchBrowserModel browser, DefaultAdvancedSearchModel model)
    {
        return (AdvancedSearchViewListener)new DefaultBrowserAdvancedSearchViewListener(browser, model);
    }


    public void unregisterListeners()
    {
        log.debug("Unregistering all listeners...");
        if(this.model != null)
        {
            this.model.removeAdvancedSearchModelListener(this.modelListener);
        }
        if(this.view != null)
        {
            this.view.removeAdvancedSearchViewListener(this.viewListener);
        }
    }
}
