package gr.devian.talosquests.backend.Factories;

import gr.devian.talosquests.backend.Exceptions.TalosQuestsLocationNotProvidedException;
import gr.devian.talosquests.backend.Exceptions.TalosQuestsLocationServiceUnavailableException;
import gr.devian.talosquests.backend.LocationProvider.LatLng;
import gr.devian.talosquests.backend.Models.Game;
import gr.devian.talosquests.backend.Models.Quest;
import gr.devian.talosquests.backend.Models.User;
import gr.devian.talosquests.backend.Services.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Nikolas on 15/12/2016.
 */
@Service
public class GameFactory {

    @Autowired
    QuestFactory questFactory;

    public Game build(User user) throws TalosQuestsLocationServiceUnavailableException, TalosQuestsLocationNotProvidedException {
        Game game = new Game();
        LatLng userLocation = user.getLastLocation();
        if (userLocation == null)
            throw new TalosQuestsLocationNotProvidedException();
        game.setUser(user);
        game.setCurrentUserLocation(userLocation);

        questFactory.setLocation(userLocation);

        game.setIncompleteQuests(questFactory.getQuestsInArea());

        return game;
    }
}
