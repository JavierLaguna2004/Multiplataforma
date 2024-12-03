/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Formularios;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.Image;
import javax.swing.JOptionPane;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author PC-Victoria
 */
public class MantenimientosUsuarios {
     private ConexionSQL c;

    public MantenimientosUsuarios() {
        this.c = new ConexionSQL();
}
     public void agregarUsuario(JTable tabla,String usuario, String contrasena, int idRol, String nombreEmpleado,int idempleado){
     
         if (usuario.isEmpty()) {
            JOptionPane.showMessageDialog(null, "El campo 'Usuario' es obligatorio.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        DefaultTableModel modelotabla=(DefaultTableModel) tabla.getModel();
        int columnas;
        ResultSet rs;
        ResultSetMetaData rsmd;
       try{
           Connection conn = c.establecerConexion();
            String sql = "{CALL sp_AgregarUsuario(?, ?, ?, ?,?)}";
            CallableStatement stmt = conn.prepareCall(sql);
            stmt.setString(1, usuario); // Parámetro @Usuario
            stmt.setString(2, contrasena); // Parámetro @Contrasena
            stmt.setInt(3, idRol); // Parámetro @IdRol
            stmt.setString(4, nombreEmpleado); // Parámetro @IdEmpleado
            stmt.setInt(5, idempleado);
               rs=stmt.executeQuery();
               rsmd=rs.getMetaData();
               
               columnas=rsmd.getColumnCount();
           while(rs.next()){
               Object[] fila = new Object[columnas];
               for(int i =0; i<columnas;i++){
               fila[i]=rs.getObject(i+1);
               
               }
               modelotabla.addRow(fila);
            
           }
       }catch(Exception ex){
                   JOptionPane.showMessageDialog(null,ex.toString());
       }
     }
     
     public void modificarUsuario(JTable tabla,int idUsuario, String usuario, String contrasena, int idRol, int idEmpleado) {
        if (usuario.isEmpty()) {
            JOptionPane.showMessageDialog(null, "El campo 'Usuario' es obligatorio.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
  DefaultTableModel modelotabla=(DefaultTableModel) tabla.getModel();
        int columnas;
        ResultSet rs;
        ResultSetMetaData rsmd;
       try{
           Connection conn = c.establecerConexion();
            String sql = "{CALL sp_ModificarUsuario(?, ?, ?, ?, ?)}";
            CallableStatement stmt = conn.prepareCall(sql);
            stmt.setInt(1, idUsuario); // Parámetro @IdUsuario
            stmt.setString(2, usuario); // Parámetro @Usuario
            stmt.setString(3, contrasena); // Parámetro @Contrasena
            stmt.setInt(4, idRol); // Parámetro @IdRol
            stmt.setInt(5, idEmpleado); // Parámetro @IdEmpleado

               rs=stmt.executeQuery();
               rsmd=rs.getMetaData();
               
               columnas=rsmd.getColumnCount();
           while(rs.next()){
               Object[] fila = new Object[columnas];
               for(int i =0; i<columnas;i++){
               fila[i]=rs.getObject(i+1);
               
               }
               modelotabla.addRow(fila);
            
           }
       }catch(Exception ex){
                   JOptionPane.showMessageDialog(null,ex.toString());
       }
       
    }
     
      public void eliminarUsuario(JTable tabla,int idUsuario) {
       DefaultTableModel modelotabla=(DefaultTableModel) tabla.getModel();
        int columnas;
        ResultSet rs;
        ResultSetMetaData rsmd;
          try{
           Connection conn = c.establecerConexion();
            String sql = "{CALL sp_ModificarUsuario(?, ?, ?, ?, ?)}";
            CallableStatement stmt = conn.prepareCall(sql);
            stmt.setInt(1, idUsuario); // Parámetro @IdUsuario
       

               rs=stmt.executeQuery();
               rsmd=rs.getMetaData();
               
               columnas=rsmd.getColumnCount();
           while(rs.next()){
               Object[] fila = new Object[columnas];
               for(int i =0; i<columnas;i++){
               fila[i]=rs.getObject(i+1);
               
               }
               modelotabla.addRow(fila);
            
           }
       }catch(Exception ex){
                   JOptionPane.showMessageDialog(null,ex.toString());
       }
    }
      
      
      public void cargartabla(JTable tabla){
      
       DefaultTableModel modelotabla=(DefaultTableModel) tabla.getModel();
       modelotabla.setRowCount(0);
        int columnas;
        ResultSet rs;
        ResultSetMetaData rsmd;
          try{
           Connection conn = c.establecerConexion();
            String sql = "{CALL sp_mostrarusuarios}";
            CallableStatement stmt = conn.prepareCall(sql);
           
               rs=stmt.executeQuery();
               rsmd=rs.getMetaData();
               
               columnas=rsmd.getColumnCount();
           while(rs.next()){
               Object[] fila = new Object[columnas];
               for(int i =0; i<columnas;i++){
               fila[i]=rs.getObject(i+1);
               
               }
               modelotabla.addRow(fila);
            
           }
       }catch(Exception ex){
                   JOptionPane.showMessageDialog(null,ex.toString());
       }
      
      
      }
}

