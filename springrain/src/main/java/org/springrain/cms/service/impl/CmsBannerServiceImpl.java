package org.springrain.cms.service.impl;

import java.io.File;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springrain.cms.entity.CmsBanner;
import org.springrain.cms.service.ICmsBannerService;
import org.springrain.frame.entity.IBaseEntity;
import org.springrain.frame.util.Finder;
import org.springrain.frame.util.Page;
import org.springrain.system.service.BaseSpringrainServiceImpl;


/**
 * TODO 在此加入类描述
 * @copyright {@link weicms.net}
 * @author springrain<Auto generate>
 * @version  2017-02-24 10:06:38
 * @see org.springrain.cms.base.service.impl.CmsBanner
 */
@Service("cmsBannerService")
public class CmsBannerServiceImpl extends BaseSpringrainServiceImpl implements ICmsBannerService {

   
    @Override
	public String  save(Object entity ) throws Exception{
	      CmsBanner cmsBanner=(CmsBanner) entity;
	       return super.save(cmsBanner).toString();
	}

    @Override
	public String  saveorupdate(Object entity ) throws Exception{
	      CmsBanner cmsBanner=(CmsBanner) entity;
		 return super.saveorupdate(cmsBanner).toString();
	}
	
	@Override
    public Integer update(IBaseEntity entity ) throws Exception{
	 CmsBanner cmsBanner=(CmsBanner) entity;
	return super.update(cmsBanner);
    }
    @Override
	public CmsBanner findCmsBannerById(Object id) throws Exception{
	 return super.findById(id,CmsBanner.class);
	}
	
/**
 * 列表查询,每个service都会重载,要把sql语句封装到service中,Finder只是最后的方案
 * @param finder
 * @param page
 * @param clazz
 * @param o
 * @return
 * @throws Exception
 */
        @Override
    public <T> List<T> findListDataByFinder(Finder finder, Page page, Class<T> clazz,
			Object o) throws Exception{
			 return super.findListDataByFinder(finder,page,clazz,o);
			}
	/**
	 * 根据查询列表的宏,导出Excel
	 * @param finder 为空则只查询 clazz表
	 * @param ftlurl 类表的模版宏
	 * @param page 分页对象
	 * @param clazz 要查询的对象
	 * @param o  querybean
	 * @return
	 * @throws Exception
	 */
		@Override
	public <T> File findDataExportExcel(Finder finder,String ftlurl, Page page,
			Class<T> clazz, Object o)
			throws Exception {
			 return super.findDataExportExcel(finder,ftlurl,page,clazz,o);
		}

	@Override
	public List<CmsBanner> findBannerListByBusinessId(String siteId,
			String businessId) throws Exception {
		Finder finder = Finder.getSelectFinder(CmsBanner.class).append(" where siteId=:siteId and businessId=:businessId");
		finder.setParam("siteId", siteId);
		finder.setParam("businessId", businessId);
		return super.queryForList(finder, CmsBanner.class);
	}

}
