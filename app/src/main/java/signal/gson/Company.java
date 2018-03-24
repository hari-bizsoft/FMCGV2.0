package signal.gson;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class Company{

	@SerializedName("UnderCompanyId")
	private int underCompanyId;

	@SerializedName("CompanyName")
	private String companyName;

	@SerializedName("LoginAccYear")
	private int loginAccYear;

	@SerializedName("CFiles")
	private List<Object> cFiles;

	@SerializedName("IsEnabled")
	private boolean isEnabled;

	@SerializedName("IsActive")
	private boolean isActive;

	@SerializedName("lstValidation")
	private List<Object> lstValidation;

	@SerializedName("CompanyType")
	private String companyType;

	@SerializedName("Id")
	private int id;

	@SerializedName("IsReadOnly")
	private boolean isReadOnly;

	public void setUnderCompanyId(int underCompanyId){
		this.underCompanyId = underCompanyId;
	}

	public int getUnderCompanyId(){
		return underCompanyId;
	}

	public void setCompanyName(String companyName){
		this.companyName = companyName;
	}

	public String getCompanyName(){
		return companyName;
	}

	public void setLoginAccYear(int loginAccYear){
		this.loginAccYear = loginAccYear;
	}

	public int getLoginAccYear(){
		return loginAccYear;
	}

	public void setCFiles(List<Object> cFiles){
		this.cFiles = cFiles;
	}

	public List<Object> getCFiles(){
		return cFiles;
	}

	public void setIsEnabled(boolean isEnabled){
		this.isEnabled = isEnabled;
	}

	public boolean isIsEnabled(){
		return isEnabled;
	}

	public void setIsActive(boolean isActive){
		this.isActive = isActive;
	}

	public boolean isIsActive(){
		return isActive;
	}

	public void setLstValidation(List<Object> lstValidation){
		this.lstValidation = lstValidation;
	}

	public List<Object> getLstValidation(){
		return lstValidation;
	}

	public void setCompanyType(String companyType){
		this.companyType = companyType;
	}

	public String getCompanyType(){
		return companyType;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setIsReadOnly(boolean isReadOnly){
		this.isReadOnly = isReadOnly;
	}

	public boolean isIsReadOnly(){
		return isReadOnly;
	}

	@Override
 	public String toString(){
		return 
			"Company{" + 
			"underCompanyId = '" + underCompanyId + '\'' + 
			",companyName = '" + companyName + '\'' + 
			",loginAccYear = '" + loginAccYear + '\'' + 
			",cFiles = '" + cFiles + '\'' + 
			",isEnabled = '" + isEnabled + '\'' + 
			",isActive = '" + isActive + '\'' + 
			",lstValidation = '" + lstValidation + '\'' + 
			",companyType = '" + companyType + '\'' + 
			",id = '" + id + '\'' + 
			",isReadOnly = '" + isReadOnly + '\'' + 
			"}";
		}
}