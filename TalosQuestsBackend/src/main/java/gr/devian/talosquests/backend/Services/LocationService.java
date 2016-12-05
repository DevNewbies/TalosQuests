package gr.devian.talosquests.backend.Services;

import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.DistanceMatrixElement;
import com.google.maps.model.TravelMode;
import gr.devian.talosquests.backend.LocationProvider.*;
import gr.devian.talosquests.backend.Models.Quest;
import gr.devian.talosquests.backend.Models.User;
import gr.devian.talosquests.backend.Utilities.Tuple;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Nikolas on 2/12/2016.
 */
@Service
public class LocationService {

    @Value("${googleapi.key}")
    private static String apiKey;

    private static GeoApiContext GeoAPIHandler;

    static {
        GeoAPIHandler = new GeoApiContext().setApiKey(apiKey);
    }

    public static LatLng getLatLng(String address) throws Exception {
        return new LatLng(GeocodingApi.geocode(GeoAPIHandler,
                address).await()[0].geometry.location);
    }

    public static String getAddress(LatLng latlng) throws Exception {
        return GeocodingApi.reverseGeocode(GeoAPIHandler,
                LatLng.getLatLng(latlng)).await()[0].formattedAddress;
    }

    public static Location getQuestDistance(LatLng origin, Quest quest) throws Exception {
        DistanceMatrix dMatrix = DistanceMatrixApi.getDistanceMatrix(GeoAPIHandler, new String[]{origin.toString()}, new String[]{quest.getLocation().toString()}).mode(TravelMode.WALKING).await();
        return new Location(new Duration(dMatrix.rows[0].elements[0].duration), new Distance(dMatrix.rows[0].elements[0].distance), origin);
    }

    public static HashMap<Quest,Location> getQuestDistances(LatLng origin, List<Quest> quests) throws Exception {
        HashMap<Quest,Location> mapQuqestDirDur = new HashMap<>();
        String[] destinationsStr = new String[quests.size()];
        int i = 0;
        for (Quest quest : quests) {
            destinationsStr[i] = quest.getLocation().toString();
            i++;
        }
        DistanceMatrix dMatrix = DistanceMatrixApi.getDistanceMatrix(GeoAPIHandler, new String[]{origin.toString()}, destinationsStr).mode(TravelMode.WALKING).await();

        i = 0;
        for (DistanceMatrixElement element : dMatrix.rows[0].elements) {
            Distance dist = new Distance(element.distance);
            Duration dur = new Duration(element.duration);
            mapQuqestDirDur.put(quests.get(i),new Location(dur,dist,origin));
            i++;
        }
        return mapQuqestDirDur;
    }

    public static Tuple<Quest,Location> getClosestQuestDistance(LatLng origin, ArrayList<Quest> availableQuests) throws Exception {
        long min = 9999;
        Location minLocation = null;
        Quest minQuest = null;


        for (Map.Entry<Quest,Location> entry : getQuestDistances(origin, availableQuests).entrySet()) {
            if (entry.getValue().getDistance().inMeters < min) {
                min = entry.getValue().getDistance().inMeters;
                minQuest = entry.getKey();
                minLocation = entry.getValue();
            }
        }
        return new Tuple<Quest,Location>(minQuest,minLocation);
    }
}
