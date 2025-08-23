package de.hybris.bootstrap.typesystem.xml;

import de.hybris.bootstrap.xml.ObjectProcessor;
import de.hybris.bootstrap.xml.ParseAbortException;
import de.hybris.bootstrap.xml.TagListener;
import java.util.Arrays;
import java.util.Collection;

class RelationTypeTagListener extends AbstractTypeSystemTagListener
{
    public static final boolean DEFAULT_MANY_TO_MANY_RELATIONS_ORDERED = false;


    RelationTypeTagListener(RelationTypesTagListener parent)
    {
        super((AbstractTypeSystemTagListener)parent, "relation");
    }


    protected Collection createSubTagListeners()
    {
        return Arrays.asList(new TagListener[] {(TagListener)new TypeSystemDeploymentsTagListener.DeploymentTagListener(this), (TagListener)new RelationEndTagListener(this, this, "sourceElement"), (TagListener)new RelationEndTagListener(this, this, "targetElement")});
    }


    protected Object processEndElement(ObjectProcessor processor) throws ParseAbortException
    {
        String oldDeplRef = getAttribute("deployment");
        String newDeplRef = null;
        Object[] deployment = (Object[])getSubTagValue("deployment");
        if(deployment != null)
        {
            newDeplRef = TypeSystemDeploymentsTagListener.processRelationDeployment(getParser(), getCode(), deployment);
        }
        RelationEndData srcEnd = (RelationEndData)getSubTagValue("sourceElement");
        RelationEndData tgtEnd = (RelationEndData)getSubTagValue("targetElement");
        getParser().loadNewRelationType(getCode(), getAttribute("metatype"),
                        getAttribute("jaloclass"), srcEnd.role, srcEnd.type, srcEnd.navigable, srcEnd.modifiers,
                        (srcEnd.uniquemodifier == null) ? false : srcEnd.uniquemodifier.booleanValue(), srcEnd.cardinality,
                        isSrcEndOrdered(srcEnd, tgtEnd), srcEnd.collectionType, srcEnd.props, srcEnd.metaType, srcEnd.description, srcEnd.modelData, tgtEnd.role, tgtEnd.type, tgtEnd.navigable, tgtEnd.modifiers,
                        (tgtEnd.uniquemodifier == null) ? false : tgtEnd.uniquemodifier.booleanValue(), tgtEnd.cardinality,
                        isTgtEndOrdered(srcEnd, tgtEnd), tgtEnd.collectionType, tgtEnd.props, tgtEnd.metaType, tgtEnd.description, tgtEnd.modelData, oldDeplRef, newDeplRef,
                        getBooleanAttribute("localized", false),
                        isAutocreate(), isGenerate());
        return null;
    }


    protected boolean isSrcEndOrdered(RelationEndData srcEnd, RelationEndData tgtEnd)
    {
        if(srcEnd.ordered != null)
        {
            return srcEnd.ordered.booleanValue();
        }
        boolean oneToMany = ("one".equalsIgnoreCase(srcEnd.cardinality) || "one".equalsIgnoreCase(tgtEnd.cardinality));
        if(oneToMany)
        {
            return false;
        }
        if("list".equalsIgnoreCase(srcEnd.collectionType))
        {
            return true;
        }
        return false;
    }


    protected boolean isTgtEndOrdered(RelationEndData srcEnd, RelationEndData tgtEnd)
    {
        if(tgtEnd.ordered != null)
        {
            return tgtEnd.ordered.booleanValue();
        }
        boolean oneToMany = ("one".equalsIgnoreCase(srcEnd.cardinality) || "one".equalsIgnoreCase(tgtEnd.cardinality));
        if(oneToMany)
        {
            return false;
        }
        if("list".equalsIgnoreCase(tgtEnd.collectionType))
        {
            return true;
        }
        return false;
    }
}
