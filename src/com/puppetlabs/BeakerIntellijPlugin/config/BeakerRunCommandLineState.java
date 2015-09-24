package com.puppetlabs.BeakerIntellijPlugin.config;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.configurations.ParamsGroup;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.projectRoots.Sdk;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.plugins.ruby.gem.GemUtil;
import org.jetbrains.plugins.ruby.ruby.run.MergingCommandLineArgumentsProvider;
import org.jetbrains.plugins.ruby.ruby.run.configuration.RubyAbstractCommandLineState;
import org.jetbrains.plugins.ruby.ruby.run.configuration.RubyCommandLineData;
import org.jetbrains.plugins.ruby.ruby.sdk.RubySdkAdditionalData;
import org.jetbrains.plugins.ruby.ruby.sdk.RubySdkUtil;

public class BeakerRunCommandLineState extends RubyAbstractCommandLineState {
    @NonNls
    private static final String e = "ruby.gem.command.runner";

    public BeakerRunCommandLineState(BeakerRunConfiguration runConfig, @NotNull ExecutionEnvironment executionEnvironment) {
        super(runConfig, executionEnvironment, false);
    }

    public BeakerRunConfiguration getConfig() {
        return (BeakerRunConfiguration)super.getConfig();
    }

    protected RubyCommandLineData createRunCommandLine() throws ExecutionException {
        BeakerRunConfiguration beakerRunConfiguration = this.getConfig();
        return createCommandLine(beakerRunConfiguration, this.getRunnerId());
    }

    public static RubyCommandLineData createCommandLine(@NotNull BeakerRunConfiguration beakerRunConfiguration, String arguments) throws ExecutionException {
        RubyCommandLineData rubyCommandLineData = createDefaultCommandLine(beakerRunConfiguration, arguments);
        Sdk beakerRunConfigurationSdk = beakerRunConfiguration.getSdk();

        assert beakerRunConfigurationSdk != null;

        String var4 = beakerRunConfiguration.getGemName();
        String var5 = beakerRunConfiguration.getExecutableName();
        String var6 = GemUtil.getGemExecutableRubyScriptPath(beakerRunConfiguration.getModule(), beakerRunConfigurationSdk, var4, var5);
        if(var6 != null) {
            GeneralCommandLine var7 = rubyCommandLineData.getUserData(RubyCommandLineData.COMMAND_LINE_KEY);

            assert var7 != null;

            RubySdkAdditionalData var8 = RubySdkUtil.getRubySdkAdditionalData(beakerRunConfigurationSdk);
            ParamsGroup var9 = addExecutionScriptGroup("ruby.gem.command.runner", rubyCommandLineData, var7, var8.getRunner(beakerRunConfiguration.getModule()).addDefaultMappings(beakerRunConfiguration.getMappingSettings()), var8.getSdkSystemAccessor(), var6);
            String var10 = beakerRunConfiguration.getExecutableArguments();
            if(!var10.isEmpty()) {
                var9.addParameters(MergingCommandLineArgumentsProvider.stringToArguments(var10));
            }
        }

        return rubyCommandLineData;
    }
}
