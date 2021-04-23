package toy.mongsil.tutorial.rest.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import toy.mongsil.tutorial.entity.Rest;
import toy.mongsil.tutorial.entity.User;

@Repository
public interface RestRepository extends CrudRepository<Rest, String>{

}
