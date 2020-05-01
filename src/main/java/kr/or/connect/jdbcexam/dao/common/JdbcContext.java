package kr.or.connect.jdbcexam.dao.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.function.Consumer;
import java.util.function.Function;

public class JdbcContext {
  private static String dbUrl =
      "jdbc:mysql://localhost:3306/connectdb?serverTimezone=Asia/Seoul&useSSL=false";
  private static String dbUser = "connectuser";
  private static String dbPassword = "connectuser123!@#";

  public <T> T executeSql(final String query, ResultsetCallback<T> resultsetCallback)
      throws SQLException {
    return executeQueryWithStatementStrategy(conn -> conn.prepareCall(query), resultsetCallback);
  }

  public <T> T executeSqlWithParameter(final String query, Object[] sqlParameter,
      ResultsetCallback<T> resultsetCallback) throws SQLException {
    return executeQueryWithStatementStrategy(conn -> {
      PreparedStatement ps = conn.prepareCall(query);
      setParameter(ps, sqlParameter);
      return ps;
    }, resultsetCallback);
  }

  public <T> T executeQueryWithStatementStrategy(StatementStrategy stmt,
      ResultsetCallback<T> callback) throws SQLException {
    return executeWithStatementStrategy(stmt, ps -> {
      try (ResultSet rs = ps.executeQuery()) {
        return callback.work(rs);
      } catch (Exception e) {
        e.printStackTrace();
        throw e;
      }
    });
  }

  public int executeUpdateWithStatementStrategy(StatementStrategy stmt) throws SQLException {
    return executeWithStatementStrategy(stmt, ps -> ps.executeUpdate());
  }


  public <T> T executeWithStatementStrategy(StatementStrategy stmt,
      ThrowableFunction<PreparedStatement, T> job) throws SQLException {
    T result = null;

    try {
      Class.forName("com.mysql.jdbc.Driver");
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    try (final Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
        final PreparedStatement ps = stmt.makePreparedStatement(conn)) {
      result = job.apply(ps);
    } catch (SQLException e) {
      e.printStackTrace();
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return result;
  }

  private void setParameter(PreparedStatement ps, Object[] sqlParameter) throws SQLException {
    for (int i = 1; i <= sqlParameter.length; i++) {
      Object obj = sqlParameter[i];
      if (obj instanceof Integer || obj.getClass().getName().equals("int")) {
        ps.setInt(i, (int) obj);
      } else if (obj instanceof Float || obj.getClass().getName().equals("float")) {
        ps.setFloat(i, (float) obj);
      } else if (obj instanceof Double || obj.getClass().getName().equals("double")) {
        ps.setDouble(i, (double) obj);
      } else if (obj instanceof Long || obj.getClass().getName().equals("long")) {
        ps.setLong(i, (long) obj);
      } else if (obj instanceof Boolean || obj.getClass().getName().equals("boolean")) {
        ps.setBoolean(i, (boolean) obj);
        return;
      } else if (obj instanceof Date) {
        ps.setDate(i, java.sql.Date.valueOf(obj.toString()));
      } else if (obj instanceof String) {
        ps.setString(i, (String) obj);
        return;
      }
    }
  }

  interface ThrowableFunction<T, A> {
    A apply(T t) throws Exception;
  }
}
