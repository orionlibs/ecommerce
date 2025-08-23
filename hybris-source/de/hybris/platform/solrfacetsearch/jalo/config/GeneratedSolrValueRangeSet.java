package de.hybris.platform.solrfacetsearch.jalo.config;

import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.solrfacetsearch.constants.GeneratedSolrfacetsearchConstants;
import de.hybris.platform.util.OneToManyHandler;
import de.hybris.platform.util.Utilities;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedSolrValueRangeSet extends GenericItem
{
    public static final String NAME = "name";
    public static final String TYPE = "type";
    public static final String QUALIFIER = "qualifier";
    public static final String FACETSEARCHCONFIGS = "facetSearchConfigs";
    protected static String SOLRFACETSEARCHCONFIG2SOLRVALUERANGESETRELATION_SRC_ORDERED = "relation.SolrFacetSearchConfig2SolrValueRangeSetRelation.source.ordered";
    protected static String SOLRFACETSEARCHCONFIG2SOLRVALUERANGESETRELATION_TGT_ORDERED = "relation.SolrFacetSearchConfig2SolrValueRangeSetRelation.target.ordered";
    protected static String SOLRFACETSEARCHCONFIG2SOLRVALUERANGESETRELATION_MARKMODIFIED = "relation.SolrFacetSearchConfig2SolrValueRangeSetRelation.markmodified";
    public static final String SOLRVALUERANGES = "solrValueRanges";
    public static final String INDEXEDPROPERTIES = "indexedProperties";
    protected static String SOLRINDEXEDPROPERTY2SOLRVALUERANGESETRELATION_SRC_ORDERED = "relation.SolrIndexedProperty2SolrValueRangeSetRelation.source.ordered";
    protected static String SOLRINDEXEDPROPERTY2SOLRVALUERANGESETRELATION_TGT_ORDERED = "relation.SolrIndexedProperty2SolrValueRangeSetRelation.target.ordered";
    protected static String SOLRINDEXEDPROPERTY2SOLRVALUERANGESETRELATION_MARKMODIFIED = "relation.SolrIndexedProperty2SolrValueRangeSetRelation.markmodified";
    protected static final OneToManyHandler<SolrValueRange> SOLRVALUERANGESHANDLER = new OneToManyHandler(GeneratedSolrfacetsearchConstants.TC.SOLRVALUERANGE, true, "solrValueRangeSet", "solrValueRangeSetPOS", true, true, 2);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("name", Item.AttributeMode.INITIAL);
        tmp.put("type", Item.AttributeMode.INITIAL);
        tmp.put("qualifier", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public List<SolrFacetSearchConfig> getFacetSearchConfigs(SessionContext ctx)
    {
        List<SolrFacetSearchConfig> items = getLinkedItems(ctx, false, GeneratedSolrfacetsearchConstants.Relations.SOLRFACETSEARCHCONFIG2SOLRVALUERANGESETRELATION, "SolrFacetSearchConfig", null,
                        Utilities.getRelationOrderingOverride(SOLRFACETSEARCHCONFIG2SOLRVALUERANGESETRELATION_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(SOLRFACETSEARCHCONFIG2SOLRVALUERANGESETRELATION_TGT_ORDERED, true));
        return items;
    }


    public List<SolrFacetSearchConfig> getFacetSearchConfigs()
    {
        return getFacetSearchConfigs(getSession().getSessionContext());
    }


    public long getFacetSearchConfigsCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, false, GeneratedSolrfacetsearchConstants.Relations.SOLRFACETSEARCHCONFIG2SOLRVALUERANGESETRELATION, "SolrFacetSearchConfig", null);
    }


    public long getFacetSearchConfigsCount()
    {
        return getFacetSearchConfigsCount(getSession().getSessionContext());
    }


    public void setFacetSearchConfigs(SessionContext ctx, List<SolrFacetSearchConfig> value)
    {
        setLinkedItems(ctx, false, GeneratedSolrfacetsearchConstants.Relations.SOLRFACETSEARCHCONFIG2SOLRVALUERANGESETRELATION, null, value,
                        Utilities.getRelationOrderingOverride(SOLRFACETSEARCHCONFIG2SOLRVALUERANGESETRELATION_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(SOLRFACETSEARCHCONFIG2SOLRVALUERANGESETRELATION_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(SOLRFACETSEARCHCONFIG2SOLRVALUERANGESETRELATION_MARKMODIFIED));
    }


    public void setFacetSearchConfigs(List<SolrFacetSearchConfig> value)
    {
        setFacetSearchConfigs(getSession().getSessionContext(), value);
    }


    public void addToFacetSearchConfigs(SessionContext ctx, SolrFacetSearchConfig value)
    {
        addLinkedItems(ctx, false, GeneratedSolrfacetsearchConstants.Relations.SOLRFACETSEARCHCONFIG2SOLRVALUERANGESETRELATION, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(SOLRFACETSEARCHCONFIG2SOLRVALUERANGESETRELATION_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(SOLRFACETSEARCHCONFIG2SOLRVALUERANGESETRELATION_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(SOLRFACETSEARCHCONFIG2SOLRVALUERANGESETRELATION_MARKMODIFIED));
    }


    public void addToFacetSearchConfigs(SolrFacetSearchConfig value)
    {
        addToFacetSearchConfigs(getSession().getSessionContext(), value);
    }


    public void removeFromFacetSearchConfigs(SessionContext ctx, SolrFacetSearchConfig value)
    {
        removeLinkedItems(ctx, false, GeneratedSolrfacetsearchConstants.Relations.SOLRFACETSEARCHCONFIG2SOLRVALUERANGESETRELATION, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(SOLRFACETSEARCHCONFIG2SOLRVALUERANGESETRELATION_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(SOLRFACETSEARCHCONFIG2SOLRVALUERANGESETRELATION_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(SOLRFACETSEARCHCONFIG2SOLRVALUERANGESETRELATION_MARKMODIFIED));
    }


    public void removeFromFacetSearchConfigs(SolrFacetSearchConfig value)
    {
        removeFromFacetSearchConfigs(getSession().getSessionContext(), value);
    }


    public List<SolrIndexedProperty> getIndexedProperties(SessionContext ctx)
    {
        List<SolrIndexedProperty> items = getLinkedItems(ctx, false, GeneratedSolrfacetsearchConstants.Relations.SOLRINDEXEDPROPERTY2SOLRVALUERANGESETRELATION, "SolrIndexedProperty", null,
                        Utilities.getRelationOrderingOverride(SOLRINDEXEDPROPERTY2SOLRVALUERANGESETRELATION_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(SOLRINDEXEDPROPERTY2SOLRVALUERANGESETRELATION_TGT_ORDERED, true));
        return items;
    }


    public List<SolrIndexedProperty> getIndexedProperties()
    {
        return getIndexedProperties(getSession().getSessionContext());
    }


    public long getIndexedPropertiesCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, false, GeneratedSolrfacetsearchConstants.Relations.SOLRINDEXEDPROPERTY2SOLRVALUERANGESETRELATION, "SolrIndexedProperty", null);
    }


    public long getIndexedPropertiesCount()
    {
        return getIndexedPropertiesCount(getSession().getSessionContext());
    }


    public void setIndexedProperties(SessionContext ctx, List<SolrIndexedProperty> value)
    {
        setLinkedItems(ctx, false, GeneratedSolrfacetsearchConstants.Relations.SOLRINDEXEDPROPERTY2SOLRVALUERANGESETRELATION, null, value,
                        Utilities.getRelationOrderingOverride(SOLRINDEXEDPROPERTY2SOLRVALUERANGESETRELATION_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(SOLRINDEXEDPROPERTY2SOLRVALUERANGESETRELATION_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(SOLRINDEXEDPROPERTY2SOLRVALUERANGESETRELATION_MARKMODIFIED));
    }


    public void setIndexedProperties(List<SolrIndexedProperty> value)
    {
        setIndexedProperties(getSession().getSessionContext(), value);
    }


    public void addToIndexedProperties(SessionContext ctx, SolrIndexedProperty value)
    {
        addLinkedItems(ctx, false, GeneratedSolrfacetsearchConstants.Relations.SOLRINDEXEDPROPERTY2SOLRVALUERANGESETRELATION, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(SOLRINDEXEDPROPERTY2SOLRVALUERANGESETRELATION_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(SOLRINDEXEDPROPERTY2SOLRVALUERANGESETRELATION_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(SOLRINDEXEDPROPERTY2SOLRVALUERANGESETRELATION_MARKMODIFIED));
    }


    public void addToIndexedProperties(SolrIndexedProperty value)
    {
        addToIndexedProperties(getSession().getSessionContext(), value);
    }


    public void removeFromIndexedProperties(SessionContext ctx, SolrIndexedProperty value)
    {
        removeLinkedItems(ctx, false, GeneratedSolrfacetsearchConstants.Relations.SOLRINDEXEDPROPERTY2SOLRVALUERANGESETRELATION, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(SOLRINDEXEDPROPERTY2SOLRVALUERANGESETRELATION_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(SOLRINDEXEDPROPERTY2SOLRVALUERANGESETRELATION_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(SOLRINDEXEDPROPERTY2SOLRVALUERANGESETRELATION_MARKMODIFIED));
    }


    public void removeFromIndexedProperties(SolrIndexedProperty value)
    {
        removeFromIndexedProperties(getSession().getSessionContext(), value);
    }


    @Deprecated(since = "2105", forRemoval = true)
    public boolean isMarkModifiedDisabled(Item referencedItem)
    {
        ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("SolrFacetSearchConfig");
        if(relationSecondEnd0.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(SOLRFACETSEARCHCONFIG2SOLRVALUERANGESETRELATION_MARKMODIFIED);
        }
        ComposedType relationSecondEnd1 = TypeManager.getInstance().getComposedType("SolrIndexedProperty");
        if(relationSecondEnd1.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(SOLRINDEXEDPROPERTY2SOLRVALUERANGESETRELATION_MARKMODIFIED);
        }
        return true;
    }


    public String getName(SessionContext ctx)
    {
        return (String)getProperty(ctx, "name");
    }


    public String getName()
    {
        return getName(getSession().getSessionContext());
    }


    public void setName(SessionContext ctx, String value)
    {
        setProperty(ctx, "name", value);
    }


    public void setName(String value)
    {
        setName(getSession().getSessionContext(), value);
    }


    public String getQualifier(SessionContext ctx)
    {
        return (String)getProperty(ctx, "qualifier");
    }


    public String getQualifier()
    {
        return getQualifier(getSession().getSessionContext());
    }


    public void setQualifier(SessionContext ctx, String value)
    {
        setProperty(ctx, "qualifier", value);
    }


    public void setQualifier(String value)
    {
        setQualifier(getSession().getSessionContext(), value);
    }


    public List<SolrValueRange> getSolrValueRanges(SessionContext ctx)
    {
        return (List<SolrValueRange>)SOLRVALUERANGESHANDLER.getValues(ctx, (Item)this);
    }


    public List<SolrValueRange> getSolrValueRanges()
    {
        return getSolrValueRanges(getSession().getSessionContext());
    }


    public void setSolrValueRanges(SessionContext ctx, List<SolrValueRange> value)
    {
        SOLRVALUERANGESHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setSolrValueRanges(List<SolrValueRange> value)
    {
        setSolrValueRanges(getSession().getSessionContext(), value);
    }


    public void addToSolrValueRanges(SessionContext ctx, SolrValueRange value)
    {
        SOLRVALUERANGESHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToSolrValueRanges(SolrValueRange value)
    {
        addToSolrValueRanges(getSession().getSessionContext(), value);
    }


    public void removeFromSolrValueRanges(SessionContext ctx, SolrValueRange value)
    {
        SOLRVALUERANGESHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromSolrValueRanges(SolrValueRange value)
    {
        removeFromSolrValueRanges(getSession().getSessionContext(), value);
    }


    public String getType(SessionContext ctx)
    {
        return (String)getProperty(ctx, "type");
    }


    public String getType()
    {
        return getType(getSession().getSessionContext());
    }


    public void setType(SessionContext ctx, String value)
    {
        setProperty(ctx, "type", value);
    }


    public void setType(String value)
    {
        setType(getSession().getSessionContext(), value);
    }
}
