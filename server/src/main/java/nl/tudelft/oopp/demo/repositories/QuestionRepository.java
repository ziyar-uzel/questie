package nl.tudelft.oopp.demo.repositories;

import java.util.Optional;

import nl.tudelft.oopp.demo.entities.Question;
import org.springframework.data.jpa.repository.JpaRepository;


public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Override
    Optional<Question> findById(Long id);
}
