package fit5171.monash.edu;

public class Airplane
{
    private int airplaneID;
    private String airplaneModel;
    private int businessSitsNumber;
    private int economySitsNumber;
    private int crewSitsNumber;

//    Seat row A-J, each row 7 seats
    private static final int SEAT_CAPACITY = 70;

    public Airplane(){}

    public Airplane(int airplaneID, String airplaneModel, int businessSitsNumber, int economySitsNumber, int crewSitsNumber)
    {
        setAirplaneID(airplaneID);
        setAirplaneModel(airplaneModel);
        setBusinessSitsNumber(businessSitsNumber);
        setEconomySitsNumber(economySitsNumber);
        setCrewSitsNumber(crewSitsNumber);
    }

    public int getAirplaneID() {
        return airplaneID;
    }

    public void setAirplaneID(int airplaneID) {
        ValidationUtil.checkPositiveValue(airplaneID, "Airplane ID");
        if (airplaneID > 10000){
            throw new IllegalArgumentException("Airplane ID should between 1 and 10000.");
        }
        this.airplaneID = airplaneID;
    }

    public String getAirplaneModel() {
        return airplaneModel;
    }

    public void setAirplaneModel(String airplaneModel) {
        airplaneModel = ValidationUtil.checkBlankString(airplaneModel, "Airplane model");

        if (airplaneModel.length() < 4 || airplaneModel.length() > 15){
            throw new IllegalArgumentException("Air plane Model should contain 4 to 15 letters");
        }

        if (!airplaneModel.matches("[A-Za-z0-9\\- ]+")){
            throw new IllegalArgumentException("Invalid airplane model.");
        }

        this.airplaneModel = airplaneModel;
    }

    public int getBusinessSitsNumber() {
        return businessSitsNumber;
    }

    public void setBusinessSitsNumber(int businessSitsNumber) {
        ValidationUtil.checkNonNegativeValue(businessSitsNumber, "Business seat number");

        int total = businessSitsNumber + getEconomySitsNumber() + getCrewSitsNumber();
        checkSeatCapacity(total);

        this.businessSitsNumber = businessSitsNumber;
    }

    public int getEconomySitsNumber() {
        return economySitsNumber;
    }

    public void setEconomySitsNumber(int economySitsNumber) {
        ValidationUtil.checkNonNegativeValue(economySitsNumber, "Economy seat number");

        int total = getBusinessSitsNumber() + economySitsNumber + getCrewSitsNumber();
        checkSeatCapacity(total);

        this.economySitsNumber = economySitsNumber;
    }

    public int getCrewSitsNumber() {
        return crewSitsNumber;
    }

    public void setCrewSitsNumber(int crewSitsNumber) {
        ValidationUtil.checkNonNegativeValue(crewSitsNumber, "Crew seat number");

        int total = getBusinessSitsNumber() + getEconomySitsNumber() + crewSitsNumber;
        checkSeatCapacity(total);

        this.crewSitsNumber = crewSitsNumber;
    }

    public String toString()
    {
        return "Airplane{" +
                "model=" + getAirplaneModel() + '\'' +
                ", business sits=" + getBusinessSitsNumber() + '\'' +
                ", economy sits=" + getEconomySitsNumber() + '\'' +
                ", crew sits=" + getCrewSitsNumber() + '\'' +
                '}';
    }

    private void checkSeatCapacity(int seatTotal){
        if (seatTotal > SEAT_CAPACITY){
            throw new IllegalArgumentException("Total seats exceed airplane capacity: " + SEAT_CAPACITY);
        }
    }
}