package com.cdac.uphmis.util;


/**
 * @author Sudeep Rai
 * This file contains all the api endpoints
 */
public class ServiceUrl {
    //todo change server ip
//    Production DGME
    public static String ip = "https://hmisdgmeup.prd.dcservices.in/";
    //UP NHM
//    public static  String nhmIp="http://10.10.10.224:8082/";
//    public static  String dgmeIp="https://hmisdgmeup.prd.dcservices.in/";
//    public static  String ip;

    //DGME UAT
//    public static String ip = "http://10.10.11.17:8082/";

    public static String railtel_uat_ip = "http://10.10.11.17:8082/";
    public static String ndhmip = "http://10.10.11.17:8082/";
    public static String hospId = "100";

//    public static String ndhmip = "https://ndhmexchange.uat.dcservices.in/";
//    public static String hospId = "100";

    public static String testingMobile = "7042420714";

    public static String snomedUl = ip + "csnoserv/api/search/search?term=";
    public static String testurl = ip + "HISServices/service/";
    public static String testurl2 = ip + "eSushrutEMRServices/service/";
    public static String DoctorDeskUrl = ip + "HISDRDESK/new_opd/transaction/DoctorDeskAction.cnt?hmode=NEW1&seat_id=";

    //opd enquiry
    public static String urldepartment = testurl + "opdDeptList/regdeptlist?hospCode=";
    public static String urlenquiry = testurl + "consultant/consultantByDept";

    //lab enquiry
    public static String getLabsurl = testurl + "investigationtest/labtestlist?hospCode=";
    //tariff enquiry
    public static String tariffurl = testurl + "tariff/tariffList?hospCode=";

    //drug availability
    //public static String getDrugAvailabilityUrl = testurl + "UserService/getDrugAvailability?hosp_code="+hospId;
    public static String getDrugAvailabilityUrl = testurl + "railtelService/getDrugAvailability";


    //lab report
    public static String getReportList = testurl + "invService/reportList?crNo=";
    public static String getReportpdf = testurl + "invService/reportData?crNo=";


    //blood stock enquiry
    public static String bloodStockurl = "https://eraktkosh.in/BLDAHIMS/eraktkoshbloodstockformp?key=ecfca931-d701-46ef-9eb6-63d123f3718c&hospCode=280561&reqType=1";


    //trends chart
    public static String getTestReoprtTableListurl = testurl + "invService/investigationDataList?crNo=";
    public static String getTrendsChart = testurl + "invService/paraRawData?crNo=";
    public static String getPatentDetails = testurl + "login/patCRLogin?hCode=" + hospId + "&crNo=";


    //prescription scan view
    public static String getPatDetailsScanPrescriptionurl = testurl + "UserService/getUserPatData";
    public static String getPatDetailsViewPrescriptionurl = testurl + "UserService/getUserPatDataViewPrescription";
    public static String ViewPrescriptionImageurl = testurl + "UserService/retriveImageData";
    public static String uploadPrescriptionurl = testurl + "UserService/getImageData";
    public static String checkPrescriptionStatus = testurl + "UserService/getPatDataCheckPrescription";


    public static String urlstates = testurl + "generalService/stateList";
    public static String urldistricts = testurl + "generalService/districtList?state=";

    public static String checkUpdateurl = testurl + "app/config?hospCode=" + hospId + "&platform=android&appVer=1&userCat=";
    public static String checkUpdateGenericurl = "app/config?hospCode=" + hospId + "&platform=android&appVer=1&userCat=";

    //Doctor Login
    public static String loginSalturl = testurl + "login/salt";
    public static String loginCheckurl = testurl + "login/checkBeforeLogin";

    public static String getHospitalUrl = testurl + "eConsultReq/teleconsultancyHospitalList";
    public static String getHospitalUrl2 = testurl + "railtelService/getHospitalList?zoneId=0&divisionId=0";


    public static String getRegisteredPatientPreviousRegistrations = testurl + "eConsultReq/getCrNoFromMobileNo?mobileNo=";

    //econsultation document upload/view
    public static String uploadDocument = testurl + "eConsultReq/setImageAndData";
    public static String viewDocument = testurl + "eConsultReq/retriveImageData";

    //teleconsultation
    public static String savePrescriptionUrl = testurl + "econsultation/savePrescription";
    public static String getPastPrescription = testurl + "eConsultReq/getPastPrescription?hospCode=";

    public static String getPastWebPrescription = ip + "HISDRDESK/services/restful/patdata/digi?";

    public static String getDocumentStatus = testurl + "eConsultReq/getDocStatus?requestId=";
    public static String generateEpisode = testurl + "eConsultReq/generateEpisodeWithReqNo?requestId=";
    public static String teleconsultationsDepartments = testurl + "eConsultReq/getSpecialClinicsList?hospCode=";
    public static String feedbackUrl = testurl + "eConsultReq/patientFeedback";
    public static String getSlots = testurl + "eConsultReq/getSpecialClinicSlot?hospCode=";
    public static String bookAppointment = testurl + "eConsultReq/raiseRequestWithAppt";
    //    public static String getHolidayList = testurl + "econsultation/holidaylist?hospCode=";
    public static String getHolidayList = testurl + "webServices/procGbltHolidayList?pModeval=1&pHospcode=";

    public static String getUpdateDoctorMessage = testurl + "econsultation/setMessage?";
    public static String viewRequestListByEmployeeCode = testurl + "eConsultReq/getRequestByDocEmpNo?docEmpNo=";
    public static String viewRequestListByPatMobNo = testurl + "eConsultReq/getRequestPatMobNo/?patMobNo=";
    public static String updateRequestStatus = testurl + "eConsultReq/updateRequestStatus?hospCode=";

    public static String getDocumentList = testurl + "econsultation/getImageDataForEconsult?modeval=2&pRequestid=";
    public static String downloadDocument = testurl + "econsultation/getDocument?fileName=";

    public static String zoneListUrl = testurl + "railtelService/getZoneList";
    public static String divisionListUrl = testurl + "railtelService/getDivisionList";
    public static String divisionAndZoneWiseHospitalList = testurl + "railtelService/getHospitalList";


    //feedback
    public static String giveFeedbackUrl = testurl + "feedback/patfeedback";
    public static String viewFeedbackUrl = testurl + "feedback/ratingfeedback?hospCode=" + hospId + "&ratingID=";
    //appointments
    //public static String appointmentDepartments = testurl + "AppointmentService/getDeptUnitList?";
    public static String appointmentDepartments = testurl + "AppointmentService/getDeptUnitListGeneric?";
    //public static String shiftNameurl = testurl + "AppointmentService/getSpecialClinicSlot?";
    public static String shiftNameurl = testurl + "AppointmentService/getAppointmentSlotsGeneric?";
    //public static String makeAppointment = testurl + "AppointmentService/bookAppointment";
    public static String makeAppointment = testurl + "genericAppointment/bookAppointment";
    public static String getPreviousAppointmentsByCRNoUrl = testurl + "AppointmentService/getPreviousAppointmentsByCRNo?";
    public static String cancelAppointmentUrl = testurl + "AppointmentService/cancelAppointment";
    public static String rescheduleappointmenturl = testurl + "AppointmentService/rescheduleAppointment";
    public static String registerurl = testurl + "genericAppointment/allotCrNo";

    public static String getUMIDataurl = testurl + "railtelService/getUMIDData?";
    public static String getStateAndDistrictCode = testurl + "generalService/addressList?";
    public static String PrescriptionListUrl = testurl + "railtelService/prescriptionList?";
    public static String reportListUrl = testurl + "railtelService/getReportList?crno=";
    public static String qrStampingUrl = testurl + "railtelUMIDService/callStampingService";
    public static String qmsListUrl = testurl + "genericAppointment/getpatEpisodeDtls/2?";
    public static String saveEhrJsonData = testurl + "AppOpdService/saveEHRData";
    public static String saveEhrJsonDataNew = testurl + "AppOpdService/saveAppEHRData";
    public static String followUpService = testurl + "AppOpdService/followUpDTL";
    public static String getPatientDetailSavePrescription = testurl + "AppOpdService/patientDetail";
    public static String saveEmrJsonData = testurl + "AppOpdService/saveGenralDataFormattedData";
    public static String updateNdhmId = testurl + "railtelService/updateNdhmHealthId";

    public static String emrDesk = ip + "emrdashboard/patientOverviewAction.cnt?hmode=DETAIL&crno=";
    public static String transactionsUrl = testurl + "AppOpdService/combInvestigation?hosp_code=&crno=";
    public static String downloadBillUrl = testurl + "AppOpdService/showReciept?crNo=";
    public static String getInstitutesUrl = testurl + "railtelService/getInstitutes";
    public static String generalInfoResource = ip + "HISServices/jsp/GeneralHealthResource.jsp";
    
    public static String downloadDischargeSlip = ip + "HISClinical/emr/uniPagePrescription.cnt?";


    public static String reimbursementClaimEnquiry = testurl + "railtelService/enquiryReimbursementClaim?";
    public static String downloadCliamPdf = testurl + "railtelService/showClaimForm?";
    public static String lpStatusurl = testurl + "inventoryServices/lpRequestStatus?crno=";


    public static String getPatientDetails = testurl + "webServices/getPatDtlsFromMobile?modeval=1&mobileNo=";
    public static String getLabBased = testurl + "webServices/getOpdApptBasedTests?hrgnumPuk=";

    //phr urls
    public static String graphUrl = testurl + "AppOpdService/getVitalDetails?crno=";
    public static String savPhrUrl = testurl + "AppOpdService/saveVitalDetails";
    public static String phrUploadDocs = testurl + "AppOpdService/uploadDocument";
    public static String UploadDocs = testurl + "webServices/uploadDocument";
    public static String ViewDocs = testurl2 + "ehr/get/patient/document/all?crNo=";
    public static String investigationTracker = testurl + "investigationTracker/getInvDetails?hospCode=";
    public static String getSlotsDateServer = testurl + "genericAppointment/getTotalAvailableSlots?pMode=1&pHospCode=";
    public static String getFamilyQrPdf = testurl + "webServices/getFamilyQrPdf?mobileNo=";
    public static String getDrugRx = testurl + "inventoryServices/getEhrjsonPatMedication?pCrno=";

    public static String announcement = testurl + "webServices/getAnnouncementListing?modeval=1&hospCode=";
    public static String FeedbackUrl = testurl + "railtelService/submitFeedback";

    //public static String getPastWebPrescription = ip+"HISDRDESK/services/restful/patdata/digi?hosp_code="+hospId;
    //public static String registerurl=testurl+"genericAppointment/allotCrNo";
    //public static String updateDoctorMessage = testurl + "eConsultReq/setDoctorMessage?requestID=";
    //public static String savePrescriptionUrl = testurl + "eConsultReq/savePatientPrescription";
    //public static String appointmentDepartments = testurl + "AppointmentService/getSpecialClinicsList?hospCode=" + hospId; old
    //public static String PatientDetailsurl = testurl + "AppointmentService/getPatientDtl?hospCode=" + hospId + "&patCRNo=";
    //public static String getPreviousAppointmentsByApptNoUrl = testurl + "AppointmentService/getPreviousAppointmentsByApptNo?hospCode=" + hospId + "&patAptNo=";
    //public static String labtestTestShiftNameAppointmentUrl = testurl + "AppointmentService/getLabTestSlot?hospCode=" + hospId;

}
