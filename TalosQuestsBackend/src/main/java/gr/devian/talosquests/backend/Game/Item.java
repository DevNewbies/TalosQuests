package gr.devian.talosquests.backend.Game;

import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by Nikolas on 2/12/2016.
 */
@Component
@Entity
public class Item {

    @GeneratedValue
    @Id
    private long id;

    //String getName();
    //String getHint();

}
