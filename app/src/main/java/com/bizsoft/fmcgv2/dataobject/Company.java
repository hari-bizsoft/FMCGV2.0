package com.bizsoft.fmcgv2.dataobject;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by shri on 8/8/17.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Company {


    @JsonProperty("Id")
    Long Id;
    @JsonProperty("CompanyName")
    public String CompanyName;
    @JsonProperty("CompanyType")
    String CompanyType;
    @JsonProperty("IsActive")
    boolean IsActive;
    @JsonProperty("AddressLine1")
    String AddressLine1;
    @JsonProperty("AddressLine2")
    String AddressLine2;
    @JsonProperty("PostalCode")
    String PostalCode;
    @JsonProperty("TelephoneNo")
    String TelephoneNo;
    @JsonProperty("EMailId")
    String EMailId;
    @JsonProperty("GSTNo")
    String GSTNo;
    @JsonProperty("CityName")
   String CityName;
    @JsonProperty("MobileNo")
   String MobileNo;
    @JsonProperty("UnderCompanyId")
    Long UnderCompanyId;
    @JsonProperty("BankId")
    Long BankId;

    public Long getBankId() {
        return BankId;
    }

    public void setBankId(Long bankId) {
        BankId = bankId;
    }

    boolean synced ;

    public boolean isSynced() {
        return synced;
    }

    public void setSynced(boolean synced) {
        this.synced = synced;
    }

    public void init()
    {
        this.synced = true;
    }

/*    @JsonProperty("Logo")
    String Logo;
    @JsonProperty("CFiles")
    List<Object> CFiles;
    @JsonProperty("lstValidation")
    List<Object>lstValidation;

    @JsonProperty("UserId")
    String UserId;
    @JsonProperty("Password")
    String Password;
    @JsonProperty("LoginAccYear")
    String LoginAccYear;
        @JsonProperty("IsReadOnly")
    boolean IsReadOnly;
    @JsonProperty("IsEnabled")
   boolean IsEnabled;
*/





    public String getMobileNo() {
        return MobileNo;
    }

    public void setMobileNo(String mobileNo) {
        MobileNo = mobileNo;
    }



    public Long getUnderCompanyId() {
        return UnderCompanyId;
    }

    public void setUnderCompanyId(Long underCompanyId) {
        UnderCompanyId = underCompanyId;
    }



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
