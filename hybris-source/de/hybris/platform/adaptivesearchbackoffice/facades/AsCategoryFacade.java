package de.hybris.platform.adaptivesearchbackoffice.facades;

import de.hybris.platform.adaptivesearchbackoffice.data.AsCategoryData;
import java.util.List;

public interface AsCategoryFacade
{
    AsCategoryData getCategoryHierarchy();


    AsCategoryData getCategoryHierarchy(String paramString1, String paramString2);


    List<AsCategoryData> buildCategoryBreadcrumbs(List<String> paramList);


    List<AsCategoryData> buildCategoryBreadcrumbs(String paramString1, String paramString2, List<String> paramList);
}
