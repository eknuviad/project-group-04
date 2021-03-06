package ca.mcgill.ecse321.petadoptionsystem.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ca.mcgill.ecse321.petadoptionsystem.model.Account;

@Repository
public interface AccountRepository extends CrudRepository<Account, Integer> {

	Account findAccountByUsername(String username);
	Account findAccountByEmail(String email);
	boolean existsByUsername(String username);
	boolean existsByEmail(String email);


}