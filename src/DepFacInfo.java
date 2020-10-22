import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;


@SuppressWarnings("serial")
public class DepFacInfo implements Serializable {
	
	//Capaz de faltar coisas?
	/**
	 * 
	 */

	public int ID;
	public String Faculdade;
	public String Departamento;
	
	
	public DepFacInfo(String b,String c) {

		this.Faculdade=b;
		this.Departamento=c;
	}
	
	@Override
	public boolean equals(Object obj){
		if(obj==null)
			return false;
		if (!DepFacInfo.class.isAssignableFrom(obj.getClass())) {
	        return false;
	    }
		final DepFacInfo other = (DepFacInfo) obj;
		if(this.ID!=other.ID)
			return false;
		if (!this.Faculdade.equals(other.Faculdade)) 
	        return false;
		if (!this.Faculdade.equals(other.Faculdade)) 
	        return false;
		return true;
	}
	

	@Override
    public int hashCode() {
        return Objects.hash( ID,Faculdade,Departamento);
    }
	
	
	public DepFacInfo deepClone() {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(this);

			ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
			ObjectInputStream ois = new ObjectInputStream(bais);
			return (DepFacInfo) ois.readObject();
		} catch (IOException e) {
			return null;
		} catch (ClassNotFoundException e) {
			return null;
		}
	}
	
	
	public void printerteste() {
		System.out.println("DEPFAC");
		System.out.println(this.ID);
		System.out.println(this.Faculdade);
		System.out.println(this.Departamento);
	}
	
}

