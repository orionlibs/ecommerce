package de.hybris.platform.adaptivesearchbackoffice.actions.results;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.CockpitAction;
import de.hybris.platform.adaptivesearchbackoffice.data.NavigationContextData;
import de.hybris.platform.adaptivesearchbackoffice.data.SearchContextData;
import de.hybris.platform.adaptivesearchbackoffice.widgets.searchresultbrowser.DocumentModel;
import de.hybris.platform.adaptivesearchbackoffice.widgets.searchresultbrowser.SearchResultBrowserViewModel;

public abstract class AbstractResultAction implements CockpitAction<DocumentModel, Object>
{
    protected static final String VIEW_MODEL_PARAM = "viewModel";


    protected SearchResultBrowserViewModel getViewModel(ActionContext<DocumentModel> ctx)
    {
        Object viewModel = ctx.getParameter("viewModel");
        if(viewModel instanceof SearchResultBrowserViewModel)
        {
            return (SearchResultBrowserViewModel)viewModel;
        }
        return null;
    }


    protected NavigationContextData getNavigationContext(ActionContext<DocumentModel> ctx)
    {
        SearchResultBrowserViewModel viewModel = getViewModel(ctx);
        if(viewModel != null && viewModel.getSearchResult() != null && viewModel.getSearchResult().getNavigationContext() != null)
        {
            return viewModel.getSearchResult().getNavigationContext();
        }
        return null;
    }


    protected SearchContextData getSearchContext(ActionContext<DocumentModel> ctx)
    {
        SearchResultBrowserViewModel viewModel = getViewModel(ctx);
        if(viewModel != null && viewModel.getSearchResult() != null && viewModel.getSearchResult().getSearchContext() != null)
        {
            return viewModel.getSearchResult().getSearchContext();
        }
        return null;
    }


    protected boolean isValidContextParams(ActionContext<DocumentModel> ctx)
    {
        SearchResultBrowserViewModel viewModel = getViewModel(ctx);
        return (ctx.getData() != null && viewModel != null && viewModel.getNavigationContext() != null && viewModel
                        .getSearchContext() != null);
    }


    protected void refreshSearchResults(ActionContext<DocumentModel> ctx)
    {
        SearchResultBrowserViewModel viewModel = getViewModel(ctx);
        if(viewModel != null)
        {
            viewModel.refreshSearchResults();
        }
    }
}
