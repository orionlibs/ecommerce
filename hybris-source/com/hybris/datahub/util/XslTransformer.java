package com.hybris.datahub.util;

import com.hybris.datahub.log.Log;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.slf4j.Logger;

public class XslTransformer
{
    private static final Logger LOGGER = (Logger)Log.getLogger(XslTransformer.class);
    private static final TransformerFactory FACTORY = TransformerFactory.newInstance();
    private final Transformer transformer;
    private Source xmlSource;


    private XslTransformer(Transformer xFormer)
    {
        this.transformer = xFormer;
    }


    public static XslTransformer transform(String pathToXsl) throws IOException, TransformerConfigurationException
    {
        return createTransformer(toSource(pathToXsl));
    }


    public static XslTransformer transform(Path pathToXsl) throws IOException, TransformerConfigurationException
    {
        return createTransformer(toSource(pathToXsl));
    }


    private static XslTransformer createTransformer(Source xsl) throws TransformerConfigurationException
    {
        Transformer transformer = FACTORY.newTransformer(xsl);
        return new XslTransformer(transformer);
    }


    public XslTransformer withEncoding(String encoding)
    {
        this.transformer.setOutputProperty("encoding", encoding);
        return this;
    }


    public XslTransformer withEncoding(Charset encoding)
    {
        return withEncoding(encoding.name());
    }


    public XslTransformer withOutputFormat(String format)
    {
        this.transformer.setOutputProperty("method", format);
        return this;
    }


    public XslTransformer withIndent()
    {
        this.transformer.setOutputProperty("indent", "yes");
        return this;
    }


    public XslTransformer withoutIndent()
    {
        this.transformer.setOutputProperty("indent", "no");
        return this;
    }


    public XslTransformer withXmlDeclaration()
    {
        this.transformer.setOutputProperty("omit-xml-declaration", "no");
        return this;
    }


    public XslTransformer withoutXmlDeclaration()
    {
        this.transformer.setOutputProperty("omit-xml-declaration", "yes");
        return this;
    }


    public XslTransformer from(String xmlFile) throws IOException
    {
        this.xmlSource = toSource(xmlFile);
        return this;
    }


    public XslTransformer from(Path xmlFile) throws IOException
    {
        this.xmlSource = toSource(xmlFile);
        return this;
    }


    private static Source toSource(String filePath) throws IOException
    {
        return toSource(toPath(filePath));
    }


    private static Path toPath(String filePath)
    {
        return Paths.get(filePath, new String[0]);
    }


    private static Source toSource(Path path) throws IOException
    {
        Reader reader = Files.newBufferedReader(path, Charset.defaultCharset());
        return new StreamSource(reader);
    }


    public Path to(String filePath) throws TransformerException, IOException
    {
        return to(toPath(filePath));
    }


    public Path to(Path file) throws TransformerException, IOException
    {
        Result res = toResult(file);
        to(res);
        return file;
    }


    private void to(Result result) throws TransformerException
    {
        this.transformer.transform(this.xmlSource, result);
    }


    private Result toResult(Path file) throws IOException
    {
        Writer writer = Files.newBufferedWriter(file, getOutputEncoding(), new java.nio.file.OpenOption[0]);
        return new StreamResult(writer);
    }


    public String getOutputFormat()
    {
        return this.transformer.getOutputProperty("method");
    }


    public Charset getOutputEncoding()
    {
        String enc = this.transformer.getOutputProperty("encoding");
        return Charset.forName(enc);
    }


    public boolean isOutputIndented()
    {
        return Boolean.getBoolean(this.transformer.getOutputProperty("indent"));
    }


    public static void main(String[] args)
    {
        if(args.length < 2 || "help".equalsIgnoreCase(args[0]))
        {
            usage();
        }
        String xslPath = args[1];
        try
        {
            if(args.length < 3)
            {
                Path outPath = toPath(args[0]);
                Path srcPath = backup(outPath);
                transform(xslPath).from(srcPath).to(outPath);
            }
            else
            {
                transform(xslPath).from(args[0]).to(args[2]);
            }
        }
        catch(Exception e)
        {
            LOGGER.error(e.getMessage(), e);
            usage();
            System.exit(1);
        }
        System.exit(0);
    }


    private static Path backup(Path path) throws IOException
    {
        Path backup = Paths.get(path.getParent().toString(), new String[] {path.getFileName().toString() + ".bak"});
        return Files.copy(path, backup, new CopyOption[] {StandardCopyOption.REPLACE_EXISTING});
    }


    private static void usage()
    {
        LOGGER.info("Transforms an input file using the specified XSL:");
        LOGGER.info("");
        LOGGER.info("\tXslTransformer <path_to_source_file> <path_to_xsl> [<path_to_transformed_file>]");
        LOGGER.info("");
        LOGGER.info("If <path_to_transformed_file> is not specified, the transformed file will be saved");
        LOGGER.info("in the <path_to_source_file> file. The source file will be backed up by");
        LOGGER.info("changing its extension");
    }
}
