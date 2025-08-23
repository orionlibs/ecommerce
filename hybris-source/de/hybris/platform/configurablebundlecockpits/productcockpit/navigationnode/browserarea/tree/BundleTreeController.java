package de.hybris.platform.configurablebundlecockpits.productcockpit.navigationnode.browserarea.tree;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.collections.ListUtils;
import org.zkoss.zul.Treeitem;

public class BundleTreeController
{
    private final List<List<Integer>> openedItems = Lists.newArrayList();


    public void captureOpenedTreeitem(Treeitem treeitem)
    {
        List<Integer> pathToRoot = getPathToRoot(treeitem);
        if(treeitem != null && treeitem.isOpen())
        {
            this.openedItems.add(pathToRoot);
        }
        else
        {
            int index = -1;
            for(List<Integer> existing : this.openedItems)
            {
                index++;
                if(ListUtils.isEqualList(existing, pathToRoot))
                {
                    break;
                }
            }
            if(index > -1)
            {
                this.openedItems.remove(index);
            }
        }
    }


    private List<Integer> getPathToRoot(Treeitem treeItem)
    {
        List<Integer> ret = new ArrayList<>();
        Treeitem currentItem = treeItem;
        while(currentItem != null)
        {
            ret.add(Integer.valueOf(currentItem.indexOf()));
            if(!currentItem.isOpen() || !currentItem.isVisible())
            {
                ret.clear();
                break;
            }
            currentItem = currentItem.getParentItem();
        }
        Collections.reverse(ret);
        return ret;
    }


    public List<List<Integer>> getOpenedPath()
    {
        return this.openedItems;
    }
}
