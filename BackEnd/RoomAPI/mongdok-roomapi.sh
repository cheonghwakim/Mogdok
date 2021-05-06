docker rm -f mongdok_roomapi
docker run -p 8084:8084 --name mongdok_roomapi --rm --network roomapi-net mongdok_roomapi
docker ps