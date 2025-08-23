package de.hybris.platform.personalizationfacades.action;

import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.personalizationfacades.data.ActionData;
import de.hybris.platform.personalizationfacades.data.ActionFullData;
import de.hybris.platform.personalizationservices.enums.CxActionType;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface ActionFacade
{
    ActionData getAction(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5);


    List<ActionData> getActions(String paramString1, String paramString2, String paramString3, String paramString4);


    SearchPageData<ActionFullData> getActions(CxActionType paramCxActionType, String paramString1, String paramString2, Map<String, String> paramMap, SearchPageData<?> paramSearchPageData);


    ActionData createAction(String paramString1, String paramString2, ActionData paramActionData, String paramString3, String paramString4);


    default Collection<ActionData> createActions(String customizationCode, String variationCode, Collection<ActionData> actions, String catalogId, String catalogVersionId)
    {
        throw new UnsupportedOperationException("Batch create is not supported");
    }


    ActionData updateAction(String paramString1, String paramString2, String paramString3, ActionData paramActionData, String paramString4, String paramString5);


    void deleteAction(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5);


    default void deleteActions(String customizationCode, String variationCode, Collection<String> actionCodes, String catalogId, String catalogVersionId)
    {
        throw new UnsupportedOperationException("Batch delete is not supported");
    }
}
