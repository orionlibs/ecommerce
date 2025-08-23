package de.hybris.platform.cockpit.services.validation.impl;

import de.hybris.platform.cockpit.helpers.validation.ValidationUIHelper;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.services.validation.CockpitValidationService;
import de.hybris.platform.cockpit.services.validation.pojos.CockpitValidationDescriptor;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.validation.enums.Severity;
import de.hybris.platform.validation.exceptions.HybrisConstraintViolation;
import de.hybris.platform.validation.services.ValidationService;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultCockpitValidationService implements CockpitValidationService
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultCockpitValidationService.class);
    private ValidationService validationService;
    private ValidationUIHelper validationUIHelper;
    private TypeService cockpitTypeService;


    public String buildFormattedValidationMessages(Set<CockpitValidationDescriptor> validationInfo)
    {
        StringBuffer result = new StringBuffer(100);
        for(CockpitValidationDescriptor valDescriptor : validationInfo)
        {
            result.append("<img src=\"cockpit/images/" +
                            getValidationUIHelper().getValidationIcon(valDescriptor.getCockpitMessageLevel()) + "\" />&nbsp;");
            result.append("<span");
            if(!valDescriptor.isFramevorkValidation())
            {
                result.append(" style=\"text-decoration: underline;\"");
            }
            result.append('>');
            result.append(valDescriptor.getValidationMessage());
            result.append("</span>");
            result.append("<br />");
        }
        return result.toString();
    }


    public String buildValidationMessages(Set<CockpitValidationDescriptor> validationInfo, String sep)
    {
        StringBuffer result = new StringBuffer(17);
        for(CockpitValidationDescriptor valDescriptor : validationInfo)
        {
            result.append(valDescriptor.getValidationMessage());
            if(sep == null)
            {
                result.append(' ');
                continue;
            }
            result.append(sep);
        }
        return result.toString();
    }


    public boolean containsLevel(Set<CockpitValidationDescriptor> validationInfo, int level)
    {
        Object found = CollectionUtils.find(validationInfo, (Predicate)new Object(this, level));
        return !(found == null);
    }


    public Set<CockpitValidationDescriptor> convertToValidationDescriptors(Set<HybrisConstraintViolation> constraintViolations)
    {
        Set<CockpitValidationDescriptor> descriptors = new HashSet<>();
        if(constraintViolations != null)
        {
            for(HybrisConstraintViolation constraintViolation : constraintViolations)
            {
                CockpitValidationDescriptor cockpitValidationDescriptor = buildCockpitValidationDescriptor(constraintViolation);
                descriptors.add(cockpitValidationDescriptor);
            }
        }
        return descriptors;
    }


    public Set<CockpitValidationDescriptor> filterByMessageLevel(Set<CockpitValidationDescriptor> violations, int cockpitMessageLevel)
    {
        Set<CockpitValidationDescriptor> result = new HashSet<>();
        for(CockpitValidationDescriptor cockpitValidationDescriptor : violations)
        {
            if(cockpitValidationDescriptor.getCockpitMessageLevel() == cockpitMessageLevel)
            {
                result.add(cockpitValidationDescriptor);
            }
        }
        return result;
    }


    public int getCockpitMessageLevel(Severity severity)
    {
        switch(null.$SwitchMap$de$hybris$platform$validation$enums$Severity[severity.ordinal()])
        {
            case 1:
                result = 3;
                return result;
            case 2:
                result = 2;
                return result;
            case 3:
                result = 0;
                return result;
        }
        int result = 0;
        return result;
    }


    public int getHighestMessageLevel(Set<CockpitValidationDescriptor> validationInfo)
    {
        Set<Integer> transformedSet = new HashSet<>();
        for(CockpitValidationDescriptor cockpitValidationDescriptor : validationInfo)
        {
            transformedSet.add(Integer.valueOf(cockpitValidationDescriptor.getCockpitMessageLevel()));
        }
        int largest = -1;
        for(Integer current : transformedSet)
        {
            if(largest < current.intValue())
            {
                largest = current.intValue();
            }
        }
        return largest;
    }


    public Set<CockpitValidationDescriptor> getTypeValidationDescriptors(Set<CockpitValidationDescriptor> violations)
    {
        Set<CockpitValidationDescriptor> result = new HashSet<>();
        for(CockpitValidationDescriptor cockpitValidationDescriptor : violations)
        {
            if(cockpitValidationDescriptor.isTypeConstraint())
            {
                result.add(cockpitValidationDescriptor);
            }
        }
        return result;
    }


    public Set<CockpitValidationDescriptor> getValidationDescriptors(Set<CockpitValidationDescriptor> violations, PropertyDescriptor propertyDescriptor)
    {
        Set<CockpitValidationDescriptor> result = new HashSet<>();
        for(CockpitValidationDescriptor cockpitValidationDescriptor : violations)
        {
            PropertyDescriptor propDescr = cockpitValidationDescriptor.getPropertyDescriptor();
            if(propDescr != null && propDescr.getQualifier().equals(propertyDescriptor.getQualifier()))
            {
                result.add(cockpitValidationDescriptor);
            }
        }
        return result;
    }


    public ValidationService getValidationService()
    {
        return this.validationService;
    }


    public void setValidationService(ValidationService validationService)
    {
        this.validationService = validationService;
    }


    public Set<CockpitValidationDescriptor> validateModel(ItemModel newItemModel)
    {
        Set<HybrisConstraintViolation> violations = getValidationService().validate(newItemModel, new Class[0]);
        return convertToValidationDescriptors(violations);
    }


    public <T> CockpitValidationDescriptor validateProperty(T object, PropertyDescriptor propertyDescr)
    {
        CockpitValidationDescriptor result = null;
        try
        {
            String propertyName = UISessionUtils.getCurrentSession().getTypeService().getAttributeCodeFromPropertyQualifier(propertyDescr.getQualifier());
            Set<HybrisConstraintViolation> constraintViolations = getValidationService().validateProperty(object, propertyName, Collections.EMPTY_SET);
            if(constraintViolations.iterator().hasNext())
            {
                HybrisConstraintViolation violation = constraintViolations.iterator().next();
                result = buildCockpitValidationDescriptor(violation);
            }
        }
        catch(IllegalArgumentException e)
        {
            String logMsg = "Attribute " + propertyDescr + " is not accessible for type " + object + " by validation framework";
            LOG.warn(logMsg);
            if(LOG.isDebugEnabled())
            {
                LOG.debug(logMsg, e);
            }
        }
        return result;
    }


    private CockpitValidationDescriptor buildCockpitValidationDescriptor(HybrisConstraintViolation violation)
    {
        PropertyDescriptor propertyDescriptor = null;
        if(!violation.getProperty().isEmpty())
        {
            propertyDescriptor = getCockpitTypeService().getPropertyDescriptor(violation.getQualifier());
        }
        return new CockpitValidationDescriptor(propertyDescriptor, getCockpitMessageLevel(violation.getViolationSeverity()), violation
                        .getLocalizedMessage(), violation.getConstraintModel());
    }


    public ValidationUIHelper getValidationUIHelper()
    {
        return this.validationUIHelper;
    }


    public void setValidationUIHelper(ValidationUIHelper validationUIHelper)
    {
        this.validationUIHelper = validationUIHelper;
    }


    protected TypeService getCockpitTypeService()
    {
        return this.cockpitTypeService;
    }


    @Required
    public void setCockpitTypeService(TypeService cockpitTypeService)
    {
        this.cockpitTypeService = cockpitTypeService;
    }
}
