package org.springrain.weixin.base.mp.bean;

import java.io.Serializable;

import org.springrain.weixin.base.mp.util.json.WxMpGsonBuilder;

/**
 * 群发时用到的视频素材
 * 
 * @author springrain
 */
public class WxMpMassVideo implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 9153925016061915637L;
  private String mediaId;
  private String title;
  private String description;

  public String getTitle() {
    return this.title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return this.description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getMediaId() {
    return this.mediaId;
  }

  public void setMediaId(String mediaId) {
    this.mediaId = mediaId;
  }

  public String toJson() {
    return WxMpGsonBuilder.INSTANCE.create().toJson(this);
  }
}
