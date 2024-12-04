/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Formularios;

import java.sql.Connection;
import java.sql.PreparedStatement; // Corrected capitalization
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement; // Corrected capitalization
import javax.swing.JOptionPane;


public class ConexionSQL {
    Connection conectar = null;
    
    String usuario = "usrsql1";
    String contrasena = "123456";
    String bd = "SistemaFacturacionLaboratorio";
    String ip = "SALVADORPCS\\SQLEXPRESS";
    String puerto = "1433";
    
    String cadena = "jdbc:sqlserver://" + ip + ":" + puerto + ";databaseName=" + bd+";"+"encrypt=true; trustServerCertificate=true;";
    
    public Connection establecerConexion() {
        try {
            conectar = DriverManager.getConnection(cadena, usuario, contrasena);
            //JOptionPane.showMessageDialog(null, "Se conectó a la base de datos");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al conectar a la base de datos: " + e.toString());
        }

        return conectar;
    }
}
