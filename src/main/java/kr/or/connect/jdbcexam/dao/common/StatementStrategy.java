package kr.or.connect.jdbcexam.dao.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface StatementStrategy {
  PreparedStatement makePreparedStatement(Connection c) throws SQLException;
}
