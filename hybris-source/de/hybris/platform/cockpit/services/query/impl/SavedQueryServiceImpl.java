package de.hybris.platform.cockpit.services.query.impl;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeUnitModel;
import de.hybris.platform.classification.features.FeatureValue;
import de.hybris.platform.cockpit.jalo.CockpitSavedQuery;
import de.hybris.platform.cockpit.model.CockpitSavedParameterValueModel;
import de.hybris.platform.cockpit.model.CockpitSavedQueryModel;
import de.hybris.platform.cockpit.model.CockpitSavedSortCriterionModel;
import de.hybris.platform.cockpit.model.advancedsearch.ConditionValue;
import de.hybris.platform.cockpit.model.advancedsearch.impl.DefaultConditionValue;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.search.Operator;
import de.hybris.platform.cockpit.model.search.Query;
import de.hybris.platform.cockpit.model.search.SearchParameterDescriptor;
import de.hybris.platform.cockpit.model.search.SearchParameterValue;
import de.hybris.platform.cockpit.model.search.SearchType;
import de.hybris.platform.cockpit.services.impl.AbstractServiceImpl;
import de.hybris.platform.cockpit.services.query.SavedQueryService;
import de.hybris.platform.cockpit.services.query.daos.SavedQueryDao;
import de.hybris.platform.cockpit.services.search.SearchProvider;
import de.hybris.platform.cockpit.services.search.SearchService;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.core.HybrisEnumValue;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.servicelayer.exceptions.ModelRemovalException;
import de.hybris.platform.util.Base64;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class SavedQueryServiceImpl extends AbstractServiceImpl implements SavedQueryService
{
    private static final Logger LOG = LoggerFactory.getLogger(SavedQueryServiceImpl.class);
    protected static final String PK_MARK = "PK:";
    protected static final String DATE_MARK = "DATE:";
    protected static final String ENUM_MARK = "ENUM:";
    protected static final String NUMBER_MARK = "NUMBER:";
    protected static final String FEATURE_VALUE_MARK = "FEATURE_VALUE:";
    protected static final String ENUM_DELIM = ".";
    protected static final String COLLECTION_DELIM = ",";
    protected static final String FEATURE_VALUE_DELIM = ",";
    protected static final String NUMBER_VALUE_DELIM = ":";
    protected static final int NUMBER_CLASS_NAME_POS = 0;
    protected static final int NUMBER_VALUE_POS = 1;
    protected static final int FEATURE_VALUE_STRING_VALUE_POS = 0;
    protected static final int FEATURE_VALUE_UNIT_POS = 1;
    private SavedQueryDao savedQueryDao;
    private EnumerationService enumerationService;
    protected SearchService searchService;


    protected String getPKsString(Collection<? extends ItemModel> items)
    {
        String pkStr = "";
        if(items != null && !items.isEmpty())
        {
            for(ItemModel cv : items)
            {
                pkStr = pkStr + "," + pkStr;
            }
            pkStr = pkStr.substring(1);
        }
        return pkStr;
    }


    @Deprecated
    protected void addSpecialParameters(CockpitSavedQuery cockpitSavedQuery, Query query)
    {
    }


    protected void addSpecialParameters(CockpitSavedQueryModel cockpitSavedQueryModel, Query query)
    {
    }


    public CockpitSavedQueryModel createSavedQuery(String label, Query query, UserModel user)
    {
        CockpitSavedQueryModel savedQueryModel = (CockpitSavedQueryModel)getModelService().create(CockpitSavedQueryModel.class);
        savedQueryModel.setLabel(label);
        savedQueryModel.setUser(user);
        savedQueryModel.setSimpleText(query.getSimpleText());
        if(query.getSelectedTypes().isEmpty())
        {
            throw new IllegalArgumentException("Query has no type set");
        }
        if(query.getSelectedTypes().size() > 1)
        {
            LOG.warn("Query has ambigious types. Only one will be saved.");
        }
        Iterator<SearchType> iterator = query.getSelectedTypes().iterator();
        if(iterator.hasNext())
        {
            savedQueryModel.setSelectedTypeCode(((SearchType)iterator.next()).getCode());
        }
        if(query.getContextParameter("objectTemplate") != null)
        {
            try
            {
                savedQueryModel.setSelectedTemplateCode(((ObjectTemplate)query
                                .getContextParameter("objectTemplate")).getCode());
            }
            catch(ClassCastException cce)
            {
                LOG.warn("Could not save selected template.", cce);
            }
        }
        getModelService().save(savedQueryModel);
        for(Map.Entry<PropertyDescriptor, Boolean> criterionEntry : (Iterable<Map.Entry<PropertyDescriptor, Boolean>>)query.getSortCriteria().entrySet())
        {
            CockpitSavedSortCriterionModel criterionModel = (CockpitSavedSortCriterionModel)getModelService().create(CockpitSavedSortCriterionModel.class);
            criterionModel.setCriterionQualifier(((PropertyDescriptor)criterionEntry.getKey()).getQualifier());
            criterionModel.setAsc(criterionEntry.getValue());
            criterionModel.setCockpitSavedQuery(savedQueryModel);
            getModelService().save(criterionModel);
        }
        for(SearchParameterValue param : query.getParameterValues())
        {
            CockpitSavedParameterValueModel parameterValueModel = (CockpitSavedParameterValueModel)getModelService().create(CockpitSavedParameterValueModel.class);
            parameterValueModel.setParameterQualifier(param.getParameterDescriptor().getQualifier());
            parameterValueModel.setOperatorQualifier(param.getOperator().getQualifier());
            parameterValueModel.setRawValue(wrapCollectionValues(getDecodedValues(param.getValue())));
            parameterValueModel.setCockpitSavedQuery(savedQueryModel);
            getModelService().save(parameterValueModel);
        }
        for(List<SearchParameterValue> orParameters : (Iterable<List<SearchParameterValue>>)query.getParameterOrValues())
        {
            if(orParameters.isEmpty())
            {
                continue;
            }
            CockpitSavedParameterValueModel parameterValueModel = (CockpitSavedParameterValueModel)getModelService().create(CockpitSavedParameterValueModel.class);
            parameterValueModel.setParameterQualifier(((SearchParameterValue)orParameters.get(0)).getParameterDescriptor().getQualifier());
            parameterValueModel.setReference(Boolean.TRUE);
            parameterValueModel.setOperatorQualifier(((SearchParameterValue)orParameters.get(0)).getOperator().getQualifier());
            StringBuilder commaSeparatedPK = new StringBuilder();
            for(SearchParameterValue paramValue : orParameters)
            {
                Object value = paramValue.getValue();
                commaSeparatedPK.append("," + ((ItemModel)value).getPk().toString());
            }
            parameterValueModel.setRawValue((commaSeparatedPK.length() != 0) ? commaSeparatedPK.substring(1) : null);
            parameterValueModel.setCockpitSavedQuery(savedQueryModel);
            getModelService().save(parameterValueModel);
        }
        getModelService().refresh(savedQueryModel);
        return savedQueryModel;
    }


    protected List<String> getDecodedValues(Object paramValue)
    {
        if(paramValue instanceof ConditionValue)
        {
            paramValue = ((ConditionValue)paramValue).getValues();
        }
        List<String> decodedValues = new ArrayList<>();
        if(paramValue instanceof Collection)
        {
            for(Object val : paramValue)
            {
                decodedValues.add(wrapSingleValue(val));
            }
        }
        else if(paramValue.getClass().isArray())
        {
            Object[] vals = (Object[])paramValue;
            for(Object val : vals)
            {
                decodedValues.add(wrapSingleValue(val));
            }
        }
        else
        {
            decodedValues.add(wrapSingleValue(paramValue));
        }
        return decodedValues;
    }


    public Collection<CockpitSavedQueryModel> getSavedQueries(SearchProvider provider, UserModel user)
    {
        Collection<CockpitSavedQueryModel> ret = new ArrayList<>();
        ret.addAll(getSavedQueryDao().findSavedQueriesByUser(user));
        ret.addAll(getSavedQueryDao().findGlobalSavedQueries());
        return ret;
    }


    public void storeUpdates(CockpitSavedQueryModel query)
    {
        getModelService().save(query);
    }


    public Query getQuery(CockpitSavedQueryModel savedQuery)
    {
        if(savedQuery.getSelectedTypeCode() == null)
        {
            throw new IllegalArgumentException("Saved query has no type code set.");
        }
        SearchType type = UISessionUtils.getCurrentSession().getSearchService().getSearchType(savedQuery.getSelectedTypeCode());
        Query ret = new Query(Collections.singletonList(type), savedQuery.getSimpleText(), 0, 0);
        List<SearchParameterValue> parameterValues = new ArrayList<>();
        String templateCode = savedQuery.getSelectedTemplateCode();
        if(templateCode != null)
        {
            ret.setContextParameter("objectTemplate",
                            UISessionUtils.getCurrentSession().getTypeService().getObjectTemplate(templateCode));
        }
        for(CockpitSavedParameterValueModel parameterValueModel : savedQuery.getCockpitSavedParameterValues())
        {
            if(StringUtils.isEmpty(parameterValueModel.getRawValue()))
            {
                LOG.warn("Invalid SavedSearchParameterValue: " + parameterValueModel.getParameterQualifier());
                continue;
            }
            if(Boolean.TRUE.equals(parameterValueModel.getReference()))
            {
                List<SearchParameterValue> orValues = new ArrayList<>();
                SearchParameterDescriptor descriptor = this.searchService.getSearchDescriptor(getTypeService().getPropertyDescriptor(parameterValueModel.getParameterQualifier()));
                Operator operator = new Operator(parameterValueModel.getOperatorQualifier());
                if(parameterValueModel.getRawValue() != null)
                {
                    for(String s : parameterValueModel.getRawValue().split(","))
                    {
                        ItemModel objectValue = (ItemModel)getModelService().get(PK.parse(s));
                        SearchParameterValue spv = new SearchParameterValue(descriptor, objectValue, operator);
                        orValues.add(spv);
                    }
                }
                ret.addParameterOrValues(orValues);
                continue;
            }
            try
            {
                SearchParameterDescriptor descriptor = this.searchService.getSearchDescriptor(getTypeService().getPropertyDescriptor(parameterValueModel.getParameterQualifier()));
                Operator operator = new Operator(parameterValueModel.getOperatorQualifier());
                SearchParameterValue spv = new SearchParameterValue(descriptor, wrap(parameterValueModel.getRawValue(), operator), operator);
                parameterValues.add(spv);
            }
            catch(IllegalArgumentException e)
            {
                LOG.error("Not able to add search parameter for qualifier '" + parameterValueModel.getParameterQualifier() + "'");
            }
        }
        ret.setParameterValues(parameterValues);
        for(CockpitSavedSortCriterionModel sortCriterion : savedQuery.getCockpitSavedSortCriteria())
        {
            PropertyDescriptor sortProperty = getTypeService().getPropertyDescriptor(sortCriterion.getCriterionQualifier());
            ret.addSortCriterion(sortProperty, sortCriterion.getAsc().booleanValue());
        }
        return ret;
    }


    public void deleteQuery(CockpitSavedQueryModel query)
    {
        try
        {
            getModelService().remove(query);
        }
        catch(ModelRemovalException mre)
        {
            LOG.error("Could not delete saved query, reason: ", (Throwable)mre);
        }
    }


    public void publishSavedQuery(CockpitSavedQueryModel query)
    {
        query.setUser(null);
        this.modelService.save(query);
    }


    public void renameQuery(CockpitSavedQueryModel query, String label)
    {
        query.setLabel(label);
        this.modelService.save(query);
    }


    @Required
    public void setSearchService(SearchService searchService)
    {
        this.searchService = searchService;
    }


    @Required
    public void setEnumerationService(EnumerationService enumerationService)
    {
        this.enumerationService = enumerationService;
    }


    protected Object unwrapSingleValue(String value)
    {
        String data = null;
        String stringValue = value;
        if(StringUtils.equalsIgnoreCase(value, Boolean.TRUE.toString()) ||
                        StringUtils.equalsIgnoreCase(value, Boolean.FALSE.toString()))
        {
            return Boolean.valueOf(value);
        }
        if(stringValue.startsWith("ENUM:"))
        {
            data = stringValue.replace("ENUM:", "");
            Iterable<String> enumCodes = Splitter.on(".").split(data);
            return this.enumerationService.getEnumerationValue((String)Iterables.get(enumCodes, 0), (String)Iterables.get(enumCodes, 1));
        }
        if(stringValue.startsWith("PK:"))
        {
            data = stringValue.replace("PK:", "");
            PK pk = PK.parse(data);
            return getModelService().get(pk);
        }
        if(stringValue.startsWith("DATE:"))
        {
            data = stringValue.replace("DATE:", "");
            return new Date(Long.parseLong(data));
        }
        if(stringValue.startsWith("FEATURE_VALUE:"))
        {
            return unwrapFeatureValue(stringValue);
        }
        if(stringValue.startsWith("NUMBER:"))
        {
            return unwrapNumber(stringValue);
        }
        return value;
    }


    protected String wrapSingleValue(Object value)
    {
        String ret = "";
        if(value instanceof HybrisEnumValue)
        {
            ret = "ENUM:" + ((HybrisEnumValue)value).getType() + "." + ((HybrisEnumValue)value).getCode();
        }
        else if(value instanceof ItemModel)
        {
            ret = "PK:" + ((ItemModel)value).getPk().toString();
        }
        else if(value instanceof Date)
        {
            ret = "DATE:" + ((Date)value).getTime();
        }
        else if(value instanceof FeatureValue)
        {
            ret = wrapFeatureValue((FeatureValue)value);
        }
        else if(value instanceof Number)
        {
            ret = wrapNumber((Number)value);
        }
        else
        {
            ret = value.toString();
        }
        return ret;
    }


    protected String wrapCollectionValues(List<String> values)
    {
        String ret = null;
        if(CollectionUtils.isNotEmpty(values))
        {
            boolean first = true;
            StringBuilder builder = new StringBuilder();
            for(String string : values)
            {
                String encoded = Base64.encodeBytes(string.getBytes());
                if(first)
                {
                    first = false;
                }
                else
                {
                    builder.append(",");
                }
                builder.append(encoded);
            }
            ret = builder.toString();
        }
        return ret;
    }


    protected String wrapFeatureValue(FeatureValue featureValue)
    {
        StringBuilder sb = new StringBuilder("FEATURE_VALUE:");
        sb.append(wrapSingleValue(featureValue.getValue()));
        if(featureValue.getUnit() != null)
        {
            sb.append(",");
            sb.append(wrapSingleValue(featureValue.getUnit()));
        }
        return sb.toString();
    }


    protected FeatureValue unwrapFeatureValue(String wrappedValue)
    {
        String[] values = StringUtils.remove(wrappedValue, "FEATURE_VALUE:").split(",");
        if(values.length <= 0)
        {
            return null;
        }
        Object featureValue = unwrapSingleValue(values[0]);
        if(featureValue == null)
        {
            return null;
        }
        ClassificationAttributeUnitModel unitModel = null;
        if(values.length > 1)
        {
            unitModel = (ClassificationAttributeUnitModel)unwrapSingleValue(values[1]);
        }
        return new FeatureValue(featureValue, null, unitModel);
    }


    protected String wrapNumber(Number value)
    {
        return String.format("%s%s%s%s", new Object[] {"NUMBER:", value.getClass().getSimpleName(), ":", value.toString()});
    }


    protected Number unwrapNumber(String wrappedValue)
    {
        String[] typeAndValue = StringUtils.remove(wrappedValue, "NUMBER:").split(":");
        String typeName = typeAndValue[0];
        String valueString = typeAndValue[1];
        if(Double.class.getSimpleName().equals(typeName))
        {
            return Double.valueOf(valueString);
        }
        if(Integer.class.getSimpleName().equals(typeName))
        {
            return Integer.valueOf(valueString);
        }
        if(Float.class.getSimpleName().equals(typeName))
        {
            return Float.valueOf(valueString);
        }
        if(Long.class.getSimpleName().equals(typeName))
        {
            return Long.valueOf(valueString);
        }
        if(Byte.class.getSimpleName().equals(typeName))
        {
            return Byte.valueOf(typeName);
        }
        return null;
    }


    protected String decode(String value)
    {
        String ret = null;
        byte[] decoded = Base64.decode(value);
        if(decoded == null)
        {
            ret = value;
        }
        else
        {
            ret = new String(decoded);
        }
        return ret;
    }


    protected List<Object> unwrapCollectionValues(String value)
    {
        List<Object> ret = null;
        if(value != null)
        {
            try
            {
                if(value.contains(","))
                {
                    ret = new ArrayList();
                    String[] split = value.split(",");
                    for(String string : split)
                    {
                        ret.add(unwrapSingleValue(decode(string)));
                    }
                }
                else
                {
                    ret = Collections.singletonList(unwrapSingleValue(decode(value)));
                }
            }
            catch(Exception e)
            {
                LOG.error("Could not create parameter values, reason: ", e);
            }
        }
        return ret;
    }


    protected ConditionValue wrap(String valStr, Operator operator)
    {
        return (valStr == null) ? (ConditionValue)new DefaultConditionValue(operator, new Object[0]) :
                        (ConditionValue)new DefaultConditionValue(operator, unwrapCollectionValues(valStr).toArray());
    }


    protected SavedQueryDao getSavedQueryDao()
    {
        return this.savedQueryDao;
    }


    @Required
    public void setSavedQueryDao(SavedQueryDao savedQueryDao)
    {
        this.savedQueryDao = savedQueryDao;
    }
}
