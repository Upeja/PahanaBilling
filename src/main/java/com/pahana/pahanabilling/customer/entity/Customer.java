package com.pahana.pahanabilling.customer.entity;

public class Customer {
    private String accountNumber;
    private String name;
    private String address;
    private String phone;
    private int unitsConsumed;

    public Customer() {}

    public Customer(String accountNumber, String name, String address, String phone, int unitsConsumed) {
        this.accountNumber = accountNumber;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.unitsConsumed = unitsConsumed;
    }

    // Getters and Setters
    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public int getUnitsConsumed() { return unitsConsumed; }
    public void setUnitsConsumed(int unitsConsumed) { this.unitsConsumed = unitsConsumed; }

    // Validation
    public boolean isValid() {
        return accountNumber != null && !accountNumber.isEmpty()
                && name != null && !name.isEmpty()
                && address != null && !address.isEmpty()
                && phone != null && !phone.isEmpty()
                && unitsConsumed >= 0;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "accountNumber='" + accountNumber + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", unitsConsumed=" + unitsConsumed +
                '}';
    }
}
