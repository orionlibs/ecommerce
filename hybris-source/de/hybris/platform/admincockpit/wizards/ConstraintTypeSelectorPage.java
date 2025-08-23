package de.hybris.platform.admincockpit.wizards;

import de.hybris.platform.admincockpit.services.ResourceAwareConfiguration;
import de.hybris.platform.admincockpit.services.TypeAwareResourceResolver;
import de.hybris.platform.cockpit.session.impl.CreateContext;
import de.hybris.platform.cockpit.session.impl.TemplateListEntry;
import de.hybris.platform.cockpit.wizards.Wizard;
import de.hybris.platform.cockpit.wizards.generic.GenericTypeSelectorPage;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.SimpleListModel;

@Deprecated
public class ConstraintTypeSelectorPage extends GenericTypeSelectorPage implements ResourceAwareConfiguration
{
    private ListitemRenderer listRenderer;
    private TypeAwareResourceResolver<String> resolver;


    public void setListRenderer(ListitemRenderer listRenderer)
    {
        this.listRenderer = listRenderer;
    }


    public ConstraintTypeSelectorPage(String pageTitle, Wizard wizard, CreateContext createContext)
    {
        super(pageTitle, wizard, createContext);
        this.listRenderer = (ListitemRenderer)new ConstraintTypeAwareListitemRenderer(this);
    }


    private List<TemplateListEntry> getInstanceableObjectTypes()
    {
        List<TemplateListEntry> types = new LinkedList<>();
        for(TemplateListEntry subType : getTemplateListEntry())
        {
            if(isValid(subType))
            {
                types.add(subType);
            }
        }
        Collections.sort(types, (Comparator<? super TemplateListEntry>)new Object(this));
        return types;
    }


    public Component createRepresentationItself()
    {
        this.pageContent.getChildren().clear();
        Div firstStep = new Div();
        firstStep.setSclass("typeSelectorPageContainer");
        firstStep.setParent((Component)this.pageContent);
        Listbox listbox = new Listbox();
        listbox.setSclass("typSelectorCmsWizardPage");
        listbox.setWidth("100%");
        List<TemplateListEntry> templateEntries = getInstanceableObjectTypes();
        if(templateEntries.isEmpty())
        {
            firstStep.appendChild((Component)new Label(Labels.getLabel("security.permision_denied")));
            this.wizard.setShowNext(false);
        }
        else
        {
            listbox.setModel((ListModel)new SimpleListModel(templateEntries));
            listbox.setParent((Component)firstStep);
        }
        listbox.setItemRenderer(this.listRenderer);
        listbox.addEventListener("onSelect", (EventListener)new Object(this, listbox));
        return (Component)this.pageContainer;
    }


    @Required
    public void setTypeAwareResourceResolver(TypeAwareResourceResolver<String> resolver)
    {
        this.resolver = resolver;
    }
}
