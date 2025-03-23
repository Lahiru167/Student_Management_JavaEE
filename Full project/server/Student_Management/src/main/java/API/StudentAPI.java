package API;

import DTO.StudntDTO;
import Util.ContextSingleton;
import Util.InMemory;
import com.google.gson.Gson;
import com.mysql.cj.protocol.a.SqlDateValueEncoder;

import javax.annotation.Resource;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(urlPatterns = "/student")
public class StudentAPI extends HttpServlet {



    @Resource(name = "connectionPool")
    DataSource pool;


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String option =req.getParameter("option");
        if (option!=null && option.equalsIgnoreCase("all")){
            getAll(req,resp);
            return;
        }

        Connection connection = null;
        PreparedStatement ps =null;
        ResultSet resultSet = null;

        try {
            connection = pool.getConnection();
            String sql = "SELECT *  FROM student WHERE id =?";
            ps = connection.prepareStatement(sql);
            ps.setString(1 ,req.getParameter("id"));

            //if the query is select
            resultSet = ps.executeQuery();
            if (resultSet.next()){
                StudntDTO obj = new StudntDTO();
                String name = resultSet.getString("name");
                String address = resultSet.getString("address");
                String gender = resultSet.getString("gender");
                String contact_number = resultSet.getString("contact_number");
                int age = resultSet.getInt("age");


                obj.setId(req.getParameter("id"));
                obj.setName(name);
                obj.setAddress(address);
                obj.setGender(gender);
                obj.setContact_number(contact_number);
                obj.setAge(age);

                resp.setContentType("application/json");
                String toSend = new Gson().toJson(obj);
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write(toSend);
                return;
            }
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write("Student not found");
        }catch (SQLException e){
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(e.getMessage());
        } finally {
            try {
                if (ps!=null && !ps.isClosed())ps.close();
                if (resultSet!=null && !resultSet.isClosed())resultSet.close();
                if (connection!=null && !connection.isClosed())connection.close();
            } catch (SQLException e){
                e.printStackTrace();
            }

        }

      /*  String id = req.getParameter("id");
        StudntDTO student = InMemory.getStudentById(id);
        if (student !=null){
            resp.setContentType("application/json");
            String toSend = new Gson().toJson(student);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(toSend);
            return;
        }*/


    }

    protected  void  getAll(HttpServletRequest req ,HttpServletResponse resp){
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs =null;
         try {
              connection = pool.getConnection();
              String sql = "SELECT * FROM student";
              ps = connection.prepareStatement(sql);

             rs = ps.executeQuery();

             List<StudntDTO> list = new ArrayList<>();
             while (rs.next()){
                 StudntDTO obj = new StudntDTO();
                 obj.setId(rs.getString("id"));
                 obj.setName(rs.getString("name"));
                 obj.setAddress(rs.getString("address"));
                 obj.setGender(rs.getString("contact_number"));
                 obj.setAge(rs.getInt("age"));
                 list.add(obj);
             }
             resp.setContentType("application/json");
             String toSend = new Gson().toJson(list);
             resp.setStatus(HttpServletResponse.SC_OK);
             resp.getWriter().write(toSend);


         } catch (SQLException | IOException e) {
             resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
             try {
                 resp.getWriter().println("Error"+e.getMessage());
             } catch (IOException ex) {
                 e.printStackTrace();
             }
             e.printStackTrace();
         }finally {
             try {
                 if(ps != null && !ps.isClosed())ps.close();
                 if(rs != null && !rs.isClosed())rs.close();
                 if(connection != null && !connection.isClosed())connection.close();
             } catch (SQLException e) {
                 e.printStackTrace();
             }
         }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String  header = req.getHeader("Content-Type");
        String status = "Student adding Failed : (";

        if(header.equals("application/json")){
            StudntDTO studentDTO = new Gson().fromJson(req.getReader(), StudntDTO.class);
            Connection connection = null;
            PreparedStatement ps = null;
            try {
                connection = pool.getConnection();
                ps = connection.prepareStatement("INSERT INTO student(id,name,address,gender,contact_number,age)VALUES (?,?,?,?,?,?)");
                ps.setString(1,studentDTO.getId());
                ps.setString(2,studentDTO.getName());
                ps.setString(3,studentDTO.getAddress());
                ps.setString(4,studentDTO.getGender());
                ps.setString(5,studentDTO.getContact_number());
                ps.setInt(6,studentDTO.getAge());

               //if the query  changes the db
               int affectedRows = ps.executeUpdate();
               status = affectedRows >0 ? "student added successfully ": status;

            } catch (SQLException e) {
                e.printStackTrace();
            }finally {
                try {
                    if (ps!=null && !ps.isClosed())ps.close();
                    if (connection!=null && !connection.isClosed())connection.close();


                }catch (SQLException e){
                    e.printStackTrace();
                }

            }
            //status = InMemory.saveStudent(studentDTO) ? "Student added successfully" : status;


           }

      //  List<StudntDTO> allStudent = InMemory.getAllStudent();

        resp.getWriter().println(status);

    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String header = req.getHeader("Content-Type");
        String status = "student update failed";
        if (header.equals("application/json")){
            StudntDTO  student = new Gson().fromJson(req.getReader(), StudntDTO.class);
            Connection connection = null;
            PreparedStatement ps = null;

            try {
                 connection = pool.getConnection();
                 ps = connection.prepareStatement("UPDATE student SET name=? , address =? , gender=?,contact_number=?, age =? WHERE id =?");
                 ps.setString(1,student.getName());
                 ps.setString(2,student.getAddress());
                 ps.setString(3,student.getGender());
                 ps.setString(4,student.getContact_number());
                 ps.setInt(5,student.getAge());
                 ps.setString(6,student.getId());

                int affectedRow = ps.executeUpdate();
                status = affectedRow >0 ? " student update success": status;
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().println("status"+status);


            } catch (SQLException e) {
                e.printStackTrace();
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().println("status"+status);
            }finally {
                try {
                    if (ps!=null && !ps.isClosed())ps.close();
                    if (connection!=null && !connection.isClosed())connection.close();

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String id =req.getParameter("id");
        Connection connection = null;
        PreparedStatement ps = null;

        try {
            connection = pool.getConnection();
            ps = connection.prepareStatement("DELETE FROM student WHERE id=?");
            ps.setString(1,id);

            int affectedRows = ps.executeUpdate();
            if (affectedRows>0){
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().println("status : Delete");
                return;

            }
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().println("Status : Not Found ");

        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().println("Exception Occured"+e.getMessage());
            e.printStackTrace();
        }finally {
            try {
                if (connection!=null && !connection.isClosed())connection.close();
                if (ps!=null && !ps.isClosed())ps.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    }
}


