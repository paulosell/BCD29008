package entities;


/**
 * Classe para receber a tabela Eleicao
 * do banco de dados
 *
 * @author Paulo Sell
 */
public class Eleicao {
    private int idEleicao;
    private String TituloEleicao;
    private String InicioEleicao;
    private String FimEleicao;
    private boolean EstadoEleicao;
    private boolean ApuradaEleicao;

    public Eleicao() {
    }

    ;

    public Eleicao(int idEleicao, String TituloEleicao, String InicioEleicao, String FimEleicao,
                   boolean EstadoEleicao, boolean ApuradaEleicao) {

        this.idEleicao = idEleicao;
        this.TituloEleicao = TituloEleicao;
        this.InicioEleicao = InicioEleicao;
        this.FimEleicao = FimEleicao;
        this.EstadoEleicao = EstadoEleicao;
        this.ApuradaEleicao = ApuradaEleicao;
    }

    public Eleicao(String TituloEleicao, String InicioEleicao, boolean EstadoEleicao) {
        this.TituloEleicao = TituloEleicao;
        this.InicioEleicao = InicioEleicao;
        this.EstadoEleicao = EstadoEleicao;

    }

    public Eleicao(int idEleicao, String TituloEleicao) {
        this.TituloEleicao = TituloEleicao;
        this.idEleicao = idEleicao;

    }

    public int getIdEleicao() {
        return this.idEleicao;
    }

    public void setIdEleicao(int idEleicao) {
        this.idEleicao = idEleicao;
    }

    public String getTituloEleicao() {
        return this.TituloEleicao;
    }

    public void setTituloEleicao(String tituloEleicao) {
        this.TituloEleicao = tituloEleicao;
    }

    public String getInicioEleicao() {
        return this.InicioEleicao;
    }

    public void setInicioEleicao(String inicioEleicao) {
        this.InicioEleicao = inicioEleicao;
    }

    public String getFimEleicao() {
        return this.FimEleicao;
    }

    public void setFimEleicao(String fimEleicao) {
        this.FimEleicao = fimEleicao;
    }

    public boolean isEstadoEleicao() {
        return this.EstadoEleicao;
    }

    public void setEstadoEleicao(boolean estadoEleicao) {
        this.EstadoEleicao = estadoEleicao;
    }

    public boolean isApuradaEleicao() {
        return this.ApuradaEleicao;
    }

    public void setApuradaEleicao(boolean apuradaEleicao) {
        this.ApuradaEleicao = apuradaEleicao;
    }


    @Override
    public String toString() {

        return String.format("|%-5d|%-25s|%-25s|%-25s|%-10b|%-10b|",
                this.idEleicao, this.TituloEleicao, this.InicioEleicao, this.FimEleicao, this.EstadoEleicao, this.ApuradaEleicao);
    }


}
