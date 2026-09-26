package br.com.mariorusso.share;

public class Endereco {
    private String endereco;
    private String municipio;
    private String estado;

    public Endereco(String endereco, String municipio, String estado) {
        validaEndereco(endereco, municipio, estado);
        this.endereco = endereco.trim();
        this.municipio = municipio.trim();
        this.estado = estado.trim();
    }

    private void validaEndereco(String endereco, String municipio, String estado) {

        if (endereco == null || endereco.isBlank())
            throw new IllegalArgumentException("Endereço não pode ser vázio");

        if (municipio == null || municipio.isBlank())
            throw new IllegalArgumentException("Municipio não pode ser vázio");

        if (estado == null || estado.isBlank())
            throw new IllegalArgumentException("Estado não pode ser vázio");

    }

    public String getEndereco() {
        return endereco;
    }

    public String getMunicipio() {
        return municipio;
    }

    public String getEstado() {
        return estado;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((endereco == null) ? 0 : endereco.hashCode());
        result = prime * result + ((municipio == null) ? 0 : municipio.hashCode());
        result = prime * result + ((estado == null) ? 0 : estado.hashCode());
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
        Endereco other = (Endereco) obj;
        if (endereco == null) {
            if (other.endereco != null)
                return false;
        } else if (!endereco.equals(other.endereco))
            return false;
        if (municipio == null) {
            if (other.municipio != null)
                return false;
        } else if (!municipio.equals(other.municipio))
            return false;
        if (estado == null) {
            if (other.estado != null)
                return false;
        } else if (!estado.equals(other.estado))
            return false;
        return true;
    }
}
