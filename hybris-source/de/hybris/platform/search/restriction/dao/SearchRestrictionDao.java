package de.hybris.platform.search.restriction.dao;

import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.type.SearchRestrictionModel;
import java.util.Collection;
import java.util.List;

public interface SearchRestrictionDao
{
    List<SearchRestrictionModel> findSearchRestrictionsByPrincipalAndType(PrincipalModel paramPrincipalModel, boolean paramBoolean, Collection<ComposedTypeModel> paramCollection);


    List<SearchRestrictionModel> findSearchRestrictionsByType(ComposedTypeModel paramComposedTypeModel);


    List<SearchRestrictionModel> findActiveSearchRestrictionsByPrincipalAndType(PrincipalModel paramPrincipalModel, boolean paramBoolean, Collection<ComposedTypeModel> paramCollection);


    List<SearchRestrictionModel> findInactiveSearchRestrictionsByPrincipalAndType(PrincipalModel paramPrincipalModel, boolean paramBoolean, Collection<ComposedTypeModel> paramCollection);
}
