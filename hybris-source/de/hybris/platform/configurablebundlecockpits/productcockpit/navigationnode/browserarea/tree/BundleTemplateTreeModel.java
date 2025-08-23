package de.hybris.platform.configurablebundlecockpits.productcockpit.navigationnode.browserarea.tree;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.model.search.ResultObject;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.session.impl.DefaultSearchBrowserModel;
import de.hybris.platform.configurablebundleservices.bundle.BundleTemplateService;
import de.hybris.platform.configurablebundleservices.model.BundleTemplateModel;
import de.hybris.platform.core.model.ItemModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.zkoss.spring.SpringUtil;
import org.zkoss.zul.AbstractTreeModel;

public class BundleTemplateTreeModel extends AbstractTreeModel
{
    private final transient BundleTemplateService bundleTemplateService;
    private final transient TypeService typeService;


    public BundleTemplateTreeModel(Object root)
    {
        super(root);
        this.bundleTemplateService = (BundleTemplateService)SpringUtil.getBean("bundleTemplateService", BundleTemplateService.class);
        this.typeService = (TypeService)SpringUtil.getBean("cockpitTypeService", TypeService.class);
    }


    public boolean isLeaf(Object node)
    {
        return (getChildCount(node) == 0);
    }


    public Object getChild(Object parent, int index)
    {
        List<BundleTemplateModel> children = getChildren(parent);
        if(children != null && children.size() > index)
        {
            return getTypeService().wrapItem(children.get(index));
        }
        return null;
    }


    public int getChildCount(Object parent)
    {
        return getChildren(parent).size();
    }


    protected List<BundleTemplateModel> getChildren(Object nodeObj)
    {
        List<BundleTemplateModel> bundleList = new ArrayList<>();
        if(nodeObj instanceof DefaultSearchBrowserModel)
        {
            DefaultSearchBrowserModel model = (DefaultSearchBrowserModel)nodeObj;
            for(ResultObject ro : model.getResult().getResult())
            {
                if(ro.getObject() instanceof BundleTemplateModel)
                {
                    bundleList.add((BundleTemplateModel)ro.getObject());
                }
            }
            if(model.getResult().getResult().size() == model.getResult().getTotalCount())
            {
                Collections.sort(bundleList, (Comparator<? super BundleTemplateModel>)new ParentBundleTemplateComparator());
            }
            return bundleList;
        }
        ItemModel parent = (ItemModel)((TypedObject)nodeObj).getObject();
        if(parent instanceof CatalogVersionModel)
        {
            List<BundleTemplateModel> rootBundles = getBundleTemplateService().getAllRootBundleTemplates((CatalogVersionModel)parent);
            bundleList.addAll(rootBundles);
            Collections.sort(bundleList, (Comparator<? super BundleTemplateModel>)new ParentBundleTemplateComparator());
            return bundleList;
        }
        bundleList = ((BundleTemplateModel)parent).getChildTemplates();
        return bundleList;
    }


    protected BundleTemplateService getBundleTemplateService()
    {
        return this.bundleTemplateService;
    }


    protected TypeService getTypeService()
    {
        return this.typeService;
    }
}
