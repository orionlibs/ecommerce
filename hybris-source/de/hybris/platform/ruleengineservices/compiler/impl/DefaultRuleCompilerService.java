package de.hybris.platform.ruleengineservices.compiler.impl;

import com.google.common.collect.Lists;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerContext;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerContextFactory;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerListener;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerListenersFactory;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerProblem;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerResult;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerResultFactory;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerService;
import de.hybris.platform.ruleengineservices.compiler.RuleIr;
import de.hybris.platform.ruleengineservices.compiler.RuleIrProcessor;
import de.hybris.platform.ruleengineservices.compiler.RuleIrProcessorFactory;
import de.hybris.platform.ruleengineservices.compiler.RuleIrVariablesGenerator;
import de.hybris.platform.ruleengineservices.compiler.RuleIrVariablesGeneratorFactory;
import de.hybris.platform.ruleengineservices.compiler.RuleSourceCodeTranslator;
import de.hybris.platform.ruleengineservices.compiler.RuleSourceCodeTranslatorFactory;
import de.hybris.platform.ruleengineservices.compiler.RuleTargetCodeGenerator;
import de.hybris.platform.ruleengineservices.compiler.RuleTargetCodeGeneratorFactory;
import de.hybris.platform.ruleengineservices.maintenance.RuleCompilationContext;
import de.hybris.platform.ruleengineservices.maintenance.RuleCompilationContextProvider;
import de.hybris.platform.ruleengineservices.model.AbstractRuleModel;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultRuleCompilerService implements RuleCompilerService
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultRuleCompilerService.class);
    private RuleCompilerListenersFactory ruleCompilerListenersFactory;
    private RuleIrVariablesGeneratorFactory ruleIrVariablesGeneratorFactory;
    private RuleCompilerContextFactory<DefaultRuleCompilerContext> ruleCompilerContextFactory;
    private RuleSourceCodeTranslatorFactory ruleSourceCodeTranslatorFactory;
    private RuleIrProcessorFactory ruleIrProcessorFactory;
    private RuleTargetCodeGeneratorFactory ruleTargetCodeGeneratorFactory;
    private RuleCompilerResultFactory ruleCompilerResultFactory;
    private ModelService modelService;
    private RuleCompilationContextProvider ruleCompilationContextProvider;


    public RuleCompilerResult compile(AbstractRuleModel rule, String moduleName)
    {
        return compile(getRuleCompilationContextProvider().getRuleCompilationContext(), rule, moduleName);
    }


    public RuleCompilerResult compile(RuleCompilationContext ruleCompilationContext, AbstractRuleModel rule, String moduleName)
    {
        List<RuleCompilerListener> listeners = getRuleCompilerListenersFactory().getListeners(RuleCompilerListener.class);
        RuleIrVariablesGenerator variablesGenerator = getRuleIrVariablesGeneratorFactory().createVariablesGenerator();
        DefaultRuleCompilerContext context = (DefaultRuleCompilerContext)getRuleCompilerContextFactory().createContext(ruleCompilationContext, rule, moduleName, variablesGenerator);
        try
        {
            executeBeforeCompileListeners(context, listeners);
            RuleSourceCodeTranslator sourceCodeTranslator = getRuleSourceCodeTranslatorFactory().getSourceCodeTranslator((RuleCompilerContext)context);
            RuleIr ruleIr = sourceCodeTranslator.translate((RuleCompilerContext)context);
            RuleCompilerResult result = getRuleCompilerResultFactory().create(rule, context.getProblems());
            if(result.getResult() == RuleCompilerResult.Result.ERROR)
            {
                executeAfterCompileErrorListeners(context, listeners);
                return result;
            }
            List<RuleIrProcessor> irProcessors = getRuleIrProcessorFactory().getRuleIrProcessors();
            for(RuleIrProcessor irProcessor : irProcessors)
            {
                irProcessor.process((RuleCompilerContext)context, ruleIr);
            }
            RuleTargetCodeGenerator targetCodeGenerator = getRuleTargetCodeGeneratorFactory().getTargetCodeGenerator((RuleCompilerContext)context);
            targetCodeGenerator.generate((RuleCompilerContext)context, ruleIr);
            executeAfterCompileListeners(context, listeners);
            return getRuleCompilerResultFactory().create(result, context.getRuleVersion());
        }
        catch(Exception e)
        {
            LOG.error("Exception caught", e);
            executeAfterCompileErrorListeners(context, listeners);
            String errorMessage = String.format("Exception caught - %s: %s", new Object[] {e.getClass().getName(), e.getMessage()});
            List<RuleCompilerProblem> ruleCompilerProblems = Lists.newArrayList((Object[])new RuleCompilerProblem[] {(RuleCompilerProblem)new DefaultRuleCompilerProblem(RuleCompilerProblem.Severity.ERROR, errorMessage)});
            return getRuleCompilerResultFactory().create(rule, ruleCompilerProblems);
        }
    }


    protected void executeBeforeCompileListeners(DefaultRuleCompilerContext context, List<RuleCompilerListener> listeners)
    {
        for(RuleCompilerListener listener : listeners)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Running beforeCompile listener for: {}", listener.getClass().getCanonicalName());
            }
            listener.beforeCompile((RuleCompilerContext)context);
        }
    }


    protected void executeAfterCompileListeners(DefaultRuleCompilerContext context, List<RuleCompilerListener> listeners)
    {
        for(RuleCompilerListener listener : Lists.reverse(listeners))
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Running afterCompile listener for: {}", listener.getClass().getCanonicalName());
            }
            listener.afterCompile((RuleCompilerContext)context);
        }
    }


    protected void executeAfterCompileErrorListeners(DefaultRuleCompilerContext context, List<RuleCompilerListener> listeners)
    {
        for(RuleCompilerListener listener : Lists.reverse(listeners))
        {
            try
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("Running afterCompileError listener for: {}", listener.getClass().getCanonicalName());
                }
                listener.afterCompileError((RuleCompilerContext)context);
            }
            catch(Exception exception)
            {
                context.addFailureException(exception);
            }
        }
    }


    protected RuleCompilerListenersFactory getRuleCompilerListenersFactory()
    {
        return this.ruleCompilerListenersFactory;
    }


    @Required
    public void setRuleCompilerListenersFactory(RuleCompilerListenersFactory ruleCompilerListenersFactory)
    {
        this.ruleCompilerListenersFactory = ruleCompilerListenersFactory;
    }


    protected RuleSourceCodeTranslatorFactory getRuleSourceCodeTranslatorFactory()
    {
        return this.ruleSourceCodeTranslatorFactory;
    }


    protected RuleIrVariablesGeneratorFactory getRuleIrVariablesGeneratorFactory()
    {
        return this.ruleIrVariablesGeneratorFactory;
    }


    @Required
    public void setRuleIrVariablesGeneratorFactory(RuleIrVariablesGeneratorFactory ruleIrVariablesGeneratorFactory)
    {
        this.ruleIrVariablesGeneratorFactory = ruleIrVariablesGeneratorFactory;
    }


    protected RuleCompilerContextFactory<DefaultRuleCompilerContext> getRuleCompilerContextFactory()
    {
        return this.ruleCompilerContextFactory;
    }


    @Required
    public void setRuleCompilerContextFactory(RuleCompilerContextFactory<DefaultRuleCompilerContext> ruleCompilerContextFactory)
    {
        this.ruleCompilerContextFactory = ruleCompilerContextFactory;
    }


    @Required
    public void setRuleSourceCodeTranslatorFactory(RuleSourceCodeTranslatorFactory ruleSourceCodeTranslatorFactory)
    {
        this.ruleSourceCodeTranslatorFactory = ruleSourceCodeTranslatorFactory;
    }


    protected RuleIrProcessorFactory getRuleIrProcessorFactory()
    {
        return this.ruleIrProcessorFactory;
    }


    @Required
    public void setRuleIrProcessorFactory(RuleIrProcessorFactory ruleIrProcessorFactory)
    {
        this.ruleIrProcessorFactory = ruleIrProcessorFactory;
    }


    protected RuleTargetCodeGeneratorFactory getRuleTargetCodeGeneratorFactory()
    {
        return this.ruleTargetCodeGeneratorFactory;
    }


    @Required
    public void setRuleTargetCodeGeneratorFactory(RuleTargetCodeGeneratorFactory ruleTargetCodeGeneratorFactory)
    {
        this.ruleTargetCodeGeneratorFactory = ruleTargetCodeGeneratorFactory;
    }


    protected RuleCompilerResultFactory getRuleCompilerResultFactory()
    {
        return this.ruleCompilerResultFactory;
    }


    @Required
    public void setRuleCompilerResultFactory(RuleCompilerResultFactory ruleCompilerResultFactory)
    {
        this.ruleCompilerResultFactory = ruleCompilerResultFactory;
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    protected RuleCompilationContextProvider getRuleCompilationContextProvider()
    {
        return this.ruleCompilationContextProvider;
    }


    @Required
    public void setRuleCompilationContextProvider(RuleCompilationContextProvider ruleCompilationContextProvider)
    {
        this.ruleCompilationContextProvider = ruleCompilationContextProvider;
    }
}
