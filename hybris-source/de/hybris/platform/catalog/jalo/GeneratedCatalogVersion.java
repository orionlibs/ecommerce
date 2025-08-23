package de.hybris.platform.catalog.jalo;

import de.hybris.platform.catalog.constants.GeneratedCatalogConstants;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Country;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.security.Principal;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import de.hybris.platform.util.OneToManyHandler;
import de.hybris.platform.util.Utilities;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedCatalogVersion extends GenericItem
{
    public static final String ACTIVE = "active";
    public static final String VERSION = "version";
    public static final String MIMEROOTDIRECTORY = "mimeRootDirectory";
    public static final String GENERATIONDATE = "generationDate";
    public static final String DEFAULTCURRENCY = "defaultCurrency";
    public static final String INCLFREIGHT = "inclFreight";
    public static final String INCLPACKING = "inclPacking";
    public static final String INCLASSURANCE = "inclAssurance";
    public static final String INCLDUTY = "inclDuty";
    public static final String TERRITORIES = "territories";
    public static final String LANGUAGES = "languages";
    public static final String GENERATORINFO = "generatorInfo";
    public static final String CATEGORYSYSTEMID = "categorySystemID";
    public static final String CATEGORYSYSTEMNAME = "categorySystemName";
    public static final String CATEGORYSYSTEMDESCRIPTION = "categorySystemDescription";
    public static final String PREVIOUSUPDATEVERSION = "previousUpdateVersion";
    public static final String CATALOG = "catalog";
    public static final String SYNCHRONIZATIONS = "synchronizations";
    public static final String INCOMINGSYNCHRONIZATIONS = "incomingSynchronizations";
    public static final String AGREEMENTS = "agreements";
    public static final String WRITEPRINCIPALS = "writePrincipals";
    protected static String PRINCIPAL2WRITEABLECATALOGVERSIONRELATION_SRC_ORDERED = "relation.Principal2WriteableCatalogVersionRelation.source.ordered";
    protected static String PRINCIPAL2WRITEABLECATALOGVERSIONRELATION_TGT_ORDERED = "relation.Principal2WriteableCatalogVersionRelation.target.ordered";
    protected static String PRINCIPAL2WRITEABLECATALOGVERSIONRELATION_MARKMODIFIED = "relation.Principal2WriteableCatalogVersionRelation.markmodified";
    public static final String READPRINCIPALS = "readPrincipals";
    protected static String PRINCIPAL2READABLECATALOGVERSIONRELATION_SRC_ORDERED = "relation.Principal2ReadableCatalogVersionRelation.source.ordered";
    protected static String PRINCIPAL2READABLECATALOGVERSIONRELATION_TGT_ORDERED = "relation.Principal2ReadableCatalogVersionRelation.target.ordered";
    protected static String PRINCIPAL2READABLECATALOGVERSIONRELATION_MARKMODIFIED = "relation.Principal2ReadableCatalogVersionRelation.markmodified";
    protected static final BidirectionalOneToManyHandler<GeneratedCatalogVersion> CATALOGHANDLER = new BidirectionalOneToManyHandler(GeneratedCatalogConstants.TC.CATALOGVERSION, false, "catalog", null, false, true, 1);
    protected static final OneToManyHandler<SyncItemJob> SYNCHRONIZATIONSHANDLER = new OneToManyHandler(GeneratedCatalogConstants.TC.SYNCITEMJOB, true, "sourceVersion", null, false, true, 2);
    protected static final OneToManyHandler<SyncItemJob> INCOMINGSYNCHRONIZATIONSHANDLER = new OneToManyHandler(GeneratedCatalogConstants.TC.SYNCITEMJOB, false, "targetVersion", null, false, true, 2);
    protected static final OneToManyHandler<Agreement> AGREEMENTSHANDLER = new OneToManyHandler(GeneratedCatalogConstants.TC.AGREEMENT, false, "catalogVersion", null, false, true, 2);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("active", Item.AttributeMode.INITIAL);
        tmp.put("version", Item.AttributeMode.INITIAL);
        tmp.put("mimeRootDirectory", Item.AttributeMode.INITIAL);
        tmp.put("generationDate", Item.AttributeMode.INITIAL);
        tmp.put("defaultCurrency", Item.AttributeMode.INITIAL);
        tmp.put("inclFreight", Item.AttributeMode.INITIAL);
        tmp.put("inclPacking", Item.AttributeMode.INITIAL);
        tmp.put("inclAssurance", Item.AttributeMode.INITIAL);
        tmp.put("inclDuty", Item.AttributeMode.INITIAL);
        tmp.put("territories", Item.AttributeMode.INITIAL);
        tmp.put("languages", Item.AttributeMode.INITIAL);
        tmp.put("generatorInfo", Item.AttributeMode.INITIAL);
        tmp.put("categorySystemID", Item.AttributeMode.INITIAL);
        tmp.put("categorySystemName", Item.AttributeMode.INITIAL);
        tmp.put("categorySystemDescription", Item.AttributeMode.INITIAL);
        tmp.put("previousUpdateVersion", Item.AttributeMode.INITIAL);
        tmp.put("catalog", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Boolean isActive(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "active");
    }


    public Boolean isActive()
    {
        return isActive(getSession().getSessionContext());
    }


    public boolean isActiveAsPrimitive(SessionContext ctx)
    {
        Boolean value = isActive(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isActiveAsPrimitive()
    {
        return isActiveAsPrimitive(getSession().getSessionContext());
    }


    public void setActive(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "active", value);
    }


    public void setActive(Boolean value)
    {
        setActive(getSession().getSessionContext(), value);
    }


    public void setActive(SessionContext ctx, boolean value)
    {
        setActive(ctx, Boolean.valueOf(value));
    }


    public void setActive(boolean value)
    {
        setActive(getSession().getSessionContext(), value);
    }


    public List<Agreement> getAgreements(SessionContext ctx)
    {
        return (List<Agreement>)AGREEMENTSHANDLER.getValues(ctx, (Item)this);
    }


    public List<Agreement> getAgreements()
    {
        return getAgreements(getSession().getSessionContext());
    }


    public void setAgreements(SessionContext ctx, List<Agreement> value)
    {
        AGREEMENTSHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setAgreements(List<Agreement> value)
    {
        setAgreements(getSession().getSessionContext(), value);
    }


    public void addToAgreements(SessionContext ctx, Agreement value)
    {
        AGREEMENTSHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToAgreements(Agreement value)
    {
        addToAgreements(getSession().getSessionContext(), value);
    }


    public void removeFromAgreements(SessionContext ctx, Agreement value)
    {
        AGREEMENTSHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromAgreements(Agreement value)
    {
        removeFromAgreements(getSession().getSessionContext(), value);
    }


    public Catalog getCatalog(SessionContext ctx)
    {
        return (Catalog)getProperty(ctx, "catalog");
    }


    public Catalog getCatalog()
    {
        return getCatalog(getSession().getSessionContext());
    }


    protected void setCatalog(SessionContext ctx, Catalog value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'catalog' is not changeable", 0);
        }
        CATALOGHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    protected void setCatalog(Catalog value)
    {
        setCatalog(getSession().getSessionContext(), value);
    }


    public String getCategorySystemDescription(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedCatalogVersion.getCategorySystemDescription requires a session language", 0);
        }
        return (String)getLocalizedProperty(ctx, "categorySystemDescription");
    }


    public String getCategorySystemDescription()
    {
        return getCategorySystemDescription(getSession().getSessionContext());
    }


    public Map<Language, String> getAllCategorySystemDescription(SessionContext ctx)
    {
        return getAllLocalizedProperties(ctx, "categorySystemDescription", C2LManager.getInstance().getAllLanguages());
    }


    public Map<Language, String> getAllCategorySystemDescription()
    {
        return getAllCategorySystemDescription(getSession().getSessionContext());
    }


    public void setCategorySystemDescription(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedCatalogVersion.setCategorySystemDescription requires a session language", 0);
        }
        setLocalizedProperty(ctx, "categorySystemDescription", value);
    }


    public void setCategorySystemDescription(String value)
    {
        setCategorySystemDescription(getSession().getSessionContext(), value);
    }


    public void setAllCategorySystemDescription(SessionContext ctx, Map<Language, String> value)
    {
        setAllLocalizedProperties(ctx, "categorySystemDescription", value);
    }


    public void setAllCategorySystemDescription(Map<Language, String> value)
    {
        setAllCategorySystemDescription(getSession().getSessionContext(), value);
    }


    public String getCategorySystemID(SessionContext ctx)
    {
        return (String)getProperty(ctx, "categorySystemID");
    }


    public String getCategorySystemID()
    {
        return getCategorySystemID(getSession().getSessionContext());
    }


    public void setCategorySystemID(SessionContext ctx, String value)
    {
        setProperty(ctx, "categorySystemID", value);
    }


    public void setCategorySystemID(String value)
    {
        setCategorySystemID(getSession().getSessionContext(), value);
    }


    public String getCategorySystemName(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedCatalogVersion.getCategorySystemName requires a session language", 0);
        }
        return (String)getLocalizedProperty(ctx, "categorySystemName");
    }


    public String getCategorySystemName()
    {
        return getCategorySystemName(getSession().getSessionContext());
    }


    public Map<Language, String> getAllCategorySystemName(SessionContext ctx)
    {
        return getAllLocalizedProperties(ctx, "categorySystemName", C2LManager.getInstance().getAllLanguages());
    }


    public Map<Language, String> getAllCategorySystemName()
    {
        return getAllCategorySystemName(getSession().getSessionContext());
    }


    public void setCategorySystemName(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedCatalogVersion.setCategorySystemName requires a session language", 0);
        }
        setLocalizedProperty(ctx, "categorySystemName", value);
    }


    public void setCategorySystemName(String value)
    {
        setCategorySystemName(getSession().getSessionContext(), value);
    }


    public void setAllCategorySystemName(SessionContext ctx, Map<Language, String> value)
    {
        setAllLocalizedProperties(ctx, "categorySystemName", value);
    }


    public void setAllCategorySystemName(Map<Language, String> value)
    {
        setAllCategorySystemName(getSession().getSessionContext(), value);
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        CATALOGHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public Currency getDefaultCurrency(SessionContext ctx)
    {
        return (Currency)getProperty(ctx, "defaultCurrency");
    }


    public Currency getDefaultCurrency()
    {
        return getDefaultCurrency(getSession().getSessionContext());
    }


    public void setDefaultCurrency(SessionContext ctx, Currency value)
    {
        setProperty(ctx, "defaultCurrency", value);
    }


    public void setDefaultCurrency(Currency value)
    {
        setDefaultCurrency(getSession().getSessionContext(), value);
    }


    public Date getGenerationDate(SessionContext ctx)
    {
        return (Date)getProperty(ctx, "generationDate");
    }


    public Date getGenerationDate()
    {
        return getGenerationDate(getSession().getSessionContext());
    }


    public void setGenerationDate(SessionContext ctx, Date value)
    {
        setProperty(ctx, "generationDate", value);
    }


    public void setGenerationDate(Date value)
    {
        setGenerationDate(getSession().getSessionContext(), value);
    }


    public String getGeneratorInfo(SessionContext ctx)
    {
        return (String)getProperty(ctx, "generatorInfo");
    }


    public String getGeneratorInfo()
    {
        return getGeneratorInfo(getSession().getSessionContext());
    }


    public void setGeneratorInfo(SessionContext ctx, String value)
    {
        setProperty(ctx, "generatorInfo", value);
    }


    public void setGeneratorInfo(String value)
    {
        setGeneratorInfo(getSession().getSessionContext(), value);
    }


    public Boolean isInclAssurance(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "inclAssurance");
    }


    public Boolean isInclAssurance()
    {
        return isInclAssurance(getSession().getSessionContext());
    }


    public boolean isInclAssuranceAsPrimitive(SessionContext ctx)
    {
        Boolean value = isInclAssurance(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isInclAssuranceAsPrimitive()
    {
        return isInclAssuranceAsPrimitive(getSession().getSessionContext());
    }


    public void setInclAssurance(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "inclAssurance", value);
    }


    public void setInclAssurance(Boolean value)
    {
        setInclAssurance(getSession().getSessionContext(), value);
    }


    public void setInclAssurance(SessionContext ctx, boolean value)
    {
        setInclAssurance(ctx, Boolean.valueOf(value));
    }


    public void setInclAssurance(boolean value)
    {
        setInclAssurance(getSession().getSessionContext(), value);
    }


    public Boolean isInclDuty(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "inclDuty");
    }


    public Boolean isInclDuty()
    {
        return isInclDuty(getSession().getSessionContext());
    }


    public boolean isInclDutyAsPrimitive(SessionContext ctx)
    {
        Boolean value = isInclDuty(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isInclDutyAsPrimitive()
    {
        return isInclDutyAsPrimitive(getSession().getSessionContext());
    }


    public void setInclDuty(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "inclDuty", value);
    }


    public void setInclDuty(Boolean value)
    {
        setInclDuty(getSession().getSessionContext(), value);
    }


    public void setInclDuty(SessionContext ctx, boolean value)
    {
        setInclDuty(ctx, Boolean.valueOf(value));
    }


    public void setInclDuty(boolean value)
    {
        setInclDuty(getSession().getSessionContext(), value);
    }


    public Boolean isInclFreight(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "inclFreight");
    }


    public Boolean isInclFreight()
    {
        return isInclFreight(getSession().getSessionContext());
    }


    public boolean isInclFreightAsPrimitive(SessionContext ctx)
    {
        Boolean value = isInclFreight(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isInclFreightAsPrimitive()
    {
        return isInclFreightAsPrimitive(getSession().getSessionContext());
    }


    public void setInclFreight(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "inclFreight", value);
    }


    public void setInclFreight(Boolean value)
    {
        setInclFreight(getSession().getSessionContext(), value);
    }


    public void setInclFreight(SessionContext ctx, boolean value)
    {
        setInclFreight(ctx, Boolean.valueOf(value));
    }


    public void setInclFreight(boolean value)
    {
        setInclFreight(getSession().getSessionContext(), value);
    }


    public Boolean isInclPacking(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "inclPacking");
    }


    public Boolean isInclPacking()
    {
        return isInclPacking(getSession().getSessionContext());
    }


    public boolean isInclPackingAsPrimitive(SessionContext ctx)
    {
        Boolean value = isInclPacking(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isInclPackingAsPrimitive()
    {
        return isInclPackingAsPrimitive(getSession().getSessionContext());
    }


    public void setInclPacking(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "inclPacking", value);
    }


    public void setInclPacking(Boolean value)
    {
        setInclPacking(getSession().getSessionContext(), value);
    }


    public void setInclPacking(SessionContext ctx, boolean value)
    {
        setInclPacking(ctx, Boolean.valueOf(value));
    }


    public void setInclPacking(boolean value)
    {
        setInclPacking(getSession().getSessionContext(), value);
    }


    public List<SyncItemJob> getIncomingSynchronizations(SessionContext ctx)
    {
        return (List<SyncItemJob>)INCOMINGSYNCHRONIZATIONSHANDLER.getValues(ctx, (Item)this);
    }


    public List<SyncItemJob> getIncomingSynchronizations()
    {
        return getIncomingSynchronizations(getSession().getSessionContext());
    }


    @Deprecated(since = "2105", forRemoval = true)
    public boolean isMarkModifiedDisabled(Item referencedItem)
    {
        ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("Principal");
        if(relationSecondEnd0.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(PRINCIPAL2WRITEABLECATALOGVERSIONRELATION_MARKMODIFIED);
        }
        ComposedType relationSecondEnd1 = TypeManager.getInstance().getComposedType("Principal");
        if(relationSecondEnd1.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(PRINCIPAL2READABLECATALOGVERSIONRELATION_MARKMODIFIED);
        }
        return true;
    }


    public Collection<Language> getLanguages(SessionContext ctx)
    {
        Collection<Language> coll = (Collection<Language>)getProperty(ctx, "languages");
        return (coll != null) ? coll : Collections.EMPTY_LIST;
    }


    public Collection<Language> getLanguages()
    {
        return getLanguages(getSession().getSessionContext());
    }


    public void setLanguages(SessionContext ctx, Collection<Language> value)
    {
        setProperty(ctx, "languages", (value == null || !value.isEmpty()) ? value : null);
    }


    public void setLanguages(Collection<Language> value)
    {
        setLanguages(getSession().getSessionContext(), value);
    }


    public String getMimeRootDirectory(SessionContext ctx)
    {
        return (String)getProperty(ctx, "mimeRootDirectory");
    }


    public String getMimeRootDirectory()
    {
        return getMimeRootDirectory(getSession().getSessionContext());
    }


    public void setMimeRootDirectory(SessionContext ctx, String value)
    {
        setProperty(ctx, "mimeRootDirectory", value);
    }


    public void setMimeRootDirectory(String value)
    {
        setMimeRootDirectory(getSession().getSessionContext(), value);
    }


    public Integer getPreviousUpdateVersion(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "previousUpdateVersion");
    }


    public Integer getPreviousUpdateVersion()
    {
        return getPreviousUpdateVersion(getSession().getSessionContext());
    }


    public int getPreviousUpdateVersionAsPrimitive(SessionContext ctx)
    {
        Integer value = getPreviousUpdateVersion(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getPreviousUpdateVersionAsPrimitive()
    {
        return getPreviousUpdateVersionAsPrimitive(getSession().getSessionContext());
    }


    public void setPreviousUpdateVersion(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "previousUpdateVersion", value);
    }


    public void setPreviousUpdateVersion(Integer value)
    {
        setPreviousUpdateVersion(getSession().getSessionContext(), value);
    }


    public void setPreviousUpdateVersion(SessionContext ctx, int value)
    {
        setPreviousUpdateVersion(ctx, Integer.valueOf(value));
    }


    public void setPreviousUpdateVersion(int value)
    {
        setPreviousUpdateVersion(getSession().getSessionContext(), value);
    }


    public List<Principal> getReadPrincipals(SessionContext ctx)
    {
        List<Principal> items = getLinkedItems(ctx, false, GeneratedCatalogConstants.Relations.PRINCIPAL2READABLECATALOGVERSIONRELATION, "Principal", null,
                        Utilities.getRelationOrderingOverride(PRINCIPAL2READABLECATALOGVERSIONRELATION_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(PRINCIPAL2READABLECATALOGVERSIONRELATION_TGT_ORDERED, true));
        return items;
    }


    public List<Principal> getReadPrincipals()
    {
        return getReadPrincipals(getSession().getSessionContext());
    }


    public long getReadPrincipalsCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, false, GeneratedCatalogConstants.Relations.PRINCIPAL2READABLECATALOGVERSIONRELATION, "Principal", null);
    }


    public long getReadPrincipalsCount()
    {
        return getReadPrincipalsCount(getSession().getSessionContext());
    }


    public void setReadPrincipals(SessionContext ctx, List<Principal> value)
    {
        setLinkedItems(ctx, false, GeneratedCatalogConstants.Relations.PRINCIPAL2READABLECATALOGVERSIONRELATION, null, value,
                        Utilities.getRelationOrderingOverride(PRINCIPAL2READABLECATALOGVERSIONRELATION_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(PRINCIPAL2READABLECATALOGVERSIONRELATION_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(PRINCIPAL2READABLECATALOGVERSIONRELATION_MARKMODIFIED));
    }


    public void setReadPrincipals(List<Principal> value)
    {
        setReadPrincipals(getSession().getSessionContext(), value);
    }


    public void addToReadPrincipals(SessionContext ctx, Principal value)
    {
        addLinkedItems(ctx, false, GeneratedCatalogConstants.Relations.PRINCIPAL2READABLECATALOGVERSIONRELATION, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(PRINCIPAL2READABLECATALOGVERSIONRELATION_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(PRINCIPAL2READABLECATALOGVERSIONRELATION_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(PRINCIPAL2READABLECATALOGVERSIONRELATION_MARKMODIFIED));
    }


    public void addToReadPrincipals(Principal value)
    {
        addToReadPrincipals(getSession().getSessionContext(), value);
    }


    public void removeFromReadPrincipals(SessionContext ctx, Principal value)
    {
        removeLinkedItems(ctx, false, GeneratedCatalogConstants.Relations.PRINCIPAL2READABLECATALOGVERSIONRELATION, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(PRINCIPAL2READABLECATALOGVERSIONRELATION_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(PRINCIPAL2READABLECATALOGVERSIONRELATION_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(PRINCIPAL2READABLECATALOGVERSIONRELATION_MARKMODIFIED));
    }


    public void removeFromReadPrincipals(Principal value)
    {
        removeFromReadPrincipals(getSession().getSessionContext(), value);
    }


    public List<SyncItemJob> getSynchronizations(SessionContext ctx)
    {
        return (List<SyncItemJob>)SYNCHRONIZATIONSHANDLER.getValues(ctx, (Item)this);
    }


    public List<SyncItemJob> getSynchronizations()
    {
        return getSynchronizations(getSession().getSessionContext());
    }


    public void setSynchronizations(SessionContext ctx, List<SyncItemJob> value)
    {
        SYNCHRONIZATIONSHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setSynchronizations(List<SyncItemJob> value)
    {
        setSynchronizations(getSession().getSessionContext(), value);
    }


    public void addToSynchronizations(SessionContext ctx, SyncItemJob value)
    {
        SYNCHRONIZATIONSHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToSynchronizations(SyncItemJob value)
    {
        addToSynchronizations(getSession().getSessionContext(), value);
    }


    public void removeFromSynchronizations(SessionContext ctx, SyncItemJob value)
    {
        SYNCHRONIZATIONSHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromSynchronizations(SyncItemJob value)
    {
        removeFromSynchronizations(getSession().getSessionContext(), value);
    }


    public Collection<Country> getTerritories(SessionContext ctx)
    {
        Collection<Country> coll = (Collection<Country>)getProperty(ctx, "territories");
        return (coll != null) ? coll : Collections.EMPTY_LIST;
    }


    public Collection<Country> getTerritories()
    {
        return getTerritories(getSession().getSessionContext());
    }


    public void setTerritories(SessionContext ctx, Collection<Country> value)
    {
        setProperty(ctx, "territories", (value == null || !value.isEmpty()) ? value : null);
    }


    public void setTerritories(Collection<Country> value)
    {
        setTerritories(getSession().getSessionContext(), value);
    }


    public String getVersion(SessionContext ctx)
    {
        return (String)getProperty(ctx, "version");
    }


    public String getVersion()
    {
        return getVersion(getSession().getSessionContext());
    }


    public void setVersion(SessionContext ctx, String value)
    {
        setProperty(ctx, "version", value);
    }


    public void setVersion(String value)
    {
        setVersion(getSession().getSessionContext(), value);
    }


    public List<Principal> getWritePrincipals(SessionContext ctx)
    {
        List<Principal> items = getLinkedItems(ctx, false, GeneratedCatalogConstants.Relations.PRINCIPAL2WRITEABLECATALOGVERSIONRELATION, "Principal", null,
                        Utilities.getRelationOrderingOverride(PRINCIPAL2WRITEABLECATALOGVERSIONRELATION_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(PRINCIPAL2WRITEABLECATALOGVERSIONRELATION_TGT_ORDERED, true));
        return items;
    }


    public List<Principal> getWritePrincipals()
    {
        return getWritePrincipals(getSession().getSessionContext());
    }


    public long getWritePrincipalsCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, false, GeneratedCatalogConstants.Relations.PRINCIPAL2WRITEABLECATALOGVERSIONRELATION, "Principal", null);
    }


    public long getWritePrincipalsCount()
    {
        return getWritePrincipalsCount(getSession().getSessionContext());
    }


    public void setWritePrincipals(SessionContext ctx, List<Principal> value)
    {
        setLinkedItems(ctx, false, GeneratedCatalogConstants.Relations.PRINCIPAL2WRITEABLECATALOGVERSIONRELATION, null, value,
                        Utilities.getRelationOrderingOverride(PRINCIPAL2WRITEABLECATALOGVERSIONRELATION_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(PRINCIPAL2WRITEABLECATALOGVERSIONRELATION_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(PRINCIPAL2WRITEABLECATALOGVERSIONRELATION_MARKMODIFIED));
    }


    public void setWritePrincipals(List<Principal> value)
    {
        setWritePrincipals(getSession().getSessionContext(), value);
    }


    public void addToWritePrincipals(SessionContext ctx, Principal value)
    {
        addLinkedItems(ctx, false, GeneratedCatalogConstants.Relations.PRINCIPAL2WRITEABLECATALOGVERSIONRELATION, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(PRINCIPAL2WRITEABLECATALOGVERSIONRELATION_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(PRINCIPAL2WRITEABLECATALOGVERSIONRELATION_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(PRINCIPAL2WRITEABLECATALOGVERSIONRELATION_MARKMODIFIED));
    }


    public void addToWritePrincipals(Principal value)
    {
        addToWritePrincipals(getSession().getSessionContext(), value);
    }


    public void removeFromWritePrincipals(SessionContext ctx, Principal value)
    {
        removeLinkedItems(ctx, false, GeneratedCatalogConstants.Relations.PRINCIPAL2WRITEABLECATALOGVERSIONRELATION, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(PRINCIPAL2WRITEABLECATALOGVERSIONRELATION_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(PRINCIPAL2WRITEABLECATALOGVERSIONRELATION_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(PRINCIPAL2WRITEABLECATALOGVERSIONRELATION_MARKMODIFIED));
    }


    public void removeFromWritePrincipals(Principal value)
    {
        removeFromWritePrincipals(getSession().getSessionContext(), value);
    }
}
