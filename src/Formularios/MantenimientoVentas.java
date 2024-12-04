/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Formularios;
import java.sql.Connection;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTable;
import java.sql.*;

public class MantenimientoVentas {
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
     
     public DefaultComboBoxModel llenarpacientes(){
        
        DefaultComboBoxModel modelo = new DefaultComboBoxModel();
        
        modelo.addElement("Seleccione: ");
        try{
            Connection con = cone.establecerConexion();
            CallableStatement cmd = con.prepareCall("{CALL sp_pacientes}");
            ResultSet rs = cmd.executeQuery();
             while(rs.next()){
                 int idPaciente = rs.getInt(1);
                String nombrePaciente = rs.getString(2);
                modelo.addElement(new ComboPaciente(idPaciente, nombrePaciente));
        }
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null,ex.toString());
        }
        return modelo;
    }
     
     public DefaultComboBoxModel llenarproductos(){
        
        DefaultComboBoxModel modelo = new DefaultComboBoxModel();
        
        modelo.addElement(new ComboEmpleado(0, "Seleccione: ")); 
        try{
            Connection con = cone.establecerConexion();
            CallableStatement cmd = con.prepareCall("{CALL sp_productos}");
            ResultSet rs = cmd.executeQuery();
             while(rs.next()){
                  int idProducto = rs.getInt(1);
                String nombreProducto = rs.getString(2);
                modelo.addElement(new ComboProducto(idProducto, nombreProducto));
        }
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null,ex.toString());
        }
        return modelo;
    }
    
    public void mantenimientosventas(int codigo, String fecha,double montototal,String metodopago, int idpaciente,int idempleado,String accion)
     {
         try{
             Connection con = cone.establecerConexion();
             CallableStatement cmd = con.prepareCall("{CALL sp_mantenimientoVentas(?,?,?,?,?,?,?)}");
         
             cmd.setInt(1,codigo);
             cmd.setString(2,fecha);
             cmd.setDouble(3,montototal);
             cmd.setString(4,metodopago);
             cmd.setInt(5,idpaciente);
             cmd.setInt(6,idempleado);
             cmd.setString(7,accion);
             
             cmd.execute();
         }catch(Exception ex){
             
             JOptionPane.showMessageDialog(null, ex.toString());}
     }
    
    public void cargartablaVentas(JTable tabla, int codigo, String fecha,double montototal,String metodopago, int idpaciente,int idempleado,String accion) {
        DefaultTableModel modelotabla = (DefaultTableModel) tabla.getModel();

        modelotabla.setRowCount(0);

        ResultSet rs;
        ResultSetMetaData rsmd;
        int columnas;

        try {
            Connection con = cone.establecerConexion();
            CallableStatement cmd = con.prepareCall("{CALL sp_mantenimientoVentas(?,?,?,?,?,?,?)}");

             cmd.setInt(1,codigo);
             cmd.setString(2,fecha);
             cmd.setDouble(3,montototal);
             cmd.setString(4,metodopago);
             cmd.setInt(5,idpaciente);
             cmd.setInt(6,idempleado);
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
}
