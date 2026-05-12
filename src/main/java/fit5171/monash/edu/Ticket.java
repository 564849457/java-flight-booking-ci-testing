package fit5171.monash.edu;
public class Ticket
{
    private int ticket_id;
    private int price;
    Flight flight;
    private boolean classVip; //indicates if this is business class ticket or not
    private boolean status; //indicates status of ticket: if it is bought by someone or not
    Passenger passenger;

    public Ticket(int ticket_id,int price, Flight flight, boolean classVip, Passenger passenger)
    {
        this.setTicket_id(ticket_id);
        this.setPassenger(passenger);
        this.setPrice(price);
        this.setFlight(flight);
        this.setClassVip(classVip);
        this.setTicketStatus(false);
    }

    public Ticket() {

    }

    public int getTicket_id() {
        return ticket_id;
    }

    public void setTicket_id(int ticket_id) {
        ValidationUtil.checkNull(ticket_id, "ticket_id");
        ValidationUtil.checkPositiveValue(ticket_id, "ticket_id");
        this.ticket_id = ticket_id;
    }

    public int getPrice() {
        if (passenger == null) {
            return this.price;
        }
        int age = passenger.getAge();
        int afterSalePrice = this.saleByAge(age);

        return this.serviceTax(afterSalePrice);
    }

    public void setPrice(int price)
    {
        ValidationUtil.checkNonNegativeValue(price, "price");
        this.price = price;
    }

    public int saleByAge(int age)
    {
        int afterSalePrice = this.price;

        if(age < 15)
        {
            afterSalePrice = (int) (this.price * 0.5);

        } else if(age>=60){
            afterSalePrice = 0; //100% sale for elder people
        }

        return afterSalePrice;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public boolean getClassVip() {
        return classVip;
    }

    public void setClassVip(boolean classVip) {
        this.classVip = classVip;
    }

    public boolean ticketStatus()
    {
        return status;
    }

    public void setTicketStatus(boolean status)
    {
        this.status = status;
    }

    public int serviceTax(int price){
        return (int) (price * 1.12);
    } //12% service tax

    public Passenger getPassenger() {
        return passenger;
    }

    public void setPassenger(Passenger passenger) {
        this.passenger = passenger;
    }

    public String toString()
    {
        return"Ticket{" +'\n'+
                "Price=" + getPrice() + "KZT, " + '\n' +
                getFlight() +'\n'+ "Vip status=" + getClassVip() + '\n' +
                getPassenger()+'\n'+ "Ticket was purchased=" + ticketStatus() + "\n}";
    }

    public String simpleDisplay(){
        return "Ticket id" + getTicket_id() + "; Price: " + getPrice() + "; VIP status: " + getClassVip() + "; Ticket is purchased: " + ticketStatus();
    }
}
