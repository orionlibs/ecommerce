package de.hybris.platform.productcockpit.services.catalog;

import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.productcockpit.model.macfinder.node.MacFinderTreeNode;
import java.util.Collection;
import java.util.List;

public interface CatalogService
{
    List<CatalogModel> getAvailableCatalogs(UserModel paramUserModel);


    List<CatalogVersionModel> getCatalogVersions(CatalogModel paramCatalogModel, UserModel paramUserModel);


    List<CatalogVersionModel> getAllCatalogVersions(UserModel paramUserModel);


    List<CatalogVersionModel> getAvailableCatalogVersions();


    List<CatalogVersionModel> getSortedCatalogVersions(UserModel paramUserModel);


    List<CategoryModel> getCategories(CatalogVersionModel paramCatalogVersionModel);


    long getCategoryCount(CatalogVersionModel paramCatalogVersionModel);


    List<CategoryModel> getCategories(CategoryModel paramCategoryModel);


    long getCategoryCount(CategoryModel paramCategoryModel);


    CatalogModel getCatalog(CatalogVersionModel paramCatalogVersionModel);


    CatalogVersionModel getCatalogVersion(CategoryModel paramCategoryModel);


    List<CategoryModel> getCategoryPath(CategoryModel paramCategoryModel);


    Collection<String> getSubcategoriesHavingSubcategories(ItemModel paramItemModel);


    CatalogVersionModel getCatalogVersion(PK paramPK);


    List<MacFinderTreeNode> getItems(MacFinderTreeNode paramMacFinderTreeNode1, CatalogVersionModel paramCatalogVersionModel, boolean paramBoolean, MacFinderTreeNode paramMacFinderTreeNode2);


    int getItemCount(MacFinderTreeNode paramMacFinderTreeNode1, CatalogVersionModel paramCatalogVersionModel, boolean paramBoolean, MacFinderTreeNode paramMacFinderTreeNode2);


    boolean isMoveCategoriesPermitted(Collection<TypedObject> paramCollection, CategoryModel paramCategoryModel1, CategoryModel paramCategoryModel2);


    boolean isMoveCategoryPermitted(CategoryModel paramCategoryModel1, CategoryModel paramCategoryModel2, CategoryModel paramCategoryModel3);


    boolean isAssignProductPermitted(TypedObject paramTypedObject, CategoryModel paramCategoryModel1, CategoryModel paramCategoryModel2);


    boolean moveCategory(CategoryModel paramCategoryModel1, CategoryModel paramCategoryModel2, CategoryModel paramCategoryModel3);


    boolean moveCategories(Collection<TypedObject> paramCollection, CategoryModel paramCategoryModel1, CategoryModel paramCategoryModel2);


    boolean setAsRootCategory(CategoryModel paramCategoryModel);


    boolean assignProduct(TypedObject paramTypedObject, CategoryModel paramCategoryModel1, CategoryModel paramCategoryModel2);


    boolean assignProducts(Collection<TypedObject> paramCollection, CategoryModel paramCategoryModel1, CategoryModel paramCategoryModel2);


    boolean assignProduct(TypedObject paramTypedObject, CatalogVersionModel paramCatalogVersionModel, CategoryModel paramCategoryModel);


    CategoryModel wrapCategory(TypedObject paramTypedObject);


    CatalogVersionModel wrapCatalogVersion(TypedObject paramTypedObject);


    TypedObject getCatalogVersionTypedObject(CatalogVersionModel paramCatalogVersionModel);


    List<TypedObject> getSupercategories(TypedObject paramTypedObject, boolean paramBoolean);


    void addToCategories(TypedObject paramTypedObject, List<TypedObject> paramList);


    void removeFromCategories(TypedObject paramTypedObject, List<TypedObject> paramList);
}
