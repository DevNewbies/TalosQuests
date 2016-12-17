package gr.devian.talosquests.backend.Repositories;

import gr.devian.talosquests.backend.Models.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Nikolas on 17/12/2016.
 */
@Transactional
@Repository
public interface GameRepository extends JpaRepository<Game,Long> {

}
