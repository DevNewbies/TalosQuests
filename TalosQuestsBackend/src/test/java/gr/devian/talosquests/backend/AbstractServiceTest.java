package gr.devian.talosquests.backend;

import com.fasterxml.jackson.core.JsonProcessingException;
import gr.devian.talosquests.backend.Exceptions.TalosQuestsCredentialsNotMetRequirementsException;
import gr.devian.talosquests.backend.Exceptions.TalosQuestsException;
import gr.devian.talosquests.backend.Exceptions.TalosQuestsInsufficientUserData;
import gr.devian.talosquests.backend.Factories.QuestFactory;
import gr.devian.talosquests.backend.LocationProvider.LatLng;
import gr.devian.talosquests.backend.Models.*;
import gr.devian.talosquests.backend.Repositories.GameRepository;
import gr.devian.talosquests.backend.Repositories.QuestRepository;
import gr.devian.talosquests.backend.Repositories.UserQuestRepository;
import gr.devian.talosquests.backend.Repositories.UserRepository;
import gr.devian.talosquests.backend.Services.GameService;
import gr.devian.talosquests.backend.Services.LocationService;
import gr.devian.talosquests.backend.Services.UserService;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

/**
 * Created by Xrysa on 18/12/2016.
 */
public abstract class AbstractServiceTest extends AbstractTest {


}
