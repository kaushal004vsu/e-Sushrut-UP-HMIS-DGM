package com.cdac.uphmis.util;

import android.content.Context;
import android.widget.Toast;

import static java.lang.Integer.parseInt;

public class Validation {

    Context registrationFragment;

   int registrationValid = 1;

    String firstname;
    String lastname;
    String age;
    String genderid;
    String fmsname;
    String stateid;
    String districtid;
    String city;
    String mobile;
    String email;
    String pincode;
    String departmentid;
    String categoryid;
    String ageLimit;
   public Validation(){}
   public Validation(Context registrationFragment, String firstname, String lastname, String age, String genderid, String fmsname, String stateid, String districtid, String city, String mobile, String email, String pincode, String departmentid, String categoryid, String ageLimit)
   {
       this.registrationFragment=registrationFragment;
       this.firstname=firstname;
       this.lastname=lastname;
       this.age=age;
       this.genderid=genderid;
       this.fmsname=fmsname;
       this.stateid=stateid;
       this.districtid=districtid;
       this.city=city;
       this.mobile=mobile;
       this.email=email;
       this.pincode=pincode;
       this.departmentid=departmentid;
       this.categoryid=categoryid;
       this.ageLimit=ageLimit;

   }

public void alert(String s)
{
    Toast.makeText(registrationFragment, ""+s, Toast.LENGTH_SHORT).show();
}
   public int checkValidation()
   {
       if (firstname.equals("")  || firstname.equals(null)) {
           registrationValid = 0;
           alert("First Name is mandatory!");
           return registrationValid;
       }

       if (hasNumber(firstname)){
           registrationValid = 0;
           alert("First name cannot be numeric!");
           return registrationValid;
       }

       if (firstname.length()>34){
            registrationValid = 0;
           alert("First name cannot be longer than 34 characters!");
           return registrationValid;
       }

       if (lastname.equals("")  || lastname .equals(null)) {
           registrationValid = 0;
           alert("Last Name is mandatory!");
           return registrationValid;
       }

       if (hasNumber(lastname)){
           registrationValid = 0;
           alert("Last name cannot be numeric!");
           return registrationValid;
       }

       if (lastname.length()>34) {
           registrationValid = 0;
           alert("Last name cannot be longer than 34 characters!");
           //document.getElementById("lastname").focus();
           return registrationValid;
       }


           //relationship name is mandatory
           if (fmsname .equals("")  || fmsname.equals(null))
           {
               registrationValid =0;
               alert("Father/Mother/Spouse's Name is mandatory!");
               return registrationValid;
           }

           if (hasNumber(fmsname)){
               registrationValid = 0;
               alert("Father/Mother/Spouse's name cannot be numeric!");
               return registrationValid;
           }

           if (fmsname.length()>34){
               registrationValid = 0;
               alert("Father/Mother/Spouse's name cannot be longer than 34 characters!");
               return registrationValid;
           }




           //age is mandatory
           if (age.equals("")) {
               registrationValid = 0;
               alert("Age is a mandatory field!");
               return registrationValid;
           }

           if (age.charAt(0)=='0'){
               registrationValid = 0;
               alert("Age cannot begin with 0!");
               return registrationValid;
           }
//       if (Integer.parseInt(age)>Integer.parseInt(ageLimit)){
//           registrationValid = 0;
//           alert("Age cannot be greater than "+ageLimit);
//           return registrationValid;
//       }
           if (parseInt(age)>125){
               registrationValid = 0;
               alert("Age cannot be greater than 125!");
               return registrationValid;
           }




       if (genderid .equals("-1")) {
           registrationValid = 0;
           alert("Gender is mandatory!");
           return registrationValid;
       }

       //state code is mandatory
       if (stateid.equals("-1")) {
           registrationValid = 0;
           alert("State is a mandatory field!");
           return registrationValid;
       }

       //district code is mandatory
       if (districtid .equals("-1")) {
           registrationValid = 0;
           alert("District is a mandatory field!");
           return registrationValid;
       }

//
//       //department is mandatory
//       if (departmentid.equals("-1")) {
//           registrationValid = 0;
//           alert("Department is a mandatory field!");
//           return registrationValid;
//       }

       //category is mandatory
       if (categoryid == "-1") {
           registrationValid = 0;
           alert("Patient category is a mandatory field!");
           return registrationValid;
       }

       //mobile number is mandatory
       if (mobile.equals("")|| mobile.equals(null)) {
           registrationValid = 0;
           alert("Mobile Number is mandatory!");
           return registrationValid;
       }

       if (mobile.length()!=10){
           registrationValid = 0;
           alert("Mobile Number should be of 10 digits!");
           return registrationValid;
       }



       if (mobile.charAt(0)=='0'){
           registrationValid = 0;
           alert("Please remove 0 before mobile number and enter the 10 digit mobile number!");

       }

       if ((mobile.charAt(0))<(int)'6'){
           registrationValid = 0;
           alert("Mobile number should begin with 6, 7, 8 or 9!");
           return registrationValid;
       }
      // echeck(email);
       if (pincode.length()>0 && pincode.length()!=6){
           registrationValid = 0;
           alert("Pincode should be of 6 digits!");
           return registrationValid;
       }


       else
       {
           return registrationValid=1;
       }
   }

public boolean hasNumber(String str)
{
    if(str.contains("1")||str.contains("2")||str.contains("3")||str.contains("4")||str.contains("5")||str.contains("6")||str.contains("7")||str.equals("8")||str.contains("9")||str.contains("0"))
    {
        return true;
    }
    else {
        return false;
    }
}


    public boolean echeck(String email) {

        String at="@";
        String dot=".";
        int lat=email.indexOf(at);
        int lstr=email.length();
        int ldot=email.indexOf(dot);
        if (email.indexOf(at)==-1){
            alert("Invalid E-mail ID");
            return false;
        }

        if (email.indexOf(at)==-1 || email.indexOf(at)==0 || email.indexOf(at)==lstr){
            alert("Invalid E-mail ID");
            return false;
        }

        if (email.indexOf(dot)==-1 || email.indexOf(dot)==0 || email.indexOf(dot)==lstr){
            alert("Invalid E-mail ID");
            return false;
        }

        if (email.indexOf(at,(lat+1))!=-1){
            alert("Invalid E-mail ID");
            return false;
        }

        if (email.substring(lat-1,lat)==dot || email.substring(lat+1,lat+2)==dot){
            alert("Invalid E-mail ID");
            return false;
        }

        if (email.indexOf(dot,(lat+2))==-1){
            alert("Invalid E-mail ID");
            return false;
        }

        if (email.indexOf(" ")!=-1){
            alert("Invalid E-mail ID");
            return false;
        }

        return true;
    }



}
