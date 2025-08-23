package de.hybris.platform.cockpit.components.navigationarea;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import java.util.List;

public interface SectionSelectorSection extends SelectorSection
{
    List<SectionSelectorSection> getSubSections();


    void setSubSections(List<SectionSelectorSection> paramList);


    SectionSelectorSection getParentSection();


    void setParentSection(SectionSelectorSection paramSectionSelectorSection);


    TypedObject getRelatedObject();


    void setRelatedObject(TypedObject paramTypedObject);


    void refreshView();


    boolean isSubSectionsVisible();


    void disable();


    void enable();


    void clear();


    boolean isMultiselect();
}
