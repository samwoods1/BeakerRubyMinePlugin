package com.puppetlabs.BeakerIntellijPlugin.config;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.configurations.RuntimeConfigurationException;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.JDOMExternalizer;
import com.intellij.openapi.util.WriteExternalException;
import com.puppetlabs.BeakerIntellijPlugin.execution.BeakerRunCommandLineState;
import com.puppetlabs.BeakerIntellijPlugin.settings.BeakerRunSettings;
import com.puppetlabs.BeakerIntellijPlugin.settings.BeakerSettingsEditor;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.plugins.ruby.ruby.run.configuration.AbstractRubyRunConfiguration;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by samwoods on 6/11/15.
 */
public class BeakerRunConfiguration extends AbstractRubyRunConfiguration<BeakerRunConfiguration> {

    private BeakerRunSettings runSettings;

    protected BeakerRunConfiguration(Project project, ConfigurationFactory factory) {
        super(project, factory);
    }

    protected String getSerializationId() {
        return "BEAKER_RUN_CONFIGURATION";
    }

    protected RunProfileState createCommandLineState(@NotNull ExecutionEnvironment var1) throws ExecutionException {
        return new BeakerRunCommandLineState(this, var1);
    }

    public BeakerRunSettings getRunSettings() {
        if (runSettings == null){
            runSettings = new BeakerRunSettings();
        }
        return runSettings != null ? runSettings : new BeakerRunSettings();
    }

    public void setRunSettings(String rsaKey, String configFile, Boolean useLatestPreserved, String optionsFile, String testFile, String additionalArguments, String workingDirectory) {
        runSettings = new BeakerRunSettings();
        runSettings.setRsaKey(rsaKey);
        runSettings.setConfigFile(configFile);
        runSettings.setUseLatestPreserved(useLatestPreserved);
        runSettings.setOptionsFile(optionsFile);
        runSettings.setTestFilePath(testFile);
        runSettings.setAdditionalArguments(additionalArguments);
        runSettings.setDirectory(workingDirectory);
    }

    public void setRunSettings(BeakerRunSettings settings){
        runSettings = settings;
    }

    @NotNull
    protected SettingsEditor<BeakerRunConfiguration> createConfigurationEditor() {
        return new BeakerSettingsEditor(getProject());
    }

    @Override
    protected void validateConfiguration(boolean var1) throws RuntimeConfigurationException {

    }

    @Override
    public void readExternal(Element element) throws InvalidDataException {
        super.readExternal(element);
        BeakerRunSettings settings = new BeakerRunSettings();
        settings.setAdditionalArguments(JDOMExternalizer.readString(element, "additionalArguments"));
        settings.setTestFilePath(JDOMExternalizer.readString(element, "testFilePath"));
        settings.setDirectory(JDOMExternalizer.readString(element, "directory"));
        settings.setConfigFile(JDOMExternalizer.readString(element, "configFile"));
        settings.setOptionsFile(JDOMExternalizer.readString(element, "optionsFile"));
        settings.setRsaKey(JDOMExternalizer.readString(element, "rsaKey"));
        settings.setUseLatestPreserved(JDOMExternalizer.readBoolean(element, "useLatestPreserved"));
        setRunSettings(settings);
    }

    @Override
    public void writeExternal(Element element) throws WriteExternalException {
        super.writeExternal(element);
        if (runSettings == null){
            try {
                readExternal(element);
            }
            catch (InvalidDataException ex){
                //TODO: Not doing this at the moment anyways.
            }
        }
        JDOMExternalizer.write(element, "additionalArguments", runSettings.getAdditionalArguments());
        JDOMExternalizer.write(element, "testFilePath", runSettings.getTestFilePath());
        JDOMExternalizer.write(element, "directory", runSettings.getDirectory());
        JDOMExternalizer.write(element, "configFile", runSettings.getConfigFile());
        JDOMExternalizer.write(element, "optionsFile", runSettings.getOptionsFile());
        JDOMExternalizer.write(element, "rsaKey", runSettings.getRsaKey());
        JDOMExternalizer.write(element, "useLatestPreserved", runSettings.getUseLatestPreserved());
    }

    @Override
    @NotNull
    public String suggestedName() {
        Path path = Paths.get(runSettings.getTestFilePath());
        if (path.toString() == null || path.toString().equals("")){
            return "BEAKER";
        }
        return "BEAKER_" + path.getFileName();
    }

    public String getGemName() {
        return "beaker";
    }

    public String getExecutableName() {
        return "beaker";
    }

    public String getExecutableArguments() {
        return null;
    }

}
