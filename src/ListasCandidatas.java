
import java.io.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

@SuppressWarnings("serial")
public class ListasCandidatas implements Serializable{
	
	/**
	 * 
	 */
	
	public int tipo; //Lista de  1-Aluno,2-Docente,3-Funcionario
	public ArrayList <Integer> Lista;
	public String nome;
	
	public ListasCandidatas(int t,ArrayList<Integer> u, String n) {
		this.tipo=t;
		this.nome=n;
		this.Lista=u;
		
	}
	
	@Override
	public boolean equals(Object obj){
		if(obj==null)
			return false;
		if (!ListasCandidatas.class.isAssignableFrom(obj.getClass())) {
	        return false;
	    }
		final ListasCandidatas other = (ListasCandidatas) obj;
		if(this.tipo!=other.tipo)
			return false;
		if(!this.nome.equals(other.nome))
			return false;
		if(!this.Lista.containsAll(other.Lista))
			return false;
		if(!other.Lista.containsAll(this.Lista))
			return false;
		return true;
	}
	public int hashCode() {
        return Objects.hash(tipo, nome, Lista);
    }
	
	public ListasCandidatas deepClone() {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(this);

			ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
			ObjectInputStream ois = new ObjectInputStream(bais);
			return (ListasCandidatas) ois.readObject();
		} catch (IOException e) {
			return null;
		} catch (ClassNotFoundException e) {
			return null;
		}
	}
	
	public void printerteste() {
		System.out.println("ListasCandidata");
		System.out.println(this.tipo);
		System.out.println(this.nome);

	}
	

}
