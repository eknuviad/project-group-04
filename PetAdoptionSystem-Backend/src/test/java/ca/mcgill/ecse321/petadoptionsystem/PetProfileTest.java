package ca.mcgill.ecse321.petadoptionsystem;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.petadoptionsystem.dao.AccountRepository;
import ca.mcgill.ecse321.petadoptionsystem.dao.AdminRepository;
import ca.mcgill.ecse321.petadoptionsystem.dao.AdoptionApplicationRepository;
import ca.mcgill.ecse321.petadoptionsystem.dao.DonationRepository;
import ca.mcgill.ecse321.petadoptionsystem.dao.ImageRepository;
import ca.mcgill.ecse321.petadoptionsystem.dao.PetAdoptionSystemRepository;
import ca.mcgill.ecse321.petadoptionsystem.dao.PetProfileRepository;
import ca.mcgill.ecse321.petadoptionsystem.dao.RegularUserRepository;
import ca.mcgill.ecse321.petadoptionsystem.model.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;


@ExtendWith(SpringExtension.class)

@SpringBootTest
public class PetProfileTest {


    @Autowired
    private PetProfileRepository petProfilerepository;

    @Autowired
    private PetAdoptionSystemRepository petAdoptionSystemRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private RegularUserRepository regularUserRepository;


    /**
     * Testing Persistance of the Database for the PetProfile
     */

    @Test
    public void TestPersistencePetProfile() {
        PetAdoptionSystem pas = TestingUtility.initPetAdoptionSystem(1);
        //Need to initialize the PetAdoptionSystem class before initializing lower classes
        petAdoptionSystemRepository.save(pas);

        Account act = TestingUtility.initAccount("Pedro", "pedro@gmail.com", pas);
        //Initializing Account
        accountRepository.save(act);
        act = null;
        act = accountRepository.findAccountByUsername("Pedro");

        RegularUser regUser = TestingUtility.initRegularUser(1111, act, pas);
        //Initializing the User
        regularUserRepository.save(regUser);
        regUser = null;
        regUser = regularUserRepository.findRegularUserById(1111);

        PetProfile petProf = TestingUtility.initPetProfile(1010, regUser, pas);
        //Initializing the PetProfile and setting the attributes that we want to test for persistence

        petProf.setBreed("chihuahua");
        petProf.setName("doggy");
        petProf.setDescription("fat and tired");
        petProf.setReasonForPosting("very ugly");
        petProf.setPetType(PetType.DOG);
        Date date = java.sql.Date.valueOf(LocalDate.of(2020, Month.FEBRUARY, 9));
        Time time = java.sql.Time.valueOf(LocalTime.of(11, 35));
        petProf.setPostDate(date);
        petProf.setPostTime(time);

        petProfilerepository.save(petProf);

        petProf = null;
        petProf = petProfilerepository.findPetProfileById(1010);

        //
        assertNotNull(petProf);
        assertEquals("chihuahua", petProf.getBreed());
        assertEquals("doggy", petProf.getName());
        assertEquals("fat and tired", petProf.getDescription());
        assertEquals("very ugly", petProf.getReasonForPosting());
        assertEquals(PetType.DOG, petProf.getPetType());
        assertEquals(date, petProf.getPostDate());
        assertEquals(time, petProf.getPostTime());

    }

    @AfterEach
    public void clearDatabase() {

        petProfilerepository.deleteAll();
        regularUserRepository.deleteAll();
        accountRepository.deleteAll();
        petAdoptionSystemRepository.deleteAll();

    }
}




