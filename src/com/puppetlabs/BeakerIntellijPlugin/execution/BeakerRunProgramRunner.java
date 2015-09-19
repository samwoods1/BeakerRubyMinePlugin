package com.puppetlabs.BeakerIntellijPlugin.execution;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.ExecutionResult;
import com.intellij.execution.RunProfileStarter;
import com.intellij.execution.configurations.RunProfile;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.executors.DefaultDebugExecutor;
import com.intellij.execution.executors.DefaultRunExecutor;
import com.intellij.execution.runners.AsyncGenericProgramRunner;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.xdebugger.XDebugProcess;
import com.intellij.xdebugger.XDebugProcessStarter;
import com.intellij.xdebugger.XDebugSession;
import com.intellij.xdebugger.XDebuggerManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.concurrency.Promise;
import com.puppetlabs.BeakerIntellijPlugin.config.BeakerRunConfiguration;
import org.jetbrains.debugger.connection.VmConnection;
import org.jetbrains.plugins.ruby.command.run.CommandRunConfigurationFactory;
import org.jetbrains.plugins.ruby.command.run.CommandRunConfigurationType;
import org.jetbrains.plugins.ruby.ruby.debugger.impl.RubyDebugProcess;

/**
 * Created by samwoods on 9/13/15.
 */
public class BeakerRunProgramRunner extends AsyncGenericProgramRunner {
    @NotNull
    @Override
    public String getRunnerId() {
        return "BeakerDriverClientRunner";
    }

    @Override
    public boolean canRun(@NotNull String executorId, @NotNull RunProfile profile) {
        return DefaultRunExecutor.EXECUTOR_ID.equals(executorId) && profile instanceof BeakerRunConfiguration;
    }

    @NotNull
    @Override
    protected Promise<RunProfileStarter> prepare(@NotNull ExecutionEnvironment environment, @NotNull RunProfileState state) throws ExecutionException {
        final ExecutionResult executionResult = state.execute(environment.getExecutor(), this);
        if (executionResult == null) {
            return Promise.resolve(null);
        }

        return Promise.<RunProfileStarter>resolve(new BeakerRunStarter(true));
    }

}
