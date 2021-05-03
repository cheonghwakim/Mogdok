<template>
  <div class="klogin">
    <div class="container d-flex justify-content-center align-items-center">
      <div class="card">
        <div class="row">
          <div class="col-md-6">
            <form class="form">
              <h2>회원가입</h2>

              <div class="inputbox mt-3">
                <span>access_token:  </span>
                <input
                  type="text"
                  placeholder="실명을 입력해주세요"
                  class="form-control"
                  v-model="form.access_token"
                  required
                />
              </div>
              <div class="inputbox mt-3">
                <span>refresh_token:  </span>
                <input
                  type="text"
                  placeholder="실명을 입력해주세요"
                  class="form-control"
                  v-model="form.refresh_token"
                  required
                />
              </div>
              <div class="inputbox mt-3">
                <span>이메일:  </span>
                <input
                  type="text"
                  v-text="form.email"
                  v-model="form.email"
                  class="form-control"
                  required
                />
              </div>
              <div class="inputbox mt-3">
                <span>id:  </span>
                <input
                  type="text"
                  v-text="form.id"
                  v-model="form.id"
                  class="form-control"
                  required
                />
              </div>
            </form>
          </div>
          <button @click="logout()">로그아웃</button>
          <button @click="auth()">토큰 정보보기</button>
          <button @click="refresh()">토큰 갱신하기</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  created() {
    this.create();
  },
  data() {
    return {
      codes: "",
      form: {
        access_token: "",
        refresh_token: "",
        email: "",
        id: "",
      },
    };
  },
  methods: {
    create() {
      console.log(this.$route.query);
      this.codes = this.$route.query.code;
      this.getToken();
    },
    login() {
      axios.post("https://localhost:2000/login", this.form).then((res) => {
        if (res.data != null) {
          document.cookie = `accessToken=${res.data}`;
          axios.defaults.headers.common["x-access-token"] = res.data;
          
          // console.log(document.cookie);
          // this.$router.push("/");
        }
      });
    },
    getToken() {
      console.log(this.codes);
      axios
        .get("https://localhost:2000/klogin?authorizeCode=" + this.codes)
        .then((res) => {
          console.log(res);
          this.form.email = res.data.email;
          this.form.access_token = res.data.access_token;
          this.form.id = res.data.id;
          this.form.refresh_token = res.data.refresh_token;
          // console.log(this.form.email);


          this.login();
        });
    },
    logout() {
      axios
        .get("https://localhost:2000/logout?accessToken=" + this.form.access_token)
        .then((res) => {
          console.log(res);
        });
    },
    auth() {
      axios
        .get("https://localhost:2000/auth?accessToken=" + this.form.access_token)
        .then((res) => {
          console.log(res);
        });
    },
    refresh() {
      axios
        .get("https://localhost:2000/refresh?refreshToken=" + this.form.refresh_token)
        .then((res) => {
          console.log(res);
        });
    },
    // onSubmit(event) {
    //   event.preventDefault();
    //   // alert(JSON.stringify(this.form));
    //   axios.post("http://localhost:8080/join", this.form).then((res) => {
    //     console.log(res.status);
    //     this.login();
    //   });
    // },

  },
};
</script>