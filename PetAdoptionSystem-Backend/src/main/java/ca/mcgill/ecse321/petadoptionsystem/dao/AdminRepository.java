package ca.mcgill.ecse321.petadoptionsystem.dao;

import ca.mcgill.ecse321.petadoptionsystem.model.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ca.mcgill.ecse321.petadoptionsystem.model.Admin;

import java.util.List;

@Repository
public interface AdminRepository extends CrudRepository<Admin, Integer> {
    Admin findAdminById(int id);

    Admin findAdminByUser(Account account);

}