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
public class MantenimientoEmpleados {
    ConexionSQL cone = new ConexionSQL();
    
     public DefaultComboBoxModel llenarsucursal(){
        
        DefaultComboBoxModel modelo = new DefaultComboBoxModel();
        
        modelo.addElement("Seleccione: ");
        try{
            Connection con = cone.establecerConexion();
            CallableStatement cmd = con.prepareCall("{CALL sp_sucursal}");
            ResultSet rs = cmd.executeQuery();
             while(rs.next()){
                 int idSucursal = rs.getInt(1);
                String nombreSucursal = rs.getString(2);
                modelo.addElement(new ComboSucursal(idSucursal, nombreSucursal));
        }
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null,ex.toString());
        }
        return modelo;
    }
     
     
     public DefaultComboBoxModel llenarpuesto(){
        
        DefaultComboBoxModel modelo = new DefaultComboBoxModel();
        
        modelo.addElement(new ComboPuesto(0, "Seleccione: ")); 
        try{
            Connection con = cone.establecerConexion();
            CallableStatement cmd = con.prepareCall("{CALL sp_puesto}");
            ResultSet rs = cmd.executeQuery();
             while(rs.next()){
                  int idPuesto = rs.getInt(1);
                String nombrePuesto = rs.getString(2);
                modelo.addElement(new ComboPuesto(idPuesto, nombrePuesto));
        }
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null,ex.toString());
        }
        return modelo;
    }
    
    public void mantenimientosempleados(int codigo, String nombre,String apellido,String correo, String direccion, String Jornada, int idSucursal, int idPuesto,String accion)
     {
         try{
             Connection con = cone.establecerConexion();
             CallableStatement cmd = con.prepareCall("{CALL sp_mantenimientoEmpleados(?,?,?,?,?,?,?,?,?)}");
         
             cmd.setInt(1,codigo);
             cmd.setString(2, nombre);
             cmd.setString(3, apellido);
             cmd.setString(4, correo);
             cmd.setString(5, direccion);
             cmd.setString(6, Jornada);
             cmd.setInt(7, idSucursal);
             cmd.setInt(8, idPuesto);
             cmd.setString(9, accion);
             
             cmd.execute();
         }catch(Exception ex){
             
             JOptionPane.showMessageDialog(null, ex.toString());}
     }
    
    public void cargartablaEmpleados(JTable tabla, int codigo, String nombre,String apellido,String correo, String direccion, String Jornada, int idSucursal, int idPuesto,String accion) {
        DefaultTableModel modelotabla = (DefaultTableModel) tabla.getModel();

        modelotabla.setRowCount(0);

        ResultSet rs;
        ResultSetMetaData rsmd;
        int columnas;

        try {
            Connection con = cone.establecerConexion();
            CallableStatement cmd = con.prepareCall("{CALL sp_mantenimientoEmpleados(?,?,?,?,?,?,?,?,?)}");
         
             cmd.setInt(1,codigo);
             cmd.setString(2, nombre);
             cmd.setString(3, apellido);
             cmd.setString(4, correo);
             cmd.setString(5, direccion);
             cmd.setString(6, Jornada);
             cmd.setInt(7, idSucursal);
             cmd.setInt(8, idPuesto);
             cmd.setString(9, accion);

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
}
