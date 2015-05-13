/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.saga.gradledigger;

import com.android.builder.model.AndroidProject;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.gradle.tooling.GradleConnector;
import org.gradle.tooling.ProjectConnection;
import org.gradle.tooling.UnknownModelException;
import org.gradle.tooling.model.GradleProject;

/**
 *
 * @author summers
 */
public class ProjectService {

    public Set getSubProjects(URI projectRoot) {
        return getSubProjects(projectRoot, GradleProject.class);
    }

    public <T> Set<T> getSubProjects(URI projectRoot, Class<T> projectModelClass) {
        File projectDir = new File(projectRoot);
        Set models = new HashSet();
        ProjectConnection projectConnection = null;
        try {
            projectConnection = GradleConnector.newConnector()
                    .useBuildDistribution()
                    .forProjectDirectory(projectDir)
                    .connect();

            GradleProject projectModel = null;
            try {
                projectModel = projectConnection.model(GradleProject.class).withArguments("--init-script", getClass().getClassLoader().getResource("init.gradle").toURI().toString()).get();
            } catch (URISyntaxException ex) {
                throw new RuntimeException(ex);
            }

            projectConnection.close();
            if (projectModelClass.equals(GradleProject.class)) {
                return (Set<T>) projectModel.getChildren();
            } else {
                for (GradleProject model : projectModel.getChildren()) {
                    ProjectConnection childProjectConnection = null;
                    try {
                        childProjectConnection = GradleConnector.newConnector()
                                .forProjectDirectory(model.getBuildScript().getSourceFile().getParentFile())
                                .connect();
                        models.add(childProjectConnection.getModel(projectModelClass));
                        
                    } catch (UnknownModelException ignore) {
                    } finally {
                        childProjectConnection.close();
                    }
                }
            }
            return models;
        } finally {
            if (projectConnection != null) {
                projectConnection.close();
            }
        }
    }

}
