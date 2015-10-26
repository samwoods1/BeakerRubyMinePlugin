package com.puppetlabs.BeakerIntellijPlugin.execution;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.configurations.ParamsGroup;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.projectRoots.Sdk;
import com.puppetlabs.BeakerIntellijPlugin.config.BeakerRunConfiguration;
import com.puppetlabs.BeakerIntellijPlugin.settings.BeakerRunSettings;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.plugins.ruby.gem.GemUtil;
import org.jetbrains.plugins.ruby.ruby.run.MergingCommandLineArgumentsProvider;
import org.jetbrains.plugins.ruby.ruby.run.configuration.RubyAbstractCommandLineState;
import org.jetbrains.plugins.ruby.ruby.run.configuration.RubyCommandLineData;
import org.jetbrains.plugins.ruby.ruby.sdk.RubySdkAdditionalData;
import org.jetbrains.plugins.ruby.ruby.sdk.RubySdkUtil;

import java.io.File;
import java.io.FileFilter;
import java.nio.file.Path;
import java.nio.file.Paths;

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
        BeakerRunSettings settings = beakerRunConfiguration.getRunSettings();
        if(var6 != null) {
            GeneralCommandLine var7 = rubyCommandLineData.getUserData(RubyCommandLineData.COMMAND_LINE_KEY);

            assert var7 != null;

            String workingDir = getWorkingDir(settings, beakerRunConfiguration.getProject().getBasePath());
            var7.withWorkDirectory(workingDir);

            RubySdkAdditionalData var8 = RubySdkUtil.getRubySdkAdditionalData(beakerRunConfigurationSdk);
            ParamsGroup var9 = addExecutionScriptGroup("ruby.gem.command.runner", rubyCommandLineData, var7, var8.getRunner(beakerRunConfiguration.getModule()).addDefaultMappings(beakerRunConfiguration.getMappingSettings()), var8.getSdkSystemAccessor(), var6);
            String var10 = getArgs(settings, workingDir);
            if(!var10.isEmpty()) {
                var9.addParameters(MergingCommandLineArgumentsProvider.stringToArguments(var10));
            }
        }

        return rubyCommandLineData;
    }

    public static String getWorkingDir(BeakerRunSettings settings, String basePath){
        String workingDir = settings.getDirectory();
        if (workingDir != null && !workingDir.isEmpty()) {
            if (!workingDir.startsWith("/") && !workingDir.startsWith("~/")) {
                workingDir = Paths.get(basePath, settings.getDirectory()).toAbsolutePath().toString();
            }
        }
        else{
            workingDir = basePath;
        }

        return workingDir;
    }

    public static String getArgs(BeakerRunSettings settings, String workingDir){
        GeneralCommandLine commandLine = new GeneralCommandLine();

        if (settings.getConfigFile() != null && !settings.getConfigFile().isEmpty()) {
            String configFile = settings.getConfigFile();
            if (settings.getUseLatestPreserved()){
                configFile = getLatestHostsPreserved(configFile, workingDir);
            }
            commandLine.addParameter("--hosts");
            commandLine.addParameter(configFile);
        }

        if (settings.getOptionsFile() != null && !settings.getOptionsFile().isEmpty()){
            commandLine.addParameter("--options-file");
            commandLine.addParameter(settings.getOptionsFile());
        }

        if (settings.getRsaKey() != null && !settings.getRsaKey().isEmpty()){
            commandLine.addParameter("--keyfile");
            commandLine.addParameter(settings.getRsaKey());
        }

        if (settings.getTestFilePath() != null && !settings.getTestFilePath().isEmpty()){
            commandLine.addParameter("--tests");
            commandLine.addParameter(settings.getTestFilePath());
        }

        if (settings.getAdditionalArguments() != null && !settings.getAdditionalArguments().isEmpty()){
            for (String param : settings.getAdditionalArguments().split("\\s+")){
                commandLine.addParameter(param);
            }
        }
        return commandLine.getParametersList().getParametersString();
    }

    //Get the hosts_preserved.yml file from the latest log directory created by beaker.
    public static String getLatestHostsPreserved(String hostsConfigFile, String workingDir) {
        Path hostsConfigFilePath = Paths.get(hostsConfigFile);
        Path hostLogDirectoryPath = Paths.get(workingDir, "log", hostsConfigFilePath.getFileName().toString());
        File fl = new File(hostLogDirectoryPath.toString());
        File[] files = fl.listFiles(new FileFilter() {
            public boolean accept(File file) {
                return file.isDirectory();
            }
        });
        long lastMod = Long.MIN_VALUE;
        File choice = null;
        for (int i = 0; i < files.length; i++) {
            File dir = files[i];
            if (dir.lastModified() > lastMod) {
                choice = dir;
                lastMod = dir.lastModified();
            }
        }

        //No hosts_preserved.yml files exist yet, use the actual config.
        if(choice == null){
            return hostsConfigFile;
        }
        Path latestPreservedPath =  Paths.get(choice.getAbsolutePath(), "hosts_preserved.yml");
        return latestPreservedPath.toString();
    }

}
