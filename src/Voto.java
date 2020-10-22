
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;

@SuppressWarnings("serial")
public class Voto implements Serializable {
	
	/**
	 * 
	 */
	public double BI;
	public Mesa_voto Mesa;
	public Date Data;
	
	public Voto(double a,Mesa_voto b) {
		this.BI = a;
		this.Mesa = b;
		this.Data = null;
		
	}
	
	
	@Override
	public boolean equals(Object obj){
		if(obj==null)
			return false;
		if (!Voto.class.isAssignableFrom(obj.getClass())) {
	        return false;
	    }
		final Voto other = (Voto) obj;
		
		if(this.BI!=other.BI)
			return false;
		
		if(!this.Mesa.equals(other.Mesa))
			return false;
		
		if(this.Data!=other.Data)
			return false;
		
		return true;
	}
	
	@Override
	public int hashCode() {
        return Objects.hash(BI, Mesa, Data);
    }
	
	
	public UserInfo deepClone() {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(this);

			ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
			ObjectInputStream ois = new ObjectInputStream(bais);
			return (UserInfo) ois.readObject();
		} catch (IOException e) {
			return null;
		} catch (ClassNotFoundException e) {
			return null;
		}
	}
	
	
}
