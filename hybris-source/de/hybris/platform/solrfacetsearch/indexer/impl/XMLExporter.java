package de.hybris.platform.solrfacetsearch.indexer.impl;

import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.indexer.exceptions.ExporterException;
import de.hybris.platform.solrfacetsearch.indexer.spi.Exporter;
import de.hybris.platform.solrfacetsearch.indexer.xml.add.Add;
import de.hybris.platform.solrfacetsearch.indexer.xml.add.DocType;
import de.hybris.platform.solrfacetsearch.indexer.xml.add.FieldType;
import de.hybris.platform.solrfacetsearch.indexer.xml.add.ObjectFactory;
import de.hybris.platform.solrfacetsearch.indexer.xml.delete.Delete;
import de.hybris.platform.util.Utilities;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.SolrInputField;

public class XMLExporter implements Exporter
{
    private static final String TEMP_ENCODING = "UTF-8";
    public static final String SOLR_JAXB_ADD_CONTEXT = "de.hybris.platform.solrfacetsearch.indexer.xml.add";
    public static final String SOLR_JAXB_DELETE_CONTEXT = "de.hybris.platform.solrfacetsearch.indexer.xml.delete";
    public static final String EXPORT_SUB_DIR = "solrExport";
    private static final String UPDATE_BASE_FILE_NAME = "update";
    private static final String DELETE_BASE_FILE_NAME = "delete";
    private static final Logger LOG = Logger.getLogger(XMLExporter.class.getName());
    private final ObjectFactory addObjectFactory = new ObjectFactory();
    private final ObjectFactory delObjectFactory = new ObjectFactory();


    public void exportToDeleteFromIndex(Collection<String> pksForDelete, FacetSearchConfig facetSearchConfig, IndexedType indexedType) throws ExporterException
    {
        IndexConfig indexConfig = facetSearchConfig.getIndexConfig();
        String exportDirPath = getExportDirPath(indexConfig);
        verifyCreateFolder(exportDirPath);
        Delete toMarshallDoc = prepareDeleteXMLDoc(pksForDelete);
        writeToXMLFile(exportDirPath, toMarshallDoc, indexConfig, "de.hybris.platform.solrfacetsearch.indexer.xml.delete", indexedType.getUniqueIndexedTypeCode(), ExportMode.DELETE);
    }


    public void exportToUpdateIndex(Collection<SolrInputDocument> solrDocuments, FacetSearchConfig facetSearchConfig, IndexedType indexedType) throws ExporterException
    {
        IndexConfig indexConfig = facetSearchConfig.getIndexConfig();
        doExport(solrDocuments, indexConfig, indexedType, ExportMode.UPDATE);
    }


    protected void writeToXMLFile(String exportDirPath, Object jaxbDocument, IndexConfig indexConfig, String jaxbContext, String typeName, ExportMode exportMode) throws ExporterException
    {
        File exportFile = prepareExportFile(typeName, exportMode, exportDirPath);
        LOG.info("Writing export result to: " + exportFile.getPath());
        try
        {
            FileOutputStream fos = new FileOutputStream(exportFile);
            try
            {
                OutputStreamWriter osw = new OutputStreamWriter(fos, getEncoding(indexConfig));
                try
                {
                    JAXBContext context = JAXBContext.newInstance(jaxbContext);
                    Marshaller marshaller = context.createMarshaller();
                    marshaller.setProperty("jaxb.formatted.output", Boolean.TRUE);
                    marshaller.marshal(jaxbDocument, osw);
                    osw.close();
                }
                catch(Throwable throwable)
                {
                    try
                    {
                        osw.close();
                    }
                    catch(Throwable throwable1)
                    {
                        throwable.addSuppressed(throwable1);
                    }
                    throw throwable;
                }
                fos.close();
            }
            catch(Throwable throwable)
            {
                try
                {
                    fos.close();
                }
                catch(Throwable throwable1)
                {
                    throwable.addSuppressed(throwable1);
                }
                throw throwable;
            }
        }
        catch(FileNotFoundException e)
        {
            LOG.error("File for export can not be created. File path = " + exportDirPath);
            throw new ExporterException("File for export can not be created. File path = " + exportDirPath, e);
        }
        catch(UnsupportedEncodingException e)
        {
            LOG.error("Problem with encoding during JAXB marshalling. " + e);
            throw new ExporterException("Uncorrect encoding for marshaller.", e);
        }
        catch(JAXBException e)
        {
            LOG.error("JAXB exception occured during marshall.");
            throw new ExporterException("JAXB exception occured during marshall.", e);
        }
        catch(IOException e)
        {
            LOG.error("Unexpected problem with writing the file.");
            throw new ExporterException("Unexpected problem with writing the file.", e);
        }
    }


    protected Delete prepareDeleteXMLDoc(Collection<String> pksForDelete)
    {
        Delete deleteRoot = this.delObjectFactory.createDelete();
        List<String> ids = deleteRoot.getId();
        for(String pk : pksForDelete)
        {
            if(StringUtils.isNotEmpty(pk))
            {
                ids.add(pk);
            }
        }
        return deleteRoot;
    }


    protected Add prepareXMLDoc(Collection<SolrInputDocument> solrDocuments)
    {
        Add addRoot = this.addObjectFactory.createAdd();
        List<DocType> docs = addRoot.getDoc();
        for(SolrInputDocument solrInputDocument : solrDocuments)
        {
            DocType exDoc = this.addObjectFactory.createDocType();
            List<FieldType> fields = exDoc.getField();
            for(SolrInputField solrInputField : solrInputDocument)
            {
                if(solrInputField != null)
                {
                    FieldType exField = this.addObjectFactory.createFieldType();
                    exField.setName(solrInputField.getName());
                    exField.setValue((solrInputField.getValue() == null) ? "" : solrInputField.getValue().toString());
                    fields.add(exField);
                }
            }
            docs.add(exDoc);
        }
        return addRoot;
    }


    protected String getExportDirPath(IndexConfig indexConfig) throws ExporterException
    {
        String confExportDirPath = indexConfig.getExportPath();
        if(StringUtils.isEmpty(confExportDirPath))
        {
            LOG.info("Export path was not set in configuration. Indexer try to get path from");
            String platformTempDir = Utilities.getPlatformConfig().getSystemConfig().getTempDir().getPath();
            if(StringUtils.isEmpty(platformTempDir))
            {
                LOG.error("Export path was not specified neither in configuration nor in HYBRIS_TEMP_DIR");
                throw new ExporterException("Unspecified export path.");
            }
            confExportDirPath = platformTempDir + platformTempDir + "solrExport";
        }
        LOG.info("Exporter dir path: " + confExportDirPath);
        return confExportDirPath;
    }


    protected String getEncoding(IndexConfig indexConfig)
    {
        return "UTF-8";
    }


    protected File prepareExportFile(String typeName, ExportMode exportMode, String dirPath) throws ExporterException
    {
        Format dateFormat = new SimpleDateFormat("yyyy-MM-dd-HHmmss", Locale.getDefault());
        String fileDateStr = dateFormat.format(new Date());
        StringBuilder filePrefix = new StringBuilder();
        if(exportMode == ExportMode.UPDATE)
        {
            filePrefix.append("update");
        }
        else if(exportMode == ExportMode.DELETE)
        {
            filePrefix.append("delete");
        }
        else
        {
            throw new IllegalArgumentException("Invalid export mode " + exportMode);
        }
        filePrefix.append('_').append(typeName).append('_').append(fileDateStr).append('_');
        try
        {
            return File.createTempFile(filePrefix.toString(), ".xml", new File(dirPath));
        }
        catch(IOException e)
        {
            LOG.error("Can not create unique file for indexer. ");
            throw new ExporterException(e);
        }
    }


    protected void verifyCreateFolder(String path) throws ExporterException
    {
        try
        {
            if(path != null)
            {
                File folder = new File(path);
                if((!folder.exists() || !folder.isDirectory() || !folder.canWrite()) && !folder.mkdirs())
                {
                    throw new IOException("Failed to create Directory: " + folder.getPath());
                }
            }
        }
        catch(Exception e)
        {
            LOG.error("Problem accessing/creating the folder: \"" + path + "\"");
            throw new ExporterException("Uncorrect destination folder for indexer files. " + path, e);
        }
    }


    protected void doExport(Collection<SolrInputDocument> solrDocuments, IndexConfig indexConfig, IndexedType indexedType, ExportMode exportMode) throws ExporterException
    {
        String exportDirPath = getExportDirPath(indexConfig);
        verifyCreateFolder(exportDirPath);
        LOG.debug("Writing export result to directory " + exportDirPath);
        Add toMarchalDoc = prepareXMLDoc(solrDocuments);
        writeToXMLFile(exportDirPath, toMarchalDoc, indexConfig, "de.hybris.platform.solrfacetsearch.indexer.xml.add", indexedType.getUniqueIndexedTypeCode(), exportMode);
    }
}
