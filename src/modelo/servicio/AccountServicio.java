package modelo.servicio;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import exceptions.SaldoInsuficienteException;
import modelo.AccMovement;
import modelo.Account;

import exceptions.InstanceNotFoundException;
import util.SessionFactoryUtil;

public class AccountServicio implements IAccountServicio{

	@Override
	public Account findAccountById(int accId) throws InstanceNotFoundException {
		SessionFactory factory=SessionFactoryUtil.getSessionFactory();
		Transaction tx=null;
		Account cuenta;
		try (Session session= factory.openSession()){
			tx= session.beginTransaction();
			cuenta=session.get(Account.class,accId);
			if(cuenta==null){
				System.out.println("No existe ninguna cuenta con el Id: "+accId);
			}
			tx.commit();

		}catch ( Exception ex) {
			System.err.println("Ha habido una exception " + ex.getMessage());
			if (tx != null) {
				tx.rollback();
			}
			throw ex;
		}
		return cuenta;
	}

	@Override
	public AccMovement transferir(int accOrigen, int accDestino, double cantidad)
			throws SaldoInsuficienteException, InstanceNotFoundException, UnsupportedOperationException {
		// TODO Auto-generated method stub
		return null;
	}


	
}
