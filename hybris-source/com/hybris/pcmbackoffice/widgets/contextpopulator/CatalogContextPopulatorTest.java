package com.hybris.pcmbackoffice.widgets.contextpopulator;

import com.hybris.backoffice.navigation.NavigationNode;
import com.hybris.backoffice.tree.model.UncategorizedNode;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.widgets.common.explorertree.data.PartitionNodeData;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CatalogContextPopulatorTest
{
    @InjectMocks
    private CatalogContextPopulator catalogContextPopulator;
    @Mock
    private TypeFacade typeFacade;


    @Test
    public void shouldPopulateContextForCatalogModel()
    {
        CatalogModel catalogModel = new CatalogModel();
        BDDMockito.given(this.typeFacade.getType(catalogModel)).willReturn("Catalog");
        Map<String, Object> context = this.catalogContextPopulator.populate(catalogModel);
        Assertions.assertThat(context.keySet()).contains((Object[])new String[] {"childTypeCode", "selectedObject", "selectedTypeCode"});
        Assertions.assertThat(context.get("childTypeCode")).isEqualTo("CatalogVersion");
        Assertions.assertThat(context.get("selectedObject")).isEqualTo(catalogModel);
        Assertions.assertThat(context.get("selectedTypeCode")).isEqualTo("Catalog");
    }


    @Test
    public void shouldPopulateContextForCatalogVersionModel()
    {
        CatalogModel catalogModel = new CatalogModel();
        CatalogVersionModel catalogVersionModel = (CatalogVersionModel)Mockito.mock(CatalogVersionModel.class);
        BDDMockito.given(catalogVersionModel.getCatalog()).willReturn(catalogModel);
        BDDMockito.given(this.typeFacade.getType(catalogVersionModel)).willReturn("CatalogVersion");
        Map<String, Object> context = this.catalogContextPopulator.populate(catalogVersionModel);
        Assertions.assertThat(context.keySet()).contains((Object[])new String[] {"childTypeCode", "selectedObject", "selectedTypeCode", "catalog"});
        Assertions.assertThat(context.get("childTypeCode")).isEqualTo("Category");
        Assertions.assertThat(context.get("selectedObject")).isEqualTo(catalogVersionModel);
        Assertions.assertThat(context.get("selectedTypeCode")).isEqualTo("CatalogVersion");
        Assertions.assertThat(context.get("catalog")).isEqualTo(catalogModel);
        Assertions.assertThat(context.get("catalogVersion")).isEqualTo(catalogVersionModel);
    }


    @Test
    public void shouldPopulateContextForCategoryModel()
    {
        CatalogModel catalogModel = (CatalogModel)Mockito.mock(CatalogModel.class);
        CatalogVersionModel catalogVersionModel = (CatalogVersionModel)Mockito.mock(CatalogVersionModel.class);
        CategoryModel categoryModel = (CategoryModel)Mockito.mock(CategoryModel.class);
        BDDMockito.given(this.typeFacade.getType(categoryModel)).willReturn("Category");
        BDDMockito.given(categoryModel.getCatalogVersion()).willReturn(catalogVersionModel);
        BDDMockito.given(catalogVersionModel.getCatalog()).willReturn(catalogModel);
        Map<String, Object> context = this.catalogContextPopulator.populate(categoryModel);
        Assertions.assertThat(context.keySet()).contains((Object[])new String[] {"childTypeCode", "selectedObject", "selectedTypeCode", "catalog", "catalogVersion", "supercategories"});
        Assertions.assertThat(context.get("childTypeCode")).isEqualTo("Category");
        Assertions.assertThat(context.get("selectedObject")).isEqualTo(categoryModel);
        Assertions.assertThat(context.get("selectedTypeCode")).isEqualTo("Category");
        Assertions.assertThat(context.get("catalog")).isEqualTo(catalogModel);
        Assertions.assertThat(context.get("catalogVersion")).isEqualTo(catalogVersionModel);
        List<CategoryModel> supercategories = (List<CategoryModel>)context.get("supercategories");
        Assertions.assertThat(supercategories).hasSize(1);
        Assertions.assertThat(supercategories).contains((Object[])new CategoryModel[] {categoryModel});
    }


    @Test
    public void shouldPopulateContextForUnknownModel()
    {
        Object unknownModel = new Object();
        String objectTypeCode = "java.lang.Object";
        BDDMockito.given(this.typeFacade.getType(unknownModel)).willReturn("java.lang.Object");
        Map<String, Object> context = this.catalogContextPopulator.populate(unknownModel);
        Assertions.assertThat(context.keySet()).contains((Object[])new String[] {"childTypeCode", "selectedObject", "selectedTypeCode"});
        Assertions.assertThat(context.get("childTypeCode")).isEqualTo("Catalog");
        Assertions.assertThat(context.get("selectedObject")).isEqualTo(unknownModel);
        Assertions.assertThat(context.get("selectedTypeCode")).isEqualTo("java.lang.Object");
    }


    @Test
    public void shouldPopulateContextIfObjectInstanceOfPartitionNodeData()
    {
        List<NavigationNode> children = new ArrayList<>();
        NavigationNode parent = (NavigationNode)Mockito.mock(NavigationNode.class);
        Object unknownModel = new PartitionNodeData(parent, children);
        Map<String, Object> context = this.catalogContextPopulator.populate(unknownModel);
        Assertions.assertThat(context.get("selectedObject")).isEqualTo(unknownModel);
        Assertions.assertThat(context.size()).isEqualTo(1);
    }


    @Test
    public void shouldPopulateContextForUncategorizedNode()
    {
        CatalogModel catalogModel = new CatalogModel();
        CatalogVersionModel catalogVersionModel = (CatalogVersionModel)Mockito.mock(CatalogVersionModel.class);
        BDDMockito.given(catalogVersionModel.getCatalog()).willReturn(catalogModel);
        UncategorizedNode uncategorizedNode = (UncategorizedNode)Mockito.mock(UncategorizedNode.class);
        BDDMockito.given(uncategorizedNode.getParentItem()).willReturn(catalogVersionModel);
        BDDMockito.given(this.typeFacade.getType(uncategorizedNode)).willReturn(UncategorizedNode.class.getName());
        Map<String, Object> context = this.catalogContextPopulator.populate(uncategorizedNode);
        Assertions.assertThat(context.keySet()).contains((Object[])new String[] {"childTypeCode", "selectedObject", "selectedTypeCode", "catalog"});
        Assertions.assertThat(context.get("childTypeCode")).isEqualTo("Category");
        Assertions.assertThat(context.get("selectedObject")).isEqualTo(uncategorizedNode);
        Assertions.assertThat(context.get("selectedTypeCode")).isEqualTo(UncategorizedNode.class.getName());
        Assertions.assertThat(context.get("catalog")).isEqualTo(catalogModel);
        Assertions.assertThat(context.get("catalogVersion")).isEqualTo(catalogVersionModel);
    }
}
