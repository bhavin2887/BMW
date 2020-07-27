package com.example.garbagecollection.bmw;

public class RouteWiseHospitalWasteCollection {

    /*
          <WasteScanningAtHospitalDetailID>long</WasteScanningAtHospitalDetailID>
          <DoctorName>string</DoctorName>
          <MembershipNo>string</MembershipNo>
          <HospitalName>string</HospitalName>
          <AreaName>string</AreaName>
          <BagQRCodeDetalID>int</BagQRCodeDetalID>
          <QRCodeContent>string</QRCodeContent>
          <NoOfBags>int</NoOfBags>
          <Weight>decimal</Weight>
          <IsCollected>boolean</IsCollected>
          <ScannedBy>string</ScannedBy>
          <ScannedDateTime>dateTime</ScannedDateTime>*/

    long WasteScanningAtHospitalDetailID;
    String DoctorName;
    String MembershipNo;
    String HospitalName;
    String Area;
    int BagQRCodeDetalID;
    String QRCodeContent;
    int NoOfBags;
    String Weight;
    boolean IsCollected;
    String ScannedBy;
    String ScannedDateTime;

    public long getWasteScanningAtHospitalDetailID() {
        return WasteScanningAtHospitalDetailID;
    }

    public void setWasteScanningAtHospitalDetailID(long wasteScanningAtHospitalDetailID) {
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

    public String getArea() {
        return Area;
    }

    public void setArea(String area) {
        Area = area;
    }

    public int getBagQRCodeDetalID() {
        return BagQRCodeDetalID;
    }

    public void setBagQRCodeDetalID(int bagQRCodeDetalID) {
        BagQRCodeDetalID = bagQRCodeDetalID;
    }

    public String getQRCodeContent() {
        return QRCodeContent;
    }

    public void setQRCodeContent(String QRCodeContent) {
        this.QRCodeContent = QRCodeContent;
    }

    public int getNoOfBags() {
        return NoOfBags;
    }

    public void setNoOfBags(int noOfBags) {
        NoOfBags = noOfBags;
    }

    public String getWeight() {
        return Weight;
    }

    public void setWeight(String weight) {
        Weight = weight;
    }

    public boolean isCollected() {
        return IsCollected;
    }

    public void setCollected(boolean collected) {
        IsCollected = collected;
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
}
