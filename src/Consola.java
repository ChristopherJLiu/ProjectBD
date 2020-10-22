
import javax.sound.midi.SysexMessage;
import java.util.Date;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;




public class Consola  {
	 static Funtions h;
	 
	 Consola() throws SQLException {
		 h = new Funtions();
		 MenuPrincipal();

	 }
	 
	 public static void main(String[] args) throws SQLException {
	        Consola start = new Consola();
	 }
	 
	 
	//Menu principal
		public static void MenuPrincipal() {
			System.out.println("Bem Vindo a consola de Admin");
			int choice=-1;
			while(choice!=11) {
				System.out.println("Escolha uma das opcoes usando um inteiro");
				System.out.println("1-Registar pessoa ou alterar dados pessoais");
				System.out.println("2-Gerir departamento ou faculdade");
				System.out.println("3-Criar eleicao");
				System.out.println("4-Gerir lista de candidatos a uma eleicao");
				System.out.println("5-Gerir mesas de voto");
				System.out.println("6-Alterar propriedades de uma eleicao");
				System.out.println("7-Saber em que lugar votou os eleitores numa eleicao");
				System.out.println("8-Consultar dados de eleicoes passadas");
				System.out.println("9-Voto Antecipado");
				System.out.println("10-Gerir membros de cada mesa de voto");
				System.out.println("11-Desligar Consola");
				choice = IntScanner(1,12);
				 if(choice==1)
					Func1();//Completo
				else if(choice==2)
					Func2();//Completo
				else if(choice==3)
					Func3();//Completo
				else if(choice==4)
					Func4();
				else if(choice==5)
					Func5();//Completo
				else if(choice==6)
					Func6();
				else if(choice==7)
					Func7();
				else if(choice==8)
					Func8();
				else if(choice==9)
					Func9();
				else if(choice==10)
					Func10();
				else if(choice==11) {
				 return;
				}
				else if(choice==12){
					 try {
						 h.FinalizaManualmente(2,328661736);//Estado da eleicao que quer e id da eleicao
					 } catch (SQLException e) {
						 // TODO Auto-generated catch block
						 System.out.println("Falha na connexao com DB");
					 }
					 System.out.println("ola");
					 MenuPrincipal();
				 }
			}
		}
	
		

		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////		
		public static void Func1 () {//Registar Pessoas
			System.out.println("Caso o BI exista os dados serao actualizados caso nao serao registados");
			DepFacInfo dep = null;
			Scanner sc = new Scanner(System.in);
			System.out.print("Introduza o nome da pessoa");
			String nome = sc.nextLine();
			System.out.print("Escolha se a pessoa e um \n1-Aluno\n2-Docente\n3-Funcionario");
			int tipo = IntScanner(1,3);
			System.out.print("Introduza uma password");
			String ps = sc.nextLine();
			System.out.print("Introduza o Contacto Telefonico");
			double CT = DoubleScanner(9);
			System.out.print("Introduza a morada");
			String morada = sc.nextLine();
			System.out.print("Introduza o numero do Bi");
			double BI = DoubleScanner(9);
			System.out.println("Introduza a validade do BI no formato'dd/MM/yyyy HH:mm'\nExemplo 27/12/2030 11:00");
			java.sql.Date BIVal=DateScanner();
			CopyOnWriteArrayList<DepFacInfo>lista=new CopyOnWriteArrayList<DepFacInfo>();
			try {
				lista= h.RetornaTodosDepFac();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println("Falha na connexao com DB");
				return;
			}
			if(lista.size()==0) {
				System.out.println("Tente criar departamentos antes de criar pessoas");
				return;
			}
			System.out.print("Insira o numero a frente do departamento consoante a lista seguinte");
			DepReader(lista);
			int posicaodep=IntScanner(0,lista.size());
			UserInfo novo = new UserInfo(nome,tipo,ps,CT,morada,BI,BIVal,lista.get(posicaodep));
			boolean sucess = false;
			try {
				sucess=h.CriarouUpdateUser(novo);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println("Falha na connexao com DB");
				return;
			}
			if(sucess)
				System.out.println("Utilizador criado");
			else
				System.out.println("Algo falhou tente de novo");
		}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////		
		
		public static void Func2() {//Gerir Departamentos e faculdades
			Scanner sc = new Scanner(System.in);
			System.out.print("Introduza o que pretende fazer\n1-Adicionar Departamento\n2-Alterar nome faculdade\n3-Alterar nome departamento\n4-Remover departamento ou faculdade\n");
			int op = IntScanner(1,4);
			boolean sucess=false;
			if(op==1) {//Criar Faculdade
				System.out.print("Introduza o nome da Faculdade");
				String nomefac = sc.nextLine();
				System.out.println("Indique o numero de departamentos que a faculdade tem ate ao maximo de 10 departamentos");
				int ndep=IntScanner(1,10);
				for(int i=0;i<ndep;i++) {
					System.out.println("Indique o nome do departamento");
					String nomedep=sc.nextLine();
					DepFacInfo dep= new DepFacInfo(nomefac,nomedep);
					try {
						sucess=h.AdicionarDepFac(dep);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						System.out.println("Falha na connexao com DB");
						return;
					}
					if(sucess)
						System.out.println("Departamento criado");
					else
						System.out.println("Departamento ja existe ou ID atribuido ja esta a ser usado tente de novo");
				}
			}
			
			else if(op==2) {
				System.out.print("Introduza o nome da Faculdade");
				String nomefac = sc.nextLine();
				System.out.print("Introduza o novo nome da Faculdade");
				String novonomefac = sc.nextLine();
				try {
					sucess=h.AlterarNomeFaculdade(nomefac,novonomefac);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					System.out.println("Falha na conexao com DB");
					return;
				}
				if(sucess)
					System.out.println("Nome da faculdade alterada");
				else
					System.out.println("Nome em uso ou faculdade nao encontrada");
			}
			
			else if(op==3) {
				System.out.print("Introduza o nome do Departamento");
				String nomedep = sc.nextLine();
				System.out.print("Introduza o novo nome do Departamento");
				String novonomedep = sc.nextLine();
				try {
					sucess=h.AlterarNomeDepartamento(nomedep,novonomedep);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					System.out.println("Falha na conexao com DB");
					return;
				}
				if(sucess)
					System.out.println("Nome do departamento alterado");
				else
					System.out.println("Nome em uso ou nome do departamento nao encontrado");
			}
			else if(op==4) {
				System.out.println("Indique o nome da faculdade ou departamento que prentende remover");
				String nomerem=sc.nextLine();
				try {
					sucess=h.RemoverDepFac(nomerem);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("Falha na conexao com DB");
					return;
				}
				if(sucess)
					System.out.println("Faculdade ou Departamento apagado");
				else
					System.out.println("Nao foi encontrada a Faculdade ou Departamento que pretende remover");
			}	
		}
		
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		public static void Func3() {//Criar Eleicao
			Scanner sc = new Scanner(System.in);
			DepFacInfo dep = null;
			System.out.print("Introduza o tipo de eleicaoo\n1-Nucleo de estudantes\n2-Conselho Geral\n3-Direcao Departamento\n4-Direcao Faculdade");
			int tipo = IntScanner(1,4);
			System.out.println("Introduza a data do inicio da eleicao no formato'dd/MM/yyyy HH:mm'\nExemplo 27/12/2030 11:00");
			java.sql.Date inicio=DateScanner();
			System.out.println("Introduza a data do fim da eleicao no formato'dd/MM/yyyy HH:mm'\nExemplo 27/12/2030 11:00");
			java.sql.Date fim=DateScanner();
			System.out.println("Introduza o titulo da eleicao");
			String tit = sc.nextLine();
			System.out.println("Introduza uma breve descricao");
			String desc = sc.nextLine();
			
			if(tipo==1 || tipo==3) {
				CopyOnWriteArrayList<DepFacInfo>lista=new CopyOnWriteArrayList<DepFacInfo>();
				try {
					lista= h.RetornaTodosDepFac();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					System.out.println("Falha na connexao com DB");
					return;
				}
				if(lista.size()==0) {
					System.out.println("Tente criar departamentos antes de criar eleicoes");
					return;
				}
				System.out.print("Insira o numero a frente do departamento consoante a lista seguinte");
				DepReader(lista);
				int posicaodep=IntScanner(0,lista.size()-1);
				ArrayList<DepFacInfo> deps = new ArrayList<DepFacInfo>();
				deps.add(lista.get(posicaodep));
				ArrayList<Integer> ids_dep = new ArrayList<Integer>();
				ids_dep.add(lista.get(posicaodep).ID);
				EleicaoInfo novo = new EleicaoInfo(tipo,deps,inicio,fim,tit,desc);
				boolean sucess = false;
				try {
					sucess=h.CriarEleicao(novo,ids_dep);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					System.out.println("Falha na connexao com DB");
					return;
				}
				if(sucess)
					System.out.println("Eleicao criada");
				else
					System.out.println("Algo falhou tente de novo");
			}
			
			
			else if (tipo==4) {
				CopyOnWriteArrayList<DepFacInfo>lista=new CopyOnWriteArrayList<DepFacInfo>();
				try {
					lista= h.RetornaTodosDepFac();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					System.out.println("Falha na connexao com DB");
					return;
				}
				if(lista.size()==0) {
					System.out.println("Tente criar faculdades antes de criar eleicoes");
					return;
				}
				System.out.print("Insira o numero a frente da faculdade consoante a lista seguinte");
				ArrayList<String> aux = new ArrayList<String>();
				for(DepFacInfo el : lista)
					if(!aux.contains(el.Faculdade))
						aux.add(el.Faculdade);
				FacReader(aux);
				int posicaofac=IntScanner(0,aux.size()-1);
				ArrayList<DepFacInfo> deps = new ArrayList<DepFacInfo>();
				ArrayList<Integer> ids_dep = new ArrayList<Integer>();
				for(int i=0;i<lista.size();i++) {
					if(lista.get(i).Faculdade.equals(aux.get(posicaofac))) {
						ids_dep.add(lista.get(posicaofac).ID);
						deps.add(lista.get(posicaofac));
					}
				}
				EleicaoInfo novo = new EleicaoInfo(tipo,deps,inicio,fim,tit,desc);
				boolean sucess = false;
				try {
					sucess=h.CriarEleicao(novo,ids_dep);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					System.out.println("Falha na connexao com DB");
					return;
				}
				if(sucess)
					System.out.println("Eleicao criada");
				else
					System.out.println("Algo falhou tente de novo");
			}
			else if(tipo==2) {
				CopyOnWriteArrayList<DepFacInfo>lista=new CopyOnWriteArrayList<DepFacInfo>();
				try {
					lista= h.RetornaTodosDepFac();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					System.out.println("Falha na connexao com DB");
					return;
				}
				if(lista.size()==0) {
					System.out.println("Tente criar departamentos antes de criar eleicoes");
					return;
				}
				ArrayList<DepFacInfo> deps = new ArrayList<DepFacInfo>();
				ArrayList<Integer> ids_dep = new ArrayList<Integer>();
				for(int i=0;i<lista.size();i++) {
					ids_dep.add(lista.get(i).ID);
					deps.add(lista.get(i));
				}
				EleicaoInfo novo = new EleicaoInfo(tipo,deps,inicio,fim,tit,desc);
				boolean sucess = false;
				try {
					sucess=h.CriarEleicao(novo,ids_dep);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					System.out.println("Falha na connexao com DB");
					return;
				}
				if(sucess)
					System.out.println("Eleicao criada");
				else
					System.out.println("Algo falhou tente de novo");
				
			}
		} 
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		public static void Func4() {//Gerir lista de candidatos a uma eleicao
			ArrayList<Integer> eleicoesdisponiveis= new ArrayList<Integer>();
			ArrayList<Integer> depids_eleicao= new ArrayList<Integer>();
			ArrayList<Integer> bis_subaptos= new ArrayList<Integer>();
			ArrayList<Integer> bis_subaptos_tipo= new ArrayList<Integer>();
			ArrayList<Integer> bis_subaptos_dep= new ArrayList<Integer>();
			ArrayList<Integer> bis_aptos= new ArrayList<Integer>();
			int eleicaoid,eleicaotipo;
			Scanner sc = new Scanner(System.in);
			String nomelista;
			int tipolista=0;
			int n_pessoas;
			ArrayList<Integer> pessoas_lista = new ArrayList<Integer>();;
			try {
				eleicoesdisponiveis=h.RetornaEleicoesNaoIniciadas();
			} catch (Exception e) {
				e.printStackTrace();
				// TODO Auto-generated catch block
				System.out.println("Falha na conexao com DB");
				return;
			}
			if(eleicoesdisponiveis.isEmpty()) {
				System.out.println("Nao existem eleicoes adicionar listas candidatas");
				return;
			}
			else {
				System.out.println("Escolha uma das eleicoes da lista anterior usando o numero apresentado");
				int escolha = IntScanner(0, eleicoesdisponiveis.size() - 1);
				eleicaoid = eleicoesdisponiveis.get(escolha);
				try {
					eleicaotipo=h.RetornaTipoEleicao(eleicaoid);
					depids_eleicao=h.RetornaDepsEleicao(eleicaoid);
				} catch (Exception e) {
					e.printStackTrace();
					// TODO Auto-generated catch block
					System.out.println("Falha na conexao com DB");
					return;
				}
				if(eleicaotipo==0){
					System.out.println("A eleicao ja comecou");
					return;
				}
				else if(eleicaotipo==2){
					System.out.println("Introduza o tipo de lista que quer criar 1-Alunos,2-Docentes,3-Funcionarios");
					tipolista=IntScanner(1,3);
					if(tipolista==1){
						try {
							bis_subaptos=h.RetornaTodosUserNaoEmListas();
						} catch (Exception e) {
							e.printStackTrace();
							// TODO Auto-generated catch block
							System.out.println("Falha na conexao com DB");
							return;
						}
						for(int bi:bis_subaptos) {
							try {
								bis_subaptos_tipo.add(h.RetornaTipoUser(bi));
								bis_subaptos_dep.add(h.RetornaUserDep(bi));
							} catch (Exception e) {
								e.printStackTrace();
								// TODO Auto-generated catch block
								System.out.println("Falha na conexao com DB");
								return;
							}
						}
						for(int i=0;i<bis_subaptos.size();i++){
							if(depids_eleicao.contains(bis_subaptos_dep.get(i)) && bis_subaptos_tipo.get(i)==1)
								bis_aptos.add(bis_subaptos.get(i));
						}
						if(bis_aptos.isEmpty()){
							System.out.println("Nao existem pessoas para criar lista");
							return;
						}
						System.out.println("Introduzao nome da lista:");
						nomelista=sc.nextLine();
						System.out.println("Introduza quantas pessoas quer adicionar a lista");
						n_pessoas=IntScanner(1,bis_aptos.size()-1);
						for(int i=0;i<bis_aptos.size();i++) {
							System.out.println(i + "-BI:" + bis_aptos.get(i));
						}
						for(int i=0;i<n_pessoas;i++){
							System.out.println("Introduza o numero a frente do Bi da pessoa que pretende adicionar a lista");
							pessoas_lista.add(bis_aptos.get(IntScanner(0,bis_aptos.size()-1)));
						}
						ListasCandidatas lista = new ListasCandidatas(tipolista,pessoas_lista,nomelista);
						boolean sucess=false;
						try {
							sucess=h.AdicionaLista(lista,eleicaoid);
						} catch (Exception e) {
							e.printStackTrace();
							// TODO Auto-generated catch block
							System.out.println("Falha na conexao com DB Lista nao adicionada");
							return;
						}
						if(sucess)
							System.out.println("Lista adicionada");
						else
							System.out.println("Lista nao adicionada");
					}
					else if (tipolista==2){
						try {
							bis_subaptos=h.RetornaTodosUserNaoEmListas();
						} catch (Exception e) {
							e.printStackTrace();
							// TODO Auto-generated catch block
							System.out.println("Falha na conexao com DB");
							return;
						}
						for(int bi:bis_subaptos) {
							try {
								bis_subaptos_tipo.add(h.RetornaTipoUser(bi));
								bis_subaptos_dep.add(h.RetornaUserDep(bi));
							} catch (Exception e) {
								e.printStackTrace();
								// TODO Auto-generated catch block
								System.out.println("Falha na conexao com DB");
								return;
							}
						}
						for(int i=0;i<bis_subaptos.size();i++){
							if(depids_eleicao.contains(bis_subaptos_dep.get(i)) && bis_subaptos_tipo.get(i)==2)
								bis_aptos.add(bis_subaptos.get(i));
						}
						if(bis_aptos.isEmpty()){
							System.out.println("Nao existem pessoas para criar lista");
							return;
						}
						System.out.println("Introduzao nome da lista:");
						nomelista=sc.nextLine();
						System.out.println("Introduza quantas pessoas quer adicionar a lista");
						n_pessoas=IntScanner(1,bis_aptos.size()-1);
						for(int i=0;i<bis_aptos.size();i++) {
							System.out.println(i + "-BI:" + bis_aptos.get(i));
						}
						for(int i=0;i<n_pessoas;i++){
							System.out.println("Introduza o numero a frente do Bi da pessoa que pretende adicionar a lista");
							pessoas_lista.add(bis_aptos.get(IntScanner(0,bis_aptos.size()-1)));
						}
						ListasCandidatas lista = new ListasCandidatas(tipolista,pessoas_lista,nomelista);
						boolean sucess=false;
						try {
							sucess=h.AdicionaLista(lista,eleicaoid);
						} catch (Exception e) {
							e.printStackTrace();
							// TODO Auto-generated catch block
							System.out.println("Falha na conexao com DB Lista nao adicionada");
							return;
						}
						if(sucess)
							System.out.println("Lista adicionada");
						else
							System.out.println("Lista nao adicionada");
					}
					else if(tipolista==3){
						try {
							bis_subaptos=h.RetornaTodosUserNaoEmListas();
						} catch (Exception e) {
							e.printStackTrace();
							// TODO Auto-generated catch block
							System.out.println("Falha na conexao com DB");
							return;
						}
						for(int bi:bis_subaptos) {
							try {
								bis_subaptos_tipo.add(h.RetornaTipoUser(bi));
								bis_subaptos_dep.add(h.RetornaUserDep(bi));
							} catch (Exception e) {
								e.printStackTrace();
								// TODO Auto-generated catch block
								System.out.println("Falha na conexao com DB");
								return;
							}
						}
						for(int i=0;i<bis_subaptos.size();i++){
							if(depids_eleicao.contains(bis_subaptos_dep.get(i)) && bis_subaptos_tipo.get(i)==3)
								bis_aptos.add(bis_subaptos.get(i));
						}
						if(bis_aptos.isEmpty()){
							System.out.println("Nao existem pessoas para criar lista");
							return;
						}
						System.out.println("Introduzao nome da lista:");
						nomelista=sc.nextLine();
						System.out.println("Introduza quantas pessoas quer adicionar a lista");
						n_pessoas=IntScanner(1,bis_aptos.size()-1);
						for(int i=0;i<bis_aptos.size();i++) {
							System.out.println(i + "-BI:" + bis_aptos.get(i));
						}
						for(int i=0;i<n_pessoas;i++){
							System.out.println("Introduza o numero a frente do Bi da pessoa que pretende adicionar a lista");
							pessoas_lista.add(bis_aptos.get(IntScanner(0,bis_aptos.size()-1)));
						}
						ListasCandidatas lista = new ListasCandidatas(tipolista,pessoas_lista,nomelista);
						boolean sucess=false;
						try {
							sucess=h.AdicionaLista(lista,eleicaoid);
						} catch (Exception e) {
							e.printStackTrace();
							// TODO Auto-generated catch block
							System.out.println("Falha na conexao com DB Lista nao adicionada");
							return;
						}
						if(sucess)
							System.out.println("Lista adicionada");
						else
							System.out.println("Lista nao adicionada");
					}
				}
				else if(eleicaotipo==3 || eleicaotipo==4){
					tipolista=2;
					try {
						bis_subaptos=h.RetornaTodosUserNaoEmListas();
					} catch (Exception e) {
						e.printStackTrace();
						// TODO Auto-generated catch block
						System.out.println("Falha na conexao com DB");
						return;
					}
					for(int bi:bis_subaptos) {
						try {
							bis_subaptos_tipo.add(h.RetornaTipoUser(bi));
							bis_subaptos_dep.add(h.RetornaUserDep(bi));
						} catch (Exception e) {
							e.printStackTrace();
							// TODO Auto-generated catch block
							System.out.println("Falha na conexao com DB");
							return;
						}
					}
					for(int i=0;i<bis_subaptos.size();i++){
						if(depids_eleicao.contains(bis_subaptos_dep.get(i)) && bis_subaptos_tipo.get(i)==2)
							bis_aptos.add(bis_subaptos.get(i));
					}
					if(bis_aptos.isEmpty()){
						System.out.println("Nao existem pessoas para criar lista");
						return;
					}
					System.out.println("Introduzao nome da lista:");
					nomelista=sc.nextLine();
					System.out.println("Introduza quantas pessoas quer adicionar a lista");
					n_pessoas=IntScanner(1,bis_aptos.size()-1);
					for(int i=0;i<bis_aptos.size();i++) {
						System.out.println(i + "-BI:" + bis_aptos.get(i));
					}
					for(int i=0;i<n_pessoas;i++){
						System.out.println("Introduza o numero a frente do Bi da pessoa que pretende adicionar a lista");
						pessoas_lista.add(bis_aptos.get(IntScanner(0,bis_aptos.size()-1)));
					}
					ListasCandidatas lista = new ListasCandidatas(tipolista,pessoas_lista,nomelista);
					boolean sucess=false;
					try {
						sucess=h.AdicionaLista(lista,eleicaoid);
					} catch (Exception e) {
						e.printStackTrace();
						// TODO Auto-generated catch block
						System.out.println("Falha na conexao com DB Lista nao adicionada");
						return;
					}
					if(sucess)
						System.out.println("Lista adicionada");
					else
						System.out.println("Lista nao adicionada");

				}
				else if(eleicaotipo==1){
					tipolista=1;
					try {
						bis_subaptos=h.RetornaTodosUserNaoEmListas();
					} catch (Exception e) {
						e.printStackTrace();
						// TODO Auto-generated catch block
						System.out.println("Falha na conexao com DB");
						return;
					}
					for(int bi:bis_subaptos) {
						try {
							bis_subaptos_tipo.add(h.RetornaTipoUser(bi));
							bis_subaptos_dep.add(h.RetornaUserDep(bi));
						} catch (Exception e) {
							e.printStackTrace();
							// TODO Auto-generated catch block
							System.out.println("Falha na conexao com DB");
							return;
						}
					}
					for(int i=0;i<bis_subaptos.size();i++){
                        System.out.println("oi");
                        System.out.println(depids_eleicao.get(0));
                        System.out.println(bis_subaptos_dep.get(i));
						if(depids_eleicao.contains(bis_subaptos_dep.get(i)) && bis_subaptos_tipo.get(i)==1)
							bis_aptos.add(bis_subaptos.get(i));
					}
					if(bis_aptos.isEmpty()){
						System.out.println("Nao existem pessoas para criar lista");
						return;
					}
					System.out.println("Introduzao nome da lista:");
					nomelista=sc.nextLine();
					System.out.println("Introduza quantas pessoas quer adicionar a lista");
					n_pessoas=IntScanner(1,bis_aptos.size()-1);
					for(int i=0;i<bis_aptos.size();i++) {
						System.out.println(i + "-BI:" + bis_aptos.get(i));
					}
					for(int i=0;i<n_pessoas;i++){
						System.out.println("Introduza o numero a frente do Bi da pessoa que pretende adicionar a lista");
						pessoas_lista.add(bis_aptos.get(IntScanner(0,bis_aptos.size()-1)));
					}
					ListasCandidatas lista = new ListasCandidatas(tipolista,pessoas_lista,nomelista);
					boolean sucess=false;
					try {
						sucess=h.AdicionaLista(lista,eleicaoid);
					} catch (Exception e) {
						e.printStackTrace();
						// TODO Auto-generated catch block
						System.out.println("Falha na conexao com DB Lista nao adicionada");
						return;
					}
					if(sucess)
						System.out.println("Lista adicionada");
					else
						System.out.println("Lista nao adicionada");

				}



			}
		}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
		public static void Func5() {//Gerir Mesas de voto
			System.out.println("Introduza a opcao que pretende:\n1-Adicionar mesa\n2-Remover Mesa");
			int sc = IntScanner(1,2);
			if(sc==1)
				AddMesas();
			else
				RemoveMesas();
			
		}
		
		public static void AddMesas() {//Adicionar Mesas de voto
			ArrayList<Integer> eleicoesdisponiveis= new ArrayList<Integer>();
			Scanner sc = new Scanner(System.in);
			try {
				eleicoesdisponiveis=h.RetornaEleicoesNaoIniciadas();
			} catch (Exception e) {
				e.printStackTrace();
				// TODO Auto-generated catch block
				System.out.println("Falha na conexao com DB");
				return;
			}
			if(eleicoesdisponiveis.isEmpty()) {
				System.out.println("Nao existem eleicoes disponiveis para adicionar mesas de voto");
				return;
			}
			else {
				System.out.println("Escolha uma das eleicoes da lista anteriro usando o numero apresentado");
				int escolha = IntScanner(0,eleicoesdisponiveis.size()-1);
				int eleicaoid= eleicoesdisponiveis.get(escolha);
				CopyOnWriteArrayList<DepFacInfo> listafac = null;
				try {
					listafac = h.RetornaTodosDepFac();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("Falha na conexao com DB");
					return;
				}
				System.out.print("Insira o numero a frente do departamento consoante a lista seguinte");
				DepReader(listafac);
				int posicaodep=IntScanner(0,listafac.size()-1);
				boolean checker=false;
				try {
					checker = h.CheckaExistenciaMesa(eleicaoid, listafac.get(posicaodep).ID);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("Falha na conexao com DB");
					return;
				}
				if(checker) {
					System.out.println("Ja existe uma mesa nesse departamento");
					return;
				}
				System.out.println("Introduza o numero de terminais que a mesa vai ter");
				int n_terminais=IntScanner(1,3);
				Mesa_voto mesa = new Mesa_voto(listafac.get(posicaodep), n_terminais);
				boolean sucess = false;
				try {
					sucess=h.AdicionarMesaVoto(eleicaoid, mesa);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					System.out.println("Falha na connexao com DB");
					return;
				}
				if(sucess)
					System.out.println("Mesa adicionada");
				else
					System.out.println("Algo falhou tente de novo");
			}
		}
		
		public static void RemoveMesas() {//Remover Mesas de voto
			ArrayList<Integer> eleicoesdisponiveis= new ArrayList<Integer>();
			ArrayList<Integer> id_mesas= new ArrayList<Integer>();
			Scanner sc = new Scanner(System.in);
			try {
				eleicoesdisponiveis=h.RetornaEleicoesNaoIniciadas();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("Falha na conexao com DB");
				return;
			}
			if(eleicoesdisponiveis.isEmpty()) {
				System.out.println("Nao existem eleicoes disponiveis para adicionar mesas de voto");
				return;
			}
			else {
				System.out.println("Escolha uma das eleicoes da lista anterior usando o numero apresentado");
				int escolha = IntScanner(0,eleicoesdisponiveis.size()-1);
				int eleicaoid= eleicoesdisponiveis.get(escolha);
				try {
					id_mesas=h.RetornaMesasVotoID(eleicaoid);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("Falha na conexao com DB");
					return;
				}
				if(id_mesas.isEmpty()) {
					System.out.println("Esta eleicao nao tem mesas");
					return;
				}
				System.out.println("Selecione a mesa usando o numero a frente dela");
				int posicao = IntScanner(0,id_mesas.size()-1);
				boolean sucess=false;
				try {
					sucess=h.RemoveMesaVoto(id_mesas.get(posicao));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("Falha na conexao com DB");
					return;
				}
				if(sucess)
					System.out.println("Mesa apagada");
				else
					System.out.println("Algo falhou tente de novo");
			}
			
		}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public static void Func6() {//Alterar propriedades de uma eleicao
			ArrayList<Integer> eleicoesdisponiveis= new ArrayList<Integer>();
			Scanner sc = new Scanner(System.in);
			java.sql.Date DataI;
			java.sql.Date DataF;
			String titulo;
			String descricao;
			try {
				eleicoesdisponiveis=h.RetornaEleicoesNaoIniciadas();
			} catch (Exception e) {
				e.printStackTrace();
				// TODO Auto-generated catch block
				System.out.println("Falha na conexao com DB");
				return;
			}
			if(eleicoesdisponiveis.isEmpty()) {
				System.out.println("Nao existem eleicoes disponiveis para adicionar mesas de voto");
				return;
			}
			else {
				System.out.println("Escolha uma das eleicoes da lista anteriro usando o numero apresentado");
				int escolha = IntScanner(0, eleicoesdisponiveis.size() - 1);
				int eleicaoid = eleicoesdisponiveis.get(escolha);
				System.out.println("Introduza um novo titulo");
				titulo=sc.nextLine();
				System.out.println("Introduza uma nova descricao");
				descricao=sc.nextLine();
				DataI=DateScanner();
				DataF=DateScanner();
				boolean sucess=false;
				try {
					sucess=h.UpdateEleicao(titulo,descricao,DataI,DataF,eleicaoid);
				} catch (Exception e) {
					e.printStackTrace();
					// TODO Auto-generated catch block
					System.out.println("Falha na conexao com DB");
					return;
				}
				if(sucess)
					System.out.println("Eleicao actualizada");
				else
					System.out.println("Eleicao nao foi actualizada por alguma razao");
			}
		}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////		
		public static void Func7() {//Saber em que local cada eleitor votou numa especifica eleicao
			ArrayList<Integer> eleicoes= new ArrayList<Integer>();
			try {
				eleicoes=h.RetornaEleicoesAcabadas();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("Falha na conexao com DB");
				return;
			}
			if(eleicoes.isEmpty())
				System.out.println("Ate ao momento nao existem eleicoes acabadas");
			else {
				System.out.println("Escolha uma das eleicoes seguintes usando o numero apresentado");
				int escolha = IntScanner(0,eleicoes.size()-1);
				int ideleicao = eleicoes.get(escolha);
				try {
					h.MostraLocalVotoEleitores(ideleicao);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("Falha na conexao com DB");
					return;
				}
			}
		}
		
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public static void Func8() {//Estatisticas
			ArrayList<Integer> eleicoes= new ArrayList<Integer>();
			try {
				eleicoes=h.RetornaEleicoesAcabadas();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("Falha na conexao com DB");
				return;
			}
			if(eleicoes.isEmpty())
				System.out.println("Ate ao momento nao existem eleicoes acabadas");
			else {
				System.out.println("Escolha uma das eleicoes seguintes usando o numero apresentado");
				int escolha = IntScanner(0,eleicoes.size()-1);
				int ideleicao = eleicoes.get(escolha);
				try {
					h.ConsultarEleicao(ideleicao);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("Falha na conexao com DB");
					return;
				}
			}
		}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static void Func9(){//Votar antecipado
		ArrayList<Integer> eleicoesdisponiveis= new ArrayList<Integer>();
		Scanner sc = new Scanner(System.in);
		try {
			eleicoesdisponiveis=h.RetornaEleicoesNaoIniciadas();
		} catch (Exception e) {
			e.printStackTrace();
			// TODO Auto-generated catch block
			System.out.println("Falha na conexao com DB");
			return;
		}
		if(eleicoesdisponiveis.isEmpty()) {
			System.out.println("Nao existem eleicoes disponiveis para adicionar mesas de voto");
			return;
		}
		else {
			System.out.println("Escolha uma das eleicoes da lista anteriro usando o numero apresentado");
			int escolha = IntScanner(0, eleicoesdisponiveis.size() - 1);
			int eleicaoid = eleicoesdisponiveis.get(escolha);
			MenuVoto(eleicaoid);
		}
	}

	public static void MenuVoto(int eleicaoid) {
			System.out.println("Votar antecipado");
			Scanner sc = new Scanner(System.in);
			ArrayList<Integer> Eleicoes = new ArrayList<Integer>();
			while(true) {
				System.out.print("Introduza o numero do Bi");
				double BI = DoubleScanner(9);
				boolean identifica=false;
				boolean naovotou=false;
				try {
					identifica=h.Identificar_VerificaEleicao((int) BI,eleicaoid);
					naovotou=h.Verifica_Votos((int) BI,eleicaoid);
				} catch (SQLException e) {
				    e.printStackTrace();
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
					if(u!=null){
						try {
							h.Votar(u,eleicaoid,0);
							MenuPrincipal();
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
				MenuPrincipal();
			}
		}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		public static void Func10() {//Gerir membros de cada mesa de voto
			// TODO Auto-generated method stub
			ArrayList<Integer> eleicoesdisponiveis= new ArrayList<Integer>();
			ArrayList<Integer> bi_pessoas= new ArrayList<Integer>();
			ArrayList<Mesa_voto> mesas = new ArrayList<Mesa_voto>();
			ArrayList<Integer> bis_aptos= new ArrayList<Integer>();
			Scanner sc = new Scanner(System.in);
			Mesa_voto mesa;
			try {
				bi_pessoas=h.RetornaTodosUsers();
				eleicoesdisponiveis=h.RetornaEleicoesNaoIniciadas();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("Falha na conexao com DB");
				return;
			}
			if(eleicoesdisponiveis.isEmpty()) {
				System.out.println("Nao existem eleicoes disponiveis para adicionar mesas de voto");
				return;
			}
			if(bi_pessoas.isEmpty()) {
				System.out.println("Nao existem pessoas suficientes na base de dados");
				return;
			}
			else {
				System.out.println("Escolha uma das eleicoes da lista anterior usando o numero apresentado");
				int escolha = IntScanner(0,eleicoesdisponiveis.size()-1);
				int eleicaoid= eleicoesdisponiveis.get(escolha);
				try {
					mesas=h.RetornaMesasEleicao(eleicaoid);
                    System.out.println(mesas.size());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("Falha na conexao com DB");
					return;
				}
				for(Mesa_voto m : mesas) {
					if(m.membros_mesa.get(0)!=0)
						mesas.remove(m);
				}
				if(mesas.isEmpty()){
					System.out.println("Nao existem mesas para adicionar pessoas nesta eleicao");
					return;
				}
				for(int i=0;i<mesas.size();i++){
					System.out.println(i+"-ID da mesa:"+mesas.get(i).ID);
				}
				System.out.println("Introduza o numero a frente do ID da mesa que quer adicionar pessoas");
				mesa=mesas.get(IntScanner(0,mesas.size()-1));
				for(int bi : bi_pessoas){
					boolean check=false;
					for(Mesa_voto m : mesas){
						if(m.membros_mesa.contains(bi)){
							check=true;
						}
					}
					if(!check)
						bis_aptos.add(bi);
				}
				if(bis_aptos.size()<3){
					System.out.println("Nao existem pessoas suficientes para adicionar a mesa");
					return;
				}
				for(int i=0;i<bis_aptos.size();i++){
					System.out.println(i+"-BI:"+bis_aptos.get(i));
				}
				for(int i=0;i<bis_aptos.size();i++){
					System.out.println("Introduza o numero a frente do bi da pessoa que quer adicionar");
					int pos=IntScanner(0,bis_aptos.size()-1);
					if(mesa.membros_mesa.contains(bis_aptos.get(pos)))
						i--;
					else {
                        mesa.membros_mesa.set(i,bis_aptos.get(pos));
                    }
				}
			}
			boolean sucess=false;
			try {
				sucess=h.UpdateMesa(mesa);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("Falha na conexao com DB");
				return;
			}
			if(sucess)
				System.out.println("Membros da mesa foram adicionads");
			else
				System.out.println("Eleicao Ja comecou");
			
		}


///////-------------------------------------------------------------------------------------------
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
