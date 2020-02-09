package ca.mcgill.ecse321.petadoptionsystem.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ca.mcgill.ecse321.petadoptionsystem.model.RegularUser;


@Repository
public interface RegularUserRepository extends CrudRepository<RegularUser, Integer> {
    RegularUser findRegularUserById(int id);
}