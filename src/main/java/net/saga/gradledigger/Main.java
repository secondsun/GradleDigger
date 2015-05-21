/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.saga.gradledigger;

import java.io.File;
import  com.android.builder.model.AndroidProject;
import java.net.URISyntaxException;
import org.gradle.tooling.GradleConnector;
import org.gradle.tooling.ProjectConnection;

/**
 * This is just setting up and printing out information from a Gradle connection.
 *
 * @author summers
 */
public class Main {
    public static void main (String args[]) throws URISyntaxException {
        
        GradleConnector connector = GradleConnector.newConnector();
        File projectDir = new File(Main.class.getClassLoader().getResource("projects/android-gradle-helloworld/files").toURI());
        connector.forProjectDirectory(projectDir);
        ProjectConnection projectConnection = connector.connect();
        AndroidProject  projectModel = projectConnection.getModel(AndroidProject.class);
        
        projectConnection.close();
    }
}
