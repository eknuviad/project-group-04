package ca.mcgill.ecse321.petadoptionsystem.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Id;
import java.sql.Date;
import java.sql.Time;

@Entity
public class AdoptionApplication{
   private PetProfile petProfile;
   
   @ManyToOne(optional=false)
   public PetProfile getPetProfile() {
      return this.petProfile;
   }
   
   public void setPetProfile(PetProfile petProfile) {
      this.petProfile = petProfile;
   }
   
   private RegularUser applicant;
   
   @ManyToOne(optional=false)
   public RegularUser getApplicant() {
      return this.applicant;
   }
   
   public void setApplicant(RegularUser applicant) {
      this.applicant = applicant;
   }
   
   private boolean isApproved;

public void setIsApproved(boolean value) {
    this.isApproved = value;
}
public boolean isIsApproved() {
    return this.isApproved;
}
private int id;

public void setId(int value) {
    this.id = value;
}
@Id
public int getId() {
    return this.id;
}
private Date postDate;

private void setPostDate(Date value) {
    this.postDate = value;
}
private Date getPostDate() {
    return this.postDate;
}
private Time postTime;

private void setPostTime(Time value) {
    this.postTime = value;
}
private Time getPostTime() {
    return this.postTime;
}
}
