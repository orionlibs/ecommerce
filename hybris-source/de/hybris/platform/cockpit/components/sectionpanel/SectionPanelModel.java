package de.hybris.platform.cockpit.components.sectionpanel;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface SectionPanelModel
{
    String getLabel();


    String getImageUrl();


    List<Message> getMessages();


    List<Section> getSections();


    Map<String, Object> getContext();


    void addModelListener(SectionPanelListener paramSectionPanelListener);


    void removeModelListener(SectionPanelListener paramSectionPanelListener);


    void showSection(Section paramSection);


    void addSection(Section paramSection);


    void addSections(List<Section> paramList);


    void setSections(List<Section> paramList);


    void removeSection(Section paramSection);


    void removeSections(Collection<Section> paramCollection);


    void removeAllSections();


    void initialize();


    void update();
}
