package team6.travelplanner.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import team6.travelplanner.models.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
