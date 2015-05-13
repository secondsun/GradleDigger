package org.jboss.feedhenry;

import javax.inject.Inject;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.PluginManager;
import org.gradle.tooling.provider.model.ToolingModelBuilder;
import org.gradle.tooling.provider.model.ToolingModelBuilderRegistry;
import com.android.build.gradle.AppPlugin;
import com.android.build.gradle.LibraryPlugin;

public class GradleSidecarPlugin implements Plugin<Project> {

    private final ToolingModelBuilderRegistry registry;

    @Inject
    public GradleSidecarPlugin(ToolingModelBuilderRegistry registry) {
        this.registry = registry;
    }

    @Override
    public void apply(Project target) {
        registry.register(new SidecarAndroidModelBuilder());
    }

    private static class SidecarAndroidModelBuilder implements ToolingModelBuilder {

        private static final String ANDROID_APP_PLUGIN_ID = AppPlugin.class.getName();
        
        public SidecarAndroidModelBuilder() {
        }

        @Override
        public boolean canBuild(String modelName) {
            return modelName.equals(SidecarAndroidModel.class.getName());
        }

        @Override
        public Object buildAll(String modelName, Project project) {
            PluginManager pluginManager = project.getPluginManager();
            if (pluginManager.hasPlugin(ANDROID_APP_PLUGIN_ID)) {
                final AppPlugin appPlugin = (AppPlugin) pluginManager.findPlugin(ANDROID_APP_PLUGIN_ID);
                return new DefaultSidecarAndroidModel();
            } else if (pluginManager.hasPlugin(LibraryPlugin.class.getName())) {
                throw new IllegalStateException("Not Supported Yet");
            }
            return null;
        }

    }

}
