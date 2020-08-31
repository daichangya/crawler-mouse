#基础镜像jdk8
# 注意，这个镜像还集成了actuator健康检查脚本
FROM data-harbor.xcar.com.cn/xcar_base/oraclejdk8:8.192.12-cleaned
#添加jar包
ARG APP_NAME
ENV app_name=${APP_NAME}
ADD target/${app_name}.jar ${app_name}.jar
#设置启动命令
CMD java -jar \
-Dspring.profiles.active=${profile_name} \
-Duser.timezone=GMT+08 \
${jvm_parameters} ${app_name}.jar