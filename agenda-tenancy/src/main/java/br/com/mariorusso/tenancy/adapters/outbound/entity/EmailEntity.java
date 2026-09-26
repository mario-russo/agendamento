package br.com.mariorusso.tenancy.adapters.outbound.entity;


import br.com.mariorusso.tenancy.domain.valueobject.Email;
import jakarta.persistence.Embeddable;

@Embeddable
public class EmailEntity {
    String value;
    public EmailEntity(){   }
    public EmailEntity(String email){
        Email domainEmail = new Email(email);
        this.value = domainEmail.getValue();
    }

    public String getValue() {
        return value;
    }

    public EmailEntity setValue(String value) {
        this.value = value;
        return this;
    }
}
