package kr.or.connect.jdbcexam.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import kr.or.connect.jdbcexam.dao.common.JdbcContext;
import kr.or.connect.jdbcexam.dto.Role;

public class RoleDao {
  JdbcContext jdbcContext = new JdbcContext();

  public Role getRole(final Integer roleId) {
    String sql = "SELECT role_id, description FROM role WHERE role_id = ?";

    try {
      return jdbcContext.executeQueryWithStatementStrategy(conn -> {
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, roleId);
        return ps;
      }, rs -> {
        if (!rs.next())
          return null;
        int roldId = rs.getInt("role_id");
        String description = rs.getString("description");
        return new Role(roldId, description);
      });
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  public List<Role> getRoles() {
    String sql = "SELECT role_id, description FROM role";

    try {
      return jdbcContext.executeSql(sql, rs -> {
        List<Role> list = new ArrayList<Role>();
        while (rs.next()) {
          String description = rs.getString("description");
          int id = rs.getInt("role_id");
          list.add(new Role(id, description));
        }
        return list;
      });
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  public int addRole(Role role) {
    String sql = "INSERT INTO role (role_id, description) VALUES ( ?, ? )";

    try {
      return jdbcContext.executeUpdateWithStatementStrategy(conn -> {
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, role.getRoleId());
        ps.setString(2, role.getDescription());
        return ps;
      });
    } catch (SQLException e) {
      e.printStackTrace();
      return -1;
    }
  }
  
  public int deleteRole(final Integer roleId) {
    String sql = "DELETE FROM role WHERE role_id = ?";

    try {
      return jdbcContext.executeUpdateWithStatementStrategy(conn -> {
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, roleId);
        return ps;
      });
    } catch (SQLException e) {
      e.printStackTrace();
      return -1;
    }
  }
  
  public int updateRole(final Role role) {
    String sql = "update role set description = ? where role_id = ?";

    try {
      return jdbcContext.executeUpdateWithStatementStrategy(conn -> {
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, role.getDescription());
        ps.setInt(2, role.getRoleId());
        return ps;
      });
    } catch (SQLException e) {
      e.printStackTrace();
      return -1;
    }
  }
}
