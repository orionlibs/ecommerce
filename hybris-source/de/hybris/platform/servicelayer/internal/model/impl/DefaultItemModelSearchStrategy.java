package de.hybris.platform.servicelayer.internal.model.impl;

import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.MapTypeModel;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.ModelLoadingException;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.internal.converter.ModelConverter;
import de.hybris.platform.servicelayer.internal.converter.PersistenceObjectNotFoundException;
import de.hybris.platform.servicelayer.internal.model.ModelSearchStrategy;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.util.Utilities;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Required;

public class DefaultItemModelSearchStrategy implements ModelSearchStrategy
{
    private FlexibleSearchService flexibleSearchService;
    private I18NService i18nService;
    private TypeService typeService;
    private SessionService sessionService;


    public <T> T getModelByExample(ModelConverter conv, T example)
    {
        Map<String, Object> params = new HashMap<>();
        SearchResult<T> searchresult = getModelByExampleInternal(conv, example, params);
        List<T> result = searchresult.getResult();
        if(result.isEmpty())
        {
            throw createNoResultException(example, params);
        }
        if(result.size() > 1)
        {
            throw new AmbiguousIdentifierException("Found " + result.size() + " results for the given example [" + example + "]. Specify the example model in more detail. Searched with these attributes: " + params);
        }
        try
        {
            T resultingModel = result.get(0);
            if(resultingModel == null)
            {
                throw createNoResultException(example, params);
            }
            return resultingModel;
        }
        catch(ModelLoadingException e)
        {
            if(Utilities.getRootCauseOfType((Throwable)e, PersistenceObjectNotFoundException.class) != null)
            {
                throw createNoResultException(example, params);
            }
            throw e;
        }
    }


    private ModelNotFoundException createNoResultException(Object example, Map<String, Object> params)
    {
        return new ModelNotFoundException("No result for the given example [" + example + "] was found. Searched with these attributes: " + params);
    }


    public <T> List<T> getModelsByExample(ModelConverter conv, T example)
    {
        Map<String, Object> params = new HashMap<>();
        SearchResult<T> searchresult = getModelByExampleInternal(conv, example, params);
        return searchresult.getResult();
    }


    private <T> SearchResult<T> getModelByExampleInternal(ModelConverter modelConv, T example, Map<String, Object> params)
    {
        ServicesUtil.validateParameterNotNull(example, "example was null");
        ServicesUtil.validateParameterNotNull(modelConv, "modelConv was null");
        Map<String, Set<Locale>> modAttrQual = modelConv.getDirtyAttributes(example);
        boolean newModel = modelConv.isNew(example);
        if(newModel && !modAttrQual.isEmpty())
        {
            String type = modelConv.getType(example);
            for(Map.Entry<String, Set<Locale>> atr : modAttrQual.entrySet())
            {
                String atrQualifier = atr.getKey();
                verifySearchability(modAttrQual, type, atrQualifier);
                createParameterMap(modelConv, example, params, atr);
            }
            String conditions = createFlexibleSearchConditions(modAttrQual);
            return this.flexibleSearchService.search("GET {" + type + "} WHERE " + conditions, params);
        }
        throw new ModelLoadingException("Given example [" + example + "] must be new (it is " + (newModel ? "" : "not ") + "new) and must contain modified attributes:" + modAttrQual);
    }


    private <T> void createParameterMap(ModelConverter modelConv, T example, Map<String, Object> params, Map.Entry<String, Set<Locale>> atr)
    {
        String atrQualifier = atr.getKey();
        Set<Locale> atrLocales = atr.getValue();
        if(atrLocales == null)
        {
            params.put(atrQualifier, modelConv.getAttributeValue(example, atrQualifier));
        }
        else
        {
            this.sessionService.executeInLocalView((SessionExecutionBody)new Object(this, atrLocales, params, atrQualifier, modelConv, example));
        }
    }


    private void verifySearchability(Map<String, Set<Locale>> modAttrQual, String type, String atrQualifier)
    {
        AttributeDescriptorModel attrDescModel = this.typeService.getAttributeDescriptor(type, atrQualifier);
        if(!attrDescModel.getSearch().booleanValue())
        {
            throw new ModelLoadingException(String.format("Attribute '%s' of the example [is new; modified attributes:%s] is not searchable.", new Object[] {atrQualifier, modAttrQual}));
        }
        if(attrDescModel.getLocalized().booleanValue())
        {
            TypeModel returntype = ((MapTypeModel)attrDescModel.getAttributeType()).getReturntype();
            if(returntype instanceof MapTypeModel || returntype instanceof de.hybris.platform.core.model.type.CollectionTypeModel)
            {
                throw new ModelLoadingException(String.format("Localized attribute '%s' of the example [is new; modified attributes:%s] contains as returntype a map/collection and therefore is not searchable.", new Object[] {atrQualifier, modAttrQual}));
            }
        }
        else if(attrDescModel.getAttributeType() instanceof MapTypeModel || attrDescModel
                        .getAttributeType() instanceof de.hybris.platform.core.model.type.CollectionTypeModel)
        {
            throw new ModelLoadingException(String.format("Non-localized attribute '%s' of the example [is new; modified attributes:%s] contains as returntype a map/collection and therefore is not searchable.", new Object[] {atrQualifier, modAttrQual}));
        }
    }


    private String createFlexibleSearchConditions(Map<String, Set<Locale>> conditions)
    {
        if(conditions.isEmpty())
        {
            return "";
        }
        StringBuilder txt = new StringBuilder();
        for(Map.Entry<String, Set<Locale>> condition : conditions.entrySet())
        {
            if(condition.getValue() == null)
            {
                txt.append("{" + (String)condition.getKey() + "}=?" + (String)condition.getKey() + " AND ");
                continue;
            }
            for(Locale loc : condition.getValue())
            {
                String langcode = this.i18nService.getBestMatchingLocale(loc).getLanguage();
                txt.append("{" + (String)condition
                                .getKey() + "[" + langcode + "]}=?" + (String)condition.getKey() + "_" + langcode + " AND ");
            }
        }
        txt.setLength(txt.length() - 4);
        return txt.toString();
    }


    @Required
    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }


    @Required
    public void setI18nService(I18NService i18nService)
    {
        this.i18nService = i18nService;
    }


    @Required
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }


    @Required
    public void setSessionService(SessionService sessionService)
    {
        this.sessionService = sessionService;
    }
}
