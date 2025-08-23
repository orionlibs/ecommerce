package de.hybris.platform.cms2.servicelayer.services.impl;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.navigation.CMSNavigationEntryModel;
import de.hybris.platform.cms2.model.navigation.CMSNavigationNodeModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cms2.servicelayer.daos.CMSNavigationDao;
import de.hybris.platform.cms2.servicelayer.services.CMSNavigationService;
import de.hybris.platform.cms2.servicelayer.services.CMSPageService;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.servicelayer.keygenerator.impl.PersistentKeyGenerator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

public class DefaultCMSNavigationService extends AbstractCMSService implements CMSNavigationService
{
    private CMSNavigationDao cmsNavigationDao;
    private CatalogVersionService catalogVersionService;
    private CMSPageService cmsPageService;
    private KeyGenerator processCodeGenerator;
    private CMSAdminSiteService cmsAdminSiteService;


    public CMSNavigationNodeModel getNavigationNodeForId(String id) throws CMSItemNotFoundException
    {
        List<CMSNavigationNodeModel> node = getCmsNavigationDao().findNavigationNodesById(id,
                        getCatalogVersionService().getSessionCatalogVersions());
        if(node.isEmpty())
        {
            throw new CMSItemNotFoundException("No NavigationNode with id [" + id + "] found.");
        }
        return node.iterator().next();
    }


    protected List<CMSNavigationNodeModel> getNavigationNodesByContentPage(ContentPageModel page)
    {
        return getCmsNavigationDao().findNavigationNodesByContentPage(page, getCatalogVersionService().getSessionCatalogVersions());
    }


    public List<CMSNavigationNodeModel> getNavigationNodesForContentPage(ContentPageModel page)
    {
        return getCmsNavigationDao().findNavigationNodesByContentPage(page, getCatalogVersionService().getSessionCatalogVersions());
    }


    public List<CMSNavigationNodeModel> getNavigationNodesForContentPageId(String pageId) throws CMSItemNotFoundException
    {
        ContentPageModel page = getCmsPageService().getPageForLabelOrId(pageId);
        if(page == null)
        {
            throw new CMSItemNotFoundException("No ContentPage with id [" + pageId + "] found.");
        }
        return getNavigationNodesByContentPage(page);
    }


    public List<CMSNavigationNodeModel> getRootNavigationNodes()
    {
        return getCmsNavigationDao().findRootNavigationNodes(Arrays.asList(new CatalogVersionModel[] {getCmsAdminSiteService().getActiveCatalogVersion()}));
    }


    protected List<CMSNavigationNodeModel> getRootNavigationNodesByContentPage(ContentPageModel page)
    {
        return getRootNavigationNodesForContentPage(page);
    }


    public List<CMSNavigationNodeModel> getRootNavigationNodesForContentPage(ContentPageModel page)
    {
        return getNavigationNodesByContentPage(page);
    }


    public List<CMSNavigationNodeModel> getRootNavigationNodesForContentPageId(String pageId) throws CMSItemNotFoundException
    {
        ContentPageModel page = getCmsPageService().getPageForLabelOrId(pageId);
        if(page == null)
        {
            throw new CMSItemNotFoundException("No ContentPage with id [" + pageId + "] found.");
        }
        return getRootNavigationNodesByContentPage(page);
    }


    public List<CMSNavigationEntryModel> getNavigationEntriesByPage(AbstractPageModel page)
    {
        return getCmsNavigationDao().findNavigationEntriesByPage(page);
    }


    public Optional<CMSNavigationEntryModel> getNavigationEntryForId(String id, CatalogVersionModel catalogVersion)
    {
        return Optional.ofNullable(getCmsNavigationDao().findNavigationEntryByUid(id, catalogVersion));
    }


    public CMSNavigationEntryModel createCmsNavigationEntry(CMSNavigationNodeModel node, String entryName, ItemModel entryContent)
    {
        List<CMSNavigationEntryModel> cmsNavigationEntries = new ArrayList<>(node.getEntries());
        CMSNavigationEntryModel cmsNavigationEntryModel = createCmsNavigationEntry(entryContent, node.getCatalogVersion());
        cmsNavigationEntryModel.setName(entryName);
        cmsNavigationEntries.add(cmsNavigationEntryModel);
        node.setEntries(cmsNavigationEntries);
        getModelService().save(node);
        return cmsNavigationEntryModel;
    }


    public CMSNavigationEntryModel createCmsNavigationEntry(CatalogVersionModel catalogVersion, ItemModel item)
    {
        return createCmsNavigationEntry(item, catalogVersion);
    }


    protected void deleteRecursively(CMSNavigationNodeModel cmsNavigationNode)
    {
        List<CMSNavigationNodeModel> children = cmsNavigationNode.getChildren();
        if(CollectionUtils.isNotEmpty(children))
        {
            for(CMSNavigationNodeModel cmsNavigationNodeModel : children)
            {
                deleteRecursively(cmsNavigationNodeModel);
            }
        }
        if(CollectionUtils.isNotEmpty(cmsNavigationNode.getEntries()))
        {
            for(CMSNavigationEntryModel entry : cmsNavigationNode.getEntries())
            {
                ItemModel item = entry.getItem();
                if(item instanceof ContentPageModel)
                {
                    List<CMSNavigationNodeModel> currentNodes = ((ContentPageModel)item).getNavigationNodeList();
                    List<CMSNavigationNodeModel> toSet = new ArrayList<>(currentNodes);
                    toSet.remove(cmsNavigationNode);
                    ((ContentPageModel)item).setNavigationNodeList(toSet);
                    getModelService().save(item);
                    getModelService().remove(entry);
                }
            }
        }
        getModelService().remove(cmsNavigationNode);
    }


    protected List<ItemModel> extractCmsNavigationEntries(CMSNavigationNodeModel navigatioNode)
    {
        List<ItemModel> extractedEntries = new ArrayList<>();
        if(navigatioNode == null)
        {
            return extractedEntries;
        }
        for(CMSNavigationEntryModel entry : navigatioNode.getEntries())
        {
            extractedEntries.add(entry.getItem());
        }
        return extractedEntries;
    }


    protected List<CMSNavigationEntryModel> wrapItemModel(Collection<ItemModel> items, CatalogVersionModel catalogVersionModel)
    {
        List<CMSNavigationEntryModel> wrappedEntries = new ArrayList<>();
        if(CollectionUtils.isEmpty(items))
        {
            return wrappedEntries;
        }
        for(ItemModel item : items)
        {
            CMSNavigationEntryModel cmsNavigationEntry = createCmsNavigationEntry(item, catalogVersionModel);
            wrappedEntries.add(cmsNavigationEntry);
        }
        return wrappedEntries;
    }


    public List<CMSNavigationNodeModel> getRootNavigationNodes(CatalogVersionModel catVer)
    {
        return getCmsNavigationDao().findRootNavigationNodes(Collections.singletonList(catVer));
    }


    public void move(CMSNavigationNodeModel sourceNode, CMSNavigationNodeModel targetModel)
    {
        CMSNavigationNodeModel oldParent = sourceNode.getParent();
        if(oldParent != null)
        {
            List<CMSNavigationNodeModel> oldChildren = new ArrayList<>();
            oldChildren.addAll(oldParent.getChildren());
            oldChildren.remove(sourceNode);
            oldParent.setChildren(oldChildren);
            List<CMSNavigationNodeModel> children = new ArrayList<>();
            children.addAll(targetModel.getChildren());
            children.add(sourceNode);
            sourceNode.setParent(targetModel);
            targetModel.setChildren(children);
            getModelService().saveAll(new Object[] {oldParent, sourceNode, targetModel});
        }
    }


    public void move(CMSNavigationNodeModel cmsNavigationNode, ItemModel sourceEntry, ItemModel targetnEntry)
    {
        List<ItemModel> items = new ArrayList<>(extractCmsNavigationEntries(cmsNavigationNode));
        int sourceIndex = items.indexOf(sourceEntry);
        int targetIndex = items.indexOf(targetnEntry);
        if(sourceIndex == -1 || targetIndex == -1)
        {
            return;
        }
        if(sourceIndex < targetIndex)
        {
            items.add(targetIndex, sourceEntry);
            items.remove(sourceIndex);
        }
        else
        {
            items.add(targetIndex, sourceEntry);
            items.remove(items.lastIndexOf(sourceEntry));
        }
        cmsNavigationNode.setEntries(wrapItemModel(items, cmsNavigationNode.getCatalogVersion()));
        getModelService().save(cmsNavigationNode);
    }


    public void move(ItemModel item, CMSNavigationNodeModel sourceNode, CMSNavigationNodeModel targetNode)
    {
        remove(sourceNode, item);
        createCmsNavigationEntry(targetNode, sourceNode.getName(), item);
    }


    @Deprecated(since = "1811", forRemoval = true)
    public void delete(CMSNavigationNodeModel cmsNavigationNode)
    {
        CMSNavigationNodeModel parentModel = cmsNavigationNode.getParent();
        List children = new ArrayList();
        if(parentModel == null)
        {
            getModelService().remove(cmsNavigationNode);
        }
        else
        {
            children.addAll(parentModel.getChildren());
            children.remove(cmsNavigationNode);
            parentModel.setChildren(children);
            getModelService().save(parentModel);
        }
        deleteRecursively(cmsNavigationNode);
        getModelService().remove(cmsNavigationNode);
    }


    public void remove(CMSNavigationNodeModel cmsNavigationNode, ItemModel item)
    {
        if(cmsNavigationNode == null || item == null)
        {
            return;
        }
        List<CMSNavigationEntryModel> entries = new ArrayList<>(cmsNavigationNode.getEntries());
        for(Iterator<CMSNavigationEntryModel> iter = entries.iterator(); iter.hasNext(); )
        {
            CMSNavigationEntryModel currentEntry = iter.next();
            if(item.equals(currentEntry.getItem()))
            {
                iter.remove();
                break;
            }
        }
        cmsNavigationNode.setEntries(entries);
        getModelService().save(cmsNavigationNode);
    }


    @Deprecated(since = "1811", forRemoval = true)
    public boolean removeNavigationEntryByUid(CMSNavigationNodeModel cmsNavigationNode, String navigationEntryUid)
    {
        List<CMSNavigationEntryModel> entries = new ArrayList<>(cmsNavigationNode.getEntries());
        CMSNavigationEntryModel entryToRemove = entries.stream().filter(entryModel -> navigationEntryUid.equals(entryModel.getUid())).findFirst().orElse(null);
        boolean removed = entries.removeIf(entryModel -> navigationEntryUid.equals(entryModel.getUid()));
        if(removed)
        {
            getModelService().remove(entryToRemove);
        }
        cmsNavigationNode.setEntries(entries);
        getModelService().save(cmsNavigationNode);
        return removed;
    }


    public CMSNavigationNodeModel createNavigationNode(ItemModel parentNode, String navigationNodeName, boolean visible, Collection<ItemModel> relatedItems)
    {
        CMSNavigationNodeModel navigationNode = (CMSNavigationNodeModel)getModelService().create(CMSNavigationNodeModel.class);
        navigationNode.setName(navigationNodeName);
        navigationNode.setVisible(visible);
        if(parentNode instanceof CatalogVersionModel)
        {
            navigationNode.setCatalogVersion((CatalogVersionModel)parentNode);
            navigationNode.setEntries(wrapItemModel(relatedItems, (CatalogVersionModel)parentNode));
            return setSuperRootNodeOnNavigationNode(navigationNode, (CatalogVersionModel)parentNode);
        }
        if(parentNode instanceof CMSNavigationNodeModel)
        {
            List<CMSNavigationNodeModel> children = new ArrayList<>();
            CMSNavigationNodeModel targetModel = (CMSNavigationNodeModel)parentNode;
            CatalogVersionModel catalogVersionModel = targetModel.getCatalogVersion();
            children.addAll(targetModel.getChildren());
            children.add(navigationNode);
            targetModel.setChildren(children);
            navigationNode.setParent(targetModel);
            navigationNode.setCatalogVersion(catalogVersionModel);
            navigationNode.setEntries(wrapItemModel(relatedItems, ((CMSNavigationNodeModel)parentNode).getCatalogVersion()));
        }
        return navigationNode;
    }


    public boolean isSuperRootNavigationNode(CMSNavigationNodeModel navigationNodeModel)
    {
        return "root".equalsIgnoreCase(navigationNodeModel.getUid());
    }


    public CMSNavigationNodeModel setSuperRootNodeOnNavigationNode(CMSNavigationNodeModel navigationNodeModel, CatalogVersionModel catalogVersionModel)
    {
        CMSNavigationNodeModel root = Optional.<CMSNavigationNodeModel>ofNullable(getSuperRootNavigationNode(catalogVersionModel)).orElseGet(() -> createSuperRootNavigationNode(catalogVersionModel));
        List<CMSNavigationNodeModel> children = new ArrayList<>(root.getChildren());
        children.add(navigationNodeModel);
        root.setChildren(children);
        navigationNodeModel.setParent(root);
        return navigationNodeModel;
    }


    public CMSNavigationNodeModel createSuperRootNavigationNode(CatalogVersionModel catalogVersion)
    {
        CMSNavigationNodeModel rootNavigationNodeModel = (CMSNavigationNodeModel)getModelService().create(CMSNavigationNodeModel.class);
        rootNavigationNodeModel.setName("root");
        rootNavigationNodeModel.setCatalogVersion(catalogVersion);
        rootNavigationNodeModel.setUid("root");
        rootNavigationNodeModel.setVisible(false);
        rootNavigationNodeModel.setChildren(Collections.emptyList());
        return rootNavigationNodeModel;
    }


    public void appendRelatedItems(CMSNavigationNodeModel cmsNavigationNode, Collection<ItemModel> items)
    {
        if(cmsNavigationNode == null)
        {
            return;
        }
        List<ItemModel> repetedItems = new ArrayList<>();
        for(CMSNavigationEntryModel entry : cmsNavigationNode.getEntries())
        {
            if(items.contains(entry.getItem()))
            {
                repetedItems.add(entry.getItem());
            }
        }
        List<ItemModel> orginalItems = new ArrayList<>((items == null) ? Collections.<ItemModel>emptyList() : items);
        CollectionUtils.removeAll(orginalItems, repetedItems);
        for(ItemModel item : orginalItems)
        {
            createCmsNavigationEntry(cmsNavigationNode, cmsNavigationNode.getName(), item);
        }
    }


    public void move(CMSNavigationNodeModel sourceNode, CMSNavigationNodeModel targetModel, boolean asChild, boolean append)
    {
        CMSNavigationNodeModel oldParent = sourceNode.getParent();
        if(oldParent != null)
        {
            List<CMSNavigationNodeModel> oldChildren = new ArrayList<>();
            oldChildren.addAll(oldParent.getChildren());
            oldChildren.remove(sourceNode);
            oldParent.setChildren(oldChildren);
            List<CMSNavigationNodeModel> children = new ArrayList<>();
            if(asChild)
            {
                children.addAll(targetModel.getChildren());
                if(append)
                {
                    children.add(sourceNode);
                }
                else
                {
                    children.add(0, sourceNode);
                }
                sourceNode.setParent(targetModel);
                targetModel.setChildren(children);
                getModelService().saveAll(new Object[] {oldParent, sourceNode, targetModel});
            }
            else
            {
                CMSNavigationNodeModel parent = targetModel.getParent();
                children.addAll(parent.getChildren());
                if(append)
                {
                    children.add(children.indexOf(targetModel) + 1, sourceNode);
                }
                else
                {
                    children.add(children.indexOf(targetModel), sourceNode);
                }
                sourceNode.setParent(parent);
                parent.setChildren(children);
                getModelService().saveAll(new Object[] {oldParent, sourceNode, targetModel, parent});
            }
        }
    }


    protected CMSNavigationEntryModel createCmsNavigationEntry(ItemModel entryContent, CatalogVersionModel catalogVersionModel)
    {
        CMSNavigationEntryModel cmsNavigationEntryModel = (CMSNavigationEntryModel)getModelService().create(CMSNavigationEntryModel.class);
        cmsNavigationEntryModel.setItem(entryContent);
        cmsNavigationEntryModel.setCatalogVersion(catalogVersionModel);
        ((PersistentKeyGenerator)getProcessCodeGenerator()).setKey("CMSNavigationEntry");
        cmsNavigationEntryModel.setUid("CMSNavigationEntry" +
                        String.valueOf(getProcessCodeGenerator().generate()));
        return cmsNavigationEntryModel;
    }


    public CMSNavigationNodeModel getSuperRootNavigationNode(CatalogVersionModel catalogVersionModel)
    {
        return getCmsNavigationDao().findSuperRootNavigationNode(catalogVersionModel);
    }


    protected CatalogVersionService getCatalogVersionService()
    {
        return this.catalogVersionService;
    }


    @Required
    public void setCatalogVersionService(CatalogVersionService catalogVersionService)
    {
        this.catalogVersionService = catalogVersionService;
    }


    protected CMSNavigationDao getCmsNavigationDao()
    {
        return this.cmsNavigationDao;
    }


    @Required
    public void setCmsNavigationDao(CMSNavigationDao cmsNavigationDao)
    {
        this.cmsNavigationDao = cmsNavigationDao;
    }


    protected CMSPageService getCmsPageService()
    {
        return this.cmsPageService;
    }


    @Required
    public void setCmsPageService(CMSPageService cmsPageService)
    {
        this.cmsPageService = cmsPageService;
    }


    protected KeyGenerator getProcessCodeGenerator()
    {
        return this.processCodeGenerator;
    }


    @Required
    public void setProcessCodeGenerator(KeyGenerator keyGenerator)
    {
        this.processCodeGenerator = keyGenerator;
    }


    protected CMSAdminSiteService getCmsAdminSiteService()
    {
        return this.cmsAdminSiteService;
    }


    @Required
    public void setCmsAdminSiteService(CMSAdminSiteService cmsAdminSiteService)
    {
        this.cmsAdminSiteService = cmsAdminSiteService;
    }
}
