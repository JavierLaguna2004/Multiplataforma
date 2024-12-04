/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Formularios;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author joser
 */
public class MantenimientoClientes {
    
    ConexionSQL cone = new ConexionSQL();
        public DefaultComboBoxModel llenarbusquedacb(){
        DefaultComboBoxModel modelo = new DefaultComboBoxModel();
        modelo.addElement("Seleccione");
        modelo.addElement("Nombre");
        modelo.addElement("Apellido");
        modelo.addElement("Numero");
        modelo.addElement("Correo");
        return modelo;
    }
    
    public void mantenimientosclientes(int codigo, String nombre,String apellido,String correo,String telefono, String direccion,String accion)
     {
         try{
             Connection con = cone.establecerConexion();
             CallableStatement cmd = con.prepareCall("{CALL mantenimientoPacientes(?,?,?,?,?,?,?)}");
         
             cmd.setInt(1,codigo);
             cmd.setString(2,nombre);
             cmd.setString(3,apellido);
             cmd.setString(4,correo);
             cmd.setString(5,telefono);
             cmd.setString(6,direccion);
             cmd.setString(7,accion);
             
             cmd.execute();
         }catch(Exception ex){
             
             JOptionPane.showMessageDialog(null, ex.toString());}
     }
    
public void cargartablaClientes(JTable tabla, int codigo, String nombre, String apellido, String correo, String telefono, String direccion, String accion) {
        DefaultTableModel modelotabla = (DefaultTableModel) tabla.getModel();

        modelotabla.setRowCount(0);

        ResultSet rs;
        ResultSetMetaData rsmd;
        int columnas;

        try {
            Connection con = cone.establecerConexion();
            CallableStatement cmd = con.prepareCall("{CALL mantenimientoPacientes(?,?,?,?,?,?,?)}");

             cmd.setInt(1,codigo);
             cmd.setString(2,nombre);
             cmd.setString(3,apellido);
             cmd.setString(4,correo);
             cmd.setString(5,telefono);
             cmd.setString(6,direccion);
             cmd.setString(7,accion);

            rs = cmd.executeQuery();
            rsmd = rs.getMetaData();
            columnas = rsmd.getColumnCount();

            while (rs.next()) {
                Object[] filas = new Object[columnas];
                for (int i = 0; i < columnas; i++) {
                    filas[i] = rs.getObject(i + 1);
                }
                modelotabla.addRow(filas);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.toString());
        }
    }
public void cargartablafiltro(JTable tabla, String parametro, String accion) {
DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
    modelo.setRowCount(0); // Limpiar la tabla antes de cargar nuevos datos

    ResultSet rs;

    try {
        Connection con = cone.establecerConexion();
        CallableStatement cmd = con.prepareCall("{CALL sp_BuscarPacientes(?, ?)}");
        cmd.setString(1, accion);
        cmd.setString(2, parametro);

        rs = cmd.executeQuery();

        
        while (rs.next()) {
            Object[] fila = {
                rs.getInt("Id"),        
                rs.getString("Nombre"),
                rs.getString("Apellido"), 
                rs.getString("Correo"), 
                rs.getString("Telefono") 
            };
            modelo.addRow(fila); 
        }
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(null, "Error al cargar los datos: " + ex.toString());
    }
}
   
}