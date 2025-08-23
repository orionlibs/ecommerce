package de.hybris.platform.catalog.model.classification;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.List;

public class ClassificationClassModel extends CategoryModel
{
    public static final String _TYPECODE = "ClassificationClass";
    public static final String EXTERNALID = "externalID";
    public static final String REVISION = "revision";
    public static final String SHOWEMPTYATTRIBUTES = "showEmptyAttributes";
    public static final String DECLAREDCLASSIFICATIONATTRIBUTES = "declaredClassificationAttributes";
    public static final String INHERITEDCLASSIFICATIONATTRIBUTES = "inheritedClassificationAttributes";
    public static final String CLASSIFICATIONATTRIBUTES = "classificationAttributes";
    public static final String DECLAREDCLASSIFICATIONATTRIBUTEASSIGNMENTS = "declaredClassificationAttributeAssignments";
    public static final String HMCXML = "hmcXML";
    public static final String ALLCLASSIFICATIONATTRIBUTEASSIGNMENTS = "allClassificationAttributeAssignments";


    public ClassificationClassModel()
    {
    }


    public ClassificationClassModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ClassificationClassModel(ClassificationSystemVersionModel _catalogVersion, String _code)
    {
        setCatalogVersion((CatalogVersionModel)_catalogVersion);
        setCode(_code);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ClassificationClassModel(ClassificationSystemVersionModel _catalogVersion, String _code, ItemModel _owner)
    {
        setCatalogVersion((CatalogVersionModel)_catalogVersion);
        setCode(_code);
        setOwner(_owner);
    }


    @Accessor(qualifier = "allClassificationAttributeAssignments", type = Accessor.Type.GETTER)
    public List<ClassAttributeAssignmentModel> getAllClassificationAttributeAssignments()
    {
        return (List<ClassAttributeAssignmentModel>)getPersistenceContext().getDynamicValue((AbstractItemModel)this, "allClassificationAttributeAssignments");
    }


    @Accessor(qualifier = "catalogVersion", type = Accessor.Type.GETTER)
    public ClassificationSystemVersionModel getCatalogVersion()
    {
        return (ClassificationSystemVersionModel)super.getCatalogVersion();
    }


    @Accessor(qualifier = "classificationAttributes", type = Accessor.Type.GETTER)
    public List<ClassificationAttributeModel> getClassificationAttributes()
    {
        return (List<ClassificationAttributeModel>)getPersistenceContext().getPropertyValue("classificationAttributes");
    }


    @Accessor(qualifier = "declaredClassificationAttributeAssignments", type = Accessor.Type.GETTER)
    public List<ClassAttributeAssignmentModel> getDeclaredClassificationAttributeAssignments()
    {
        return (List<ClassAttributeAssignmentModel>)getPersistenceContext().getPropertyValue("declaredClassificationAttributeAssignments");
    }


    @Accessor(qualifier = "declaredClassificationAttributes", type = Accessor.Type.GETTER)
    public List<ClassificationAttributeModel> getDeclaredClassificationAttributes()
    {
        return (List<ClassificationAttributeModel>)getPersistenceContext().getPropertyValue("declaredClassificationAttributes");
    }


    @Accessor(qualifier = "externalID", type = Accessor.Type.GETTER)
    public String getExternalID()
    {
        return (String)getPersistenceContext().getPropertyValue("externalID");
    }


    @Accessor(qualifier = "hmcXML", type = Accessor.Type.GETTER)
    public String getHmcXML()
    {
        return (String)getPersistenceContext().getPropertyValue("hmcXML");
    }


    @Accessor(qualifier = "inheritedClassificationAttributes", type = Accessor.Type.GETTER)
    public List<ClassificationAttributeModel> getInheritedClassificationAttributes()
    {
        return (List<ClassificationAttributeModel>)getPersistenceContext().getPropertyValue("inheritedClassificationAttributes");
    }


    @Accessor(qualifier = "revision", type = Accessor.Type.GETTER)
    public String getRevision()
    {
        return (String)getPersistenceContext().getPropertyValue("revision");
    }


    @Accessor(qualifier = "showEmptyAttributes", type = Accessor.Type.GETTER)
    public Boolean getShowEmptyAttributes()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("showEmptyAttributes");
    }


    @Accessor(qualifier = "catalogVersion", type = Accessor.Type.SETTER)
    public void setCatalogVersion(CatalogVersionModel value)
    {
        if(value == null || value instanceof ClassificationSystemVersionModel)
        {
            super.setCatalogVersion(value);
        }
        else
        {
            throw new IllegalArgumentException("Given value is not instance of de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel");
        }
    }


    @Accessor(qualifier = "declaredClassificationAttributeAssignments", type = Accessor.Type.SETTER)
    public void setDeclaredClassificationAttributeAssignments(List<ClassAttributeAssignmentModel> value)
    {
        getPersistenceContext().setPropertyValue("declaredClassificationAttributeAssignments", value);
    }


    @Accessor(qualifier = "externalID", type = Accessor.Type.SETTER)
    public void setExternalID(String value)
    {
        getPersistenceContext().setPropertyValue("externalID", value);
    }


    @Accessor(qualifier = "hmcXML", type = Accessor.Type.SETTER)
    public void setHmcXML(String value)
    {
        getPersistenceContext().setPropertyValue("hmcXML", value);
    }


    @Accessor(qualifier = "revision", type = Accessor.Type.SETTER)
    public void setRevision(String value)
    {
        getPersistenceContext().setPropertyValue("revision", value);
    }


    @Accessor(qualifier = "showEmptyAttributes", type = Accessor.Type.SETTER)
    public void setShowEmptyAttributes(Boolean value)
    {
        getPersistenceContext().setPropertyValue("showEmptyAttributes", value);
    }
}
