import java.util.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;

public class MesaTerminal {
	static Funtions h;
	static int mesaid;
	static int eleicaoid;
	
	 
	 MesaTerminal() throws SQLException {
		 h = new Funtions();
		 Escolhe_eleicao_mesa();
		 MenuPrincipal();
	 }
	 
	 public static void main(String[] args) throws SQLException {
	        MesaTerminal start = new MesaTerminal();
	 }
	 
	 public static void MenuPrincipal() {
		 int choice = -1;
		 while(choice!=2) {
			System.out.println("1-Autenticar");
			System.out.println("2-Desligar");
			choice = IntScanner(1,2);
			if(choice==1)
				MenuVoto();
			else if(choice==2) {
				DesligaMesa();
				System.exit(0);
			}
		 }
	 }
	 
	 public static void DesligaMesa() {
		 try {
				h.AlteraEstadoMesa(mesaid, 0);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("Falha na conexao com DB");
				DesligaMesa();
			}
	 }
	 
	 
	 public static void MenuVoto() {
			System.out.println("Bem Vindo a Mesa/Terminal de Voto");
			Scanner sc = new Scanner(System.in);
			ArrayList<Integer> Eleicoes = new ArrayList<Integer>();
			while(true) {
				System.out.print("Introduza o número do Bi");
				double BI = DoubleScanner(9);
				boolean identifica=false;
				boolean naovotou=false;
				try {
					identifica=h.Identificar_VerificaEleicao((int) BI,eleicaoid);
					naovotou=h.Verifica_Votos((int) BI,eleicaoid);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					System.out.println("Falha na connexao com DB");
					MenuPrincipal();
				}
				if(identifica && naovotou) {
					UserInfo u=null;
					System.out.println("Introduza a password");
					String pass = sc.nextLine();
					try {
						u=h.AutenticarEleitor(BI, pass);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						System.out.println("Falha na connexao com DB");
						MenuPrincipal();
					}
					if(u!=null) {
						try {
							h.Votar(u,eleicaoid,mesaid);
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							System.out.println("Falha na connexao com DB");
							MenuPrincipal();
						}
					}
					else {
						System.out.println("Nao corresponde BI a PASSWORD");
						MenuPrincipal();
					}
					
				}
				System.out.println("Nao pode votar nesta eleicao ou ja votou");
			}
	 }
	 
	 

	public static void Escolhe_eleicao_mesa() {
		 ArrayList<Integer> eleicoesdisponiveis= new ArrayList<Integer>();
		 ArrayList<Integer> id_mesas= new ArrayList<Integer>();
			Scanner sc = new Scanner(System.in);
			try {
				eleicoesdisponiveis=h.RetornaEleicoesDecorrer();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("Falha na conexao com DB");
				System.exit(0);
			}
			if(eleicoesdisponiveis.isEmpty()) {
				System.out.println("Nao existem eleicoes a decorrer");
				System.exit(0);
			}
			else {
				System.out.println("Escolha uma das eleicoes da lista anterior usando o numero apresentado");
				int escolha = IntScanner(0,eleicoesdisponiveis.size()-1);
				eleicaoid = eleicoesdisponiveis.get(escolha);
				try {
					id_mesas=h.RetornaMesasVotoID(eleicaoid);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("Falha na conexao com DB");
					System.exit(0);
				}
				if(id_mesas.isEmpty()) {
					System.out.println("Esta eleicao nao tem mesas");
					System.exit(0);;
				}
				System.out.println("Selecione a mesa usando o numero a frente dela");
				int posicao = IntScanner(1,id_mesas.size()-1);
				mesaid=id_mesas.get(posicao);
				try {
					h.AlteraEstadoMesa(mesaid, 1);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("Falha na conexao com DB");
					System.exit(0);
				}
			}
	 }
	 
	 
	 
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//Scanners		
			public static int IntScanner(int lb, int ub) {//Scanner para ints lb= lower bound , ub=upperbound
				int input = -1;
				Scanner sc;
				while(true) {
					try {
						sc = new Scanner(System.in);
						input = sc.nextInt();
						if(input<lb || input> ub)
							System.out.println("Introduza uma das opcoes de "+Integer.toString(lb)+" a "+ Integer.toString(ub));
						else
							return input;
					}catch (InputMismatchException e) { 
					    System.err.println("Introduza um numero");
					}
				}
			}
			
			
			public static double DoubleScanner( int ndigits) {//Scanner para doubles ndigis = n digitos que o numero e suposto ter
				int input = -1;
				Scanner sc;
				while(true) {
					try {
						sc = new Scanner(System.in);
						input = sc.nextInt();
						if(String.valueOf(input).length()!=ndigits)
							System.out.println("Introduza um numero com o maximo de "+Integer.toString(ndigits)+"digitos" );
						else
							return input;
					}catch (InputMismatchException e) { 
					    System.err.println("Introduza um numero");
					}
				}
			}
			
			
			public static java.sql.Date DateScanner() {//Scanner de data
				Date date2=null;
				while(true) {
					try {
						Scanner sc = new Scanner(System.in);
						String date = sc.nextLine();
						SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");//EXEMPLO:16/10/2017 11:00:00 PM
					    date2 =  dateFormat.parse(date);
					    java.sql.Date datefinal = new java.sql.Date(date2.getTime());
					    return datefinal;
					} catch (ParseException e) {
					    // TODO Auto-generated catch block
						System.err.println("Introduza uma data no formato dd/MM/yyyy HH:mm ");
					}
				}
			}
			
			
			public static void FacReader(ArrayList<String> lista) {//Printer para printar todos as Fac
				for(int i=0;i<lista.size();i++)
					System.out.println(i+"Nome"+lista.get(i));
			}
			
			public static void DepReader(CopyOnWriteArrayList<DepFacInfo> lista) {//Printer para printar todos Dep existentes
				System.out.println("Todos os departamentos existentes de momento:");
				int counter=0;
				for(DepFacInfo dep : lista) {
					System.out.println(counter+"Nome:"+dep.Departamento);
					counter++;
				}
			}
	 
}
