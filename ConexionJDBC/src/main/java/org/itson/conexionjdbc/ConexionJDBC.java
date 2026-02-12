/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package org.itson.conexionjdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author LABORATORIOS
 */
public class ConexionJDBC {
    
    public static void main(String[] args) {
        crearTabla();
        
        System.out.println("Inserts");
        insertar("juan","1234");
        insertar("maria","abcd");
        insertar("Pedro", "qwerty");
        insertar("Ana", "6789");
        
        System.out.println("Todos los clientes");
        obtenerClientes();
        
        System.out.println("Cliente por id");
        obtenerClienteID(1);
        obtenerClienteID(2);
        
        System.out.println("Actualizar cliente");
        actualizar(2, "Maria nueva", "newpass");
        obtenerClienteID(2);
        
        System.out.println("Eliminar");
        eliminar(4);
        obtenerClientes();
        
        System.out.println("Login normal");
        System.out.println("Login Maria nueva/newpass: "+ login("Maria nueva", "newpass"));
        System.out.println("Login Pedro/qwerty: "+ login("Pedro", "qwerty"));
        
        System.out.println("Login Seguro Maria nueva/newpass: "+ loginSeguro("Maria nueva", "newpass"));
        System.out.println("Login seguro Pedro/qwerty: "+ loginSeguro("Pedro", "qwerty"));
        
        System.out.println("Sql Injection");
        System.out.println("Intento Login normal: "+ login("Juan", "' OR '1'='1'"));
        System.out.println("Intento Login Seguro: "+ loginSeguro("Juan", "' OR '1'='1'"));
    }

    
    public static void crearTabla(){
        String sql = "create table if no exists clientes("+"id INT Auto_Increment Primary Key,"
                +"nombre Varchar(100),"+"password varchar(100)";
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/BD", "admin", "itson");
            Statement st = con.createStatement();
            st.execute(sql);
            
            System.out.println("Tabla creada");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static void obtenerClientes(){
        
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/BD", "admin", "itson");
            Statement st = con.createStatement();
            ResultSet rs =  st.executeQuery("Select id, nombre from clientes");
            
            while (rs.next()) {                
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                System.out.println(id+" | "+nombre);
            }
            
            System.out.println("Tabla creada");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static boolean login(String nombre, String password){
        String sql = "Select * from clientes Where nomnre='"+nombre+"'AND password='"+password+"'";
                
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/BD", "admin", "itson");
            Statement st = con.createStatement();
            ResultSet rs =  st.executeQuery(sql);
            
            
            return rs.next();
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        
    }
    
    public static boolean loginSeguro(String nombre, String password){
        String sql = "Select * from clientes Where nomnre=? AND password=?";
                
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/BD", "admin", "itson");
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, nombre);
            ps.setString(2, password);
            
            try(ResultSet rs =  ps.executeQuery(sql);){
                return rs.next();
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static void insertar(String nombre, String password){
        String sql = "insert into clientes (nombre, password) values(?,?)";
        
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/BD", "admin", "itson");
            PreparedStatement ps = con.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, nombre);
            ps.setString(2, password);
            
            int filas = ps.executeUpdate();
            
            if (filas>0) {
                try(ResultSet rs =  ps.getGeneratedKeys();){
                    while (rs.next()) {                        
                        System.out.println("Clientes con id insertado: " + rs.getInt(1));
                    }
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            
        }
    }
    
    public static void obtenerClienteID(int id){
        String sql = "Select id, nombre, password from clientes where id= ?";
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/BD", "admin", "itson");
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            
            try(ResultSet rs =  ps.executeQuery();){
                    if (rs.next()) {
                        System.out.println("ID: "+rs.getInt("id")+", Nombre: "+rs.getString("nombre")+", Password: "+ rs.getString("password"));
                        
                }else{
                        System.out.println("No se encontro un usuario");
                    }
                }
            
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static void actualizar(int id, String nombre, String password){
        String sql = "Update clientes set nombre=?, password=? where id= ?";
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/BD", "admin", "itson");
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, nombre);
            ps.setString(2, password);
            ps.setInt(3, id);
            
            int filas = ps.executeUpdate();
            
            if (filas > 0) {
                System.out.println("cliente eliminado con id: "+ id);
            }else{
                System.out.println("No se encontro un usuario");
            }
            
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static void eliminar(int id){
        String sql = "Delete from clientes where id= ?";
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/BD", "admin", "itson");
            PreparedStatement ps = con.prepareStatement(sql);
            
            ps.setInt(3, id);
            
            int filas = ps.executeUpdate();
            
            if (filas > 0) {
                System.out.println("cliente actualizado con id: "+ id);
            }else{
                System.out.println("No se encontro un usuario");
            }
            
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
}
