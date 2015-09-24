package com.puppetlabs.BeakerIntellijPlugin.execution;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.RunProfile;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.executors.DefaultDebugExecutor;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.ui.RunContentDescriptor;
import com.intellij.openapi.project.Project;
import com.puppetlabs.BeakerIntellijPlugin.config.BeakerRunConfiguration;
import com.puppetlabs.BeakerIntellijPlugin.settings.BeakerRunSettings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.plugins.ruby.ruby.RubyUtil;
import org.jetbrains.plugins.ruby.ruby.debugger.RubyDebugRunner;
import org.jetbrains.plugins.ruby.ruby.run.configuration.RubyRunConfigurationType;
import org.jetbrains.plugins.ruby.ruby.run.configuration.RunConfigurationUtil;
import org.jetbrains.plugins.ruby.ruby.run.configuration.rubyScript.RubyRunConfiguration;

/**
 * Created by samwoods on 9/14/15.
 */
public class BeakerDebugProgramRunner extends RubyDebugRunner {
    @NotNull
    @Override
    public String getRunnerId() {
        return "BeakerDriverClientRunner";
    }

    @Override
    public boolean canRun(@NotNull String executorId, @NotNull RunProfile profile) {
        return DefaultDebugExecutor.EXECUTOR_ID.equals(executorId) && profile instanceof BeakerRunConfiguration;
    }

    @Override
    protected RunContentDescriptor doExecute(@NotNull Project project, @NotNull final RunProfileState state, RunContentDescriptor contentToReuse, @NotNull final ExecutionEnvironment env) throws ExecutionException {
        BeakerRunProfileState beakerState = (BeakerRunProfileState)state;

        RubyRunConfigurationType type = new RubyRunConfigurationType();
        RubyRunConfigurationType.RubyRunConfigurationFactory factory = (RubyRunConfigurationType.RubyRunConfigurationFactory)type.getConfigurationFactories()[0];

        BeakerRunSettings settings = beakerState.config.getRunSettings();
        String cmdLine = ((BeakerRunProfileState) state).getCommandLine(settings).getCommandLineString();

        String args = cmdLine.substring("Beaker ".length());

        RubyRunConfiguration rubyConfig = new RubyRunConfiguration(project, factory);
        String rubyArgs = RunConfigurationUtil.collectArguments(RubyUtil.SYNC_IO_ARGUMENTS);
        rubyConfig.setRubyArgs(rubyArgs);
        rubyConfig.setScriptPath("beaker");
        rubyConfig.setScriptArgs(args);
        rubyConfig.setWorkingDirectory(settings.getDirectory());

        return super.doExecute(project, rubyConfig.getState(env.getExecutor(), env),contentToReuse, env);
    }
}
