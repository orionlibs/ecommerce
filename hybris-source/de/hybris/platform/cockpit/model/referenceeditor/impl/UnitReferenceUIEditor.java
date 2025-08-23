package de.hybris.platform.cockpit.model.referenceeditor.impl;

import de.hybris.platform.cockpit.model.editor.EditorListener;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.TypeTools;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.zkoss.zk.ui.HtmlBasedComponent;

public class UnitReferenceUIEditor extends SimpleReferenceUIEditor
{
    private String allowedUnitType = null;


    protected List<TypedObject> getAvailableValues()
    {
        List<TypedObject> availableValues = super.getAvailableValues();
        List<TypedObject> ret = new ArrayList<>();
        for(TypedObject typedObject : availableValues)
        {
            Object type = TypeTools.getObjectAttributeValue(typedObject, "unitType",
                            UISessionUtils.getCurrentSession().getTypeService());
            if(type instanceof String && StringUtils.equalsIgnoreCase((String)type, this.allowedUnitType))
            {
                ret.add(typedObject);
            }
        }
        return ret;
    }


    public HtmlBasedComponent createViewComponent(Object initialValue, Map<String, ? extends Object> parameters, EditorListener listener)
    {
        Object cond = parameters.get("allowedUnitType");
        if(cond instanceof String)
        {
            this.allowedUnitType = (String)cond;
        }
        return super.createViewComponent(initialValue, parameters, listener);
    }
}
