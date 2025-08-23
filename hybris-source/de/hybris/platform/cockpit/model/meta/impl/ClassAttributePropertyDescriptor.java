package de.hybris.platform.cockpit.model.meta.impl;

import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.catalog.jalo.classification.ClassAttributeAssignment;
import de.hybris.platform.catalog.jalo.classification.ClassificationClass;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.servicelayer.internal.i18n.I18NConstants;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import java.util.Collection;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public class ClassAttributePropertyDescriptor implements PropertyDescriptor
{
    public static final String ATTRIBUTE_DELIMITER_PATTERN = ".";
    public static final String ESCAPE_PATTERN = "\\\\\\.";
    public static final String ESCAPED_PATTERN_MARKER = "<escaped_dot>";
    private static final String CLASS_ATTRIBUTE_REQUEST_CACHE = "classAttributeRequestCache";
    private final ClassificationClassPath classClassPath;
    private final String attributeQualifier;
    private final PropertyDescriptor.Multiplicity multiplicity;
    private final boolean localized;
    private final Integer position;
    private final String escapedQualifier;
    private PropertyDescriptor.Occurrence occurence;
    private String editorType;
    private final String rawQualifier;
    private String classificationAttributeValueInfo;


    public ClassAttributePropertyDescriptor(String qualifier)
    {
        this.rawQualifier = qualifier;
        String escapedQualifier = qualifier.replaceAll("\\\\\\.", "<escaped_dot>");
        int attrSeparatorIdx = escapedQualifier.lastIndexOf('.');
        if(attrSeparatorIdx == -1)
        {
            throw new IllegalArgumentException("Invalid class. attribute qualifier '" + qualifier + "' - expected <classification class path>.<qualifier>");
        }
        String escapedClassCode = escapedQualifier.substring(0, attrSeparatorIdx);
        String classCode = escapedClassCode.replaceAll("<escaped_dot>", ".");
        this.classClassPath = new ClassificationClassPath(classCode);
        String escapedQualifierAttributeQualifier = escapedQualifier.substring(attrSeparatorIdx + 1);
        this
                        .attributeQualifier = escapedQualifierAttributeQualifier.replaceAll("<escaped_dot>", ".");
        try
        {
            ClassAttributeAssignment assignment = getAttributeAssignment();
            this.position = assignment.getPosition();
            this.localized = assignment.isLocalized().booleanValue();
            this
                            .multiplicity = assignment.isMultiValuedAsPrimitive() ? (assignment.isRangeAsPrimitive() ? PropertyDescriptor.Multiplicity.RANGE : PropertyDescriptor.Multiplicity.LIST) : PropertyDescriptor.Multiplicity.SINGLE;
            if(assignment.isMandatoryAsPrimitive())
            {
                this.occurence = PropertyDescriptor.Occurrence.REQUIRED;
            }
            else
            {
                this.occurence = PropertyDescriptor.Occurrence.OPTIONAL;
            }
            this.escapedQualifier = calculateEscapedQualifier();
        }
        catch(JaloItemNotFoundException e)
        {
            throw new IllegalArgumentException("invalid qualifier '" + qualifier + "' - cannot find attribute", e);
        }
    }


    @Required
    public void setEditorType(String type)
    {
        if(type == null || type.trim().isEmpty())
        {
            throw new IllegalArgumentException("editor type was null or empty");
        }
        this.editorType = type;
    }


    public String getEditorType()
    {
        return this.editorType;
    }


    public PropertyDescriptor.Multiplicity getMultiplicity()
    {
        return this.multiplicity;
    }


    public void setOccurence(PropertyDescriptor.Occurrence occ)
    {
        this.occurence = occ;
    }


    public PropertyDescriptor.Occurrence getOccurence()
    {
        return this.occurence;
    }


    public String getQualifier()
    {
        return this.escapedQualifier;
    }


    protected String calculateEscapedQualifier()
    {
        return ClassificationClassPath.getClassCode(getClassificationClass()) + "." + ClassificationClassPath.getClassCode(getClassificationClass());
    }


    public String getAttributeQualifier()
    {
        return this.attributeQualifier;
    }


    public boolean isLocalized()
    {
        return this.localized;
    }


    public String toString()
    {
        return getClassAttributeInfo().getStringRepresentation();
    }


    protected String createStringRep()
    {
        ClassAttributeAssignment assignment = getAttributeAssignment();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getQualifier()).append("::");
        if(isLocalized())
        {
            stringBuilder.append("<l>");
        }
        switch(null.$SwitchMap$de$hybris$platform$cockpit$model$meta$PropertyDescriptor$Multiplicity[getMultiplicity().ordinal()])
        {
            case 1:
            case 2:
                stringBuilder.append("[");
                break;
            case 3:
                stringBuilder.append("{");
                break;
        }
        stringBuilder.append(getEditorType());
        switch(null.$SwitchMap$de$hybris$platform$cockpit$model$meta$PropertyDescriptor$Multiplicity[getMultiplicity().ordinal()])
        {
            case 1:
            case 2:
                stringBuilder.append("]");
                break;
            case 3:
                stringBuilder.append("}");
                break;
        }
        switch(null.$SwitchMap$de$hybris$platform$cockpit$model$meta$PropertyDescriptor$Occurrence[getOccurence().ordinal()])
        {
            case 1:
                stringBuilder.append("!");
                break;
            case 2:
                stringBuilder.append("!?");
                break;
            case 3:
                stringBuilder.append("?");
                break;
        }
        stringBuilder.append("(").append((assignment != null) ? assignment.getPK() : "n/a").append(")");
        return stringBuilder.toString();
    }


    public ClassAttributeAssignment getAttributeAssignment()
    {
        return getClassAttributeInfo().getClassAttributeAssignment();
    }


    public ClassAttributeAssignment getAttributeAssignmentInternal()
    {
        ClassificationClass cclass = getClassificationClass();
        if(cclass != null)
        {
            SessionContext ctx = null;
            try
            {
                ctx = JaloSession.getCurrentSession().createLocalSessionContext();
                ctx.setAttribute("disableRestrictions", Boolean.TRUE);
                Collection<ClassAttributeAssignment> assignments = cclass.getClassificationAttributeAssignments();
                for(ClassAttributeAssignment assignment : assignments)
                {
                    if(this.attributeQualifier.equalsIgnoreCase(assignment.getClassificationAttribute().getCode()))
                    {
                        return assignment;
                    }
                }
            }
            finally
            {
                if(ctx != null)
                {
                    JaloSession.getCurrentSession().removeLocalSessionContext();
                }
            }
        }
        return null;
    }


    public ClassificationClass getClassificationClass()
    {
        return getClassAttributeInfo().getClassificationClass();
    }


    protected ClassificationClass getClassificationClassInternal()
    {
        SessionContext ctx = null;
        try
        {
            ctx = JaloSession.getCurrentSession().createLocalSessionContext();
            ctx.setAttribute("disableRestrictions", Boolean.TRUE);
            return CatalogManager.getInstance().getClassificationClass(this.classClassPath.getClassSystem(), this.classClassPath
                            .getClassVersion(), this.classClassPath.getClassClass());
        }
        finally
        {
            if(ctx != null)
            {
                JaloSession.getCurrentSession().removeLocalSessionContext();
            }
        }
    }


    public int hashCode()
    {
        return getQualifier().hashCode();
    }


    public boolean equals(Object obj)
    {
        return (obj != null && getQualifier().equalsIgnoreCase(((PropertyDescriptor)obj).getQualifier()));
    }


    public String getName()
    {
        return getClassAttributeInfo().getName();
    }


    public String getName(String languageIso)
    {
        SessionService sessionService = (SessionService)Registry.getApplicationContext().getBean("sessionService", SessionService.class);
        String name = (String)sessionService.executeInLocalView((SessionExecutionBody)new Object(this, languageIso));
        return StringUtils.isBlank(name) ? null : name;
    }


    public String getDescription()
    {
        return getClassAttributeInfo().getDescription();
    }


    protected String getDescriptionInternal()
    {
        String description = "";
        SessionContext ctx = null;
        try
        {
            ctx = JaloSession.getCurrentSession().createLocalSessionContext();
            ctx.setAttribute("disableRestrictions", Boolean.TRUE);
            ctx.setAttribute("enable.language.fallback.serviceLayer", Boolean.TRUE);
            ctx.setAttribute(I18NConstants.LANGUAGE_FALLBACK_ENABLED, Boolean.TRUE);
            description = getAttributeAssignment().getDescription(ctx);
        }
        finally
        {
            if(ctx != null)
            {
                JaloSession.getCurrentSession().removeLocalSessionContext();
            }
        }
        return description;
    }


    public boolean isReadable()
    {
        return true;
    }


    public boolean isWritable()
    {
        return true;
    }


    public String getSelectionOf()
    {
        return null;
    }


    public Integer getPosition()
    {
        return this.position;
    }


    public static String escapeDots(String input)
    {
        if(input == null)
        {
            throw new IllegalArgumentException("input string cannot be null");
        }
        return input.replaceAll("\\.", "<escaped_dot>");
    }


    public String getClassificationAttributeValueInfo()
    {
        if(this.classificationAttributeValueInfo == null)
        {
            StringBuilder valueInfoBuilder = new StringBuilder();
            ClassificationClass classificationClass = getClassificationClass();
            valueInfoBuilder.append(classificationClass.getSystemVersion().getClassificationSystem().getId());
            valueInfoBuilder.append(" - ");
            valueInfoBuilder.append(classificationClass.getSystemVersion().getVersion());
            valueInfoBuilder.append(" : ");
            valueInfoBuilder.append(classificationClass.getName());
            valueInfoBuilder.append(" - ");
            valueInfoBuilder.append(getAttributeQualifier());
            this.classificationAttributeValueInfo = valueInfoBuilder.toString();
        }
        return this.classificationAttributeValueInfo;
    }


    private ClassAttributeInfo getClassAttributeInfo()
    {
        return (ClassAttributeInfo)(new Object(this, "classAttributeRequestCache"))
                        .get(this.rawQualifier);
    }
}
