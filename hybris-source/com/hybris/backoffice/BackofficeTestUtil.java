package com.hybris.backoffice;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.CollectionTypeModel;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import java.lang.reflect.Field;
import org.mockito.Mockito;

public class BackofficeTestUtil
{
    public static void setPk(AbstractItemModel itemModel, long pk)
    {
        try
        {
            Field field = itemModel.getItemModelContext().getClass().getDeclaredField("pk");
            field.setAccessible(true);
            field.set(itemModel.getItemModelContext(), PK.fromLong(pk));
        }
        catch(NoSuchFieldException | IllegalAccessException e)
        {
            throw new RuntimeException("Cannot set pk on itemModel: " + itemModel, e);
        }
    }


    public static AttributeDescriptorModel mockAttributeDescriptor(String typecode)
    {
        AttributeDescriptorModel attributeDescriptor = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        TypeModel typeModel = (TypeModel)Mockito.mock(TypeModel.class);
        Mockito.lenient().when(typeModel.getCode()).thenReturn(typecode);
        Mockito.lenient().when(attributeDescriptor.getAttributeType()).thenReturn(typeModel);
        return attributeDescriptor;
    }


    public static AttributeDescriptorModel mockCollectionTypeAttributeDescriptor(String typecode)
    {
        AttributeDescriptorModel attributeDescriptor = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        CollectionTypeModel collectionType = (CollectionTypeModel)Mockito.mock(CollectionTypeModel.class);
        Mockito.lenient().when(collectionType.getCode()).thenReturn("CollectionType");
        Mockito.when(attributeDescriptor.getAttributeType()).thenReturn(collectionType);
        TypeModel typeModel = (TypeModel)Mockito.mock(TypeModel.class);
        Mockito.when(typeModel.getCode()).thenReturn(typecode);
        Mockito.when(collectionType.getElementType()).thenReturn(typeModel);
        return attributeDescriptor;
    }
}
