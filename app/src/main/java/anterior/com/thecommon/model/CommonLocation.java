package anterior.com.thecommon.model;


import android.location.Address;
import android.location.Location;

public class CommonLocation {
    public Location currentLocation;

    public double latitude;
    public double longitude;
    public String address;
    public String address1;
    public String city;
    public String state;
    public String country;
    public String postalCode;
    public String countryCode;


    public void setAddress(Address locationAddress){
        address      = locationAddress.getAddressLine(0);
        address1     = locationAddress.getAddressLine(1);
        city         = locationAddress.getLocality();
        state        = locationAddress.getAdminArea();
        country      = locationAddress.getCountryName();
        postalCode   = locationAddress.getPostalCode();
        countryCode  = locationAddress.getCountryCode();
    }
}
