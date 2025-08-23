package de.hybris.platform.platformbackoffice.actions.savedqueries;

import com.hybris.backoffice.widgets.advancedsearch.impl.AdvancedSearchData;
import com.hybris.cockpitng.dataaccess.facades.type.exceptions.TypeNotFoundException;
import java.util.Collection;

public interface SaveQueryActionChecker
{
    Collection<SaveQueryInvalidCondition> check(AdvancedSearchData paramAdvancedSearchData) throws TypeNotFoundException;
}
