package de.hybris.platform.cockpit.jalo.template;

import de.hybris.platform.cockpit.constants.GeneratedCockpitConstants;
import de.hybris.platform.cockpit.jalo.CockpitManager;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;

public class CockpitItemTemplate extends GeneratedCockpitItemTemplate
{
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        ComposedType itemType = (ComposedType)allAttributes.get("relatedType");
        String qualifier = (String)allAttributes.get("code");
        if(itemType == null)
        {
            throw new JaloBusinessException(GeneratedCockpitConstants.TC.COCKPITITEMTEMPLATE + ".relatedType can not be null", 0);
        }
        if(qualifier == null)
        {
            throw new JaloBusinessException(GeneratedCockpitConstants.TC.COCKPITITEMTEMPLATE + ".code can not be null", 0);
        }
        if(CockpitManager.getInstance().getCockpitItemTemplate(itemType, qualifier) != null)
        {
            throw new JaloBusinessException(GeneratedCockpitConstants.TC.COCKPITITEMTEMPLATE + " with code '" + GeneratedCockpitConstants.TC.COCKPITITEMTEMPLATE + "' already exists", 0);
        }
        Item item = super.createItem(ctx, type, allAttributes);
        return item;
    }


    public void setCode(SessionContext ctx, String code)
    {
        if(code == null)
        {
            throw new JaloInvalidParameterException(GeneratedCockpitConstants.TC.COCKPITITEMTEMPLATE + ".code can not be null", 0);
        }
        if(code.contains("."))
        {
            throw new JaloInvalidParameterException(GeneratedCockpitConstants.TC.COCKPITITEMTEMPLATE + ".code can not contain '.'", 0);
        }
        ComposedType type = getRelatedType(ctx);
        if(type != null && CockpitManager.getInstance().getCockpitItemTemplate(type, code) != null)
        {
            throw new JaloInvalidParameterException(GeneratedCockpitConstants.TC.COCKPITITEMTEMPLATE + " with code '" + GeneratedCockpitConstants.TC.COCKPITITEMTEMPLATE + "' already exists", 0);
        }
        super.setCode(ctx, code);
    }
}
