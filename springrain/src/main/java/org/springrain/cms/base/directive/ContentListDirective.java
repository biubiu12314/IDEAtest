package org.springrain.cms.base.directive;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;
import org.springrain.cms.base.directive.util.DirectiveUtils;
import org.springrain.cms.base.entity.CmsContent;
import org.springrain.cms.base.service.ICmsContentService;
import org.springrain.frame.util.Page;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

@Component("contentListDirective")
public class ContentListDirective implements TemplateDirectiveModel {
	
	@Resource
	HttpServletRequest request;
	@Resource
	private ICmsContentService cmsContentService;
	
	
	
	/**
	 * 模板名称
	 */
	public static final String TPL_NAME = "cms_content_list";
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		
		String type = DirectiveUtils.getString("type", params);
		int pageIndex = DirectiveUtils.getInt("p", params);
		Page page = new Page(pageIndex);
		List<CmsContent> contentList;
		try {
			if(type.equals("0")){//查询站点首页下的内容
				String siteId = request.getAttribute("siteId").toString();
				contentList = cmsContentService.findListBySiteId(siteId, page);
			}else if (type.equals("1")){//查询栏目下的内容
				String channelId = request.getAttribute("businessId").toString();
				contentList = cmsContentService.findContentByChannelId(channelId, page);
			}else{
				//目前暂时没有其它类型，先占位
				contentList = new ArrayList<>();
			}
		} catch (Exception e) {
			contentList = new ArrayList<>();
		}
		env.setVariable("content_list", DirectiveUtils.wrap(contentList));
		if (body != null) { 
			body.render(env.getOut());  
		}
	}
	
}