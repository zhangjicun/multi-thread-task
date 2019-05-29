# multi-thread-task
多线程（线程数可调节），处理千万级mysql数据表，预处理+数据迁移到新表


# 构建打包（loginlog-task-0.0.1-SNAPSHOT.jar）
mvn clean package

# 执行脚本（参数20000 为每一个线程执行的数据量，代码默认30线程）
java -Xms512m -Xmx1024m -XX:+HeapDumpOnOutOfMemoryError -jar loginlog-task-0.0.1-SNAPSHOT.jar 20000
