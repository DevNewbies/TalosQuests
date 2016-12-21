package gr.devian.talosquests.backend.Models;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by Nikolas on 13/11/2016.
 */
@Component
@ConfigurationProperties
public class ServiceInfo {

    @Value("${version}")
    private String version;

    @Value("${application.build}")
    private String buildNumber;

    private final Boolean isOnline = true;

    private String RemoteAddr;

    public ServiceInfo() {

    }

    public void setRemoteAddr(String remoteAddr) {
        RemoteAddr = remoteAddr;
    }

    public String getVersion() {
        return version+"-build"+buildNumber;
    }

    public Boolean getOnline() {
        return isOnline;
    }

    public String getRemoteAddr() {
        return RemoteAddr;
    }

}
