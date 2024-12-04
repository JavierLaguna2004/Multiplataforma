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
public class MantenimientoPlanillas {
    ConexionSQL cone = new ConexionSQL();
    
    public DefaultComboBoxModel llenarempleados(){
        
        DefaultComboBoxModel modelo = new DefaultComboBoxModel();
        
        modelo.addElement(new ComboEmpleado(0, "Seleccione: ")); 
        try{
            Connection con = cone.establecerConexion();
            CallableStatement cmd = con.prepareCall("{CALL sp_empleados}");
            ResultSet rs = cmd.executeQuery();
             while(rs.next()){
                  int idEmpleado = rs.getInt(1);
                String correo = rs.getString(2);
                modelo.addElement(new ComboEmpleado(idEmpleado, correo));
        }
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null,ex.toString());
        }
        return modelo;
    }
    
    public void mantenimientoplanillas(int codigo, String fecha,double salario,int idempleado, String accion)
     {
         try{
             Connection con = cone.establecerConexion();
             CallableStatement cmd = con.prepareCall("{CALL sp_mantenimientoplanillas(?,?,?,?,?)}");
         
             cmd.setInt(1,codigo);
             cmd.setString(2,fecha);
             cmd.setDouble(3,salario);
             cmd.setInt(4,idempleado);
             cmd.setString(5,accion);
             
             cmd.execute();
         }catch(Exception ex){
             
             JOptionPane.showMessageDialog(null, ex.toString());}
     }
    
    public void cargartablaPlanillas(JTable tabla, int codigo, String fecha,double salario,int idempleado, String accion) {
        DefaultTableModel modelotabla = (DefaultTableModel) tabla.getModel();

        modelotabla.setRowCount(0);

        ResultSet rs;
        ResultSetMetaData rsmd;
        int columnas;

        try {
            Connection con = cone.establecerConexion();
            CallableStatement cmd = con.prepareCall("{CALL sp_mantenimientoplanillas(?,?,?,?,?)}");

            cmd.setInt(1,codigo);
             cmd.setString(2,fecha);
             cmd.setDouble(3,salario);
             cmd.setInt(4,idempleado);
             cmd.setString(5,accion);

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
