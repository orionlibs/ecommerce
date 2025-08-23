package de.hybris.platform.cms2.jalo.pages;

import de.hybris.platform.cms2.constants.GeneratedCms2Constants;
import de.hybris.platform.cms2.jalo.contents.CMSItem;
import de.hybris.platform.cms2.jalo.relations.ContentSlotForPage;
import de.hybris.platform.cms2.jalo.restrictions.AbstractRestriction;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import de.hybris.platform.util.OneToManyHandler;
import de.hybris.platform.util.Utilities;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedAbstractPage extends CMSItem
{
    public static final String APPROVALSTATUS = "approvalStatus";
    public static final String PAGESTATUS = "pageStatus";
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final String MASTERTEMPLATE = "masterTemplate";
    public static final String DEFAULTPAGE = "defaultPage";
    public static final String ONLYONERESTRICTIONMUSTAPPLY = "onlyOneRestrictionMustApply";
    public static final String PREVIEWIMAGE = "previewImage";
    public static final String CONTENTSLOTS = "contentSlots";
    public static final String TYPE = "type";
    public static final String TYPECODE = "typeCode";
    public static final String MISSINGCONTENTSLOTS = "missingContentSlots";
    public static final String AVAILABLECONTENTSLOTS = "availableContentSlots";
    public static final String VIEW = "view";
    public static final String COPYTOCATALOGSDISABLED = "copyToCatalogsDisabled";
    public static final String ROBOTTAG = "robotTag";
    public static final String RESTRICTIONS = "restrictions";
    protected static String RESTRICTIONSFORPAGES_SRC_ORDERED = "relation.RestrictionsForPages.source.ordered";
    protected static String RESTRICTIONSFORPAGES_TGT_ORDERED = "relation.RestrictionsForPages.target.ordered";
    protected static String RESTRICTIONSFORPAGES_MARKMODIFIED = "relation.RestrictionsForPages.markmodified";
    public static final String LOCKEDBY = "lockedBy";
    public static final String ORIGINALPAGE = "originalPage";
    public static final String LOCALIZEDPAGES = "localizedPages";
    protected static final BidirectionalOneToManyHandler<GeneratedAbstractPage> LOCKEDBYHANDLER = new BidirectionalOneToManyHandler(GeneratedCms2Constants.TC.ABSTRACTPAGE, false, "lockedBy", null, false, true, 0);
    protected static final BidirectionalOneToManyHandler<GeneratedAbstractPage> ORIGINALPAGEHANDLER = new BidirectionalOneToManyHandler(GeneratedCms2Constants.TC.ABSTRACTPAGE, false, "originalPage", null, false, true, 0);
    protected static final OneToManyHandler<AbstractPage> LOCALIZEDPAGESHANDLER = new OneToManyHandler(GeneratedCms2Constants.TC.ABSTRACTPAGE, false, "originalPage", null, false, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(CMSItem.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("approvalStatus", Item.AttributeMode.INITIAL);
        tmp.put("pageStatus", Item.AttributeMode.INITIAL);
        tmp.put("title", Item.AttributeMode.INITIAL);
        tmp.put("description", Item.AttributeMode.INITIAL);
        tmp.put("masterTemplate", Item.AttributeMode.INITIAL);
        tmp.put("defaultPage", Item.AttributeMode.INITIAL);
        tmp.put("onlyOneRestrictionMustApply", Item.AttributeMode.INITIAL);
        tmp.put("previewImage", Item.AttributeMode.INITIAL);
        tmp.put("copyToCatalogsDisabled", Item.AttributeMode.INITIAL);
        tmp.put("robotTag", Item.AttributeMode.INITIAL);
        tmp.put("lockedBy", Item.AttributeMode.INITIAL);
        tmp.put("originalPage", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public EnumerationValue getApprovalStatus(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "approvalStatus");
    }


    public EnumerationValue getApprovalStatus()
    {
        return getApprovalStatus(getSession().getSessionContext());
    }


    public void setApprovalStatus(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "approvalStatus", value);
    }


    public void setApprovalStatus(EnumerationValue value)
    {
        setApprovalStatus(getSession().getSessionContext(), value);
    }


    public String getAvailableContentSlots()
    {
        return getAvailableContentSlots(getSession().getSessionContext());
    }


    public List<ContentSlotForPage> getContentSlots()
    {
        return getContentSlots(getSession().getSessionContext());
    }


    public Boolean isCopyToCatalogsDisabled(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "copyToCatalogsDisabled");
    }


    public Boolean isCopyToCatalogsDisabled()
    {
        return isCopyToCatalogsDisabled(getSession().getSessionContext());
    }


    public boolean isCopyToCatalogsDisabledAsPrimitive(SessionContext ctx)
    {
        Boolean value = isCopyToCatalogsDisabled(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isCopyToCatalogsDisabledAsPrimitive()
    {
        return isCopyToCatalogsDisabledAsPrimitive(getSession().getSessionContext());
    }


    public void setCopyToCatalogsDisabled(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "copyToCatalogsDisabled", value);
    }


    public void setCopyToCatalogsDisabled(Boolean value)
    {
        setCopyToCatalogsDisabled(getSession().getSessionContext(), value);
    }


    public void setCopyToCatalogsDisabled(SessionContext ctx, boolean value)
    {
        setCopyToCatalogsDisabled(ctx, Boolean.valueOf(value));
    }


    public void setCopyToCatalogsDisabled(boolean value)
    {
        setCopyToCatalogsDisabled(getSession().getSessionContext(), value);
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        LOCKEDBYHANDLER.newInstance(ctx, allAttributes);
        ORIGINALPAGEHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public Boolean isDefaultPage(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "defaultPage");
    }


    public Boolean isDefaultPage()
    {
        return isDefaultPage(getSession().getSessionContext());
    }


    public boolean isDefaultPageAsPrimitive(SessionContext ctx)
    {
        Boolean value = isDefaultPage(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isDefaultPageAsPrimitive()
    {
        return isDefaultPageAsPrimitive(getSession().getSessionContext());
    }


    public void setDefaultPage(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "defaultPage", value);
    }


    public void setDefaultPage(Boolean value)
    {
        setDefaultPage(getSession().getSessionContext(), value);
    }


    public void setDefaultPage(SessionContext ctx, boolean value)
    {
        setDefaultPage(ctx, Boolean.valueOf(value));
    }


    public void setDefaultPage(boolean value)
    {
        setDefaultPage(getSession().getSessionContext(), value);
    }


    public String getDescription(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedAbstractPage.getDescription requires a session language", 0);
        }
        return (String)getLocalizedProperty(ctx, "description");
    }


    public String getDescription()
    {
        return getDescription(getSession().getSessionContext());
    }


    public Map<Language, String> getAllDescription(SessionContext ctx)
    {
        return getAllLocalizedProperties(ctx, "description", C2LManager.getInstance().getAllLanguages());
    }


    public Map<Language, String> getAllDescription()
    {
        return getAllDescription(getSession().getSessionContext());
    }


    public void setDescription(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedAbstractPage.setDescription requires a session language", 0);
        }
        setLocalizedProperty(ctx, "description", value);
    }


    public void setDescription(String value)
    {
        setDescription(getSession().getSessionContext(), value);
    }


    public void setAllDescription(SessionContext ctx, Map<Language, String> value)
    {
        setAllLocalizedProperties(ctx, "description", value);
    }


    public void setAllDescription(Map<Language, String> value)
    {
        setAllDescription(getSession().getSessionContext(), value);
    }


    @Deprecated(since = "2105", forRemoval = true)
    public boolean isMarkModifiedDisabled(Item referencedItem)
    {
        ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("AbstractRestriction");
        if(relationSecondEnd0.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(RESTRICTIONSFORPAGES_MARKMODIFIED);
        }
        return true;
    }


    public Collection<AbstractPage> getLocalizedPages(SessionContext ctx)
    {
        return LOCALIZEDPAGESHANDLER.getValues(ctx, (Item)this);
    }


    public Collection<AbstractPage> getLocalizedPages()
    {
        return getLocalizedPages(getSession().getSessionContext());
    }


    public void setLocalizedPages(SessionContext ctx, Collection<AbstractPage> value)
    {
        LOCALIZEDPAGESHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setLocalizedPages(Collection<AbstractPage> value)
    {
        setLocalizedPages(getSession().getSessionContext(), value);
    }


    public void addToLocalizedPages(SessionContext ctx, AbstractPage value)
    {
        LOCALIZEDPAGESHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToLocalizedPages(AbstractPage value)
    {
        addToLocalizedPages(getSession().getSessionContext(), value);
    }


    public void removeFromLocalizedPages(SessionContext ctx, AbstractPage value)
    {
        LOCALIZEDPAGESHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromLocalizedPages(AbstractPage value)
    {
        removeFromLocalizedPages(getSession().getSessionContext(), value);
    }


    public User getLockedBy(SessionContext ctx)
    {
        return (User)getProperty(ctx, "lockedBy");
    }


    public User getLockedBy()
    {
        return getLockedBy(getSession().getSessionContext());
    }


    public void setLockedBy(SessionContext ctx, User value)
    {
        LOCKEDBYHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setLockedBy(User value)
    {
        setLockedBy(getSession().getSessionContext(), value);
    }


    public PageTemplate getMasterTemplate(SessionContext ctx)
    {
        return (PageTemplate)getProperty(ctx, "masterTemplate");
    }


    public PageTemplate getMasterTemplate()
    {
        return getMasterTemplate(getSession().getSessionContext());
    }


    public void setMasterTemplate(SessionContext ctx, PageTemplate value)
    {
        setProperty(ctx, "masterTemplate", value);
    }


    public void setMasterTemplate(PageTemplate value)
    {
        setMasterTemplate(getSession().getSessionContext(), value);
    }


    public String getMissingContentSlots()
    {
        return getMissingContentSlots(getSession().getSessionContext());
    }


    public Boolean isOnlyOneRestrictionMustApply(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "onlyOneRestrictionMustApply");
    }


    public Boolean isOnlyOneRestrictionMustApply()
    {
        return isOnlyOneRestrictionMustApply(getSession().getSessionContext());
    }


    public boolean isOnlyOneRestrictionMustApplyAsPrimitive(SessionContext ctx)
    {
        Boolean value = isOnlyOneRestrictionMustApply(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isOnlyOneRestrictionMustApplyAsPrimitive()
    {
        return isOnlyOneRestrictionMustApplyAsPrimitive(getSession().getSessionContext());
    }


    public void setOnlyOneRestrictionMustApply(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "onlyOneRestrictionMustApply", value);
    }


    public void setOnlyOneRestrictionMustApply(Boolean value)
    {
        setOnlyOneRestrictionMustApply(getSession().getSessionContext(), value);
    }


    public void setOnlyOneRestrictionMustApply(SessionContext ctx, boolean value)
    {
        setOnlyOneRestrictionMustApply(ctx, Boolean.valueOf(value));
    }


    public void setOnlyOneRestrictionMustApply(boolean value)
    {
        setOnlyOneRestrictionMustApply(getSession().getSessionContext(), value);
    }


    public AbstractPage getOriginalPage(SessionContext ctx)
    {
        return (AbstractPage)getProperty(ctx, "originalPage");
    }


    public AbstractPage getOriginalPage()
    {
        return getOriginalPage(getSession().getSessionContext());
    }


    public void setOriginalPage(SessionContext ctx, AbstractPage value)
    {
        ORIGINALPAGEHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setOriginalPage(AbstractPage value)
    {
        setOriginalPage(getSession().getSessionContext(), value);
    }


    public EnumerationValue getPageStatus(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "pageStatus");
    }


    public EnumerationValue getPageStatus()
    {
        return getPageStatus(getSession().getSessionContext());
    }


    public void setPageStatus(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "pageStatus", value);
    }


    public void setPageStatus(EnumerationValue value)
    {
        setPageStatus(getSession().getSessionContext(), value);
    }


    public Media getPreviewImage(SessionContext ctx)
    {
        return (Media)getProperty(ctx, "previewImage");
    }


    public Media getPreviewImage()
    {
        return getPreviewImage(getSession().getSessionContext());
    }


    public void setPreviewImage(SessionContext ctx, Media value)
    {
        (new Object(this))
                        .setValue(ctx, value);
    }


    public void setPreviewImage(Media value)
    {
        setPreviewImage(getSession().getSessionContext(), value);
    }


    public List<AbstractRestriction> getRestrictions(SessionContext ctx)
    {
        List<AbstractRestriction> items = getLinkedItems(ctx, true, GeneratedCms2Constants.Relations.RESTRICTIONSFORPAGES, "AbstractRestriction", null,
                        Utilities.getRelationOrderingOverride(RESTRICTIONSFORPAGES_SRC_ORDERED, true), false);
        return items;
    }


    public List<AbstractRestriction> getRestrictions()
    {
        return getRestrictions(getSession().getSessionContext());
    }


    public long getRestrictionsCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, true, GeneratedCms2Constants.Relations.RESTRICTIONSFORPAGES, "AbstractRestriction", null);
    }


    public long getRestrictionsCount()
    {
        return getRestrictionsCount(getSession().getSessionContext());
    }


    public void setRestrictions(SessionContext ctx, List<AbstractRestriction> value)
    {
        setLinkedItems(ctx, true, GeneratedCms2Constants.Relations.RESTRICTIONSFORPAGES, null, value,
                        Utilities.getRelationOrderingOverride(RESTRICTIONSFORPAGES_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(RESTRICTIONSFORPAGES_MARKMODIFIED));
    }


    public void setRestrictions(List<AbstractRestriction> value)
    {
        setRestrictions(getSession().getSessionContext(), value);
    }


    public void addToRestrictions(SessionContext ctx, AbstractRestriction value)
    {
        addLinkedItems(ctx, true, GeneratedCms2Constants.Relations.RESTRICTIONSFORPAGES, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(RESTRICTIONSFORPAGES_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(RESTRICTIONSFORPAGES_MARKMODIFIED));
    }


    public void addToRestrictions(AbstractRestriction value)
    {
        addToRestrictions(getSession().getSessionContext(), value);
    }


    public void removeFromRestrictions(SessionContext ctx, AbstractRestriction value)
    {
        removeLinkedItems(ctx, true, GeneratedCms2Constants.Relations.RESTRICTIONSFORPAGES, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(RESTRICTIONSFORPAGES_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(RESTRICTIONSFORPAGES_MARKMODIFIED));
    }


    public void removeFromRestrictions(AbstractRestriction value)
    {
        removeFromRestrictions(getSession().getSessionContext(), value);
    }


    public EnumerationValue getRobotTag(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "robotTag");
    }


    public EnumerationValue getRobotTag()
    {
        return getRobotTag(getSession().getSessionContext());
    }


    public void setRobotTag(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "robotTag", value);
    }


    public void setRobotTag(EnumerationValue value)
    {
        setRobotTag(getSession().getSessionContext(), value);
    }


    public String getTitle(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedAbstractPage.getTitle requires a session language", 0);
        }
        return (String)getLocalizedProperty(ctx, "title");
    }


    public String getTitle()
    {
        return getTitle(getSession().getSessionContext());
    }


    public Map<Language, String> getAllTitle(SessionContext ctx)
    {
        return getAllLocalizedProperties(ctx, "title", C2LManager.getInstance().getAllLanguages());
    }


    public Map<Language, String> getAllTitle()
    {
        return getAllTitle(getSession().getSessionContext());
    }


    public void setTitle(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedAbstractPage.setTitle requires a session language", 0);
        }
        setLocalizedProperty(ctx, "title", value);
    }


    public void setTitle(String value)
    {
        setTitle(getSession().getSessionContext(), value);
    }


    public void setAllTitle(SessionContext ctx, Map<Language, String> value)
    {
        setAllLocalizedProperties(ctx, "title", value);
    }


    public void setAllTitle(Map<Language, String> value)
    {
        setAllTitle(getSession().getSessionContext(), value);
    }


    public String getType()
    {
        return getType(getSession().getSessionContext());
    }


    public Map<Language, String> getAllType()
    {
        return getAllType(getSession().getSessionContext());
    }


    public String getTypeCode()
    {
        return getTypeCode(getSession().getSessionContext());
    }


    public String getView()
    {
        return getView(getSession().getSessionContext());
    }


    public abstract String getAvailableContentSlots(SessionContext paramSessionContext);


    public abstract List<ContentSlotForPage> getContentSlots(SessionContext paramSessionContext);


    public abstract String getMissingContentSlots(SessionContext paramSessionContext);


    public abstract String getType(SessionContext paramSessionContext);


    public abstract Map<Language, String> getAllType(SessionContext paramSessionContext);


    public abstract String getTypeCode(SessionContext paramSessionContext);


    public abstract String getView(SessionContext paramSessionContext);
}
