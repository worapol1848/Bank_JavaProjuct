abstract class Person {
    protected String name;
    protected String birthdate;
    protected String gender;
    protected String address;

    public Person(String name, String birthdate, String gender, String address) {
        this.name = name;
        this.birthdate = birthdate;
        this.gender = gender;
        this.address = address;
    }

    public String getName() { return name; }
    public String getBirthdate() { return birthdate; }
    public String getGender() { return gender; }
    public String getAddress() { return address; }
}

interface Verifiable {
    boolean verifyPin(String inputPin);
}

class User extends Person implements Verifiable {
    private String pin;
    private String district;
    private String province;
    private String postalCode;
    private String cardNumber;
    private Account account;

    public User(String name, String pin, String birthdate, String gender,
                String address, String district, String province,
                String postalCode, String cardNumber, Account account) {
        super(name, birthdate, gender, address);
        this.pin = pin;
        this.district = district;
        this.province = province;
        this.postalCode = postalCode;
        this.cardNumber = cardNumber;
        this.account = account;
    }

    @Override
    public boolean verifyPin(String inputPin) {
        return this.pin.equals(inputPin);
    }

    public String getDistrict() { return district; }
    public String getProvince() { return province; }
    public String getPostalCode() { return postalCode; }
    public String getCardNumber() { return cardNumber; }
    public Account getAccount() { return account; }

    public String getUserInfo() {
        return String.format("Name: %s\nBirthdate: %s\nGender: %s\nAddress: %s\n" +
                        "District: %s\nProvince: %s\nPostal Code: %s\n" +
                        "Card Number: %s",
                name, birthdate, gender, address, district,
                province, postalCode, cardNumber);
    }
}