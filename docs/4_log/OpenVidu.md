# OpenVidu 서버 구축

## 1. 기본환경 설정

### JDK + Maven설치
```
sudo apt-get install -y openjdk-11-jdk
sudo apt-get install -y maven
```

## 2. OpenVidu Clone
```
mkdir openvidu-server
cd openvidu-server/
git clone https://github.com/OpenVidu/openvidu.git
```

## 3. Configuration
```
cd src/main/resources
vim application.properties
```

### application.properties 설정
* DOMAIN_OR_PUBLIC_IP=k4a401.p.ssafy.io
* OPENVIDU_SECRET=MY_SECRET
* KMS_URIS=["ws://k4a401.p.ssafy.io:8888/kurento"]

### application-container.properties 설정
* DOMAIN_OR_PUBLIC_IP=k4a401.a.ssafy.io
* OPENVIDU_SECRET=MY_SECRET
* HTTPS_PORT=4443

