package de.hybris.platform.cockpit.services.validation;

import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.services.validation.pojos.CockpitValidationDescriptor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.validation.enums.Severity;
import de.hybris.platform.validation.exceptions.HybrisConstraintViolation;
import java.util.Set;

public interface CockpitValidationService
{
    String buildFormattedValidationMessages(Set<CockpitValidationDescriptor> paramSet);


    String buildValidationMessages(Set<CockpitValidationDescriptor> paramSet, String paramString);


    boolean containsLevel(Set<CockpitValidationDescriptor> paramSet, int paramInt);


    Set<CockpitValidationDescriptor> convertToValidationDescriptors(Set<HybrisConstraintViolation> paramSet);


    Set<CockpitValidationDescriptor> filterByMessageLevel(Set<CockpitValidationDescriptor> paramSet, int paramInt);


    int getCockpitMessageLevel(Severity paramSeverity);


    int getHighestMessageLevel(Set<CockpitValidationDescriptor> paramSet);


    Set<CockpitValidationDescriptor> getTypeValidationDescriptors(Set<CockpitValidationDescriptor> paramSet);


    Set<CockpitValidationDescriptor> getValidationDescriptors(Set<CockpitValidationDescriptor> paramSet, PropertyDescriptor paramPropertyDescriptor);


    Set<CockpitValidationDescriptor> validateModel(ItemModel paramItemModel);


    <T> CockpitValidationDescriptor validateProperty(T paramT, PropertyDescriptor paramPropertyDescriptor);
}
