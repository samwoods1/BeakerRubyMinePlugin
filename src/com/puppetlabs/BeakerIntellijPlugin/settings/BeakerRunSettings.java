package com.puppetlabs.BeakerIntellijPlugin.settings;

/**
 * Created by samwoods on 9/6/15.
 */
public class BeakerRunSettings {
    private String configFile;
    private boolean useLatestPreserved;
    private String optionsFile;
    private String directory;
    private String testFilePath;
    private String rsaKey;
    private String additionalArguments;

    public BeakerRunSettings(){
        configFile = "hosts.cfg";
        useLatestPreserved = false;
        optionsFile = "options.rb";
        directory = "";
        testFilePath = "";
        rsaKey = "~/.ssh/id_rsa-acceptance";
        additionalArguments = "";
    }

    public String getConfigFile() {
        return configFile;
    }

    public boolean getUseLatestPreserved() {
        return useLatestPreserved;
    }

    public String getOptionsFile() {
        return optionsFile;
    }

    public String getDirectory() {
        return directory;
    }

    public String getTestFilePath() {
        return testFilePath;
    }

    public String getAdditionalArguments() {
        return additionalArguments;
    }

    public String getRsaKey() {
        return rsaKey;
    }

    public void setConfigFile(String myConfigFile) {
        this.configFile = myConfigFile;
    }

    public void setUseLatestPreserved(boolean useLatestPreserved) {
        this.useLatestPreserved = useLatestPreserved;
    }

    public void setOptionsFile(String myOptionsFile) {
        this.optionsFile = myOptionsFile;
    }

    public void setDirectory(String myDirectory) {
        this.directory = myDirectory;
    }

    public void setTestFilePath(String myTestFilePath) {
        this.testFilePath = myTestFilePath;
    }

    public void setRsaKey(String myRsaKey) {
        this.rsaKey = myRsaKey;
    }

    public void setAdditionalArguments(String additionalArguments) {
        this.additionalArguments = additionalArguments;
    }

}
