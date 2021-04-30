docker rm -f mongdok_roomapi
docker run -d -p 8084:8084 --name mongdok_roomapi --restart=always -e spring.profiles.active=prod -v /home/ubuntu/data/mongdok_roomapi:/home/ubuntu/data/mongdok_roomapi mongdok_roomapi
docker ps