package de.hybris.platform.cockpit.services.xmlprovider.impl;

import de.hybris.platform.catalog.model.classification.ClassificationAttributeValueModel;
import de.hybris.platform.classification.features.FeatureValue;
import de.hybris.platform.cockpit.model.editor.EditorHelper;
import de.hybris.platform.cockpit.model.listview.ValueHandler;
import de.hybris.platform.cockpit.model.listview.impl.DefaultColumnDescriptor;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.config.ColumnConfiguration;
import de.hybris.platform.cockpit.services.config.EditorRowConfiguration;
import de.hybris.platform.cockpit.services.config.EditorSectionConfiguration;
import de.hybris.platform.cockpit.services.config.ListViewConfiguration;
import de.hybris.platform.cockpit.services.config.UIConfigurationService;
import de.hybris.platform.cockpit.services.config.impl.DefaultEditorSectionConfiguration;
import de.hybris.platform.cockpit.services.config.jaxb.editor.preview.AttributeType;
import de.hybris.platform.cockpit.services.config.jaxb.editor.preview.AttributesType;
import de.hybris.platform.cockpit.services.config.jaxb.editor.preview.ColumnsTitlesType;
import de.hybris.platform.cockpit.services.config.jaxb.editor.preview.ObjectFactory;
import de.hybris.platform.cockpit.services.config.jaxb.editor.preview.ReferenceType;
import de.hybris.platform.cockpit.services.config.jaxb.editor.preview.RowType;
import de.hybris.platform.cockpit.services.config.jaxb.editor.preview.SectionType;
import de.hybris.platform.cockpit.services.config.jaxb.editor.preview.TableColumnType;
import de.hybris.platform.cockpit.services.config.jaxb.editor.preview.TableRowType;
import de.hybris.platform.cockpit.services.config.jaxb.editor.preview.TableRowsType;
import de.hybris.platform.cockpit.services.config.jaxb.editor.preview.TitleType;
import de.hybris.platform.cockpit.services.label.LabelService;
import de.hybris.platform.cockpit.services.media.MediaInfo;
import de.hybris.platform.cockpit.services.media.MediaInfoService;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.services.values.ValueHandlerException;
import de.hybris.platform.cockpit.services.values.ValueService;
import de.hybris.platform.cockpit.services.xmlprovider.TableColumnContainer;
import de.hybris.platform.cockpit.services.xmlprovider.XmlDataProvider;
import de.hybris.platform.cockpit.session.UISession;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.TypeTools;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.localization.Localization;
import java.io.File;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultXmlDataProvider implements XmlDataProvider
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultXmlDataProvider.class);
    private static final String COMMA = ", ";
    private final ObjectFactory objFactory = new ObjectFactory();
    private UIConfigurationService uiConfService;
    private MediaInfoService mediaInfoService;
    private ModelService modelService;
    private ValueService valueService;
    private LabelService labelService;
    private TypeService typeService;


    public SectionType generateAsXml(EditorSectionConfiguration editorSection, TypedObject curObj)
    {
        SectionType xmlSection = this.objFactory.createSectionType();
        List<EditorRowConfiguration> cockpitSectionRow = editorSection.getSectionRows();
        String label = (editorSection instanceof DefaultEditorSectionConfiguration) ? ((DefaultEditorSectionConfiguration)editorSection).getLabelWithFallback() : editorSection.getLabel();
        xmlSection.setName(label);
        for(EditorRowConfiguration editorRow : cockpitSectionRow)
        {
            if(editorRow.isVisible())
            {
                XmlDataProvider xmlDataProvider = editorRow.getXmlDataProvider();
                Object xmlRow = xmlDataProvider.generateAsXml(editorRow, curObj);
                xmlSection.getRow().add(xmlRow);
            }
        }
        xmlSection.setType(XmlDataProvider.SECTION_TYPE.simple.toString());
        return xmlSection;
    }


    public RowType generateAsXml(EditorRowConfiguration editorRow, TypedObject curObj)
    {
        RowType xmlRow = this.objFactory.createRowType();
        xmlRow.setType(XmlDataProvider.ROW_TYPE.simple.toString());
        PropertyDescriptor descriptor = editorRow.getPropertyDescriptor();
        Object rowValue = getPropertyValue(curObj, descriptor);
        rowValue = (rowValue == null) ? "" : rowValue;
        if(rowValue instanceof Collection)
        {
            Collection<TypedObject> coll = (Collection)rowValue;
            if(coll.isEmpty())
            {
                createSimpleRow(descriptor, xmlRow, "");
            }
            else
            {
                if("REFERENCE".equals(descriptor.getEditorType()))
                {
                    if("browserContextEditor".equals(editorRow.getEditor()))
                    {
                        return ctxAreaReference(xmlRow, editorRow, coll, curObj);
                    }
                    if("listViewReferenceEditor".equals(editorRow.getEditor()))
                    {
                        ObjectTemplate template = UISessionUtils.getCurrentSession().getTypeService().getBestTemplate(coll.iterator().next());
                        ListViewConfiguration conf = (ListViewConfiguration)getUiConfigurationService().getComponentConfiguration(template, "editorListViewSection", ListViewConfiguration.class);
                        List<? extends ColumnConfiguration> columnsConf = conf.getRootColumnGroupConfiguration().getAllColumnConfigurations();
                        return createOneLevelReferenceTableRow(xmlRow, coll, columnsConf, descriptor);
                    }
                }
                return createCollectionRow(descriptor, xmlRow, coll);
            }
        }
        else
        {
            if(rowValue instanceof FeatureValue)
            {
                return createSimpleRow(descriptor, xmlRow, getFeatureValueAsString((FeatureValue)rowValue));
            }
            if(rowValue instanceof TypedObject)
            {
                ItemModel itemModel = (ItemModel)((TypedObject)rowValue).getObject();
                if(itemModel instanceof MediaModel)
                {
                    createMediaRow(descriptor, xmlRow, (MediaModel)itemModel);
                }
                else
                {
                    createOtherRow(descriptor, xmlRow, (TypedObject)rowValue);
                }
            }
            else if(rowValue instanceof String)
            {
                createSimpleRow(descriptor, xmlRow, (String)rowValue);
            }
            else if(rowValue instanceof Date)
            {
                createSimpleRow(descriptor, xmlRow, getLocalizedDate((Date)rowValue));
            }
            else
            {
                LOG.debug("Unknow type for pdf editor area preview.");
                createSimpleRow(descriptor, xmlRow, TypeTools.getValueAsString(this.labelService, rowValue));
            }
        }
        return xmlRow;
    }


    protected Object getPropertyValue(TypedObject curObj, PropertyDescriptor descriptor)
    {
        return TypeTools.getPropertyValue(getValueService(), curObj, descriptor);
    }


    protected String getFeatureValueAsString(FeatureValue fValue)
    {
        Object value = fValue.getValue();
        if(value instanceof ClassificationAttributeValueModel)
        {
            value = ((ClassificationAttributeValueModel)value).getName();
        }
        else if(value instanceof String)
        {
            value = fValue.getValue();
        }
        else if(value instanceof Double)
        {
            NumberFormat instance = NumberFormat.getInstance(UISessionUtils.getCurrentSession().getLocale());
            instance.setMaximumFractionDigits(10);
            instance.setMinimumFractionDigits(0);
            instance.setGroupingUsed(false);
            value = instance.format(((Double)value).doubleValue());
        }
        else if(value instanceof Date)
        {
            value = getLocalizedDate((Date)value);
        }
        else if(value instanceof Boolean)
        {
            value = ((Boolean)value).booleanValue() ? Localization.getLocalizedString("featurevalue.true") : Localization.getLocalizedString("featurevalue.false");
        }
        else
        {
            LOG.warn("Can't render xml for feature value of type: " + value.getClass());
            value = fValue.getValue().toString();
        }
        String unit = (fValue.getUnit() == null) ? null : fValue.getUnit().getName();
        return (unit == null) ? (String)value : ("" + value + " " + value);
    }


    protected RowType ctxAreaReference(RowType xmlRow, EditorRowConfiguration editorRow, Collection<TypedObject> collection, TypedObject curObj)
    {
        String printoutas = editorRow.getPrintoutAs();
        PropertyDescriptor descriptor = editorRow.getPropertyDescriptor();
        ObjectTemplate template = getObjectTemplate(descriptor);
        template = processVariantTypeCheck(curObj, descriptor, template);
        ListViewConfiguration conf = (ListViewConfiguration)getUiConfigurationService().getComponentConfiguration(template, "listViewContentBrowserContext", ListViewConfiguration.class);
        List<? extends ColumnConfiguration> columnsConf = conf.getRootColumnGroupConfiguration().getAllColumnConfigurations();
        if(StringUtils.isEmpty(printoutas))
        {
            createOneLevelReferenceTableRow(xmlRow, collection, columnsConf, descriptor);
        }
        else
        {
            Map<String, ColumnConfiguration> columnsTitles = null;
            Object firstObject = collection.iterator().next();
            if(firstObject instanceof TypedObject)
            {
                for(TypedObject referencedObject : collection)
                {
                    String refTableTitle = getLabelService().getObjectTextLabelForTypedObject(referencedObject).trim();
                    Object value = getObjectAttributeValue(printoutas, referencedObject);
                    if(value instanceof Collection)
                    {
                        Collection<TypedObject> col = (Collection<TypedObject>)value;
                        if(col.isEmpty())
                        {
                            ReferenceType xmlReference = this.objFactory.createReferenceType();
                            xmlReference.setName(refTableTitle);
                            xmlRow.getReference().add(xmlReference);
                            xmlRow.setName(getName(descriptor));
                            continue;
                        }
                        if(columnsTitles == null)
                        {
                            columnsTitles = getColumnTitles(col.iterator().next(), null);
                        }
                        createDeeperReferenceTableRow(xmlRow, refTableTitle, col, columnsTitles, descriptor);
                        continue;
                    }
                    LOG.warn("Can't render xml for reference value of type: " + firstObject.getClass());
                }
            }
            else
            {
                LOG.warn("Can't render xml for collection of: " + firstObject.getClass());
            }
            xmlRow.setType(XmlDataProvider.ROW_TYPE.reference_table.toString());
        }
        return xmlRow;
    }


    protected ObjectTemplate processVariantTypeCheck(TypedObject curObj, PropertyDescriptor propDesc, ObjectTemplate template)
    {
        return EditorHelper.processVariantTypeCheck(template, curObj, propDesc, getTypeService());
    }


    protected Object getObjectAttributeValue(String printoutas, TypedObject referencedObject)
    {
        return TypeTools.getObjectAttributeValue(referencedObject, printoutas, getTypeService());
    }


    protected ObjectTemplate getObjectTemplate(PropertyDescriptor propDesc)
    {
        return TypeTools.getValueTypeAsObjectTemplate(propDesc, getTypeService());
    }


    public TypeService getTypeService()
    {
        return this.typeService;
    }


    protected RowType createSimpleRow(PropertyDescriptor descriptor, RowType xmlRow, String value)
    {
        xmlRow.setType(XmlDataProvider.ROW_TYPE.simple.toString());
        String name = getName(descriptor);
        AttributeType attrib = this.objFactory.createAttributeType();
        xmlRow.setAttribute(attrib);
        attrib.setName(name);
        attrib.setValue(value);
        return xmlRow;
    }


    protected String getName(PropertyDescriptor descriptor)
    {
        String name = descriptor.getName();
        if(name == null)
        {
            name = descriptor.getQualifier();
        }
        return name;
    }


    protected void createOtherRow(PropertyDescriptor descriptor, RowType xmlRow, TypedObject val)
    {
        String value = getLabelService().getObjectTextLabelForTypedObject(val);
        createSimpleRow(descriptor, xmlRow, value);
    }


    protected void createMediaRow(PropertyDescriptor descriptor, RowType xmlRow, MediaModel mediaModel)
    {
        String name = getName(descriptor);
        String value = getMediaValue(mediaModel);
        String imgUrl = getMediaImageUrl(mediaModel);
        AttributeType attrib = this.objFactory.createAttributeType();
        xmlRow.setAttribute(attrib);
        attrib.setName(name);
        attrib.setValue(value);
        attrib.setImgUrl(imgUrl);
        xmlRow.setType(XmlDataProvider.ROW_TYPE.media.toString());
    }


    protected String getMediaValue(MediaModel mediaModel)
    {
        String value = "";
        if(!getMediaInfoService().isWebMedia(mediaModel).booleanValue())
        {
            String realName = mediaModel.getRealFileName();
            value = mediaModel.getCode() + mediaModel.getCode();
        }
        return value;
    }


    protected RowType createCollectionRow(PropertyDescriptor descriptor, RowType xmlRow, Collection<TypedObject> val)
    {
        if(CollectionUtils.isNotEmpty(val) && val.iterator().next() instanceof TypedObject && ((TypedObject)val
                        .iterator().next()).getObject() instanceof MediaModel)
        {
            xmlRow.setType(XmlDataProvider.ROW_TYPE.attributes.toString());
            AttributesType attributesType = this.objFactory.createAttributesType();
            attributesType.setName(descriptor.getName());
            xmlRow.setAttributes(attributesType);
            for(TypedObject object : val)
            {
                MediaModel media = (MediaModel)object.getObject();
                AttributeType attributeType = this.objFactory.createAttributeType();
                attributeType.setName(media.getCode());
                attributeType.setValue("");
                attributeType.setImgUrl(getMediaImageUrl(media));
                attributesType.getAttribute().add(attributeType);
            }
            return xmlRow;
        }
        String collAsStr = getCollectionAsString(val);
        return createSimpleRow(descriptor, xmlRow, collAsStr);
    }


    protected RowType createOneLevelReferenceTableRow(RowType xmlRow, Collection<TypedObject> coll, List<? extends ColumnConfiguration> columnsConf, PropertyDescriptor descriptor)
    {
        Map<String, ColumnConfiguration> columnsTitles = getColumnTitles(coll.iterator().next(), columnsConf);
        List<List<TableColumnContainer>> tableRows = getTableRows(coll, columnsTitles);
        xmlRow.setName(getName(descriptor));
        xmlRow.setColumnsTitles(createTableTitlesSection(columnsTitles));
        xmlRow.setRows(createTableRowsSection(tableRows));
        xmlRow.setType(XmlDataProvider.ROW_TYPE.table.toString());
        return xmlRow;
    }


    protected void createDeeperReferenceTableRow(RowType xmlRow, String refTableTitle, Collection<TypedObject> rowsValues, Map<String, ColumnConfiguration> columnsTitles, PropertyDescriptor descriptor)
    {
        if(!rowsValues.isEmpty())
        {
            ReferenceType xmlReference = this.objFactory.createReferenceType();
            xmlReference.setName(refTableTitle);
            List<List<TableColumnContainer>> tableRows = getTableRows(rowsValues, columnsTitles);
            xmlReference.setColumnsTitles(createTableTitlesSection(columnsTitles));
            xmlReference.setRows(createTableRowsSection(tableRows));
            xmlRow.setName(getName(descriptor));
            xmlRow.getReference().add(xmlReference);
        }
    }


    protected ColumnsTitlesType createTableTitlesSection(Map<String, ColumnConfiguration> columnsTitles)
    {
        ColumnsTitlesType xmlColumnsTitles = this.objFactory.createColumnsTitlesType();
        Set<Map.Entry<String, ColumnConfiguration>> titlesAndConfs = columnsTitles.entrySet();
        for(Map.Entry<String, ColumnConfiguration> titleAndConf : titlesAndConfs)
        {
            String title = titleAndConf.getKey();
            TitleType xmlTitle = this.objFactory.createTitleType();
            xmlTitle.setValue(title);
            xmlColumnsTitles.getTitle().add(xmlTitle);
        }
        return xmlColumnsTitles;
    }


    protected TableRowsType createTableRowsSection(List<List<TableColumnContainer>> tableRows)
    {
        TableRowsType xmlRows = this.objFactory.createTableRowsType();
        for(List<TableColumnContainer> row : tableRows)
        {
            TableRowType xmlTableRow = this.objFactory.createTableRowType();
            for(TableColumnContainer colCont : row)
            {
                TableColumnType columnValue = this.objFactory.createTableColumnType();
                columnValue.setValue(colCont.getValue());
                xmlTableRow.getColumn().add(columnValue);
                columnValue.setType(colCont.getColumnType().toString());
            }
            xmlRows.getTableRow().add(xmlTableRow);
        }
        return xmlRows;
    }


    protected Map<String, ColumnConfiguration> getColumnTitles(TypedObject row, List<? extends ColumnConfiguration> columnsConf)
    {
        if(columnsConf == null)
        {
            ObjectTemplate template = getTypeService().getBestTemplate(row);
            ListViewConfiguration conf = (ListViewConfiguration)getUiConfigurationService().getComponentConfiguration(template, "listViewContentBrowserContext", ListViewConfiguration.class);
            columnsConf = conf.getRootColumnGroupConfiguration().getAllColumnConfigurations();
        }
        LinkedHashMap<String, ColumnConfiguration> columnsTitles = new LinkedHashMap<>();
        for(ColumnConfiguration cConf : columnsConf)
        {
            DefaultColumnDescriptor defaultColumnDescriptor = cConf.getColumnDescriptor();
            if(defaultColumnDescriptor.isVisible() && !(cConf instanceof de.hybris.platform.cockpit.components.listview.ActionColumnConfiguration))
            {
                String name = defaultColumnDescriptor.getName();
                columnsTitles.put(name, cConf);
            }
        }
        return columnsTitles;
    }


    protected List<List<TableColumnContainer>> getTableRows(Collection<TypedObject> rowValues, Map<String, ColumnConfiguration> columnsTitles)
    {
        List<List<TableColumnContainer>> rows = new ArrayList<>();
        List orderedRowValues = Arrays.asList(rowValues.toArray());
        for(TypedObject item : rowValues)
        {
            List<TableColumnContainer> row = new ArrayList<>(columnsTitles.size());
            Set<Map.Entry<String, ColumnConfiguration>> columns = columnsTitles.entrySet();
            for(Iterator<Map.Entry<String, ColumnConfiguration>> it = columns.iterator(); it.hasNext(); )
            {
                Map.Entry<String, ColumnConfiguration> column = it.next();
                ColumnConfiguration cConf = column.getValue();
                ValueHandler valHandl = cConf.getValueHandler();
                Object valObj = null;
                XmlDataProvider.TABLE_COLUMN_TYPE columnType = XmlDataProvider.TABLE_COLUMN_TYPE.simple;
                try
                {
                    if(valHandl == null)
                    {
                        if(cConf instanceof de.hybris.platform.cockpit.services.config.impl.LineNumberColumn)
                        {
                            valObj = String.valueOf(orderedRowValues.indexOf(item) + 1);
                        }
                        else
                        {
                            valObj = "";
                        }
                    }
                    else
                    {
                        valObj = valHandl.getValue(item);
                    }
                }
                catch(ValueHandlerException e)
                {
                    LOG.error("Couldn't retrive value for reference table of pdf preview of editor area.", (Throwable)e);
                }
                if(valObj instanceof TypedObject)
                {
                    ItemModel itemModel = (ItemModel)((TypedObject)valObj).getObject();
                    if(itemModel instanceof MediaModel)
                    {
                        if(getMediaInfoService().isWebMedia((MediaModel)itemModel).booleanValue())
                        {
                            valObj = getMediaImageUrl((MediaModel)itemModel);
                            columnType = XmlDataProvider.TABLE_COLUMN_TYPE.media;
                        }
                        else
                        {
                            valObj = getMediaValue((MediaModel)itemModel);
                        }
                    }
                    else
                    {
                        valObj = getLabelService().getObjectTextLabelForTypedObject((TypedObject)valObj);
                    }
                }
                else if(valObj instanceof Collection)
                {
                    valObj = getCollectionAsString((Collection)valObj);
                }
                else if(valObj instanceof Date)
                {
                    valObj = getLocalizedDate((Date)valObj);
                }
                String rowValue = (valObj == null) ? "" : TypeTools.getValueAsString(this.labelService, valObj);
                row.add(new TableColumnContainer(columnType, rowValue));
            }
            rows.add(row);
        }
        return rows;
    }


    protected String getCollectionAsString(Collection val)
    {
        StringBuffer value = new StringBuffer(100);
        if(!val.isEmpty())
        {
            Object firstObject = val.iterator().next();
            if(firstObject instanceof TypedObject)
            {
                ItemModel itemModel = (ItemModel)((TypedObject)firstObject).getObject();
                if(itemModel instanceof MediaModel)
                {
                    for(TypedObject object : val)
                    {
                        value.append(getMediaValue((MediaModel)object.getObject()));
                        value.append(", ");
                    }
                }
                else
                {
                    for(TypedObject object : val)
                    {
                        value.append(getLabelService().getObjectTextLabelForTypedObject(object));
                        value.append(", ");
                    }
                }
            }
            else if(firstObject instanceof FeatureValue)
            {
                for(FeatureValue object : val)
                {
                    value.append(getFeatureValueAsString(object));
                    value.append(", ");
                }
            }
            else
            {
                for(Object object : val)
                {
                    value.append(TypeTools.getValueAsString(this.labelService, object));
                    value.append(", ");
                }
            }
        }
        String result = value.toString();
        result = result.endsWith(", ") ? result.substring(0, result.length() - 2) : result;
        return result;
    }


    protected String getMediaImageUrl(MediaModel mediaModel)
    {
        String imgUrl = null;
        if(getMediaInfoService().isWebMedia(mediaModel).booleanValue())
        {
            try
            {
                Collection<File> files = ((Media)getModelService().getSource(mediaModel)).getFiles();
                imgUrl = files.isEmpty() ? "" : ((File)files.iterator().next()).getPath();
            }
            catch(NullPointerException npe)
            {
                imgUrl = "(celum asset)";
            }
        }
        else
        {
            String mime = mediaModel.getMime();
            MediaInfo mediaInfo = getMediaInfoService().getNonWebMediaInfo(mime);
            imgUrl = (mime == null) ? "" : ((mediaInfo == null) ? "" : mediaInfo.getIcon());
        }
        return imgUrl;
    }


    protected String getLocalizedDate(Date date)
    {
        Locale locale = getUISession().getLocale();
        DateFormat dateFormat = SimpleDateFormat.getDateTimeInstance(1, 3, locale);
        return dateFormat.format(date);
    }


    protected UISession getUISession()
    {
        return UISessionUtils.getCurrentSession();
    }


    @Required
    public void setLabelService(LabelService labelService)
    {
        this.labelService = labelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    @Required
    public void setValueService(ValueService valueService)
    {
        this.valueService = valueService;
    }


    @Required
    public void setUiConfigurationService(UIConfigurationService uiConfService)
    {
        this.uiConfService = uiConfService;
    }


    @Required
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }


    public void setMediaInfoService(MediaInfoService mediaInfoService)
    {
        this.mediaInfoService = mediaInfoService;
    }


    public MediaInfoService getMediaInfoService()
    {
        return this.mediaInfoService;
    }


    public ModelService getModelService()
    {
        return this.modelService;
    }


    public LabelService getLabelService()
    {
        return this.labelService;
    }


    public UIConfigurationService getUiConfigurationService()
    {
        return this.uiConfService;
    }


    private ValueService getValueService()
    {
        return this.valueService;
    }
}
