package com.puppetlabs.BeakerIntellijPlugin.config;

import com.intellij.execution.Location;
import com.intellij.execution.actions.ConfigurationContext;
import com.intellij.execution.actions.RunConfigurationProducer;
import com.intellij.execution.configurations.ConfigurationTypeUtil;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.Ref;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.util.ObjectUtils;
import org.jetbrains.annotations.NotNull;
import com.puppetlabs.BeakerIntellijPlugin.settings.BeakerRunSettings;

/**
 * Created by samwoods on 9/6/15.
 */
public class BeakerRunConfigurationProducer extends RunConfigurationProducer<BeakerRunConfiguration>{
    private static final Logger LOG = Logger.getInstance(BeakerRunConfigurationProducer.class);

    public BeakerRunConfigurationProducer() {
        super(new BeakerConfigurationType());
    }

    @Override
    protected boolean setupConfigurationFromContext(BeakerRunConfiguration configuration, ConfigurationContext context, Ref<PsiElement> element) {

        RunConfiguration original = context.getOriginalConfiguration(null);
        if (original != null && !ConfigurationTypeUtil.equals(original.getType(),
                ConfigurationTypeUtil.findConfigurationType(BeakerConfigurationType.class))) {
            return false;
        }

        BeakerRunSettings settings = getBeakerRunSettings(configuration.getRunSettings(), context);
        configuration.setRunSettings(settings);

        configuration.setName(configuration.suggestedName());

        return true;
    }

    @NotNull
    private BeakerRunSettings getBeakerRunSettings(BeakerRunSettings baseSettings, ConfigurationContext context) {
        Location location = context.getLocation();
        BeakerRunSettings settings = baseSettings;
        if (settings == null){
            settings = new BeakerRunSettings();
        }

        PsiElement element = location.getPsiElement();

        PsiFile psiFile = ObjectUtils.tryCast(element, PsiFile.class);

        if (psiFile != null){
            VirtualFile file = psiFile.getVirtualFile();
            if (file == null) {
                LOG.info("Beaker Intellij Plugin - virtualFile is null");
            }
            settings.setTestFilePath(FileUtil.toSystemDependentName(file.getPath()));
        }
        else{
            PsiDirectory psiDirectory = ObjectUtils.tryCast(element, PsiDirectory.class);
            if (psiDirectory != null){
                VirtualFile directory = psiDirectory.getVirtualFile();
                settings.setTestFilePath(FileUtil.toSystemDependentName(directory.getPath()));
            }
        }

        return settings;
    }

    @Override
    public boolean isConfigurationFromContext(BeakerRunConfiguration runConfiguration, ConfigurationContext configurationContext) {
        BeakerRunSettings pattern = getBeakerRunSettings(new BeakerRunSettings(), configurationContext);
        if (pattern == null){
            return false;
        }

        BeakerRunSettings candidate = runConfiguration.getRunSettings();
        return pattern.getTestFilePath() == candidate.getTestFilePath();

    }

}
