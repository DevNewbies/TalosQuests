package gr.devian.talosquests.backend.Repositories;

/**
 * Created by Nikolas on 3/12/2016.
 */

import gr.devian.talosquests.backend.Models.User;
import gr.devian.talosquests.backend.Models.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface SessionRepository extends JpaRepository<Session,Long> {
    Session findSessionByToken(String token);
    Session findSessionByUser(User user);
    void deleteSessionByUser(User user);
    void deleteSessionByToken(String token);
}
