package de.hybris.platform.util;

import de.hybris.platform.core.PK;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SearchResult;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

class ItemUtilities
{
    private static final Logger LOG = Logger.getLogger(ItemUtilities.class.getName());
    static final String DEFAULT_ITEMLINK_ENCODING = "UTF-8";
    static final String ITEMLINK_PREFIX = "linkTo.";
    static final String ITEMLINK_PROTOCOLL_SPECIFIER = "item://";
    static final String ITEMLINK_TYPE_PREFIX = "type:";
    static final String ITEMLINK_ITEMATTRIBUTE_START = "(";
    static final String ITEMLINK_ITEMATTRIBUTE_END = ")";
    static final String ITEMLINK_ELEMENT_SEPARATOR = "/";
    static final String ITEMLINK_VALUE_SEPARATOR = "=";
    static final String ITEMLINK_PATTERN_ATTRIBUTE_SEPARATOR = ",";
    static final String ITEMLINK_LINK_ID = "$link_id";
    static final String ITEMLINK_REPLACEMENT_TYPE = "$replacementType";
    private static final Comparator<Item> itemComp = (Comparator<Item>)new Object();


    private Map<String, String> splitItemLink(String itemLink) throws UnsupportedEncodingException
    {
        Collection<String> combinedAttributeAndValues = new LinkedList<>();
        int attributeStart = 0;
        int attributeEnd = 0;
        int bracketLevel = 0;
        for(int i = 0; i < itemLink.length(); i++)
        {
            char currentChar = itemLink.charAt(i);
            if(isStartOfVariable("/", itemLink, currentChar, i))
            {
                if(bracketLevel == 0)
                {
                    attributeEnd = i;
                    combinedAttributeAndValues.add(itemLink.substring(attributeStart, attributeEnd));
                    attributeStart = i + "/".length();
                }
            }
            else if(isStartOfVariable("(", itemLink, currentChar, i))
            {
                bracketLevel++;
            }
            else if(isStartOfVariable(")", itemLink, currentChar, i))
            {
                bracketLevel--;
            }
        }
        combinedAttributeAndValues.add(itemLink
                        .substring(attributeStart, itemLink.length()));
        Map<String, String> separatedAttributesAndValues = new HashMap<>(combinedAttributeAndValues.size());
        for(String combinedAttributeAndValue : combinedAttributeAndValues)
        {
            if(combinedAttributeAndValue.contains("="))
            {
                String qualifier = combinedAttributeAndValue.substring(0, combinedAttributeAndValue
                                .indexOf("="));
                String value = combinedAttributeAndValue.substring(combinedAttributeAndValue
                                .indexOf("=") + "="
                                .length(), combinedAttributeAndValue.length());
                separatedAttributesAndValues.put(URLDecoder.decode(qualifier, "UTF-8"),
                                URLDecoder.decode(value, "UTF-8"));
                continue;
            }
            separatedAttributesAndValues.put(URLDecoder.decode(combinedAttributeAndValue, "UTF-8"), "");
        }
        return separatedAttributesAndValues;
    }


    private boolean isStartOfVariable(String variable, String completeText, char currentChar, int currentCharIndex)
    {
        if(currentChar == variable.charAt(0))
        {
            if(completeText.length() >= currentCharIndex + variable.length())
            {
                String possibleVariable = completeText.substring(currentCharIndex, currentCharIndex + variable.length());
                return possibleVariable.equals(variable);
            }
            return false;
        }
        return false;
    }


    private Item getItemFromLinkPart(ComposedType itemType, String itemLinkPart, SessionContext ctx) throws JaloBusinessException
    {
        Map<String, String> attributes = null;
        try
        {
            attributes = splitItemLink(itemLinkPart);
        }
        catch(UnsupportedEncodingException e)
        {
            throw new JaloBusinessException(e, "Could not decode item link " + itemLinkPart + ": " + e, 0);
        }
        if(attributes.containsKey(Item.PK))
        {
            String pk = attributes.get(Item.PK);
            if(pk != null)
            {
                Item linkedItem = JaloSession.getCurrentSession().getItem(PK.parse(pk));
                if(linkedItem != null)
                {
                    return linkedItem;
                }
                throw new JaloItemNotFoundException("Did not find any item with PK " + pk + " for item link part [" + itemLinkPart + "]", 0);
            }
            throw new JaloInvalidParameterException("Could not extract item PK from item link part [" + itemLinkPart + "]", 0);
        }
        Map<String, Object> queryParameters = new HashMap<>(attributes);
        String query = "SELECT {" + Item.PK + "} FROM {" + itemType.getCode() + "} ";
        String CONDITION_PREFIX = "WHERE ";
        boolean executeSearch = false;
        for(Map.Entry<String, String> entry : attributes.entrySet())
        {
            if("$link_id".equals(entry.getKey()) || "$replacementType".equals(entry.getKey()))
            {
                continue;
            }
            AttributeDescriptor attributeDescriptor = itemType.getAttributeDescriptorIncludingPrivate(entry.getKey());
            if(attributeDescriptor.isLocalized())
            {
                continue;
            }
            if(!attributeDescriptor.isProperty() && attributeDescriptor.getDatabaseColumn() == null)
            {
                continue;
            }
            String value = entry.getValue();
            if(!"".equals(value))
            {
                if(value.contains("("))
                {
                    Type subItemType = attributeDescriptor.getAttributeType(ctx);
                    if(subItemType instanceof ComposedType)
                    {
                        value = value.substring(value
                                        .indexOf("(") + "(".length(), value
                                        .lastIndexOf(")"));
                        Item subItem = getItemFromLinkPart((ComposedType)subItemType, value, ctx);
                        if(subItem == null)
                        {
                            throw new JaloBusinessException("Found an item link part that references a subItem in an attribute of the superItem, but could not find referenced subItem");
                        }
                        queryParameters.put(entry.getKey(), subItem);
                    }
                    else
                    {
                        throw new JaloBusinessException("Found an item link part that references a subItem in an attribute of the superItem, but the Type of the attribute is not ComposedType?!");
                    }
                }
                query = query + query + "{" + CONDITION_PREFIX + "} = ?" + (String)entry.getKey() + " ";
            }
            else
            {
                query = query + query + "{" + CONDITION_PREFIX + "} = '' ";
            }
            executeSearch = true;
            CONDITION_PREFIX = "AND ";
        }
        if(executeSearch)
        {
            SearchResult result = JaloSession.getCurrentSession().getFlexibleSearch().search(ctx, query, queryParameters,
                            Collections.singletonList(itemType.getClass()), true, true, 0, -1);
            List<Item> resultList = result.getResult();
            if(resultList.size() <= 0)
            {
                throw new JaloItemNotFoundException("Did not find any item for superItemType [" + itemType + "] and item link part [" + itemLinkPart + "]", 0);
            }
            if(resultList.size() > 1)
            {
                throw new JaloInvalidParameterException("Ambiguously Result: found " + resultList.size() + " items for superItemType [" + itemType + "] and item link part [" + itemLinkPart + "]", 0);
            }
            return resultList.iterator().next();
        }
        throw new JaloBusinessException("The parsed attributemap from the link contains no searchable attributes. Cannot execute search! superItemType [" + itemType + "], item link part [" + itemLinkPart + "], attributemap [" + attributes
                        .toString() + "]", 0);
    }


    private String[] splitItemLinkPatternAttributes(String itemLinkPattern)
    {
        Collection<String> attributes = new LinkedList<>();
        int attributeStart = 0;
        int attributeEnd = 0;
        int bracketLevel = 0;
        for(int i = 0; i < itemLinkPattern.length(); i++)
        {
            char currentChar = itemLinkPattern.charAt(i);
            if(isStartOfVariable(",", itemLinkPattern, currentChar, i))
            {
                if(bracketLevel == 0)
                {
                    attributeEnd = i;
                    attributes.add(itemLinkPattern.substring(attributeStart, attributeEnd));
                    attributeStart = i + 1;
                }
            }
            else if(isStartOfVariable("(", itemLinkPattern, currentChar, i))
            {
                bracketLevel++;
            }
            else if(isStartOfVariable(")", itemLinkPattern, currentChar, i))
            {
                bracketLevel--;
            }
        }
        attributes.add(itemLinkPattern.substring(attributeStart, itemLinkPattern
                        .length()));
        String[] result = new String[attributes.size()];
        attributes.toArray(result);
        return result;
    }


    private String generateLinkId()
    {
        return PK.createUUIDPK(42).toString();
    }


    private String getAttributeValueForItemLink(Item superItem, String itemLinkPatternPart, SessionContext ctx) throws JaloBusinessException
    {
        StringBuilder linkPart = new StringBuilder();
        String superItemAttributeQualifier = "";
        if(itemLinkPatternPart.contains("("))
        {
            superItemAttributeQualifier = itemLinkPatternPart.substring(0, itemLinkPatternPart.indexOf("("));
        }
        else
        {
            superItemAttributeQualifier = itemLinkPatternPart;
        }
        Object superItemAttributeValueObject = null;
        try
        {
            superItemAttributeValueObject = superItem.getAttribute(ctx, superItemAttributeQualifier);
        }
        catch(JaloInvalidParameterException e)
        {
            LOG.error("Could not read attribute " + superItemAttributeQualifier + " from item of type " + superItem
                            .getComposedType().getCode() + ": " + Utilities.getStackTraceAsString((Throwable)e));
            throw new JaloBusinessException(e, "Could not read attribute " + superItemAttributeQualifier + " from item of type " + superItem
                            .getComposedType().getCode() + ": " + e, 0);
        }
        catch(JaloSecurityException e)
        {
            LOG.error("Could not read attribute " + superItemAttributeQualifier + " from item of type " + superItem
                            .getComposedType()
                            .getCode() + " due to security reasons: " + Utilities.getStackTraceAsString((Throwable)e));
            throw new JaloBusinessException(e, "Could not read attribute " + superItemAttributeQualifier + " from item of type " + superItem
                            .getComposedType().getCode() + " due to security reasons: " + e, 0);
        }
        if(superItemAttributeValueObject instanceof Item)
        {
            if(itemLinkPatternPart.contains("("))
            {
                String attributeList = itemLinkPatternPart.substring(itemLinkPatternPart
                                .indexOf("(") + "("
                                .length(), itemLinkPatternPart
                                .lastIndexOf(")"));
                String[] attributes = splitItemLinkPatternAttributes(attributeList);
                linkPart.append("(");
                String LOCAL_ITEMLINK_ELEMENT_SEPARATOR = "/";
                for(int i = 0; i < attributes.length; i++)
                {
                    if(i + 1 == attributes.length)
                    {
                        LOCAL_ITEMLINK_ELEMENT_SEPARATOR = "";
                    }
                    String attributeQualifier = attributes[i].trim();
                    if(attributeQualifier.contains("("))
                    {
                        linkPart.append(attributeQualifier
                                        .substring(0, attributeQualifier.indexOf("(")));
                    }
                    else
                    {
                        linkPart.append(attributeQualifier);
                    }
                    linkPart.append("=");
                    linkPart.append(getAttributeValueForItemLink((Item)superItemAttributeValueObject, attributeQualifier, ctx));
                    linkPart.append(LOCAL_ITEMLINK_ELEMENT_SEPARATOR);
                }
                linkPart.append(")");
            }
            else
            {
                if(LOG.isInfoEnabled())
                {
                    LOG.info("The item in the attribute [" + superItemAttributeQualifier + "] that is used to create an ItemLink, is not referenced by its own specific attributes but by PK only.");
                }
                linkPart.append(((Item)superItemAttributeValueObject).getPK().getLongValueAsString());
            }
        }
        else
        {
            try
            {
                linkPart.append((superItemAttributeValueObject == null) ? "" : URLEncoder.encode(superItemAttributeValueObject
                                .toString(), "UTF-8"));
            }
            catch(UnsupportedEncodingException e)
            {
                LOG.error("Could not encode attribute " + superItemAttributeQualifier + " of item of type " + superItem
                                .getComposedType().getCode() + ": " + Utilities.getStackTraceAsString(e));
                throw new JaloBusinessException(e, "Could not encode attribute " + superItemAttributeQualifier + " of item of type " + superItem
                                .getComposedType().getCode() + ": " + e, 0);
            }
        }
        return linkPart.toString();
    }


    String createLink(SessionContext ctx, Item item) throws JaloBusinessException
    {
        if(item == null)
        {
            return "";
        }
        Class<?> itemClass = item.getClass();
        String typeName = "";
        String attributeList = null;
        while(attributeList == null)
        {
            typeName = itemClass.getSimpleName();
            if("Item".equals(typeName))
            {
                attributeList = Item.PK;
                break;
            }
            attributeList = Config.getParameter("linkTo." + typeName);
            if(attributeList == null)
            {
                itemClass = itemClass.getSuperclass();
            }
        }
        return createLink(ctx, item, typeName, attributeList);
    }


    String createLink(SessionContext ctx, Item item, String typeName, String attributeList) throws JaloBusinessException
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Create item link for item " + item);
        }
        if(item == null)
        {
            return "";
        }
        StringBuilder link = new StringBuilder();
        try
        {
            link.append("type:");
            link.append(URLEncoder.encode(typeName, "UTF-8"));
            link.append("/");
        }
        catch(UnsupportedEncodingException e)
        {
            LOG.error("Could not encode item name " + typeName + ": " + Utilities.getStackTraceAsString(e));
            throw new JaloBusinessException(e, "Could not encode item name " + typeName + ": " + e, 0);
        }
        String LOCAL_ITEMLINK_ELEMENT_SEPARATOR = "/";
        String[] attributes = splitItemLinkPatternAttributes(attributeList);
        for(int i = 0; i < attributes.length; i++)
        {
            if(i + 1 == attributes.length)
            {
                LOCAL_ITEMLINK_ELEMENT_SEPARATOR = "";
            }
            String attributeQualifier = attributes[i].trim();
            if(attributeQualifier.contains("("))
            {
                link.append(attributeQualifier.substring(0, attributeQualifier.indexOf("(")));
            }
            else
            {
                link.append(attributeQualifier);
            }
            link.append("=");
            link.append(getAttributeValueForItemLink(item, attributeQualifier, ctx));
            link.append(LOCAL_ITEMLINK_ELEMENT_SEPARATOR);
        }
        link.append("/");
        link.append("$link_id");
        link.append("=");
        link.append(generateLinkId());
        return link.toString();
    }


    String getAttributeFromLink(SessionContext ctx, String itemLink, String attributeName) throws JaloBusinessException, JaloInvalidParameterException, JaloItemNotFoundException
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("SessionContext is not specified.", 0);
        }
        if(StringUtils.isBlank(itemLink))
        {
            throw new JaloInvalidParameterException("No link is specified.", 0);
        }
        if(itemLink.startsWith("item://"))
        {
            itemLink = itemLink.substring("item://".length());
        }
        String itemTypeCode = null;
        if(itemLink.contains("type:"))
        {
            int itemTypeCodeStart = itemLink.indexOf("type:");
            int itemTypeCodeEnd = itemLink.indexOf("/", itemTypeCodeStart);
            itemTypeCode = itemLink.substring(itemTypeCodeStart + "type:".length(), itemTypeCodeEnd);
            itemLink = itemLink.replace("type:" + itemTypeCode, "");
            if(itemLink.startsWith("/"))
            {
                itemLink = itemLink.substring("/".length());
            }
        }
        else
        {
            throw new JaloInvalidParameterException("Could not extract item type from item link " + itemLink, 0);
        }
        Map<String, String> attributes = null;
        try
        {
            attributes = splitItemLink(itemLink);
        }
        catch(UnsupportedEncodingException e)
        {
            throw new JaloBusinessException(e, "Could not decode item link " + itemLink + ": " + e, 0);
        }
        if(attributes.containsKey(attributeName))
        {
            String replacementType = attributes.get(attributeName);
            return replacementType;
        }
        return null;
    }


    Item getItemFromLink(SessionContext ctx, String itemLink) throws JaloBusinessException, JaloInvalidParameterException, JaloItemNotFoundException
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("SessionContext is not specified.", 0);
        }
        if(StringUtils.isBlank(itemLink))
        {
            throw new JaloInvalidParameterException("No link is specified.", 0);
        }
        if(itemLink.startsWith("item://"))
        {
            itemLink = itemLink.substring("item://".length());
        }
        String itemTypeCode = null;
        if(itemLink.contains("type:"))
        {
            int itemTypeCodeStart = itemLink.indexOf("type:");
            int itemTypeCodeEnd = itemLink.indexOf("/", itemTypeCodeStart);
            itemTypeCode = itemLink.substring(itemTypeCodeStart + "type:".length(), itemTypeCodeEnd);
            itemLink = itemLink.replace("type:" + itemTypeCode, "");
            if(itemLink.startsWith("/"))
            {
                itemLink = itemLink.substring("/".length());
            }
        }
        else
        {
            throw new JaloInvalidParameterException("Could not extract item type from item link " + itemLink, 0);
        }
        ComposedType itemType = TypeManager.getInstance().getComposedType(itemTypeCode);
        Item linkedItem = getItemFromLinkPart(itemType, itemLink, ctx);
        if(linkedItem != null)
        {
            return linkedItem;
        }
        throw new JaloItemNotFoundException("Did not find any item for item link [" + itemLink + "]", 0);
    }


    List<? extends Item> sortItemsByPK(Collection<? extends Item> items)
    {
        if(items.isEmpty())
        {
            return Collections.EMPTY_LIST;
        }
        if(items.size() == 1)
        {
            return Collections.singletonList(items.iterator().next());
        }
        List<Item> ret = new ArrayList<>(items);
        Collections.sort(ret, itemComp);
        return ret;
    }


    <T> T getCacheBoundVersion(T value)
    {
        if(value instanceof Item)
        {
            Item cacheBound = ((Item)value).getCacheBoundItem();
            if(cacheBound != value)
            {
                return (T)cacheBound;
            }
        }
        else if(value instanceof Collection)
        {
            if(containsStaleItems((Collection<Object>)value))
            {
                Collection<Object> copyColl = copyCollectionWithCacheBoundItems((Collection<Object>)value);
                return (T)copyColl;
            }
        }
        else if(value instanceof Map)
        {
            if(containsStaleItems((Map<Object, Object>)value))
            {
                Map<Object, Object> copy = copyMapWithCacheBoundItems((Map<Object, Object>)value);
                return (T)copy;
            }
        }
        return value;
    }


    private Map<Object, Object> copyMapWithCacheBoundItems(Map<Object, Object> orig)
    {
        Map<Object, Object> copy = new LinkedHashMap<>((int)(orig.size() / 0.75F * 2.0F) + 1);
        for(Map.Entry<Object, Object> e : orig.entrySet())
        {
            Object key = e.getKey();
            if(key instanceof Item)
            {
                key = ((Item)key).getCacheBoundItem();
            }
            Object value = e.getValue();
            if(value instanceof Item)
            {
                value = ((Item)value).getCacheBoundItem();
            }
            copy.put(key, value);
        }
        return copy;
    }


    private Collection<Object> copyCollectionWithCacheBoundItems(Collection<Object> orig)
    {
        Collection<Object> copyColl = (orig instanceof java.util.Set) ? new LinkedHashSet((int)(orig.size() / 0.75F * 2.0F) + 1) : new ArrayList(orig.size());
        for(Object o : orig)
        {
            if(o instanceof Item)
            {
                copyColl.add(((Item)o).getCacheBoundItem());
                continue;
            }
            copyColl.add(o);
        }
        return copyColl;
    }


    private boolean containsStaleItems(Collection<Object> coll)
    {
        if(CollectionUtils.isNotEmpty(coll))
        {
            for(Object o : coll)
            {
                if(o instanceof Item && ((Item)o).getCacheBoundItem() != o)
                {
                    return true;
                }
            }
        }
        return false;
    }


    private boolean containsStaleItems(Map<Object, Object> map)
    {
        if(MapUtils.isNotEmpty(map))
        {
            for(Map.Entry<Object, Object> e : map.entrySet())
            {
                Object key = e.getKey();
                if(key instanceof Item && ((Item)key).getCacheBoundItem() != key)
                {
                    return true;
                }
                Object value = e.getValue();
                if(value instanceof Item && ((Item)value).getCacheBoundItem() != value)
                {
                    return true;
                }
            }
        }
        return false;
    }
}
