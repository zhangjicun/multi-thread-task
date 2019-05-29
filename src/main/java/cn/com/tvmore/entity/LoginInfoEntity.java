package cn.com.tvmore.entity;

import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class LoginInfoEntity {
	private Integer id;
	private String moretvId;
	private String loginSource;
	private String loginChannel;
	private String lastloginIp;
	private Date createTime;
	private Date updateTime;
	private Long loginTime;
	private String guid;
	private String version;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMoretvId() {
		return moretvId;
	}

	public void setMoretvId(String moretvId) {
		this.moretvId = moretvId;
	}

	public String getLoginSource() {
		return loginSource;
	}

	public void setLoginSource(String loginSource) {
		this.loginSource = loginSource;
	}

	public String getLoginChannel() {
		return loginChannel;
	}

	public void setLoginChannel(String loginChannel) {
		this.loginChannel = loginChannel;
	}

	public String getLastloginIp() {
		return lastloginIp;
	}

	public void setLastloginIp(String lastloginIp) {
		this.lastloginIp = lastloginIp;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Long getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Long loginTime) {
		this.loginTime = loginTime;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
