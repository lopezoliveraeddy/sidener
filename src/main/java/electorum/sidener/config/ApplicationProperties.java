package electorum.sidener.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to JHipster.
 * <p>
 * Properties are configured in the application.yml file.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    private final Storage storage = new Storage();
    private final Scheduled scheduled = new Scheduled();

    public Storage getStorage() {
        return storage;
    }

    public Scheduled getScheduled() {
        return scheduled;
    }

    public static class Storage {
        /**
         * Folder location for storing files
         */
        private String location = "/files";
        private String temporal = "/tmp";

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getTemporal() {
            return temporal;
        }

        public void setTemporal(String temporal) {
            this.temporal = temporal;
        }

    }
    public static class Scheduled{

        private String deletefiles = "0 0 1 * * *";

        public String getDeletefiles() {
            return deletefiles;
        }

        public void setDeletefiles(String deletefiles) {
            this.deletefiles = deletefiles;
        }

    }

}
