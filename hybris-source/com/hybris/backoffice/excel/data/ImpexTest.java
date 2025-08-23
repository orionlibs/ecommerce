package com.hybris.backoffice.excel.data;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class ImpexTest
{
    private static final String PRODUCT_TYPE_CODE = "Product";
    private static final String CATEGORY_TYPE_CODE = "Category";
    private static final String APPROVAL_STATUS_CHECK = "Check";
    private static final String FIRST_PRODUCT_NAME = "Jeans";
    private static final String SECOND_PRODUCT_NAME = "Blue Jeans";
    private static final String CATEGORY_NAME = "Blue";
    private static final String CATEGORY_VERSION = "version(code, catalog(id))";
    private static final String APPROVAL_STATUS_APPROVED = "Approved";
    private static final Integer FIRST_ROW_INDEX = Integer.valueOf(0);
    private static final Integer SECOND_ROW_INDEX = Integer.valueOf(1);
    private static final ImpexHeaderValue PRODUCT_HEADER_NAME = new ImpexHeaderValue("name", false);
    private static final ImpexHeaderValue PRODUCT_HEADER_APPROVAL_STATUS = new ImpexHeaderValue("approvalStatus", false);
    private static final ImpexHeaderValue CATEGORY_HEADER_CODE = new ImpexHeaderValue("code", false);
    private static final ImpexHeaderValue CATEGORY_HEADER_VERSION = new ImpexHeaderValue("version(code, catalog(id))", false);


    @Test
    public void shouldFindUpdatesForTypeCode()
    {
        Impex impex = new Impex();
        ImpexForType expectedImpexForType = impex.createNewImpex("aTypeCode");
        impex.createNewImpex("otherTypeCode");
        ImpexForType result = impex.findUpdates("aTypeCode");
        Assertions.assertThat(result).isEqualTo(expectedImpexForType);
    }


    @Test
    public void shouldMergeImpexWhenMainImpexIsEmpty()
    {
        Impex mainImpex = new Impex();
        Impex subImpex = new Impex();
        ImpexForType impexForType = subImpex.findUpdates("Product");
        impexForType.putValue(FIRST_ROW_INDEX, PRODUCT_HEADER_NAME, "Jeans");
        mainImpex.mergeImpex(subImpex, "Product", FIRST_ROW_INDEX);
        Assertions.assertThat(mainImpex.getImpexes()).hasSize(1);
        Assertions.assertThat(((ImpexForType)mainImpex.getImpexes().get(0)).getTypeCode()).isEqualTo("Product");
        Assertions.assertThat(((ImpexForType)mainImpex.getImpexes().get(0)).getImpexTable().rowKeySet().size()).isEqualTo(1);
        Assertions.assertThat(((ImpexForType)mainImpex.getImpexes().get(0)).getImpexTable().columnKeySet().size()).isEqualTo(1);
        Assertions.assertThat(((ImpexForType)mainImpex.getImpexes().get(0)).getImpexTable().get(FIRST_ROW_INDEX, PRODUCT_HEADER_NAME))
                        .isEqualTo("Jeans");
    }


    @Test
    public void shouldMergeImpexForTheSameTypeCodeForExistingRow()
    {
        Impex mainImpex = new Impex();
        ImpexForType mainImpexForType = mainImpex.findUpdates("Product");
        mainImpexForType.putValue(FIRST_ROW_INDEX, PRODUCT_HEADER_NAME, "Jeans");
        Impex subImpex = new Impex();
        ImpexForType impexForType = subImpex.findUpdates("Product");
        impexForType.putValue(FIRST_ROW_INDEX, PRODUCT_HEADER_APPROVAL_STATUS, "Approved");
        mainImpex.mergeImpex(subImpex, "Product", FIRST_ROW_INDEX);
        Assertions.assertThat(mainImpex.getImpexes()).hasSize(1);
        Assertions.assertThat(((ImpexForType)mainImpex.getImpexes().get(FIRST_ROW_INDEX.intValue())).getTypeCode()).isEqualTo("Product");
        Assertions.assertThat(((ImpexForType)mainImpex.getImpexes().get(FIRST_ROW_INDEX.intValue())).getImpexTable().rowKeySet().size()).isEqualTo(1);
        Assertions.assertThat(((ImpexForType)mainImpex.getImpexes().get(FIRST_ROW_INDEX.intValue())).getImpexTable().columnKeySet().size()).isEqualTo(2);
        Assertions.assertThat(((ImpexForType)mainImpex.getImpexes().get(FIRST_ROW_INDEX.intValue())).getImpexTable().get(FIRST_ROW_INDEX, PRODUCT_HEADER_NAME))
                        .isEqualTo("Jeans");
        Assertions.assertThat(((ImpexForType)mainImpex.getImpexes().get(FIRST_ROW_INDEX.intValue())).getImpexTable().get(FIRST_ROW_INDEX, PRODUCT_HEADER_APPROVAL_STATUS))
                        .isEqualTo("Approved");
    }


    @Test
    public void shouldMergeImpexForTheSameTypeCodeForNextRow()
    {
        Impex mainImpex = new Impex();
        ImpexForType mainImpexForType = mainImpex.findUpdates("Product");
        mainImpexForType.setOrder(100);
        mainImpexForType.putValue(FIRST_ROW_INDEX, PRODUCT_HEADER_NAME, "Jeans");
        mainImpexForType.putValue(FIRST_ROW_INDEX, PRODUCT_HEADER_APPROVAL_STATUS, "Approved");
        Impex subImpex = new Impex();
        ImpexForType impexForType = subImpex.findUpdates("Product");
        impexForType.putValue(SECOND_ROW_INDEX, PRODUCT_HEADER_NAME, "Blue Jeans");
        mainImpex.mergeImpex(subImpex, "Product", SECOND_ROW_INDEX);
        Assertions.assertThat(mainImpex.getImpexes()).hasSize(1);
        Assertions.assertThat(((ImpexForType)mainImpex.getImpexes().get(FIRST_ROW_INDEX.intValue())).getTypeCode()).isEqualTo("Product");
        Assertions.assertThat(((ImpexForType)mainImpex.getImpexes().get(FIRST_ROW_INDEX.intValue())).getImpexTable().rowKeySet().size()).isEqualTo(2);
        Assertions.assertThat(((ImpexForType)mainImpex.getImpexes().get(FIRST_ROW_INDEX.intValue())).getImpexTable().columnKeySet().size()).isEqualTo(2);
        Assertions.assertThat(((ImpexForType)mainImpex.getImpexes().get(FIRST_ROW_INDEX.intValue())).getImpexTable().get(FIRST_ROW_INDEX, PRODUCT_HEADER_NAME))
                        .isEqualTo("Jeans");
        Assertions.assertThat(((ImpexForType)mainImpex.getImpexes().get(FIRST_ROW_INDEX.intValue())).getImpexTable().get(FIRST_ROW_INDEX, PRODUCT_HEADER_APPROVAL_STATUS))
                        .isEqualTo("Approved");
        Assertions.assertThat(((ImpexForType)mainImpex.getImpexes().get(FIRST_ROW_INDEX.intValue())).getImpexTable().get(SECOND_ROW_INDEX, PRODUCT_HEADER_NAME))
                        .isEqualTo("Blue Jeans");
    }


    @Test
    public void shouldMergeImpexForDifferentTypeCode()
    {
        Impex mainImpex = new Impex();
        ImpexForType mainProductImpex = mainImpex.findUpdates("Product");
        mainProductImpex.putValue(FIRST_ROW_INDEX, PRODUCT_HEADER_NAME, "Jeans");
        mainProductImpex.putValue(FIRST_ROW_INDEX, PRODUCT_HEADER_APPROVAL_STATUS, "Approved");
        Impex subImpex = new Impex();
        ImpexForType productImpex = subImpex.findUpdates("Product");
        productImpex.setOrder(100);
        productImpex.putValue(FIRST_ROW_INDEX, PRODUCT_HEADER_NAME, "Blue Jeans");
        ImpexForType categorySubImpex = subImpex.findUpdates("Category");
        categorySubImpex.putValue(FIRST_ROW_INDEX, CATEGORY_HEADER_CODE, "Blue");
        categorySubImpex.putValue(FIRST_ROW_INDEX, CATEGORY_HEADER_VERSION, "version(code, catalog(id))");
        mainImpex.mergeImpex(subImpex, "Product", SECOND_ROW_INDEX);
        Assertions.assertThat(mainImpex.getImpexes()).hasSize(2);
        Assertions.assertThat(((ImpexForType)mainImpex.getImpexes().get(FIRST_ROW_INDEX.intValue())).getTypeCode()).isEqualTo("Category");
        Assertions.assertThat(((ImpexForType)mainImpex.getImpexes().get(FIRST_ROW_INDEX.intValue())).getImpexTable().rowKeySet().size()).isEqualTo(1);
        Assertions.assertThat(((ImpexForType)mainImpex.getImpexes().get(FIRST_ROW_INDEX.intValue())).getImpexTable().columnKeySet().size()).isEqualTo(2);
        Assertions.assertThat(((ImpexForType)mainImpex.getImpexes().get(FIRST_ROW_INDEX.intValue())).getImpexTable().get(FIRST_ROW_INDEX, CATEGORY_HEADER_CODE))
                        .isEqualTo("Blue");
        Assertions.assertThat(((ImpexForType)mainImpex.getImpexes().get(FIRST_ROW_INDEX.intValue())).getImpexTable().get(FIRST_ROW_INDEX, CATEGORY_HEADER_VERSION))
                        .isEqualTo("version(code, catalog(id))");
        Assertions.assertThat(((ImpexForType)mainImpex.getImpexes().get(SECOND_ROW_INDEX.intValue())).getTypeCode()).isEqualTo("Product");
        Assertions.assertThat(((ImpexForType)mainImpex.getImpexes().get(SECOND_ROW_INDEX.intValue())).getImpexTable().rowKeySet().size()).isEqualTo(2);
        Assertions.assertThat(((ImpexForType)mainImpex.getImpexes().get(SECOND_ROW_INDEX.intValue())).getImpexTable().columnKeySet().size()).isEqualTo(2);
        Assertions.assertThat(((ImpexForType)mainImpex.getImpexes().get(SECOND_ROW_INDEX.intValue())).getImpexTable().get(FIRST_ROW_INDEX, PRODUCT_HEADER_NAME))
                        .isEqualTo("Jeans");
        Assertions.assertThat(((ImpexForType)mainImpex
                                        .getImpexes().get(SECOND_ROW_INDEX.intValue())).getImpexTable().get(FIRST_ROW_INDEX, PRODUCT_HEADER_APPROVAL_STATUS))
                        .isEqualTo("Approved");
        Assertions.assertThat(((ImpexForType)mainImpex.getImpexes().get(SECOND_ROW_INDEX.intValue())).getImpexTable().get(SECOND_ROW_INDEX, PRODUCT_HEADER_NAME))
                        .isEqualTo("Blue Jeans");
    }


    @Test
    public void shouldMergeTwoImpexes()
    {
        Impex mainImpex = new Impex();
        ImpexForType mainProductImpex = mainImpex.findUpdates("Product");
        mainProductImpex.setOrder(100);
        mainProductImpex.putValue(FIRST_ROW_INDEX, PRODUCT_HEADER_NAME, "Jeans");
        mainProductImpex.putValue(FIRST_ROW_INDEX, PRODUCT_HEADER_APPROVAL_STATUS, "Approved");
        Impex subImpex = new Impex();
        ImpexForType categoryImpex = subImpex.findUpdates("Category");
        categoryImpex.putValue(FIRST_ROW_INDEX, CATEGORY_HEADER_CODE, "Blue");
        categoryImpex.putValue(FIRST_ROW_INDEX, CATEGORY_HEADER_VERSION, "version(code, catalog(id))");
        ImpexForType productSubImpex = subImpex.findUpdates("Product");
        productSubImpex.putValue(FIRST_ROW_INDEX, PRODUCT_HEADER_NAME, "Blue Jeans");
        productSubImpex.putValue(FIRST_ROW_INDEX, PRODUCT_HEADER_APPROVAL_STATUS, "Check");
        mainImpex.mergeImpex(subImpex);
        Assertions.assertThat(mainImpex.getImpexes()).hasSize(2);
        Assertions.assertThat(((ImpexForType)mainImpex.getImpexes().get(FIRST_ROW_INDEX.intValue())).getTypeCode()).isEqualTo("Category");
        Assertions.assertThat(((ImpexForType)mainImpex.getImpexes().get(FIRST_ROW_INDEX.intValue())).getImpexTable().rowKeySet().size()).isEqualTo(1);
        Assertions.assertThat(((ImpexForType)mainImpex.getImpexes().get(FIRST_ROW_INDEX.intValue())).getImpexTable().columnKeySet().size()).isEqualTo(2);
        Assertions.assertThat(((ImpexForType)mainImpex.getImpexes().get(FIRST_ROW_INDEX.intValue())).getImpexTable().get(FIRST_ROW_INDEX, CATEGORY_HEADER_CODE))
                        .isEqualTo("Blue");
        Assertions.assertThat(((ImpexForType)mainImpex.getImpexes().get(FIRST_ROW_INDEX.intValue())).getImpexTable().get(FIRST_ROW_INDEX, CATEGORY_HEADER_VERSION))
                        .isEqualTo("version(code, catalog(id))");
        Assertions.assertThat(((ImpexForType)mainImpex.getImpexes().get(SECOND_ROW_INDEX.intValue())).getTypeCode()).isEqualTo("Product");
        Assertions.assertThat(((ImpexForType)mainImpex.getImpexes().get(SECOND_ROW_INDEX.intValue())).getImpexTable().rowKeySet().size()).isEqualTo(2);
        Assertions.assertThat(((ImpexForType)mainImpex.getImpexes().get(SECOND_ROW_INDEX.intValue())).getImpexTable().columnKeySet().size()).isEqualTo(2);
        Assertions.assertThat(((ImpexForType)mainImpex.getImpexes().get(SECOND_ROW_INDEX.intValue())).getImpexTable().get(FIRST_ROW_INDEX, PRODUCT_HEADER_NAME))
                        .isEqualTo("Jeans");
        Assertions.assertThat(((ImpexForType)mainImpex
                                        .getImpexes().get(SECOND_ROW_INDEX.intValue())).getImpexTable().get(FIRST_ROW_INDEX, PRODUCT_HEADER_APPROVAL_STATUS))
                        .isEqualTo("Approved");
        Assertions.assertThat(((ImpexForType)mainImpex.getImpexes().get(SECOND_ROW_INDEX.intValue())).getImpexTable().get(SECOND_ROW_INDEX, PRODUCT_HEADER_NAME))
                        .isEqualTo("Blue Jeans");
        Assertions.assertThat(((ImpexForType)mainImpex
                                        .getImpexes().get(SECOND_ROW_INDEX.intValue())).getImpexTable().get(SECOND_ROW_INDEX, PRODUCT_HEADER_APPROVAL_STATUS))
                        .isEqualTo("Check");
    }
}
