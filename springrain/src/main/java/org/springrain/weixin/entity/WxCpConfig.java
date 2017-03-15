package org.springrain.weixin.entity;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springrain.frame.entity.BaseEntity;
import org.springrain.weixin.sdk.common.api.IWxCpConfig;

@Table(name="wx_cpconfig")
public class WxCpConfig   extends BaseEntity implements IWxCpConfig {
	private static final long serialVersionUID = 1L;
	
	   private volatile String id;
	
	  private volatile String appId;
	  private volatile String secret;
	  private volatile String partnerId;
	  private volatile String partnerKey;
	  private volatile String token;
	  private volatile String aesKey;
	  private volatile Integer active;
	  private volatile String siteId;
	  
	  
	  private volatile String oauth2redirectUri;

	  private volatile String httpProxyHost;
	  private volatile Integer httpProxyPort;
	  private volatile String httpProxyUsername;
	  private volatile String httpProxyPassword;

	  
	  
	  private volatile String certificateFile ;
	  private volatile String tmpDirFile;
	  
	  private volatile String corpId;
	  private volatile String corpSecret;
	  private volatile Integer agentId;
	  
	  
	  
	  private volatile String accessToken;
	  private volatile Long accessTokenExpiresTime=0L;
	  
	  private volatile String jsApiTicket;
	  private volatile Long jsApiTicketExpiresTime=0L;
	  
	  private volatile String cardApiTicket;
	  private volatile Long cardApiTicketExpiresTime=0L;
	  
	  
	  @Id
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	@Transient
	public String getSecret() {
		return secret;
	}
	public void setSecret(String secret) {
		this.secret = secret;
	}
	@Transient
	public String getPartnerId() {
		return partnerId;
	}
	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}
	@Transient
	public String getPartnerKey() {
		return partnerKey;
	}
	public void setPartnerKey(String partnerKey) {
		this.partnerKey = partnerKey;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	@Transient
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	@Transient
	public String getAesKey() {
		return aesKey;
	}
	public void setAesKey(String aesKey) {
		this.aesKey = aesKey;
	}
	
	
	
	@Transient
	public String getOauth2redirectUri() {
		return oauth2redirectUri;
	}
	public void setOauth2redirectUri(String oauth2redirectUri) {
		this.oauth2redirectUri = oauth2redirectUri;
	}
	@Transient
	public String getHttpProxyHost() {
		return httpProxyHost;
	}
	public void setHttpProxyHost(String httpProxyHost) {
		this.httpProxyHost = httpProxyHost;
	}
	@Transient
	public Integer getHttpProxyPort() {
		return httpProxyPort;
	}
	public void setHttpProxyPort(Integer httpProxyPort) {
		this.httpProxyPort = httpProxyPort;
	}
	@Transient
	public String getHttpProxyUsername() {
		return httpProxyUsername;
	}
	public void setHttpProxyUsername(String httpProxyUsername) {
		this.httpProxyUsername = httpProxyUsername;
	}
	@Transient
	public String getHttpProxyPassword() {
		return httpProxyPassword;
	}
	public void setHttpProxyPassword(String httpProxyPassword) {
		this.httpProxyPassword = httpProxyPassword;
	}
	@Transient
	public String getJsApiTicket() {
		return jsApiTicket;
	}
	public void setJsApiTicket(String jsApiTicket) {
		this.jsApiTicket = jsApiTicket;
	}
	
	@Transient
	public String getCardApiTicket() {
		return cardApiTicket;
	}
	public void setCardApiTicket(String cardApiTicket) {
		this.cardApiTicket = cardApiTicket;
	}
	
	@Transient
	public String getCertificateFile() {
		return certificateFile;
	}
	public void setCertificateFile(String certificateFile) {
		this.certificateFile = certificateFile;
	}
	
	@Transient
	public String getTmpDirFile() {
		return tmpDirFile;
	}
	public void setTmpDirFile(String tmpDirFile) {
		this.tmpDirFile = tmpDirFile;
	}
	
	
	@Transient
	public Long getAccessTokenExpiresTime() {
		return accessTokenExpiresTime;
	}
	public void setAccessTokenExpiresTime(Long accessTokenExpiresTime) {
		this.accessTokenExpiresTime =  System.currentTimeMillis() + (accessTokenExpiresTime - 600) * 1000L;//预留10分钟
	}
	
	
	@Transient
	public Long getCardApiTicketExpiresTime() {
		return cardApiTicketExpiresTime;
	}
	public void setCardApiTicketExpiresTime(Long cardApiTicketExpiresTime) {
		//预留10分钟
		this.cardApiTicketExpiresTime = System.currentTimeMillis() + (cardApiTicketExpiresTime - 600) * 1000L;//预留10分钟
	}

	@Transient
	public Long getJsApiTicketExpiresTime() {
		return jsApiTicketExpiresTime;
	}
	public void setJsApiTicketExpiresTime(Long jsApiTicketExpiresTime) {
		this.jsApiTicketExpiresTime =  System.currentTimeMillis() + (jsApiTicketExpiresTime - 600) * 1000L;//预留10分钟
	}
	
	
	
	@Transient
	public boolean isAccessTokenExpired() {
		 return System.currentTimeMillis() > this.accessTokenExpiresTime;
	}
	@Transient
	public boolean isJsApiTicketExpired() {
	    return System.currentTimeMillis() > this.jsApiTicketExpiresTime;
	  }
	@Transient
	public boolean isCardApiTicketExpired() {
	    return System.currentTimeMillis() > this.cardApiTicketExpiresTime;
	  }
	@Transient
	public boolean autoRefreshToken() {
	    return true;
	  }
	@Transient
	public String getCorpId() {
		return corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
	@Transient
	public String getCorpSecret() {
		return corpSecret;
	}
	public void setCorpSecret(String corpSecret) {
		this.corpSecret = corpSecret;
	}
	@Transient
	public Integer getAgentId() {
		return agentId;
	}
	public void setAgentId(Integer agentId) {
		this.agentId = agentId;
	}
	public Integer getActive() {
		return active;
	}
	public void setActive(Integer active) {
		this.active = active;
	}
	public String getSiteId() {
		return siteId;
	}
	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

}
