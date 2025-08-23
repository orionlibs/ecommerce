package de.hybris.platform.cockpit.services.search.impl;

import de.hybris.platform.cockpit.model.advancedsearch.ConditionValue;
import de.hybris.platform.cockpit.model.search.Operator;
import de.hybris.platform.cockpit.model.search.SearchParameterDescriptor;
import de.hybris.platform.cockpit.model.search.SearchParameterValue;
import de.hybris.platform.cockpit.services.search.ConditionTranslatorContext;
import de.hybris.platform.cockpit.services.search.GenericQueryConditionTranslator;
import de.hybris.platform.cockpit.util.TypeTools;
import de.hybris.platform.core.GenericCondition;
import de.hybris.platform.core.GenericConditionList;
import de.hybris.platform.core.GenericQuery;
import de.hybris.platform.core.GenericSearchField;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.util.Config;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultGenericQueryConditionTranslator implements GenericQueryConditionTranslator
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultGenericQueryConditionTranslator.class);
    private static final Object NULL_VALUE = new String();


    public boolean isCaseInsensitiveLikesSetting()
    {
        String param = Config.getParameter("cockpit.search.advancedsearch.casesensitive");
        return !Boolean.parseBoolean(param);
    }


    public GenericCondition translate(SearchParameterValue paramValue, ConditionTranslatorContext ctx)
    {
        GenericCondition ret = null;
        SearchParameterDescriptor paramDescr = paramValue.getParameterDescriptor();
        Object value = paramValue.getValue();
        if(!isEmpty(value))
        {
            if(ctx instanceof GenericQuerySearchProvider.GenericConditionTranslatorContext)
            {
                if((((GenericQuerySearchProvider.GenericConditionTranslatorContext)ctx).isWrapToItemNeeded() && value instanceof de.hybris.platform.core.model.ItemModel) || value instanceof de.hybris.platform.core.HybrisEnumValue)
                {
                    value = TypeTools.getModelService().getSource(value);
                }
                if(paramDescr instanceof GenericSearchParameterDescriptor)
                {
                    if(value instanceof ConditionValue)
                    {
                        ret = createCondition(((GenericQuerySearchProvider.GenericConditionTranslatorContext)ctx).getGenericQuery(), (ConditionValue)value, paramDescr);
                    }
                    else
                    {
                        try
                        {
                            ret = ((GenericSearchParameterDescriptor)paramDescr).createCondition(((GenericQuerySearchProvider.GenericConditionTranslatorContext)ctx)
                                            .getGenericQuery(), value, paramValue.getOperator());
                        }
                        catch(Exception e)
                        {
                            LOG.error("Could not translate condition, reason: ", e);
                        }
                    }
                }
            }
            else
            {
                LOG.error("Could not translate condition, context not a " + GenericQuerySearchProvider.GenericConditionTranslatorContext.class.getSimpleName());
            }
        }
        return ret;
    }


    protected GenericCondition createCondition(GenericQuery query, ConditionValue conditionValue, SearchParameterDescriptor descriptor)
    {
        GenericCondition ret = null;
        Operator operator = conditionValue.getOperator();
        String typeCode = getTypeCode((GenericSearchParameterDescriptor)descriptor);
        String attribute = getAttributeQuali((GenericSearchParameterDescriptor)descriptor);
        preprocess(query, conditionValue, (GenericSearchParameterDescriptor)descriptor);
        Object value = null;
        Object secondValue = null;
        try
        {
            Iterator<Object> iterator = conditionValue.getValues().iterator();
            if(iterator.hasNext())
            {
                value = iterator.next();
            }
            if(iterator.hasNext())
            {
                secondValue = iterator.next();
            }
        }
        catch(Exception e)
        {
            LOG.error("Could not resolve condition value.", e);
        }
        if(value == null)
        {
            value = NULL_VALUE;
        }
        if(operator == null)
        {
            ret = GenericCondition.equals(new GenericSearchField(typeCode, attribute), value);
        }
        else if("contains".equalsIgnoreCase(operator.getQualifier()))
        {
            ret = isCaseInsensitiveLikesSetting() ? GenericCondition.like(new GenericSearchField(typeCode, attribute), prepare((String)value, "%", "%", false)) : GenericCondition.caseSensitiveLike(new GenericSearchField(typeCode, attribute),
                            prepare((String)value, "%", "%", false));
        }
        else if("startswith".equalsIgnoreCase(operator.getQualifier()))
        {
            ret = isCaseInsensitiveLikesSetting() ? GenericCondition.like(new GenericSearchField(typeCode, attribute), prepare((String)value, null, "%", false)) : GenericCondition.caseSensitiveLike(new GenericSearchField(typeCode, attribute),
                            prepare((String)value, null, "%", false));
        }
        else if("endswith".equalsIgnoreCase(operator.getQualifier()))
        {
            ret = isCaseInsensitiveLikesSetting() ? GenericCondition.like(new GenericSearchField(typeCode, attribute), prepare((String)value, "%", null, false)) : GenericCondition.caseSensitiveLike(new GenericSearchField(typeCode, attribute),
                            prepare((String)value, "%", null, false));
        }
        else if("like".equalsIgnoreCase(operator.getQualifier()))
        {
            ret = isCaseInsensitiveLikesSetting() ? GenericCondition.like(new GenericSearchField(typeCode, attribute), prepare((String)value, null, null, false)) : GenericCondition.caseSensitiveLike(new GenericSearchField(typeCode, attribute),
                            prepare((String)value, null, null, false));
        }
        else if("equals".equalsIgnoreCase(operator.getQualifier()))
        {
            ret = GenericCondition.equals(new GenericSearchField(typeCode, attribute), value);
        }
        else if("greater".equalsIgnoreCase(operator.getQualifier()))
        {
            ret = GenericCondition.greater(new GenericSearchField(typeCode, attribute), value);
        }
        else if("less".equalsIgnoreCase(operator.getQualifier()))
        {
            ret = GenericCondition.less(new GenericSearchField(typeCode, attribute), value);
        }
        else if("greaterOrEquals".equalsIgnoreCase(operator.getQualifier()))
        {
            ret = GenericCondition.greaterOrEqual(new GenericSearchField(typeCode, attribute), value);
        }
        else if("lessOrEquals".equalsIgnoreCase(operator.getQualifier()))
        {
            ret = GenericCondition.lessOrEqual(new GenericSearchField(typeCode, attribute), value);
        }
        if(value == NULL_VALUE)
        {
            if(ret == null)
            {
                value = null;
            }
            else
            {
                return null;
            }
        }
        if(ret == null)
        {
            if("isEmpty".equalsIgnoreCase(operator.getQualifier()))
            {
                GenericSearchField field = new GenericSearchField(typeCode, attribute);
                GenericConditionList genericConditionList = GenericCondition.or(new GenericCondition[] {GenericCondition.createIsNullCondition(field), GenericCondition.equals(field, "")});
            }
            else if("between".equalsIgnoreCase(operator.getQualifier()))
            {
                List<GenericCondition> conditions = new ArrayList<>();
                GenericSearchField field = new GenericSearchField(typeCode, attribute);
                if(value != null)
                {
                    conditions.add(GenericCondition.greaterOrEqual(field, value));
                }
                if(secondValue != null)
                {
                    conditions.add(GenericCondition.lessOrEqual(field, secondValue));
                }
                if(conditions.size() > 1)
                {
                    GenericConditionList genericConditionList = GenericCondition.and(conditions);
                }
                else if(conditions.size() == 1)
                {
                    ret = conditions.get(0);
                }
            }
            else
            {
                throw new IllegalArgumentException("Unsupported operator " + operator + " for descriptor " + this);
            }
        }
        return ret;
    }


    protected void preprocess(GenericQuery query, ConditionValue conditionValue, GenericSearchParameterDescriptor descriptor)
    {
        if(!isSingleAttribute(descriptor))
        {
            List<AttributeDescriptorModel> attributeDescriptors = ((ItemAttributeSearchDescriptor)descriptor).getAttributeDescriptors();
            int index = 0;
            String prevTypeCode = null;
            for(AttributeDescriptorModel ad : attributeDescriptors)
            {
                if(index++ < attributeDescriptors.size() - 1 && ad.getAttributeType() instanceof ComposedTypeModel)
                {
                    ComposedTypeModel newType = (ComposedTypeModel)ad.getAttributeType();
                    query.addOuterJoin(newType.getCode(), GenericCondition.createJoinCondition(new GenericSearchField(prevTypeCode, ad
                                    .getQualifier()), new GenericSearchField(newType.getCode(), "pk")));
                    prevTypeCode = newType.getCode();
                }
            }
        }
    }


    protected String getTypeCode(GenericSearchParameterDescriptor descriptor)
    {
        if(!isSingleAttribute(descriptor))
        {
            List<AttributeDescriptorModel> descriptors = ((ItemAttributeSearchDescriptor)descriptor).getAttributeDescriptors();
            return ((AttributeDescriptorModel)descriptors.get(descriptors.size() - 1)).getEnclosingType().getCode();
        }
        return null;
    }


    protected String getAttributeQuali(GenericSearchParameterDescriptor descriptor)
    {
        if(descriptor instanceof ItemAttributeSearchDescriptor)
        {
            return ((ItemAttributeSearchDescriptor)descriptor).getLastAttributeDescriptor().getQualifier();
        }
        return descriptor.getAttributeQualifier();
    }


    protected boolean isSingleAttribute(GenericSearchParameterDescriptor descriptor)
    {
        return (!(descriptor instanceof ItemAttributeSearchDescriptor) || ((ItemAttributeSearchDescriptor)descriptor)
                        .getAttributeDescriptors().size() <= 1);
    }


    protected String prepare(String token, String prefix, String postfix, boolean exact)
    {
        String tmp = exact ? token.trim() : token.trim().replace('*', '%').replace('?', '_');
        if(prefix != null && !tmp.startsWith(prefix))
        {
            tmp = prefix + prefix;
        }
        if(postfix != null && !tmp.endsWith(postfix))
        {
            tmp = tmp + tmp;
        }
        return tmp;
    }


    protected boolean isEmpty(Object value)
    {
        return (value == null || "".equals(value) || (value instanceof Collection && ((Collection)value).isEmpty()));
    }
}
