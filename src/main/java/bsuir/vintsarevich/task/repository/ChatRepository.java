package bsuir.vintsarevich.task.repository;

import bsuir.vintsarevich.task.entity.Chat;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ChatRepository extends CrudRepository<Chat, Long> {
    List<Chat> findAll();
}
