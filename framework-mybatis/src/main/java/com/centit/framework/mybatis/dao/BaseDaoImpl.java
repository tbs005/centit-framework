package com.centit.framework.mybatis.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.builder.StaticSqlSource;
import org.apache.ibatis.exceptions.TooManyResultsException;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;

/**
 * MyBatis执行sql工具
 *
 * @author liuzh
 * @since 2015-03-10
 */
public class BaseDaoImpl {
	private final MSUtils msUtils;
	private final SqlSession sqlSession;

	/**
	 * 构造方法，默认缓存MappedStatement
	 *
	 * @param sqlSession sqlSession
	 */
	public BaseDaoImpl(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
		this.msUtils = new MSUtils(sqlSession.getConfiguration());
	}

	/**
	 * 获取List中最多只有一个的数据
	 *
	 * @param list
	 *            List结果
	 * @param <T>
	 *            泛型类型
	 * @return
	 */
	private <T> T getOne(List<T> list) {
		if (list.size() == 1) {
			return list.get(0);
		} else if (list.size() > 1) {
			throw new TooManyResultsException(
					"Expected one result (or null) to be returned by selectOne(), but found: " + list.size());
		} else {
			return null;
		}
	}

	/**
	 * 查询返回一个结果，多个结果时抛出异常
	 *
	 * @param sql 执行的sql
	 * @return  Map selectOne
	 */
	public Map<String, Object> selectOne(String sql) {
		List<Map<String, Object>> list = selectList(sql);
		return getOne(list);
	}

	/**
	 * 查询返回一个结果，多个结果时抛出异常
	 *
	 * @param sql 执行的sql
	 * @param value   参数
	 * @return Map selectOne
	 */
	public Map<String, Object> selectOne(String sql, Object value) {
		List<Map<String, Object>> list = selectList(sql, value);
		return getOne(list);
	}

	/**
	 * 查询返回一个结果，多个结果时抛出异常
	 *
	 * @param sql 执行的sql
	 * @param resultType 返回的结果类型
	 * @param <T> 泛型类型
	 * @return Map selectOne
	 */
	public <T> T selectOne(String sql, Class<T> resultType) {
		List<T> list = selectList(sql, resultType);
		return getOne(list);
	}

	/**
	 * 查询返回一个结果，多个结果时抛出异常
	 *
	 * @param sql 执行的sql
	 * @param value 参数
	 * @param resultType 返回的结果类型
	 * @param <T> 泛型类型
	 * @return Map selectOne
	 */
	public <T> T selectOne(String sql, Object value, Class<T> resultType) {
		List<T> list = selectList(sql, value, resultType);
		return getOne(list);
	}

	/**
	 * 查询返回
	 *
	 * @param sql 执行的sql
	 * @return List
	 */
	public List<Map<String, Object>> selectList(String sql) {
		String msId = msUtils.select(sql);
		return sqlSession.selectList(msId);
	}

	/**
	 * 查询返回
	 *
	 * @param sql 执行的sql
	 * @param value 参数
	 * @return List
	 */
	public List<Map<String, Object>> selectList(String sql, Object value) {
		Class<?> parameterType = value != null ? value.getClass() : null;
		String msId = msUtils.selectDynamic(sql, parameterType);
		return sqlSession.selectList(msId, value);
	}

	/**
	 * 查询返回指定的结果类型
	 *
	 * @param sql 执行的sql
	 * @param resultType 返回的结果类型
	 * @param <T>  泛型类型
	 * @return List
	 */
	public <T> List<T> selectList(String sql, Class<T> resultType) {
		String msId;
		if (resultType == null) {
			msId = msUtils.select(sql);
		} else {
			msId = msUtils.select(sql, resultType);
		}
		return sqlSession.selectList(msId);
	}

	/**
	 * 查询返回指定的结果类型
	 *
	 * @param sql  执行的sql
	 * @param value 参数
	 * @param resultType 返回的结果类型
	 * @param <T>  泛型类型
	 * @return List
	 */
	public <T> List<T> selectList(String sql, Object value, Class<T> resultType) {
		String msId;
		Class<?> parameterType = value != null ? value.getClass() : null;
		if (resultType == null) {
			msId = msUtils.selectDynamic(sql, parameterType);
		} else {
			msId = msUtils.selectDynamic(sql, parameterType, resultType);
		}
		return sqlSession.selectList(msId, value);
	}

	/**
	 * 插入数据
	 *
	 * @param sql  执行的sql
	 * @return  插入数量
	 */
	public int insert(String sql) {
		String msId = msUtils.insert(sql);
		return sqlSession.insert(msId);
	}

	/**
	 * 插入数据
	 *
	 * @param sql 执行的sql
	 * @param value  参数
	 * @return 插入数量
	 */
	public int insert(String sql, Object value) {
		Class<?> parameterType = value != null ? value.getClass() : null;
		String msId = msUtils.insertDynamic(sql, parameterType);
		return sqlSession.insert(msId, value);
	}

	/**
	 * 更新数据
	 *
	 * @param sql 执行的sql
	 * @return 更新数量
	 */
	public int update(String sql) {
		String msId = msUtils.update(sql);
		return sqlSession.update(msId);
	}

	/**
	 * 更新数据
	 *
	 * @param sql 执行的sql
	 * @param value 参数
	 * @return 更新数量
	 */
	public int update(String sql, Object value) {
		Class<?> parameterType = value != null ? value.getClass() : null;
		String msId = msUtils.updateDynamic(sql, parameterType);
		return sqlSession.update(msId, value);
	}

	/**
	 * 删除数据
	 *
	 * @param sql 执行的sql
	 * @return 删除数量
	 */
	public int delete(String sql) {
		String msId = msUtils.delete(sql);
		return sqlSession.delete(msId);
	}

	/**
	 * 删除数据
	 *
	 * @param sql 执行的sql
	 * @param value 参数
	 * @return 删除数量
	 */
	public int delete(String sql, Object value) {
		Class<?> parameterType = value != null ? value.getClass() : null;
		String msId = msUtils.deleteDynamic(sql, parameterType);
		return sqlSession.delete(msId, value);
	}

	private class MSUtils {
		private Configuration configuration;
		private LanguageDriver languageDriver;

		private MSUtils(Configuration configuration) {
			this.configuration = configuration;
			languageDriver = configuration.getDefaultScriptingLanguageInstance();
		}

		/**
		 * 创建MSID
		 *
		 * @param sql  执行的sql
		 * @param sql 执行的sqlCommandType
		 * @return 创建MSID
		 */
		private String newMsId(String sql, SqlCommandType sqlCommandType) {
			StringBuilder msIdBuilder = new StringBuilder(sqlCommandType.toString());
			msIdBuilder.append(".").append(sql.hashCode());
			return msIdBuilder.toString();
		}

		/**
		 * 是否已经存在该ID
		 *
		 * @param msId msId
		 * @return 是否已经存在该ID
		 */
		private boolean hasMappedStatement(String msId) {
			return configuration.hasStatement(msId, false);
		}

		/**
		 * 创建一个查询的MS
		 *
		 * @param msId msId
		 * @param sqlSource 执行的sqlSource
		 * @param resultType 返回的结果类型
		 */
		private void newSelectMappedStatement(String msId, SqlSource sqlSource, final Class<?> resultType) {
			MappedStatement ms = new MappedStatement.Builder(configuration, msId, sqlSource, SqlCommandType.SELECT)
					.resultMaps(new ArrayList<ResultMap>() {
						private static final long serialVersionUID = 1L;

						{
							add(new ResultMap.Builder(configuration, "defaultResultMap", resultType,
									new ArrayList<ResultMapping>(0)).build());
						}
					}).build();
			// 缓存
			configuration.addMappedStatement(ms);
		}

		/**
		 * 创建一个简单的MS
		 *
		 * @param msId msId
		 * @param sqlSource 执行的sqlSource
		 * @param sqlCommandType 执行的sqlCommandType
		 */
		private void newUpdateMappedStatement(String msId, SqlSource sqlSource, SqlCommandType sqlCommandType) {
			MappedStatement ms = new MappedStatement.Builder(configuration, msId, sqlSource, sqlCommandType)
					.resultMaps(new ArrayList<ResultMap>() {
						private static final long serialVersionUID = 1L;

						{
							add(new ResultMap.Builder(configuration, "defaultResultMap", int.class,
									new ArrayList<ResultMapping>(0)).build());
						}
					}).build();
			// 缓存
			configuration.addMappedStatement(ms);
		}

		private String select(String sql) {
			String msId = newMsId(sql, SqlCommandType.SELECT);
			if (hasMappedStatement(msId)) {
				return msId;
			}
			StaticSqlSource sqlSource = new StaticSqlSource(configuration, sql);
			newSelectMappedStatement(msId, sqlSource, Map.class);
			return msId;
		}

		private String selectDynamic(String sql, Class<?> parameterType) {
			String msId = newMsId(sql + parameterType, SqlCommandType.SELECT);
			if (hasMappedStatement(msId)) {
				return msId;
			}
			SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, parameterType);
			newSelectMappedStatement(msId, sqlSource, Map.class);
			return msId;
		}

		private String select(String sql, Class<?> resultType) {
			String msId = newMsId(resultType + sql, SqlCommandType.SELECT);
			if (hasMappedStatement(msId)) {
				return msId;
			}
			StaticSqlSource sqlSource = new StaticSqlSource(configuration, sql);
			newSelectMappedStatement(msId, sqlSource, resultType);
			return msId;
		}

		private String selectDynamic(String sql, Class<?> parameterType, Class<?> resultType) {
			String msId = newMsId(resultType + sql + parameterType, SqlCommandType.SELECT);
			if (hasMappedStatement(msId)) {
				return msId;
			}
			SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, parameterType);
			newSelectMappedStatement(msId, sqlSource, resultType);
			return msId;
		}

		private String insert(String sql) {
			String msId = newMsId(sql, SqlCommandType.INSERT);
			if (hasMappedStatement(msId)) {
				return msId;
			}
			StaticSqlSource sqlSource = new StaticSqlSource(configuration, sql);
			newUpdateMappedStatement(msId, sqlSource, SqlCommandType.INSERT);
			return msId;
		}

		private String insertDynamic(String sql, Class<?> parameterType) {
			String msId = newMsId(sql + parameterType, SqlCommandType.INSERT);
			if (hasMappedStatement(msId)) {
				return msId;
			}
			SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, parameterType);
			newUpdateMappedStatement(msId, sqlSource, SqlCommandType.INSERT);
			return msId;
		}

		private String update(String sql) {
			String msId = newMsId(sql, SqlCommandType.UPDATE);
			if (hasMappedStatement(msId)) {
				return msId;
			}
			StaticSqlSource sqlSource = new StaticSqlSource(configuration, sql);
			newUpdateMappedStatement(msId, sqlSource, SqlCommandType.UPDATE);
			return msId;
		}

		private String updateDynamic(String sql, Class<?> parameterType) {
			String msId = newMsId(sql + parameterType, SqlCommandType.UPDATE);
			if (hasMappedStatement(msId)) {
				return msId;
			}
			SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, parameterType);
			newUpdateMappedStatement(msId, sqlSource, SqlCommandType.UPDATE);
			return msId;
		}

		private String delete(String sql) {
			String msId = newMsId(sql, SqlCommandType.DELETE);
			if (hasMappedStatement(msId)) {
				return msId;
			}
			StaticSqlSource sqlSource = new StaticSqlSource(configuration, sql);
			newUpdateMappedStatement(msId, sqlSource, SqlCommandType.DELETE);
			return msId;
		}

		private String deleteDynamic(String sql, Class<?> parameterType) {
			String msId = newMsId(sql + parameterType, SqlCommandType.DELETE);
			if (hasMappedStatement(msId)) {
				return msId;
			}
			SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, parameterType);
			newUpdateMappedStatement(msId, sqlSource, SqlCommandType.DELETE);
			return msId;
		}
	}
}