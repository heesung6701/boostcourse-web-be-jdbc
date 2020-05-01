package kr.or.connect.jdbcexam.dao.common;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultsetCallback<T> {
  T work(ResultSet rs) throws SQLException;
}
