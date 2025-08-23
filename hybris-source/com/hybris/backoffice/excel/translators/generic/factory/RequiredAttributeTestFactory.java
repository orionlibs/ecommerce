package com.hybris.backoffice.excel.translators.generic.factory;

import com.hybris.backoffice.excel.translators.generic.RequiredAttribute;
import de.hybris.platform.core.model.type.AtomicTypeModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.type.TypeModel;

public class RequiredAttributeTestFactory
{
    public static RequiredAttribute prepareStructureForCatalog()
    {
        RequiredAttribute catalogOfCatalogVersion = new RequiredAttribute((TypeModel)prepareComposedType("Catalog"), "CatalogVersion", "catalog", true, true, false);
        RequiredAttribute idOfCatalog = new RequiredAttribute((TypeModel)prepareAtomicTypeModel(String.class.getName()), "Catalog", "id", true, true, false);
        catalogOfCatalogVersion.addChild(idOfCatalog);
        return catalogOfCatalogVersion;
    }


    public static RequiredAttribute prepareStructureForCatalogVersion()
    {
        return prepareStructureForCatalogVersion(false, false);
    }


    public static RequiredAttribute prepareStructureForCatalogVersion(boolean mandatory, boolean unique)
    {
        RequiredAttribute catalogVersionOfProduct = new RequiredAttribute((TypeModel)prepareComposedType("CatalogVersion"), "Product", "catalogVersion", unique, mandatory, false);
        RequiredAttribute versionOfCatalogVersion = new RequiredAttribute((TypeModel)prepareAtomicTypeModel(String.class.getName()), "CatalogVersion", "version", false, false, false);
        RequiredAttribute catalogOfCatalogVersion = new RequiredAttribute((TypeModel)prepareComposedType("Catalog"), "CatalogVersion", "catalog", false, false, false);
        RequiredAttribute idOfCatalog = new RequiredAttribute((TypeModel)prepareAtomicTypeModel(String.class.getName()), "Catalog", "id", false, false, false);
        catalogOfCatalogVersion.addChild(idOfCatalog);
        catalogVersionOfProduct.addChild(versionOfCatalogVersion);
        catalogVersionOfProduct.addChild(catalogOfCatalogVersion);
        return catalogVersionOfProduct;
    }


    public static RequiredAttribute prepareStructureForSupercategories()
    {
        return prepareStructureForSupercategories(false, false);
    }


    public static RequiredAttribute prepareStructureForSupercategories(boolean unique, boolean mandatory)
    {
        RequiredAttribute supercategoriesOfProduct = new RequiredAttribute((TypeModel)prepareComposedType("Category"), "Product", "supercategories", false, false, false);
        RequiredAttribute codeOfCategory = new RequiredAttribute((TypeModel)prepareAtomicTypeModel(String.class.getName()), "Category", "code", false, false, false);
        RequiredAttribute catalogVersionOfCategory = new RequiredAttribute((TypeModel)prepareComposedType("CatalogVersion"), "Category", "catalogVersion", false, false, false);
        RequiredAttribute versionOfCatalogVersion = new RequiredAttribute((TypeModel)prepareAtomicTypeModel(String.class.getName()), "CatalogVersion", "version", false, false, false);
        RequiredAttribute catalogOfCatalogVersion = new RequiredAttribute((TypeModel)prepareComposedType("Catalog"), "CatalogVersion", "catalog", false, false, false);
        RequiredAttribute idOfCatalog = new RequiredAttribute((TypeModel)prepareAtomicTypeModel(String.class.getName()), "Catalog", "id", false, false, false);
        supercategoriesOfProduct.addChild(codeOfCategory);
        supercategoriesOfProduct.addChild(catalogVersionOfCategory);
        catalogVersionOfCategory.addChild(versionOfCatalogVersion);
        catalogVersionOfCategory.addChild(catalogOfCatalogVersion);
        catalogOfCatalogVersion.addChild(idOfCatalog);
        return supercategoriesOfProduct;
    }


    public static RequiredAttribute prepareStructureForPrices()
    {
        RequiredAttribute pricesOfProduct = new RequiredAttribute(null, "Product", "europe1Prices", true, false, false);
        RequiredAttribute codeOfUnit = new RequiredAttribute(null, "Unit", "code", true, true, false);
        RequiredAttribute currencyOfPriceRow = new RequiredAttribute(null, "PriceRow", "currency", true, true, false);
        RequiredAttribute priceValueOfPriceRow = new RequiredAttribute(null, "PriceRow", "price", true, true, false);
        RequiredAttribute isoCodeOfCurrency = new RequiredAttribute(null, "Currency", "isocode", true, true, false);
        RequiredAttribute unitOfPriceRow = new RequiredAttribute(null, "PriceRow", "unit", true, false, false);
        pricesOfProduct.addChild(priceValueOfPriceRow);
        pricesOfProduct.addChild(unitOfPriceRow);
        pricesOfProduct.addChild(currencyOfPriceRow);
        unitOfPriceRow.addChild(codeOfUnit);
        currencyOfPriceRow.addChild(isoCodeOfCurrency);
        return pricesOfProduct;
    }


    private static ComposedTypeModel prepareComposedType(String code)
    {
        ComposedTypeModel composedType = new ComposedTypeModel();
        composedType.setCode(code);
        return composedType;
    }


    private static AtomicTypeModel prepareAtomicTypeModel(String code)
    {
        AtomicTypeModel atomicTypeModel = new AtomicTypeModel();
        atomicTypeModel.setCode(code);
        return atomicTypeModel;
    }
}
