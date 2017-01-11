package gr.devian.talosquests.backend.Repositories;

import gr.devian.talosquests.backend.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Nikolas on 20/11/2016.
 */
@Transactional
@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    User findUserByUserName(String userName);
    User findUserByEmail(String email);
    List<User> findUserByUserNameLike(String userName);
    List<User> findUserByEmailLike(String email);
}
