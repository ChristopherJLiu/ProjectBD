import java.util.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import oracle.jdbc.driver.*;


public class Funtions {
	Connection conn;


    Funtions() throws SQLException {
        conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "bd", "bd");
    }
    
    




	public boolean CriarouUpdateUser(UserInfo m) throws SQLException{
		// TODO Auto-generated method stub
		String envia;
		PreparedStatement pstmt;
		conn.setAutoCommit(false);
        try {
			envia = "INSERT INTO UTILIZADORES (BI,NOME,TIPO,PASSWORD,CONTATOTEL,MORADA,DATAVALBI,DEPFACINFOID) VALUES (?,?,?,?,?,?,?,?)";
			pstmt = conn.prepareStatement(envia);
			pstmt.setInt(1, (int) m.BI);
			pstmt.setString(2,m.Nome);
			pstmt.setInt(3, m.Tipo);
			pstmt.setString(4, m.Password);
			pstmt.setInt(5, (int) m.ContactoTel);
			pstmt.setString(6, m.Morada);
			pstmt.setDate(7, m.BIval);
			pstmt.setInt(8, m.DepFac.ID);
			pstmt.executeUpdate();
			conn.commit();
        }catch(SQLException se){
            // If there is any error.
			se.printStackTrace();
            System.out.println("OperationFailed rolling back\n");
            conn.rollback();
            conn.setAutoCommit(true);
            return false;
        }
        conn.setAutoCommit(true);
		return true;
	}





	public Boolean AdicionarDepFac(DepFacInfo m) throws SQLException{
		String envia;
		ResultSet rs;
		PreparedStatement pstmt;
		Random rnd;
		int n;
		conn.setAutoCommit(false);
		try {
			envia = "INSERT INTO DEPFACINFO (ID,FACULDADE,DEPARTAMENTO) VALUES (?,?,?)";
			pstmt = conn.prepareStatement(envia);
			rnd = new Random();
			n = rnd.nextInt(999999999);
			pstmt.setInt(1,n);
			pstmt.setString(2, m.Faculdade);
			pstmt.setString(3, m.Departamento);
			pstmt.executeUpdate();
			conn.commit();
        }catch(SQLException se){
            // If there is any error.
			se.printStackTrace();
            System.out.println("OperationFailed rolling back\n");
            conn.rollback();
            conn.setAutoCommit(true);
            return false;
        }
		conn.setAutoCommit(true);
		return true;
	}
	
	
	public boolean RemoverDepFac(String nomerem) throws SQLException{
		// TODO Auto-generated method stub
		String envia;
		ResultSet rs;
		PreparedStatement pstmt;
		conn.setAutoCommit(false);
		int count=0;
        try {
            envia="DELETE FROM DEPFACINFO WHERE LTRIM(RTRIM(FACULDADE))=? OR LTRIM(RTRIM(DEPARTAMENTO))=?";
            pstmt = conn.prepareStatement(envia);
    		pstmt.setString(1, nomerem);
    		pstmt.setString(2, nomerem);
			count=pstmt.executeUpdate();
			conn.commit();
        }catch(SQLException se){
            // If there is any error.
			se.printStackTrace();
            System.out.println("OperationFailed rolling back\n");
            conn.rollback();
            conn.setAutoCommit(true);
            return false;
        }
        conn.setAutoCommit(true);
        if(count>0)
			return true;
        else
        	return false;
	}
	
	public boolean AlterarNomeFaculdade(String antigo,String novo) throws SQLException{
		String envia;
		ResultSet rs;
		PreparedStatement pstmt;
		conn.setAutoCommit(false);
		int count=0;
		try{
			envia="UPDATE DEPFACINFO SET FACULDADE=? WHERE LTRIM(RTRIM(FACULDADE))=?";
			pstmt = conn.prepareStatement(envia);
			pstmt.setString(1, novo);
			pstmt.setString(2, antigo);
			count=pstmt.executeUpdate();
    		conn.commit();
        }catch(SQLException se){
            // If there is any error.
			se.printStackTrace();
            System.out.println("OperationFailed rolling back\n");
            conn.rollback();
            conn.setAutoCommit(true);
            return false;
        }
		conn.setAutoCommit(true);
		if(count>0)
			return true;
		else
			return false;
		
	}
	
	public boolean AlterarNomeDepartamento(String antigo, String novo) throws SQLException{
		String envia;
		PreparedStatement pstmt;
		int count=0;
		conn.setAutoCommit(false);
		try{
			envia="UPDATE DEPFACINFO SET DEPARTAMENTO=? WHERE LTRIM(RTRIM(DEPARTAMENTO))=?";
			pstmt = conn.prepareStatement(envia);
			pstmt.setString(1, novo);
			pstmt.setString(2, antigo);
			count=pstmt.executeUpdate();
    		conn.commit();
        }catch(SQLException se){
            // If there is any error.
			se.printStackTrace();
            System.out.println("OperationFailed rolling back\n");
            conn.rollback();
            conn.setAutoCommit(true);
            return false;
        }
		conn.setAutoCommit(true);
		if(count>0)
			return true;
		else
			return false;
	}
	
	public UserInfo AutenticarEleitor(double bi, String pass) throws SQLException{
		// TODO Auto-generated method stub
		String envia;
		ResultSet rs;
		envia = "SELECT * FROM UTILIZADORES  WHERE BI=? AND LTRIM(RTRIM(PASSWORD))=?";
		PreparedStatement pstmt = conn.prepareStatement(envia);
		pstmt.setInt(1,(int)bi);
		pstmt.setString(2, pass);
		rs = pstmt.executeQuery();
        if (rs.next()) {
			String Nome=rs.getString("NOME");
			int Tipo=rs.getInt("TIPO");
			int BI=rs.getInt("BI");
			String Password=rs.getString("PASSWORD");
			int ContactoTel=rs.getInt("CONTATOTEL");
			String Morada=rs.getString("MORADA");
			java.sql.Date BIval=rs.getDate("DATAVALBI");
			int ID=rs.getInt("DEPFACINFOID");
			DepFacInfo aux = new DepFacInfo("Somewhere","Somewhwere");
			aux.ID=ID;
			UserInfo u = new UserInfo(Nome,Tipo,Password,ContactoTel,Morada,BI,BIval,aux);
    		return u;
        }
		return null;
	}
	
	public CopyOnWriteArrayList<DepFacInfo> RetornaTodosDepFac() throws SQLException{
		String envia;
		ResultSet rs;
		CopyOnWriteArrayList<DepFacInfo> res = new CopyOnWriteArrayList<DepFacInfo>();
		envia = "SELECT * FROM DEPFACINFO";
		PreparedStatement pstmt = conn.prepareStatement(envia);;
		rs = pstmt.executeQuery();
		while(rs.next()) {
			DepFacInfo aux=new DepFacInfo(rs.getString("FACULDADE"),rs.getString("DEPARTAMENTO"));
			aux.ID=rs.getInt("ID");
			res.add(aux);
		}
		return res;
	}
	
	
	
	
	
	public boolean CriarEleicao(EleicaoInfo e,ArrayList<Integer>ids) throws SQLException{
		String envia,envia2;
		envia = "INSERT INTO ELEICAOINFO (ID,DATAINICIO,DATAFIM,TITULO,DESCRICAO,TIPO,ESTADO) VALUES (?,?,?,?,?,?,?)";
		envia2 = "INSERT INTO ELEICAOINFO_DEPFACINFO (ELEICAOINFOID,DEPFACINFOID) VALUES (?,?)";
		PreparedStatement pstmt,pstmt2;
		conn.setAutoCommit(false);
		try {
			pstmt = conn.prepareStatement(envia);
			Random rnd = new Random();
			int eleicaoid = rnd.nextInt(999999999);
			pstmt.setInt(1,eleicaoid);
			pstmt.setDate(2, e.DataI);
			pstmt.setDate(3, e.DataF);
			pstmt.setString(4, e.Titulo);
			pstmt.setString(5, e.Descricao);
			pstmt.setInt(6, e.Tipo);
			pstmt.setInt(7, 0);//0=false desligado
			pstmt.executeUpdate();
			pstmt2 = conn.prepareStatement(envia2);
			for(int i=0;i<ids.size();i++) {
				pstmt2.setInt(1, eleicaoid);
				pstmt2.setInt(2, ids.get(i));
				pstmt2.executeUpdate();
			}
			conn.commit();
		}catch(SQLException se){
            // If there is any error.
			se.printStackTrace();
            System.out.println("OperationFailed rolling back\n");
            conn.rollback();
            conn.setAutoCommit(true);
            return false;
        }
		conn.setAutoCommit(true);
		return true;
	}
	
	public boolean CheckaExistenciaMesa(int eid,int did) throws SQLException{
		String envia;
		ResultSet rs;
		CopyOnWriteArrayList<DepFacInfo> res = new CopyOnWriteArrayList<DepFacInfo>();
		envia = "SELECT * FROM MESAVOTO";
		PreparedStatement pstmt = conn.prepareStatement(envia);;
		rs = pstmt.executeQuery();
		while(rs.next()) {
			if(rs.getInt("ELEICAOINFOID")==eid && rs.getInt("DEPFACINFOID")==did)
				return true;
		}
		return false;
		
	}
	
	public boolean AdicionarMesaVoto(int eid, Mesa_voto m) throws SQLException{
		String envia;
		envia = "INSERT INTO MESAVOTO (ID,NTERMINAIS,ESTADO,ELEICAOINFOID,DEPFACINFOID,NVOTOS,BI1,BI2,BI3) VALUES (?,?,?,?,?,?,?,?,?)";
		conn.setAutoCommit(false);
		try {
			PreparedStatement pstmt = conn.prepareStatement(envia);
			Random rnd = new Random();
			int n = rnd.nextInt(999999999);
			pstmt.setInt(1,n);
			pstmt.setInt(2, m.numero_terminais);
			pstmt.setInt(3, 0);//0 = false desligado
			pstmt.setInt(4, eid);
			pstmt.setInt(5, m.local.ID);
			pstmt.setInt(6, 0);
			pstmt.setInt(7,0);
			pstmt.setInt(8,0);
			pstmt.setInt(9,0);
			pstmt.executeUpdate();
			conn.commit();
		}catch(SQLException se){
            // If there is any error.
			se.printStackTrace();
            System.out.println("OperationFailed rolling back\n");
            conn.rollback();
            conn.setAutoCommit(true);
            return false;
        }
		conn.setAutoCommit(true);
		return true;
	}
	
	public ArrayList<Integer> RetornaMesasVotoID(int eid) throws SQLException{
		ArrayList<Integer> list = new ArrayList<Integer>();
		String envia;
		ResultSet rs;
		envia = "SELECT * FROM MESAVOTO WHERE ELEICAOINFOID=?";
		PreparedStatement pstmt = conn.prepareStatement(envia);
		pstmt.setInt(1,eid);
		rs = pstmt.executeQuery();
		int counter=0;
		while(rs.next()) {
			System.out.println(counter+"-ID da mesa:"+rs.getInt("ID"));
			list.add(rs.getInt("ID"));
			counter++;
		}
		return list;
	}
	
	public boolean RemoveMesaVoto(int id) throws SQLException{
		// TODO Auto-generated method stub
		String envia;
		PreparedStatement pstmt;
		conn.setAutoCommit(false);
		int count=0;
		try{
			envia="DELETE FROM MESAVOTO WHERE ID=?";
			pstmt = conn.prepareStatement(envia);
			pstmt.setInt(1, id);
		    count=pstmt.executeUpdate();
		    conn.commit();
		}catch(SQLException se){
            // If there is any error.
			se.printStackTrace();
            System.out.println("OperationFailed rolling back\n");
            conn.rollback();
            conn.setAutoCommit(true);
            return false;
        }
		conn.setAutoCommit(true);
		if(count>0)
			return true;
		else
			return false;
	}
	
	public boolean Identificar_VerificaEleicao(int bi,int eid) throws SQLException{
		String envia;
		ResultSet rs;
		PreparedStatement pstmt;
		envia = "SELECT * FROM UTILIZADORES WHERE BI=?";
		pstmt = conn.prepareStatement(envia);
		pstmt.setInt(1, bi);
		rs = pstmt.executeQuery();
		if (rs.next()) {
			String Nome=rs.getString("NOME");
            int Tipo=rs.getInt("TIPO");
            int BI=rs.getInt("BI");
            String Password=rs.getString("PASSWORD");
            int ContactoTel=rs.getInt("CONTATOTEL");
            String Morada=rs.getString("MORADA");
            java.sql.Date BIval=rs.getDate("DATAVALBI");
            int ID=rs.getInt("DEPFACINFOID");
            DepFacInfo aux = new DepFacInfo("Somewhere","Somewhwere");
            aux.ID=ID;
			UserInfo u = new UserInfo(Nome,Tipo,Password,ContactoTel,Morada,BI,BIval,aux);
            envia = "SELECT DEPFACINFOID FROM ELEICAOINFO_DEPFACINFO WHERE ELEICAOINFOID=?";
    		pstmt = conn.prepareStatement(envia);
    		pstmt.setInt(1, eid);
    		rs = pstmt.executeQuery();
    		while(rs.next()) {
    			if(rs.getInt("DEPFACINFOID")==u.DepFac.ID) {
    				envia = "SELECT TIPO FROM ELEICAOINFO WHERE ID=?";
    	    		pstmt = conn.prepareStatement(envia);
    	    		pstmt.setInt(1, eid);
    	    		rs = pstmt.executeQuery();
    	    		if(rs.next()) {
    	    			int tipo_eleicao=rs.getInt("TIPO");
    	    			if(tipo_eleicao==2)
    	    				return true;
    	    			if(tipo_eleicao==1 && u.Tipo==1)
    	    				return true;
    	    			else if(tipo_eleicao==3 && u.Tipo==2) 
    	    				return true;
    	    			else if(tipo_eleicao==3 && u.Tipo==2)
    	    				return true;
    	    			else
    	    				return false;
    	    		}
    			}
    		}
    		return false;
		}	
		return false;
	}
	
	public boolean Verifica_Votos(int bi,int eid) throws SQLException{
		String envia;
		ResultSet rs;
		PreparedStatement pstmt;
		envia = "SELECT USERBI FROM VOTO WHERE ELEICAOINFOID=?";
		pstmt = conn.prepareStatement(envia);
		pstmt.setInt(1, eid);
		rs = pstmt.executeQuery();
		while(rs.next()) {
			if(rs.getInt("USERBI")==bi)
				return false;
		}
		return true;
	}
	
	
	public void MostraLocalVotoEleitores(int eid) throws SQLException{
		String envia;
		ResultSet rs;
		envia = "SELECT * FROM VOTO WHERE ELEICAOINFOID=?";
		PreparedStatement pstmt = conn.prepareStatement(envia);
		pstmt.setInt(1, eid);
		rs = pstmt.executeQuery();
		while(rs.next()) {
			Voto aux = null;
			aux.BI=rs.getInt("USERBI");
			aux.Data=rs.getDate("DATAVOTO");
			envia = "SELECT DEPFACINFOID FROM MESAVOTO WHERE ID=?";
			pstmt = conn.prepareStatement(envia);
			pstmt.setInt(1, rs.getInt("MESAVOTOID"));
			rs = pstmt.executeQuery();
			if(rs.next()) {
				envia = "SELECT DEPARTAMENTO FROM DEPFACINFO WHERE ID=?";
				pstmt = conn.prepareStatement(envia);
				pstmt.setInt(1, rs.getInt("DEPFACINFOID"));
				rs = pstmt.executeQuery();
				if(rs.next()) {
					aux.Mesa.local.Departamento=rs.getString("DEPARTAMENTO");
				}
			}
			System.out.println("O user com bi:"+aux.BI+"Votou as:"+aux.Data+"em:"+aux.Mesa.local.Departamento);
		}
	}
	
	public void ConsultarEleicao(int eid) throws SQLException{
		String envia;
		ResultSet rs;
		int total=0;
		ArrayList<String> listas = new ArrayList<String>();
		ArrayList<Integer>votos_lista = new ArrayList<Integer>();
		envia = "SELECT * FROM LISTACANDIDATA WHERE ELEICAOINFOID=?";
		PreparedStatement pstmt = conn.prepareStatement(envia);
		pstmt.setInt(1, eid);
		rs = pstmt.executeQuery();
		while(rs.next()) {
			listas.add(rs.getString("NOME"));
			int x=rs.getInt("NVOTOS");
			votos_lista.add(x);
			total+=x;
		}
		System.out.println("Na eleicao escolhida");
		for(int i=0; i<listas.size();i++) {
			System.out.println("A Lista:"+listas.get(i)+" obteve:"+votos_lista.get(i)+"votos sendo uma percentagem de"+(votos_lista.get(i)/total)*100+"votos.");
		}
	}
	
	public ArrayList<Integer> RetornaEleicoesAcabadas() throws SQLException{
		// TODO Auto-generated method stub
		ArrayList<Integer> idsacabadas= new ArrayList<Integer>();
		String envia;
		ResultSet rs;
		envia = "SELECT * FROM ELEICAOINFO WHERE ESTADO=?";//0 NAO INICIADO 1 A DECORRER 2 ACABADO
		PreparedStatement pstmt = conn.prepareStatement(envia);
		pstmt.setInt(1,2);
		rs = pstmt.executeQuery();
		int counter=0;
		while(rs.next()) {
			System.out.println(counter+"-TITULO:"+rs.getString("TITULO:")+" DESCRICAO:"+rs.getString("DESCRICAO"));
			idsacabadas.add(rs.getInt("ID"));
			counter++;
		}
		return idsacabadas;
	}
	
	public ArrayList<Integer> RetornaEleicoesNaoIniciadas() throws SQLException{
		// TODO Auto-generated method stub
		ArrayList<Integer> idsni= new ArrayList<Integer>();
		String envia;
		ResultSet rs;
		envia = "SELECT * FROM ELEICAOINFO WHERE ESTADO=0";//0 NAO INICIADO 1 A DECORRER 2 ACABADO
		PreparedStatement pstmt = conn.prepareStatement(envia);
		rs = pstmt.executeQuery();
		int counter=0;
		while(rs.next()) {
			System.out.println(counter+"-TITULO:"+rs.getString("TITULO")+" DESCRICAO:"+rs.getString("DESCRICAO"));
			idsni.add(rs.getInt("ID"));
			counter++;
		}
		return idsni;
	}
	
	public ArrayList<Integer> RetornaEleicoesDecorrer() throws SQLException{
		// TODO Auto-generated method stub
		ArrayList<Integer> idi= new ArrayList<Integer>();
		String envia;
		ResultSet rs;
		envia = "SELECT * FROM ELEICAOINFO WHERE ESTADO=1";//0 NAO INICIADO 1 A DECORRER 2 ACABADO
		PreparedStatement pstmt = conn.prepareStatement(envia);
		rs = pstmt.executeQuery();
		int counter=0;
		while(rs.next()) {
			System.out.println(counter+"-TITULO:"+rs.getString("TITULO:")+" DESCRICAO:"+rs.getString("DESCRICAO"));
			idi.add(rs.getInt("ID"));
			counter++;
		}
		return idi;
	}

	public int RetornaTipoEleicao(int eid) throws SQLException{
		String envia;
		ResultSet rs;
		envia = "SELECT TIPO FROM ELEICAOINFO WHERE ESTADO=0 AND ID=?";//0 NAO INICIADO 1 A DECORRER 2 ACABADO
		PreparedStatement pstmt = conn.prepareStatement(envia);
		pstmt.setInt(1,eid);
		int tipo=0;
		rs = pstmt.executeQuery();
		if(rs.next()){
			tipo=rs.getInt("TIPO");
		}
		return tipo;

	}

	public ArrayList<Integer> RetornaDepsEleicao(int eid) throws SQLException{
    	ArrayList<Integer> deps = new ArrayList<Integer>();
		String envia;
		ResultSet rs;
		envia = "SELECT DEPFACINFOID FROM ELEICAOINFO_DEPFACINFO WHERE ELEICAOINFOID=?";//0 NAO INICIADO 1 A DECORRER 2 ACABADO
		PreparedStatement pstmt = conn.prepareStatement(envia);
		pstmt.setInt(1,eid);
		rs = pstmt.executeQuery();
		while(rs.next()){
			deps.add(rs.getInt("DEPFACINFOID"));
		}
    	return deps;
	}

	public ArrayList<Integer> RetornaTodosUserNaoEmListas() throws SQLException{
		ArrayList<Integer> listauseremlista = new ArrayList<Integer>();
		ArrayList<Integer> todosusers = new ArrayList<Integer>();
		ArrayList<Integer> bis_aptos = new ArrayList<Integer>();
		String envia,envia2;
		PreparedStatement pstmt,pstmt2;
		ResultSet rs;
		envia = "SELECT * FROM LISTACANDIDATA_USER";
		envia2 = "SELECT * FROM UTILIZADORES";
		pstmt = conn.prepareStatement(envia);
		pstmt2 = conn.prepareStatement(envia2);;
		rs = pstmt.executeQuery();
		while(rs.next()){
			listauseremlista.add(rs.getInt("USERBI"));
		}
		rs=pstmt2.executeQuery();
		while(rs.next()) {
			todosusers.add(rs.getInt("BI"));
		}
		for(int i : todosusers){
			if(!listauseremlista.contains(i))
				bis_aptos.add(i);
		}
		return bis_aptos;
	}

	public int RetornaTipoUser(int bi) throws SQLException{
		String envia;
		ResultSet rs;
		envia = "SELECT TIPO FROM UTILIZADORES WHERE BI=?";//0 NAO INICIADO 1 A DECORRER 2 ACABADO
		PreparedStatement pstmt = conn.prepareStatement(envia);
		pstmt.setInt(1,bi);
		int tipo=0;
		rs = pstmt.executeQuery();
		if(rs.next()){
			tipo=rs.getInt("TIPO");
		}
		return tipo;

	}


	public boolean AdicionaLista(ListasCandidatas lista, int eid) throws SQLException{
		String envia,envia2;
		envia = "INSERT INTO LISTACANDIDATA (ID,TIPO,NOME,NVOTOS,ELEICAOINFOID) VALUES (?,?,?,?,?)";
		envia2 = "INSERT INTO LISTACANDIDATA_USER (LISTACANDIDATAID, USERBI) VALUES (?,?)";
		PreparedStatement pstmt,pstmt2;
		conn.setAutoCommit(false);
		try {
			pstmt = conn.prepareStatement(envia);
			Random rnd = new Random();
			int listaid = rnd.nextInt(999999999);
			pstmt.setInt(1,listaid);
			pstmt.setInt(2, lista.tipo);
			pstmt.setString(3, lista.nome);
			pstmt.setInt(4, 0);
			pstmt.setInt(5, eid);//
			pstmt.executeUpdate();
			pstmt2 = conn.prepareStatement(envia2);
			for(int i=0;i<lista.Lista.size();i++) {
				pstmt2.setInt(1, listaid);
				pstmt2.setInt(2, lista.Lista.get(i));
				pstmt2.executeUpdate();
			}
			conn.commit();
		}catch(SQLException se){
			// If there is any error.
			se.printStackTrace();
			System.out.println("OperationFailed rolling back\n");
			conn.rollback();
			conn.setAutoCommit(true);
			return false;
		}
		conn.setAutoCommit(true);
		return true;
	}

	public boolean UpdateEleicao(String titulo, String descricao,java.sql.Date DataI, java.sql.Date DataF, int eid) throws SQLException{
		String envia;
		PreparedStatement pstmt;
		int count=0;
		conn.setAutoCommit(false);
		try{
			envia="UPDATE ELEICAOINFO SET TITULO=? , DESCRICAO=? , DATAINICIO=? , DATAFIM=?  WHERE ID=?";
			pstmt = conn.prepareStatement(envia);
			pstmt.setString(1, titulo);
			pstmt.setString(2, descricao);
			pstmt.setDate(3, DataI);
			pstmt.setDate(4, DataF);
			pstmt.setInt(5, eid);
			count=pstmt.executeUpdate();
			conn.commit();
		}catch(SQLException se){
			// If there is any error.
			se.printStackTrace();
			System.out.println("OperationFailed rolling back\n");
			conn.rollback();
			conn.setAutoCommit(true);
			return false;
		}
		conn.setAutoCommit(true);
		if(count>0)
			return true;
		else
			return false;
	}


	
	
	public void Votar(UserInfo u,int eid,int mid) throws SQLException{
		String envia;
		ResultSet rs;
		envia = "SELECT * FROM LISTACANDIDATA WHERE ELEICAOINFOID=?";
		ArrayList<Integer> lista_ids= new ArrayList<Integer>();
		PreparedStatement pstmt = conn.prepareStatement(envia);
		pstmt.setInt(1, eid);
		rs = pstmt.executeQuery();
		int counter=0;
		while(rs.next()) {
			if(u.Tipo==rs.getInt("TIPO")) {
				lista_ids.add(rs.getInt("ID"));
				System.out.println(counter+"-Nome da Lista:"+rs.getString("Nome"));
				counter++;
			}
		}
		if(lista_ids.isEmpty()) {
			System.out.println("Nao existem listas candidatas em que pode votar");
			return;
		}
		System.out.println("Escolha a lista que pretende votar inserindo o numero");
		int pos=IntScanner(0,lista_ids.size()-1);
		int listaescolhida=lista_ids.get(pos);
		conn.setAutoCommit(false);
		int count=0;
		try {
			String envia2,envia3;
			envia2 = "INSERT INTO VOTO (ID,DATAVOTO,ELEICAOINFOID,MESAVOTOID,USERBI) VALUES (?,?,?,?,?)";
			envia3 = envia="UPDATE LISTACANDIDATA SET NVOTOS=NVOTOS+1 WHERE ID=?";
			PreparedStatement pstmt2,pstmt3;
			pstmt2 = conn.prepareStatement(envia2);
			Random rnd = new Random();
			int id = rnd.nextInt(999999999);
			pstmt2.setInt(1,id);
			java.sql.Date dataactual = new java.sql.Date(Calendar.getInstance().getTime().getTime());
			pstmt2.setDate(2, dataactual);
			pstmt2.setInt(3, eid);
			pstmt2.setInt(4, mid);
			pstmt2.setInt(5, (int) u.BI);
			pstmt2.executeUpdate();
			pstmt3 = conn.prepareStatement(envia3);
			pstmt3.setInt(1,id);
			count=pstmt3.executeUpdate();
		}catch(SQLException se){
            // If there is any error.
			se.printStackTrace();
            System.out.println("OperationFailed rolling back\n");
            conn.rollback();
            conn.setAutoCommit(true);
            System.out.println("Voto nao registado");
        }
		conn.setAutoCommit(true);
		if(count>0)
			System.out.println("Voto registado");
		else
			System.out.println("Voto nao registado");
	}
	
	public void AlteraEstadoMesa(int mid, int estado) throws SQLException {
		conn.setAutoCommit(false);
		int count=0;
		try {
			String envia;
			envia = "UPDATE MESAVOTO SET ESTADO=? WHERE ID=?";
			PreparedStatement pstmt = conn.prepareStatement(envia);
			pstmt.setInt(1, estado);
			pstmt.setInt(2, mid);
			count=pstmt.executeUpdate();
			conn.commit();
		}catch(SQLException se){
            // If there is any error.
			se.printStackTrace();
            System.out.println("OperationFailed rolling back\n");
            conn.rollback();
            conn.setAutoCommit(true);
        }
		conn.setAutoCommit(true);
		if(count==0) {
			System.out.println("Erro ao alterar o estado da mesa");
			System.exit(0);
		}
	}

	public int RetornaUserDep(int bi) throws SQLException {
		String envia;
		ResultSet rs;
		envia = "SELECT * FROM UTILIZADORES WHERE BI=?";//0 NAO INICIADO 1 A DECORRER 2 ACABADO
		PreparedStatement pstmt = conn.prepareStatement(envia);
		pstmt.setInt(1, bi);
		int dep = 0;
		rs = pstmt.executeQuery();
		if (rs.next()) {
			dep = rs.getInt("DEPFACINFOID");
		}
		return dep;

	}

	public ArrayList<Integer> RetornaTodosUsers() throws SQLException {
		String envia;
		ArrayList<Integer> bis= new ArrayList<Integer>();
		ResultSet rs;
		envia = "SELECT BI FROM UTILIZADORES";
		PreparedStatement pstmt = conn.prepareStatement(envia);
		int tipo = 0;
		rs = pstmt.executeQuery();
		while (rs.next()) {
			bis.add(rs.getInt("BI"));
		}
		return bis;

	}

	public ArrayList<Mesa_voto> RetornaMesasEleicao(int eid ) throws SQLException{
		String envia;
		ResultSet rs;
		ArrayList<Mesa_voto> mesas = new ArrayList<Mesa_voto>();
		envia = "SELECT * FROM MESAVOTO WHERE ELEICAOINFOID=?";
		PreparedStatement pstmt = conn.prepareStatement(envia);
		pstmt.setInt(1,eid);
		rs = pstmt.executeQuery();
		while(rs.next()){
			ArrayList<Integer> bis = new ArrayList<Integer>();
			if(rs.getInt("BI1")==0) {
				DepFacInfo dep = new DepFacInfo("Somewhere","Somewhere");
				dep.ID=rs.getInt("DEPFACINFOID");
				bis.add(rs.getInt("BI1"));
				bis.add(rs.getInt("BI2"));
				bis.add(rs.getInt("BI3"));
				Mesa_voto mesa= new Mesa_voto(dep,rs.getInt("NTERMINAIS"));
				mesa.membros_mesa=bis;
				mesa.ID=rs.getInt("ID");
				mesas.add(mesa);
			}

		}
		return mesas;
	}

	public boolean UpdateMesa(Mesa_voto m) throws SQLException{
		String envia;
		ResultSet rs;
		PreparedStatement pstmt;
		conn.setAutoCommit(false);
		int count=0;
		try {
			envia="UPDATE MESAVOTO SET BI1=? , BI2=? , BI3=? WHERE ID=? AND ESTADO=0";
			pstmt = conn.prepareStatement(envia);
			pstmt.setInt(1,m.membros_mesa.get(0));
			pstmt.setInt(2,m.membros_mesa.get(1));
			pstmt.setInt(3,m.membros_mesa.get(2));
			pstmt.setInt(4,m.ID);
			count=pstmt.executeUpdate();
			conn.commit();
		}catch(SQLException se){
			// If there is any error.
			se.printStackTrace();
			System.out.println("OperationFailed rolling back\n");
			conn.rollback();
			conn.setAutoCommit(true);
			return false;
		}
		conn.setAutoCommit(true);
		if(count>0)
			return true;
		else
			return false;
	}


	
//----------------------------------------------------------------------------------------------------------------------------------
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

////PARA DEBUG
	public void FinalizaManualmente(int estado,int eid) throws SQLException{
    	System.out.println("ola");
		String envia;
		ResultSet rs;
		PreparedStatement pstmt;
		conn.setAutoCommit(false);
		int count=0;
		try{
			envia="UPDATE ELEICAOINFO SET ESTADO=? WHERE ID=?";
			pstmt = conn.prepareStatement(envia);
			pstmt.setInt(1,estado);
			pstmt.setInt(2,eid);
			count=pstmt.executeUpdate();
			conn.commit();
			return;
		}catch(SQLException se){
			// If there is any error.
			se.printStackTrace();
			System.out.println("OperationFailed rolling back\n");
			conn.rollback();
			conn.setAutoCommit(true);
		}
		conn.setAutoCommit(true);
		return;
	}
	

}

