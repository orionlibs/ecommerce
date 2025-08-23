package de.hybris.platform.servicelayer.cronjob.impl;

import de.hybris.platform.cronjob.enums.JobLogLevel;
import de.hybris.platform.cronjob.model.JobLogModel;
import de.hybris.platform.cronjob.model.StepModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.i18n.FormatFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class DefaultJobLogConverter implements Converter<List<JobLogModel>, String>
{
    private static final String DATE_TIME_PATTERN = "dd.MM.yyyy HH:mm:ss";
    private static final Logger LOG = Logger.getLogger(DefaultJobLogConverter.class.getName());
    private FormatFactory formatFactory;
    private int maxMessageRows = 10;
    private String dateFormatPattern = "dd.MM.yyyy HH:mm:ss";


    public void setDateFormatPattern(String dateFormatPattern)
    {
        this.dateFormatPattern = dateFormatPattern;
    }


    public void setMaxMessageRows(int maxMessageRows)
    {
        this.maxMessageRows = maxMessageRows;
    }


    @Required
    public void setFormatFactory(FormatFactory formatFactory)
    {
        this.formatFactory = formatFactory;
    }


    public String convert(List<JobLogModel> logs) throws ConversionException
    {
        StringBuilder logtext = new StringBuilder();
        SimpleDateFormat dateformat = (SimpleDateFormat)this.formatFactory.createDateTimeFormat(2, -1);
        dateformat.applyPattern(this.dateFormatPattern);
        for(JobLogModel jobLog : logs)
        {
            JobLogLevel level = jobLog.getLevel();
            StringBuilder strLevel = new StringBuilder();
            if(level != null)
            {
                strLevel.append(level.getCode()).append(": ");
            }
            StepModel step = jobLog.getStep();
            StringBuilder strStep = new StringBuilder();
            if(step != null)
            {
                strStep.append(step.getCode()).append(": ");
            }
            logtext.append(dateformat.format(jobLog.getCreationtime())).append(": ").append(strLevel);
            logtext.append(strStep);
            try
            {
                String jlMessage = jobLog.getMessage();
                if(jlMessage == null)
                {
                    jlMessage = "<empty>";
                }
                BufferedReader buffreader = new BufferedReader(new StringReader(jlMessage));
                for(int i = 0; i < this.maxMessageRows && buffreader.ready(); i++)
                {
                    String line = buffreader.readLine();
                    if(line == null)
                    {
                        break;
                    }
                    logtext.append(line);
                    if(i == this.maxMessageRows - 1)
                    {
                        logtext.append(" ...");
                    }
                    logtext.append('\n');
                }
            }
            catch(IOException e)
            {
                LOG.error(e.getMessage(), e);
            }
        }
        return logtext.toString();
    }


    public String convert(List<JobLogModel> source, String prototype) throws ConversionException
    {
        throw new UnsupportedOperationException();
    }
}
