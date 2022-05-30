

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class HilosObjetosTCPJugadorAdivina {
	
public static void main(String[] arg) throws IOException, ClassNotFoundException {
		
		String Host ="localhost";
		int puerto = 6001;
		Socket cliente = new Socket(Host, puerto);
		
		ObjectOutputStream fSalida = new ObjectOutputStream (cliente.getOutputStream());
		//fSalida.flush();
		ObjectInputStream fEntrada = new ObjectInputStream (cliente.getInputStream());
		
		//FLUJO PARA ENTRADA ESTANDAR
		Scanner sc = new Scanner(System.in);
		String cadena = "";
		
		//OBTENER PRIMER OBJETO ENVIADO POR EL SERVIDOR
		HilosObjetosTCPDatos datos =(HilosObjetosTCPDatos) fEntrada.readObject();
		int identificador = datos.getIdentificador();
		System.out.println("Id jugador: "+identificador);
		System.out.println(datos.getCadena());
		
		if(!datos.getJuega()) cadena = "*";
		int i=0;
		while(datos.getJuega() && !cadena.trim().equals("*")) {
			i++;
			datos.setIntentos(i);
			System.out.println("intento numero : "+datos.getIntentos());
			cadena = sc.nextLine();
			HilosObjetosTCPDatos d = new HilosObjetosTCPDatos();
			
			//if (!validarCadena(cadena)) continue; //comprobar si es un int
			d.setCadena(cadena);
			
			//ENVIAR DATOS AL SERVIDOR, el número leído por teclado
			fSalida.reset();
			fSalida.writeObject(d);
			
			//RECIBIR DATOS DEL SERVIDOR
			datos = (HilosObjetosTCPDatos) fEntrada.readObject();
			System.out.println(datos.cadena);
			if(!datos.cadena.equals("")){
				i--;
				datos.setIntentos(i);
				System.out.println("entro");
			}
			System.out.println("\t"+datos.getCadena());
			System.out.println("intento numero antes de comparar fin : "+datos.getIntentos());

			if (datos.getIntentos()>=5) {
				System.out.println("\t<<JUEGO FINALIZADO, NO HAY MÁS INTENTOS>>");
				cadena="*";
			}
			if (datos.getGana()) {
				System.out.println("<<HAS GANADO!! EL JUEGO HA TERMINADO>>");
				cadena="*";
			}else {
				if (!(datos.getJuega())) {
					System.out.println("<<EL JUEGO HA TERMINADO, HAN ADIVINADO EL NÚMERO>>");
					cadena="*";
				}
			}
		}//while
		fSalida.close();
		fEntrada.close();
		System.out.println("Fin de proceso...");
		sc.close();
	}//main
	
	private static boolean validarCadena(String cadena) {
		//comprueba si la cadena es numérica
		boolean valor = false;
		try {
			Integer.parseInt(cadena);
			valor = true;
		}catch (NumberFormatException e) {}
		return valor;
	}

}
