package main;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import exceptions.SaldoInsuficienteException;
import modelo.AccMovement;
import modelo.Account;
import modelo.servicio.AccountServicio;
import modelo.servicio.IAccountServicio;
import exceptions.InstanceNotFoundException;
import javax.swing.JLabel;
import java.awt.Font;

import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextArea;
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;

public class TxWindow extends JFrame {

	private JPanel contentPane;
	private JTextField textFieldOrigen;
	private JTextField textFieldDestino;
	private JTextField textField_cantidad_transfer;
	
	private JTextArea mensajes_text_Area;
	private JLabel label_saldo_origen;
	private JLabel label_saldo_destino;
	private JButton btnTransferir;
	
	
	private IAccountServicio accountServicio;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TxWindow frame = new TxWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public TxWindow() {

		accountServicio = new AccountServicio();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 847, 772);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel panel = new JPanel();
		panel.setBounds(8, 8, 821, 500);
		contentPane.add(panel);
		panel.setLayout(null);

		JLabel lblNCuentaOrigen = new JLabel("Nº cuenta origen:");
		lblNCuentaOrigen.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNCuentaOrigen.setBounds(51, 55, 162, 21);
		panel.add(lblNCuentaOrigen);

		JLabel label_cuenta_destino = new JLabel("Nº cuenta destino:");
		label_cuenta_destino.setFont(new Font("Tahoma", Font.PLAIN, 12));
		label_cuenta_destino.setBounds(51, 84, 162, 21);
		panel.add(label_cuenta_destino);

		textFieldOrigen = new JTextField();

		textFieldOrigen.setBounds(195, 58, 86, 17);
		panel.add(textFieldOrigen);
		textFieldOrigen.setColumns(10);

		textFieldDestino = new JTextField();
		textFieldDestino.setColumns(10);
		textFieldDestino.setBounds(195, 87, 86, 17);
		panel.add(textFieldDestino);

		JLabel label_info_saldo_cuenta_origen = new JLabel("Saldo actual cuenta origen:");
		label_info_saldo_cuenta_origen.setFont(new Font("Tahoma", Font.PLAIN, 12));
		label_info_saldo_cuenta_origen.setBounds(332, 55, 162, 21);
		panel.add(label_info_saldo_cuenta_origen);

		label_saldo_origen = new JLabel("");
		label_saldo_origen.setForeground(new Color(0, 0, 255));
		label_saldo_origen.setFont(new Font("Tahoma", Font.PLAIN, 12));
		label_saldo_origen.setBounds(526, 55, 162, 21);
		panel.add(label_saldo_origen);

		JLabel label_info_saldo_cuenta_destino = new JLabel("Saldo actual cuenta destino:");
		label_info_saldo_cuenta_destino.setFont(new Font("Tahoma", Font.PLAIN, 12));
		label_info_saldo_cuenta_destino.setBounds(332, 84, 162, 21);
		panel.add(label_info_saldo_cuenta_destino);

		btnTransferir = new JButton("Transferir");
		btnTransferir.setEnabled(false);
		btnTransferir.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnTransferir.setBounds(208, 183, 120, 31);
		panel.add(btnTransferir);

		JLabel label_cuenta_destino_1 = new JLabel("Cantidad a transferir: ");
		label_cuenta_destino_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		label_cuenta_destino_1.setBounds(51, 131, 162, 21);
		panel.add(label_cuenta_destino_1);

		textField_cantidad_transfer = new JTextField();

		textField_cantidad_transfer.setColumns(10);
		textField_cantidad_transfer.setBounds(195, 134, 86, 17);
		panel.add(textField_cantidad_transfer);

		label_saldo_destino = new JLabel("");
		label_saldo_destino.setForeground(new Color(0, 0, 255));
		label_saldo_destino.setFont(new Font("Tahoma", Font.PLAIN, 12));
		label_saldo_destino.setBounds(536, 84, 162, 21);
		panel.add(label_saldo_destino);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(19, 264, 669, 228);
		panel.add(scrollPane);

		mensajes_text_Area = new JTextArea();
		scrollPane.setViewportView(mensajes_text_Area);
		mensajes_text_Area.setEditable(false);
		mensajes_text_Area.setText("Panel de mensajes");
		mensajes_text_Area.setForeground(new Color(255, 0, 0));
		mensajes_text_Area.setFont(new Font("Monospaced", Font.PLAIN, 13));

		// Eventos
		textField_cantidad_transfer.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				enableBtnTransferir();
			}
		});

		// Mostrar saldo cuando se presiona ENTER y existe un id de cuenta válido
		KeyAdapter enterKeyAdapter = new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				String textIntroducido = "";

				int key = e.getKeyCode();
				// Cuando se presiona la tecla enter, intentamos obtener el saldo de la cuenta
				if ((key == KeyEvent.VK_ENTER)) {

					textIntroducido = ((JTextField) e.getSource()).getText().trim();
					try {
						int accId = Integer.parseInt(textIntroducido);

						Account account = accountServicio.findAccountById(accId);

						if (e.getSource() == textFieldOrigen) {
							label_saldo_origen.setText(String.valueOf(account.getAmount()));
						} else if (e.getSource() == textFieldDestino) {
							label_saldo_destino.setText(String.valueOf(account.getAmount()));
						}

					} catch (NumberFormatException nfe) {

						addMensaje(true, "Introduzca un número entero");
						clearSaldoText(e);

					} catch (InstanceNotFoundException infe) {

						addMensaje(true, "La cuenta: " + textIntroducido + " no existe");
						clearSaldoText(e);
					} catch (Exception ex) {
						System.out.println("Ha ocurrido una excepción: " + ex.getMessage());
						addMensaje(true, "Ha ocurrido un error y no se ha podido recuperar la cuenta con id: "
								+ textIntroducido);

					}
				} else {
					clearSaldoText(e);
				}
				enableBtnTransferir();
			}
		};
		// Mostrar saldo cuando se presiona ENTER y existe un id de cuenta válido
		textFieldOrigen.addKeyListener(enterKeyAdapter);
		textFieldDestino.addKeyListener(enterKeyAdapter);

		// Llama al servicio para transferir la cantidad entre cuentas
		btnTransferir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String texto_intro = textField_cantidad_transfer.getText();
				double cantidad_transferir;

				try {
					cantidad_transferir = Double.parseDouble(texto_intro.trim());

					int accIdOrigen = getAccIdOrigen();
					int accIdDestino = getAccIdDestino();
					if ((accIdOrigen != -1) && (accIdDestino != -1)) {
						AccMovement accMovement = accountServicio.transferir(accIdOrigen, accIdDestino,
								cantidad_transferir);
						if (accMovement != null) {
							addMensaje(true, "Se ha producido el movimiento: " + accMovement);

							Account destino = accountServicio.findAccountById(accIdDestino);
							Account origen = accountServicio.findAccountById(accIdOrigen);

							label_saldo_origen.setText(String.valueOf(origen.getAmount()));
							label_saldo_destino.setText(String.valueOf(destino.getAmount()));
						}
					}

				} catch (NumberFormatException nfe) {
					addMensaje(true, "Introduzca un número con hasta dos decimales");
				} catch (SaldoInsuficienteException sie) {
					addMensaje(true, "No hay saldo suficiente");
				} catch (InstanceNotFoundException infe) {
					addMensaje(true, "No se ha encontrado una de las cuentas. No se ha podido realizar la operación.");
				} catch (Exception ex) {
					System.out.println("Ha ocurrido una excepción: " + ex.getMessage());
					addMensaje(true, "Ha ocurrido un error inesperado");

				}
			}
		});

	}

	private void addMensaje(boolean keepText, String msg) {
		String oldText = "";
		if (keepText) {
			oldText = mensajes_text_Area.getText();

		}
		oldText = oldText + "\n" + msg;
		mensajes_text_Area.setText(oldText);

	}

	private int getAccIdOrigen() {
		return getAccId(textFieldOrigen, "origen");
	}

	private int getAccIdDestino() {
		return getAccId(textFieldDestino, "destino");
	}

	private int getAccId(JTextField textField, String msg_identificador) {
		try {
			String texto = textField.getText();
			int accId = Integer.parseInt(texto.trim());
			return accId;
		} catch (NumberFormatException nfe) {
			addMensaje(true, "Introduzca un número entero para la cuenta " + msg_identificador);
			return -1;
		}
	}

	private void clearSaldoText(KeyEvent e) {
		if (e.getSource() == textFieldOrigen) {
			label_saldo_origen.setText("");
		} else if (e.getSource() == textFieldDestino) {
			label_saldo_destino.setText("");
		}
	}

	private void enableBtnTransferir() {
		boolean valor = !(textFieldOrigen.getText().trim().equals(""))
				&& !(textFieldDestino.getText().trim().equals(""))

				&& !(textField_cantidad_transfer.getText().equals(""));
		// Son valores aparentemente correctos
		if (valor) {
			try {
				int enteroOrigen = Integer.parseInt(textFieldOrigen.getText().trim());
				int enteroDest = Integer.parseInt(textFieldDestino.getText().trim());
				double cantidad = Double.parseDouble(textField_cantidad_transfer.getText().trim());

				valor = (enteroOrigen > 0) && (enteroDest > 0) && (cantidad > 0);
				btnTransferir.setEnabled(valor);
			} catch (NumberFormatException ex) {
				btnTransferir.setEnabled(false);
			}
		} else {
			btnTransferir.setEnabled(false);
		}

	}
}
