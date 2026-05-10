package org.example.aipoweredstudyresourcegenerator.Repo;

import org.example.aipoweredstudyresourcegenerator.Model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepo extends JpaRepository<Users, Long> {

    Optional<Users> findByGithubId(String githubId);
}
