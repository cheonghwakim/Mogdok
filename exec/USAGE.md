### 🎈 Usage

#### 💻 Front-end

- Project Setup

  - ```bash
    $ npm install 
    ```

- Compiles and hot-reloads for development

  - ```bash
    $ npm run serve
    ```

- Compiles and minifies for production

  - ```bash
    $ npm run build
    ```

- Run your tests

  - ```bash
    $ npm run test
    ```

- Lints and fixes files

  - ```bash
    $ npm run lint
    ```

- Customize configuration

  - [Configuration Reference](https://cli.vuejs.org/config/)

#### 💻 Back-end

**Install**

- Docker

  - redis

    - pull image

      ```bash
      $ docker pull redis
      ```

    - run container

      ```bash
      $ docker run --name mongdok-redis -p 6388:6379 --network redis-net redis --appendonly yes --requirepass {your password} &
      ```

  - mariadb 

    - pull image

      ```bash
      $ docker pull mariadb
      ```

    - run container

      ```bash
      $ docker run -d -p 3306:3306 -e MYSQL_ROOT_PASSWORD={your password} --name mariadb_mongsil mariadb
      ```

  - Desk server

    - maven clean, maven build

    - run container

      ``` bash
      $ docker run -d -p 2000:8081 --name mongdok_desk --restart=always mongdok_desk
      ```

  - Login server

    - maven clean, maven build

    - run container

      ``` bash
      $ docker run -d -p 8080:8080 --name mongdok-login --restart=always mongdok_login
      ```

  - Room server

    - gradle clean, gradle build

    - pull image & run container

      ``` bash
      $sudo docker build -t mongdok_roomapi /home/ubuntu/jenkins/deploy/.
      
      $docker run -p 8084:8084 --name mongdok_roomapi --rm --network roomapi-net mongdok_roomapi &
      ```

