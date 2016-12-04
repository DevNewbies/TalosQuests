package gr.devian.talosquests.backend.Repositories;

/**
 * Created by Nikolas on 3/12/2016.
 */

import gr.devian.talosquests.backend.Game.User;
import gr.devian.talosquests.backend.Game.UserSession;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface UserSessionRepository extends CrudRepository<UserSession,Long> {
    UserSession findUserSessionByToken(String token);
    void deleteUserSessionByUser(User usr);
}
