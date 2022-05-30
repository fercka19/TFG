

public class HilosObjetosTCPObjetoCompartido {
	
	private String numero; //numero a adivinar
	private boolean acabo; //true cuando se haya terminado el juego
	private int ganador; //jugador ganador
	
	public HilosObjetosTCPObjetoCompartido(String numero) {
		this.numero = numero; //numero a adivinar
		this.acabo = false;
	}
	
	public boolean SeAcabo () {return acabo;}
	
	public int getGanador() {return ganador;}
	
	public synchronized String nuevaJugada(int jugador, String suNumero) {
		String cad = "";
		if (!SeAcabo()) {
			if (numero.contains(suNumero)) {//grande
				cad = suNumero;

			}
			if (suNumero == numero) {//se ha acertado
				cad = "Jugador "+jugador+"gana, adivinó el número: "+numero;
				acabo = true;
				ganador = jugador;
			}
			
		}else {
			cad = "Jugador "+ganador+" adivinó el número: "+numero;
		}
		return cad;
	}

}
