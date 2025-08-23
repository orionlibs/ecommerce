package de.hybris.platform.cockpit.session.impl;

import de.hybris.platform.cockpit.components.navigationarea.NavigationPanelSection;
import de.hybris.platform.cockpit.components.navigationarea.NavigationSectionRenderer;
import de.hybris.platform.cockpit.components.navigationarea.renderer.AbstractNavigationAreaSectionRenderer;
import de.hybris.platform.cockpit.components.sectionpanel.Section;
import de.hybris.platform.cockpit.components.sectionpanel.SectionPanelModel;
import de.hybris.platform.cockpit.components.sectionpanel.SectionRenderer;
import de.hybris.platform.cockpit.events.CockpitEvent;
import de.hybris.platform.cockpit.events.CockpitEventAcceptor;
import de.hybris.platform.cockpit.session.FocusablePerspectiveArea;
import de.hybris.platform.cockpit.session.NavigationAreaListener;
import de.hybris.platform.cockpit.session.UICockpitPerspective;
import de.hybris.platform.cockpit.session.UINavigationArea;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract class AbstractUINavigationArea implements UINavigationArea
{
    private final List<NavigationAreaListener> listeners = new ArrayList<>();
    private SectionPanelModel sectionModel;
    private SectionRenderer sectionRenderer;
    private UICockpitPerspective perspective;
    private String viewURI;
    private String headerURI;
    private final List<CockpitEventAcceptor> cockpitEventAcceptors = new LinkedList<>();
    private String width;


    public void addAreaListener(NavigationAreaListener listener)
    {
        if(!this.listeners.contains(listener))
        {
            this.listeners.add(listener);
        }
    }


    public void removeAreaListener(NavigationAreaListener listener)
    {
        if(this.listeners.contains(listener))
        {
            this.listeners.remove(listener);
        }
    }


    public List<NavigationAreaListener> getListeners()
    {
        return this.listeners;
    }


    public void setSectionModel(SectionPanelModel sectionModel)
    {
        this.sectionModel = sectionModel;
    }


    public SectionPanelModel getSectionModel()
    {
        return this.sectionModel;
    }


    public SectionRenderer getSectionRenderer()
    {
        if(this.sectionRenderer == null)
        {
            this.sectionRenderer = (SectionRenderer)new NavigationSectionRenderer();
        }
        return this.sectionRenderer;
    }


    public void setSectionRenderer(SectionRenderer sectionRenderer)
    {
        this.sectionRenderer = sectionRenderer;
    }


    public void setPerspective(UICockpitPerspective perspective)
    {
        this.perspective = perspective;
    }


    public UICockpitPerspective getPerspective()
    {
        return this.perspective;
    }


    public void showAllSections()
    {
        for(Section section : getSectionModel().getSections())
        {
            getSectionModel().showSection(section);
        }
    }


    public String getViewURI()
    {
        return this.viewURI;
    }


    public void setViewURI(String viewURI)
    {
        this.viewURI = viewURI;
    }


    public UICockpitPerspective getManagingPerspective()
    {
        return getPerspective();
    }


    public String getHeaderURI()
    {
        return this.headerURI;
    }


    public void setHeaderURI(String headerURI)
    {
        this.headerURI = headerURI;
    }


    public boolean isFocused()
    {
        return equals(getManagingPerspective().getFocusedArea());
    }


    public void setFocus(boolean focus)
    {
        if(getManagingPerspective() != null)
        {
            getManagingPerspective().setFocusedArea((FocusablePerspectiveArea)this);
        }
    }


    public void addSection(Section section)
    {
        setNavigationArea2Renderer(section);
        if(getSectionModel() != null)
        {
            getSectionModel().addSection(section);
        }
    }


    public void addSections(List<Section> sections)
    {
        for(Section section : sections)
        {
            setNavigationArea2Renderer(section);
        }
        if(getSectionModel() != null)
        {
            getSectionModel().addSections(sections);
        }
    }


    public void removeAllSections()
    {
        if(getSectionModel() != null)
        {
            getSectionModel().removeAllSections();
        }
    }


    public void removeSection(Section section)
    {
        if(getSectionModel() != null)
        {
            getSectionModel().removeSection(section);
        }
    }


    public void removeSections(Collection<Section> sections)
    {
        if(getSectionModel() != null)
        {
            getSectionModel().removeSections(sections);
        }
    }


    public void setSections(List<Section> sections)
    {
        for(Section section : sections)
        {
            setNavigationArea2Renderer(section);
        }
        if(getSectionModel() != null)
        {
            getSectionModel().setSections(sections);
        }
    }


    private void setNavigationArea2Renderer(Section section)
    {
        if(section instanceof NavigationPanelSection && ((NavigationPanelSection)section).getRenderer() != null)
        {
            NavigationPanelSection navPanelSection = (NavigationPanelSection)section;
            if(navPanelSection.getRenderer() instanceof AbstractNavigationAreaSectionRenderer)
            {
                ((AbstractNavigationAreaSectionRenderer)navPanelSection.getRenderer()).setNavigationArea(this);
            }
        }
    }


    public void addCockpitEventAcceptor(CockpitEventAcceptor acceptor)
    {
        this.cockpitEventAcceptors.add(acceptor);
    }


    public void removeCockpitEventAcceptor(CockpitEventAcceptor acceptor)
    {
        this.cockpitEventAcceptors.remove(acceptor);
    }


    public void onCockpitEvent(CockpitEvent event)
    {
        for(CockpitEventAcceptor acceptor : this.cockpitEventAcceptors)
        {
            acceptor.onCockpitEvent(event);
        }
    }


    public abstract void initialize(Map<String, Object> paramMap);


    public abstract void update();


    public void setWidth(String width)
    {
        this.width = width;
    }


    public String getWidth()
    {
        return this.width;
    }
}
