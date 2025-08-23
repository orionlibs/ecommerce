package de.hybris.platform.cockpit.model.referenceeditor.impl;

import de.hybris.platform.cockpit.model.advancedsearch.AdvancedSearchModelListener;
import de.hybris.platform.cockpit.model.advancedsearch.AdvancedSearchViewListener;
import de.hybris.platform.cockpit.model.advancedsearch.UIAdvancedSearchView;
import de.hybris.platform.cockpit.model.advancedsearch.impl.DefaultAdvancedSearchModel;
import de.hybris.platform.cockpit.model.misc.ComponentController;
import de.hybris.platform.cockpit.model.referenceeditor.AbstractReferenceSelectorModel;

public class DefaultSelectorAdvancedSearchController implements ComponentController
{
    protected final AbstractReferenceSelectorModel referenceSelectorModel;
    protected final transient UIAdvancedSearchView view;
    private AdvancedSearchModelListener advancedSearchModelListener;
    private AdvancedSearchViewListener advancedSearchViewListener;


    public DefaultSelectorAdvancedSearchController(AbstractReferenceSelectorModel referenceSelectorModel, UIAdvancedSearchView view)
    {
        this.referenceSelectorModel = referenceSelectorModel;
        this.view = view;
    }


    public void initialize()
    {
        this.advancedSearchModelListener = createModelListener(this.view);
        this.advancedSearchViewListener = createViewListener((DefaultAdvancedSearchModel)this.referenceSelectorModel
                        .getAdvancedSearchModel());
        ((DefaultAdvancedSearchModel)this.referenceSelectorModel.getAdvancedSearchModel())
                        .addAdvancedSearchModelListener(this.advancedSearchModelListener);
        this.view.addAdvancedSearchViewListener(this.advancedSearchViewListener);
    }


    protected AdvancedSearchModelListener createModelListener(UIAdvancedSearchView view)
    {
        return (AdvancedSearchModelListener)new DefaultSelectorAdvancedSearchModelListener(view);
    }


    protected AdvancedSearchViewListener createViewListener(DefaultAdvancedSearchModel model)
    {
        return (AdvancedSearchViewListener)new DefaultSelectorAdvancedSearchViewListener(model, this.referenceSelectorModel);
    }


    public void unregisterListeners()
    {
        if(this.view != null)
        {
            this.view.removeAdvancedSearchViewListener(this.advancedSearchViewListener);
        }
        if(this.referenceSelectorModel.getAdvancedSearchModel() != null)
        {
            ((DefaultAdvancedSearchModel)this.referenceSelectorModel.getAdvancedSearchModel())
                            .removeAdvancedSearchModelListener(this.advancedSearchModelListener);
        }
    }
}
