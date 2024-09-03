package dev.momory.moneymindbackend.repository;

import dev.momory.moneymindbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Boolean existsByUserid(String userid);

    User findByUserid(String userid);

}
