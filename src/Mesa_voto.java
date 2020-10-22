
import java.io.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

public class Mesa_voto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public int ID;
	public DepFacInfo local;
	public ArrayList<Integer> membros_mesa;
	public int numero_terminais;
	public int estado;
	
	public Mesa_voto(DepFacInfo a, int b){
		this.local=a;
		this.membros_mesa = new ArrayList<Integer>();
		this.numero_terminais=b;
		this.estado=0; //False=Dsligado True =Ligado
	}
	
	@Override
	public boolean equals(Object obj){
		if(obj==null)
			return false;
		if (!Mesa_voto.class.isAssignableFrom(obj.getClass())) {
	        return false;
	    }
		final Mesa_voto other = (Mesa_voto) obj;
		
		if(this.ID!=(other.ID))
			return false;
		
		if(!this.local.equals(other.local))
			return false;
		
		if(this.numero_terminais!=other.numero_terminais)
			return false;
		
		if(!this.membros_mesa.containsAll(other.membros_mesa))
			return false;
		
		if(!other.membros_mesa.containsAll(this.membros_mesa))
			return false;
		return true;
	}
	
	@Override
	public int hashCode() {
        return Objects.hash(ID,local, membros_mesa, numero_terminais);
    }
	
	public Mesa_voto deepClone() {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(this);

			ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
			ObjectInputStream ois = new ObjectInputStream(bais);
			return (Mesa_voto) ois.readObject();
		} catch (IOException e) {
			return null;
		} catch (ClassNotFoundException e) {
			return null;
		}
	}
	
	
	public void printerteste() {
		System.out.println("Mesa");
		System.out.println(this.ID);
		System.out.println(this.local.Departamento);
		System.out.println(this.numero_terminais);
		System.out.println("Membros da mesa");

	}

}
