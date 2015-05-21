package org.jboss.feedhenry;

import java.io.Serializable;
import org.gradle.tooling.model.Model;

public interface SidecarAndroidModel extends Model, Serializable {
    String getBuildToolsVersion();
}
