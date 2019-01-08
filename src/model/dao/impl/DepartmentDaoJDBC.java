package model.dao.impl;

import db.DB;
import db.DbException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentDaoJDBC implements DepartmentDao {
  
    private Connection conn;
    
    public DepartmentDaoJDBC(Connection conn){
        this.conn = conn; 
    }

    @Override
    public void insert(Department obj) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(
                    "INSERT INTO department (Name) values (?);",
                    Statement.RETURN_GENERATED_KEYS);
            
            ps.setString(1, obj.getName());
            int row = ps.executeUpdate();
            if(row > 0){
                ResultSet rs = ps.getGeneratedKeys();
                if(rs.next()){
                    int id = rs.getInt(1);
                    obj.setId(id);
                }
                DB.closeResultSet(rs);
            }
            else{
                throw new DbException("Unexpected error to insert new department!");
            }
        } 
        catch (SQLException e) {
            throw new DbException( e.getMessage() );
        }
        finally{
            DB.closeStatement(ps);
        }
    }

    @Override
    public void update(Department obj) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("UPDATE department SET Name = ? WHERE Id = ? ;");
            ps.setString(1, obj.getName());
            ps.setInt(2, obj.getId());
            int rows = ps.executeUpdate();
            System.out.println("Update sussecfully! Rows affected: " + rows);
        } 
        catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    @Override
    public void deleteById(Integer id) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("DELETE FROM department WHERE Id = ?;");
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            if(rows == 0){
                throw new DbException("Error on deleting by id");
            }
        } 
        catch (SQLException e) {
            throw new DbException( e.getMessage());
        }
        finally{
            DB.closeStatement(ps);
        }            
    }

    @Override
    public Department findById(Integer id) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement("SELECT * FROM department WHERE Id = ?;");
            st.setInt(1, id);
            rs = st.executeQuery();
            if(rs.next()){
                Department department = new Department();
                department.setId( rs.getInt("Id") );
                department.setName( rs.getString("Name"));
                return department;
            }
            throw new DbException("No department founded");
        } 
        catch (SQLException e) {
            throw new DbException( e.getMessage() );
        }
        finally{
            DB.closeResultSet(rs);
            DB.closeStatement(st);
        }
    }

    @Override
    public List<Department> findAll() {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Department> list = new ArrayList<>();
        try {
            ps = conn.prepareStatement("SELECT * FROM department ORDER BY Id ASC;");
            rs = ps.executeQuery();
            while(rs.next()){
                list.add(new Department( rs.getInt("Id"), rs.getString("Name")));
            }
            return list;
        } 
        catch (SQLException e) {
            throw new DbException( e.getMessage() );
        }
        finally{
            DB.closeResultSet(rs);
            DB.closeStatement(ps);
        }
    }  
}
