package ca.mcgill.ecse321.petadoptionsystem.controller;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.petadoptionsystem.dto.DonationDTO;
import ca.mcgill.ecse321.petadoptionsystem.dto.RegularUserDTO;
import ca.mcgill.ecse321.petadoptionsystem.model.Donation;
import ca.mcgill.ecse321.petadoptionsystem.service.DonationService;

/**
 * @author Ousmane Baricisse
 */
@CrossOrigin(origins = "*")
@RestController
public class DonationRestController {

    @Autowired
    private DonationService donationService;

    /**
     *
     * @param amount
     * @param username
     * @return
     * @throws IllegalArgumentException
     */
    @PostMapping(value = { "/donations", "/donation/" })
    public DonationDTO createDonation(
            @RequestParam float amount,
            @RequestParam("username") String username,
            @RequestParam("email") String email
            ) throws IllegalArgumentException {
            
            LocalTime localTime = LocalTime.now();
            LocalDate localDate = LocalDate.now();
            Time time = Time.valueOf(localTime);
            Date date = Date.valueOf(localDate);

        Donation d = donationService.createDonation(amount, date, time, username, email);

        return convertDonationToDTO(d);
    }

    /**
     * 
     * @return list of donations
     */
    @GetMapping(value = { "/donations", "/donations/" })
    public List<DonationDTO> getAllDonations() {
        List<DonationDTO> donationDtos = new ArrayList<>();
        for (Donation d : donationService.getAllDonation()) {
            donationDtos.add(convertDonationToDTO(d));
        }
        return donationDtos;
    }

    /**
     *
     * @param username
     * @return
     */
    @GetMapping(value = { "/donations/username", "/donations/username/" })
    public List<DonationDTO> getDonationByUsername(
            @RequestParam("username") String username) {
        List<DonationDTO> userDonationDtos = new ArrayList<>();
        for(Donation d : donationService.getDonationsByUsername(username)){
            userDonationDtos.add(convertDonationToDTO(d));
        }
        return userDonationDtos;
    }

    /**
     *
     * @param d
     * @return
     */
    public DonationDTO convertDonationToDTO(Donation d) {
        if (d == null)
            throw new IllegalArgumentException("There is no such Donation!");
       
        return new DonationDTO(d.getDonorEmail(), d.getTime(), d.getDate(), d.getAmount());
    }



}