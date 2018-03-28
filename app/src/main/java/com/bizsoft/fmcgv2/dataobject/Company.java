package com.bizsoft.fmcgv2.dataobject;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by shri on 8/8/17.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Company {

    Long Id;
    public String CompanyName;
    String CompanyType;
    boolean IsActive;
    String AddressLine1;
    String AddressLine2;
    @JsonProperty("PostalCode")
    String PostalCode;


    String TelephoneNo;
    String EMailId;
    String GSTNo;
   String CityName;
    @JsonProperty("MobileNo")
   String MobileNo;

    public String getMobileNo() {
        return MobileNo;
    }

    public void setMobileNo(String mobileNo) {
        MobileNo = mobileNo;
    }

    boolean IsReadOnly;
   boolean IsEnabled;
    @JsonProperty("Logo")
    String Logo;

    public String getLogo() {
        return Logo;
    }

    public void setLogo(String logo) {
        Logo = logo;
    }

    public boolean isReadOnly() {
        return IsReadOnly;
    }

    public void setReadOnly(boolean readOnly) {
        IsReadOnly = readOnly;
    }

    public boolean isEnabled() {
        return IsEnabled;
    }

    public void setEnabled(boolean enabled) {
        IsEnabled = enabled;
    }

    public String getUnderCompanyId() {
        return UnderCompanyId;
    }

    public void setUnderCompanyId(String underCompanyId) {
        UnderCompanyId = underCompanyId;
    }

    String UnderCompanyId;

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        this.Id = id;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }

    public String getCompanyType() {
        return CompanyType;
    }

    public void setCompanyType(String companyType) {
        CompanyType = companyType;
    }

    public boolean isActive() {
        return IsActive;
    }

    public void setActive(boolean active) {
        IsActive = active;
    }

    public String getAddressLine1() {

        if(AddressLine1== null)
        {
            AddressLine1= "";
        }
        return AddressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        AddressLine1 = addressLine1;
    }

    public String getAddressLine2() {

        if(AddressLine2== null)
        {
            AddressLine2= "";
        }
        return AddressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        AddressLine2 = addressLine2;
    }

    public String getPostalCode() {

        if(PostalCode== null)
        {
            PostalCode= "";
        }
        return PostalCode;
    }

    public void setPostalCode(String postalCode) {
        PostalCode = postalCode;
    }

    public String getTelephoneNo() {
        if(TelephoneNo== null)
        {
            TelephoneNo = "";
        }
        return TelephoneNo;
    }

    public void setTelephoneNo(String telephoneNo) {
        TelephoneNo = telephoneNo;
    }

    public String getEMailId() {

        if(EMailId == null)
        {
            EMailId= "";
        }
        return EMailId;
    }

    public void setEMailId(String EMailId) {
        this.EMailId = EMailId;
    }

    public String getGSTNo() {

        if(GSTNo == null)
        {
            GSTNo= "";
        }

        return GSTNo;
    }

    public void setGSTNo(String GSTNo) {
        this.GSTNo = GSTNo;
    }

    public String getCityName() {
        return CityName;
    }

    public void setCityName(String cityName) {
        CityName = cityName;
    }
    public String toString() {
        StringBuilder result = new StringBuilder();
        String newLine = System.getProperty("line.separator");

        result.append( this.getClass().getName() );
        result.append( " Object {" );
        result.append(newLine);

        //determine fields declared in this class only (no fields of superclass)
        Field[] fields = this.getClass().getDeclaredFields();

        //print field names paired with their values
        for ( Field field : fields  ) {
            result.append("  ");
            try {
                result.append( field.getName() );
                result.append(": ");
                //requires access to private field:
                result.append( field.get(this) );
            } catch ( IllegalAccessException ex ) {
                System.out.println(ex);
            }
            result.append(newLine);
        }
        result.append("}");

        return result.toString();
    }

}
