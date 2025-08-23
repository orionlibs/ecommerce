package de.hybris.platform.integrationservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class IntegrationObjectItemClassificationAttributeModel extends AbstractIntegrationObjectItemAttributeModel
{
    public static final String _TYPECODE = "IntegrationObjectItemClassificationAttribute";
    public static final String _INTEGOBJITEM2CLASSIFICATIONINTEGOBJITEMATTR = "IntegObjItem2ClassificationIntegObjItemAttr";
    public static final String CLASSATTRIBUTEASSIGNMENT = "classAttributeAssignment";
    public static final String INTEGRATIONOBJECTITEM = "integrationObjectItem";


    public IntegrationObjectItemClassificationAttributeModel()
    {
    }


    public IntegrationObjectItemClassificationAttributeModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public IntegrationObjectItemClassificationAttributeModel(String _attributeName, ClassAttributeAssignmentModel _classAttributeAssignment, IntegrationObjectItemModel _integrationObjectItem)
    {
        setAttributeName(_attributeName);
        setClassAttributeAssignment(_classAttributeAssignment);
        setIntegrationObjectItem(_integrationObjectItem);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public IntegrationObjectItemClassificationAttributeModel(String _attributeName, ClassAttributeAssignmentModel _classAttributeAssignment, IntegrationObjectItemModel _integrationObjectItem, ItemModel _owner)
    {
        setAttributeName(_attributeName);
        setClassAttributeAssignment(_classAttributeAssignment);
        setIntegrationObjectItem(_integrationObjectItem);
        setOwner(_owner);
    }


    @Accessor(qualifier = "classAttributeAssignment", type = Accessor.Type.GETTER)
    public ClassAttributeAssignmentModel getClassAttributeAssignment()
    {
        return (ClassAttributeAssignmentModel)getPersistenceContext().getPropertyValue("classAttributeAssignment");
    }


    @Accessor(qualifier = "integrationObjectItem", type = Accessor.Type.GETTER)
    public IntegrationObjectItemModel getIntegrationObjectItem()
    {
        return (IntegrationObjectItemModel)getPersistenceContext().getPropertyValue("integrationObjectItem");
    }


    @Accessor(qualifier = "classAttributeAssignment", type = Accessor.Type.SETTER)
    public void setClassAttributeAssignment(ClassAttributeAssignmentModel value)
    {
        getPersistenceContext().setPropertyValue("classAttributeAssignment", value);
    }


    @Accessor(qualifier = "integrationObjectItem", type = Accessor.Type.SETTER)
    public void setIntegrationObjectItem(IntegrationObjectItemModel value)
    {
        getPersistenceContext().setPropertyValue("integrationObjectItem", value);
    }
}
