package nl.tudelft.oopp.demo.repositories;

import nl.tudelft.oopp.demo.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    default <S extends User> boolean existsByIdString(String id) {
        Long userIdLong = Long.parseLong(id);
        return this.existsById(userIdLong);
    }

    default <S extends User> User getOneByIdString(String id) {
        Long userIdLong = Long.parseLong(id);
        return this.findById(userIdLong).get();
    }

}
