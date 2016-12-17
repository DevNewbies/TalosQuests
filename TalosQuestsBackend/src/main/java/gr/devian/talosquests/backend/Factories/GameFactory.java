package gr.devian.talosquests.backend.Factories;

import gr.devian.talosquests.backend.Exceptions.TalosQuestsLocationServiceUnavailableException;
import gr.devian.talosquests.backend.LocationProvider.LatLng;
import gr.devian.talosquests.backend.Models.Game;
import gr.devian.talosquests.backend.Models.User;

/**
 * Created by Nikolas on 15/12/2016.
 */
public class GameFactory {
    public static Game build(User user) throws TalosQuestsLocationServiceUnavailableException {
        Game game = new Game();
        LatLng userLocation = user.getLastLocation();
        game.setUser(user);
        game.setCurrentUserLocation(userLocation);

        QuestFactory qFactory = new QuestFactory(userLocation);

        game.setIncompleteQuests(qFactory.getQuestsInArea());

        return game;
    }
}
