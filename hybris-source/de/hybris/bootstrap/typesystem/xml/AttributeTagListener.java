package de.hybris.bootstrap.typesystem.xml;

import de.hybris.bootstrap.xml.ObjectProcessor;
import de.hybris.bootstrap.xml.ParseAbortException;
import de.hybris.bootstrap.xml.StringValueTagListener;
import de.hybris.bootstrap.xml.TagListener;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

class AttributeTagListener extends AbstractTypeSystemTagListener
{
    AttributeTagListener(AttributesTagListener parent, String tagName)
    {
        super((AbstractTypeSystemTagListener)parent, tagName);
    }


    protected Collection createSubTagListeners()
    {
        return
                        Arrays.asList(new TagListener[] {(TagListener)new ModifiersTagListener(this, "modifiers"), (TagListener)new StringValueTagListener((TagListener)this, "defaultvalue"), (TagListener)new StringValueTagListener((TagListener)this, "description"),
                                        (TagListener)new AttributePersistenceTagListener(this, "persistence"), (TagListener)new CustomPropsTagListener(this, "custom-properties"), (TagListener)new ModelTagListener(this, "model")});
    }


    protected Object processEndElement(ObjectProcessor processor) throws ParseAbortException
    {
        Object[] persistence = (Object[])getSubTagValue("persistence");
        boolean redeclare = getBooleanAttribute("redeclare", false);
        Integer intmodifier = (Integer)getSubTagValueMap("modifiers").get("modifiers");
        Boolean unique = (Boolean)getSubTagValueMap("modifiers").get("uniqueflag");
        getParser().loadNewItemAttribute(((ItemTypeTagListener)
                                        getParent(ItemTypeTagListener.class)).getCode(),
                        getAttribute("qualifier"),
                        getAttribute("type"),
                        (intmodifier == null) ? 31 : intmodifier.intValue(), redeclare,
                        getAttribute("isSelectionOf"),
                        (persistence != null) ? (String)persistence[0] : null,
                        (persistence != null) ? (String)persistence[1] : null,
                        (persistence != null) ? (Map)persistence[3] : null,
                        (persistence != null) ? (String)persistence[2] : null, (String)
                                        getSubTagValue("defaultvalue"), (String)
                                        getSubTagValue("description"), (Map)
                                        getSubTagValue("custom-properties"),
                        getAttribute("metatype"),
                        isAutocreate(),
                        isGenerate(),
                        (getSubTagValue("model") == null) ? new ModelTagListener.ModelData() :
                                        (ModelTagListener.ModelData)getSubTagValue("model"),
                        (unique == null) ? false : unique.booleanValue());
        return null;
    }
}
