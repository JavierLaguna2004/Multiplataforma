/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Formularios;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class ConexionSQL {
    Connection con;
    public Connection obtenerConexion(){
        try{
            String db = "jdbc";
            con = DriverManager.getConnection(db,"root","");
            return con;
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null, ex.toString());
        }
        return null;
    }
}
