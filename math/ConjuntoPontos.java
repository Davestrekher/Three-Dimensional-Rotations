package math;

import java.util.ArrayList;

public class ConjuntoPontos {
	private ArrayList<Ponto3D> listaPontos;
	private ArrayList<Ponto3D> listaPontosInicial;
	private Ponto3D vetorInicial;

	public ConjuntoPontos(ArrayList<Ponto3D> listaPontos) {
		this.listaPontos = listaPontos;
		this.listaPontosInicial = listaPontos;
	}

	public ArrayList<Ponto3D> getPonto() {
		return listaPontos;
	}

	public void setPonto(ArrayList<Ponto3D> ponto) {
		this.listaPontos = ponto;
	}

	public ArrayList<Ponto3D> getPontoInicial() {
		return listaPontosInicial;
	}

	public void setVetorInicial(Ponto3D vetor) {
		vetorInicial = vetor;
	}

	public Ponto3D getVetorInicial() {
		return vetorInicial;
	}

}
