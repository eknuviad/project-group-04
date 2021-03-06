package ca.mcgill.ecse321.petadoptionsystem.controller;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.petadoptionsystem.service.AccountService;
import ca.mcgill.ecse321.petadoptionsystem.service.AdoptionApplicationService;
import ca.mcgill.ecse321.petadoptionsystem.service.PetProfileService;
import ca.mcgill.ecse321.petadoptionsystem.dto.AdoptionApplicationDTO;
import ca.mcgill.ecse321.petadoptionsystem.dto.PetProfileDTO;
import ca.mcgill.ecse321.petadoptionsystem.dto.RegularUserDTO;
import ca.mcgill.ecse321.petadoptionsystem.model.Account;
import ca.mcgill.ecse321.petadoptionsystem.model.AdoptionApplication;
import ca.mcgill.ecse321.petadoptionsystem.model.PetProfile;
import ca.mcgill.ecse321.petadoptionsystem.model.RegularUser;

@CrossOrigin(origins = "*")
@RestController
public class AdoptionApplicationRestController {

    @Autowired
    private AdoptionApplicationService appservice;

    @Autowired
    private PetProfileService petProfileservice;

    @Autowired
    private AccountService accountService;

    /**
     *
     * @return
     * @throws IllegalArgumentException
     */
    @GetMapping(value = { "/applications", "/applications/" })
    public List<AdoptionApplicationDTO> getAllApplications() throws IllegalArgumentException {
        List<AdoptionApplicationDTO> appDtos = new ArrayList<>();
        for (AdoptionApplication app : appservice.getAllApplications()) {
            appDtos.add(convertToDto(app));
        }
        return appDtos;
    }

    /**
     *
     * @param id
     * @return
     * @throws IllegalArgumentException
     */
    @GetMapping(value = { "/application/{id}", "/application/{id}/" })
    public AdoptionApplicationDTO getApplicationbyId(@PathVariable("id") int id) throws IllegalArgumentException {
        AdoptionApplication app = appservice.getApplicationbyId(id);
        return convertToDto(app);
    }

    /**
     *
     * @param ruDTO
     * @param ppDTO
     * @return
     * @throws IllegalArgumentException
     */
    @GetMapping(value = { "/application", "/application/" })
    public AdoptionApplicationDTO getApplication(RegularUserDTO ruDTO, PetProfileDTO ppDTO)
            throws IllegalArgumentException {

        if (ruDTO == null) {
            throw new NullPointerException("A user is required to create an application.");
        }
        if (ppDTO == null) {
            throw new NullPointerException("A pet profile is required to create an application.");
        }
        String applicant = ruDTO.getClient();
        int ppId = ppDTO.getId();

        AdoptionApplication app = appservice.getAppbyAdopterAndPetProfile(applicant, ppId);

        return convertToDto(app);
    }

    /**
     *
     * @param applicantName
     * @param petProfileID
     * @return
     * @throws IllegalArgumentException
     */
    @PostMapping(value = { "/apply", "/apply/" })
    public AdoptionApplicationDTO createApplication(
            @RequestParam("applicantName") String applicantName,
            @RequestParam("petProfileID") int petProfileID
            ) throws IllegalArgumentException {
        if (applicantName == null || applicantName.trim().length()==0) {
            throw new NullPointerException("A user is required to create an application.");
        }
        PetProfile petProf = petProfileservice.getPetProfileById(petProfileID);
        if ( petProf== null) {
            throw new NullPointerException("A pet profile is required to create an application.");
        }
        Account  act = accountService.getAccountByUsername(applicantName);
        if(act==null) throw new NullPointerException("There is no account associated to this username");
        LocalTime localTime = LocalTime.now();
        LocalDate localDate = LocalDate.now();
        Time time = Time.valueOf(localTime);
        Date date = Date.valueOf(localDate);

        AdoptionApplication a = appservice.createApplication(date, time, applicantName, petProfileID);

        return convertToDto(a);
    }

    /**
     *
     * @param appDTO
     * @param regUserDTO
     * @param petprofDTO
     * @return
     * @throws IllegalArgumentException
     */
    @DeleteMapping(value = { "/deleteApplication", "/deleteApplication/" })
    public boolean deleteApplication(AdoptionApplicationDTO appDTO, RegularUserDTO regUserDTO, PetProfileDTO petprofDTO)
            throws IllegalArgumentException {

        if (appDTO == null) {
            throw new NullPointerException("An application is required to be deleted");
        }
        String applicant = regUserDTO.getClient();
        int ppId = petprofDTO.getId();

        AdoptionApplication a = appservice.getAppbyAdopterAndPetProfile(applicant, ppId);
        Boolean result = appservice.deleteApplication(a);

        return result;
    }

    /**
     *
     * @param regUserDTO
     * @return
     * @throws IllegalArgumentException
     */
    @GetMapping(value = { "/browse/applications/applicant", "/browse/applications/applicant/" })
    public List<AdoptionApplicationDTO> browseApplicationsOfApplicant(RegularUserDTO regUserDTO)
            throws IllegalArgumentException {

        if (regUserDTO == null) {
            throw new NullPointerException("A user is required to browse applications.");
        }
        String ru = regUserDTO.getClient();

        List<AdoptionApplicationDTO> appDtos = new ArrayList<>();
        for (AdoptionApplication app : appservice.getApplicationsByUser(ru)) {
            appDtos.add(convertToDto(app));
        }
        return appDtos;
    }

    /**
     *
     * @param petprofDTO
     * @return
     * @throws IllegalArgumentException
     */
    @GetMapping(value = { "/browse/applications/petprofile", "/browse/applications/petprofile/" })
    public List<AdoptionApplicationDTO> browseApplicationsToPetProfile(PetProfileDTO petprofDTO)
            throws IllegalArgumentException {

        if (petprofDTO == null) {
            throw new NullPointerException("A pet is required to see all applications.");
        }
        PetProfile pp = petProfileservice.getPetProfileById(petprofDTO.getId());
        List<AdoptionApplicationDTO> appDtos = new ArrayList<>();
        for (AdoptionApplication app : appservice.getApplicationsByPetProfile(pp.getId())) {
            appDtos.add(convertToDto(app));
        }
        return appDtos;
    }

    /**
     *
     * @param appDTO
     * @param approve
     * @param confirm
     * @return
     */
    @PutMapping(value = { "/updateApplication", "/updateApplication/" })
    public AdoptionApplicationDTO updateApplication(AdoptionApplicationDTO appDTO,
            @RequestParam(value = "approve", required = false, defaultValue = "false") boolean approve,
            @RequestParam(value = "confirm", required = false, defaultValue = "false") boolean confirm) {

        if (appDTO == null) {
            throw new NullPointerException("An application is required to be updated");
        }
        AdoptionApplication updatedApp = appservice.updateApplicationStatus(convertToDomainObject(appDTO), approve,
                confirm);

        return convertToDto(updatedApp);

    }

    /**
     *
     * @param appDto
     * @return
     */
    private AdoptionApplication convertToDomainObject(AdoptionApplicationDTO appDto) {
        List<AdoptionApplication> allapps = appservice.getAllApplications();
        for (AdoptionApplication app : allapps) {
            if (app.getId() == (appDto.getApplicationID())) {
                return app;
            }
        }
        return null;
    }

    /**
     *
     * @param app
     * @return
     */
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

    /**
     *
     * @param petProfile
     * @return
     */
    private PetProfileDTO convertToDto(PetProfile petProfile) {// TODO
        if (petProfile == null) {
            throw new IllegalArgumentException("There is no Pet Profile.");
        }
        PetProfileDTO profileDTO = new PetProfileDTO( petProfile.getImages(),
                petProfile.getApplication(), petProfile.getName(), petProfile.getPetType(), petProfile.getBreed(),
                petProfile.getDescription(), petProfile.getId(), petProfile.getReasonForPosting(),
                petProfile.getPostDate(), petProfile.getPostTime(), petProfile.isIsAvailable());
        return profileDTO;
    }

    /**
     *
     * @param applicant
     * @return
     */
    private RegularUserDTO convertToDto(RegularUser applicant) {// TODO
        if (applicant == null) {
            throw new IllegalArgumentException("There is no Applicant.");
        }
        RegularUserDTO userDTO = new RegularUserDTO(applicant.getClient().getUsername(),
                applicant.getName(), applicant.getApplication(), applicant.getHomeDescription(),
                applicant.getPhoneNumber());
        return userDTO;
    }

}