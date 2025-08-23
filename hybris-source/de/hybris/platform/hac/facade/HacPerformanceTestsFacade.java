package de.hybris.platform.hac.facade;

import de.hybris.platform.hac.performance.PerformanceTest;
import de.hybris.platform.jdbcwrapper.HybrisJdbcTemplate;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jnt.scimark2.commandline;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

public class HacPerformanceTestsFacade
{
    private static final Logger LOG = Logger.getLogger(HacPerformanceTestsFacade.class);
    @Autowired
    private Map<String, PerformanceTest> performanceTests;
    private HybrisJdbcTemplate jdbcTemplate;


    public synchronized List<Map<String, Object>> executeOverallTests(int seconds)
    {
        LOG.info("Executing overall tests");
        List<Map<String, Object>> results = new ArrayList<>();
        int count = 1;
        int size = this.performanceTests.size();
        for(Map.Entry<String, PerformanceTest> entry : this.performanceTests.entrySet())
        {
            PerformanceTest performanceTest = entry.getValue();
            LOG.info("Executing test " + count + " of " + size + ": [" + performanceTest.getTestName() + "] with given seconds per loop: " + seconds);
            results.add(performanceTest.computeScores(seconds));
            count++;
        }
        LOG.info("Finished overall tests");
        return results;
    }


    public Map<String, PerformanceTest> getOverallTests()
    {
        return this.performanceTests;
    }


    public synchronized Map<String, Object> executeSqlTest(String sql, Integer count, Integer seconds)
    {
        LOG.info("Running sql test...");
        Map<String, Object> result = new HashMap<>();
        try
        {
            long time1 = System.currentTimeMillis();
            long time2 = time1;
            long cnt = 0L;
            while(time1 + (seconds.intValue() * 1000) >= time2)
            {
                cnt++;
                this.jdbcTemplate.execute(sql);
                time2 = System.currentTimeMillis();
            }
            result.put("statementsCount", Long.valueOf(cnt));
            result.put("statementsPerSecond", Long.valueOf(cnt / (time2 - time1) / 1000L));
            result.put("timePerStatement", Double.valueOf(1000.0D / cnt / (time2 - time1) / 1000.0D));
        }
        catch(RuntimeException e)
        {
            result.put("error", e.getMessage());
        }
        LOG.info("Finished SQL Test");
        return result;
    }


    @Transactional("txManager")
    public synchronized Map<String, Object> executeSqlMaxTest()
    {
        LOG.info("Running sql max test...");
        Map<String, Object> result = new HashMap<>();
        createTestingTable();
        long t1 = System.currentTimeMillis();
        fillIn10000Rows();
        long t2 = System.currentTimeMillis();
        result.put("durationAdded", Long.valueOf(t2 - t1));
        fillIn10000RowsExecutingMaxFunction();
        long t3 = System.currentTimeMillis();
        result.put("durationAddedMax", Long.valueOf(t3 - t2));
        createIndexOnMaxtestTable();
        long t4 = System.currentTimeMillis();
        fillIn10000RowsExecutingMaxFunction();
        long t5 = System.currentTimeMillis();
        result.put("durationAddedMaxIndex", Long.valueOf(t5 - t4));
        dropTestingTable();
        return result;
    }


    private void fillIn10000RowsExecutingMaxFunction()
    {
        LOG.info("Fill in 10000 rows into testing table");
        for(int i = 0; i < 10000; i++)
        {
            int nr = ((Integer)this.jdbcTemplate.queryForObject("select max(nr) from maxtest", Integer.class)).intValue();
            nr++;
            this.jdbcTemplate.update("INSERT INTO maxtest ( id, nr ) VALUES ( 'row" + nr + "', " + nr + " ) ");
        }
    }


    private void fillIn10000Rows()
    {
        LOG.info("Fill in 10000 rows into testing table");
        for(int i = 0; i < 10000; i++)
        {
            this.jdbcTemplate.update("INSERT INTO maxtest ( id, nr ) VALUES ( 'row" + i + "', " + i + " ) ");
        }
    }


    private void createTestingTable()
    {
        LOG.info("Create testing table 'maxtests'");
        dropTestingTable();
        this.jdbcTemplate.update("CREATE TABLE maxtest ( id varchar(10) primary key , nr decimal(12,0) )");
    }


    private void dropTestingTable()
    {
        try
        {
            this.jdbcTemplate.update("DROP TABLE maxtest");
        }
        catch(Exception exception)
        {
        }
    }


    private void createIndexOnMaxtestTable()
    {
        LOG.info("Create index nrIdx on maxtest");
        this.jdbcTemplate.update("create index nrIdx on maxtest ( nr )");
    }


    public synchronized Map<String, String> executeLinpack()
    {
        LOG.info("Running Linpack Test...");
        Map<String, String> map = new HashMap<>();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PrintStream OUT = System.out;
        System.setOut(new PrintStream(bos));
        try
        {
            commandline.main(new String[0]);
        }
        finally
        {
            System.setOut(OUT);
        }
        map.put("result", new String(bos.toByteArray()));
        LOG.info("Finished Linpack Test");
        return map;
    }


    @Required
    public void setJdbcTemplate(HybrisJdbcTemplate jdbcTemplate)
    {
        this.jdbcTemplate = jdbcTemplate;
    }
}
