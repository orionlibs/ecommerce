package de.hybris.bootstrap.xml;

import java.io.File;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

public class XMLSchemaValidator
{
    private File xmlfile;
    private File xsdfile;


    public void setXmlFile(File xmlfile)
    {
        if(!xmlfile.exists())
        {
            throw new IllegalArgumentException("Can not find xml file " + xmlfile);
        }
        this.xmlfile = xmlfile;
    }


    public void setXsdFile(File xsdfile)
    {
        if(!xsdfile.exists())
        {
            System.out.println("Can not find xsd file " + xsdfile);
        }
        else
        {
            this.xsdfile = xsdfile;
        }
    }


    public void validate()
    {
        if(this.xmlfile != null)
        {
            try
            {
                ErrorChecker errors = new ErrorChecker(this);
                if(this.xsdfile != null)
                {
                    SchemaFactory schemaFactory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
                    schemaFactory.setProperty("http://javax.xml.XMLConstants/property/accessExternalSchema", "");
                    schemaFactory.setProperty("http://javax.xml.XMLConstants/property/accessExternalDTD", "");
                    Source schemaSource = new StreamSource(this.xsdfile);
                    Schema schema = schemaFactory.newSchema(schemaSource);
                    Validator validator = schema.newValidator();
                    validator.setErrorHandler((ErrorHandler)errors);
                    validator.validate(new StreamSource(this.xmlfile));
                }
                else
                {
                    System.out.println("Skipped validation of " + this.xmlfile + " in cause of missing xsd schema!");
                }
                if(errors.warningMessages.length() > 0)
                {
                    System.out.println("WARNING(S): [" + this.xmlfile + "]");
                    System.out.println("\n" + errors.warningMessages.toString());
                }
                if(errors.errorMessages.length() > 0)
                {
                    System.out.println("\nERROR(S): [" + this.xmlfile + "]");
                    System.out.println("\n" + errors.errorMessages.toString());
                    throw new IllegalArgumentException("Encountered validation errors at " + this.xmlfile);
                }
            }
            catch(Exception e)
            {
                System.out.println("\nERROR(S): [" + this.xmlfile + "]");
                String message = "";
                if(e instanceof SAXParseException)
                {
                    SAXParseException spe = (SAXParseException)e;
                    message = message + "line:" + message + ",column:" + spe.getLineNumber() + " : ";
                }
                throw new IllegalArgumentException(message + message);
            }
        }
        else
        {
            throw new IllegalArgumentException("Property xmlfile has to be specified.");
        }
    }
}
