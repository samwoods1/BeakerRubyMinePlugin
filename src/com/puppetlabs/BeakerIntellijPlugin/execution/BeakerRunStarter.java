package com.puppetlabs.BeakerIntellijPlugin.execution;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.ExecutionResult;
import com.intellij.execution.Executor;
import com.intellij.execution.RunProfileStarter;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.executors.DefaultDebugExecutor;
import com.intellij.execution.executors.DefaultRunExecutor;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.runners.RunContentBuilder;
import com.intellij.execution.ui.RunContentDescriptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BeakerRunStarter extends RunProfileStarter {
    private final boolean myFromDebug;

    public BeakerRunStarter(boolean fromDebug) {
        myFromDebug = fromDebug;
    }

    @Nullable
    @Override
    public RunContentDescriptor execute(@NotNull RunProfileState state, @NotNull ExecutionEnvironment environment) throws ExecutionException {
        BeakerRunProfileState BeakerState = (BeakerRunProfileState)state;
        Executor executor;
        ExecutionResult executionResult;
        if (this.myFromDebug){
            executor = new DefaultDebugExecutor();
            executionResult = BeakerState.execute(executor, new BeakerDebugProgramRunner());
        }
        else {
            executor = new DefaultRunExecutor();
            executionResult = BeakerState.execute(executor, new BeakerRunProgramRunner());
        }

        RunContentBuilder contentBuilder = new RunContentBuilder(executionResult, environment);
        final RunContentDescriptor descriptor = contentBuilder.showRunContent(environment.getContentToReuse());
        return descriptor;
    }
}
