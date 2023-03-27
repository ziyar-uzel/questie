package nl.tudelft.oopp.demo.repositories;

import java.util.Optional;
import nl.tudelft.oopp.demo.entities.LecturePace;
import nl.tudelft.oopp.demo.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;



public interface LecturePaceRepository extends JpaRepository<LecturePace, Long> {


    @Query("SELECT l FROM LecturePace l WHERE l.room=?1")
    Optional<LecturePace>  findByRoom(Room room);







}
