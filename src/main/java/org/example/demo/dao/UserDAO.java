//package org.example.demo.dao;
//
//import org.example.demo.DB.DBUtil;
//import org.example.demo.models.User;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//
//public class UserDAO {
//    public User findById(Long id) throws SQLException {
//        String sql = "SELECT * FROM users WHERE id = ?";
//        try (Connection conn = DBUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
//            stmt.setLong(1, id);
//            ResultSet rs = stmt.executeQuery();
//            if (rs.next()) {
//                return new User(
//                        rs.getLong("id"),
//                        rs.getString("name"),
//                        rs.getString("email")
//                );
//            }
//        }
//        return null;
//    }
//
//    public void save(User user) throws SQLException {
//        String sql = "INSERT INTO users (id, name, email) VALUES (?, ?, ?)";
//        try (Connection conn = DBUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
//            stmt.setLong(1, user.getId());
//            stmt.setString(2, user.getName());
//            stmt.setString(3, user.getEmail());
//            stmt.executeUpdate();
//        }
//    }
//
//    public User findByEmail(String email) throws SQLException {
//        String sql = "SELECT * FROM users WHERE email = ?";
//        try (Connection conn = DBUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
//            stmt.setString(1, email);
//            ResultSet rs = stmt.executeQuery();
//            if (rs.next()) {
//                long id = rs.getLong("id");
//                String name = rs.getString("name");
//                String userEmail = rs.getString("email");
//                return new User(id, name, userEmail);
//            }
//        }
//        return null;  // Return null if no user found
//    }
//
//}
