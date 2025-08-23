package de.hybris.platform.productcockpit.model.listview.impl;

import de.hybris.platform.cockpit.model.general.ListModel;
import de.hybris.platform.cockpit.model.listview.DynamicColumnProvider;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.config.ColumnConfiguration;
import de.hybris.platform.cockpit.services.config.impl.PropertyColumnConfiguration;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.variants.model.VariantAttributeDescriptorModel;
import de.hybris.platform.variants.model.VariantProductModel;
import de.hybris.platform.variants.model.VariantTypeModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class VariantsDynamicColumnProvider implements DynamicColumnProvider
{
    public List<ColumnConfiguration> getDynamicColums(ListModel listModel)
    {
        List<ColumnConfiguration> result = new ArrayList<>();
        Collection<PropertyDescriptor> descriptors = new HashSet<>();
        if(listModel != null && listModel.getElements() != null)
        {
            List<VariantTypeModel> allVariants = getAllVariantTypes(listModel.getElements());
            for(VariantTypeModel variantType : allVariants)
            {
                for(VariantAttributeDescriptorModel variantAttributeDescriptorModel : variantType.getVariantAttributes())
                {
                    PropertyDescriptor propertyDescriptor = getTypeService().getPropertyDescriptor(variantType
                                    .getCode() + "." + variantType.getCode());
                    if(!descriptors.contains(propertyDescriptor))
                    {
                        PropertyColumnConfiguration propertyColumnConfiguration = new PropertyColumnConfiguration(propertyDescriptor);
                        result.add(propertyColumnConfiguration);
                    }
                    descriptors.add(propertyDescriptor);
                }
            }
        }
        return result;
    }


    private List<VariantTypeModel> getAllVariantTypes(List products)
    {
        List<VariantTypeModel> result = new ArrayList<>();
        for(Object rowItem : products)
        {
            Object model = ((TypedObject)rowItem).getObject();
            if(model instanceof VariantProductModel && ((VariantProductModel)model).getBaseProduct() != null)
            {
                VariantTypeModel variantType = ((VariantProductModel)model).getBaseProduct().getVariantType();
                if(variantType != null)
                {
                    result.add(variantType);
                }
            }
        }
        return result;
    }


    private TypeService getTypeService()
    {
        return UISessionUtils.getCurrentSession().getTypeService();
    }
}
