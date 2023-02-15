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
				throw new InstanceNotFoundException(Account.class.getName());
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
		SessionFactory factory=SessionFactoryUtil.getSessionFactory();
		Transaction tx=null;
		Account cuentaOrigen, cuentaDestino;
		AccMovement accMovement=null;
		BigDecimal cantidadBG=new BigDecimal(cantidad);
		try (Session session= factory.openSession()){
			tx= session.beginTransaction();
			cuentaOrigen=RescatarCuenta(session, accOrigen);
			cuentaDestino=RescatarCuenta(session,accDestino);
			if(cuentaOrigen==null || cuentaDestino==null){
				//System.out.println("No existe alguna de las dos cuentas");
				throw new InstanceNotFoundException("No existe alguna de las cuentas");

			}else{
				if(cantidad>cuentaOrigen.getAmount().doubleValue()){
					throw new SaldoInsuficienteException("Saldo Insuficiente",cuentaOrigen.getAmount(),cantidadBG);

				}else{
					cuentaOrigen.setAmount(BigDecimal.valueOf((cuentaOrigen.getAmount().doubleValue())-cantidad));
					cuentaDestino.setAmount(BigDecimal.valueOf((cuentaDestino.getAmount().doubleValue())+cantidad));
					session.saveOrUpdate(cuentaOrigen);
					session.saveOrUpdate(cuentaDestino);
					LocalDateTime dateTime=LocalDateTime.now();
					accMovement=new AccMovement(cuentaOrigen,cuentaDestino,cantidadBG,dateTime);
					session.save(accMovement);
					tx.commit();
				}




			}



		}catch ( Exception ex) {
			System.err.println("Ha habido una exception " + ex.getMessage());
			if (tx != null) {
				tx.rollback();
			}
			throw ex;
		}
		return accMovement;
	}

	private Account RescatarCuenta(Session session, int accOrigen) {
		Account cuenta=(Account) session.createQuery("Select c from Account c where c.accountno=:idCuentaOrigen").setParameter("idCuentaOrigen",accOrigen).uniqueResult();
		return cuenta;
	}


}
