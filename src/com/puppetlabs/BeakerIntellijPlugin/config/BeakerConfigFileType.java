package com.puppetlabs.BeakerIntellijPlugin.config;


import com.intellij.icons.AllIcons;
import com.intellij.lang.Language;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.fileTypes.PlainTextLanguage;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * Created by samwoods on 9/13/15.
 */
public class BeakerConfigFileType extends LanguageFileType {

    public static final BeakerConfigFileType INSTANCE = new BeakerConfigFileType();

    /**
     * Creates a language file type for the specified language.
     */
    protected BeakerConfigFileType() {
        super(findLanguage());
    }

    @NotNull
    private static Language findLanguage() {
        Language language = Language.findLanguageByID("yaml");
        if (language == null) {
            language = PlainTextLanguage.INSTANCE;
        }
        return language;
    }

    @NotNull
    @Override
    public String getName() {
        return "Beaker Test Runner";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Beaker Test Runner configuration file";
    }

    @NotNull
    @Override
    public String getDefaultExtension() {
        return "beaker";
    }

    @NotNull
    @Override
    public Icon getIcon() {
        return AllIcons.General.Information;
    }

}