# pika-lowcoding
> 一个快速生成管理系统的框架
>

### Docker 快速安装 Minio
```shell
docker run --name=minio -p 5000:9000 -p 5001:9001 -e "MINIO_ROOT_USER=pika-admin" -e "MINIO_ROOT_PASSWORD=pika-admin" -v F:/Develop/Java/pika-lowcoding/pika-lowcoding-cloud/minio-repostory/data:/data -d minio/minio server /data --console-address ":9000" --address ":9001"
```