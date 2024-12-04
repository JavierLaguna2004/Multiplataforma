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
 * @author carlo
 */
public class MantenimientoProductos {
     ConexionSQL conexion = new ConexionSQL();
     
      public void cargartabla(JTable tabla,int codigo,String Nombre,String Descripcion,Double precio,int stock,int idproveedor,String accion){
    DefaultTableModel modelotabla= (DefaultTableModel) tabla.getModel();
    modelotabla.setRowCount(0);
  ResultSet rs;
    ResultSetMetaData rsmd;
    int columnas;
    
    
    try{
     
         Connection con =conexion.establecerConexion();
         CallableStatement cmd=con.prepareCall("{CALL mantenimientoProductos(?,?,?,?,?,?,?)}");
         cmd.setInt(1, codigo);
         cmd.setString(2, Nombre);
          cmd.setString(3, Descripcion);
           cmd.setDouble(4, precio);
            cmd.setInt(5, stock);
            cmd.setInt(6, idproveedor);
             cmd.setString(7,accion);
         
         rs=cmd.executeQuery();
         rsmd=rs.getMetaData();
         columnas=rsmd.getColumnCount();
         while(rs.next()){
         
         Object[] fila= new Object [columnas];
         for(int i  =0;i<columnas;i++){
         
         fila[i]=rs.getObject(i+1);
         }
         modelotabla.addRow(fila);
         }
            
        
    }catch(Exception ex){
     JOptionPane.showMessageDialog(null, ex.toString());
    }
      }
      
        public void BuscarProductos(JTable tabla,String Dato,String accion){
    DefaultTableModel modelotabla= (DefaultTableModel) tabla.getModel();
    modelotabla.setRowCount(0);
  ResultSet rs;
    ResultSetMetaData rsmd;
    int columnas;
     try{
     //jj
         Connection con =conexion.establecerConexion();
         CallableStatement cmd=con.prepareCall("{CALL sp_busquedaproductos(?,?)}");
         cmd.setString(1, Dato);
         cmd.setString(2, accion);
           
         rs=cmd.executeQuery();
         rsmd=rs.getMetaData();
         columnas=rsmd.getColumnCount();
         while(rs.next()){
         
         Object[] fila= new Object [columnas];
         for(int i  =0;i<columnas;i++){
         
         fila[i]=rs.getObject(i+1);
         }
         modelotabla.addRow(fila);
         }

     
         
     }catch(Exception ex){
     
     JOptionPane.showMessageDialog(null, ex.toString());
     }
    
    }
      
      
      public void mantenimientoproductos(int codigo,String Nombre,String Descripcion,Double precio,int stock,int idproveedor,String accion){
    
    
 
    try{
     
         Connection con =conexion.establecerConexion();
         CallableStatement cmd=con.prepareCall("{CALL mantenimientoProductos(?,?,?,?,?,?,?)}");
         cmd.setInt(1, codigo);
         cmd.setString(2, Nombre);
          cmd.setString(3, Descripcion);
           cmd.setDouble(4, precio);
            cmd.setInt(5, stock);
            cmd.setInt(6, idproveedor);
             cmd.setString(7,accion);
         
         cmd.executeQuery();
         
            
        
    }catch(Exception ex){
     JOptionPane.showMessageDialog(null, ex.toString());
    }
    
    
    
    }
       public DefaultComboBoxModel llenarproveedores(){
    
        DefaultComboBoxModel modelo = new DefaultComboBoxModel();
    modelo.addElement("Seleccione");
    try{
        Connection con= conexion.establecerConexion();
        CallableStatement cmd=con.prepareCall("{CALL sp_proveedores}");
        ResultSet rs= cmd.executeQuery();
        while (rs.next()){
        modelo.addElement(rs.getString(2));
        
        }
    
    }catch(Exception ex){
    
    JOptionPane.showMessageDialog(null, ex.toString());
    }
        return modelo;
    
    }
}
