package bsuir.vintsarevich.task.repository;

import bsuir.vintsarevich.task.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    List<User> findByAuth0Id(String auth0Id);
    User getById(Integer id);
}