package ru.gulllak.placefinder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.gulllak.placefinder.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
