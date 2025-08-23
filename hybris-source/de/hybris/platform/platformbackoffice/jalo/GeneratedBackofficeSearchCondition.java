package de.hybris.platform.platformbackoffice.jalo;

import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.platformbackoffice.constants.GeneratedPlatformbackofficeConstants;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedBackofficeSearchCondition extends GenericItem
{
    public static final String ATTRIBUTE = "attribute";
    public static final String VALUE = "value";
    public static final String VALUEREFERENCE = "valueReference";
    public static final String LANGUAGECODE = "languageCode";
    public static final String OPERATORCODE = "operatorCode";
    public static final String SELECTED = "selected";
    public static final String EDITOR = "editor";
    public static final String EDITORPARAMETERS = "editorParameters";
    public static final String SORTABLE = "sortable";
    public static final String DISABLED = "disabled";
    public static final String MANDATORY = "mandatory";
    public static final String SAVEDQUERY = "savedQuery";
    protected static final BidirectionalOneToManyHandler<GeneratedBackofficeSearchCondition> SAVEDQUERYHANDLER = new BidirectionalOneToManyHandler(GeneratedPlatformbackofficeConstants.TC.BACKOFFICESEARCHCONDITION, false, "savedQuery", null, false, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("attribute", Item.AttributeMode.INITIAL);
        tmp.put("value", Item.AttributeMode.INITIAL);
        tmp.put("valueReference", Item.AttributeMode.INITIAL);
        tmp.put("languageCode", Item.AttributeMode.INITIAL);
        tmp.put("operatorCode", Item.AttributeMode.INITIAL);
        tmp.put("selected", Item.AttributeMode.INITIAL);
        tmp.put("editor", Item.AttributeMode.INITIAL);
        tmp.put("editorParameters", Item.AttributeMode.INITIAL);
        tmp.put("sortable", Item.AttributeMode.INITIAL);
        tmp.put("disabled", Item.AttributeMode.INITIAL);
        tmp.put("mandatory", Item.AttributeMode.INITIAL);
        tmp.put("savedQuery", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public String getAttribute(SessionContext ctx)
    {
        return (String)getProperty(ctx, "attribute");
    }


    public String getAttribute()
    {
        return getAttribute(getSession().getSessionContext());
    }


    public void setAttribute(SessionContext ctx, String value)
    {
        setProperty(ctx, "attribute", value);
    }


    public void setAttribute(String value)
    {
        setAttribute(getSession().getSessionContext(), value);
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        SAVEDQUERYHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public Boolean isDisabled(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "disabled");
    }


    public Boolean isDisabled()
    {
        return isDisabled(getSession().getSessionContext());
    }


    public boolean isDisabledAsPrimitive(SessionContext ctx)
    {
        Boolean value = isDisabled(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isDisabledAsPrimitive()
    {
        return isDisabledAsPrimitive(getSession().getSessionContext());
    }


    public void setDisabled(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "disabled", value);
    }


    public void setDisabled(Boolean value)
    {
        setDisabled(getSession().getSessionContext(), value);
    }


    public void setDisabled(SessionContext ctx, boolean value)
    {
        setDisabled(ctx, Boolean.valueOf(value));
    }


    public void setDisabled(boolean value)
    {
        setDisabled(getSession().getSessionContext(), value);
    }


    public String getEditor(SessionContext ctx)
    {
        return (String)getProperty(ctx, "editor");
    }


    public String getEditor()
    {
        return getEditor(getSession().getSessionContext());
    }


    public void setEditor(SessionContext ctx, String value)
    {
        setProperty(ctx, "editor", value);
    }


    public void setEditor(String value)
    {
        setEditor(getSession().getSessionContext(), value);
    }


    public Map<String, String> getAllEditorParameters(SessionContext ctx)
    {
        Map<String, String> map = (Map<String, String>)getProperty(ctx, "editorParameters");
        return (map != null) ? map : Collections.EMPTY_MAP;
    }


    public Map<String, String> getAllEditorParameters()
    {
        return getAllEditorParameters(getSession().getSessionContext());
    }


    public void setAllEditorParameters(SessionContext ctx, Map<String, String> value)
    {
        setProperty(ctx, "editorParameters", value);
    }


    public void setAllEditorParameters(Map<String, String> value)
    {
        setAllEditorParameters(getSession().getSessionContext(), value);
    }


    public String getLanguageCode(SessionContext ctx)
    {
        return (String)getProperty(ctx, "languageCode");
    }


    public String getLanguageCode()
    {
        return getLanguageCode(getSession().getSessionContext());
    }


    public void setLanguageCode(SessionContext ctx, String value)
    {
        setProperty(ctx, "languageCode", value);
    }


    public void setLanguageCode(String value)
    {
        setLanguageCode(getSession().getSessionContext(), value);
    }


    public Boolean isMandatory(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "mandatory");
    }


    public Boolean isMandatory()
    {
        return isMandatory(getSession().getSessionContext());
    }


    public boolean isMandatoryAsPrimitive(SessionContext ctx)
    {
        Boolean value = isMandatory(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isMandatoryAsPrimitive()
    {
        return isMandatoryAsPrimitive(getSession().getSessionContext());
    }


    public void setMandatory(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "mandatory", value);
    }


    public void setMandatory(Boolean value)
    {
        setMandatory(getSession().getSessionContext(), value);
    }


    public void setMandatory(SessionContext ctx, boolean value)
    {
        setMandatory(ctx, Boolean.valueOf(value));
    }


    public void setMandatory(boolean value)
    {
        setMandatory(getSession().getSessionContext(), value);
    }


    public String getOperatorCode(SessionContext ctx)
    {
        return (String)getProperty(ctx, "operatorCode");
    }


    public String getOperatorCode()
    {
        return getOperatorCode(getSession().getSessionContext());
    }


    public void setOperatorCode(SessionContext ctx, String value)
    {
        setProperty(ctx, "operatorCode", value);
    }


    public void setOperatorCode(String value)
    {
        setOperatorCode(getSession().getSessionContext(), value);
    }


    public BackofficeSavedQuery getSavedQuery(SessionContext ctx)
    {
        return (BackofficeSavedQuery)getProperty(ctx, "savedQuery");
    }


    public BackofficeSavedQuery getSavedQuery()
    {
        return getSavedQuery(getSession().getSessionContext());
    }


    public void setSavedQuery(SessionContext ctx, BackofficeSavedQuery value)
    {
        SAVEDQUERYHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setSavedQuery(BackofficeSavedQuery value)
    {
        setSavedQuery(getSession().getSessionContext(), value);
    }


    public Boolean isSelected(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "selected");
    }


    public Boolean isSelected()
    {
        return isSelected(getSession().getSessionContext());
    }


    public boolean isSelectedAsPrimitive(SessionContext ctx)
    {
        Boolean value = isSelected(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isSelectedAsPrimitive()
    {
        return isSelectedAsPrimitive(getSession().getSessionContext());
    }


    public void setSelected(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "selected", value);
    }


    public void setSelected(Boolean value)
    {
        setSelected(getSession().getSessionContext(), value);
    }


    public void setSelected(SessionContext ctx, boolean value)
    {
        setSelected(ctx, Boolean.valueOf(value));
    }


    public void setSelected(boolean value)
    {
        setSelected(getSession().getSessionContext(), value);
    }


    public Boolean isSortable(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "sortable");
    }


    public Boolean isSortable()
    {
        return isSortable(getSession().getSessionContext());
    }


    public boolean isSortableAsPrimitive(SessionContext ctx)
    {
        Boolean value = isSortable(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isSortableAsPrimitive()
    {
        return isSortableAsPrimitive(getSession().getSessionContext());
    }


    public void setSortable(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "sortable", value);
    }


    public void setSortable(Boolean value)
    {
        setSortable(getSession().getSessionContext(), value);
    }


    public void setSortable(SessionContext ctx, boolean value)
    {
        setSortable(ctx, Boolean.valueOf(value));
    }


    public void setSortable(boolean value)
    {
        setSortable(getSession().getSessionContext(), value);
    }


    public String getValue(SessionContext ctx)
    {
        return (String)getProperty(ctx, "value");
    }


    public String getValue()
    {
        return getValue(getSession().getSessionContext());
    }


    public void setValue(SessionContext ctx, String value)
    {
        setProperty(ctx, "value", value);
    }


    public void setValue(String value)
    {
        setValue(getSession().getSessionContext(), value);
    }


    public Item getValueReference(SessionContext ctx)
    {
        return (Item)getProperty(ctx, "valueReference");
    }


    public Item getValueReference()
    {
        return getValueReference(getSession().getSessionContext());
    }


    public void setValueReference(SessionContext ctx, Item value)
    {
        setProperty(ctx, "valueReference", value);
    }


    public void setValueReference(Item value)
    {
        setValueReference(getSession().getSessionContext(), value);
    }
}
