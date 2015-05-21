package org.jboss.feedhenry;

import javax.inject.Inject;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.tooling.provider.model.ToolingModelBuilder;
import org.gradle.tooling.provider.model.ToolingModelBuilderRegistry;
import com.android.build.gradle.AppPlugin;
import com.android.build.gradle.LibraryPlugin;

public class GradleSidecarPlugin implements Plugin<Project> {

    private final ToolingModelBuilderRegistry registry;
    private static final String ANDROID_APP_PLUGIN_ID = "com.android.application";
    
    
    @Inject
    public GradleSidecarPlugin(ToolingModelBuilderRegistry registry) {
        this.registry = registry;
    }

    @Override
    public void apply(Project target) {
        registry.register(new SidecarAndroidModelBuilder());
    }

    private static class SidecarAndroidModelBuilder implements ToolingModelBuilder {

        public SidecarAndroidModelBuilder() {
        }

        @Override
        public boolean canBuild(String modelName) {
            return modelName.equals(SidecarAndroidModel.class.getName());
        }

        @Override
        public Object buildAll(String modelName, Project project) {
            
            if (project.getPlugins().hasPlugin(ANDROID_APP_PLUGIN_ID)) {
                Object appPlugin = project.getPlugins().findPlugin(ANDROID_APP_PLUGIN_ID);
                
                DefaultSidecarAndroidModel.Builder builder = new DefaultSidecarAndroidModel.Builder();
                builder.buildToolsVersion(appPlugin.extension.buildToolsRevision.toString());
                
                return new DefaultSidecarAndroidModel(builder);
            } else if (project.getPlugins().hasPlugin(LibraryPlugin.class.getName())) {
                throw new IllegalStateException("Not Supported Yet");
            }
            return null;
        }

    }

}
