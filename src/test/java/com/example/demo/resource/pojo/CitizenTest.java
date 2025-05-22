package com.example.demo.resource.pojo;

import com.example.demo.resource.model.PersonBinding;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Collections;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

public class CitizenTest {

    private Citizen citizen1;
    private Citizen citizen2;
    private Citizen.Phone phone1;
    private Citizen.Phone phone2;

    @BeforeEach
    void setUp() {
        citizen1 = new Citizen();
        citizen2 = new Citizen();

        phone1 = new Citizen.Phone();
        phone1.setNumber("123-456-7890");
        phone1.setType(PersonBinding.Person.PhoneType.MOBILE);

        phone2 = new Citizen.Phone();
        phone2.setNumber("123-456-7890");
        phone2.setType(PersonBinding.Person.PhoneType.MOBILE);
    }

    private void configureCitizen(Citizen citizen, String name, int id, String email, List<Citizen.Phone> phones) {
        citizen.setName(name);
        citizen.setId(id);
        citizen.setEmail(email);
        citizen.setPhones(phones);
    }

    // Tests for Citizen class
    @Test
    void testCitizenEquals_Symmetric() {
        configureCitizen(citizen1, "John Doe", 1, "john.doe@example.com", Collections.singletonList(phone1));
        configureCitizen(citizen2, "John Doe", 1, "john.doe@example.com", Collections.singletonList(phone2)); // phone2 is equal to phone1

        assertThat(citizen1).isEqualTo(citizen2);
        assertThat(citizen2).isEqualTo(citizen1);
    }

    @Test
    void testCitizenHashCode_Consistent() {
        configureCitizen(citizen1, "John Doe", 1, "john.doe@example.com", Collections.singletonList(phone1));
        configureCitizen(citizen2, "John Doe", 1, "john.doe@example.com", Collections.singletonList(phone2));

        assertThat(citizen1.hashCode()).isEqualTo(citizen2.hashCode());
    }

    @Test
    void testCitizenNotEquals_DifferentName() {
        configureCitizen(citizen1, "John Doe", 1, "john.doe@example.com", Collections.singletonList(phone1));
        configureCitizen(citizen2, "Jane Doe", 1, "john.doe@example.com", Collections.singletonList(phone1));

        assertThat(citizen1).isNotEqualTo(citizen2);
    }

    @Test
    void testCitizenNotEquals_DifferentId() {
        configureCitizen(citizen1, "John Doe", 1, "john.doe@example.com", Collections.singletonList(phone1));
        configureCitizen(citizen2, "John Doe", 2, "john.doe@example.com", Collections.singletonList(phone1));

        assertThat(citizen1).isNotEqualTo(citizen2);
    }
    
    @Test
    void testCitizenNotEquals_DifferentEmail() {
        configureCitizen(citizen1, "John Doe", 1, "john.doe@example.com", Collections.singletonList(phone1));
        configureCitizen(citizen2, "John Doe", 1, "jane.doe@example.com", Collections.singletonList(phone1));

        assertThat(citizen1).isNotEqualTo(citizen2);
    }

    @Test
    void testCitizenNotEquals_DifferentPhones() {
        configureCitizen(citizen1, "John Doe", 1, "john.doe@example.com", Collections.singletonList(phone1));
        Citizen.Phone differentPhone = new Citizen.Phone();
        differentPhone.setNumber("987-654-3210");
        differentPhone.setType(PersonBinding.Person.PhoneType.HOME);
        configureCitizen(citizen2, "John Doe", 1, "john.doe@example.com", Collections.singletonList(differentPhone));

        assertThat(citizen1).isNotEqualTo(citizen2);
    }
    
    @Test
    void testCitizenEquals_NullFields() {
        // Both null
        configureCitizen(citizen1, null, 1, null, null);
        configureCitizen(citizen2, null, 1, null, null);
        assertThat(citizen1).isEqualTo(citizen2);

        // One null, one not
        configureCitizen(citizen1, "John Doe", 1, "john.doe@example.com", Collections.singletonList(phone1));
        configureCitizen(citizen2, null, 1, null, null);
        assertThat(citizen1).isNotEqualTo(citizen2);
    }


    @Test
    void testCitizenGettersAndSetters() {
        citizen1.setName("Test Name");
        assertThat(citizen1.getName()).isEqualTo("Test Name");

        citizen1.setId(100);
        assertThat(citizen1.getId()).isEqualTo(100);

        citizen1.setEmail("test@example.com");
        assertThat(citizen1.getEmail()).isEqualTo("test@example.com");
        
        List<Citizen.Phone> phones = Collections.singletonList(phone1);
        citizen1.setPhones(phones);
        assertThat(citizen1.getPhones()).isEqualTo(phones);
    }

    @Test
    void testCitizenToString() {
        configureCitizen(citizen1, "John Doe", 1, "john.doe@example.com", Collections.singletonList(phone1));
        String citizenString = citizen1.toString();
        assertThat(citizenString).contains("name='John Doe'");
        assertThat(citizenString).contains("id=1");
        assertThat(citizenString).contains("email='john.doe@example.com'");
        assertThat(citizenString).contains("phones="); // Content of phones list might be complex to assert precisely without more specific toString in Phone
    }

    // Tests for Citizen.Phone class
    @Test
    void testPhoneEquals_Symmetric() {
        phone1.setNumber("555-5555");
        phone1.setType(PersonBinding.Person.PhoneType.WORK);
        phone2.setNumber("555-5555");
        phone2.setType(PersonBinding.Person.PhoneType.WORK);

        assertThat(phone1).isEqualTo(phone2);
        assertThat(phone2).isEqualTo(phone1);
    }

    @Test
    void testPhoneHashCode_Consistent() {
        phone1.setNumber("555-5555");
        phone1.setType(PersonBinding.Person.PhoneType.WORK);
        phone2.setNumber("555-5555");
        phone2.setType(PersonBinding.Person.PhoneType.WORK);
        
        assertThat(phone1.hashCode()).isEqualTo(phone2.hashCode());
    }

    @Test
    void testPhoneNotEquals_DifferentNumber() {
        phone1.setNumber("555-5555");
        phone1.setType(PersonBinding.Person.PhoneType.WORK);
        phone2.setNumber("555-0000"); // Different number
        phone2.setType(PersonBinding.Person.PhoneType.WORK);

        assertThat(phone1).isNotEqualTo(phone2);
    }

    @Test
    void testPhoneNotEquals_DifferentType() {
        phone1.setNumber("555-5555");
        phone1.setType(PersonBinding.Person.PhoneType.WORK);
        phone2.setNumber("555-5555"); 
        phone2.setType(PersonBinding.Person.PhoneType.HOME); // Different type

        assertThat(phone1).isNotEqualTo(phone2);
    }
    
    @Test
    void testPhoneGettersAndSetters() {
        phone1.setNumber("111-2222");
        assertThat(phone1.getNumber()).isEqualTo("111-2222");

        phone1.setType(PersonBinding.Person.PhoneType.WORK); // Changed from OTHER to WORK
        assertThat(phone1.getType()).isEqualTo(PersonBinding.Person.PhoneType.WORK);
    }
}
