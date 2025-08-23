package de.hybris.platform.cockpit.jalo.template;

import de.hybris.platform.catalog.jalo.classification.ClassificationClass;
import de.hybris.platform.cockpit.constants.GeneratedCockpitConstants;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import de.hybris.platform.util.Utilities;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedCockpitItemTemplate extends GenericItem
{
    public static final String CODE = "code";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String RELATEDTYPE = "relatedType";
    public static final String CLASSIFICATIONCLASSES = "classificationClasses";
    protected static String COCKPITITEMTEMPLATE2CLASSIFICATIONCLASSRELATION_SRC_ORDERED = "relation.CockpitItemTemplate2ClassificationClassRelation.source.ordered";
    protected static String COCKPITITEMTEMPLATE2CLASSIFICATIONCLASSRELATION_TGT_ORDERED = "relation.CockpitItemTemplate2ClassificationClassRelation.target.ordered";
    protected static String COCKPITITEMTEMPLATE2CLASSIFICATIONCLASSRELATION_MARKMODIFIED = "relation.CockpitItemTemplate2ClassificationClassRelation.markmodified";
    protected static final BidirectionalOneToManyHandler<GeneratedCockpitItemTemplate> RELATEDTYPEHANDLER = new BidirectionalOneToManyHandler(GeneratedCockpitConstants.TC.COCKPITITEMTEMPLATE, false, "relatedType", null, false, true, 1);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("code", Item.AttributeMode.INITIAL);
        tmp.put("name", Item.AttributeMode.INITIAL);
        tmp.put("description", Item.AttributeMode.INITIAL);
        tmp.put("relatedType", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Collection<ClassificationClass> getClassificationClasses(SessionContext ctx)
    {
        List<ClassificationClass> items = getLinkedItems(ctx, true, GeneratedCockpitConstants.Relations.COCKPITITEMTEMPLATE2CLASSIFICATIONCLASSRELATION, "ClassificationClass", null, false, false);
        return items;
    }


    public Collection<ClassificationClass> getClassificationClasses()
    {
        return getClassificationClasses(getSession().getSessionContext());
    }


    public long getClassificationClassesCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, true, GeneratedCockpitConstants.Relations.COCKPITITEMTEMPLATE2CLASSIFICATIONCLASSRELATION, "ClassificationClass", null);
    }


    public long getClassificationClassesCount()
    {
        return getClassificationClassesCount(getSession().getSessionContext());
    }


    public void setClassificationClasses(SessionContext ctx, Collection<ClassificationClass> value)
    {
        setLinkedItems(ctx, true, GeneratedCockpitConstants.Relations.COCKPITITEMTEMPLATE2CLASSIFICATIONCLASSRELATION, null, value, false, false,
                        Utilities.getMarkModifiedOverride(COCKPITITEMTEMPLATE2CLASSIFICATIONCLASSRELATION_MARKMODIFIED));
    }


    public void setClassificationClasses(Collection<ClassificationClass> value)
    {
        setClassificationClasses(getSession().getSessionContext(), value);
    }


    public void addToClassificationClasses(SessionContext ctx, ClassificationClass value)
    {
        addLinkedItems(ctx, true, GeneratedCockpitConstants.Relations.COCKPITITEMTEMPLATE2CLASSIFICATIONCLASSRELATION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(COCKPITITEMTEMPLATE2CLASSIFICATIONCLASSRELATION_MARKMODIFIED));
    }


    public void addToClassificationClasses(ClassificationClass value)
    {
        addToClassificationClasses(getSession().getSessionContext(), value);
    }


    public void removeFromClassificationClasses(SessionContext ctx, ClassificationClass value)
    {
        removeLinkedItems(ctx, true, GeneratedCockpitConstants.Relations.COCKPITITEMTEMPLATE2CLASSIFICATIONCLASSRELATION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(COCKPITITEMTEMPLATE2CLASSIFICATIONCLASSRELATION_MARKMODIFIED));
    }


    public void removeFromClassificationClasses(ClassificationClass value)
    {
        removeFromClassificationClasses(getSession().getSessionContext(), value);
    }


    public String getCode(SessionContext ctx)
    {
        return (String)getProperty(ctx, "code");
    }


    public String getCode()
    {
        return getCode(getSession().getSessionContext());
    }


    public void setCode(SessionContext ctx, String value)
    {
        setProperty(ctx, "code", value);
    }


    public void setCode(String value)
    {
        setCode(getSession().getSessionContext(), value);
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        RELATEDTYPEHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public String getDescription(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedCockpitItemTemplate.getDescription requires a session language", 0);
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
            throw new JaloInvalidParameterException("GeneratedCockpitItemTemplate.setDescription requires a session language", 0);
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
        ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("ClassificationClass");
        if(relationSecondEnd0.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(COCKPITITEMTEMPLATE2CLASSIFICATIONCLASSRELATION_MARKMODIFIED);
        }
        return true;
    }


    public String getName(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedCockpitItemTemplate.getName requires a session language", 0);
        }
        return (String)getLocalizedProperty(ctx, "name");
    }


    public String getName()
    {
        return getName(getSession().getSessionContext());
    }


    public Map<Language, String> getAllName(SessionContext ctx)
    {
        return getAllLocalizedProperties(ctx, "name", C2LManager.getInstance().getAllLanguages());
    }


    public Map<Language, String> getAllName()
    {
        return getAllName(getSession().getSessionContext());
    }


    public void setName(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedCockpitItemTemplate.setName requires a session language", 0);
        }
        setLocalizedProperty(ctx, "name", value);
    }


    public void setName(String value)
    {
        setName(getSession().getSessionContext(), value);
    }


    public void setAllName(SessionContext ctx, Map<Language, String> value)
    {
        setAllLocalizedProperties(ctx, "name", value);
    }


    public void setAllName(Map<Language, String> value)
    {
        setAllName(getSession().getSessionContext(), value);
    }


    public ComposedType getRelatedType(SessionContext ctx)
    {
        return (ComposedType)getProperty(ctx, "relatedType");
    }


    public ComposedType getRelatedType()
    {
        return getRelatedType(getSession().getSessionContext());
    }


    public void setRelatedType(SessionContext ctx, ComposedType value)
    {
        RELATEDTYPEHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setRelatedType(ComposedType value)
    {
        setRelatedType(getSession().getSessionContext(), value);
    }
}
