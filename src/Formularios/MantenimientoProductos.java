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
public class MantenimientoProductos {
    ConexionSQL cone = new ConexionSQL();
    
     public DefaultComboBoxModel llenarproveedores(){
        
        DefaultComboBoxModel modelo = new DefaultComboBoxModel();
        
        modelo.addElement("Seleccione: ");
        try{
            Connection con = cone.establecerConexion();
            CallableStatement cmd = con.prepareCall("{CALL sp_proveedores}");
            ResultSet rs = cmd.executeQuery();
             while(rs.next()){
                 int idProveedor = rs.getInt(1);
                String nombreProveedor = rs.getString(2);
                modelo.addElement(new ComboProveedor(idProveedor, nombreProveedor));
        }
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null,ex.toString());
        }
        return modelo;
    }
     
     public void mantenimientosproductos(int codigo, String nombre,String descripcion,double precio, int stock,int idProveedor,String accion)
     {
         try{
             Connection con = cone.establecerConexion();
             CallableStatement cmd = con.prepareCall("{CALL sp_mantenimientoProductos(?,?,?,?,?,?,?)}");
         
             cmd.setInt(1,codigo);
             cmd.setString(2,nombre);
             cmd.setString(3,descripcion);
             cmd.setDouble(4,precio);
             cmd.setInt(5,stock);
             cmd.setInt(6,idProveedor);
             cmd.setString(7,accion);
             
             cmd.execute();
         }catch(Exception ex){
             
             JOptionPane.showMessageDialog(null, ex.toString());}
     }
    
    public void cargartablaUsuarios(JTable tabla, int codigo, String nombre,String descripcion,double precio, int stock,int idProveedor,String accion) {
        DefaultTableModel modelotabla = (DefaultTableModel) tabla.getModel();

        modelotabla.setRowCount(0);

        ResultSet rs;
        ResultSetMetaData rsmd;
        int columnas;

        try {
            Connection con = cone.establecerConexion();
            CallableStatement cmd = con.prepareCall("{CALL sp_mantenimientoProductos(?,?,?,?,?,?,?)}");

             cmd.setInt(1,codigo);
             cmd.setString(2,nombre);
             cmd.setString(3,descripcion);
             cmd.setDouble(4,precio);
             cmd.setInt(5,stock);
             cmd.setInt(6,idProveedor);
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
