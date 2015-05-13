/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.aerogear.digger;

import com.android.builder.model.AndroidLibrary;
import com.android.builder.model.AndroidProject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;
import net.saga.gradledigger.Main;
import net.saga.gradledigger.ProjectService;
import org.jboss.feedhenry.SidecarAndroidModel;
import static org.jboss.util.ObjectUtils.printTree;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author summers
 */
public class BasicGradleProjectTest {

    private static final String HELLO_WORLD_PROJECT_ROOT = "projects/android-gradle-helloworld/files";

    @Test
    public void helloWorldHasOneProject() throws URISyntaxException {
        ProjectService service = new ProjectService();
        URI projectRoot = Main.class.getClassLoader().getResource(HELLO_WORLD_PROJECT_ROOT).toURI();
        Set projects = service.getSubProjects(projectRoot);
        assertEquals(1, projects.size());
    }

    @Test
    public void detectAndroidApplicationProject() throws URISyntaxException {
        ProjectService service = new ProjectService();
        URI projectRoot = Main.class.getClassLoader().getResource(HELLO_WORLD_PROJECT_ROOT).toURI();
        Set projects = service.getSubProjects(projectRoot, AndroidProject.class);
        assertEquals(1, projects.size());
    }

    @Test
    public void detectAndroidLibraryProject() throws URISyntaxException {
        ProjectService service = new ProjectService();
        URI projectRoot = Main.class.getClassLoader().getResource(HELLO_WORLD_PROJECT_ROOT).toURI();
        Set projects = service.getSubProjects(projectRoot, AndroidLibrary.class);
        assertEquals(0, projects.size());
    }

    @Test
    public void fetchProjectProperties() throws URISyntaxException {
        ProjectService service = new ProjectService();
        URI projectRoot = Main.class.getClassLoader().getResource(HELLO_WORLD_PROJECT_ROOT).toURI();
        Set<SidecarAndroidModel> projects = service.getSubProjects(projectRoot, SidecarAndroidModel.class);
        SidecarAndroidModel project = projects.iterator().next();
        System.out.println(printTree(project, 10));
        
    }



}
