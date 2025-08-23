package de.hybris.platform.audit.internal.config.validation;

import de.hybris.platform.audit.internal.config.AbstractAttribute;
import de.hybris.platform.audit.internal.config.AbstractTypedAttribute;
import de.hybris.platform.audit.internal.config.AtomicAttribute;
import de.hybris.platform.audit.internal.config.AuditReportConfig;
import de.hybris.platform.audit.internal.config.ReferenceAttribute;
import de.hybris.platform.audit.internal.config.Type;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.type.TypeService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang.StringUtils;
import org.assertj.core.util.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class AuditReportConfigValidator
{
    private static final Logger LOG = LoggerFactory.getLogger(AuditReportConfigValidator.class);
    private TypeService typeService;


    public void markInvalidConfigurationFragments(AuditReportConfig config)
    {
        LOG.debug("--- Start model verification for {}", config.getName());
        Collection<AuditConfigViolation> vList = validateSubTypes(config, true);
        LOG.debug("--- Stop model verification for {}. Total violations number {}", config.getName(),
                        Integer.valueOf(vList.size()));
    }


    public List<AuditConfigViolation> validate(AuditReportConfig config)
    {
        return validate(config, AuditConfigViolation.ViolationLevel.ERROR);
    }


    public List<AuditConfigViolation> validate(AuditReportConfig config, AuditConfigViolation.ViolationLevel level)
    {
        if(isRootTypeInvalid(config))
        {
            return Collections.singletonList(new AuditConfigViolation(
                            String.format("No correct root type configured in '%s' report", new Object[] {config.getName()}), AuditConfigViolation.ViolationLevel.ERROR));
        }
        Collection<AuditConfigViolation> violationList = validateSubTypes(config, false);
        return (List<AuditConfigViolation>)violationList.stream().filter(v -> matchesViolationLevel(level, v.getLevel()))
                        .collect(Collectors.toList());
    }


    private boolean isRootTypeInvalid(AuditReportConfig config)
    {
        return isTypeInvalid(config.getGivenRootType());
    }


    public boolean matchesViolationLevel(AuditConfigViolation.ViolationLevel configuredLevel, AuditConfigViolation.ViolationLevel violationLevel)
    {
        return (configuredLevel == AuditConfigViolation.ViolationLevel.WARNING || configuredLevel == violationLevel);
    }


    private Collection<AuditConfigViolation> validateSubTypes(AuditReportConfig config, boolean markConfigInvalid)
    {
        List<AuditConfigViolation> violationList = new ArrayList<>();
        for(Type typeToCheck : config.getAllTypes())
        {
            if(isTypeInvalid(typeToCheck))
            {
                String type = (typeToCheck != null) ? typeToCheck.getCode() : "unknown";
                String invalidMsg = String.format("Type %s is not defined in the system and its configuration will be ignored", new Object[] {type});
                violationList.add(createWarningViolation(invalidMsg));
                if(typeToCheck != null && markConfigInvalid)
                {
                    typeToCheck.setValid(false);
                }
                continue;
            }
            if(typeToCheck != null)
            {
                List<ComposedTypeModel> allSubtypes = getAllSubTypesForType(typeToCheck);
                validateAtomicAttributes(typeToCheck, allSubtypes, violationList, markConfigInvalid);
                validateReferenceAttributes(typeToCheck, allSubtypes, violationList, markConfigInvalid);
                validateVirtualAttributes(typeToCheck, violationList, markConfigInvalid);
                validateRelationAttributes(typeToCheck, violationList, markConfigInvalid);
            }
        }
        return violationList;
    }


    private void validateAtomicAttributes(Type subType, List<ComposedTypeModel> allSubtypes, List<AuditConfigViolation> violationList, boolean markConfigInvalid)
    {
        for(AtomicAttribute refAttr : subType.getAllAtomicAttributes())
        {
            List<AuditConfigViolation> auditConfigViolations = validateQualifier(subType, allSubtypes, (AbstractAttribute)refAttr);
            violationList.addAll(auditConfigViolations);
            if(markConfigInvalid && !auditConfigViolations.isEmpty())
            {
                refAttr.setValid(false);
            }
        }
    }


    private void validateReferenceAttributes(Type subType, List<ComposedTypeModel> allSubtypes, List<AuditConfigViolation> violationList, boolean markConfigInvalid)
    {
        for(ReferenceAttribute refAttr : subType.getAllReferenceAttributes())
        {
            validateTypeAndQualifierForRefAttribute(subType, allSubtypes, refAttr, violationList, markConfigInvalid);
        }
    }


    private void validateVirtualAttributes(Type typeToCheck, List<AuditConfigViolation> violationList, boolean markConfigInvalid)
    {
        for(AbstractTypedAttribute attr : typeToCheck.getAllVirtualAttributes())
        {
            violationList.addAll(validateType(typeToCheck, attr, markConfigInvalid));
            if(attr.getResolvesBy() == null || attr.getResolvesBy().getExpression() == null)
            {
                String msg = String.format("Required resolves-by definition for type %s and virtual-attribute %s is missing or is wrongly configured (missing expression)", new Object[] {typeToCheck
                                .getCode(), attr});
                violationList.add(new AuditConfigViolation(msg, AuditConfigViolation.ViolationLevel.ERROR));
            }
        }
    }


    private void validateRelationAttributes(Type typeToCheck, List<AuditConfigViolation> violationList, boolean markConfigInvalid)
    {
        for(AbstractTypedAttribute attr : typeToCheck.getAllRelationAttributes())
        {
            violationList.addAll(validateType(typeToCheck, attr, markConfigInvalid));
        }
    }


    private List<AuditConfigViolation> validateType(Type typeToCheck, AbstractTypedAttribute attr, boolean markConfigInvalid)
    {
        List<AuditConfigViolation> violations = new ArrayList<>();
        if(attr.getType() == null)
        {
            violations.add(new AuditConfigViolation(
                            String.format("Attribute: %s (defined by %s type) has empty type", new Object[] {attr, typeToCheck.getDefaultName()}), AuditConfigViolation.ViolationLevel.ERROR));
        }
        else if(isTypeInvalid(attr.getType()))
        {
            String invalidMsg = String.format("Attribute: %s (defined by %s type) is incorrect and will be ignored", new Object[] {attr
                            .getDefaultName(), typeToCheck.getDefaultName()});
            violations.add(createWarningViolation(invalidMsg));
            if(markConfigInvalid)
            {
                attr.setValid(false);
            }
        }
        return violations;
    }


    private AuditConfigViolation createWarningViolation(String desc)
    {
        return new AuditConfigViolation(desc, AuditConfigViolation.ViolationLevel.WARNING);
    }


    private List<ComposedTypeModel> getAllSubTypesForType(Type typeToFindSubTypes)
    {
        List<ComposedTypeModel> allSubtypes = new ArrayList<>();
        if(typeToFindSubTypes != null)
        {
            try
            {
                ComposedTypeModel compTypeModel = this.typeService.getComposedTypeForCode(typeToFindSubTypes.getCode());
                allSubtypes.add(compTypeModel);
                allSubtypes.addAll(compTypeModel.getAllSubTypes());
            }
            catch(UnknownIdentifierException uie)
            {
                LOG.debug(String.format("UnknownIdentifierException for Type code %s", new Object[] {typeToFindSubTypes.getCode()}), (Throwable)uie);
            }
        }
        return allSubtypes;
    }


    public List<AuditConfigViolation> validateQualifier(Type subType, List<ComposedTypeModel> composedModelList, AbstractAttribute attr)
    {
        boolean foundQualifier = false;
        List<AuditConfigViolation> violations = new ArrayList<>();
        Set<String> dynamicAttributes = Sets.newHashSet();
        for(ComposedTypeModel ctm : composedModelList)
        {
            String qualifier = extractQualifier(attr);
            if(this.typeService.hasAttribute(ctm, qualifier))
            {
                foundQualifier = true;
                AttributeDescriptorModel attributeDescriptor = this.typeService.getAttributeDescriptor(ctm.getCode(), qualifier);
                if(isDynamicAttribute(attributeDescriptor))
                {
                    dynamicAttributes.add(attr.getDefaultName());
                }
            }
        }
        if(!foundQualifier)
        {
            String invalidMsg = String.format("Atomic attribute %s configured for type %s is not defined in the model and will be ignored", new Object[] {attr
                            .getDefaultName(), subType.getDefaultName()});
            violations.add(createWarningViolation(invalidMsg));
        }
        for(String dynamicAttribute : dynamicAttributes)
        {
            String invalidMsg = String.format("Atomic attribute %s configured for type %s is a dynamic attribute", new Object[] {dynamicAttribute, subType
                            .getDefaultName()});
            violations.add(new AuditConfigViolation(invalidMsg, AuditConfigViolation.ViolationLevel.ERROR));
        }
        return violations;
    }


    boolean isDynamicAttribute(AttributeDescriptorModel attributeDescriptor)
    {
        return StringUtils.isNotEmpty(attributeDescriptor.getAttributeHandler());
    }


    private boolean isQualifierValid(List<ComposedTypeModel> composedModelList, AbstractAttribute attr)
    {
        boolean valid = false;
        for(ComposedTypeModel ctm : composedModelList)
        {
            String qualifier = extractQualifier(attr);
            if(this.typeService.hasAttribute(ctm, qualifier))
            {
                AttributeDescriptorModel attributeDescriptor = this.typeService.getAttributeDescriptor(ctm.getCode(), qualifier);
                if(StringUtils.isEmpty(attributeDescriptor.getAttributeHandler()))
                {
                    valid = true;
                }
            }
        }
        return valid;
    }


    private String extractQualifier(AbstractAttribute attr)
    {
        String qualifier;
        if(attr instanceof AtomicAttribute)
        {
            qualifier = ((AtomicAttribute)attr).getQualifier();
        }
        else if(attr instanceof ReferenceAttribute)
        {
            qualifier = ((ReferenceAttribute)attr).getQualifier();
        }
        else
        {
            throw new IllegalStateException("Only attributes instanceof AtomicAttribute or ReferenceAttribute is allowed");
        }
        return qualifier;
    }


    private void validateTypeAndQualifierForRefAttribute(Type subType, List<ComposedTypeModel> composedModelList, ReferenceAttribute attributeToCheck, List<AuditConfigViolation> violationList, boolean markConfigInvalid)
    {
        if(attributeToCheck.getType() == null)
        {
            violationList.add(new AuditConfigViolation(String.format("Reference attribute: %s (defined by %s type) target type is not configured or is configured incorrectly", new Object[] {attributeToCheck
                            .getQualifier(), subType.getCode()}), AuditConfigViolation.ViolationLevel.ERROR));
            return;
        }
        if(isTypeInvalid(attributeToCheck.getType()))
        {
            String invalidMsg = String.format("Reference attribute: %s (defined by %s type) is incorrect and will be ignored", new Object[] {attributeToCheck
                            .getQualifier(), subType.getCode()});
            violationList.add(createWarningViolation(invalidMsg));
            if(markConfigInvalid)
            {
                attributeToCheck.setValid(false);
            }
            return;
        }
        if(!isQualifierValid(composedModelList, (AbstractAttribute)attributeToCheck))
        {
            String invalidMsg = String.format("Reference attribute: %s (defined by %s type) incorrect and will be ignored", new Object[] {attributeToCheck
                            .getQualifier(), subType.getCode()});
            violationList.add(createWarningViolation(invalidMsg));
            if(markConfigInvalid)
            {
                attributeToCheck.setValid(false);
            }
        }
    }


    private boolean isTypeInvalid(Type typeToCheck)
    {
        return !isTypeValid(typeToCheck);
    }


    private boolean isTypeValid(Type typeToCheck)
    {
        return (typeToCheck != null && checkIfTypeExist(typeToCheck));
    }


    private boolean checkIfTypeExist(Type typeToValidate)
    {
        try
        {
            this.typeService.getComposedTypeForCode(typeToValidate.getCode());
        }
        catch(UnknownIdentifierException uie)
        {
            return false;
        }
        return true;
    }


    @Required
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }
}
