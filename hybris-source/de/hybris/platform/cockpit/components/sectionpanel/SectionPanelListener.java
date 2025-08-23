package de.hybris.platform.cockpit.components.sectionpanel;

import org.zkoss.zul.Menupopup;

public interface SectionPanelListener
{
    void expanded(Section paramSection, boolean paramBoolean);


    void labelChanged(Section paramSection);


    void labelChanged(SectionRow paramSectionRow);


    void sectionRemoved(Section paramSection);


    void sectionAdded(Section paramSection);


    void sectionHide(Section paramSection);


    void sectionShow(Section paramSection);


    void sectionUpdate(Section paramSection);


    void sectionMoved();


    void rowHide(SectionRow paramSectionRow);


    void rowShow(SectionRow paramSectionRow);


    void rowUpdate(SectionRow paramSectionRow);


    void rowStatusChange(SectionRow paramSectionRow, int paramInt);


    void sectionHeaderStatusChange(Section paramSection, int paramInt);


    void rowStatusChange(SectionRow paramSectionRow, int paramInt, String paramString);


    void attacheValidationMenupopup(SectionRow paramSectionRow, Menupopup paramMenupopup);


    void messagesChanged();


    void modelClear();
}
