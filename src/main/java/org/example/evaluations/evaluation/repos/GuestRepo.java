package org.example.evaluations.evaluation.repos;

import org.example.evaluations.evaluation.models.Guest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GuestRepo extends JpaRepository<Guest,String> {
    Optional<Guest> findByEmail(String email);
}
