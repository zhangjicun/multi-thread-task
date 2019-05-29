package cn.com.tvmore.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.com.tvmore.entity.LoginLogEntity;

@Component
public class LoginLogDao {
	
	@Resource
	private JdbcTemplate jdbcTemplate;
	
	public int saveLoginLog(LoginLogEntity entity) {
		String sql = "INSERT INTO login_log(MORETV_ID, LOGIN_SOURCE, LOGIN_CHANNEL, LASTLOGIN_IP, CREATE_TIME, UPDATE_TIME, "
				+ "LOGIN_TIME, GUID, VERSION, PROVINCE, CITY) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
		int row = jdbcTemplate.update(sql, new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, entity.getMoretvId());
				ps.setString(2, entity.getLoginSource());
				ps.setString(3, entity.getLoginChannel());
				ps.setString(4, entity.getLastloginIp());
				ps.setTimestamp(5, entity.getCreateTime() == null? null:new Timestamp(entity.getCreateTime().getTime()));
				ps.setTimestamp(6, entity.getUpdateTime() == null? null:new Timestamp(entity.getUpdateTime().getTime()));
				if(entity.getLoginTime() != null) {
					ps.setLong(7, entity.getLoginTime());
				} else {
					ps.setObject(7, null);
				}
				ps.setString(8, entity.getGuid());
				ps.setString(9, entity.getVersion());
				ps.setString(10, entity.getProvince());
				ps.setString(11, entity.getCity());
			}
		});
		return row;
	}
	
	public List<LoginLogEntity> getEntitys() {
		String sql = "SELECT * FROM login_log WHERE PROVINCE IS NULL AND CITY IS NULL";
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
				/*entity.setLoginTime(rs.getInt("LOGIN_TIME"));*/
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
	
	public int updateEntity(LoginLogEntity entity) {
		String sql = "UPDATE login_log SET PROVINCE=?, CITY=? WHERE ID=?";
		int row = jdbcTemplate.update(sql, new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, entity.getProvince());
				ps.setString(2, entity.getCity());
				ps.setInt(3, entity.getId());
			}
		});
		return row;
	}
}
