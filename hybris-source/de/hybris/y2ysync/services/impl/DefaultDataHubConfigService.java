package de.hybris.y2ysync.services.impl;

import com.google.common.base.Preconditions;
import de.hybris.deltadetection.model.StreamConfigurationContainerModel;
import de.hybris.deltadetection.model.StreamConfigurationModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.Utilities;
import de.hybris.y2ysync.model.Y2YStreamConfigurationContainerModel;
import de.hybris.y2ysync.model.Y2YStreamConfigurationModel;
import de.hybris.y2ysync.services.DataHubConfigService;
import de.hybris.y2ysync.services.DataHubExtGenerationConfig;
import java.io.StringWriter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Required;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class DefaultDataHubConfigService implements DataHubConfigService
{
    private ModelService modelService;


    public String createModelDefinitions(Y2YStreamConfigurationModel streamConfig, DataHubExtGenerationConfig dataHubExtConfig)
    {
        Preconditions.checkState(!this.modelService.isNew(streamConfig), "Stream config must be persisted");
        return renderModelDefinitions(streamConfig.getContainer(), dataHubExtConfig);
    }


    public String createModelDefinitions(Y2YStreamConfigurationModel streamConfig)
    {
        Preconditions.checkState(!this.modelService.isNew(streamConfig), "Stream config must be persisted");
        return createModelDefinitions(streamConfig, null);
    }


    public String createModelDefinitions(Y2YStreamConfigurationContainerModel container)
    {
        Preconditions.checkState(!this.modelService.isNew(container), "Stream configuration container must be persisted");
        return renderModelDefinitions((StreamConfigurationContainerModel)container);
    }


    public String createDataHubExtension(Y2YStreamConfigurationContainerModel container, DataHubExtGenerationConfig dataHubExtConfig)
    {
        Preconditions.checkState(!this.modelService.isNew(container), "Stream configuration container must be persisted");
        Preconditions.checkState((container.getConfigurations().size() > 0), "Stream configuration container must have assigned at least one configuration");
        return renderModelDefinitions((StreamConfigurationContainerModel)container, dataHubExtConfig);
    }


    private String renderModelDefinitions(StreamConfigurationContainerModel container)
    {
        return renderModelDefinitions(container, null);
    }


    private String renderModelDefinitions(StreamConfigurationContainerModel container, DataHubExtGenerationConfig dataHubExtConfig)
    {
        List<StreamConfigurationModel> configurations = (List<StreamConfigurationModel>)container.getConfigurations().stream().sorted(Comparator.comparing(StreamConfigurationModel::getStreamId)).collect(Collectors.toList());
        Document document = getXmlDocument();
        Element rootElement = getRootElement(document, container.getId());
        document.appendChild(rootElement);
        if(dataHubExtConfig == null || dataHubExtConfig.isGenerateRawItems())
        {
            rootElement.appendChild(getRawItems(document, configurations));
        }
        if(dataHubExtConfig == null || dataHubExtConfig.isGenerateCanonicalItems())
        {
            rootElement.appendChild(getCanonicalItems(document, configurations));
        }
        if(dataHubExtConfig == null || dataHubExtConfig.isGenerateTargetItems())
        {
            rootElement.appendChild(getTargetSystem(document, dataHubExtConfig, container));
        }
        return getXmlString(document, (dataHubExtConfig != null && dataHubExtConfig.isPrettyFormat()));
    }


    private Element getTargetSystem(Document doc, DataHubExtGenerationConfig dataHubExtConfig, StreamConfigurationContainerModel container)
    {
        TargetSystemModelRenderer targetSystemModelRenderer = new TargetSystemModelRenderer(this, doc);
        return targetSystemModelRenderer.getTargetSystems(dataHubExtConfig, container);
    }


    private Document getXmlDocument()
    {
        try
        {
            DocumentBuilderFactory builder = DocumentBuilderFactory.newInstance();
            builder.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            builder.setFeature("http://xml.org/sax/features/external-general-entities", false);
            DocumentBuilder documentBuilder = builder.newDocumentBuilder();
            return documentBuilder.newDocument();
        }
        catch(ParserConfigurationException e)
        {
            throw new IllegalStateException(e);
        }
    }


    private Element getRootElement(Document doc, String extName)
    {
        Element extension = doc.createElementNS("http://www.hybris.com/schema/", "extension");
        extension.setAttribute("name", extName);
        return extension;
    }


    private Element getRawItems(Document doc, List<StreamConfigurationModel> streamConfigurations)
    {
        RawModelRenderer renderer = new RawModelRenderer(doc);
        return renderer.getRawItems(streamConfigurations);
    }


    private Element getCanonicalItems(Document doc, List<StreamConfigurationModel> streamConfigurations)
    {
        CanonicalModelRenderer renderer = new CanonicalModelRenderer(doc);
        return renderer.getCanonicalItems(streamConfigurations);
    }


    private Element getTargetItems(Document doc, List<StreamConfigurationModel> streamConfigurations)
    {
        TargetModelRenderer renderer = new TargetModelRenderer(doc);
        return renderer.getTargetItems(streamConfigurations);
    }


    private String getXmlString(Document document, boolean prettyFormat)
    {
        StringWriter stringWriter = new StringWriter();
        try
        {
            TransformerFactory tf = Utilities.getTransformerFactory();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty("indent", "yes");
            transformer.setOutputProperty("omit-xml-declaration", "yes");
            if(prettyFormat)
            {
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            }
            DOMSource source = new DOMSource(document);
            StreamResult streamResult = new StreamResult(stringWriter);
            transformer.transform(source, streamResult);
            return streamResult.getWriter().toString();
        }
        catch(TransformerException e)
        {
            throw new IllegalStateException(e);
        }
        finally
        {
            IOUtils.closeQuietly(stringWriter);
        }
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }
}
