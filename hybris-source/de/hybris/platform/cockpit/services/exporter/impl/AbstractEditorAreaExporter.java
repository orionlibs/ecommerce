package de.hybris.platform.cockpit.services.exporter.impl;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.config.EditorSectionConfiguration;
import de.hybris.platform.cockpit.services.config.jaxb.editor.preview.ObjectFactory;
import de.hybris.platform.cockpit.services.config.jaxb.editor.preview.SectionsListType;
import de.hybris.platform.cockpit.services.exporter.EditorAreaExporter;
import de.hybris.platform.cockpit.services.xmlprovider.XmlDataProvider;
import java.io.ByteArrayOutputStream;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public abstract class AbstractEditorAreaExporter implements EditorAreaExporter
{
    private static final Logger LOG = LoggerFactory.getLogger(AbstractEditorAreaExporter.class);
    protected static final String JAXB_CONTEXT_PACKAGE = "de.hybris.platform.cockpit.services.config.jaxb.editor.preview";
    private String preferencesTitle;
    private String dataSourceExpParamName;
    private final ObjectFactory objFactory = new ObjectFactory();


    protected String getPreferencesTitle()
    {
        return this.preferencesTitle;
    }


    protected byte[] generateXml(List<EditorSectionConfiguration> ediorsSections, TypedObject curObj)
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        SectionsListType sectionsList = this.objFactory.createSectionsListType();
        for(EditorSectionConfiguration editorSection : ediorsSections)
        {
            if(editorSection.isVisible() && editorSection.isPrintable())
            {
                XmlDataProvider sectionXmlDataProvider = editorSection.getXmlDataProvider();
                Object xmlSection = sectionXmlDataProvider.generateAsXml(editorSection, curObj);
                sectionsList.getSection().add(xmlSection);
            }
        }
        try
        {
            JAXBContext jaxbCtx = JAXBContext.newInstance("de.hybris.platform.cockpit.services.config.jaxb.editor.preview");
            Marshaller marshaller = jaxbCtx.createMarshaller();
            marshaller.setProperty("jaxb.formatted.output", Boolean.TRUE);
            marshaller.marshal(this.objFactory.createRoot(sectionsList), outputStream);
        }
        catch(JAXBException e)
        {
            LOG.error("Can't marshall xml for editor area report generation", (Throwable)e);
        }
        return outputStream.toByteArray();
    }


    @Required
    public void setPreferencesTitle(String preferencesTitle)
    {
        this.preferencesTitle = preferencesTitle;
    }


    protected String getDataSourceExpParamName()
    {
        return this.dataSourceExpParamName;
    }


    @Required
    public void setDataSourceExpParamName(String dataSourceExpression)
    {
        this.dataSourceExpParamName = dataSourceExpression;
    }
}
