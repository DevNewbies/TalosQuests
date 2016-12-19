package gr.devian.talosquests.backend.Services;

import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.DistanceMatrixElement;
import com.google.maps.model.TravelMode;
import gr.devian.talosquests.backend.Exceptions.TalosQuestsLocationServiceUnavailableException;
import gr.devian.talosquests.backend.LocationProvider.*;
import gr.devian.talosquests.backend.Models.Quest;
import gr.devian.talosquests.backend.Models.QuestChoice;
import gr.devian.talosquests.backend.Models.QuestModel;
import gr.devian.talosquests.backend.Repositories.QuestRepository;
import gr.devian.talosquests.backend.Utilities.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Nikolas on 2/12/2016.
 */
@Service
public class LocationService {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    public static Boolean enableService = true;

    @Autowired
    private void setRepo(QuestRepository repo) {
        questRepository = repo;
    }

    QuestRepository questRepository;

    @Value("${googleapi.key}")
    private void setApiKey(String api) {
        apiKey = api;
        GeoAPIHandler = new GeoApiContext().setApiKey(apiKey);

    }

    private String apiKey;

    private GeoApiContext GeoAPIHandler;

    public LocationService() {

    }

    public LatLng getLatLng(String address) throws TalosQuestsLocationServiceUnavailableException {
        if (!enableService)
            throw new TalosQuestsLocationServiceUnavailableException();
        try {
            return new LatLng(GeocodingApi.geocode(GeoAPIHandler,
                    address).await()[0].geometry.location);
        } catch (Exception e) {
            logger.info(e.getMessage());
            throw new TalosQuestsLocationServiceUnavailableException();
        }
    }

    public String getAddress(LatLng latlng) throws TalosQuestsLocationServiceUnavailableException {
        if (!enableService)
            throw new TalosQuestsLocationServiceUnavailableException();
        try {
            return GeocodingApi.reverseGeocode(GeoAPIHandler,
                    LatLng.getLatLng(latlng)).await()[0].formattedAddress;
        } catch (Exception e) {
            logger.info(e.getMessage());
            throw new TalosQuestsLocationServiceUnavailableException();
        }
    }

    public Location getQuestDistance(LatLng origin, Quest quest) throws TalosQuestsLocationServiceUnavailableException {
        if (!enableService)
            throw new TalosQuestsLocationServiceUnavailableException();
        try {
            DistanceMatrix dMatrix = DistanceMatrixApi.getDistanceMatrix(GeoAPIHandler, new String[]{origin.toString()}, new String[]{quest.getLocation().toString()}).mode(TravelMode.WALKING).await();
            return new Location(new Duration(dMatrix.rows[0].elements[0].duration), new Distance(dMatrix.rows[0].elements[0].distance), origin);
        } catch (Exception e) {
            logger.info(e.getMessage());
            throw new TalosQuestsLocationServiceUnavailableException();
        }
    }

    public HashMap<Quest, Location> getQuestDistances(LatLng origin, List<Quest> quests) throws TalosQuestsLocationServiceUnavailableException {
        if (!enableService)
            throw new TalosQuestsLocationServiceUnavailableException();
        try {
            HashMap<Quest, Location> mapQuqestDirDur = new HashMap<>();
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
                mapQuqestDirDur.put(quests.get(i), new Location(dur, dist, origin));
                i++;
            }
            return mapQuqestDirDur;

        } catch (Exception e) {
            logger.info(e.getMessage());
            throw new TalosQuestsLocationServiceUnavailableException();
        }
    }

    public ArrayList<Quest> getQuestsInRadius(LatLng origin, ArrayList<Quest> availableQuests, int radiusInMeters) throws TalosQuestsLocationServiceUnavailableException {

        return getQuestDistances(origin, availableQuests)
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue().getDistance().inMeters <= radiusInMeters)
                .map(Map.Entry::getKey)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public Tuple<Quest, Location> getClosestQuestDistance(LatLng origin, ArrayList<Quest> availableQuests) throws TalosQuestsLocationServiceUnavailableException {
        long min = Long.MAX_VALUE;
        Location minLocation = null;
        Quest minQuest = null;


        for (Map.Entry<Quest, Location> entry : getQuestDistances(origin, availableQuests).entrySet()) {
            if (entry.getValue().getDistance().inMeters < min) {
                min = entry.getValue().getDistance().inMeters;
                minQuest = entry.getKey();
                minLocation = entry.getValue();
            }
        }
        return new Tuple<Quest, Location>(minQuest, minLocation);
    }
}
