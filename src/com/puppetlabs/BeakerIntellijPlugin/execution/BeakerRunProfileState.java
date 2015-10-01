package com.puppetlabs.BeakerIntellijPlugin.execution;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.CommandLineState;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.process.ColoredProcessHandler;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.process.ProcessTerminatedListener;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.puppetlabs.BeakerIntellijPlugin.config.BeakerRunConfiguration;
import com.puppetlabs.BeakerIntellijPlugin.settings.BeakerRunSettings;

import java.io.File;
import java.io.FileFilter;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by samwoods on 9/9/15.
 */
public class BeakerRunProfileState extends CommandLineState {
    public BeakerRunConfiguration config;

    public BeakerRunProfileState(ExecutionEnvironment environment, BeakerRunConfiguration config) {
        super(environment);
        this.config = config;
    }

    protected ProcessHandler startProcess() throws ExecutionException {
        BeakerRunSettings settings = config.getRunSettings();

        GeneralCommandLine cmdLine = getCommandLine(settings);

        Process p = cmdLine.createProcess();
        System.out.println("Beaker Intellij Plugin - Executing command: '" + cmdLine.getCommandLineString() + "'");
        ColoredProcessHandler handler = new ColoredProcessHandler(p, cmdLine.getCommandLineString());
        ProcessTerminatedListener.attach(handler);

        return handler;
    }

    public GeneralCommandLine getCommandLine(BeakerRunSettings settings) {
        String exe = "beaker";
        if (settings.getUseBundler())
            exe = "bundle";
        GeneralCommandLine commandLine = getArgs(settings);
        commandLine.setExePath(exe);

        return commandLine;
    }

    public static GeneralCommandLine getArgs(BeakerRunSettings settings){
        GeneralCommandLine commandLine = new GeneralCommandLine();

        if (settings.getUseBundler()){
            commandLine.addParameter("exec");
            commandLine.addParameter("beaker");
        }

        if (settings.getConfigFile() != null && !settings.getConfigFile().isEmpty()) {
            String configFile = settings.getConfigFile();
            if (settings.getUseLatestPreserved()){
                configFile = getLatestHostsPreserved(configFile);
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
        return commandLine;
    }

    //Get the hosts_preserved.yml file from the latest log directory created by beaker.
    public static String getLatestHostsPreserved(String hostsConfigFile) {
        Path hostsConfigFilePath = Paths.get(hostsConfigFile);
        Path hostLogDirectoryPath = Paths.get(hostsConfigFilePath.getParent().resolve("log").toString(), hostsConfigFilePath.getFileName().toString());
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
