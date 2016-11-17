package gr.devian.talosquests.backend;

/**
 * Created by Nikolas on 13/11/2016.
 */
public class ServiceInfo {

    private final String version = "1.0.0";

    private final Boolean isOnline = true;

    private final String RemoteAddr;
    private final String RemoteUser;

    public ServiceInfo(String ip,String user) {
        RemoteAddr = ip; RemoteUser = user;
    }

    public String getVersion() {
        return version;
    }

    public Boolean getOnline() {
        return isOnline;
    }

    public String getRemoteAddr() {
        return RemoteAddr;
    }
    public String getRemoteUser() { return RemoteUser; }
}
