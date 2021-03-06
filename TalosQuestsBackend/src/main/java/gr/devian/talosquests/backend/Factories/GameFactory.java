package gr.devian.talosquests.backend.Factories;

import gr.devian.talosquests.backend.Exceptions.TalosQuestsLocationNotProvidedException;
import gr.devian.talosquests.backend.Exceptions.TalosQuestsLocationServiceUnavailableException;
import gr.devian.talosquests.backend.Exceptions.TalosQuestsLocationsNotAvailableException;
import gr.devian.talosquests.backend.Models.LatLng;
import gr.devian.talosquests.backend.Models.Game;
import gr.devian.talosquests.backend.Models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Nikolas on 15/12/2016.
 */
@Service
public class GameFactory {

    @Autowired
    QuestFactory questFactory;

    public Game build(User user) throws TalosQuestsLocationServiceUnavailableException, TalosQuestsLocationNotProvidedException, TalosQuestsLocationsNotAvailableException {
        Game game = new Game();
        LatLng userLocation = user.getLastLocation();
        if (userLocation == null)
            throw new TalosQuestsLocationNotProvidedException();
        if (userLocation.getLat() == 0 || userLocation.getLng() == 0)
            throw new TalosQuestsLocationNotProvidedException();
        game.setUser(user);
        game.setCurrentUserLocation(userLocation);

        questFactory.setLocation(userLocation);

        game.setIncompleteUserQuests(questFactory.getQuestsInArea());

        return game;
    }
}
