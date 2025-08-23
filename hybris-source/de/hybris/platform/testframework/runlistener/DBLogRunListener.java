package de.hybris.platform.testframework.runlistener;

import de.hybris.platform.util.Config;
import de.hybris.platform.util.Utilities;
import java.io.File;
import org.apache.log4j.Logger;
import org.junit.runner.Description;
import org.junit.runner.notification.RunListener;

public class DBLogRunListener extends RunListener
{
    private static final Logger LOG = Logger.getLogger(DBLogRunListener.class);
    private DBLogConfig logConfig;


    public void testStarted(Description description) throws Exception
    {
        String className = description.getClassName();
        String methodName = description.getMethodName();
        String id = String.valueOf(System.currentTimeMillis());
        File logDir = new File(Utilities.getPlatformConfig().getSystemConfig().getLogDir().getAbsolutePath() + "s");
        logDir.mkdirs();
        String logFile = (new File(logDir, className + "_" + className + "_" + methodName + ".log")).getAbsolutePath();
        LOG.info(">>>> " + logFile);
        this
                        .logConfig = new DBLogConfig(Config.getString("db.log.file.path", null), Config.getBoolean("db.log.active", false), Config.getBoolean("db.log.appendStackTrace", false), Config.getString("db.log.excludecategories", null), Config.getString("db.log.includecategories", null));
        Config.setParameter("db.log.file.path", logFile);
        Config.setParameter("db.log.active", "true");
        Config.setParameter("db.log.appendStackTrace", "true");
        Config.setParameter("db.log.excludecategories", null);
        Config.setParameter("db.log.includecategories", null);
    }


    public void testFinished(Description description) throws Exception
    {
        Config.setParameter("db.log.file.path", this.logConfig.getLogFile());
        Config.setParameter("db.log.active", Boolean.toString(this.logConfig.isDbLogActive()));
        Config.setParameter("db.log.appendStackTrace", Boolean.toString(this.logConfig.isDbAppendStackTrace()));
        Config.setParameter("db.log.excludecategories", this.logConfig.getExcludedCategories());
        Config.setParameter("db.log.includecategories", this.logConfig.getIncludedCategories());
    }
}
