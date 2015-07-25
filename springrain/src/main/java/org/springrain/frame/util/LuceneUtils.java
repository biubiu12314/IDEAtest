package org.springrain.frame.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.shiro.util.CollectionUtils;

/**
 * lucene 工具类
 * 
 * @author caomei
 *
 */
public class LuceneUtils {

	// 分词器
	public static Analyzer analyzer = new SmartChineseAnalyzer();

	// 根索引路径
	public static final String rootdir = "lucene/index";

	/**
	 * 根据实体类查询结果
	 * 
	 * @param clazz
	 * @param page
	 * @param searchkeyword
	 * @return
	 * @throws Exception
	 */
	public static List searchDocument(Class clazz, Page page, String searchkeyword) throws Exception {
		List<String> luceneFields = ClassUtils.getLuceneFields(clazz);
		if (CollectionUtils.isEmpty(luceneFields)) {
			return null;
		}
		String[] fields = (String[]) luceneFields.toArray(new String[luceneFields.size()]);
		return searchDocument(clazz, page, fields, searchkeyword);
	}

	/**
	 * 根据某个字段类查询结果
	 * 
	 * @param clazz
	 * @param page
	 * @param searchkeyword
	 * @return
	 * @throws Exception
	 */
	public static List searchDocument(Class clazz, Page page, String field, String searchkeyword) throws Exception {
		if (StringUtils.isBlank(field)) {
			return null;
		}
		String[] fields = new String[] { field };
		return searchDocument(clazz, page, fields, searchkeyword);
	}

	/**
	 * 
	 * @param clazz
	 * @param page
	 * @param fields
	 * @param searchkeyword
	 * @return
	 * @throws Exception
	 */
	public static <T> List<T> searchDocument(Class<T> clazz, Page page, String[] fields, String searchkeyword)
			throws Exception {

		if (fields == null || fields.length < 1) {
			return null;
		}

		// 获取索引目录文件
		Directory directory = getDirectory(clazz);
		if (directory == null) {
			return null;
		}

		// 获取读取的索引
		IndexReader indexReader = DirectoryReader.open(directory);
		// 获取索引的查询器
		IndexSearcher indexSearcher = new IndexSearcher(indexReader);
		// 查询指定字段的转换器
		// QueryParser parser = new QueryParser(field, analyzer);
		QueryParser parser = new MultiFieldQueryParser(fields, analyzer);
		// 需要查询的关键字
		Query query = parser.parse(searchkeyword);

		// 查询出的结果文档
		int _size = 20;
		if (page != null && page.getPageSize() > 0) {
			_size = page.getPageSize();
		}
		
		//总条数
     	int totalCount=indexSearcher.count(query);
		page.setTotalCount(totalCount);
		
		int _max=page.getPageIndex()*(page.getPageIndex()-1);
		if(_max-totalCount>=0){
			return null;
		}
		
		
		
		//先获取上一页的最后一个元素
        ScoreDoc lastscoreDoc = getLastScoreDoc(page.getPageIndex(), _size, query, indexSearcher);
      //通过最后一个元素搜索下页的pageSize个元素
        TopDocs topDocs = indexSearcher.searchAfter(lastscoreDoc,query, _size);
		// 查询出的结果文档
		ScoreDoc[] hits = topDocs.scoreDocs;

		if (hits == null || hits.length < 1) {
			return null;
		}

		List<T> list = new ArrayList<T>(hits.length);
		for (int i = 0; i < hits.length; i++) {
			Document hitDoc = indexSearcher.doc(hits[i].doc);
			T t = clazz.newInstance();
			for (String fieldName : fields) {
				String fieldValue = hitDoc.get(fieldName);
				ClassUtils.setPropertieValue(fieldName, t, fieldValue);
			}
			list.add(t);
		}
		indexReader.close();
		directory.close();

		return list;
	}

	/**
	 * 根据实体类保存到索引,使用 LuceneSearch和LuceneField
	 * 
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	public static String saveDocument(Object entity) throws Exception {
		// 获取索引的字段,为null则不进行保存
		List<String> luceneFields = ClassUtils.getLuceneFields(entity.getClass());
		if (CollectionUtils.isEmpty(luceneFields)) {
			return "error";
		}

		// 索引写入配置
		IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
		// 获取索引目录文件
		Directory directory = getDirectory(entity.getClass());
		if (directory == null) {
			return null;
		}
		IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);
		Document doc = new Document();

		for (String fieldName : luceneFields) {
			String _value = ClassUtils.getPropertieValue(fieldName, entity).toString();
			doc.add(new Field(fieldName, _value, TextField.TYPE_STORED));
		}
		indexWriter.addDocument(doc);
		indexWriter.close();
		directory.close();

		return null;
	}

	/**
	 * 根据实体类批量保存到索引,使用 LuceneSearch和LuceneField
	 * 
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	public static <T> String saveDocument(List<T> list) throws Exception {
		if (CollectionUtils.isEmpty(list)) {
			return "error";
		}

		T t = list.get(0);
		// 获取索引的字段,为null则不进行保存
		List<String> luceneFields = ClassUtils.getLuceneFields(t.getClass());
		if (CollectionUtils.isEmpty(luceneFields)) {
			return "error";
		}

		// 索引写入配置
		IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
		// 获取索引目录文件
		Directory directory = getDirectory(t.getClass());
		if (directory == null) {
			return null;
		}
		IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);
		for (T t1 : list) {
			Document doc = new Document();
			for (String fieldName : luceneFields) {
				String _value = ClassUtils.getPropertieValue(fieldName, t1).toString();
				doc.add(new Field(fieldName, _value, TextField.TYPE_STORED));
			}
			indexWriter.addDocument(doc);
		}
		indexWriter.commit();
		indexWriter.close();
		directory.close();

		return null;
	}

	/**
	 * 删除文档
	 * 
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	public static String deleteDocument(Object id, Class clazz) throws Exception {

		String pkName = ClassUtils.getEntityInfoByClass(clazz).getPkName();
		// 索引写入配置
		IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
		// 获取索引目录文件
		Directory directory = getDirectory(clazz);
		if (directory == null) {
			return null;
		}
		IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);
		
		//Term term = new Term(pkName, id.toString());
		
		QueryParser parser = new QueryParser(pkName, analyzer);
		// 需要查询的关键字
		Query query = parser.parse(id.toString());
		indexWriter.deleteDocuments(query);
		//indexWriter.deleteDocuments(term);
		indexWriter.commit();
		indexWriter.close(); // 记得关闭,否则删除不会被同步到索引文件中
		directory.close(); // 关闭目录

		return null;
	}

	/**
	 * 批量删除文档
	 * 
	 * @param entity
	 * @return
	 * @throws Exception 
	 */
	public static  String deleteDocument(List<String> ids,Class clazz) throws Exception {
		if(CollectionUtils.isEmpty(ids)){
			return "error";
		}
		String pkName = ClassUtils.getEntityInfoByClass(clazz).getPkName();
		// 索引写入配置
		IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
		// 获取索引目录文件
		Directory directory = getDirectory(clazz);
		if (directory == null) {
			return null;
		}
		IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);
		
		for(String t:ids){
			Term term = new Term(pkName, t);
			indexWriter.deleteDocuments(term);
		}
		indexWriter.commit();
		indexWriter.close(); // 记得关闭,否则删除不会被同步到索引文件中
		directory.close(); // 关闭目录
		return null;
	}

	/**
	 * 修改文档
	 * 
	 * @param entity
	 * @return
	 * @throws Exception 
	 */
	public static String updateDocument(Object entity) throws Exception {
		String pkValue=ClassUtils.getPKValue(entity).toString();
		deleteDocument(pkValue,entity.getClass());
		saveDocument(entity);
		return null;
	}

	/**
	 * 批量修改文档
	 * 
	 * @param entity
	 * @return
	 * @throws Exception 
	 */
	public static <T> String updateDocument(List<T> list) throws Exception {
		
		if(CollectionUtils.isEmpty(list)){
			return null;
		}
		List<String> ids=new ArrayList<String>();
		Class clazz=list.get(0).getClass();
		for(T t:list){
			String id=ClassUtils.getPKValue(t).toString();
			ids.add(id);
		}
		deleteDocument(ids,clazz);
		saveDocument(list);
		return null;
	}

	/**
	 * 获取索引的目录
	 * 
	 * @param clazz
	 * @return
	 */
	public static File getIndexDirFile(Class clazz) {
		if (clazz == null) {
			return null;
		}
		File file = new File(rootdir + "/" + clazz.getName());
		if (!file.exists()) {
			file.mkdirs();
		}
		return file;

	}

	/**
	 * 获取实体类的索引文档
	 * 
	 * @param clazz
	 * @return
	 * @throws IOException
	 */
	public static Directory getDirectory(Class clazz) throws IOException {
		File indexDirFile = getIndexDirFile(clazz);
		if (indexDirFile == null) {
			return null;
		}
		Path indexDirPath = indexDirFile.toPath();
		// 索引不可读
		if (!Files.isReadable(indexDirPath)) {
			return null;
		}
		// 获取索引目录文件
		Directory directory = FSDirectory.open(indexDirPath);
		return directory;

	}
	
	  /**
     * 根据页码和分页大小获取上一次的最后一个ScoreDoc
     */
    private static ScoreDoc getLastScoreDoc(int pageIndex,int pageSize,Query query,IndexSearcher searcher) throws IOException {
        if(pageIndex<=1)return null;//如果是第一页就返回空
        int num = pageSize*(pageIndex-1);//获取上一页的数量
        TopDocs tds = searcher.search(query, num);
        return tds.scoreDocs[num-1];
    }

}