package com.oktech.boasaude;

import com.oktech.boasaude.dto.AddressCreateRequestDto;
import com.oktech.boasaude.entity.Address;
import com.oktech.boasaude.entity.User;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AddressUnitTest {

    @Test
    void constructorFromDto_setsFields() {
        AddressCreateRequestDto dto = new AddressCreateRequestDto(
            "Rua Exemplo", "Salvador", "BA", "Apto 1", "40110150"
        );

    User user = new User();
    user.setName("João");
    user.setEmail("joao@example.com");

    Address address = new Address(dto, user);

        assertEquals("Rua Exemplo", address.getStreet());
        assertEquals("Salvador", address.getCity());
        assertEquals("BA", address.getState());
        assertEquals("Apto 1", address.getComplement());
        assertEquals("40110150", address.getCep());
    }

    @Test
    void settersAndGetters_work() {
        Address a = new Address();
        a.setStreet("Outra Rua");
        a.setCity("Feira");
        a.setState("BA");
        a.setComplement("Casa");
        a.setCep("44000000");

        assertEquals("Outra Rua", a.getStreet());
        assertEquals("Feira", a.getCity());
        assertEquals("BA", a.getState());
        assertEquals("Casa", a.getComplement());
        assertEquals("44000000", a.getCep());
    }
}
