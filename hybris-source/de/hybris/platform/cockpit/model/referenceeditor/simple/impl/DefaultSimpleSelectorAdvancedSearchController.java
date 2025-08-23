package de.hybris.platform.cockpit.model.referenceeditor.simple.impl;

import de.hybris.platform.cockpit.model.advancedsearch.AdvancedSearchModelListener;
import de.hybris.platform.cockpit.model.advancedsearch.AdvancedSearchViewListener;
import de.hybris.platform.cockpit.model.advancedsearch.UIAdvancedSearchView;
import de.hybris.platform.cockpit.model.advancedsearch.impl.DefaultAdvancedSearchModel;
import de.hybris.platform.cockpit.model.misc.ComponentController;
import de.hybris.platform.cockpit.model.referenceeditor.impl.DefaultSelectorAdvancedSearchModelListener;
import de.hybris.platform.cockpit.model.referenceeditor.simple.AbstractSimpleReferenceSelectorModel;

public class DefaultSimpleSelectorAdvancedSearchController implements ComponentController
{
    protected final AbstractSimpleReferenceSelectorModel referenceSelectorModel;
    protected final transient UIAdvancedSearchView view;
    private AdvancedSearchModelListener advancedModelListener;
    private AdvancedSearchViewListener advancedViewListener;


    public DefaultSimpleSelectorAdvancedSearchController(AbstractSimpleReferenceSelectorModel referenceSelectorModel, UIAdvancedSearchView view)
    {
        this.referenceSelectorModel = referenceSelectorModel;
        this.view = view;
    }


    public void initialize()
    {
        this.advancedModelListener = createModelListener(this.view);
        this.advancedViewListener = createViewListener((DefaultAdvancedSearchModel)this.referenceSelectorModel
                        .getAdvancedSearchModel());
        ((DefaultAdvancedSearchModel)this.referenceSelectorModel.getAdvancedSearchModel())
                        .addAdvancedSearchModelListener(this.advancedModelListener);
        this.view.addAdvancedSearchViewListener(this.advancedViewListener);
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
            this.view.removeAdvancedSearchViewListener(this.advancedViewListener);
        }
        if(this.referenceSelectorModel.getAdvancedSearchModel() != null)
        {
            ((DefaultAdvancedSearchModel)this.referenceSelectorModel.getAdvancedSearchModel())
                            .removeAdvancedSearchModelListener(this.advancedModelListener);
        }
    }
}
