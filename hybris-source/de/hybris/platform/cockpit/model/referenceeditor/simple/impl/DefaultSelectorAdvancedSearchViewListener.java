package de.hybris.platform.cockpit.model.referenceeditor.simple.impl;

import de.hybris.platform.cockpit.model.advancedsearch.AdvancedSearchParameterContainer;
import de.hybris.platform.cockpit.model.advancedsearch.impl.AbstractAdvancedSearchViewListener;
import de.hybris.platform.cockpit.model.advancedsearch.impl.DefaultAdvancedSearchModel;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.referenceeditor.simple.AbstractSimpleReferenceSelectorModel;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.session.UISessionUtils;

public class DefaultSelectorAdvancedSearchViewListener extends AbstractAdvancedSearchViewListener
{
    private final AbstractSimpleReferenceSelectorModel referenceSelectorModel;
    private TypeService typeService = null;


    public DefaultSelectorAdvancedSearchViewListener(DefaultAdvancedSearchModel model, AbstractSimpleReferenceSelectorModel referenceSelectorModel)
    {
        super(model);
        this.referenceSelectorModel = referenceSelectorModel;
    }


    public void searchButtonClicked(AdvancedSearchParameterContainer parameterContainer)
    {
        ObjectTemplate rootType = this.model.getSelectedType();
        this.model.setSortedByProperty(parameterContainer.getSortProperty());
        this.referenceSelectorModel.doSearch(rootType, parameterContainer, 0);
    }


    protected TypeService getTypeService()
    {
        if(this.typeService == null)
        {
            this.typeService = UISessionUtils.getCurrentSession().getTypeService();
        }
        return this.typeService;
    }
}
