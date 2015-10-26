package com.puppetlabs.BeakerIntellijPlugin.settings;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserFactory;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.components.JBLabel;
import com.intellij.util.ui.UIUtil;
import com.puppetlabs.BeakerIntellijPlugin.config.BeakerRunConfiguration;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BeakerSettingsEditor extends SettingsEditor<BeakerRunConfiguration> {
    private static final Logger LOG = Logger.getInstance(BeakerSettingsEditor.class);

    private int verticalIncrementer = 1;

    private JPanel panel;
    private JBLabel rsaKeyLabel = new JBLabel("RSA Key:");
    private TextFieldWithBrowseButton rsaKeyTextFieldWithBrowseButton = new TextFieldWithBrowseButton();
    private JBLabel hostsConfigLabel = new JBLabel("Hosts Config file:");
    private TextFieldWithBrowseButton hostsConfigTextFieldWithBrowseButton = new TextFieldWithBrowseButton();
    private JBLabel optionsFileLabel = new JBLabel("Options file:");
    private TextFieldWithBrowseButton optionsFileTextFieldWithBrowseButton = new TextFieldWithBrowseButton();
    private JBLabel testFileLabel = new JBLabel("Test File or Dir:");
    private TextFieldWithBrowseButton testFileTextFieldWithBrowseButton = new TextFieldWithBrowseButton();
    private FileChooserDescriptor testFileChooserDescriptor = new FileChooserDescriptor(true, true, false, false, false, false);
    private JBLabel additionalArgumentsLabel = new JBLabel("Additional Arguments:");
    private JTextField additionalArgumentsTextField = new JTextField();
    private JBLabel workingDirectoryLabel = new JBLabel("Working Directory");
    private TextFieldWithBrowseButton workingDirectoryTextFieldWithBrowseButton = new TextFieldWithBrowseButton();
    private FileChooserDescriptor workingDirectoryFileChooserDescriptor = new FileChooserDescriptor(false, true, false, false, false, false);
    private JBCheckBox useLatestPreservedCheckBox = new JBCheckBox("Use latest preserved for this hosts file?");

    public BeakerSettingsEditor(Project project) {
        panel = new JPanel(new GridBagLayout());
        GridBagConstraints mainLabelConstraints = new GridBagConstraints(
                0, 0,
                5, 1,
                0.0, 0.0,
                GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, UIUtil.DEFAULT_HGAP),
                0, 0
        );
        panel.setPreferredSize(new Dimension(750, 0));
        mainLabelConstraints.gridwidth = 5;
        JBLabel mainLabel = new JBLabel("Beaker Test Runner Configuration");
        mainLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(mainLabel, mainLabelConstraints);
        this.AddFileChooserToPanel(hostsConfigLabel, hostsConfigTextFieldWithBrowseButton, true);
        panel.add(useLatestPreservedCheckBox, getFieldConstraints());
        verticalIncrementer++;
        this.AddFileChooserToPanel(optionsFileLabel, optionsFileTextFieldWithBrowseButton, true);
        this.AddProjectFileChooserToPanel(project, testFileLabel, testFileChooserDescriptor, testFileTextFieldWithBrowseButton);
        this.AddFileChooserToPanel(rsaKeyLabel, rsaKeyTextFieldWithBrowseButton, false);
        this.AddFieldToPanel(additionalArgumentsLabel, additionalArgumentsTextField);
        this.AddProjectFileChooserToPanel(project, workingDirectoryLabel, workingDirectoryFileChooserDescriptor, workingDirectoryTextFieldWithBrowseButton);
        verticalIncrementer++;
    }

    private void AddFileChooserToPanel(JBLabel chooserLabel, TextFieldWithBrowseButton textFieldWithBrowseButton, boolean hideFiles){
        chooserLabel.setAnchor(textFieldWithBrowseButton);
        chooserLabel.setLabelFor(textFieldWithBrowseButton);
        final FileChooserDescriptor descriptor = new FileChooserDescriptor(true, false, false, false, false, false);
        final JTextField myTextField = textFieldWithBrowseButton.getTextField();
        FileChooserFactory.getInstance().installFileCompletion(myTextField, descriptor, false, null);
        textFieldWithBrowseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                chooser.setFileHidingEnabled(hideFiles);
                int returnVal = chooser.showDialog(panel, "Create");
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    myTextField.setText(chooser.getSelectedFile().getPath());
                }
            }
        });
        AddFieldToPanel(chooserLabel, textFieldWithBrowseButton);
    }

    private void AddProjectFileChooserToPanel(Project project, JBLabel chooserLabel, FileChooserDescriptor fileChooser, TextFieldWithBrowseButton textFieldWithBrowseButton){
        textFieldWithBrowseButton.addBrowseFolderListener(
                null,
                null,
                project,
                fileChooser
        );
        AddFieldToPanel(chooserLabel, textFieldWithBrowseButton);
    }

    private void AddFieldToPanel(JBLabel label, JComponent component){
        label.setAnchor(component);
        label.setLabelFor(component);
        label.setMinimumSize(new Dimension(200, 0));
        panel.add(label, getLabelConstraints());
        panel.add(component, getFieldConstraints());
        verticalIncrementer++;
    }

    @NotNull
    private GridBagConstraints getLabelConstraints() {
        return new GridBagConstraints(
                0, verticalIncrementer,
                2, 1,
                0.17, 0.0,
                GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL,
                new Insets(UIUtil.DEFAULT_VGAP, 0, 0, UIUtil.DEFAULT_HGAP),
                0, 0
        );
    }

    @NotNull
    private GridBagConstraints getFieldConstraints() {
        return new GridBagConstraints(
                2, verticalIncrementer,
                3, 1,
                0.83, 0.0,
                GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL,
                new Insets(UIUtil.DEFAULT_VGAP, 0, 0, UIUtil.DEFAULT_HGAP),
                0, 0
        );
    }

    @Override
    protected void resetEditorFrom(BeakerRunConfiguration runConfiguration) {
        BeakerRunSettings runSettings = runConfiguration.getRunSettings();
        if (runSettings == null){
            LOG.info("Beaker Intellij Plugin - Run Settings were null, getting default settings.");
            runSettings = new BeakerRunSettings();
        }

        rsaKeyTextFieldWithBrowseButton.getTextField().setText(runSettings.getRsaKey());
        hostsConfigTextFieldWithBrowseButton.getTextField().setText(runSettings.getConfigFile());
        useLatestPreservedCheckBox.setSelected(runSettings.getUseLatestPreserved());
        optionsFileTextFieldWithBrowseButton.getTextField().setText(runSettings.getOptionsFile());
        testFileTextFieldWithBrowseButton.getTextField().setText(runSettings.getTestFilePath());
        additionalArgumentsTextField.setText(runSettings.getAdditionalArguments());
        LOG.debug("working directory = '" + runSettings.getDirectory() + "'");
        workingDirectoryTextFieldWithBrowseButton.getTextField().setText(runSettings.getDirectory());
    }

    @Override
    protected void applyEditorTo(BeakerRunConfiguration runConfiguration) throws ConfigurationException {
        runConfiguration.setRunSettings(
                rsaKeyTextFieldWithBrowseButton.getTextField().getText(),
                hostsConfigTextFieldWithBrowseButton.getTextField().getText(),
                useLatestPreservedCheckBox.isSelected(),
                optionsFileTextFieldWithBrowseButton.getTextField().getText(),
                testFileTextFieldWithBrowseButton.getTextField().getText(),
                additionalArgumentsTextField.getText(),
                workingDirectoryTextFieldWithBrowseButton.getTextField().getText());
    }

    @NotNull
    @Override
    protected JComponent createEditor() {
        return panel;
    }

}