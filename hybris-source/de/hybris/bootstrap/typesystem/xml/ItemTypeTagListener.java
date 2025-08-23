package de.hybris.bootstrap.typesystem.xml;

import de.hybris.bootstrap.xml.ObjectProcessor;
import de.hybris.bootstrap.xml.ParseAbortException;
import de.hybris.bootstrap.xml.TagListener;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

class ItemTypeTagListener extends AbstractTypeSystemTagListener
{
    ItemTypeTagListener(ItemTypesTagListener parent)
    {
        super((AbstractTypeSystemTagListener)parent, "itemtype");
    }


    ItemTypeTagListener(ItemTypeGroupTagListener parent)
    {
        super((AbstractTypeSystemTagListener)parent, "itemtype");
    }


    protected Collection createSubTagListeners()
    {
        return Arrays.asList(new TagListener[] {(TagListener)new TypeSystemDeploymentsTagListener.DeploymentTagListener(this), (TagListener)new ItemDescriptionTagListener(this, "description"), (TagListener)new AttributeTagListener.AttributesTagListener(this, "attributes"),
                        (TagListener)new IndexesTagListener(this, "indexes"), (TagListener)new CustomPropsTagListener(this, "custom-properties"), (TagListener)new ModelTagListener(this, "model")});
    }


    protected Object processEndElement(ObjectProcessor processor) throws ParseAbortException
    {
        String oldDeplRef = getAttribute("deployment");
        String newDeplRef = null;
        Object[] deployment = (Object[])getSubTagValue("deployment");
        if(deployment != null)
        {
            newDeplRef = TypeSystemDeploymentsTagListener.processItemDeployment(getParser(), getCode(), deployment);
        }
        getParser().loadNewItemType(
                        getCode(),
                        getAttribute("extends"),
                        getAttribute("jaloclass"),
                        getBooleanAttribute("abstract", false),
                        getBooleanAttribute("singleton", false),
                        getBooleanAttribute("jaloonly", false),
                        getAttribute("metatype"), oldDeplRef, newDeplRef,
                        isAutocreate(),
                        isGenerate(), (Map)
                                        getSubTagValue("custom-properties"),
                        (getSubTagValue("model") == null) ? new ModelTagListener.ModelData() : (ModelTagListener.ModelData)getSubTagValue("model"),
                        (getSubTagValue("description") == null) ? null :
                                        String.valueOf(getSubTagValue("description")),
                        getBooleanAttribute("legacyPersistence", false),
                        getAttribute("deprecatedSince"));
        return null;
    }
}
