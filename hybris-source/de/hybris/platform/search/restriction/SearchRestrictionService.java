package de.hybris.platform.search.restriction;

import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.type.SearchRestrictionModel;
import de.hybris.platform.search.restriction.session.SessionSearchRestriction;
import java.util.Collection;

public interface SearchRestrictionService
{
    void addSessionSearchRestrictions(Collection<SessionSearchRestriction> paramCollection);


    void addSessionSearchRestrictions(SessionSearchRestriction... paramVarArgs);


    void clearSessionSearchRestrictions();


    void disableSearchRestrictions();


    void enableSearchRestrictions();


    Collection<SearchRestrictionModel> getActiveSearchRestrictions(PrincipalModel paramPrincipalModel, boolean paramBoolean, Collection<ComposedTypeModel> paramCollection);


    Collection<SearchRestrictionModel> getInactiveSearchRestrictions(PrincipalModel paramPrincipalModel, boolean paramBoolean, Collection<ComposedTypeModel> paramCollection);


    Collection<SearchRestrictionModel> getSearchRestrictions(PrincipalModel paramPrincipalModel, boolean paramBoolean, Collection<ComposedTypeModel> paramCollection);


    Collection<SearchRestrictionModel> getSearchRestrictionsForType(ComposedTypeModel paramComposedTypeModel);


    Collection<SessionSearchRestriction> getSessionSearchRestrictions();


    Collection<SessionSearchRestriction> getSessionSearchRestrictions(ComposedTypeModel paramComposedTypeModel);


    boolean hasRestrictions(PrincipalModel paramPrincipalModel, boolean paramBoolean, ComposedTypeModel paramComposedTypeModel);


    boolean isSearchRestrictionsEnabled();


    void removeSessionSearchRestrictions(Collection<SessionSearchRestriction> paramCollection);


    void removalAllSessionSearchRestrictions();
}
