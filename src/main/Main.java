package main;

import exceptions.InstanceNotFoundException;
import exceptions.SaldoInsuficienteException;
import modelo.Account;
import modelo.servicio.AccountServicio;

public class Main {
    public static void main(String[] args) {
        AccountServicio accountServicio=new AccountServicio();
        try {
            Account cuenta=accountServicio.findAccountById(2);
            System.out.println(cuenta);
        } catch (InstanceNotFoundException e) {
            throw new RuntimeException(e);
        }

        try {
            accountServicio.transferir(100,2,10);
        } catch (SaldoInsuficienteException e) {

            System.err.println(e.getMessage());
        } catch (InstanceNotFoundException e) {
            System.out.println("No existe alguna de las cuentas");
            System.err.println(e.getMessage());
        }
    }
}
