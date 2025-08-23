package de.hybris.platform.warehousing.atp.formula.dao.impl;

import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.warehousing.atp.formula.dao.AtpFormulaDao;
import de.hybris.platform.warehousing.model.AtpFormulaModel;
import java.util.Collection;

public class DefaultAtpFormulaDao extends AbstractItemDao implements AtpFormulaDao
{
    protected static final String ALL_ATPFORMULA_QUERY = "SELECT {pk} FROM {AtpFormula}";


    public Collection<AtpFormulaModel> getAllAtpFormula()
    {
        SearchResult<AtpFormulaModel> searchResult = search(new FlexibleSearchQuery("SELECT {pk} FROM {AtpFormula}"));
        ServicesUtil.validateIfAnyResult(searchResult.getResult(), "No AtpFormula found");
        return searchResult.getResult();
    }
}
