package fit5171.monash.edu;

public class Passenger extends Person
{
    private String email;
    private String phoneNumber;
    private String cardNumber;
    private int securityCode;
    private String passport;

    private static final String INTERNATIONAL_PASSPORT_PATTERN = "[A-Z]{2}\\d{7}";
    private static final String NUMERIC_PASSPORT_PATTERN = "\\d{9}";

    public Passenger(){}

    public Passenger(String firstName, String secondName, int age, String gender,String email, String phoneNumber, String passport, String cardNumber,int securityCode)
    {
        super(firstName, secondName, age, gender);
        setSecurityCode(securityCode);
        setCardNumber(cardNumber);
        setPassport(passport);
        setEmail(email);
        setPhoneNumber(phoneNumber);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        email = ValidationUtil.checkBlankString(email, "Email");

        if (!email.matches("^[A-Za-z0-9]+@[A-Za-z0-9]+\\.[A-Za-z]{2,}$")){
            throw new IllegalArgumentException("Invalid email format");
        }
        this.email = email;
    }

    public String getPassport() {
        return passport;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public int getSecurityCode() {
        return securityCode;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        cardNumber = ValidationUtil.checkBlankString(cardNumber, "Card number");
        cardNumber = cardNumber.replace(" ", "");

        if (!cardNumber.matches("\\d+")){
            throw new IllegalArgumentException("Card number must be digits.");
        }

        if (cardNumber.length() != 16){
            throw new IllegalArgumentException("Card number must be 16 digits.");
        }

        this.cardNumber = cardNumber;
    }

    public void setSecurityCode(int securityCode) {
        ValidationUtil.checkPositiveValue(securityCode, "Security code");
        if (securityCode > 999999999){
            throw new IllegalArgumentException("Security code must not exceed 999999999.");
        }

        this.securityCode = securityCode;
    }


    public void setPassport(String passport) {
        passport = ValidationUtil.checkBlankString(passport, "Passport");

        if (passport.matches(INTERNATIONAL_PASSPORT_PATTERN)
                || passport.matches(NUMERIC_PASSPORT_PATTERN)) {
            this.passport = passport;
            return;
        }

        throw new IllegalArgumentException("Invalid passport format.");
    }

    public void setPhoneNumber(String phoneNumber) {
        phoneNumber = ValidationUtil.checkBlankString(phoneNumber, "Phone number");
        phoneNumber = phoneNumber.replace(" ", "");

//        check only digits and optional leading +
        if (!phoneNumber.matches("^\\+?\\d+$")) {
            throw new IllegalArgumentException("Invalid phone number format.");
        }

        // Australia local format: 04XXXXXXXX
        if (phoneNumber.startsWith("04")) {
            if (phoneNumber.length() != 10) {
                throw new IllegalArgumentException("Australian local number must be 10 digits (04XXXXXXXX).");
            }
            this.phoneNumber = "+61" + phoneNumber.substring(1);
            return;
        }

        // Australia international: +614XXXXXXXX
        if (phoneNumber.startsWith("+614")) {
            if (phoneNumber.length() != 12) {
                throw new IllegalArgumentException("Australian international number must be 12 characters (+614XXXXXXXX).");
            }
            this.phoneNumber = phoneNumber;
            return;
        }

        // Thailand international: +66XXXXXXXXX
        if (phoneNumber.startsWith("+66")) {
            if (phoneNumber.length() != 12) {
                throw new IllegalArgumentException("Thailand number must be 12 characters (+66XXXXXXXXX).");
            }
            this.phoneNumber = phoneNumber;
            return;
        }

        // Taiwan international: +8869XXXXXXXX
        if (phoneNumber.startsWith("+8869")) {
            if (phoneNumber.length() != 13) {
                throw new IllegalArgumentException("Taiwan number must be 13 characters (+8869XXXXXXXX).");
            }
            this.phoneNumber = phoneNumber;
            return;
        }

        throw new IllegalArgumentException("Invalid phone number format.");
    }

    @Override
    public String toString()
    {
        return "Passenger{" + " Full name= "+ super.getFirstName()+" "+super.getSecondName()+
                " ,email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", passport='" + passport +
                '}';
    }
}
