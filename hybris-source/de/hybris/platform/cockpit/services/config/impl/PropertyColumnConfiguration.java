package de.hybris.platform.cockpit.services.config.impl;

import de.hybris.platform.cockpit.model.editor.ReferenceUIEditor;
import de.hybris.platform.cockpit.model.editor.UIEditor;
import de.hybris.platform.cockpit.model.editor.impl.DefaultTextUIEditor;
import de.hybris.platform.cockpit.model.listview.CellRenderer;
import de.hybris.platform.cockpit.model.listview.ValueHandler;
import de.hybris.platform.cockpit.model.listview.impl.DefaultColumnDescriptor;
import de.hybris.platform.cockpit.model.listview.impl.DefaultTextCellRenderer;
import de.hybris.platform.cockpit.model.listview.impl.DefaultValueHandler;
import de.hybris.platform.cockpit.model.meta.DefaultPropertyEditorDescriptor;
import de.hybris.platform.cockpit.model.meta.EditorFactory;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.PropertyEditorDescriptor;
import de.hybris.platform.cockpit.model.meta.impl.ItemAttributePropertyDescriptor;
import de.hybris.platform.cockpit.model.search.SearchType;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.TypeTools;
import de.hybris.platform.core.model.c2l.LanguageModel;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zkplus.spring.SpringUtil;

public class PropertyColumnConfiguration extends AbstractColumnConfiguration
{
    private static final Logger LOG = LoggerFactory.getLogger(PropertyColumnConfiguration.class);
    private final PropertyDescriptor propertyDescriptor;
    private ValueHandler valueHandler = null;
    private List<LanguageModel> languages;
    private EditorFactory editorFactory = null;
    private TypeService typeService = null;


    public PropertyColumnConfiguration(PropertyDescriptor propertyDescriptor)
    {
        if(propertyDescriptor == null)
        {
            throw new IllegalArgumentException("PropertyDescriptor can not be null.");
        }
        this.propertyDescriptor = propertyDescriptor;
        loadValues();
    }


    private void loadValues()
    {
        this.name = this.propertyDescriptor.getQualifier();
        PropertyDescriptor.Occurrence occurrence = this.propertyDescriptor.getOccurence();
        if(occurrence.equals(PropertyDescriptor.Occurrence.REQUIRED))
        {
            this.visible = true;
        }
        else
        {
            this.visible = false;
        }
        this.sortable = false;
        if((this.propertyDescriptor instanceof ItemAttributePropertyDescriptor && (
                        TypeTools.primitiveValue(((ItemAttributePropertyDescriptor)this.propertyDescriptor).getLastAttributeDescriptor()
                                        .getWritable()) || TypeTools.primitiveValue(((ItemAttributePropertyDescriptor)this.propertyDescriptor)
                                        .getLastAttributeDescriptor().getInitial()))) || this.propertyDescriptor
                        .isWritable())
        {
            this.editable = true;
        }
        else
        {
            this.editable = false;
        }
        this.selectable = true;
    }


    public UIEditor getCellEditor()
    {
        UIEditor uIEditor1, editor = null;
        DefaultTextUIEditor defaultTextUIEditor = new DefaultTextUIEditor();
        if(this.propertyDescriptor != null)
        {
            EditorFactory editorFactory = getEditorFactory();
            String editorType = this.propertyDescriptor.getEditorType();
            Collection<PropertyEditorDescriptor> matchingEditorDescriptors = editorFactory.getMatchingEditorDescriptors(editorType);
            PropertyEditorDescriptor editorDescriptor = null;
            if(matchingEditorDescriptors == null || matchingEditorDescriptors.isEmpty())
            {
                LOG.warn("No matching editors found. Using default one.");
            }
            else
            {
                editorDescriptor = matchingEditorDescriptors.iterator().next();
                if(getEditor() == null)
                {
                    if("BOOLEAN".equals(this.propertyDescriptor.getEditorType()) && editorDescriptor instanceof DefaultPropertyEditorDescriptor && ((DefaultPropertyEditorDescriptor)editorDescriptor)
                                    .getEditors().containsKey("dropdown"))
                    {
                        uIEditor1 = editorDescriptor.createUIEditor("dropdown");
                    }
                    else if("REFERENCE".equals(this.propertyDescriptor.getEditorType()) && editorDescriptor instanceof DefaultPropertyEditorDescriptor && ((DefaultPropertyEditorDescriptor)editorDescriptor)
                                    .getEditors().containsKey("browserContextEditor") &&
                                    !PropertyDescriptor.Multiplicity.SINGLE.equals(this.propertyDescriptor.getMultiplicity()))
                    {
                        uIEditor1 = editorDescriptor.createUIEditor("browserContextEditor");
                    }
                    else
                    {
                        uIEditor1 = editorDescriptor.createUIEditor("single");
                    }
                }
                else
                {
                    uIEditor1 = editorDescriptor.createUIEditor(getEditor());
                }
                if(uIEditor1 instanceof ReferenceUIEditor)
                {
                    SearchType rootSearchType = null;
                    SearchType rootType = getRootSearchType(this.propertyDescriptor);
                    if(rootType == null)
                    {
                        LOG.warn("Could not root type for reference editor (Reason: No root search type could be retrieved for property descriptor '" + this.propertyDescriptor + "').");
                    }
                    else
                    {
                        ((ReferenceUIEditor)uIEditor1).setRootType((ObjectType)rootType);
                        ((ReferenceUIEditor)uIEditor1).setRootSearchType((ObjectType)rootSearchType);
                    }
                }
            }
        }
        return uIEditor1;
    }


    public CellRenderer getCellRenderer()
    {
        if(super.getCellRenderer() == null)
        {
            setCellRenderer((CellRenderer)new DefaultTextCellRenderer());
        }
        return super.getCellRenderer();
    }


    public DefaultColumnDescriptor getColumnDescriptor()
    {
        String name = this.propertyDescriptor.getName();
        if(StringUtils.isBlank(name))
        {
            name = "[" + this.propertyDescriptor.getQualifier() + "]";
        }
        DefaultColumnDescriptor colDescr = new DefaultColumnDescriptor(name);
        colDescr.setEditable(this.editable);
        colDescr.setSelectable(this.selectable);
        colDescr.setSortable(this.sortable);
        colDescr.setVisible(this.visible);
        return colDescr;
    }


    public ValueHandler getValueHandler()
    {
        if(this.valueHandler == null)
        {
            this.valueHandler = (ValueHandler)new DefaultValueHandler(this.propertyDescriptor);
        }
        return this.valueHandler;
    }


    public PropertyDescriptor getPropertyDescriptor()
    {
        return this.propertyDescriptor;
    }


    public List<LanguageModel> getLanguages()
    {
        return this.languages;
    }


    public void setLanguages(List<LanguageModel> languages)
    {
        this.languages = languages;
    }


    public boolean isLocalized()
    {
        return this.propertyDescriptor.isLocalized();
    }


    protected EditorFactory getEditorFactory()
    {
        if(this.editorFactory == null)
        {
            this.editorFactory = (EditorFactory)SpringUtil.getBean("EditorFactory");
        }
        return this.editorFactory;
    }


    protected TypeService getTypeService()
    {
        if(this.typeService == null)
        {
            this.typeService = UISessionUtils.getCurrentSession().getTypeService();
        }
        return this.typeService;
    }


    protected SearchType getRootSearchType(PropertyDescriptor propDescr)
    {
        SearchType searchType = null;
        String valueTypeCode = getTypeService().getValueTypeCode(propDescr);
        if(valueTypeCode != null)
        {
            try
            {
                searchType = UISessionUtils.getCurrentSession().getSearchService().getSearchType(valueTypeCode);
            }
            catch(Exception e)
            {
                LOG.warn("Could not get search type for property descriptor (Reason: '" + e.getMessage() + "').");
            }
        }
        return searchType;
    }


    public void setCellEditor(UIEditor editor)
    {
    }


    public int hashCode()
    {
        return getPropertyDescriptor().hashCode();
    }


    public boolean equals(Object obj)
    {
        return (obj != null && obj instanceof PropertyColumnConfiguration &&
                        getPropertyDescriptor().equals(((PropertyColumnConfiguration)obj).getPropertyDescriptor()));
    }
}
