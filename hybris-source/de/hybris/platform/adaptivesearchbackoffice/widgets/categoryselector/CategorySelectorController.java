package de.hybris.platform.adaptivesearchbackoffice.widgets.categoryselector;

import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.util.DefaultWidgetController;
import de.hybris.platform.adaptivesearch.model.AbstractAsSearchProfileModel;
import de.hybris.platform.adaptivesearch.services.AsSearchConfigurationService;
import de.hybris.platform.adaptivesearch.services.AsSearchProfileService;
import de.hybris.platform.adaptivesearchbackoffice.data.AsCategoryData;
import de.hybris.platform.adaptivesearchbackoffice.data.CatalogVersionData;
import de.hybris.platform.adaptivesearchbackoffice.data.CategoryData;
import de.hybris.platform.adaptivesearchbackoffice.data.NavigationContextData;
import de.hybris.platform.adaptivesearchbackoffice.facades.AsCategoryFacade;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.AbstractTreeModel;
import org.zkoss.zul.DefaultTreeModel;
import org.zkoss.zul.DefaultTreeNode;
import org.zkoss.zul.Tree;
import org.zkoss.zul.TreeModel;
import org.zkoss.zul.TreeNode;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.ext.SelectionControl;

public class CategorySelectorController extends DefaultWidgetController
{
    protected static final String CATEGORY_SELECTOR_ID = "categorySelector";
    protected static final String SELECTED_CATEGORY_OUT_SOCKET = "selectedCategory";
    protected static final int[] ROOT_NODE_PATH = new int[] {0};
    @WireVariable
    protected transient CatalogVersionService catalogVersionService;
    @WireVariable
    protected transient SessionService sessionService;
    @WireVariable
    protected transient I18NService i18nService;
    @WireVariable
    protected transient AsCategoryFacade asCategoryFacade;
    @WireVariable
    protected transient AsSearchProfileService asSearchProfileService;
    @WireVariable
    protected transient AsSearchConfigurationService asSearchConfigurationService;
    protected Tree categorySelector;
    protected transient TreeModel<TreeNode<CategoryModel>> categoriesModel;
    private Set<String> qualifiers;
    private CatalogVersionData catalogVersion;
    private CategoryData selectedCategory;
    private String searchProfile;
    private NavigationContextData navigationContext;


    public void initialize(Component component)
    {
        initializeCategoriesTree();
    }


    protected void initializeCategoriesTree()
    {
        DefaultTreeNode defaultTreeNode = new DefaultTreeNode(null, new ArrayList());
        this.categoriesModel = (TreeModel<TreeNode<CategoryModel>>)new DefaultTreeModel((TreeNode)defaultTreeNode);
        this.categorySelector.setModel(this.categoriesModel);
        populateCategoriesTree();
    }


    protected void updateCategoriesTree()
    {
        this.sessionService.executeInLocalView((SessionExecutionBody)new Object(this));
    }


    protected void populateCategoriesTree()
    {
        ((TreeNode)this.categoriesModel.getRoot()).getChildren().clear();
        AsCategoryData globalCategory = (this.catalogVersion == null) ? this.asCategoryFacade.getCategoryHierarchy() : this.asCategoryFacade.getCategoryHierarchy(this.catalogVersion.getCatalogId(), this.catalogVersion.getVersion());
        if(globalCategory != null)
        {
            populateCategoriesTreeNode((TreeNode<CategoryModel>)this.categoriesModel.getRoot(), globalCategory, this.qualifiers);
            if(this.categoriesModel instanceof DefaultTreeModel)
            {
                DefaultTreeModel treeModel = (DefaultTreeModel)this.categoriesModel;
                treeModel.clearOpen();
                treeModel.addOpenPath(ROOT_NODE_PATH);
                treeModel.clearSelection();
                treeModel.addSelectionPath(ROOT_NODE_PATH);
                treeModel.setSelectionControl((SelectionControl)new Object(this, (AbstractTreeModel)treeModel));
            }
        }
    }


    protected Set<String> populateCategoriesTreeNode(TreeNode<CategoryModel> parentNode, AsCategoryData category, Set<String> qualifiers)
    {
        DefaultTreeNode defaultTreeNode;
        Set<String> childrenConfigurationQualifiers = new HashSet<>();
        CategoryModel categoryModel = new CategoryModel();
        categoryModel.setCode(category.getCode());
        categoryModel.setName(category.getName());
        categoryModel.setVirtual(category.isVirtual());
        if(qualifiers != null && qualifiers.contains(category.getCode()))
        {
            categoryModel.setHasSearchConfiguration(true);
            childrenConfigurationQualifiers.add(category.getCode());
        }
        if(CollectionUtils.isEmpty(category.getChildren()))
        {
            defaultTreeNode = new DefaultTreeNode(categoryModel);
        }
        else
        {
            defaultTreeNode = new DefaultTreeNode(categoryModel, new ArrayList());
            for(AsCategoryData childCategory : category.getChildren())
            {
                childrenConfigurationQualifiers.addAll(populateCategoriesTreeNode((TreeNode<CategoryModel>)defaultTreeNode, childCategory, qualifiers));
            }
        }
        categoryModel.setNumberOfConfigurations(childrenConfigurationQualifiers.size());
        parentNode.add((TreeNode)defaultTreeNode);
        return childrenConfigurationQualifiers;
    }


    protected void sendSelectedCategory()
    {
        if(this.selectedCategory != null)
        {
            CategoryData clonedSelectedCategory = new CategoryData();
            clonedSelectedCategory.setCode(this.selectedCategory.getCode());
            clonedSelectedCategory.setPath(this.selectedCategory.getPath());
            sendOutput("selectedCategory", clonedSelectedCategory);
        }
        else
        {
            sendOutput("selectedCategory", null);
        }
    }


    @SocketEvent(socketId = "navigationContext")
    public void onNavigationContextChanged(NavigationContextData newNavigationContext)
    {
        if(isNavigationContextChanged(newNavigationContext))
        {
            this.navigationContext = newNavigationContext;
            this.catalogVersion = (this.navigationContext == null) ? null : this.navigationContext.getCatalogVersion();
            this.searchProfile = (this.navigationContext == null) ? null : this.navigationContext.getCurrentSearchProfile();
            this.selectedCategory = null;
            updateQualifiers(this.navigationContext);
            updateCategoriesTree();
            sendSelectedCategory();
        }
    }


    protected boolean isNavigationContextChanged(NavigationContextData newNavigationContext)
    {
        if(newNavigationContext == null)
        {
            return false;
        }
        if(!Objects.equals(newNavigationContext.getCatalogVersion(), this.catalogVersion))
        {
            return true;
        }
        if(!Objects.equals(newNavigationContext.getCategory(), this.selectedCategory))
        {
            return true;
        }
        return !Objects.equals(newNavigationContext.getCurrentSearchProfile(), this.searchProfile);
    }


    @ViewEvent(componentID = "categorySelector", eventName = "onSelect")
    public void onCategorySelected(SelectEvent<Treeitem, String> event)
    {
        Treeitem selectedItem = (Treeitem)event.getReference();
        CategoryModel selectedCategoryModel = (CategoryModel)selectedItem.getValue();
        this.selectedCategory = new CategoryData();
        this.selectedCategory.setCode(selectedCategoryModel.getCode());
        List<String> categoryPath = new ArrayList<>();
        buildCategoryPath(selectedItem, categoryPath);
        this.selectedCategory.setPath(categoryPath);
        sendSelectedCategory();
    }


    protected void buildCategoryPath(Treeitem selectedItem, List<String> path)
    {
        if(selectedItem.getParentItem() != null)
        {
            buildCategoryPath(selectedItem.getParentItem(), path);
        }
        if(selectedItem.getValue() instanceof CategoryModel)
        {
            CategoryModel category = (CategoryModel)selectedItem.getValue();
            if(!category.isVirtual() && category.getCode() != null)
            {
                path.add(category.getCode());
            }
        }
    }


    protected CatalogVersionModel resolveCatalogVersion(CatalogVersionData catalogVersion)
    {
        if(catalogVersion == null)
        {
            return null;
        }
        return this.catalogVersionService.getCatalogVersion(catalogVersion.getCatalogId(), catalogVersion.getVersion());
    }


    @SocketEvent(socketId = "refresh")
    public void refreshCategoryAfterChange(Object obj)
    {
        if(this.categoriesModel instanceof DefaultTreeModel)
        {
            Set<String> oldQualifiers = this.qualifiers;
            updateQualifiers(this.navigationContext);
            if(!Objects.equals(this.qualifiers, oldQualifiers))
            {
                DefaultTreeModel treeModel = (DefaultTreeModel)this.categoriesModel;
                int[] selectionPath = treeModel.getSelectionPath();
                int[][] openPaths = treeModel.getOpenPaths();
                updateCategoriesTree();
                treeModel.addOpenPaths(openPaths);
                treeModel.addSelectionPath(selectionPath);
            }
        }
    }


    protected void updateQualifiers(NavigationContextData navigationContext)
    {
        if(navigationContext != null && StringUtils.isNotEmpty(navigationContext.getCurrentSearchProfile()))
        {
            CatalogVersionModel catalog = resolveCatalogVersion(navigationContext.getCatalogVersion());
            Optional<AbstractAsSearchProfileModel> searchProfileOptional = this.asSearchProfileService.getSearchProfileForCode(catalog, navigationContext.getCurrentSearchProfile());
            if(searchProfileOptional.isPresent())
            {
                this.qualifiers = this.asSearchConfigurationService.getSearchConfigurationQualifiers(searchProfileOptional.get());
            }
            else
            {
                this.qualifiers = Collections.emptySet();
            }
        }
        else
        {
            this.qualifiers = Collections.emptySet();
        }
    }


    public TreeModel<TreeNode<CategoryModel>> getCategoriesModel()
    {
        return this.categoriesModel;
    }


    protected CatalogVersionData getCatalogVersion()
    {
        return this.catalogVersion;
    }


    protected void setCatalogVersion(CatalogVersionData catalogVersion)
    {
        this.catalogVersion = catalogVersion;
    }


    protected CategoryData getSelectedCategory()
    {
        return this.selectedCategory;
    }


    protected void setSelectedCategory(CategoryData selectedCategory)
    {
        this.selectedCategory = selectedCategory;
    }


    public void setNavigationContext(NavigationContextData navigationContext)
    {
        this.navigationContext = navigationContext;
    }


    public NavigationContextData getNavigationContext()
    {
        return this.navigationContext;
    }
}
