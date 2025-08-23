package de.hybris.platform.catalog.jalo.classification;

import de.hybris.platform.catalog.constants.GeneratedCatalogConstants;
import de.hybris.platform.catalog.jalo.ProductFeature;
import de.hybris.platform.directpersistence.annotation.ForceJALO;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.Config;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;

public class ClassificationAttributeValue extends GeneratedClassificationAttributeValue
{
    private static final Logger LOG = Logger.getLogger(ClassificationAttributeValue.class.getName());


    @ForceJALO(reason = "something else")
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        Set<String> missing = new HashSet<>();
        if(((!checkMandatoryAttribute("code", allAttributes, missing) ? 1 : 0) | (
                        !checkMandatoryAttribute("systemVersion", allAttributes, missing) ? 1 : 0)) != 0)
        {
            throw new JaloInvalidParameterException("missing " + missing + " to create a new " + type.getCode(), 0);
        }
        allAttributes.setAttributeMode("code", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("systemVersion", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("externalID", Item.AttributeMode.INITIAL);
        return super.createItem(ctx, type, allAttributes);
    }


    @ForceJALO(reason = "something else")
    public void remove(SessionContext ctx) throws ConsistencyCheckException
    {
        String column;
        List<ProductFeature> list = null;
        if(Config.isOracleUsed() || Config.isHanaUsed())
        {
            column = "to_char({stringValue})";
        }
        else
        {
            column = "{stringValue}";
        }
        String query = "SELECT {" + PK + "} FROM {" + GeneratedCatalogConstants.TC.PRODUCTFEATURE + "} WHERE " + column + "=?strPK";
        Map<String, String> params = Collections.singletonMap("strPK", getPK().toString());
        List<?> resultClass = Collections.singletonList(getClass());
        do
        {
            list = FlexibleSearch.getInstance().search(query, params, resultClass, true, true, 0, 100).getResult();
            for(ProductFeature feat : list)
            {
                feat.remove(ctx);
            }
        }
        while(list.size() == 100);
        super.remove(ctx);
    }


    public String toString()
    {
        if(getImplementation() == null)
        {
            return super.toString();
        }
        return getCode() + "(" + getCode() + ")";
    }
}
