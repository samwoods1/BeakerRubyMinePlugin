package com.puppetlabs.BeakerIntellijPlugin.config;

import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ConfigurationType;
import com.intellij.execution.configurations.ConfigurationTypeUtil;
import com.intellij.icons.AllIcons;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * Created by samwoods on 6/11/15.
 */
public class BeakerConfigurationType implements ConfigurationType {

    public BeakerConfigurationType() {
    }

    @Override
    public String getDisplayName() {
        return "Beaker";
    }

    @Override
    public String getConfigurationTypeDescription() {
        return "Beaker Test Run Configuration";
    }

    @Override
    public Icon getIcon() {
        return AllIcons.General.Information;
    }

    @NotNull
    @Override
    public String getId() {
        return "Beaker Test Runner";
    }

    @Override
    public ConfigurationFactory[] getConfigurationFactories() {
        return new ConfigurationFactory[]{this.a};
    }


    private final BeakerConfigurationFactory a = new BeakerConfigurationFactory(this);


    public static BeakerConfigurationType getInstance() {
        return ConfigurationTypeUtil.findConfigurationType(BeakerConfigurationType.class);
    }

    public BeakerConfigurationFactory getFactory() {
        return this.a;
    }

}