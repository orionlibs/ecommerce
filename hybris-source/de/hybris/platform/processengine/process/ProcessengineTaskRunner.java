package de.hybris.platform.processengine.process;

import com.google.common.base.Suppliers;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.core.threadregistry.OperationInfo;
import de.hybris.platform.core.threadregistry.RevertibleUpdate;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.persistence.property.JDBCValueMappings;
import de.hybris.platform.processengine.definition.Node;
import de.hybris.platform.processengine.definition.NodeExecutionContext;
import de.hybris.platform.processengine.definition.NodeExecutionException;
import de.hybris.platform.processengine.definition.ProcessDefinition;
import de.hybris.platform.processengine.definition.ProcessDefinitionFactory;
import de.hybris.platform.processengine.definition.ProcessDefinitionId;
import de.hybris.platform.processengine.definition.SupportsTimeout;
import de.hybris.platform.processengine.enums.ProcessState;
import de.hybris.platform.processengine.helpers.ProcessParameterHelper;
import de.hybris.platform.processengine.jalo.ProcessTask;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.processengine.model.ProcessTaskModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.task.RetryLaterException;
import de.hybris.platform.task.TaskConditionModel;
import de.hybris.platform.task.TaskRunner;
import de.hybris.platform.task.TaskService;
import de.hybris.platform.task.logging.ProcessEngineLoggingCtx;
import de.hybris.platform.task.logging.TaskLoggingCtx;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.MessageFormatUtils;
import de.hybris.platform.util.Utilities;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.support.TransactionTemplate;

public class ProcessengineTaskRunner implements TaskRunner<ProcessTaskModel>, InitializingBean, ApplicationContextAware
{
    public static final String PASSTHROUGH_RUNNER = "passthroughRunner";
    public static final String MARK_AS_DONE_ENABLED = "mark.process.as.done.enabled";
    private static final Logger LOG = LoggerFactory.getLogger(ProcessengineTaskRunner.class);
    private static final String UPDATE_TASK_STATEMENT = "UPDATE {0} SET {1}=0, {2}=? WHERE pk=?";
    protected final Supplier<String> filledUpdateTaskStatement = (Supplier<String>)Suppliers.memoize(this::fillUpdateStatement);
    private TransactionTemplate transactionTemplate;
    private ProcessDefinitionFactory processDefinitionFactory;
    private ProcessParameterHelper processParameterHelper;
    private ModelService modelService;
    private UserService userService;
    private SessionService sessionService;
    private JdbcTemplate jdbcTemplate;
    private ApplicationContext context;


    public void run(TaskService taskService, ProcessTaskModel task)
    {
        beforeRun(taskService, task);
        String currentTaskActionId = "";
        String afterRunTaskActionId = "";
        do
        {
            refreshTask(task);
            currentTaskActionId = task.getAction();
            log("1", task, currentTaskActionId, task.getAction());
            withLogging(task, () -> doRun(taskService, task)).invoke();
            log("2", task, currentTaskActionId, task.getAction());
            refreshTask(task);
            afterRunTaskActionId = task.getAction();
            log("3", task, currentTaskActionId, task.getAction());
        }
        while(!currentTaskActionId.equals(afterRunTaskActionId));
    }


    private void log(String denominator, ProcessTaskModel task, String currentTaskActionId, String action)
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("{}. task: {}, expected current task action ID: {}, task action id: {}, is up to date: {}", new Object[] {denominator, task, currentTaskActionId, action,
                            Boolean.valueOf(task.getItemModelContext().isUpToDate())});
        }
    }


    private void refreshTask(ProcessTaskModel task)
    {
        if(!this.modelService.isNew(task))
        {
            this.modelService.refresh(task);
        }
    }


    protected String doRun(TaskService taskService, ProcessTaskModel task)
    {
        return withRunErrorHandling(taskService, task, () -> runProcessTaskInTransaction(task)).invoke();
    }


    protected String runProcessTaskInTransaction(ProcessTaskModel task) throws NodeExecutionException
    {
        return withinTransaction(() -> runProcessTask(task)).invoke();
    }


    private RetryLaterException handleRetryLaterExceptionInCallback(RetryLaterException e)
    {
        if(e.isRollBack())
        {
            throw new RuntimeExceptionWrapper(e);
        }
        return e;
    }


    private String getStringOrThrowRetryLaterException(Object callbackResult)
    {
        if(callbackResult instanceof RetryLaterException)
        {
            throw (RetryLaterException)callbackResult;
        }
        return (String)callbackResult;
    }


    private void switchUserIfNeeded(BusinessProcessModel bpm)
    {
        if(bpm != null)
        {
            UserModel defaultUser = this.userService.getCurrentUser();
            UserModel processUser = bpm.getUser();
            if(processUser != null && !processUser.equals(defaultUser))
            {
                this.userService.setCurrentUser(processUser);
            }
        }
    }


    protected String runProcessTask(ProcessTaskModel task) throws NodeExecutionException
    {
        return withinNewSession(task, () -> {
            Node node = getNodeForExecution(task);
            String result = null;
            String actionBefore = task.getAction();
            if(node.isExecutionContextRequired())
            {
                result = node.executeWithContext(createContextForTask(task));
            }
            else
            {
                result = node.execute(task.getProcess());
            }
            markAsDoneAndRemoveConditionsIfPossible(task, actionBefore);
            return result;
        }).invoke();
    }


    private void markAsDoneAndRemoveConditionsIfPossible(ProcessTaskModel task, String actionBefore)
    {
        if(canMarkAsDone(task, actionBefore))
        {
            removeConditions(task);
            markAsDone(task);
        }
    }


    private boolean canMarkAsDone(ProcessTaskModel task, String actionBefore)
    {
        return (isEnabledMarkAsDone() && actionBefore.equals(task.getAction()) && !this.modelService.isNew(task));
    }


    private boolean isEnabledMarkAsDone()
    {
        return Config.getBoolean("mark.process.as.done.enabled", false);
    }


    private void removeConditions(ProcessTaskModel task)
    {
        this.modelService.removeAll(task.getConditions());
    }


    private void markAsDone(ProcessTaskModel task)
    {
        JDBCValueMappings.ValueWriter<PK, ?> pkWriter = (JDBCValueMappings.getInstance()).PK_WRITER;
        JDBCValueMappings.ValueWriter<String, ?> stringWriter = (JDBCValueMappings.getInstance()).STRING_WRITER;
        int updatedRows = this.jdbcTemplate.update(this.filledUpdateTaskStatement.get(), preparedStatement -> {
            stringWriter.setValue(preparedStatement, 1, "passthroughRunner");
            pkWriter.setValue(preparedStatement, 2, task.getPk());
        });
        if(updatedRows == 0)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug(MessageFormatUtils.format("Could not mark ProcessTaskModel {0} as done", new Object[] {task.getPk()}));
            }
        }
        else
        {
            Utilities.invalidateCache(task.getPk());
        }
    }


    private String fillUpdateStatement()
    {
        return MessageFormatUtils.format("UPDATE {0} SET {1}=0, {2}=? WHERE pk=?", new Object[] {getTableName(ProcessTask.class),
                        getColumnName(ProcessTask.class, "process"),
                        getColumnName(ProcessTask.class, "runnerBean")});
    }


    protected String getTableName(Class<?> clazz)
    {
        ComposedType type = TypeManager.getInstance().getComposedType(clazz);
        return type.getTable();
    }


    protected String getColumnName(Class<?> clazz, String attribute)
    {
        ComposedType type = TypeManager.getInstance().getComposedType(clazz);
        return type.getAttributeDescriptor(attribute).getDatabaseColumn();
    }


    private NodeExecutionContext createContextForTask(ProcessTaskModel task)
    {
        return (new NodeExecutionContext(task.getProcess())).withChoices(collectChoicesFor(task)).withProcessTaskModel(task);
    }


    private Set<String> collectChoicesFor(ProcessTaskModel task)
    {
        return (Set<String>)((Set)Optional.<Set>ofNullable(task.getConditions())
                        .orElse(Collections.emptySet()))
                        .stream()
                        .map(TaskConditionModel::getChoice)
                        .filter(StringUtils::isNotEmpty)
                        .collect(Collectors.toSet());
    }


    protected Node getNodeForExecution(ProcessTaskModel task)
    {
        BusinessProcessModel processModel = task.getProcess();
        ProcessDefinition processDef = getProcessDefinition(processModel);
        Node node = processDef.retrieve(task.getAction());
        if(LOG.isDebugEnabled())
        {
            LOG.debug(String.format("Executing node with ID %s for process with code %s", new Object[] {node.getId(), processModel.getCode()}));
        }
        return node;
    }


    protected String processRunError(TaskService taskService, ProcessTaskModel task, Throwable e)
    {
        BusinessProcessModel process = task.getProcess();
        process.setState(ProcessState.ERROR);
        this.modelService.save(process);
        ProcessDefinition processDefinition = getProcessDefinition(process);
        Node errorNode = processDefinition.getOnErrorNode();
        if(errorNode != null)
        {
            errorNode.trigger(process);
        }
        handleError(taskService, task, e);
        return null;
    }


    protected void beforeRun(TaskService taskService, ProcessTaskModel task)
    {
        if(taskService == null)
        {
            throw new IllegalArgumentException("No Task Service given");
        }
        if(task == null)
        {
            throw new IllegalArgumentException("No task given");
        }
        if(LOG.isDebugEnabled())
        {
            LOG.debug(String.format("Running task with action %s for process with code %s", new Object[] {task.getAction(), task
                            .getProcess().getCode()}));
        }
    }


    public void handleError(TaskService taskManager, ProcessTaskModel task, Throwable error)
    {
        if(isErrorCausedByTimeout(error))
        {
            LOG.info("Timeout occured");
            if(handleTimeout(taskManager, task))
            {
                LOG.info("Timeout succesfully handled.");
                return;
            }
        }
        if(error instanceof de.hybris.platform.tx.TransactionException && "transaction rolled back because it has been marked as rollback-only"
                        .equals(error.getMessage()))
        {
            return;
        }
        LOG.error(error.getMessage(), error);
        BusinessProcessModel process = task.getProcess();
        if(process != null)
        {
            this.processParameterHelper.setProcessParameter(process, "errorStackTrace", stackTrace(error));
        }
        else
        {
            LOG.warn("Task has no process. Will not save strack trace.");
        }
    }


    private boolean isErrorCausedByTimeout(Throwable error)
    {
        return error instanceof de.hybris.platform.task.TaskTimeoutException;
    }


    private boolean handleTimeout(TaskService taskService, ProcessTaskModel task)
    {
        Node node = getNodeForExecution(task);
        if(!(node instanceof SupportsTimeout))
        {
            return false;
        }
        SupportsTimeout nodeWithTimeoutSupport = (SupportsTimeout)node;
        withLogging(task,
                        withRunErrorHandling(taskService, task,
                                        withinTransaction(
                                                        withinNewSession(task, () -> {
                                                            NodeExecutionContext ctx = createContextForTask(task);
                                                            return nodeWithTimeoutSupport.handleTimeout(ctx);
                                                        })))).invoke();
        return true;
    }


    protected ProcessDefinition getProcessDefinition(BusinessProcessModel process)
    {
        return this.processDefinitionFactory.getProcessDefinition(ProcessDefinitionId.of(process));
    }


    private String stackTrace(Throwable error)
    {
        StringWriter ret = new StringWriter();
        error.printStackTrace(new PrintWriter(ret));
        return ret.toString();
    }


    @Required
    public void setTransactionTemplate(TransactionTemplate transactionTemplate)
    {
        this.transactionTemplate = transactionTemplate;
    }


    @Required
    public void setProcessDefinitionFactory(ProcessDefinitionFactory processDefinitionFactory)
    {
        this.processDefinitionFactory = processDefinitionFactory;
    }


    @Required
    public void setProcessParameterHelper(ProcessParameterHelper processParameterHelper)
    {
        this.processParameterHelper = processParameterHelper;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    @Required
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }


    @Required
    public void setSessionService(SessionService sessionService)
    {
        this.sessionService = sessionService;
    }


    public void setJdbcTemplate(JdbcTemplate jdbcTemplate)
    {
        this.jdbcTemplate = jdbcTemplate;
    }


    public void afterPropertiesSet()
    {
        if(this.jdbcTemplate == null)
        {
            LOG.warn("Found missing jdbcTemplate dependency for bean " + this + ", please adjust spring configuration");
            this.jdbcTemplate = (JdbcTemplate)this.context.getBean("jdbcTemplate", JdbcTemplate.class);
        }
    }


    public void setApplicationContext(ApplicationContext context)
    {
        this.context = context;
    }


    private NodeInvoker withLogging(ProcessTaskModel task, NodeInvoker target)
    {
        return () -> {
            Date startDate = new Date();
            String startAction = task.getAction();
            ProcessEngineLoggingCtx loggingCtx = null;
            String result = null;
            try
            {
                loggingCtx = initLoggingCtx(task);
                result = target.invoke();
                return result;
            }
            finally
            {
                if(loggingCtx != null)
                {
                    loggingCtx.registerExecutionInfo(startDate, result, startAction);
                    stopLoggingCtx((TaskLoggingCtx)loggingCtx);
                }
            }
        };
    }


    public ProcessEngineLoggingCtx initLoggingCtx(ProcessTaskModel task)
    {
        return new ProcessEngineLoggingCtx(task, this.modelService);
    }


    public void stopLoggingCtx(TaskLoggingCtx taskCtx)
    {
        if(taskCtx != null)
        {
            taskCtx.finishAndClose();
        }
    }


    private NodeInvoker withRunErrorHandling(TaskService taskService, ProcessTaskModel task, RawNodeInvoker target)
    {
        return () -> invokeTargetWithErrorHandling(taskService, task, target);
    }


    private String invokeTargetWithErrorHandling(TaskService taskService, ProcessTaskModel task, RawNodeInvoker target) throws Error
    {
        try
        {
            RevertibleUpdate revertibleInfo = OperationInfo.updateThread(
                            OperationInfo.builder().withBusinessProcessCode(task.getProcess().getCode()).build());
            try
            {
                String str = target.invoke();
                if(revertibleInfo != null)
                {
                    revertibleInfo.close();
                }
                return str;
            }
            catch(Throwable throwable)
            {
                if(revertibleInfo != null)
                {
                    try
                    {
                        revertibleInfo.close();
                    }
                    catch(Throwable throwable1)
                    {
                        throwable.addSuppressed(throwable1);
                    }
                }
                throw throwable;
            }
        }
        catch(RetryLaterException e)
        {
            throw e;
        }
        catch(NodeExecutionException e)
        {
            return processRunError(taskService, task, (Throwable)e);
        }
        catch(Error e)
        {
            processRunError(taskService, task, e);
            throw e;
        }
        catch(Throwable e)
        {
            return processRunError(taskService, task, e);
        }
    }


    private RawNodeInvoker withinTransaction(RawNodeInvoker target)
    {
        return () -> {
            try
            {
                return getStringOrThrowRetryLaterException(this.transactionTemplate.execute(()));
            }
            catch(RuntimeExceptionWrapper wrappedEx)
            {
                wrappedEx.throwLegalException();
                return null;
            }
        };
    }


    private Serializable invokeTarget(RawNodeInvoker target)
    {
        try
        {
            return target.invoke();
        }
        catch(RetryLaterException e)
        {
            return (Serializable)handleRetryLaterExceptionInCallback(e);
        }
        catch(NodeExecutionException e)
        {
            throw new RuntimeExceptionWrapper(e);
        }
    }


    private RawNodeInvoker withinNewSession(ProcessTaskModel task, RawNodeInvoker target)
    {
        return () -> {
            this.sessionService.createNewSession();
            try
            {
                switchUserIfNeeded(task.getProcess());
                return target.invoke();
            }
            finally
            {
                this.sessionService.closeCurrentSession();
            }
        };
    }
}
