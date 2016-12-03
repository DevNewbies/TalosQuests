package gr.devian.talosquests.backend.Repositories;

import gr.devian.talosquests.backend.Game.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Nikolas on 20/11/2016.
 */
@Transactional
@Repository
public interface UserRepository extends CrudRepository<User,Long> {
    User findUserByUserName(String userName);
    User findUserByEmail(String email);
}
