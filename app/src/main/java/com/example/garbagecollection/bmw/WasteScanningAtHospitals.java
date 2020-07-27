package com.example.garbagecollection.bmw;

public class WasteScanningAtHospitals {


        /*parameters3.Add(new SqlParameter("@DoctorID", scanningData[i].DoctorID));
        parameters3.Add(new SqlParameter("@BagQRCodeDetalID", scanningData[i].BagQRCodeDetalID));
        parameters3.Add(new SqlParameter("@QRCodeContent", scanningData[i].QRCodeContent));
        parameters3.Add(new SqlParameter("@NoOfBags", scanningData[i].NoOfBags));
        parameters3.Add(new SqlParameter("@Weight", scanningData[i].Weight));
        parameters3.Add(new SqlParameter("@ScannedBy", scanningData[i].ScannedBy));
        parameters3.Add(new SqlParameter("@ScannedDateTime", scanningData[i].ScannedDateTime));
        parameters3.Add(new SqlParameter("@IsCollected", scanningData[i].IsCollected));*/

        String WasteScanningAtHospitalDetailID;
        String DoctorName;
        String MembershipNo;
        String HospitalName;
        String AreaName;

        String Route;
        String ImageTitle;
        String DoctorID;
        String BagQRCodeDetalID;
        String QRCodeContent;
        String NoofBags;
        String Weight;
        String ScannedBy;
        String ScannedDateTime;
        String IsCollected;

    public String getImageTitle() {
        return ImageTitle;
    }

    public void setImageTitle(String imageTitle) {
        ImageTitle = imageTitle;
    }

    public String getRoute() {
        return Route;
    }

    public void setRoute(String route) {
        Route = route;
    }

    public String getWasteScanningAtHospitalDetailID() {
        return WasteScanningAtHospitalDetailID;
    }

    public void setWasteScanningAtHospitalDetailID(String wasteScanningAtHospitalDetailID) {
        WasteScanningAtHospitalDetailID = wasteScanningAtHospitalDetailID;
    }

    public String getDoctorName() {
        return DoctorName;
    }

    public void setDoctorName(String doctorName) {
        DoctorName = doctorName;
    }

    public String getMembershipNo() {
        return MembershipNo;
    }

    public void setMembershipNo(String membershipNo) {
        MembershipNo = membershipNo;
    }

    public String getHospitalName() {
        return HospitalName;
    }

    public void setHospitalName(String hospitalName) {
        HospitalName = hospitalName;
    }

    public String getAreaName() {
        return AreaName;
    }

    public void setAreaName(String areaName) {
        AreaName = areaName;
    }

    public String getDoctorID() {
        return DoctorID;
    }

    public void setDoctorID(String doctorID) {
        DoctorID = doctorID;
    }

    public String getBagQRCodeDetalID() {
        return BagQRCodeDetalID;
    }

    public void setBagQRCodeDetalID(String bagQRCodeDetalID) {
        BagQRCodeDetalID = bagQRCodeDetalID;
    }

    public String getQRCodeContent() {
        return QRCodeContent;
    }

    public void setQRCodeContent(String QRCodeContent) {
        this.QRCodeContent = QRCodeContent;
    }

    public String getNoofBags() {
        return NoofBags;
    }

    public void setNoofBags(String noofBags) {
        NoofBags = noofBags;
    }

    public String getWeight() {
        return Weight;
    }

    public void setWeight(String weight) {
        Weight = weight;
    }

    public String getScannedBy() {
        return ScannedBy;
    }

    public void setScannedBy(String scannedBy) {
        ScannedBy = scannedBy;
    }

    public String getScannedDateTime() {
        return ScannedDateTime;
    }

    public void setScannedDateTime(String scannedDateTime) {
        ScannedDateTime = scannedDateTime;
    }

    public String getIsCollected() {
        return IsCollected;
    }

    public void setIsCollected(String isCollected) {
        IsCollected = isCollected;
    }
}
