package de.hybris.platform.cockpit.components.navigationarea;

import de.hybris.platform.cockpit.components.sectionpanel.AbstractSectionPanelModel;
import de.hybris.platform.cockpit.components.sectionpanel.Section;
import de.hybris.platform.cockpit.components.sectionpanel.SectionPanelModel;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.SearchBrowserModel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DefaultSectionSelectorSection extends AbstractSelectorSection implements SectionSelectorSection
{
    protected List<SectionSelectorSection> subSections = new ArrayList<>();
    protected SectionSelectorSection parentSection = null;
    protected TypedObject relatedObject = null;
    protected List<TypedObject> relatedObjects = new ArrayList<>();
    protected boolean multiselect = false;


    public void addToRelatedObjects(TypedObject relatedObject)
    {
        this.relatedObjects.add(relatedObject);
    }


    public void clear()
    {
        setRelatedObject(null);
    }


    public void disable()
    {
        setRelatedObject(null);
        setOpen(Boolean.FALSE.booleanValue());
    }


    public void updateItems()
    {
    }


    public void disableSubSections()
    {
        for(SectionSelectorSection childSection : this.subSections)
        {
            if(!childSection.getSubSections().isEmpty())
            {
                ((DefaultSectionSelectorSection)childSection).disableSubSections();
            }
            childSection.disable();
        }
    }


    public void enable()
    {
        setOpen(Boolean.TRUE.booleanValue());
    }


    public void enableSubSections()
    {
        for(SectionSelectorSection sec : this.subSections)
        {
            sec.enable();
        }
    }


    public SectionSelectorSection getParentSection()
    {
        return this.parentSection;
    }


    public TypedObject getRelatedObject()
    {
        return this.relatedObject;
    }


    public List<TypedObject> getRelatedObjects()
    {
        return this.relatedObjects;
    }


    protected SectionSelectorSection getRootSection()
    {
        SectionSelectorSection rootSection = getParentSection();
        if(rootSection == null)
        {
            return this;
        }
        while(rootSection.getParentSection() != null)
        {
            rootSection = rootSection.getParentSection();
        }
        return rootSection;
    }


    public List<SectionSelectorSection> getSubSections()
    {
        return this.subSections;
    }


    public boolean isMultiselect()
    {
        return this.multiselect;
    }


    public boolean isSubSectionsVisible()
    {
        Iterator<SectionSelectorSection> iterator = this.subSections.iterator();
        if(iterator.hasNext())
        {
            SectionSelectorSection subSection = iterator.next();
            return subSection.isOpen();
        }
        return false;
    }


    public void refreshView()
    {
        SectionPanelModel sectionPanelModel = getNavigationAreaModel().getNavigationArea().getSectionModel();
        if(sectionPanelModel instanceof AbstractSectionPanelModel)
        {
            ((AbstractSectionPanelModel)sectionPanelModel).sectionUpdated((Section)getRootSection());
        }
        BrowserModel browser = getNavigationAreaModel().getNavigationArea().getPerspective().getBrowserArea().getFocusedBrowser();
        if(browser instanceof SearchBrowserModel)
        {
            SearchBrowserModel searchBrowser = (SearchBrowserModel)browser;
            searchBrowser.setSimpleQuery("");
            searchBrowser.updateItems(0);
        }
    }


    public void removeToRelatedObjects(TypedObject relatedObject)
    {
        if(this.relatedObjects.contains(relatedObject))
        {
            this.relatedObjects.remove(relatedObject);
        }
    }


    public void selectionChanged()
    {
    }


    public void setMultiselect(boolean multiselect)
    {
        this.multiselect = multiselect;
    }


    public void setParentSection(SectionSelectorSection parentSection)
    {
        this.parentSection = parentSection;
    }


    public void setRelatedObject(TypedObject relatdObject)
    {
        this.relatedObject = relatdObject;
    }


    public void setRelatedObjects(List<TypedObject> relatedObjects)
    {
        this.relatedObjects = relatedObjects;
    }


    public void setSubSections(List<SectionSelectorSection> subSections)
    {
        for(SectionSelectorSection sect : subSections)
        {
            sect.setParentSection(this);
        }
        if(this.subSections != subSections)
        {
            this.subSections = subSections;
        }
    }
}
