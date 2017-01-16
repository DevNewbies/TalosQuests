package gr.devian.talosquests.backend.Services;

import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.GeocodingApiRequest;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.DistanceMatrixElement;
import com.google.maps.model.TravelMode;
import gr.devian.talosquests.backend.Exceptions.TalosQuestsLocationServiceUnavailableException;
import gr.devian.talosquests.backend.Exceptions.TalosQuestsLocationsNotAvailableException;
import gr.devian.talosquests.backend.Models.Distance;
import gr.devian.talosquests.backend.Models.Duration;
import gr.devian.talosquests.backend.Models.LatLng;
import gr.devian.talosquests.backend.Models.Location;
import gr.devian.talosquests.backend.Models.UserQuest;
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

    public Boolean verifyLatLng(LatLng location) throws TalosQuestsLocationServiceUnavailableException {
        try {
            if (!enableService)
                throw new TalosQuestsLocationServiceUnavailableException();
            return GeocodingApi.reverseGeocode(GeoAPIHandler, LatLng.getLatLng(location)).await().length > 0;
        } catch (Exception e) {
            throw new TalosQuestsLocationServiceUnavailableException();
        }
    }

    /*
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
    */
    public HashMap<UserQuest, Location> getQuestDistances(LatLng origin, List<UserQuest> userQuests) throws TalosQuestsLocationServiceUnavailableException, TalosQuestsLocationsNotAvailableException {
        try {
            if (!enableService)
                throw new TalosQuestsLocationServiceUnavailableException();
            if (userQuests.size() <= 0)
                throw new TalosQuestsLocationsNotAvailableException();
            HashMap<UserQuest, Location> mapQuqestDirDur = new HashMap<>();
            String[] destinationsStr = new String[userQuests.size()];
            int i = 0;
            for (UserQuest userQuest : userQuests) {
                destinationsStr[i] = userQuest.getLocation().toString();
                i++;
            }

            DistanceMatrix dMatrix = DistanceMatrixApi.getDistanceMatrix(GeoAPIHandler, new String[]{origin.toString()}, destinationsStr).mode(TravelMode.WALKING).await();

            i = 0;
            for (DistanceMatrixElement element : dMatrix.rows[0].elements) {
                Distance dist = new Distance(element.distance);
                Duration dur = new Duration(element.duration);
                mapQuqestDirDur.put(userQuests.get(i), new Location(dur, dist));
                i++;
            }
            return mapQuqestDirDur;
        } catch (TalosQuestsLocationsNotAvailableException exc) {
            throw exc;
        } catch (Exception e) {
            logger.info(e.getMessage());
            throw new TalosQuestsLocationServiceUnavailableException();
        }
    }

    public ArrayList<UserQuest> getQuestsInRadius(LatLng origin, ArrayList<UserQuest> availableUserQuests, int radiusInMeters) throws TalosQuestsLocationServiceUnavailableException, TalosQuestsLocationsNotAvailableException {
        ArrayList<UserQuest> userQuests = getQuestDistances(origin, availableUserQuests)
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue().getDistance().inMeters <= radiusInMeters)
                .map(Map.Entry::getKey)
                .collect(Collectors.toCollection(ArrayList::new));
        if (userQuests.size() <= 0)
            throw new TalosQuestsLocationsNotAvailableException();
        return userQuests;
    }

    public Tuple<UserQuest, Location> getClosestQuestDistance(LatLng origin, ArrayList<UserQuest> availableUserQuests) throws TalosQuestsLocationServiceUnavailableException, TalosQuestsLocationsNotAvailableException {
        long min = Long.MAX_VALUE;
        Location minLocation = null;
        UserQuest minUserQuest = null;

        for (Map.Entry<UserQuest, Location> entry : getQuestDistances(origin, availableUserQuests).entrySet()) {
            if (entry.getValue().getDistance().inMeters < min) {
                min = entry.getValue().getDistance().inMeters;
                minUserQuest = entry.getKey();
                minLocation = entry.getValue();
            } else if (entry.getValue().getDistance().inMeters == min) {
                if (entry.getValue().getDuration().inSeconds < minLocation.getDuration().inSeconds) {
                    min = entry.getValue().getDistance().inMeters;
                    minUserQuest = entry.getKey();
                    minLocation = entry.getValue();
                }
            }
        }
        return new Tuple<>(minUserQuest, minLocation);
    }
}
