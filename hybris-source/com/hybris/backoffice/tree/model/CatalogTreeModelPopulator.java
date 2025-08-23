/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.tree.model;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import com.hybris.backoffice.navigation.NavigationNode;
import com.hybris.backoffice.navigation.impl.SimpleNode;
import com.hybris.backoffice.tree.model.helper.AsciiTreePrettyPrinter;
import com.hybris.backoffice.widgets.advancedsearch.engine.AdvancedSearchQueryData;
import com.hybris.cockpitng.core.context.CockpitContext;
import com.hybris.cockpitng.core.context.impl.DefaultCockpitContext;
import com.hybris.cockpitng.core.user.CockpitUserService;
import com.hybris.cockpitng.dataaccess.facades.permissions.PermissionFacade;
import com.hybris.cockpitng.dataaccess.facades.search.FieldSearchFacade;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.dataaccess.facades.type.exceptions.TypeNotFoundException;
import com.hybris.cockpitng.labels.LabelService;
import com.hybris.cockpitng.model.ComponentModelPopulator;
import com.hybris.cockpitng.search.data.SearchAttributeDescriptor;
import com.hybris.cockpitng.search.data.SearchQueryCondition;
import com.hybris.cockpitng.search.data.SearchQueryConditionList;
import com.hybris.cockpitng.search.data.ValueComparisonOperator;
import com.hybris.cockpitng.search.data.pageable.Pageable;
import com.hybris.cockpitng.testing.annotation.InextensibleMethod;
import com.hybris.cockpitng.tree.factory.NavigationTreeFactory;
import com.hybris.cockpitng.tree.node.DynamicNode;
import com.hybris.cockpitng.tree.node.DynamicNodePopulator;
import com.hybris.cockpitng.tree.node.SearchableDynamicNodePopulator;
import com.hybris.cockpitng.tree.util.TreeUtils;
import com.hybris.cockpitng.widgets.common.explorertree.ExplorerTreeController;
import com.hybris.cockpitng.widgets.common.explorertree.data.PartitionNodeData;
import com.hybris.cockpitng.widgets.common.explorertree.model.RefreshableTreeModel;
import de.hybris.platform.catalog.CatalogService;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.Config;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.commons.math3.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;
import org.zkoss.zul.AbstractTreeModel;
import org.zkoss.zul.DefaultTreeNode;
import org.zkoss.zul.TreeModel;
import org.zkoss.zul.TreeNode;

public class CatalogTreeModelPopulator
                implements ComponentModelPopulator<TreeModel<TreeNode<ItemModel>>>, SearchableDynamicNodePopulator
{
    public static final String MULTI_SELECT = CatalogTreeModelPopulator.class.getName() + "_multipleChoice";
    public static final String SHOW_ALL_CATALOGS_NODE = CatalogTreeModelPopulator.class.getSimpleName() + "_showAllCatalogsNode";
    public static final String SHOW_UNCATEGORIZED_ROOT_NODE = CatalogTreeModelPopulator.class.getSimpleName()
                    + "_showUncategorizedRootNode";
    public static final String SHOW_UNCATEGORIZED_CATALOG_NODE = CatalogTreeModelPopulator.class.getSimpleName()
                    + "_showUncategorizedCatalogNode";
    public static final String SHOW_UNCATEGORIZED_CATALOG_VERSION_NODE = CatalogTreeModelPopulator.class.getSimpleName()
                    + "_showUncategorizedCatalogVersionNode";
    public static final String SHOW_UNCATEGORIZED_CLASSIFICATION_CATALOG_VERSION_NODE = CatalogTreeModelPopulator.class
                    .getSimpleName() + "_showUncategorizedClassificationCatalogVersionNode";
    public static final String ALL_CATALOGS_NODE_ID = "allCatalogs";
    public static final String UNCATEGORIZED_PRODUCTS_NODE_ID = "uncategorizedProducts";
    public static final String I18N_CATALOGTREEMODELPOPULATOR_ALLCATALOGS = "catalogtreemodelpopulator.allcatalogs";
    public static final String I18N_CATALOGTREEMODELPOPULATOR_UNCATEGORIZED = "catalogtreemodelpopulator.uncategorized";
    public static final String SIMPLE_LABELS_CTX_PARAMETERS = "simpleLabels";
    public static final String BACKOFFICE_CONFIGURATION_CATALOG_ID = "_boconfig";
    public static final String I18N_EXPLORER_TREE_FILTERED_TOO_MANY_RESULT = "explorer.tree.filtered.too.many.result";
    public static final int FALLBACK_OVERFLOW_THRESHOLD_VALUE = 200;
    public static final String CONFIG_BACKOFFICE_CATALOG_TREE_POPULATOR_SEARCH_BASED_ACTIVE = "backoffice.catalog.tree.populator.search.based.active";
    public static final String CTX_TREE_LOOKUP_FS_ENABLED = "treeLookupFSEnabled";
    public static final String CTX_TREE_LOOKUP_ATTRIBUTES = "treeLookupAttributes";
    public static final String CTX_TREE_LOOKUP_ALLOW_FOR_FLAT_LIST = "treeLookupAllowForFlatList";
    public static final String CTX_TREE_LOOKUP_FLAT_LIST_THRESHOLD = "treeLookupFlatListThreshold";
    public static final String CTX_TREE_LOOKUP_OVERFLOW_THRESHOLD = "treeLookupOverflowThreshold";
    private static final String PROPERTY_SIMPLE_LABELS_ENABLED = "backoffice.cockpitng.explorertree.simplelabels.enabled";
    private static final Logger LOG = LoggerFactory.getLogger(CatalogTreeModelPopulator.class);
    private final Map<String, Boolean> supportedCheckedTypeCodes = new ConcurrentHashMap<>();
    private CatalogService catalogService;
    private CatalogVersionService catalogVersionService;
    private PermissionFacade permissionFacade;
    private TypeFacade typeFacade;
    private TypeService typeService;
    private FieldSearchFacade fieldSearchFacade;
    /**
     * @deprecated since 19.05 - implementation not use it any more, see
     *             {@link #getAllReadableCatalogVersionsForCurrentUser()}
     */
    @Deprecated(since = "19.05", forRemoval = true)
    private CockpitUserService cockpitUserService;
    private UserService userService;
    private Set<String> excludedTypes;
    private LabelService labelService;
    private SessionService sessionService;
    private CatalogTreeSimpleLabelProvider catalogTreeSimpleLabelProvider;
    private int partitionThreshold;
    private int overflowThreshold;
    private int flatListThreshold;
    private boolean allowForFlatList;
    private Set<String> lookupQueryAttributes;


    @PostConstruct
    public void postConstruct()
    {
        final Iterator<String> it = getExcludedTypes().iterator();
        while(it.hasNext())
        {
            final String type = it.next();
            try
            {
                getTypeService().getTypeForCode(type);
            }
            catch(final UnknownIdentifierException uie)
            {
                LOG.warn("Misspelled or unknown type name {}, excluding from filtered types", type);
                LOG.debug(uie.getMessage(), uie);
                it.remove();
            }
        }
        supportedCheckedTypeCodes.clear();
    }


    @Override
    public TreeModel<TreeNode<ItemModel>> createModel(final CockpitContext context)
    {
        final CatalogTreeModel model = new CatalogTreeModel(getRoot(context));
        if(context.containsParameter(MULTI_SELECT))
        {
            model.setMultiple(context.getParameterAsBoolean(MULTI_SELECT, false));
        }
        return model;
    }


    @Override
    public List<NavigationNode> getChildren(final NavigationNode node)
    {
        if(!(node instanceof DynamicNode))
        {
            throw new IllegalArgumentException("Only Dynamic Nodes are supported");
        }
        final List<NavigationNode> children = findChildrenNavigationNodes(node);
        if(children.size() > getPartitionThreshold())
        {
            return partitionNodes(node, children);
        }
        return children;
    }


    @Override
    public List<NavigationNode> getChildren(final DynamicNode parent, final String filterString, final boolean camelCase,
                    final TreeUtils.MatchMode matchMode)
    {
        final Object nodeData = parent.getData();
        if(StringUtils.isNotBlank(filterString) && nodeData instanceof CatalogVersionModel && isSearchBasedPopulatorActive(parent))
        {
            final CatalogVersionModel cv = (CatalogVersionModel)nodeData;
            final AdvancedSearchQueryData queryData = preparePopulatingQuery(parent, cv, filterString, camelCase, matchMode);
            final Pageable<CategoryModel> result = getFieldSearchFacade().search(queryData);
            final int totalCountInCV = result.getTotalCount();
            if(totalCountInCV > getOverflowThreshold(parent))
            {
                return buildOverflowNode(totalCountInCV, getOverflowThreshold(parent), cv);
            }
            else if(isAllowForFlatList(parent) && totalCountInCV > getFlatListThreshold(parent))
            {
                return buildFlatListNodes(parent, cv, result, totalCountInCV);
            }
            else
            {
                return buildSpanningTree(parent, filterString, cv, result);
            }
        }
        else
        {
            return getChildren(parent);
        }
    }


    /**
     * @return true in case the search-based tre population should be used, false otherwise
     * @param parent
     */
    protected boolean isSearchBasedPopulatorActive(final DynamicNode parent)
    {
        final CockpitContext context = parent.getContext();
        final boolean defaultValue = true;
        if(context.getParameter(CTX_TREE_LOOKUP_FS_ENABLED) != null)
        {
            return context.getParameterAsBoolean(CTX_TREE_LOOKUP_FS_ENABLED, defaultValue);
        }
        return Config.getBoolean(CONFIG_BACKOFFICE_CATALOG_TREE_POPULATOR_SEARCH_BASED_ACTIVE, defaultValue);
    }


    protected List<NavigationNode> buildFlatListNodes(final DynamicNode parent, final CatalogVersionModel cv,
                    final Pageable<CategoryModel> result, final int totalCount)
    {
        final List<CategoryModel> allResults = result.getAllResults();
        final List<NavigationNode> children = allResults.stream().map(element -> {
            String label = prepareNodeLabel(element, parent, getLabelService()::getObjectLabel);
            return createDynamicNode(parent, element, label, this, false);
        }).collect(Collectors.toList());
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Building flat tree for CV: '{}:{}' with {} matching results.", cv.getCatalog().getId(), cv.getVersion(),
                            children.size());
        }
        if(totalCount > getPartitionThreshold())
        {
            return partitionNodes(parent, children);
        }
        return children;
    }


    protected List<NavigationNode> buildOverflowNode(final int total, final int max, final CatalogVersionModel cv)
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Too many results in CV: '{}:{}'", cv.getCatalog().getId(), cv.getVersion());
        }
        final String label = Labels.getLabel(I18N_EXPLORER_TREE_FILTERED_TOO_MANY_RESULT, new Object[]
                        {total, max});
        final SimpleNode node = new SimpleNode(label, null);
        node.setName(label);
        return Collections.singletonList(node);
    }


    protected AdvancedSearchQueryData preparePopulatingQuery(final DynamicNode parent, final CatalogVersionModel cv,
                    final String filterString, final boolean caseSensitive, final TreeUtils.MatchMode matchMode)
    {
        final AdvancedSearchQueryData.Builder builder = new AdvancedSearchQueryData.Builder(CategoryModel._TYPECODE);
        builder.globalOperator(ValueComparisonOperator.AND);
        builder.searchQueryText(filterString);
        builder.pageSize(getOverflowThreshold(parent));
        builder.caseSensitive(caseSensitive);
        builder.includeSubtypes(true);
        final List<SearchQueryCondition> conditions = new ArrayList<>();
        final ValueComparisonOperator operator = resolvedMatchOperator(matchMode);
        final Set<String> qualifiers = getLookupQueryAttributes(parent);
        for(final String qualifier : qualifiers)
        {
            final SearchAttributeDescriptor descriptor = new SearchAttributeDescriptor(qualifier);
            final SearchQueryCondition condition = new SearchQueryCondition(descriptor, filterString, operator);
            conditions.add(condition);
        }
        final SearchQueryConditionList condList = new SearchQueryConditionList(ValueComparisonOperator.OR, conditions);
        final SearchQueryCondition cvCond = new SearchQueryCondition(new SearchAttributeDescriptor(CategoryModel.CATALOGVERSION),
                        cv, ValueComparisonOperator.EQUALS);
        builder.conditions(condList, cvCond);
        return builder.build();
    }


    protected ValueComparisonOperator resolvedMatchOperator(final TreeUtils.MatchMode matchMode)
    {
        switch(matchMode)
        {
            case STARTS_WITH:
                return ValueComparisonOperator.STARTS_WITH;
            case CONTAINS:
                return ValueComparisonOperator.CONTAINS;
            default:
                LOG.warn("Unsupported match mode was provided: '{}', default of '{}' will be used.", matchMode,
                                ValueComparisonOperator.CONTAINS);
                return ValueComparisonOperator.CONTAINS;
        }
    }


    protected List<NavigationNode> buildSpanningTree(final DynamicNode root, final String filterString,
                    final CatalogVersionModel cv, final Pageable<CategoryModel> data)
    {
        final List<NavigationNode> spanningTreeRoots;
        final StopWatch sw = StopWatch.createStarted();
        try
        {
            spanningTreeRoots = buildSpanningTree(root, data.getAllResults(), cv);
        }
        finally
        {
            sw.stop();
        }
        if(LOG.isDebugEnabled())
        {
            final String asciiTree = AsciiTreePrettyPrinter.prettyPrintTree(spanningTreeRoots);
            final long elapsedTime = sw.getTime(TimeUnit.MILLISECONDS);
            LOG.debug("\n──── Spanning tree for query: '{}' ────\n{}" + //
                                            "Tree computation time: {}ms\n" + //
                                            "Catalog Version: {}:{}\n" + //
                                            "Overflow threshold: {}\n" + //
                                            "Flat list threshold: {} (enabled: {})\n" + //
                                            "Query attributes: {}", filterString, asciiTree, elapsedTime, cv.getCatalog().getId(), cv.getVersion(),
                            getOverflowThreshold(root), getFlatListThreshold(root), isAllowForFlatList(root), getLookupQueryAttributes(root));
        }
        return spanningTreeRoots;
    }


    /**
     * This method builds spanning tree for given categories as described in the documentation of the <a href=
     * "https://help.sap.com/viewer/DRAFT/5c9ea0c629214e42b727bf08800d8dfa/2011/en-US/8bbd709c86691014b29eb8858354b837.html">Explorer
     * Tree Catalog Tree Model Populator</a>.
     *
     * @param root
     *           parent {@link DynamicNode}
     * @param categories
     *           Categories to be included in the tree
     * @param cv
     *           catalog version to which the nodes should belong
     * @return list of direct children of the given root node
     */
    protected List<NavigationNode> buildSpanningTree(final DynamicNode root, final List<CategoryModel> categories,
                    final CatalogVersionModel cv)
    {
        final List<NavigationNode> rootNodes = new ArrayList<>();
        final List<CategoryModel> visitedCategories = new ArrayList<>(categories.size());
        final Map<CategoryModel, Pair<DynamicNode, List<NavigationNode>>> treeNodes = new HashMap<>();
        final SearchableDynamicNodePopulator filteredTreeNodesStaticPopulator = new SpanningTreeModelBasedSearchableDynamicNodePopulator(
                        this, categories, treeNodes::get);
        final Deque<Pair<CategoryModel, List<CategoryModel>>> stack = new LinkedList<>();
        final List collect = categories.stream().map(cat -> new Pair(cat, new ArrayList())).collect(Collectors.toList());
        stack.addAll(collect);
        while(!stack.isEmpty())
        {
            final Pair<CategoryModel, List<CategoryModel>> pair = stack.pop();
            final CategoryModel category = pair.getFirst();
            final List<CategoryModel> subCategories = pair.getSecond();
            if(isRootCategory(category, cv))
            {
                final String label = prepareNodeLabel(category, root, getLabelService()::getShortObjectLabel);
                final DynamicNode node = createDynamicNode(root, category, label, filteredTreeNodesStaticPopulator, true);
                final Pair<DynamicNode, List<NavigationNode>> categoryNode = new Pair<>(node, new ArrayList<>());
                treeNodes.put(category, categoryNode);
                rootNodes.add(node);
                addCategoriesFromTheTail(treeNodes, filteredTreeNodesStaticPopulator, subCategories, categoryNode);
            }
            else
            {
                category.getSupercategories().forEach(parentCat -> {
                    if(visitedCategories.contains(parentCat) && treeNodes.containsKey(parentCat))
                    {
                        final Pair<DynamicNode, List<NavigationNode>> parent = treeNodes.get(parentCat);
                        final String label = prepareNodeLabel(category, parent.getFirst(), getLabelService()::getShortObjectLabel);
                        final DynamicNode newNode = createDynamicNode(parent.getFirst(), category, label,
                                        filteredTreeNodesStaticPopulator, true);
                        parent.getSecond().add(newNode);
                        final Pair<DynamicNode, List<NavigationNode>> categoryNode = new Pair<>(newNode, new ArrayList<>());
                        treeNodes.put(category, categoryNode);
                        addCategoriesFromTheTail(treeNodes, filteredTreeNodesStaticPopulator, subCategories, categoryNode);
                    }
                    else if(!treeNodes.containsKey(parentCat))
                    {
                        final List<CategoryModel> tail = new ArrayList<>(subCategories);
                        if(!tail.contains(category))
                        {
                            tail.add(0, category);
                        }
                        stack.push(new Pair<>(parentCat, tail));
                    }
                });
            }
            visitedCategories.add(category);
        }
        final boolean expandParent = !rootNodes.isEmpty();
        if(expandParent)
        {
            NavigationNode parent = root;
            while(parent instanceof SimpleNode)
            {
                ((SimpleNode)parent).setExpandedByDefault(true);
                parent = parent.getParent();
            }
        }
        return rootNodes;
    }


    protected void addCategoriesFromTheTail(final Map<CategoryModel, Pair<DynamicNode, List<NavigationNode>>> treeNodes,
                    final SearchableDynamicNodePopulator treeNodesToTreePopulator, final List<CategoryModel> tail,
                    Pair<DynamicNode, List<NavigationNode>> categoryNode)
    {
        for(final CategoryModel category : tail)
        {
            final String label = prepareNodeLabel(category, categoryNode.getFirst(), getLabelService()::getShortObjectLabel);
            final DynamicNode tailDN = createDynamicNode(categoryNode.getFirst(), category, label, treeNodesToTreePopulator, true);
            categoryNode.getSecond().add(0, tailDN);
            categoryNode = new Pair<>(tailDN, new ArrayList<>());
            treeNodes.put(category, categoryNode);
        }
    }


    protected boolean isRootCategory(final CategoryModel category, final CatalogVersionModel cv)
    {
        return Objects.equals(category.getCatalogVersion(), cv)
                        && category.getSupercategories().stream().noneMatch(cat -> Objects.equals(cat.getCatalogVersion(), cv));
    }


    protected List<NavigationNode> findChildrenNavigationNodes(final NavigationNode node)
    {
        final Object nodeData = node.getData();
        if(nodeData instanceof CatalogModel)
        {
            return prepareCatalogVersionNodes(node, (CatalogModel)nodeData);
        }
        else if(nodeData instanceof CatalogVersionModel)
        {
            return prepareRootCategoryNodes(node, (CatalogVersionModel)nodeData);
        }
        else if(nodeData instanceof CategoryModel)
        {
            return prepareSubcategoryNodes(node, (CategoryModel)nodeData);
        }
        else if(nodeData instanceof PartitionNodeData)
        {
            return ((PartitionNodeData)nodeData).getChildren();
        }
        else
        {
            return prepareCatalogNodes(node);
        }
    }


    protected List<NavigationNode> prepareCatalogNodes(final NavigationNode node)
    {
        final TreeModel<TreeNode<ItemModel>> model = createModel(node.getContext());
        final List<NavigationNode> rootNodes = model.getRoot().getChildren().stream().filter(treeNode -> treeNode.getData() != null)
                        .map(getRegularNodeCreatorFunction(node)).collect(Collectors.toList());
        if(node.getContext().getParameterAsBoolean(SHOW_UNCATEGORIZED_ROOT_NODE, false))
        {
            rootNodes.add(0, prepareUncategorizedProductsNode(node, null));
        }
        if(node.getContext().getParameterAsBoolean(SHOW_ALL_CATALOGS_NODE, true))
        {
            rootNodes.add(0, prepareAllCatalogsNode(node));
        }
        return rootNodes;
    }


    protected List<NavigationNode> prepareCatalogVersionNodes(final NavigationNode node, final CatalogModel nodeData)
    {
        final List<NavigationNode> catalogVersionsNodes = getAllReadableCatalogVersions(nodeData).stream()
                        .filter(catalogVersion -> isCatalogVersionAvailableInContext(catalogVersion, node.getContext()))
                        .map(DefaultTreeNode::new).map(getRegularNodeCreatorFunction(node)).collect(Collectors.toList());
        if(!(nodeData instanceof ClassificationSystemModel)
                        && node.getContext().getParameterAsBoolean(SHOW_UNCATEGORIZED_CATALOG_NODE, false))
        {
            catalogVersionsNodes.add(0, prepareUncategorizedProductsNode(node, nodeData));
        }
        return catalogVersionsNodes;
    }


    protected List<NavigationNode> prepareRootCategoryNodes(final NavigationNode node, final CatalogVersionModel nodeData)
    {
        final List<NavigationNode> categoriesNodes = filterAvailableCategories(getCategoryDynamicNodeCreatorFunction(node),
                        nodeData.getRootCategories());
        if(showUncategorizedNodeForCatalogVersion(node, nodeData))
        {
            categoriesNodes.add(0, prepareUncategorizedProductsNode(node, nodeData));
        }
        return categoriesNodes;
    }


    protected boolean showUncategorizedNodeForCatalogVersion(final NavigationNode node, final CatalogVersionModel nodeData)
    {
        if(nodeData instanceof ClassificationSystemVersionModel)
        {
            return node.getContext().getParameterAsBoolean(SHOW_UNCATEGORIZED_CLASSIFICATION_CATALOG_VERSION_NODE, false);
        }
        return node.getContext().getParameterAsBoolean(SHOW_UNCATEGORIZED_CATALOG_VERSION_NODE, true);
    }


    protected List<NavigationNode> prepareSubcategoryNodes(final NavigationNode node, final CategoryModel nodeData)
    {
        return filterAvailableCategories(getCategoryDynamicNodeCreatorFunction(node), nodeData.getCategories());
    }


    protected Function<TreeNode, DynamicNode> getRegularNodeCreatorFunction(final NavigationNode node)
    {
        return treeNode -> {
            final String label = prepareNodeLabel(treeNode, node, getLabelService()::getObjectLabel);
            return createDynamicNode(node, treeNode, label);
        };
    }


    protected Function<TreeNode, DynamicNode> getCategoryDynamicNodeCreatorFunction(final NavigationNode node)
    {
        return treeNode -> {
            final String label = prepareNodeLabel(treeNode, node, getLabelService()::getShortObjectLabel);
            return createDynamicNode(node, treeNode, label);
        };
    }


    protected String prepareNodeLabel(final TreeNode treeNode, final NavigationNode parentNode,
                    final Function<Object, String> labelServiceFn)
    {
        return prepareNodeLabel(treeNode.getData(), parentNode, labelServiceFn);
    }


    protected String prepareNodeLabel(final Object data, final NavigationNode parentNode,
                    final Function<Object, String> labelServiceFn)
    {
        if(simpleLabelsEnabled(parentNode))
        {
            final Object currentNodeData = data;
            final Object parentNodeData = parentNode.getData();
            final Optional<String> calculatedLabel = getCatalogTreeSimpleLabelProvider().getNodeLabel(parentNodeData,
                            currentNodeData);
            if(calculatedLabel.isPresent())
            {
                return calculatedLabel.get();
            }
        }
        return labelServiceFn.apply(data);
    }


    protected DynamicNode createDynamicNode(final NavigationNode node, final TreeNode<?> treeNode, final String label)
    {
        return createDynamicNode(node, treeNode.getData(), label, this, false);
    }


    protected DynamicNode createDynamicNode(final NavigationNode parent, final Object data, final String label,
                    final DynamicNodePopulator populator, final boolean expanded)
    {
        final int index = ((DynamicNode)parent).getIndexingDepth() - 1;
        final DynamicNode dynamicNode = new DynamicNode(createDynamicNodeId(parent, label), populator, index);
        dynamicNode.setData(data);
        dynamicNode.setName(label);
        dynamicNode.setContext(createCockpitContext(parent));
        dynamicNode.setSelectable(true);
        dynamicNode.setParent(parent);
        dynamicNode.setExpandedByDefault(expanded);
        return dynamicNode;
    }


    protected boolean simpleLabelsEnabled(final NavigationNode node)
    {
        return node.getContext().getParameterAsBoolean(SIMPLE_LABELS_CTX_PARAMETERS, false)
                        || Config.getBoolean(PROPERTY_SIMPLE_LABELS_ENABLED, false);
    }


    protected DynamicNode prepareAllCatalogsNode(final NavigationNode rootNode)
    {
        final DynamicNode allCatalogsNode = new DynamicNode(createDynamicNodeId(rootNode, ALL_CATALOGS_NODE_ID),
                        node -> Collections.emptyList(), 1);
        allCatalogsNode.setSelectable(true);
        allCatalogsNode.setActionAware(false);
        allCatalogsNode.setName(Labels.getLabel(I18N_CATALOGTREEMODELPOPULATOR_ALLCATALOGS));
        return allCatalogsNode;
    }


    protected DynamicNode prepareUncategorizedProductsNode(final NavigationNode rootNode, final ItemModel parentObject)
    {
        final DynamicNode uncategorizedNode = new DynamicNode(createDynamicNodeId(rootNode, UNCATEGORIZED_PRODUCTS_NODE_ID),
                        node -> Collections.emptyList(), 1);
        uncategorizedNode.setSelectable(true);
        uncategorizedNode.setData(new UncategorizedNode(parentObject));
        uncategorizedNode.setName(Labels.getLabel(I18N_CATALOGTREEMODELPOPULATOR_UNCATEGORIZED));
        uncategorizedNode.setActionAware(false);
        return uncategorizedNode;
    }


    protected String createDynamicNodeId(final NavigationNode node, final String postFix)
    {
        final boolean isRoot = node != null && StringUtils.startsWith(node.getId(), NavigationTreeFactory.ROOT_NODE_ID);
        final String prefix = isRoot ? node.getId() : createParentNodesIdPrefix(node);
        return prefix + postFix;
    }


    protected String createParentNodesIdPrefix(final NavigationNode node)
    {
        final StringBuilder prefix = new StringBuilder();
        NavigationNode current = node;
        while(current != null)
        {
            final String id = current.getId();
            if(id != null && !isParentIdAppended(id + "_", prefix))
            {
                prefix.insert(0, '_');
                prefix.insert(0, id.toLowerCase(Locale.ENGLISH));
            }
            current = current.getParent();
        }
        return prefix.toString();
    }


    protected boolean isParentIdAppended(final String parentId, final StringBuilder childId)
    {
        return (childId.length() >= parentId.length()) && (parentId.equals(childId.substring(0, parentId.length())));
    }


    protected List<NavigationNode> filterAvailableCategories(final Function<TreeNode, DynamicNode> nodeCreator,
                    final Collection<CategoryModel> categories)
    {
        final Map<String, Boolean> supportedCategories = new HashMap<>();
        final Collection<CatalogVersionModel> allReadableCatalogVersions = getAllReadableCatalogVersionsForCurrentUser();
        return ObjectUtils.defaultIfNull(categories, Collections.<CategoryModel>emptyList()).stream()
                        .filter(category -> allReadableCatalogVersions.contains(category.getCatalogVersion()))
                        .filter(category -> supportedCategories.computeIfAbsent(category.getItemtype(),
                                        typeCode -> isSupportedType(typeCode) && getPermissionFacade().canReadType(typeCode)))
                        .map(DefaultTreeNode::new).map(nodeCreator).collect(Collectors.toList());
    }


    protected List<NavigationNode> partitionNodes(final NavigationNode parent, final List<NavigationNode> nodes)
    {
        final List<List<NavigationNode>> partitions = Lists.partition(nodes, getPartitionThreshold());
        final List<NavigationNode> portionsNodes = new ArrayList<>();
        for(int i = 0; i < partitions.size(); i++)
        {
            final List<NavigationNode> partition = partitions.get(i);
            final int from = i * getPartitionThreshold();
            final int to = from + partition.size();
            final String nodeName = String.format("%d ... %d", Integer.valueOf(from + 1), Integer.valueOf(to));
            final DynamicNode partitionNode = new DynamicNode(nodeName, this, getIndexingDepth(parent));
            partitionNode.setData(new PartitionNodeData(parent, partition));
            partitionNode.setSelectable(true);
            portionsNodes.add(partitionNode);
            partitionNode.setName(nodeName);
            partitionNode.setParent(parent);
        }
        return portionsNodes;
    }


    protected int getIndexingDepth(final NavigationNode node)
    {
        return node instanceof DynamicNode ? ((DynamicNode)node).getIndexingDepth() : 0;
    }


    protected CockpitContext createCockpitContext(final NavigationNode node)
    {
        final CockpitContext context = new DefaultCockpitContext();
        context.addAllParameters(node.getContext());
        return context;
    }


    public Collection<CatalogModel> getAllReadableCatalogs(final CockpitContext context)
    {
        final Map<String, Boolean> typePermissions = new HashMap<>();
        return getCatalogService().getAllCatalogs().stream()
                        .filter(catalog -> !BACKOFFICE_CONFIGURATION_CATALOG_ID.equals(catalog.getId()))
                        .filter(catalog -> typePermissions.computeIfAbsent(catalog.getItemtype(),
                                        typeCode -> isSupportedType(typeCode) && getPermissionFacade().canReadType(typeCode)))
                        .filter(catalog -> isCatalogAvailableInContext(catalog, context)).collect(Collectors.toList());
    }


    protected boolean isCatalogAvailableInContext(final CatalogModel catalogModel, final CockpitContext context)
    {
        if(context == null)
        {
            return true;
        }
        final Collection<Object> selectedItems = (Collection)context
                        .getParameter(ExplorerTreeController.DYNAMIC_NODE_SELECTION_CONTEXT);
        if(selectedItems == null || selectedItems.isEmpty())
        {
            return true;
        }
        if(selectedItems.contains(catalogModel))
        {
            return true;
        }
        for(final CatalogVersionModel catalogVersion : catalogModel.getCatalogVersions())
        {
            if(selectedItems.contains(catalogVersion))
            {
                return true;
            }
        }
        return false;
    }


    protected boolean isCatalogVersionAvailableInContext(final CatalogVersionModel catalogVersionModel,
                    final CockpitContext context)
    {
        if(context == null)
        {
            return true;
        }
        final Collection<Object> selectedItems = (Collection<Object>)context
                        .getParameter(ExplorerTreeController.DYNAMIC_NODE_SELECTION_CONTEXT);
        if(selectedItems == null || selectedItems.isEmpty())
        {
            return true;
        }
        if(selectedItems.contains(catalogVersionModel))
        {
            return true;
        }
        return selectedItems.contains(catalogVersionModel.getCatalog());
    }


    public synchronized TreeNode<ItemModel> getRoot(final CockpitContext context)
    {
        final List<DefaultTreeNode<CatalogModel>> nodes = getAllReadableCatalogs(context).stream().map(DefaultTreeNode::new)
                        .collect(Collectors.toList());
        nodes.add(new AllCatalogsTreeNode(null));
        return new DefaultTreeNode(null, nodes);
    }


    public CatalogService getCatalogService()
    {
        return catalogService;
    }


    @Required
    public void setCatalogService(final CatalogService catalogService)
    {
        this.catalogService = catalogService;
    }


    public PermissionFacade getPermissionFacade()
    {
        return permissionFacade;
    }


    @Required
    public void setPermissionFacade(final PermissionFacade permissionFacade)
    {
        this.permissionFacade = permissionFacade;
    }


    public CatalogVersionService getCatalogVersionService()
    {
        return catalogVersionService;
    }


    @Required
    public void setCatalogVersionService(final CatalogVersionService catalogVersionService)
    {
        this.catalogVersionService = catalogVersionService;
    }


    /**
     * @deprecated since 19.05 - implementation not use it any more, see
     *             {@link #getAllReadableCatalogVersionsForCurrentUser()}
     */
    @Deprecated(since = "19.05", forRemoval = true)
    public CockpitUserService getCockpitUserService()
    {
        return cockpitUserService;
    }


    /**
     * @deprecated since 19.05 - implementation not use it any more, see
     *             {@link #getAllReadableCatalogVersionsForCurrentUser()}
     */
    @Deprecated(since = "19.05", forRemoval = true)
    public void setCockpitUserService(final CockpitUserService cockpitUserService)
    {
        this.cockpitUserService = cockpitUserService;
    }


    public UserService getUserService()
    {
        return userService;
    }


    public void setUserService(final UserService userService)
    {
        this.userService = userService;
    }


    protected List<CatalogVersionModel> getAllReadableCatalogVersions(final CatalogModel data)
    {
        final TreeSet<CatalogVersionModel> versions = new TreeSet<>(
                        (o1, o2) -> -ObjectUtils.compare(o1.getVersion(), o2.getVersion()));
        final Set<CatalogVersionModel> catalogVersions = data.getCatalogVersions();
        if(catalogVersions != null)
        {
            versions.addAll(catalogVersions);
        }
        final Collection<CatalogVersionModel> readableCatalogVersions = getAllReadableCatalogVersionsForCurrentUser();
        return versions.stream().filter(readableCatalogVersions::contains).filter(cv -> isSupportedType(cv.getItemtype()))
                        .collect(Collectors.toList());
    }


    protected Collection<CatalogVersionModel> getAllReadableCatalogVersionsForCurrentUser()
    {
        final UserModel currentUser = getUserService().getCurrentUser();
        if(getUserService().isAdmin(currentUser))
        {
            return getAllCatalogVersions();
        }
        return getCatalogVersionService().getAllReadableCatalogVersions(currentUser);
    }


    protected Collection<CatalogVersionModel> getAllCatalogVersions()
    {
        return getSessionService().executeInLocalView(new SessionExecutionBody()
        {
            @Override
            public Object execute()
            {
                return getCatalogVersionService().getAllCatalogVersions();
            }
        }, getUserService().getAdminUser());
    }


    protected boolean isSupportedType(final String typeCode)
    {
        if(StringUtils.isBlank(typeCode))
        {
            return false;
        }
        else
        {
            return supportedCheckedTypeCodes.computeIfAbsent(typeCode, this::computeTypeSupported);
        }
    }


    @InextensibleMethod
    private boolean computeTypeSupported(final String typeCode)
    {
        for(final String type : getExcludedTypes())
        {
            if(getTypeService().isAssignableFrom(type, typeCode))
            {
                return false;
            }
        }
        return true;
    }


    public TypeFacade getTypeFacade()
    {
        return typeFacade;
    }


    @Required
    public void setTypeFacade(final TypeFacade typeFacade)
    {
        this.typeFacade = typeFacade;
    }


    public FieldSearchFacade getFieldSearchFacade()
    {
        return fieldSearchFacade;
    }


    @Required
    public void setFieldSearchFacade(final FieldSearchFacade fieldSearchFacade)
    {
        this.fieldSearchFacade = fieldSearchFacade;
    }


    public TypeService getTypeService()
    {
        return typeService;
    }


    @Required
    public void setTypeService(final TypeService typeService)
    {
        this.typeService = typeService;
    }


    public int getPartitionThreshold()
    {
        return partitionThreshold;
    }


    @Required
    public void setPartitionThreshold(final int partitionThreshold)
    {
        this.partitionThreshold = partitionThreshold;
    }


    public CatalogTreeSimpleLabelProvider getCatalogTreeSimpleLabelProvider()
    {
        return catalogTreeSimpleLabelProvider;
    }


    @Required
    public void setCatalogTreeSimpleLabelProvider(final CatalogTreeSimpleLabelProvider catalogTreeSimpleLabelProvider)
    {
        this.catalogTreeSimpleLabelProvider = catalogTreeSimpleLabelProvider;
    }


    public Set<String> getExcludedTypes()
    {
        if(excludedTypes == null)
        {
            excludedTypes = Collections.emptySet();
        }
        return excludedTypes;
    }


    public void setExcludedTypes(final Set<String> excludedTypes)
    {
        this.excludedTypes = excludedTypes;
    }


    public LabelService getLabelService()
    {
        return labelService;
    }


    @Required
    public void setLabelService(final LabelService labelService)
    {
        this.labelService = labelService;
    }


    protected SessionService getSessionService()
    {
        return sessionService;
    }


    @Required
    public void setSessionService(final SessionService sessionService)
    {
        this.sessionService = sessionService;
    }


    public int getOverflowThreshold()
    {
        return overflowThreshold;
    }


    public void setOverflowThreshold(final int overflowThreshold)
    {
        if(overflowThreshold <= 0)
        {
            LOG.warn("Overflow threshold must be a positive number. Fallback value of {} was used.",
                            FALLBACK_OVERFLOW_THRESHOLD_VALUE);
            this.overflowThreshold = FALLBACK_OVERFLOW_THRESHOLD_VALUE;
        }
        else
        {
            this.overflowThreshold = overflowThreshold;
        }
    }


    public int getOverflowThreshold(final DynamicNode parent)
    {
        return parent.getContext().getParameterAsInt(CTX_TREE_LOOKUP_OVERFLOW_THRESHOLD, getOverflowThreshold());
    }


    public int getFlatListThreshold()
    {
        return flatListThreshold;
    }


    public void setFlatListThreshold(final int flatListThreshold)
    {
        this.flatListThreshold = flatListThreshold;
    }


    public int getFlatListThreshold(final DynamicNode parent)
    {
        return parent.getContext().getParameterAsInt(CTX_TREE_LOOKUP_FLAT_LIST_THRESHOLD, getFlatListThreshold());
    }


    public boolean isAllowForFlatList()
    {
        return allowForFlatList;
    }


    public void setAllowForFlatList(final boolean allowForFlatList)
    {
        this.allowForFlatList = allowForFlatList;
    }


    public boolean isAllowForFlatList(final DynamicNode parent)
    {
        return parent.getContext().getParameterAsBoolean(CTX_TREE_LOOKUP_ALLOW_FOR_FLAT_LIST, isAllowForFlatList());
    }


    public Set<String> getLookupQueryAttributes()
    {
        return lookupQueryAttributes;
    }


    public void setLookupQueryAttributes(final Set<String> lookupQueryAttributes)
    {
        this.lookupQueryAttributes = lookupQueryAttributes;
    }


    public Set<String> getLookupQueryAttributes(final DynamicNode parent)
    {
        final Object attributes = parent.getContext().getParameter(CTX_TREE_LOOKUP_ATTRIBUTES);
        final DataType categoryType;
        try
        {
            categoryType = getTypeFacade().load(CategoryModel._TYPECODE);
        }
        catch(final TypeNotFoundException tnfe)
        {
            throw new IllegalStateException(tnfe);
        }
        final Set<String> allCategoryAttributes = categoryType.getAttributes().stream().map(att -> att.getQualifier())
                        .collect(Collectors.toSet());
        final Set<String> qualifiers;
        if(attributes instanceof String)
        {
            final List<String> configuredAttributes = Arrays.asList(((String)attributes).trim().split(","));
            qualifiers = filterConfiguredCategoryAttributes(allCategoryAttributes, configuredAttributes);
        }
        else if(getLookupQueryAttributes() != null)
        {
            qualifiers = filterConfiguredCategoryAttributes(allCategoryAttributes, getLookupQueryAttributes());
        }
        else
        {
            LOG.warn("No query attribute was resolved. Using fallback value of '{}'", CategoryModel.CODE);
            qualifiers = Collections.singleton(CategoryModel.CODE);
        }
        return qualifiers;
    }


    protected Set<String> filterConfiguredCategoryAttributes(final Set<String> validAttributes,
                    final Collection<String> configuredAttributes)
    {
        return configuredAttributes.stream().map(q -> q.trim()).filter(q -> validAttributes.contains(q))
                        .collect(Collectors.toSet());
    }


    public class CatalogTreeModel extends AbstractTreeModel<TreeNode<ItemModel>> implements RefreshableTreeModel
    {
        private final transient LoadingCache<CatalogModel, List<CatalogVersionModel>> catalogVersionsCache = CacheBuilder
                        .newBuilder().build(new CacheLoader<CatalogModel, List<CatalogVersionModel>>()
                        {
                            @Override
                            public List<CatalogVersionModel> load(final CatalogModel data)
                            {
                                return getAllReadableCatalogVersions(data);
                            }
                        });


        public CatalogTreeModel(final TreeNode<ItemModel> root)
        {
            super(root);
        }


        @Override
        public boolean isLeaf(final TreeNode<ItemModel> node)
        {
            if(node == super.getRoot())
            {
                return node.getChildCount() == 0;
            }
            final ItemModel data = node.getData();
            if(data instanceof CatalogModel)
            {
                return getCatalogVersions((CatalogModel)data).isEmpty();
            }
            return true;
        }


        @Override
        public TreeNode<ItemModel> getChild(final TreeNode<ItemModel> node, final int i)
        {
            if(node == super.getRoot())
            {
                return node.getChildAt(i);
            }
            final ItemModel data = node.getData();
            if(data instanceof CatalogModel)
            {
                return new DefaultTreeNode<>(getCatalogVersions((CatalogModel)data).get(i));
            }
            return null;
        }


        @Override
        public int getChildCount(final TreeNode<ItemModel> node)
        {
            if(node == super.getRoot())
            {
                return node.getChildren().size();
            }
            final ItemModel data = node.getData();
            if(data instanceof CatalogModel)
            {
                return getCatalogVersions((CatalogModel)data).size();
            }
            return 0;
        }


        private List<CatalogVersionModel> getCatalogVersions(final CatalogModel data)
        {
            try
            {
                return catalogVersionsCache.get(data);
            }
            catch(final ExecutionException e)
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("Cannot get catalog versions", e);
                }
                return Collections.emptyList();
            }
        }


        @Override
        public int[] getPath(final TreeNode<ItemModel> child)
        {
            final Deque<Integer> indexes = new ArrayDeque<>();
            TreeNode current = child;
            while(current != null)
            {
                final TreeNode parent = current.getParent();
                if(parent != null)
                {
                    final int indexOfNode = parent.getChildren().indexOf(current);
                    if(indexOfNode >= 0)
                    {
                        indexes.push(Integer.valueOf(indexOfNode));
                    }
                    else
                    {
                        break;
                    }
                }
                current = parent;
            }
            return indexes.stream().mapToInt(Integer::intValue).toArray();
        }


        @Override
        public void refreshChildren(final Object node, final List children)
        {
            catalogVersionsCache.invalidate(node);
        }


        /**
         * Always throws {@link UnsupportedOperationException}.
         *
         * @return nothing.
         * @throws UnsupportedOperationException
         *            always.
         */
        @Override
        public List findNodesByData(final Object data)
        {
            throw new UnsupportedOperationException();
        }
    }
}
