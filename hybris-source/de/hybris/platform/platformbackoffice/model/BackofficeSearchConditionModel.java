package de.hybris.platform.platformbackoffice.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Map;

public class BackofficeSearchConditionModel extends ItemModel
{
    public static final String _TYPECODE = "BackofficeSearchCondition";
    public static final String _BACKOFFICESAVEDQUERY2SEARCHCONDITIONRELATION = "BackofficeSavedQuery2SearchConditionRelation";
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


    public BackofficeSearchConditionModel()
    {
    }


    public BackofficeSearchConditionModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public BackofficeSearchConditionModel(String _attribute, String _operatorCode)
    {
        setAttribute(_attribute);
        setOperatorCode(_operatorCode);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public BackofficeSearchConditionModel(String _attribute, String _operatorCode, ItemModel _owner)
    {
        setAttribute(_attribute);
        setOperatorCode(_operatorCode);
        setOwner(_owner);
    }


    @Accessor(qualifier = "attribute", type = Accessor.Type.GETTER)
    public String getAttribute()
    {
        return (String)getPersistenceContext().getPropertyValue("attribute");
    }


    @Accessor(qualifier = "disabled", type = Accessor.Type.GETTER)
    public Boolean getDisabled()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("disabled");
    }


    @Accessor(qualifier = "editor", type = Accessor.Type.GETTER)
    public String getEditor()
    {
        return (String)getPersistenceContext().getPropertyValue("editor");
    }


    @Accessor(qualifier = "editorParameters", type = Accessor.Type.GETTER)
    public Map<String, String> getEditorParameters()
    {
        return (Map<String, String>)getPersistenceContext().getPropertyValue("editorParameters");
    }


    @Accessor(qualifier = "languageCode", type = Accessor.Type.GETTER)
    public String getLanguageCode()
    {
        return (String)getPersistenceContext().getPropertyValue("languageCode");
    }


    @Accessor(qualifier = "mandatory", type = Accessor.Type.GETTER)
    public Boolean getMandatory()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("mandatory");
    }


    @Accessor(qualifier = "operatorCode", type = Accessor.Type.GETTER)
    public String getOperatorCode()
    {
        return (String)getPersistenceContext().getPropertyValue("operatorCode");
    }


    @Accessor(qualifier = "savedQuery", type = Accessor.Type.GETTER)
    public BackofficeSavedQueryModel getSavedQuery()
    {
        return (BackofficeSavedQueryModel)getPersistenceContext().getPropertyValue("savedQuery");
    }


    @Accessor(qualifier = "selected", type = Accessor.Type.GETTER)
    public Boolean getSelected()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("selected");
    }


    @Accessor(qualifier = "sortable", type = Accessor.Type.GETTER)
    public Boolean getSortable()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("sortable");
    }


    @Accessor(qualifier = "value", type = Accessor.Type.GETTER)
    public String getValue()
    {
        return (String)getPersistenceContext().getPropertyValue("value");
    }


    @Accessor(qualifier = "valueReference", type = Accessor.Type.GETTER)
    public ItemModel getValueReference()
    {
        return (ItemModel)getPersistenceContext().getPropertyValue("valueReference");
    }


    @Accessor(qualifier = "attribute", type = Accessor.Type.SETTER)
    public void setAttribute(String value)
    {
        getPersistenceContext().setPropertyValue("attribute", value);
    }


    @Accessor(qualifier = "disabled", type = Accessor.Type.SETTER)
    public void setDisabled(Boolean value)
    {
        getPersistenceContext().setPropertyValue("disabled", value);
    }


    @Accessor(qualifier = "editor", type = Accessor.Type.SETTER)
    public void setEditor(String value)
    {
        getPersistenceContext().setPropertyValue("editor", value);
    }


    @Accessor(qualifier = "editorParameters", type = Accessor.Type.SETTER)
    public void setEditorParameters(Map<String, String> value)
    {
        getPersistenceContext().setPropertyValue("editorParameters", value);
    }


    @Accessor(qualifier = "languageCode", type = Accessor.Type.SETTER)
    public void setLanguageCode(String value)
    {
        getPersistenceContext().setPropertyValue("languageCode", value);
    }


    @Accessor(qualifier = "mandatory", type = Accessor.Type.SETTER)
    public void setMandatory(Boolean value)
    {
        getPersistenceContext().setPropertyValue("mandatory", value);
    }


    @Accessor(qualifier = "operatorCode", type = Accessor.Type.SETTER)
    public void setOperatorCode(String value)
    {
        getPersistenceContext().setPropertyValue("operatorCode", value);
    }


    @Accessor(qualifier = "savedQuery", type = Accessor.Type.SETTER)
    public void setSavedQuery(BackofficeSavedQueryModel value)
    {
        getPersistenceContext().setPropertyValue("savedQuery", value);
    }


    @Accessor(qualifier = "selected", type = Accessor.Type.SETTER)
    public void setSelected(Boolean value)
    {
        getPersistenceContext().setPropertyValue("selected", value);
    }


    @Accessor(qualifier = "sortable", type = Accessor.Type.SETTER)
    public void setSortable(Boolean value)
    {
        getPersistenceContext().setPropertyValue("sortable", value);
    }


    @Accessor(qualifier = "value", type = Accessor.Type.SETTER)
    public void setValue(String value)
    {
        getPersistenceContext().setPropertyValue("value", value);
    }


    @Accessor(qualifier = "valueReference", type = Accessor.Type.SETTER)
    public void setValueReference(ItemModel value)
    {
        getPersistenceContext().setPropertyValue("valueReference", value);
    }
}
