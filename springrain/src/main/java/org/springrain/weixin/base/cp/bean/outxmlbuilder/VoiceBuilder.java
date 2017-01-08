package org.springrain.weixin.base.cp.bean.outxmlbuilder;

import org.springrain.weixin.base.cp.bean.WxCpXmlOutVoiceMessage;

/**
 * 语音消息builder
 *
 * @author springrain
 */
public final class VoiceBuilder extends BaseBuilder<VoiceBuilder, WxCpXmlOutVoiceMessage> {

  private String mediaId;

  public VoiceBuilder mediaId(String mediaId) {
    this.mediaId = mediaId;
    return this;
  }

  @Override
  public WxCpXmlOutVoiceMessage build() {
    WxCpXmlOutVoiceMessage m = new WxCpXmlOutVoiceMessage();
    setCommon(m);
    m.setMediaId(this.mediaId);
    return m;
  }

}
