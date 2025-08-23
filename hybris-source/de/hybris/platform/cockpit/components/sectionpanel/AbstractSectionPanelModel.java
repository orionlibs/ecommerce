package de.hybris.platform.cockpit.components.sectionpanel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zul.Menupopup;

public abstract class AbstractSectionPanelModel implements SectionPanelModel
{
    private static final Logger LOG = LoggerFactory.getLogger(AbstractSectionPanelModel.class.getName());
    private final List<SectionPanelListener> listeners = new ArrayList<>();
    private final List<Section> sections = new ArrayList<>();
    private String label;
    private String imageUrl;
    private final List<Message> messages = new ArrayList<>();


    public String getLabel()
    {
        return this.label;
    }


    public void setLabel(String label)
    {
        if((this.label == null && label != null) || (this.label != null && !this.label.equals(label)))
        {
            this.label = label;
            fireMessagesChanged();
        }
    }


    public void setImageUrl(String url)
    {
        if((this.imageUrl == null && url != null) || (this.imageUrl != null && !this.imageUrl.equals(url)))
        {
            this.imageUrl = url;
            fireMessagesChanged();
        }
    }


    public String getImageUrl()
    {
        return this.imageUrl;
    }


    public Section getSectionByLabel(String label)
    {
        Section ret = null;
        for(Section s : getSections())
        {
            if(s.getLabel().equals(label))
            {
                ret = s;
                break;
            }
        }
        return ret;
    }


    public List<Section> getSections()
    {
        return this.sections;
    }


    public void renameSection(Section section, String label)
    {
        if(section instanceof EditableSection)
        {
            ((EditableSection)section).setLabel(label);
            fireSectionRenamed(section);
        }
        else
        {
            LOG.warn("Cannot edit non-editable section.");
        }
    }


    public void addModelListener(SectionPanelListener sectionPanelListener)
    {
        if(sectionPanelListener != null && !this.listeners.contains(sectionPanelListener))
        {
            this.listeners.add(sectionPanelListener);
        }
    }


    public void removeModelListener(SectionPanelListener sectionPanelListener)
    {
        if(sectionPanelListener != null && this.listeners.contains(sectionPanelListener))
        {
            this.listeners.remove(sectionPanelListener);
        }
    }


    public void removeSection(Section section)
    {
        if(section != null && this.sections.contains(section))
        {
            this.sections.remove(section);
            fireSectionRemoved(section);
        }
    }


    public void removeAllSections()
    {
        if(this.sections != null && !this.sections.isEmpty())
        {
            Collection<Section> removed = new ArrayList<>(this.sections);
            this.sections.clear();
            for(Section r : removed)
            {
                fireSectionRemoved(r);
            }
        }
    }


    public void addSection(Section section)
    {
        if(section != null && !this.sections.contains(section))
        {
            this.sections.add(section);
            fireSectionAdded(section);
        }
    }


    public void addSections(List<Section> sections)
    {
        for(Section section : sections)
        {
            addSection(section);
        }
    }


    public void removeSections(Collection<Section> sections)
    {
        for(Section invalidSection : sections)
        {
            removeSection(invalidSection);
        }
    }


    public void setSections(List<Section> newSections)
    {
        if(newSections == null || newSections.isEmpty())
        {
            removeAllSections();
        }
        else
        {
            List<Section> toAdd = new ArrayList<>();
            List<Section> toRemove = new ArrayList<>(this.sections);
            toRemove.removeAll(newSections);
            for(Section section : newSections)
            {
                if(!this.sections.contains(section))
                {
                    toAdd.add(section);
                }
            }
            removeSections(toRemove);
            addSections(toAdd);
        }
    }


    public abstract void hideSection(Section paramSection);


    public abstract void showSection(Section paramSection);


    public void sectionUpdated(Section section)
    {
        fireSectionUpdate(section);
    }


    public void rowUpdated(SectionRow row)
    {
        fireRowUpdate(row);
    }


    protected void fireSectionAdded(Section section)
    {
        for(SectionPanelListener l : this.listeners)
        {
            l.sectionAdded(section);
        }
    }


    protected void fireSectionRenamed(Section section)
    {
        for(SectionPanelListener l : this.listeners)
        {
            l.labelChanged(section);
        }
    }


    protected void fireSectionRemoved(Section section)
    {
        for(SectionPanelListener l : this.listeners)
        {
            l.sectionRemoved(section);
        }
    }


    protected void fireAttacheValidationIconMenu(SectionRow row, Menupopup menuPopup)
    {
        for(SectionPanelListener listener : this.listeners)
        {
            listener.attacheValidationMenupopup(row, menuPopup);
        }
    }


    protected void fireSectionHide(Section section)
    {
        for(SectionPanelListener l : this.listeners)
        {
            l.sectionHide(section);
        }
    }


    protected void fireSectionShow(Section section)
    {
        for(SectionPanelListener l : this.listeners)
        {
            l.sectionShow(section);
        }
    }


    protected void fireSectionUpdate(Section section)
    {
        for(SectionPanelListener l : this.listeners)
        {
            l.sectionUpdate(section);
        }
    }


    protected void fireRowUpdate(SectionRow row)
    {
        for(SectionPanelListener l : this.listeners)
        {
            l.rowUpdate(row);
        }
    }


    protected void fireRowHide(SectionRow row)
    {
        for(SectionPanelListener l : this.listeners)
        {
            l.rowHide(row);
        }
    }


    protected void fireRowShow(SectionRow row)
    {
        for(SectionPanelListener l : this.listeners)
        {
            l.rowShow(row);
        }
    }


    protected void fireRowStatusChange(SectionRow row, int status, String localizedMsg)
    {
        for(SectionPanelListener l : this.listeners)
        {
            l.rowStatusChange(row, status, localizedMsg);
        }
    }


    protected void fireSectionHeaderChange(Section section, int status)
    {
        for(SectionPanelListener l : this.listeners)
        {
            l.sectionHeaderStatusChange(section, status);
        }
    }


    protected void fireMessagesChanged()
    {
        for(SectionPanelListener l : this.listeners)
        {
            l.messagesChanged();
        }
    }


    protected void fireSectionMoved()
    {
        for(SectionPanelListener l : this.listeners)
        {
            l.sectionMoved();
        }
    }


    protected void fireRowMoved(Section source, Section target)
    {
        for(SectionPanelListener l : this.listeners)
        {
            l.sectionUpdate(source);
            if(!source.equals(target))
            {
                l.sectionUpdate(target);
            }
        }
    }


    public List<Message> getMessages()
    {
        return this.messages;
    }


    public void addMessage(Message msg)
    {
        getMessages().add(msg);
        fireMessagesChanged();
    }


    public void clearMessages()
    {
        getMessages().clear();
        fireMessagesChanged();
    }


    public void showMessage(Message msg)
    {
        msg.setVisible(true);
        fireMessagesChanged();
    }


    public void showAllMessages()
    {
        for(Message m : getMessages())
        {
            m.setVisible(true);
        }
        fireMessagesChanged();
    }


    public void refreshInfoContainer()
    {
        fireMessagesChanged();
    }


    public void hideMessage(Message msg)
    {
        msg.setVisible(false);
        fireMessagesChanged();
    }


    public void moveSection(Section section, int index)
    {
        int current = this.sections.indexOf(section);
        if(current < 0)
        {
            throw new IllegalArgumentException("Section " + section + " does not belong to model " + this);
        }
        int targetIndex = index;
        if(current != targetIndex)
        {
            this.sections.remove(section);
            if(index < 0)
            {
                this.sections.add(section);
            }
            else
            {
                this.sections.add(targetIndex, section);
            }
            fireSectionMoved();
        }
    }


    public Map<String, Object> getContext()
    {
        return Collections.EMPTY_MAP;
    }
}
