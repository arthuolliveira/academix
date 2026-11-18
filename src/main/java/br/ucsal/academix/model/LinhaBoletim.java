package br.ucsal.academix.model;

public class LinhaBoletim {

    private String disciplina;
    private Double nota1;
    private Double nota2;
    private Double media;
    private long totalAulas;
    private long faltas;
    private Double percentualFaltas;
    private Double percentualPresenca;

    public LinhaBoletim(String disciplina, Double nota1, Double nota2, Double media,
                        long totalAulas, long faltas,
                        Double percentualFaltas, Double percentualPresenca) {
        this.disciplina = disciplina;
        this.nota1 = nota1;
        this.nota2 = nota2;
        this.media = media;
        this.totalAulas = totalAulas;
        this.faltas = faltas;
        this.percentualFaltas = percentualFaltas;
        this.percentualPresenca = percentualPresenca;
    }

    public String getDisciplina() { return disciplina; }
    public Double getNota1() { return nota1; }
    public Double getNota2() { return nota2; }
    public Double getMedia() { return media; }
    public long getTotalAulas() { return totalAulas; }
    public long getFaltas() { return faltas; }
    public Double getPercentualFaltas() { return percentualFaltas; }
    public Double getPercentualPresenca() { return percentualPresenca; }
}
