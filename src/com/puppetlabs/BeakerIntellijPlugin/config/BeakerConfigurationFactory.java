

package com.puppetlabs.BeakerIntellijPlugin.config;

import com.intellij.execution.RunnerAndConfigurationSettings;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.ArrayUtil;
import com.intellij.util.Function;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.plugins.ruby.ruby.run.configuration.AbstractRubyRunConfiguration;
import org.jetbrains.plugins.ruby.ruby.run.configuration.RubyRunConfigurationFactoryBase;
import org.jetbrains.plugins.ruby.ruby.run.configuration.RunConfigurationUtil;

/**
 * Created by samwoods on 6/11/15.
 */
public class BeakerConfigurationFactory extends RubyRunConfigurationFactoryBase {
    private static final String FACTORY_NAME = "Beaker configuration factory";

    public BeakerConfigurationFactory(BeakerConfigurationType beakerConfigurationType) {
        super(beakerConfigurationType);
        this.setFactoryMethod(new Function() {
            public AbstractRubyRunConfiguration fun(Object var1) {
                return new BeakerRunConfiguration((Project)var1, BeakerConfigurationFactory.this);
            }
        });
    }

    public RunnerAndConfigurationSettings createConfigurationSettings(@NotNull Module var1, @NotNull String var2) {
        String var3 = a(var1);
        RunnerAndConfigurationSettings var4 = RunConfigurationUtil.createSettings(var1.getProject(), this, "");
        BeakerRunConfiguration var5 = (BeakerRunConfiguration)var4.getConfiguration();
        var5.setWorkingDirectory(var3);
        return var4;
    }

    protected void initWithDefaultModule(AbstractRubyRunConfiguration var1, @NotNull Module var2) {
        super.initWithDefaultModule(var1, var2);
        var1.setWorkingDirectory(a(var2));
    }

    @Nullable
    private static String a(@NotNull Module var0) {
        ModuleRootManager var1 = ModuleRootManager.getInstance(var0);
        if(var1 != null) {
            VirtualFile[] var2 = var1.getContentRoots();
            if(var2.length == 1) {
                VirtualFile var3 = ArrayUtil.getFirstElement(var2);
                if(var3 != null) {
                    return var3.getPath();
                }
            }
        }

        return null;
    }
}
