package gr.devian.talosquests.backend.Repositories;

import gr.devian.talosquests.backend.Models.AccessLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Nikolas on 11/1/2017.
 */
@Transactional
@Repository
public interface AccessRepository extends JpaRepository<AccessLevel,Long> {
    AccessLevel findAccessLevelByName(String name);
}
