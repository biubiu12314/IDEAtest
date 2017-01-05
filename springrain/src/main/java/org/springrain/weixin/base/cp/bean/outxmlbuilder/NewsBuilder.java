package org.springrain.weixin.base.cp.bean.outxmlbuilder;

import org.springrain.weixin.base.cp.bean.WxCpXmlOutNewsMessage;
import org.springrain.weixin.base.cp.bean.WxCpXmlOutNewsMessage.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * 图文消息builder
 *
 * @author Daniel Qian
 */
public final class NewsBuilder extends BaseBuilder<NewsBuilder, WxCpXmlOutNewsMessage> {

  protected final List<Item> articles = new ArrayList<>();

  public NewsBuilder addArticle(Item item) {
    this.articles.add(item);
    return this;
  }

  @Override
  public WxCpXmlOutNewsMessage build() {
    WxCpXmlOutNewsMessage m = new WxCpXmlOutNewsMessage();
    for (Item item : this.articles) {
      m.addArticle(item);
    }
    setCommon(m);
    return m;
  }

}
