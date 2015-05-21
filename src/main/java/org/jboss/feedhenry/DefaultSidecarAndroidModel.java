package org.jboss.feedhenry;

class DefaultSidecarAndroidModel implements SidecarAndroidModel {
    
    private final String buildToolsVersion;

    public DefaultSidecarAndroidModel(Builder builder) {
        buildToolsVersion = builder.getBuildToolsVersion();
    }

    @Override
    public String getBuildToolsVersion() {
        return buildToolsVersion;
    }

    public static class Builder {

        private String buildToolsVersion = "";
        
        public Builder() {
        }
        
        public Builder buildToolsVersion(String buildToolsVersion) {
            this.buildToolsVersion = buildToolsVersion;
            return this;
        }

        public String getBuildToolsVersion() {
            return buildToolsVersion;
        }
        
        
        
    }
    
}
