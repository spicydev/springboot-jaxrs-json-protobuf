package com.example.demo.resource.pojo;


import com.example.demo.resource.model.PersonBinding;

import java.util.List;
import java.util.Objects;

public class Citizen {

    private String name;
    private int id;
    private String email;
    private List<Phone> phones;

    public static class Phone {
        private PersonBinding.Person.PhoneType type;
        private String number;

        public PersonBinding.Person.PhoneType getType() {
            return type;
        }

        public void setType(PersonBinding.Person.PhoneType type) {
            this.type = type;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Phone phone = (Phone) o;
            return type == phone.type && Objects.equals(number, phone.number);
        }

        @Override
        public int hashCode() {
            return Objects.hash(type, number);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Phone> getPhones() {
        return phones;
    }

    public void setPhones(List<Phone> phones) {
        this.phones = phones;
    }

    @Override
    public String toString() {
        return "Citizen{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", email='" + email + '\'' +
                ", phones=" + phones +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Citizen citizen = (Citizen) o;
        return id == citizen.id && Objects.equals(name, citizen.name) && Objects.equals(email, citizen.email) && Objects.equals(phones, citizen.phones);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, id, email, phones);
    }
}
