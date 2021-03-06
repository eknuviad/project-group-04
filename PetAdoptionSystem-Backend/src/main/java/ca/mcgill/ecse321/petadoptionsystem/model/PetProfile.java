package ca.mcgill.ecse321.petadoptionsystem.model;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.HashSet;
import java.util.Set;
import java.sql.Date;
import java.sql.Time;

@Entity
public class PetProfile {
    public UserRole poster;

    @ManyToOne(optional = false)
    @JoinColumn(name="poster_id")
    @JsonBackReference
    public UserRole getPoster() {
        return this.poster;
    }

    public void setPoster(UserRole poster) {
        this.poster = poster;
    } 
    
    public HashSet<String> images = new HashSet<>();

    public void addImage(String image){
        this.images.add(image);
    }
    public void setImages(HashSet<String> set){
        this.images = set;
    }

    public HashSet<String> getImages(){
        return images;
    }

    private Set<AdoptionApplication> application;
   @JsonBackReference
    @OneToMany(mappedBy = "petProfile", cascade = CascadeType.REMOVE)
    public Set<AdoptionApplication> getApplication() {
        return this.application;
    }

    public void setApplication(Set<AdoptionApplication> applications) {
        this.application = applications;
    }

    public String name;

    public void setName(String value) {
        this.name = value;
    }

    public String getName() {
        return this.name;
    }

    public PetType petType;

    public void setPetType(PetType value) {
        this.petType = value;
    }

    @Enumerated
    public PetType getPetType() {
        return this.petType;
    }

    public String breed;

    public void setBreed(String value) {
        this.breed = value;
    }

    public String getBreed() {
        return this.breed;
    }

    public String description;

    public void setDescription(String value) {
        this.description = value;
    }

    public String getDescription() {
        return this.description;
    }

    public int id;

    public void setId(int value) {
        this.id = value;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int getId() {
        return this.id;
    }

    public String reasonForPosting;

    public void setReasonForPosting(String value) {
        this.reasonForPosting = value;
    }

    public String getReasonForPosting() {
        return this.reasonForPosting;
    }

    public Date postDate;

    public void setPostDate(Date value) {
        this.postDate = value;
    }

    public Date getPostDate() {
        return this.postDate;
    }

    public Time postTime;

    public void setPostTime(Time value) {
        this.postTime = value;
    }

    public Time getPostTime() {
        return this.postTime;
    }

    public boolean isAvailable;

    public void setIsAvailable(boolean value) {
        this.isAvailable = value;
    }

    public boolean isIsAvailable() {
        return this.isAvailable;
    }

}
