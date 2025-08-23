package de.hybris.platform.cockpit.services.search.impl;

import de.hybris.platform.catalog.model.ProductReferenceModel;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.search.Operator;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.comments.constants.GeneratedCommentsConstants;
import de.hybris.platform.core.GenericCondition;
import de.hybris.platform.core.GenericConditionList;
import de.hybris.platform.core.GenericQuery;
import de.hybris.platform.core.GenericSearchField;
import de.hybris.platform.core.GenericSelectField;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.RelationDescriptorModel;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.Config;
import de.hybris.platform.variants.model.VariantProductModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenericQueryParameterCreator
{
    private static final Logger LOG = LoggerFactory.getLogger(GenericQueryParameterCreator.class);
    private final GenericSearchParameterDescriptor descriptor;


    public GenericQueryParameterCreator(GenericSearchParameterDescriptor descriptor)
    {
        this.descriptor = descriptor;
    }


    public GenericSearchParameterDescriptor getDescriptor()
    {
        return this.descriptor;
    }


    public GenericCondition createCondition(Object value, Operator operator)
    {
        GenericCondition ret = null;
        if(operator != null && "between".equalsIgnoreCase(operator.getQualifier()))
        {
            ret = createRangeCondition(value);
        }
        else if(PropertyDescriptor.Multiplicity.LIST.equals(this.descriptor.getMultiplicity()) || PropertyDescriptor.Multiplicity.SET.equals(this.descriptor.getMultiplicity()))
        {
            ret = createInCondition(value);
        }
        else if(value instanceof String)
        {
            List<String> tokens = splitQuery((String)value);
            if(tokens.size() > 1)
            {
                GenericConditionList genericConditionList = GenericCondition.or(new GenericCondition[0]);
                for(String s : tokens)
                {
                    genericConditionList.addToConditionList(createSingleTokenCondition(s, operator));
                }
            }
            else
            {
                ret = createSingleTokenCondition(tokens.get(0), operator);
            }
        }
        else
        {
            ret = createSingleTokenCondition(value, operator);
        }
        return ret;
    }


    protected GenericCondition createRangeCondition(Object rawValue)
    {
        GenericConditionList genericConditionList;
        GenericCondition ret = null;
        if(rawValue instanceof Collection && ((Collection)rawValue).size() == 2)
        {
            List<Object> values = new ArrayList((Collection)rawValue);
            List<GenericCondition> conditions = new ArrayList<>();
            Object fromValue = values.get(0);
            Object toValue = values.get(1);
            GenericCondition goeCondition = null;
            GenericCondition loeCondition = null;
            if(fromValue != null && StringUtils.isNotBlank(fromValue.toString()))
            {
                goeCondition = createSingleTokenCondition(values.get(0), Operator.GREATER_OR_EQUALS);
            }
            if(toValue != null && StringUtils.isNotBlank(toValue.toString()))
            {
                loeCondition = createSingleTokenCondition(values.get(1), Operator.LESS_OR_EQUALS);
            }
            if(goeCondition != null)
            {
                conditions.add(goeCondition);
            }
            if(loeCondition != null)
            {
                conditions.add(loeCondition);
            }
            if(!conditions.isEmpty())
            {
                genericConditionList = GenericCondition.and(conditions);
            }
        }
        else
        {
            LOG.error("Range condition could not be created. Reason: Range requires two values.");
            return null;
        }
        return (GenericCondition)genericConditionList;
    }


    private List wrapValue(Object rawValue)
    {
        List<Object> value = null;
        if(!(rawValue instanceof Collection))
        {
            value = Collections.singletonList((rawValue instanceof ItemModel) ? ((ItemModel)rawValue).getPk() : rawValue);
        }
        else
        {
            value = new ArrayList();
            for(Object object : rawValue)
            {
                value.add((object instanceof ItemModel) ? ((ItemModel)object).getPk() : object);
            }
        }
        return value;
    }


    protected GenericCondition createInCondition(Object rawValue)
    {
        GenericCondition ret = null;
        Object value = wrapValue(rawValue);
        if(this.descriptor instanceof ItemAttributeSearchDescriptor && ((ItemAttributeSearchDescriptor)this.descriptor)
                        .getLastAttributeDescriptor() instanceof RelationDescriptorModel)
        {
            RelationDescriptorModel relDescr = (RelationDescriptorModel)((ItemAttributeSearchDescriptor)this.descriptor).getLastAttributeDescriptor();
            boolean source = BooleanUtils.toBoolean(relDescr.getIsSource());
            if(BooleanUtils.toBoolean(((RelationDescriptorModel)((ItemAttributeSearchDescriptor)this.descriptor)
                            .getLastAttributeDescriptor()).getRelationType().getAbstract()))
            {
                List<PK> foreignKeys = null;
                if(source)
                {
                    foreignKeys = extractForeignKeys(value, (AttributeDescriptorModel)relDescr.getRelationType().getTargetAttribute());
                }
                else
                {
                    foreignKeys = extractForeignKeys(value, (AttributeDescriptorModel)relDescr.getRelationType().getSourceAttribute());
                }
                if(!foreignKeys.isEmpty())
                {
                    ret = GenericCondition.createConditionForValueComparison(new GenericSearchField("item", "PK"), Operator.IN, foreignKeys);
                }
            }
            else
            {
                GenericQuery subQuery = new GenericQuery(relDescr.getRelationName());
                if(source)
                {
                    subQuery.addCondition(GenericCondition.createConditionForValueComparison(new GenericSearchField("target"), Operator.IN, value));
                    subQuery.addSelectField(new GenericSelectField("source", PK.class));
                }
                else
                {
                    subQuery.addCondition(GenericCondition.createConditionForValueComparison(new GenericSearchField("source"), Operator.IN, value));
                    subQuery.addSelectField(new GenericSelectField("target", PK.class));
                }
                ret = GenericCondition.createSubQueryCondition(new GenericSearchField("item", "PK"), Operator.IN, subQuery);
            }
        }
        else if(this.descriptor != null && this.descriptor
                        .getQualifier().equalsIgnoreCase("Comment.relatedItems"))
        {
            GenericQuery subQuery = new GenericQuery(GeneratedCommentsConstants.Relations.COMMENTITEMRELATION);
            subQuery.addCondition(GenericCondition.createConditionForValueComparison(new GenericSearchField("target"), Operator.IN, value));
            subQuery.addSelectField(new GenericSelectField("source", PK.class));
            ret = GenericCondition.createSubQueryCondition(new GenericSearchField("item", "PK"), Operator.IN, subQuery);
        }
        else
        {
            List<PK> foreignKeys = extractForeignKeys(value, null);
            if(!foreignKeys.isEmpty())
            {
                ret = GenericCondition.createConditionForValueComparison(new GenericSearchField("item", "PK"), Operator.IN, foreignKeys);
            }
        }
        return ret;
    }


    private List<PK> extractForeignKeys(Object value, AttributeDescriptorModel attribute)
    {
        if(value instanceof PK)
        {
            ItemModel model = (ItemModel)UISessionUtils.getCurrentSession().getModelService().get((PK)value);
            return extractForeignKeys(model, attribute);
        }
        List<PK> foreignKeys = new ArrayList<>();
        try
        {
            if(value instanceof Collection)
            {
                for(Object element : value)
                {
                    List<PK> collKeys = extractForeignKeys(element, attribute);
                    if(!collKeys.isEmpty())
                    {
                        foreignKeys.addAll(extractForeignKeys(element, attribute));
                    }
                }
            }
            else if(value instanceof ItemModel && attribute != null)
            {
                Object result = getValue((ItemModel)value, attribute);
                if(result instanceof ItemModel)
                {
                    foreignKeys.add(((ItemModel)result).getPk());
                }
            }
            else if(value instanceof VariantProductModel)
            {
                foreignKeys.add(((VariantProductModel)value).getBaseProduct().getPk());
            }
            else if(value instanceof ProductReferenceModel)
            {
                foreignKeys.add(((ProductReferenceModel)value).getSource().getPk());
            }
            else
            {
                LOG.warn("Can not create generic search condition (Reason: Type '" + value + "' currently not supported).");
            }
        }
        catch(Exception e)
        {
            LOG.error("An error occurred while extracting foreign keys.", e);
            foreignKeys.clear();
        }
        return foreignKeys;
    }


    private Object getValue(ItemModel model, AttributeDescriptorModel attribute)
    {
        ModelService modelService = UISessionUtils.getCurrentSession().getModelService();
        Object result = null;
        try
        {
            result = modelService.getAttributeValue(model, attribute.getQualifier());
        }
        catch(Exception e)
        {
            try
            {
                if(!modelService.isNew(model))
                {
                    result = modelService.toModelLayer(((Item)modelService.getSource(model)).getAttribute(attribute.getQualifier()));
                }
            }
            catch(Exception e1)
            {
                LOG.error("An error occurred while extracting foreign keys.", e1);
            }
        }
        return result;
    }


    protected GenericCondition createSingleTokenCondition(Object value, Operator operator)
    {
        return createSingleTokenCondition(getTypeCode(this.descriptor), getAttributeQuali(this.descriptor), value, operator, this.descriptor
                        .isLocalized());
    }


    protected String getTypeCode(GenericSearchParameterDescriptor descriptor)
    {
        if(descriptor instanceof ItemAttributeSearchDescriptor && ((ItemAttributeSearchDescriptor)descriptor)
                        .getAttributeDescriptors().size() > 1)
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


    private boolean isCaseSensitive()
    {
        String param = Config.getParameter("cockpit.search.simplesearch.casesensitive");
        return Boolean.parseBoolean(param);
    }


    protected GenericCondition createSingleTokenCondition(String typeCode, String attribute, Object value, Operator operator)
    {
        GenericCondition ret;
        if(operator == null)
        {
            ret = GenericCondition.equals(new GenericSearchField(typeCode, attribute), value);
        }
        else if("contains".equalsIgnoreCase(operator.getQualifier()))
        {
            ret = isCaseSensitive() ? GenericCondition.caseSensitiveLike(new GenericSearchField(typeCode, attribute), prepare((String)value, "%", "%", false)) : GenericCondition.like(new GenericSearchField(typeCode, attribute),
                            prepare((String)value, "%", "%", false));
        }
        else if("startswith".equalsIgnoreCase(operator.getQualifier()))
        {
            ret = isCaseSensitive() ? GenericCondition.caseSensitiveLike(new GenericSearchField(typeCode, attribute), prepare((String)value, null, "%", false)) : GenericCondition.like(new GenericSearchField(typeCode, attribute),
                            prepare((String)value, null, "%", false));
        }
        else if("endswith".equalsIgnoreCase(operator.getQualifier()))
        {
            ret = isCaseSensitive() ? GenericCondition.caseSensitiveLike(new GenericSearchField(typeCode, attribute), prepare((String)value, "%", null, false)) : GenericCondition.like(new GenericSearchField(typeCode, attribute),
                            prepare((String)value, "%", null, false));
        }
        else if("like".equalsIgnoreCase(operator.getQualifier()))
        {
            ret = GenericCondition.like(new GenericSearchField(typeCode, attribute), prepare((String)value, null, null, false));
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
        else
        {
            throw new IllegalArgumentException("Unsupported operator " + operator + " for descriptor " + this);
        }
        return ret;
    }


    protected GenericCondition createSingleTokenCondition(String typeCode, String attribute, Object value, Operator operator, boolean localized)
    {
        return createSingleTokenCondition(typeCode, attribute + attribute, value, operator);
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


    protected List<String> splitQuery(String query)
    {
        List<String> ret = new ArrayList<>();
        int last = 0;
        int index = 0;
        boolean block = false;
        for(int s = query.length(); index < s; index++)
        {
            char character = query.charAt(index);
            if(character == '"')
            {
                if(block)
                {
                    String tmp = query.substring(last, index).trim();
                    if(tmp.length() > 0)
                    {
                        ret.add(tmp);
                    }
                    block = false;
                    last = index + 1;
                }
                else
                {
                    block = true;
                    if(index > last)
                    {
                        String tmp = query.substring(last, index).trim();
                        if(tmp.length() > 0)
                        {
                            ret.add(tmp);
                        }
                    }
                    last = index + 1;
                }
            }
            else if(!block && (character == ' ' || character == '\t' || character == ',' || character == ';' || character == '\n' || character == '\r' || character == '\f'))
            {
                String tmp = query.substring(last, index).trim();
                if(tmp.length() > 0)
                {
                    ret.add(tmp);
                }
                last = index + 1;
            }
        }
        if(last < query.length())
        {
            String tmp = query.substring(last).trim();
            if(tmp.length() > 0)
            {
                ret.add(tmp);
            }
        }
        return ret;
    }
}
