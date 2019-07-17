package entities;

/**
 * Classe para receber a tabela Pessoa
 * do banco de dados
 *
 * @author Paulo Sell
 */
public class Pessoa {
    private String loginPessoa;
    private String senhaPessoa;
    private String NomePessoa;

    public Pessoa() {
    }

    ;

    public Pessoa(String NomePessoa, String loginPessoa, String senhaPessoa) {
        this.loginPessoa = loginPessoa;
        this.senhaPessoa = senhaPessoa;
        this.NomePessoa = NomePessoa;
    }

    public String getLoginPessoa() {
        return this.loginPessoa;
    }

    public void setLoginPessoa(String loginPessoa) {
        this.loginPessoa = loginPessoa;
    }

    public String getSenhaPessoa() {
        return this.senhaPessoa;
    }

    public void setSenhaPessoa(String senhaPessoa) {
        this.senhaPessoa = senhaPessoa;
    }

    public String getNomePessoa() {
        return this.NomePessoa;
    }

    public void setNomePessoa(String nomePessoa) {
        NomePessoa = nomePessoa;
    }

    @Override
    public String toString() {
        return String.format("|%-25s|%-25s|%-25s|",
                this.NomePessoa, this.loginPessoa, this.senhaPessoa);
    }
}
