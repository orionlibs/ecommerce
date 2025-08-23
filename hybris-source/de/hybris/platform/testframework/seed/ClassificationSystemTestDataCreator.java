package de.hybris.platform.testframework.seed;

import de.hybris.platform.catalog.enums.ClassificationAttributeTypeEnum;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.model.ModelService;

public class ClassificationSystemTestDataCreator extends TestDataCreator
{
    public ClassificationSystemTestDataCreator(ModelService modelService)
    {
        super(modelService);
    }


    public ClassificationSystemModel createClassificationSystem(String id)
    {
        ClassificationSystemModel system = (ClassificationSystemModel)getModelService().create(ClassificationSystemModel.class);
        system.setId(id);
        system.setDefaultCatalog(Boolean.TRUE);
        getModelService().save(system);
        return system;
    }


    public ClassificationSystemVersionModel createClassificationSystemVersion(String versionId, ClassificationSystemModel system)
    {
        ClassificationSystemVersionModel version = (ClassificationSystemVersionModel)getModelService().create(ClassificationSystemVersionModel.class);
        version.setVersion(versionId);
        version.setCatalog((CatalogModel)system);
        getModelService().save(version);
        return version;
    }


    public ClassificationClassModel createClassificationClass(String code, ClassificationSystemVersionModel classificationSystemVersion)
    {
        ClassificationClassModel classificationClass = (ClassificationClassModel)getModelService().create(ClassificationClassModel.class);
        classificationClass.setCode(code);
        classificationClass.setCatalogVersion((CatalogVersionModel)classificationSystemVersion);
        getModelService().save(classificationClass);
        return classificationClass;
    }


    public ClassificationAttributeModel createClassificationAttribute(String code, ClassificationSystemVersionModel classificationSystemVersion)
    {
        ClassificationAttributeModel attribute = (ClassificationAttributeModel)getModelService().create(ClassificationAttributeModel.class);
        attribute.setCode(code);
        attribute.setSystemVersion(classificationSystemVersion);
        getModelService().save(attribute);
        return attribute;
    }


    public ClassAttributeAssignmentModel createRefClassAttributeAssignmentWithoutSubtypes(ClassificationAttributeModel attribute, ClassificationClassModel classificationClass, ComposedTypeModel type)
    {
        ClassAttributeAssignmentModel assignment = createRefClassAttributeAssignment(attribute, classificationClass, type);
        assignment.setReferenceIncludesSubTypes(Boolean.FALSE);
        getModelService().save(assignment);
        return assignment;
    }


    public ClassAttributeAssignmentModel createRefClassAttributeAssignment(ClassificationAttributeModel attribute, ClassificationClassModel classificationClass, ComposedTypeModel type)
    {
        ClassAttributeAssignmentModel assignment = createClassAttributeAssignment(attribute, classificationClass);
        assignment.setReferenceType(type);
        assignment.setAttributeType(ClassificationAttributeTypeEnum.REFERENCE);
        getModelService().save(assignment);
        return assignment;
    }


    public ClassAttributeAssignmentModel createClassAttributeAssignment(ClassificationAttributeModel attribute, ClassificationClassModel classificationClass)
    {
        ClassAttributeAssignmentModel assignment = (ClassAttributeAssignmentModel)getModelService().create(ClassAttributeAssignmentModel.class);
        assignment.setClassificationAttribute(attribute);
        assignment.setClassificationClass(classificationClass);
        getModelService().save(assignment);
        return assignment;
    }
}
