package de.hybris.platform.servicelayer.type;

import de.hybris.platform.core.HybrisEnumValue;
import de.hybris.platform.core.model.enumeration.EnumerationMetaTypeModel;
import de.hybris.platform.core.model.enumeration.EnumerationValueModel;
import de.hybris.platform.core.model.type.AtomicTypeModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.type.TypeModel;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import org.slf4j.LoggerFactory;

public interface TypeService
{
    @Deprecated(since = "ages", forRemoval = true)
    ComposedTypeModel getComposedType(String paramString);


    ComposedTypeModel getComposedTypeForCode(String paramString);


    @Deprecated(since = "ages", forRemoval = true)
    ComposedTypeModel getComposedType(Class paramClass);


    ComposedTypeModel getComposedTypeForClass(Class paramClass);


    AttributeDescriptorModel getAttributeDescriptor(ComposedTypeModel paramComposedTypeModel, String paramString);


    AttributeDescriptorModel getAttributeDescriptor(String paramString1, String paramString2);


    @Deprecated(since = "ages", forRemoval = true)
    Set<AttributeDescriptorModel> getAttributeDescriptors(ComposedTypeModel paramComposedTypeModel);


    Set<AttributeDescriptorModel> getAttributeDescriptorsForType(ComposedTypeModel paramComposedTypeModel);


    @Deprecated(since = "ages", forRemoval = true)
    Set<AttributeDescriptorModel> getAttributeDescriptors(String paramString);


    Set<AttributeDescriptorModel> getInitialAttributeDescriptorsForType(ComposedTypeModel paramComposedTypeModel);


    @Deprecated(since = "ages", forRemoval = true)
    AtomicTypeModel getAtomicType(String paramString);


    AtomicTypeModel getAtomicTypeForCode(String paramString);


    AtomicTypeModel getAtomicTypeForJavaClass(Class paramClass);


    @Deprecated(since = "ages", forRemoval = true)
    TypeModel getType(String paramString);


    TypeModel getTypeForCode(String paramString);


    @Deprecated(since = "ages", forRemoval = true)
    EnumerationMetaTypeModel getEnumerationType(String paramString);


    EnumerationMetaTypeModel getEnumerationTypeForCode(String paramString);


    EnumerationValueModel getEnumerationValue(String paramString1, String paramString2);


    EnumerationValueModel getEnumerationValue(HybrisEnumValue paramHybrisEnumValue);


    Set<String> getMandatoryAttributes(String paramString, boolean paramBoolean);


    Set<String> getUniqueAttributes(String paramString);


    String getUniqueModelRootType(String paramString);


    Map<String, Object> getDefaultValues(String paramString);


    Map<String, Object> getDefaultValues(String paramString, Collection<String> paramCollection);


    boolean isAssignableFrom(TypeModel paramTypeModel1, TypeModel paramTypeModel2);


    boolean isAssignableFrom(String paramString1, String paramString2);


    boolean isInstance(TypeModel paramTypeModel, Object paramObject);


    Set<String> getPartOfAttributes(String paramString);


    boolean hasAttribute(ComposedTypeModel paramComposedTypeModel, String paramString);


    <T extends de.hybris.platform.core.model.ItemModel> Class<T> getModelClass(ComposedTypeModel paramComposedTypeModel);


    <T extends de.hybris.platform.core.model.ItemModel> Class<T> getModelClass(String paramString);


    Set<AttributeDescriptorModel> getAttributesForModifiers(String paramString, AttributeModifierCriteria paramAttributeModifierCriteria);


    default Collection<AttributeDescriptorModel> getRuntimeAttributeDescriptorsForType(ComposedTypeModel composedType)
    {
        LoggerFactory.getLogger(TypeService.class).info("Collection<AttributeDescriptorModel> getRuntimeAttributeDescriptorsForType(final ComposedTypeModel composedType) is not implemented.");
        return Collections.emptyList();
    }


    default Boolean isRuntimeAttribute(AttributeDescriptorModel attributeDescriptorModel)
    {
        LoggerFactory.getLogger(TypeService.class).info("Boolean isRuntimeAttribute(final AttributeDescriptorModel attributeDescriptorModel) is not implemented.");
        return Boolean.FALSE;
    }
}
