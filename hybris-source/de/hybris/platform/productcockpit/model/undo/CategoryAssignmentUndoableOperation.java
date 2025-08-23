package de.hybris.platform.productcockpit.model.undo;

import de.hybris.platform.catalog.constants.GeneratedCatalogConstants;
import de.hybris.platform.cockpit.events.CockpitEvent;
import de.hybris.platform.cockpit.events.impl.ItemChangedEvent;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.model.undo.UndoableOperation;
import de.hybris.platform.cockpit.model.undo.impl.CannotRedoException;
import de.hybris.platform.cockpit.model.undo.impl.CannotUndoException;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.TypeTools;
import de.hybris.platform.productcockpit.services.catalog.CatalogService;
import de.hybris.platform.util.Config;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.zkoss.util.resource.Labels;

public class CategoryAssignmentUndoableOperation implements UndoableOperation
{
    private static final String SEPARATOR = " | ";
    private static final String FROM_TO = " -> ";
    protected List<TypedObject> sourceCategories;
    protected List<TypedObject> targetCategories;
    protected final List<TypedObject> elements;
    protected boolean move;
    protected String undoPresentationName;
    protected String redoPresentationName;
    protected CatalogService productCockpitCatalogService;


    public CategoryAssignmentUndoableOperation(List<TypedObject> sourceCategories, List<TypedObject> targetCategories, List<TypedObject> elements, boolean move, CatalogService productCockpitCatalogService)
    {
        this
                        .sourceCategories = (sourceCategories == null) ? Collections.EMPTY_LIST : Collections.<TypedObject>unmodifiableList(sourceCategories);
        this
                        .targetCategories = (targetCategories == null) ? Collections.EMPTY_LIST : Collections.<TypedObject>unmodifiableList(targetCategories);
        this.elements = (elements == null) ? Collections.EMPTY_LIST : Collections.<TypedObject>unmodifiableList(elements);
        this.move = move;
        this.productCockpitCatalogService = productCockpitCatalogService;
    }


    public boolean canRedo()
    {
        return true;
    }


    public boolean canUndo()
    {
        return true;
    }


    public String getRedoPresentationName()
    {
        return Labels.getLabel("undo.operation.categoryassignment");
    }


    public String getUndoPresentationName()
    {
        return Labels.getLabel("undo.operation.categoryassignment");
    }


    public void redo() throws CannotRedoException
    {
        TypeService typeService = UISessionUtils.getCurrentSession().getTypeService();
        Set<PropertyDescriptor> subcatsPropSet = new HashSet<>();
        boolean threshold = (this.elements.size() > Config.getInt("cockpit.changeevents.threshold", 3));
        for(TypedObject element : this.elements)
        {
            String subcatsPropQuali = "categories";
            String supercatsPropQuali = "supercategories";
            if(TypeTools.checkInstanceOfProduct(typeService, element))
            {
                subcatsPropQuali = "products";
                supercatsPropQuali = GeneratedCatalogConstants.Attributes.Product.SUPERCATEGORIES;
            }
            else if(TypeTools.checkInstanceOfMedia(typeService, element))
            {
                subcatsPropQuali = "medias";
                supercatsPropQuali = GeneratedCatalogConstants.Attributes.Media.SUPERCATEGORIES;
            }
            subcatsPropSet.add(typeService.getPropertyDescriptor(GeneratedCatalogConstants.TC.CATEGORY + "." + GeneratedCatalogConstants.TC.CATEGORY));
            if(this.move)
            {
                this.productCockpitCatalogService.removeFromCategories(element, this.sourceCategories);
            }
            this.productCockpitCatalogService.addToCategories(element, this.targetCategories);
            if(!threshold)
            {
                UISessionUtils.getCurrentSession()
                                .sendGlobalEvent((CockpitEvent)new ItemChangedEvent(this, element, Collections.singleton(typeService.getPropertyDescriptor(GeneratedCatalogConstants.TC.CATEGORY + "." + GeneratedCatalogConstants.TC.CATEGORY))));
            }
        }
        if(threshold)
        {
            UISessionUtils.getCurrentSession().sendGlobalEvent((CockpitEvent)new ItemChangedEvent(this, null, Collections.EMPTY_LIST));
        }
        if(this.move)
        {
            for(TypedObject category : this.sourceCategories)
            {
                UISessionUtils.getCurrentSession().sendGlobalEvent((CockpitEvent)new ItemChangedEvent(this, category, subcatsPropSet));
            }
        }
        for(TypedObject category : this.targetCategories)
        {
            UISessionUtils.getCurrentSession().sendGlobalEvent((CockpitEvent)new ItemChangedEvent(this, category, subcatsPropSet));
        }
    }


    public void undo() throws CannotUndoException
    {
        TypeService typeService = UISessionUtils.getCurrentSession().getTypeService();
        Set<PropertyDescriptor> subcatsPropSet = new HashSet<>();
        boolean threshold = (this.elements.size() > Config.getInt("cockpit.changeevents.threshold", 3));
        for(TypedObject element : this.elements)
        {
            String subcatsPropQuali = "categories";
            String supercatsPropQuali = "supercategories";
            if(TypeTools.checkInstanceOfProduct(typeService, element))
            {
                subcatsPropQuali = "products";
                supercatsPropQuali = GeneratedCatalogConstants.Attributes.Product.SUPERCATEGORIES;
            }
            else if(TypeTools.checkInstanceOfMedia(typeService, element))
            {
                subcatsPropQuali = "medias";
                supercatsPropQuali = GeneratedCatalogConstants.Attributes.Media.SUPERCATEGORIES;
            }
            subcatsPropSet.add(typeService.getPropertyDescriptor(GeneratedCatalogConstants.TC.CATEGORY + "." + GeneratedCatalogConstants.TC.CATEGORY));
            if(this.move)
            {
                this.productCockpitCatalogService.addToCategories(element, this.sourceCategories);
            }
            this.productCockpitCatalogService.removeFromCategories(element, this.targetCategories);
            if(!threshold)
            {
                UISessionUtils.getCurrentSession()
                                .sendGlobalEvent((CockpitEvent)new ItemChangedEvent(this, element, Collections.singleton(typeService.getPropertyDescriptor(GeneratedCatalogConstants.TC.CATEGORY + "." + GeneratedCatalogConstants.TC.CATEGORY))));
            }
        }
        if(threshold)
        {
            UISessionUtils.getCurrentSession().sendGlobalEvent((CockpitEvent)new ItemChangedEvent(this, null, Collections.EMPTY_LIST));
        }
        if(this.move)
        {
            for(TypedObject category : this.sourceCategories)
            {
                UISessionUtils.getCurrentSession().sendGlobalEvent((CockpitEvent)new ItemChangedEvent(this, category, subcatsPropSet));
            }
        }
        for(TypedObject category : this.targetCategories)
        {
            UISessionUtils.getCurrentSession().sendGlobalEvent((CockpitEvent)new ItemChangedEvent(this, category, subcatsPropSet));
        }
    }


    public String getRedoContextDescription()
    {
        return getUndoContextDescription();
    }


    public String getUndoContextDescription()
    {
        StringBuilder ctxDescr = new StringBuilder();
        if(this.sourceCategories != null && this.sourceCategories.size() == 1 && this.targetCategories != null && this.targetCategories
                        .size() == 1)
        {
            if(this.elements != null && this.elements.size() == 1)
            {
                ctxDescr.append(UISessionUtils.getCurrentSession().getLabelService().getObjectTextLabel(this.elements.get(0)));
                ctxDescr.append(" | ");
            }
            ctxDescr.append(UISessionUtils.getCurrentSession().getLabelService().getObjectTextLabel(this.sourceCategories.get(0)));
            ctxDescr.append(" -> ");
            ctxDescr.append(UISessionUtils.getCurrentSession().getLabelService().getObjectTextLabel(this.targetCategories.get(0)));
        }
        return ctxDescr.toString();
    }
}
