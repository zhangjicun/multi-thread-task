package cn.com.tvmore.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.com.tvmore.entity.LoginLogEntity;

@Component
public class LoginInfoDao {
	@Resource
	private JdbcTemplate jdbcTemplate;
	
	/*public List<LoginInfoEntity> getLoginInfoList(int startId, int rows) {*/
	public List<LoginLogEntity> getLoginInfoList(int startId, int rows) {
		StringBuilder sb = new StringBuilder("SELECT * FROM login_info WHERE id > ").append(startId);
		sb.append(" ORDER BY ID LIMIT ");
		sb.append(rows);
		
		String sql = sb.toString();
		System.out.println("[QUERY SQL] -> " + sql);
		
		List<LoginLogEntity> list = jdbcTemplate.query(sql, new RowMapper<LoginLogEntity>() {
			@Override
			public LoginLogEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
				LoginLogEntity entity = new LoginLogEntity();
				entity.setId(rs.getInt("ID"));
				entity.setMoretvId(rs.getString("MORETV_ID"));
				entity.setLoginSource(rs.getString("LOGIN_SOURCE"));
				entity.setLoginChannel(rs.getString("LOGIN_CHANNEL"));
				entity.setLastloginIp(rs.getString("LASTLOGIN_IP"));
				entity.setCreateTime(rs.getTimestamp("CREATE_TIME"));
				entity.setUpdateTime(rs.getTimestamp("UPDATE_TIME"));
				entity.setLoginTime(rs.getLong("LOGIN_TIME"));
				if(rs.wasNull()) {
					entity.setLoginTime(null);
				}
				entity.setGuid(rs.getString("GUID"));
				entity.setVersion(rs.getString("VERSION"));
				
				return entity;
			}
		});
		return list;
	}
	
	public List<LoginLogEntity> getEntityList(int offset, int rows) {
		StringBuilder sb = new StringBuilder("SELECT * FROM login_info LIMIT ");
		sb.append(offset).append(",").append(rows);
		String sql = sb.toString();
		System.out.println("[QUERY SQL] -> " + sql);
		List<LoginLogEntity> list = jdbcTemplate.query(sql, new RowMapper<LoginLogEntity>() {
			@Override
			public LoginLogEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
				LoginLogEntity entity = new LoginLogEntity();
				entity.setId(rs.getInt("ID"));
				entity.setMoretvId(rs.getString("MORETV_ID"));
				entity.setLoginSource(rs.getString("LOGIN_SOURCE"));
				entity.setLoginChannel(rs.getString("LOGIN_CHANNEL"));
				entity.setLastloginIp(rs.getString("LASTLOGIN_IP"));
				entity.setCreateTime(rs.getTimestamp("CREATE_TIME"));
				entity.setUpdateTime(rs.getTimestamp("UPDATE_TIME"));
				entity.setLoginTime(rs.getLong("LOGIN_TIME"));
				if(rs.wasNull()) {
					entity.setLoginTime(null);
				}
				entity.setGuid(rs.getString("GUID"));
				entity.setVersion(rs.getString("VERSION"));
				return entity;
			}
		});
		return list;
	}
	
	public List<LoginLogEntity> getLoginInfoList(int rows) {
		StringBuilder sb = new StringBuilder("SELECT * FROM login_info  ORDER BY ID LIMIT ");
		sb.append(rows);
		
		String sql = sb.toString();
		System.out.println("[QUERY SQL] -> " + sql);
		
		List<LoginLogEntity> list = jdbcTemplate.query(sql, new RowMapper<LoginLogEntity>() {
			@Override
			public LoginLogEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
				LoginLogEntity entity = new LoginLogEntity();
				entity.setId(rs.getInt("ID"));
				entity.setMoretvId(rs.getString("MORETV_ID"));
				entity.setLoginSource(rs.getString("LOGIN_SOURCE"));
				entity.setLoginChannel(rs.getString("LOGIN_CHANNEL"));
				entity.setLastloginIp(rs.getString("LASTLOGIN_IP"));
				entity.setCreateTime(rs.getTimestamp("CREATE_TIME"));
				entity.setUpdateTime(rs.getTimestamp("UPDATE_TIME"));
				entity.setLoginTime(rs.getLong("LOGIN_TIME"));
				if(rs.wasNull()) {
					entity.setLoginTime(null);
				}
				entity.setGuid(rs.getString("GUID"));
				entity.setVersion(rs.getString("VERSION"));
				
				return entity;
			}
		});
		return list;
	}
	
	public int deleteLoginInfoById(int id) {
		String sql = "DELETE FROM login_info WHERE ID = ?";
		
		int resRow = jdbcTemplate.update(sql, new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setInt(1, id);
			}
		});
		
		return resRow;
	}
	
	public int deleteLoginInfoBefore(int id) {
		String sql = "DELETE FROM login_info WHERE ID <= ?";
		
		int resRow = jdbcTemplate.update(sql, new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setInt(1, id);
			}
		});
		
		return resRow;
	}
}
