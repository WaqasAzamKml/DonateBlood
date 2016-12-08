package net.itempire.donateblood;

/**
 * Created by MIRSAAB on 12/3/2016.
 */

public class DonationRequests {
    private String requestBloodGroup;
    private String requestDate;
    private String requestCity;
    private String requestIcon = "{faw-eye}";

    public DonationRequests(){

    }

    public String getRequestBloodGroup() {
        return requestBloodGroup;
    }

    public void setRequestBloodGroup(String requestBloodGroup) {
        this.requestBloodGroup = requestBloodGroup;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }

    public String getRequestCity() {
        return requestCity;
    }

    public void setRequestCity(String requestCity) {
        this.requestCity = requestCity;
    }

    public String getRequestIcon() {
        return requestIcon;
    }
}
