package br.com.mariorusso.tenancy.domain.valueobject;

public class Telefone {

    private final String phone;

    public Telefone(String telefone) {
        this.phone = setPhone(telefone);
    }

    public String getPhone() {
        return phone;
    }

    private String setPhone(String numero) {
        if (numero == null || !numero.matches("\\d{10,11}")) {
            throw new IllegalArgumentException("Telefone inválido");
        }
        return numero;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((phone == null) ? 0 : phone.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Telefone other = (Telefone) obj;
        if (phone == null) {
            if (other.phone != null)
                return false;
        } else if (!phone.equals(other.phone))
            return false;
        return true;
    }
}
