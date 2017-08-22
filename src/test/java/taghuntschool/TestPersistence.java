package taghuntschool;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import br.cin.ufpe.taghuntschool.domain.GeneralUsr;

public class TestPersistence {
	
	public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("taghuntschoolDS");
        EntityManager em = emf.createEntityManager();
 
        try {
            em.getTransaction().begin();
 
            GeneralUsr usuario = new GeneralUsr();
            usuario.setName("TESTE de Persistencia");
 
            em.persist(usuario);
 
            em.getTransaction().commit();
        }
        catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        }
        finally{
            emf.close();
        }
 
        System.out.println("It is over");
    }
	
}
