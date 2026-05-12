package fit5171.monash.edu;

import java.sql.Timestamp;

public class Flight {
    private int flightID;
    private String departTo;
    private String departFrom;
    private String code;
    private String company;
    private Timestamp dateFrom;
    private Timestamp dateTo;
    Airplane airplane;
    
    public Flight(){}

    public Flight(int flight_id, String departTo, String departFrom, String code, String company,
                  Timestamp dateFrom, Timestamp dateTo, Airplane airplane) {
        setFlightID(flight_id);
        setDepartTo(departTo);
        setDepartFrom(departFrom);
        setCode(code);
        setCompany(company);
        setDateFrom(dateFrom);
        setDateTo(dateTo);
        setAirplane(airplane);
    }

    public int getFlightID() {
        return flightID;
    }

    public void setFlightID(int flightId) {
        this.flightID = ValidationUtil.checkPositiveValue(flightId, "FlightID");
    }

    public String getDepartTo() {
        return departTo;
    }

    public void setDepartTo(String departTo) {
        this.departTo = ValidationUtil.checkBlankString(departTo, "DepartTo");
    }

    public String getDepartFrom() {
        return departFrom;
    }

    public void setDepartFrom(String departFrom) {
        this.departFrom = ValidationUtil.checkBlankString(departFrom, "DepartFrom");
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = ValidationUtil.checkBlankString(code, "Flight Code");
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = ValidationUtil.checkBlankString(company, "Company");
    }

    public Timestamp getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Timestamp dateFrom) {
        ValidationUtil.checkNull(dateFrom, "dateFrom");
        if(this.dateTo != null) ValidationUtil.checkDateOrder(dateFrom, this.dateTo);
        this.dateFrom = dateFrom;
    }

    public Timestamp getDateTo() {
        return dateTo;
    }

    public void setDateTo(Timestamp dateTo) {
        ValidationUtil.checkNull(dateTo, "dateTo");
        ValidationUtil.checkDateOrder(this.dateFrom, dateTo);
        this.dateTo = dateTo;
    }

    public void setAirplane(Airplane airplane) {
        this.airplane = ValidationUtil.checkNull(airplane, "Airplane");
    }

    public Airplane getAirplane() {
        return airplane;
    }

    public String toString()
    {
            return "Flight{" + airplane.toString() +
                    ", date to=" +  Util.dateToString(getDateTo()) + Util.timeToString(getDateTo()) + ", " + '\'' +
                    ", date from='" + Util.dateToString(getDateFrom()) + Util.timeToString(getDateFrom()) + '\'' +
                    ", depart from='" + getDepartFrom() + '\'' +
                    ", depart to='" + getDepartTo() + '\'' +
                    ", code=" + getCode() + '\'' +
                    ", company=" + getCompany() + '\'' +
                    '}';
    }
}
