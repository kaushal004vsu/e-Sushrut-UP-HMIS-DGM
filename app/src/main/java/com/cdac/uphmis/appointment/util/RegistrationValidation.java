package com.cdac.uphmis.appointment.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Toast;

import static java.lang.Integer.parseInt;

public class RegistrationValidation {
    private Context context;
    private int appointmentValid = 1;

    private String patGender, patName, patCrNo, email, patMobileNo, address, patAge, country_code, district_code, state_code, pat_guardian, isPaymentDone, isGatewayAvailable;

    public RegistrationValidation(Context context,String patGender, String patName, String patCrNo, String email, String patMobileNo, String address, String patAge, String country_code, String district_code, String state_code, String pat_guardian, String isPaymentDone, String isGatewayAvailable) {
        this.context = context;
        this.patGender = patGender;
        this.patName = patName;
        this.patCrNo = patCrNo;
        this.email = email;
        this.patMobileNo = patMobileNo;
        this.address = address;
        this.patAge = patAge;
        this.country_code = country_code;
        this.district_code = district_code;
        this.state_code = state_code;
        this.pat_guardian = pat_guardian;
        this.isPaymentDone = isPaymentDone;
        this.isGatewayAvailable = isGatewayAvailable;
    }
    private void alert(String s) {
        Toast.makeText(context, "" + s, Toast.LENGTH_SHORT).show();
    }

    public Context getContext() {
        return context;
    }


    public int checkValidation() {
        //first name
        if (patName == null || patName.equals("")) {
            appointmentValid = 0;
            alert("Patient Name is mandatory!");
            return appointmentValid;
        }

        if (hasNumber(patName)) {
            appointmentValid = 0;
            alert("Patient name cannot be numeric!");
            return appointmentValid;
        }

//        if (patName.length() > 34) {
//            appointmentValid = 0;
//            alert("Patient name cannot be longer than 34 characters!");
//            return appointmentValid;
//        }


        if (address.isEmpty())
        {
            appointmentValid = 0;
            alert("Address is a mandatory field!");
            return appointmentValid;
        }

        //state code is mandatory
        if (state_code.equals("-1")) {
            appointmentValid = 0;
            alert("State is a mandatory field!");
            return appointmentValid;
        }

        //district code is mandatory
        if (district_code .equals("-1")) {
            appointmentValid = 0;
            alert("District is a mandatory field!");
            return appointmentValid;
        }

        //father name
        if (pat_guardian == null || pat_guardian.equals("")) {
            appointmentValid = 0;
            alert("Father's/Mother's/Spouse's name is mandatory!");
            return appointmentValid;
        }

        if (hasNumber(pat_guardian)) {
            appointmentValid = 0;
            alert("Father's/Mother's/Spouse's name cannot be numeric!");
            return appointmentValid;
        }

//        if (pat_guardian.length() > 34) {
//            appointmentValid = 0;
//            alert("Father's/Mother's/Spouse's name cannot be longer than 34 characters!");
//            return appointmentValid;
//        }




        //age is mandatory
        if (patAge.equals("")) {
            appointmentValid = 0;
            alert("Age is a mandatory field!");
            return appointmentValid;
        }

//        if (patAge.charAt(0) == '0') {
//            appointmentValid = 0;
//            alert("Age cannot begin with 0!");
//            return appointmentValid;
//        }


//        if (parseInt(patAge) > 125) {
//            try {
//                appointmentValid = 0;
//                alert("Age cannot be greater than 125!");
//
//            }catch(Exception ex)
//            {
//                ex.printStackTrace();
//            }
//            return appointmentValid;
//        }



        if (patGender.equals("-1")) {
            appointmentValid = 0;
            alert("Gender is mandatory!");
            return appointmentValid;
        }






        //mobile number is mandatory
        if (patMobileNo == null || patMobileNo.equals("")) {
            appointmentValid = 0;
            alert("Mobile Number is mandatory!");
            return appointmentValid;
        }

        if (patMobileNo.length() != 10) {
            appointmentValid = 0;
            alert("Mobile Number should be of 10 digits!");
            return appointmentValid;
        }


        if (patMobileNo.charAt(0) == '0') {
            appointmentValid = 0;
            alert("Please remove 0 before mobile number and enter the 10 digit mobile number!");
            return appointmentValid;
        }else {
            return appointmentValid = 1;
        }

//        if ((patMobileNo.charAt(0)) < (int) '6') {
//            appointmentValid = 0;
//            alert("Mobile number should begin with 6, 7, 8 or 9!");
//            return appointmentValid;
//        }
//        if (!isValidEmail(email))
//        {
//            appointmentValid = 0;
//            alert("Please enter a valid Email address.");
//            return appointmentValid;
//        }

    }

    private boolean hasNumber(String str) {
        if (str.contains("1") || str.contains("2") || str.contains("3") || str.contains("4") || str.contains("5") || str.contains("6") || str.contains("7") || str.equals("8") || str.contains("9") || str.contains("0")) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isValidEmail(CharSequence target) {
        if (!TextUtils.isEmpty(target)) {
            boolean b = Patterns.EMAIL_ADDRESS.matcher(target).matches();
            return b;
        }
        return true;
    }
}
