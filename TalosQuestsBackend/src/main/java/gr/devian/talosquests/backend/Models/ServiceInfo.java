package gr.devian.talosquests.backend.Models;

import com.fasterxml.jackson.annotation.JsonView;
import gr.devian.talosquests.backend.TalosQuestsBackendApplication;
import gr.devian.talosquests.backend.Views.View;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Created by Nikolas on 13/11/2016.
 */
@Component
@ConfigurationProperties
public class ServiceInfo {

    @Value("${version}")
    @JsonView(View.Simple.class)
    private String version;

    @Value("${application.build}")
    @JsonView(View.Simple.class)
    private String buildNumber;

    @JsonView(View.Simple.class)
    private final Boolean online = true;

    @JsonView(View.Simple.class)
    private String remoteAddr;


    public void setRemoteAddr(String remoteAddr) {
        this.remoteAddr = remoteAddr;
    }

    public String getVersion() {
        return version+"-build"+buildNumber;
    }

    public Boolean getOnline() {
        return online;
    }

    public String getRemoteAddr() {
        return remoteAddr;
    }

    @JsonView(View.Simple.class)
    public long getUptimeMilliseconds() {
        return new DateTime().getMillis()-TalosQuestsBackendApplication.getStartTime();
    }

    @JsonView(View.Simple.class)
    public String getUptime() {
        long millis = getUptimeMilliseconds();
        return String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), // The change is in this line
                TimeUnit.MILLISECONDS.toSeconds(millis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
    }
}
