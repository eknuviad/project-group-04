package ca.mcgill.ecse321.petadoptionsystem.controller;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.petadoptionsystem.service.AdoptionApplicationService;
import ca.mcgill.ecse321.petadoptionsystem.service.PetProfileService;
import ca.mcgill.ecse321.petadoptionsystem.service.RegularUserService;
import ca.mcgill.ecse321.petadoptionsystem.dto.AdoptionApplicationDTO;
import ca.mcgill.ecse321.petadoptionsystem.dto.PetProfileDTO;
import ca.mcgill.ecse321.petadoptionsystem.dto.RegularUserDTO;
import ca.mcgill.ecse321.petadoptionsystem.model.AdoptionApplication;
import ca.mcgill.ecse321.petadoptionsystem.model.PetProfile;
import ca.mcgill.ecse321.petadoptionsystem.model.RegularUser;

@CrossOrigin(origins = "*")
@RestController
public class AdoptionApplicationRestController {

    @Autowired
    private AdoptionApplicationService appservice;

    @Autowired
    private RegularUserService regservice;

    @Autowired
    private PetProfileService profileservice;

    
    @GetMapping(value = { "/applications", "/applications/" })
    public List<AdoptionApplicationDTO> getAllApplications() throws IllegalArgumentException {
        List<AdoptionApplicationDTO> appDtos = new ArrayList<>();
        for (AdoptionApplication app : appservice.getAllApplications()) {
            appDtos.add(convertToDto(app));
        }
        return appDtos;
    }

    @GetMapping(value = { "/application/{id}", "/application/{id}/" })
    public AdoptionApplication getApplicationbyId(@PathVariable("id") int id) throws IllegalArgumentException {
        AdoptionApplication app = appservice.getApplicationbyId(id);
        return app;
    }

    @GetMapping(value = { "/application", "/application/" })
    public AdoptionApplication getApplication(@RequestParam(name = "application") AdoptionApplicationDTO appDTO)
            throws IllegalArgumentException {

        RegularUser ru = convertToDomainObject(appDTO.getRegularUserDTO());
        PetProfile pp = convertToDomainObject(appDTO.getPetProfileDTO());

        AdoptionApplication app = appservice.getAppbyAdopterAndPetProfile(ru, pp);

        return app;
    }

    @PostMapping(value = { "/apply", "/apply/" })
    public AdoptionApplicationDTO createApplication(@RequestParam Date postDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME, pattern = "HH:mm") LocalTime postTime,
            @RequestParam(name = "applicant") RegularUserDTO regUserDTO,
            @RequestParam(name = "petProfile") PetProfileDTO petprofDTO) throws IllegalArgumentException {

        RegularUser ru = regservice.getRegularUserByUsername(regUserDTO.getUser());
        PetProfile pp = profileservice.getPetProfileById(petprofDTO.getId());
        AdoptionApplication a = appservice.createApplication(postDate, Time.valueOf(postTime), ru, pp);

        return convertToDto(a);
    }

    @DeleteMapping(value = { "/deleteApplication", "/deleteApplication/" })
    public boolean deleteApplication(@RequestParam(name = "application") AdoptionApplicationDTO appDTO,
            @RequestParam(name = "applicant") RegularUserDTO regUserDTO,
            @RequestParam(name = "petProfile") PetProfileDTO petprofDTO) throws IllegalArgumentException {

        if (appDTO == null) {
            throw new NullPointerException("An application is required to be deleted");
        }
        RegularUser ru = regservice.getRegularUserByUsername(regUserDTO.getUser());
        PetProfile pp = profileservice.getPetProfileById(petprofDTO.getId());
   
        AdoptionApplication a = appservice.getAppbyAdopterAndPetProfile(ru, pp);
        Boolean result = appservice.deleteApplication(a);

        return result;
    }

    @GetMapping(value = { "/browse/applications/applicant", "/browse/applications/applicant/" })
    public List<AdoptionApplicationDTO> browseApplicationsOfApplicant(
            @RequestParam(name = "applicant") RegularUserDTO regUserDTO) throws IllegalArgumentException {

        if (regUserDTO == null) {
            throw new NullPointerException("A user is required to browse applications.");
        }
        RegularUser ru = regservice.getRegularUserByUsername(regUserDTO.getUser());

        List<AdoptionApplicationDTO> appDtos = new ArrayList<>();
        for (AdoptionApplication app : appservice.getApplicationsByUser(ru)) {
            appDtos.add(convertToDto(app));
        }
        return appDtos;
    }

    @GetMapping(value = { "/browse/applications/petprofile", "/browse/applications/petprofile/" })
    public List<AdoptionApplicationDTO> browseApplicationsToPetProfile(
            @RequestParam(name = "petProfile") PetProfileDTO petprofDTO) throws IllegalArgumentException {

        if (petprofDTO == null) {
            throw new NullPointerException("A pet is required to see all applications.");
        }
        PetProfile pp = profileservice.getPetProfileById(petprofDTO.getId());
        List<AdoptionApplicationDTO> appDtos = new ArrayList<>();
        for (AdoptionApplication app : appservice.getApplicationsByPetProfile(pp)) {
            appDtos.add(convertToDto(app));
        }
        return appDtos;
    }

    @PutMapping(value = { "/updateApplication", "/updateApplication/" })
    public AdoptionApplicationDTO updateApplication(@RequestParam(name = "application") AdoptionApplicationDTO appDTO,
            @RequestParam(value = "approve", required = false, defaultValue = "false") boolean approve,
            @RequestParam(value = "confirm", required = false, defaultValue = "false") boolean confirm) {

        if (appDTO == null) {
            throw new NullPointerException("An application is required to be updated");
        }
        AdoptionApplication updatedApp = appservice.updateApplicationStatus(convertToDomainObject(appDTO), approve,
                confirm);

        return convertToDto(updatedApp);

    }

    private RegularUser convertToDomainObject(RegularUserDTO ruDto) {
        List<RegularUser> allusers = regservice.getAllRegularUsers();
        for (RegularUser ru : allusers) {
            if (ru.getId() == (ruDto.getId())) {
                return ru;
            }
        }
        return null;
    }

    private PetProfile convertToDomainObject(PetProfileDTO ppDto) {
        List<PetProfile> allprofiles = profileservice.getAllPetProfiles();
        for (PetProfile pp : allprofiles) {
            if (pp.getId() == (ppDto.getId())) {
                return pp;
            }
        }
        return null;
    }

    private AdoptionApplication convertToDomainObject(AdoptionApplicationDTO appDto) {
        List<AdoptionApplication> allapps = appservice.getAllApplications();
        for (AdoptionApplication app : allapps) {
            if (app.getId() == (appDto.getId())) {
                return app;
            }
        }
        return null;
    }

    private AdoptionApplicationDTO convertToDto(AdoptionApplication app) {// TODO
        if (app == null) {
            throw new IllegalArgumentException("There is no such Application!");
        }
        RegularUserDTO regUser = convertToDto(app.getApplicant());
        PetProfileDTO petprof = convertToDto(app.getPetProfile());
        AdoptionApplicationDTO appDto = new AdoptionApplicationDTO(app.getId(), app.getPostDate(), app.getPostTime(),
                regUser, petprof);

        return appDto;
    }

    private PetProfileDTO convertToDto(PetProfile petProfile) {// TODO
        if (petProfile == null) {
            throw new IllegalArgumentException("There is no Pet Profile.");
        }
        PetProfileDTO profileDTO = new PetProfileDTO(petProfile.getPoster(), petProfile.getImage(),
                petProfile.getApplication(), petProfile.getName(), petProfile.getPetType(), petProfile.getBreed(),
                petProfile.getDescription(), petProfile.getId(), petProfile.getReasonForPosting(),
                petProfile.getPostDate(), petProfile.getPostTime(), petProfile.isIsAvailable());
        // This might have to be changed to convert image and poster into dtos before
        // creating pet perofile dtos
        return profileDTO;
    }

    private RegularUserDTO convertToDto(RegularUser applicant) {// TODO
        if (applicant == null) {
            throw new IllegalArgumentException("There is no Applicant.");
        }
        RegularUserDTO userDTO = new RegularUserDTO(applicant.getDonation(), applicant.getUser().getUsername(),
                applicant.getName(), applicant.getApplication(), applicant.getHomeDescription(),
                applicant.getPhoneNumber());
        // This might have to be changed to convert image and poster into dtos before
        // creating user dtos
        return userDTO;
    }

}