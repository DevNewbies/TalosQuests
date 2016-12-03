package gr.devian.talosquests.backend.Game.Location;

import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.DistanceMatrixElement;
import com.google.maps.model.TravelMode;
import gr.devian.talosquests.backend.Game.Quest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nikolas on 2/12/2016.
 */
public class LocationProvider {

    private static GeoApiContext GeoAPIHandler;

    static {
        GeoAPIHandler = new GeoApiContext().setApiKey("AIzaSyBYSx7H98T4SCeqBPCz6lMDpDhfKBbXmHo");
    }

    public static LatLng getLatLng(String address) throws Exception {
        return new LatLng(GeocodingApi.geocode(GeoAPIHandler,
                address).await()[0].geometry.location);
    }

    public static String getAddress(LatLng latlng) throws Exception {
        return GeocodingApi.reverseGeocode(GeoAPIHandler,
                LatLng.getLatLng(latlng)).await()[0].formattedAddress;
    }

    public static QuestDistance getQuestDistance(LatLng origin, Quest quest) throws Exception {
        DistanceMatrix dMatrix = DistanceMatrixApi.getDistanceMatrix(GeoAPIHandler, new String[]{origin.toString()}, new String[]{quest.getLocation().toString()}).mode(TravelMode.WALKING).await();
        return new QuestDistance(new Distance(dMatrix.rows[0].elements[0].distance), new Duration(dMatrix.rows[0].elements[0].duration), origin, quest.getLocation(),quest);
    }

    public static ArrayList<QuestDistance> getQuestDistances(LatLng origin, List<Quest> quests) throws Exception {
        ArrayList<QuestDistance> qDList = new ArrayList<>();
        String[] destinationsStr = new String[quests.size()];
        int i = 0;
        for (Quest quest : quests) {
            destinationsStr[i] = quest.getLocation().toString();
            i++;
        }
        DistanceMatrix dMatrix = DistanceMatrixApi.getDistanceMatrix(GeoAPIHandler, new String[]{origin.toString()}, destinationsStr).mode(TravelMode.WALKING).await();

        i = 0;
        for (DistanceMatrixElement element : dMatrix.rows[0].elements) {
            QuestDistance dist = new QuestDistance(new Distance(element.distance), new Duration(element.duration), origin, quests.get(i).getLocation(), quests.get(i));
            qDList.add(dist);
        }
        return qDList;
    }

    public static QuestDistance getClosestQuestDistance(LatLng origin, ArrayList<Quest> availableQuests) throws Exception {
        long min = 9999;
        QuestDistance minQuestDistance = null;

        for (QuestDistance questDistance : getQuestDistances(origin, availableQuests)) {
            if (questDistance.getDistance().inMeters < min) {
                min = questDistance.getDistance().inMeters;
                minQuestDistance = questDistance;
            }
        }
        return minQuestDistance;
    }
}
