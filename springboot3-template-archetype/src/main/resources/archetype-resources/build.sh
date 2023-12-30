#!/bin/bash
# 使用：
# 脚本授权：chmod +x build.sh
# 执行脚本：./build.sh -e dev -d
# -e 指定环境 ；-d 开启docker打包

# 默认参数
ENVIRONMENT="prod"
DOCKER_BUILD=false
IMAGE_NAME="spring-boot-app:latest"
CONTAINER_NAME="spring-boot-app"

# 处理命令行参数
while getopts ":e:d" opt; do
  case $opt in
    e)
      ENVIRONMENT="$OPTARG"
      ;;
    d)
      DOCKER_BUILD=true
      ;;
    \?)
      echo "Invalid option: -$OPTARG" >&2
      exit 1
      ;;
    :)
      echo "Option -$OPTARG requires an argument." >&2
      exit 1
      ;;
  esac
done

# Maven打包
mvn -DskipTests=true clean package -Dspring.profiles.active="$ENVIRONMENT"

# Docker打包
if [ "$DOCKER_BUILD" = true ]; then
  # 停止并移除已有的Docker容器
  docker stop $CONTAINER_NAME
  docker rm $CONTAINER_NAME

  # 删除已有的Docker镜像
  docker rmi $IMAGE_NAME

  # 构建新的Docker镜像
  docker build -t $IMAGE_NAME -f Dockerfile .
fi
