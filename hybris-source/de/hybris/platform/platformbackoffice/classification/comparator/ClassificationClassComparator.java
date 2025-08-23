package de.hybris.platform.platformbackoffice.classification.comparator;

import com.hybris.cockpitng.core.util.CockpitProperties;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public class ClassificationClassComparator implements Comparator<ClassificationClassModel>
{
    private static final String CONFIG_KEY_DEEP_SORT = "classification-tab-renderer.deep-sorting.enabled";
    private CockpitProperties cockpitProperties;


    public int compare(ClassificationClassModel leftObject, ClassificationClassModel rightObject)
    {
        int result = comapreByOrder(leftObject, rightObject);
        if(result != 0)
        {
            return result;
        }
        result = compareByCatalogs(leftObject, rightObject);
        if(result != 0)
        {
            return result;
        }
        result = comapreByDirectCategories(leftObject, rightObject);
        if(result != 0)
        {
            return result;
        }
        if(shouldSortByAllSubcategories())
        {
            result = compareByAllCategories(leftObject, rightObject);
            if(result != 0)
            {
                return result;
            }
        }
        return compareByNameOrCode(leftObject, rightObject);
    }


    private int comapreByOrder(ClassificationClassModel leftObject, ClassificationClassModel rightObject)
    {
        if(leftObject.getOrder() != null && rightObject.getOrder() != null &&
                        !leftObject.getOrder().equals(rightObject.getOrder()))
        {
            return leftObject.getOrder().compareTo(rightObject.getOrder());
        }
        if(leftObject.getOrder() != null && rightObject.getOrder() == null)
        {
            return -1;
        }
        if(leftObject.getOrder() == null && rightObject.getOrder() != null)
        {
            return 1;
        }
        return 0;
    }


    private int compareByCatalogs(ClassificationClassModel leftObject, ClassificationClassModel rightObject)
    {
        ClassificationSystemVersionModel leftObjectCatalogVersion = leftObject.getCatalogVersion();
        ClassificationSystemVersionModel rightObjectCatalogVersion = rightObject.getCatalogVersion();
        if(!Objects.equals(leftObjectCatalogVersion.getCatalog(), rightObjectCatalogVersion.getCatalog()))
        {
            return Objects.compare(leftObjectCatalogVersion.getCatalog().getName(), rightObjectCatalogVersion.getCatalog().getName(),
                            Comparator.naturalOrder());
        }
        if(!Objects.equals(leftObjectCatalogVersion, rightObjectCatalogVersion))
        {
            return Objects.compare(leftObjectCatalogVersion.getVersion(), rightObjectCatalogVersion.getVersion(),
                            Comparator.naturalOrder());
        }
        return 0;
    }


    private int comapreByDirectCategories(ClassificationClassModel leftObject, ClassificationClassModel rightObject)
    {
        List<CategoryModel> leftObjectCategories = leftObject.getCategories();
        if(leftObjectCategories != null && leftObjectCategories.contains(rightObject))
        {
            return -1;
        }
        List<CategoryModel> rightObjectCategories = rightObject.getCategories();
        if(rightObjectCategories != null && rightObjectCategories.contains(leftObject))
        {
            return 1;
        }
        return 0;
    }


    private int compareByAllCategories(ClassificationClassModel leftObject, ClassificationClassModel rightObject)
    {
        Collection<CategoryModel> leftObjectAllSubcategories = leftObject.getAllSubcategories();
        if(leftObjectAllSubcategories != null && leftObjectAllSubcategories.contains(rightObject))
        {
            return -1;
        }
        Collection<CategoryModel> rightObjectAllSubcategories = rightObject.getAllSubcategories();
        if(rightObjectAllSubcategories != null && rightObjectAllSubcategories.contains(leftObject))
        {
            return 1;
        }
        return 0;
    }


    private int compareByNameOrCode(ClassificationClassModel leftObject, ClassificationClassModel rightObject)
    {
        if(StringUtils.equals(rightObject.getName(), leftObject.getName()))
        {
            return StringUtils.compareIgnoreCase(leftObject.getCode(), rightObject.getCode());
        }
        return getNameOrCode(leftObject).compareToIgnoreCase(getNameOrCode(rightObject));
    }


    private String getNameOrCode(ClassificationClassModel classificationClassModel)
    {
        return (classificationClassModel.getName() != null) ? classificationClassModel.getName() : classificationClassModel.getCode();
    }


    private boolean shouldSortByAllSubcategories()
    {
        return getCockpitProperties().getBoolean("classification-tab-renderer.deep-sorting.enabled", true);
    }


    protected CockpitProperties getCockpitProperties()
    {
        return this.cockpitProperties;
    }


    @Required
    public void setCockpitProperties(CockpitProperties cockpitProperties)
    {
        this.cockpitProperties = cockpitProperties;
    }
}
