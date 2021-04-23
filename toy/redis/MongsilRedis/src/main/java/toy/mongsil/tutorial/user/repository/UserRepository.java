package toy.mongsil.tutorial.user.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import toy.mongsil.tutorial.entity.User;

@Repository
public interface UserRepository extends CrudRepository<User, String>{

}
