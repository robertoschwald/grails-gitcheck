dataSource {
  pooled = true
  driverClassName = "org.h2.Driver"
  dbCreate = "update"
  url = "jdbc:h2:mem:testDb;MVCC=TRUE;LOCK_TIMEOUT=10000"
  logSql = true
  username = "sa"
  password = ""
}
hibernate {
  cache.use_second_level_cache = true
  cache.use_query_cache = true
  cache.region.factory_class = 'net.sf.ehcache.hibernate.EhCacheRegionFactory'
  format_sql = true
  use_sql_comments = true
}
