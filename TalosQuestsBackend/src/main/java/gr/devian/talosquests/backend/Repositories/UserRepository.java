package gr.devian.talosquests.backend.Repositories;

import gr.devian.talosquests.backend.Models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Nikolas on 20/11/2016.
 */
@Transactional
@Repository
public interface UserRepository extends CrudRepository<User,Long> {
    User findUserByFacebookId(long facebookId);
    User findUserByUsername(String username);
    User findUserByAccessToken(String accessToken);
}
