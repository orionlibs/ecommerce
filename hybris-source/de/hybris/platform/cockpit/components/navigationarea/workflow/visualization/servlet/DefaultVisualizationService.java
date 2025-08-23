package de.hybris.platform.cockpit.components.navigationarea.workflow.visualization.servlet;

import com.mxgraph.canvas.mxSvgCanvas;
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.layout.mxParallelEdgeLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.util.mxCellRenderer;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxStylesheet;
import de.hybris.platform.cockpit.components.navigationarea.workflow.visualization.EdgeStyle;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

public class DefaultVisualizationService implements VisualizationService
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultVisualizationService.class);
    private static final String EDGE_STYLE = "edgeStyle=elbowEdgeStyle;elbow=vertical;strokeWidth=1;strokeColor=#634E4E;fontColor=#000000;fillColor=#ffffff;spacing=2;fontSize=14;fontStyle=3";
    private static final String NODE_STYLE_END = "NODE_STYLE_END";
    private static final String NODE_STYLE_START = "NODE_STYLE_START";
    private static final String NODE_STYLE_MIDDLE_LABEL = "NODE_STYLE_MIDDLE_LABEL";
    private static final String NODE_STYLE_INNER_NODE = "NODE_STYLE_INNER_NODE";
    private static final long DEFAULT_NODE_HEIGHT = 30L;
    private static final long DEFAULT_LABEL_NODE_HEIGHT = 20L;
    private static final long DEFAULT_NODE_WIDTH = 100L;
    private List<GraphConverter> converters;
    private I18NService i18nService;
    private SessionService sessionService;


    public String prepareVisualization(Object elementToShow, EdgeStyle edgeStyle, double scale, String lang) throws Exception
    {
        return (String)this.sessionService.executeInLocalView((SessionExecutionBody)new Object(this, lang, elementToShow, edgeStyle, scale));
    }


    private String createGraph(GraphRepresentation graphModel, double scale) throws Exception
    {
        mxGraph graph = new mxGraph();
        mxCell parent = (mxCell)graph.getDefaultParent();
        graph.getModel().beginUpdate();
        try
        {
            mxStylesheet style = prepareCustomStylesForGraph();
            graph.setStylesheet(style);
            mxCell start = null;
            mxCell end = null;
            if(graphModel.isShowStart())
            {
                start = (mxCell)graph.insertVertex(parent, null, "", 0.0D, 0.0D, 10.0D, 10.0D, "NODE_STYLE_START");
            }
            if(graphModel.isShowEnd())
            {
                end = (mxCell)graph.insertVertex(parent, null, "", 0.0D, 0.0D, 10.0D, 10.0D, "NODE_STYLE_END");
            }
            Set<Vertex> vertexes = graphModel.getVertexes();
            Map<Vertex, mxCell> vertexMap = addVertexesToGraph(graphModel, graph, parent, start, end, vertexes);
            addEdgesToGraph(graph, parent, vertexMap, graphModel);
            layoutGraph(graph);
        }
        catch(Exception e)
        {
            LOG.warn(e.getMessage(), e);
        }
        finally
        {
            graph.getModel().endUpdate();
        }
        mxSvgCanvas canvas = (mxSvgCanvas)mxCellRenderer.drawCells(graph, null, scale, null, (mxCellRenderer.CanvasFactory)new Object(this));
        Document document = canvas.getDocument();
        TransformerFactory transformerFactory = TransformerFactory.newInstance("com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl", null);
        transformerFactory.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", true);
        transformerFactory.setAttribute("http://javax.xml.XMLConstants/property/accessExternalDTD", "");
        transformerFactory.setAttribute("http://javax.xml.XMLConstants/property/accessExternalStylesheet", "");
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty("omit-xml-declaration", "no");
        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(document), new StreamResult(writer));
        String output = writer.getBuffer().toString().replaceAll("\n|\r", "");
        return output;
    }


    private Map<Vertex, mxCell> addVertexesToGraph(GraphRepresentation graphModel, mxGraph graph, mxCell parent, mxCell start, mxCell end, Set<Vertex> vertexes)
    {
        Map<Vertex, mxCell> vertexMap = new HashMap<>();
        for(Vertex vertex : vertexes)
        {
            String label = getEncodedLabel(vertex.getLabel());
            vertexMap.put(vertex, (mxCell)graph.insertVertex(parent, null, label, 0.0D, 0.0D, computeNodeWidth(label), 30.0D, "NODE_STYLE_INNER_NODE;" + mxConstants.STYLE_FILLCOLOR + "=" + vertex
                            .getColor().getHexColor()));
            if(graphModel.isShowStart() && vertex.isStartPoint())
            {
                graph.insertEdge(parent, null, null, start, vertexMap.get(vertex), "edgeStyle=elbowEdgeStyle;elbow=vertical;strokeWidth=1;strokeColor=#634E4E;fontColor=#000000;fillColor=#ffffff;spacing=2;fontSize=14;fontStyle=3");
            }
            if(graphModel.isShowEnd() && vertex.isEndPoint())
            {
                graph.insertEdge(parent, null, null, vertexMap.get(vertex), end, "edgeStyle=elbowEdgeStyle;elbow=vertical;strokeWidth=1;strokeColor=#634E4E;fontColor=#000000;fillColor=#ffffff;spacing=2;fontSize=14;fontStyle=3");
            }
        }
        return vertexMap;
    }


    private String getEncodedLabel(String label)
    {
        return label;
    }


    private long computeNodeWidth(String label)
    {
        if(label != null)
        {
            return Math.max(100L, (20 + label.length() * 5));
        }
        return 100L;
    }


    private void addEdgesToGraph(mxGraph graph, mxCell parent, Map<Vertex, mxCell> vertexMap, GraphRepresentation graphModel)
    {
        Set<Edge> edges = graphModel.getEdges();
        EdgeStyle edgeStyle = graphModel.getEdgeStyle();
        switch(null.$SwitchMap$de$hybris$platform$cockpit$components$navigationarea$workflow$visualization$EdgeStyle[edgeStyle.ordinal()])
        {
            default:
                for(Edge edge : edges)
                {
                    String label = getEncodedLabel(edge.getLabel());
                    mxCell source = vertexMap.get(edge.getSource());
                    mxCell target = vertexMap.get(edge.getTarget());
                    mxCell middleVertex = (mxCell)graph.insertVertex(parent, null, label, 0.0D, 0.0D, computeNodeWidth(label), 20.0D, "NODE_STYLE_MIDDLE_LABEL");
                    graph.insertEdge(parent, null, null, source, middleVertex, "edgeStyle=elbowEdgeStyle;elbow=vertical;strokeWidth=1;strokeColor=#634E4E;fontColor=#000000;fillColor=#ffffff;spacing=2;fontSize=14;fontStyle=3");
                    graph.insertEdge(parent, null, null, middleVertex, target, "edgeStyle=elbowEdgeStyle;elbow=vertical;strokeWidth=1;strokeColor=#634E4E;fontColor=#000000;fillColor=#ffffff;spacing=2;fontSize=14;fontStyle=3");
                }
                return;
            case 2:
                break;
        }
        for(Edge edge : edges)
        {
            graph.insertEdge(parent, null, getEncodedLabel(edge.getLabel()), vertexMap.get(edge.getSource()), vertexMap
                            .get(edge.getTarget()), "edgeStyle=elbowEdgeStyle;elbow=vertical;strokeWidth=1;strokeColor=#634E4E;fontColor=#000000;fillColor=#ffffff;spacing=2;fontSize=14;fontStyle=3");
        }
    }


    private void layoutGraph(mxGraph graph)
    {
        graph.setAutoSizeCells(true);
        graph.setLabelsClipped(false);
        graph.setExtendParents(true);
        graph.setAutoOrigin(true);
        mxHierarchicalLayout layout = new mxHierarchicalLayout(graph);
        layout.execute(graph.getDefaultParent());
        mxParallelEdgeLayout parallels = new mxParallelEdgeLayout(graph);
        parallels.execute(graph.getDefaultParent());
    }


    private mxStylesheet prepareCustomStylesForGraph()
    {
        mxStylesheet style = new mxStylesheet();
        HashMap<String, Object> startStyle = new HashMap<>();
        startStyle.put(mxConstants.STYLE_SHAPE, "ellipse");
        startStyle.put(mxConstants.STYLE_FILLCOLOR, "#ffffff");
        style.putCellStyle("NODE_STYLE_START", startStyle);
        HashMap<String, Object> endStyle = new HashMap<>();
        endStyle.put(mxConstants.STYLE_SHAPE, "ellipse");
        endStyle.put(mxConstants.STYLE_FILLCOLOR, "#000000");
        style.putCellStyle("NODE_STYLE_END", endStyle);
        HashMap<String, Object> labelNodeStyle = new HashMap<>();
        labelNodeStyle.put(mxConstants.STYLE_SHAPE, "line");
        labelNodeStyle.put(mxConstants.STYLE_PERIMETER, "ellipsePerimeter");
        labelNodeStyle.put(mxConstants.STYLE_FILLCOLOR, "#FFFFFF");
        labelNodeStyle.put(mxConstants.STYLE_FONTCOLOR, "#4b5b70");
        labelNodeStyle.put(mxConstants.STYLE_LABEL_BACKGROUNDCOLOR, "#FFFFFF");
        labelNodeStyle.put(mxConstants.STYLE_LABEL_BORDERCOLOR, "#FFFFFF");
        labelNodeStyle.put(mxConstants.STYLE_STROKEWIDTH, "0");
        style.putCellStyle("NODE_STYLE_MIDDLE_LABEL", labelNodeStyle);
        HashMap<String, Object> innerStyle = new HashMap<>();
        innerStyle.put(mxConstants.STYLE_SHAPE, "rectangle");
        innerStyle.put(mxConstants.STYLE_AUTOSIZE, "1");
        innerStyle.put(mxConstants.STYLE_FONTCOLOR, "#FFFFFF");
        style.putCellStyle("NODE_STYLE_INNER_NODE", innerStyle);
        return style;
    }


    public void setConverters(List<GraphConverter> converters)
    {
        this.converters = converters;
    }


    public List<GraphConverter> getConverters()
    {
        return this.converters;
    }


    public void setI18nService(I18NService i18nService)
    {
        this.i18nService = i18nService;
    }


    public void setSessionService(SessionService sessionService)
    {
        this.sessionService = sessionService;
    }
}
