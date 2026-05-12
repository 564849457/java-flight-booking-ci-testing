package fit5171.monash.edu;
public class Person //abstract class Person
{
    private String firstName;
    private String secondName;
    private int age;
    private String gender;

    public Person(){}

    public Person(String firstName, String secondName, int age, String gender){
        setAge(age);
        setGender(gender);
        setFirstName(firstName);
        setSecondName(secondName);
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        if (age < 0 || age > 122){
            throw new IllegalArgumentException("Age must between 0 and 122.");
        }
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        gender = ValidationUtil.checkNull(gender, "Gender");

        boolean flag = false;
        String[] validGenders = {"Woman", "Man", "Non-Binary", "Prefer not to say", "Other"};
        for (String g : validGenders){
            if (gender.equals(g)){
                flag = true;
                break;
            }
        }

        if (!flag){
            throw new IllegalArgumentException("Invalid gender option: " + gender);
        }

        this.gender = gender;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setFirstName(String firstName) {
        verifyName(firstName, "First name");
        this.firstName = firstName;
    }

    public void setSecondName(String secondName) {
        verifyName(secondName, "Second name");
        this.secondName = secondName;
    }

    private void verifyName(String name, String field){
        if (name == null || name.isEmpty()){
            throw new IllegalArgumentException(field + "cannot be blank or null.");
        }

        if (!Character.isUpperCase(name.charAt(0))){
            throw new IllegalArgumentException(field + "must start with upper letter.");
        }

        if (!name.matches("[A-Za-z]+")){
            throw new IllegalArgumentException(field + "must only contain letters.");
        }
    }

    @Override
    public String toString()
    {
        return "Person{" +
                "firstName='" + firstName + '\'' +
                ", secondName='" + secondName + '\'' +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                '}';
    }
}
