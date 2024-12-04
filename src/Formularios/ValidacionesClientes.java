/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Formularios;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import javax.swing.JOptionPane;
/**
 *
 * @author sadri
 */
public class ValidacionesClientes {
       public static boolean validarCampoVacio(String texto, String mensaje) {
        if (texto.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, mensaje);
            return false;
        }
        return true;
    }

   
public static boolean validarNumero(String texto, String mensaje) {
    try {
//numerofsafd
        if (texto.length() > 9) {
            JOptionPane.showMessageDialog(null, "El número no puede tener más de 9 dígitos.");
            return false;
        }


        Double.parseDouble(texto);
        return true;
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(null, mensaje);
        return false;
    }
}


public static boolean validarCorreo(String correo, String mensaje) {
    String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    if (!correo.matches(regex)) {
        JOptionPane.showMessageDialog(null, mensaje);
        return false;
    }
    return true;
}

    
    public static boolean validarLongitud(String texto, int longitudMinima, String mensaje) {
        if (texto.length() < longitudMinima) {
            JOptionPane.showMessageDialog(null, mensaje);
            return false;
        }
        return true;
    }


public static boolean validarSoloLetras(String texto, String mensaje) {

    texto = texto.trim();
    

    if (texto.isEmpty()) {
        JOptionPane.showMessageDialog(null, mensaje);
        return false;
    }
    

    if (!texto.matches("[a-zA-Z\\s]+")) {
        JOptionPane.showMessageDialog(null, mensaje);
        return false;
    }
    
    return true;
}

}