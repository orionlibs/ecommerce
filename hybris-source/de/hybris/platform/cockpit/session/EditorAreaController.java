package de.hybris.platform.cockpit.session;

import de.hybris.platform.cockpit.components.sectionpanel.Section;
import de.hybris.platform.cockpit.components.sectionpanel.SectionPanel;
import de.hybris.platform.cockpit.components.sectionpanel.SectionPanelEvent;
import de.hybris.platform.cockpit.components.sectionpanel.SectionPanelLabelRenderer;
import de.hybris.platform.cockpit.components.sectionpanel.SectionPanelModel;
import de.hybris.platform.cockpit.components.sectionpanel.SectionRenderer;
import de.hybris.platform.cockpit.components.sectionpanel.SectionRowRenderer;
import de.hybris.platform.cockpit.model.meta.EditorFactory;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import java.util.Map;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;

public interface EditorAreaController
{
    boolean initialize();


    SectionPanelModel getSectionPanelModel();


    void setSectionRowRenderer(SectionRowRenderer paramSectionRowRenderer);


    SectionRowRenderer getSectionRowRenderer();


    SectionPanelLabelRenderer getSectionPanelLabelRenderer();


    SectionRenderer getSectionRenderer();


    Component getOnLaterComponent();


    void showAllSections();


    void resetSectionPanelModel();


    void updateEditorRequest(TypedObject paramTypedObject, PropertyDescriptor paramPropertyDescriptor);


    UIEditorArea getModel();


    void setModel(UIEditorArea paramUIEditorArea);


    ObjectType getCreateFromTemplate();


    void setCreateFromTemplate(ObjectType paramObjectType);


    void setCreateFromTemplate(ObjectType paramObjectType, Map<String, ? extends Object> paramMap);


    void setCreateFromTemplate(ObjectType paramObjectType, Map<String, ? extends Object> paramMap, boolean paramBoolean);


    void setEditorFactory(EditorFactory paramEditorFactory);


    EditorFactory getEditorFactory();


    void onLater(Event paramEvent);


    void onMessageClicked(SectionPanelEvent paramSectionPanelEvent);


    void onRowHide(SectionPanelEvent paramSectionPanelEvent);


    void onRowMoved(SectionPanelEvent paramSectionPanelEvent);


    void onRowShow(SectionPanelEvent paramSectionPanelEvent);


    void onSectionHide(SectionPanelEvent paramSectionPanelEvent);


    void onSectionMoved(SectionPanelEvent paramSectionPanelEvent);


    void onSectionShow(SectionPanelEvent paramSectionPanelEvent);


    void onSectionOpen(SectionPanelEvent paramSectionPanelEvent);


    void onSectionClosed(SectionPanelEvent paramSectionPanelEvent);


    void onShowAllRows(Section paramSection, SectionPanel paramSectionPanel);
}
