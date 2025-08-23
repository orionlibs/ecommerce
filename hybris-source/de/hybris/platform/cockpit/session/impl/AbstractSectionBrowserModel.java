package de.hybris.platform.cockpit.session.impl;

import de.hybris.platform.cockpit.components.contentbrowser.AbstractContentBrowser;
import de.hybris.platform.cockpit.events.impl.SectionModelListener;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.BrowserSectionModel;
import de.hybris.platform.cockpit.session.SectionBrowserModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public abstract class AbstractSectionBrowserModel extends AbstractAdvancedBrowserModel implements SectionBrowserModel
{
    private final List<BrowserSectionModel> browserSectionModels = new ArrayList<>();
    private SectionModelListener sectionModelListener = null;


    public BrowserSectionModel getBrowserSectionModel(int index)
    {
        BrowserSectionModel sectionModel = null;
        if(this.browserSectionModels.size() > index)
        {
            sectionModel = this.browserSectionModels.get(index);
        }
        return sectionModel;
    }


    public List<BrowserSectionModel> getBrowserSectionModels()
    {
        return Collections.unmodifiableList(this.browserSectionModels);
    }


    public void setSectionModelListener(SectionModelListener listener)
    {
        this.sectionModelListener = listener;
    }


    public SectionModelListener getSectionModelListener()
    {
        return this.sectionModelListener;
    }


    public void setBrowserSectionModels(List<? extends BrowserSectionModel> sectionModels)
    {
        this.browserSectionModels.clear();
        if(sectionModels != null && !sectionModels.isEmpty())
        {
            this.browserSectionModels.addAll(sectionModels);
        }
    }


    public List<Integer> getSelectedIndexes()
    {
        return Collections.EMPTY_LIST;
    }


    public List<TypedObject> getItems()
    {
        return Collections.EMPTY_LIST;
    }


    public TypedObject getItem(int index)
    {
        return null;
    }


    public void updateItems()
    {
    }


    public void blacklistItems(Collection<Integer> indexes)
    {
    }


    public void removeItems(Collection<Integer> indexes)
    {
    }


    public abstract Object clone() throws CloneNotSupportedException;


    public abstract AbstractContentBrowser createViewComponent();
}
